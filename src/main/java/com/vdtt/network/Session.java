package com.vdtt.network;

import com.vdtt.map.MapManager;
import com.vdtt.map.world.planetblackstar.ZPlanetBlackStar;
import com.vdtt.model.User;
import com.vdtt.server.GameServer;
import com.vdtt.server.ServerManager;
import com.vdtt.server.SpamCheck;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import lombok.Getter;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class Session implements ISession {
    public String IPAddress;
    private Sender sender;
    public int id;
    public int ver;
    public Socket sc;
    public DataInputStream dis;
    public DataOutputStream dos;
    private IMessageHandler controller;
    @Getter
    private Service service;
    private Thread collectorThread;
    private Thread sendThread;
    public boolean connected;
    public boolean sendKeyComplete;
    private boolean isClosed;
    private boolean isSenData;
    private boolean isLoginSuccess;
    public boolean isLogin;
    public User user;
    public Object waitSendSession = new Object();
    private int messCount;
    public long lastCheck;


    public Session(Socket sc, int id) throws IOException {

        this.sc = sc;
        this.id = id;
        this.sc.setKeepAlive(true);
        connected = true;
        Log.info("Client IP " + this.IPAddress + " Version " + this.ver + " connected");
        this.dis = new DataInputStream(sc.getInputStream());
        this.dos = new DataOutputStream(sc.getOutputStream());
        setHandler(new Controller(this));
        setService(new Service(this));
        sendThread = new Thread(sender = new Sender());
        String remoteSocketAddress = sc.getRemoteSocketAddress().toString();
        sendThread.setName("sender: " + remoteSocketAddress);
        collectorThread = new Thread(new MessageCollector());
        collectorThread.setName("dis: " + remoteSocketAddress);
        collectorThread.start();
    }

    @Override
    public boolean isConnected() {
        return connected && sc != null && sc.isConnected();
    }

    @Override
    public void setHandler(IMessageHandler messageHandler) {
        this.controller = messageHandler;
    }

    public IMessageHandler getMessageHandler() {
        return this.controller;
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public void sendMessage(Message message) {
        if (isConnected()) {
            sender.addMessage(message);
        }
    }

    @Override
    public void close() {
        cleanNetwork();
    }

    private void cleanNetwork() {
        try {
            if (user != null) {
                if (user.sltChar != null) {
                    if (!user.sltChar.isCleaned) {
                        user.sltChar.cleanUp();
                    }
                }
                if (!user.isCleaned) {
                    user.cleanUp();
                }
            }
            connected = false;
            isLoginSuccess = false;
            if (sc != null) {
                sc.close();
                sc = null;
            }
            if (dos != null) {
                dos.close();
                dos = null;
            }
            if (dis != null) {
                dis.close();
                dis = null;
            }
            if (sendThread != null) {
                sendThread.interrupt();
                sendThread = null;
            }
            if (collectorThread != null) {
                collectorThread.interrupt();
                collectorThread = null;
            }
            controller = null;
            service = null;
            System.gc();
        } catch (Exception e) {
            Log.error("cleanNetwork err", e);
        }
    }

    private void processMessage(Message ms) {
        if (!isClosed && isConnected()) {
            controller.onMessage(ms);
        }
    }

    public void readKey(Message mss) {
        try {
            if (!sendKeyComplete) {
                byte firstByte = mss.reader().readByte();
                byte secondByte = mss.reader().readByte();
                short firstShort = mss.reader().readShort();
                short secondShort = mss.reader().readShort();
                byte thirdByte = mss.reader().readByte();
                byte fourthByte = mss.reader().readByte();
                byte fifthByte = mss.reader().readByte();
                sendKeyComplete = true;
                sendThread.start();
            }
        } catch (IOException e) {

        }
    }

    public void login(Message ms) {
        try {
            String username = ms.readUTF();
            String password = ms.readUTF();
            int ver = ms.reader().readInt();
            this.ver = ver;
            if (ver < 3) {
                service.serverDialog("Vui lòng sử dụng phiên bản mới nhất tại trieuhoirongthan.net!");
                return;
            }
            try {
                String key = ms.readUTF();
                if (!key.equals("j5Z3dgPbnROc3gmpdEVrWI4ost")) {
                    service.serverDialog("Vui lòng sử dụng phiên bản chính thức từ trang chủ!");
                    return;
                }
            } catch (IOException e) {
                service.serverDialog("Đã xảy ra lỗi, vui lòng thử lại hoặc tải bản mới nhất!");
                disconnect();
                return;
            }
            if (!connected || GameServer.isStop || !sendKeyComplete) {
                disconnect();
                return;
            }
            if (!isSenData) {
                isSenData = true;
                service.updateTextClient();
                service.updateData();
            }
            if (this.isLoginSuccess) {
                return;
            }
            if (isLogin) {
                return;
            }
            isLogin = true;
            User us = new User(this, username, password);
            us.login();
            if (us.isLoadFinish) {
                this.isLoginSuccess = true;
                this.isLogin = false;
                this.user = us;
                Controller controller = (Controller) getMessageHandler();
                if (controller == null) {
                    disconnect();
                    return;
                }
                controller.setUser(us);
                controller.setService(service);
                service.updateData();
                service.sendTabSelectChar(us);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean CMD_MOVE(byte cmd) {
        return cmd == 123 || cmd == 124 || cmd == 125 || cmd == -82 || cmd == -83 || cmd == -84;
    }

    private void doSendMessage(Message m) {
        try {
            byte cmd = m.getCommand();
            byte[] data = m.getData();
            switch (cmd) {
                case -92:
                case -91:
                case -87:
                case -81:
                case -65:
                case -64:
                case -63:
                case -41:
                case 20:
                case 61:
                case 123:
                case -84:
                    this.dos.writeByte(cmd);
                    this.dos.write(data);
                    break;
                default:
                    if (!m.inflate) {
                        this.dos.writeByte(cmd);
                        if (data != null && data.length > 0) {
                            this.dos.writeShort(data.length);
                            this.dos.write(data);
                        } else {
                            this.dos.writeShort(0);
                        }
                    } else {
                        data = Util.deflateByteArray(data);
                        this.dos.writeByte(-80);
                        this.dos.writeByte(cmd);
                        if (data != null && data.length > 0) {
                            this.dos.writeShort(data.length);
                            this.dos.write(data);
                        } else {
                            this.dos.writeShort(0);
                        }
                    }
            }
            dos.flush();
        } catch (Exception e) {
            closeMessage();
        }
    }


    public void disconnect() {
        try {
            if (sc != null) {
                sc.close();
            }
        } catch (Exception e) {
            Log.error("disconnect err", e);
        }
    }

    public void closeMessage() {
        try {
            if (isClosed) {
                return;
            }
            isClosed = true;
            try {
                ServerManager.remove(this.IPAddress);
            } catch (Exception e) {
                Log.error("remove ipv4 from list", e);
            }
            try {
                if (user != null) {
                    if (user.sltChar != null && user.sltChar.zone != null) {
                        if (user.sltChar.zone instanceof ZPlanetBlackStar zPlanetBlackStar) {
                            zPlanetBlackStar.dropBall(user.sltChar);
                        }
                    }
                    try {
                        user.saveData();
                    } catch (Exception e) {
                        Log.error("save user: " + user.username + " - err: " + e.getMessage(), e);
                    } finally {
                        ServerManager.removeUser(user);
                    }
                    if (user.sltChar != null && user.isLoginFinish) {
                        try {
                            user.sltChar.close();
                            user.sltChar.saveData();
                        } catch (Exception e) {
                            Log.error("save player: " + user.sltChar.name + " - err: " + e.getMessage(), e);
                        } finally {
                            user.sltChar.outZone();
                            ServerManager.removeChar(user.sltChar);
                        }

                    }
                }
            } finally {
                close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCountUseGiftCode() {
        String ip = this.IPAddress;
        if (!ServerManager.countUseGiftCodeByIp.containsKey(ip)) {
            ServerManager.countUseGiftCodeByIp.put(ip, 0);
        }
        return ServerManager.countUseGiftCodeByIp.get(this.IPAddress);
    }

    public void addUseGiftCode() {
        String ip = this.IPAddress;
        int countUseGiftCodeByIp = getCountUseGiftCode();
        ServerManager.countUseGiftCodeByIp.put(ip, countUseGiftCodeByIp + 1);
    }


    private class Sender implements Runnable {

        private final Vector<Message> sendingMessage;

        public Sender() {
            sendingMessage = new Vector<>();
        }

        public void addMessage(Message message) {
            sendingMessage.add(message);
        }

        @Override
        public void run() {
            while (isConnected()) {
                if (sendKeyComplete) {
                    while (sendingMessage != null && sendingMessage.size() > 0) {
                        try {
                            Message m = sendingMessage.get(0);
                            if (m != null) {
                                doSendMessage(m);
                            }
                            sendingMessage.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                            closeMessage();
                        }
                    }
                }
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    class MessageCollector implements Runnable {

        @Override
        public void run() {
            Message message;
            try {
                while (isConnected()) {
                    byte cmd = dis.readByte();
                    ++messCount;
                    if (messCount > 5000) {
                        if (System.currentTimeMillis() - lastCheck < 5000L) {
                            Util.log("User " + user.username + " send " + messCount + " message at time, suspicious spam. Last cmd: " + cmd + "ip:" + user.IPAddress, "spam.txt");
                            System.out.println("User " + user.username + " send " + messCount + " message at time, suspicious spam. Last cmd: " + cmd + "ip:" + user.IPAddress);
                            SpamCheck spam = ServerManager.ipSpamMsg.get(String.valueOf(user.IPAddress));
                            if (spam == null) {
                                spam = new SpamCheck();
                                ServerManager.ipSpamMsg.put(String.valueOf(user.IPAddress), new SpamCheck());
                            } else {
                                long timeBan = 60L * 1000L;
                                spam.timeLock = System.currentTimeMillis() + timeBan;
                            }
                            ++spam.countSpam;
                            disconnect();
                            break;
                        } else {
                            lastCheck = System.currentTimeMillis();
                            messCount = 0;
                        }
                    }
                    int byte1;
                    int byte2;
                    int byte3;
                    switch (cmd) {
                        case -82:
                            byte1 = dis.readByte();
                            int[] arrayXY = null;
                            try {
                                user.sltChar.x += byte1;
                                service.updateMove(true);
                            } catch (Exception e) {

                            }
                            continue;
                        case -83:
                            byte1 = dis.readByte();
                            try {
                                user.sltChar.y += byte1;
                                service.updateMove(true);
                            } catch (Exception e) {

                            }
                            continue;
                        case -84:
                            byte1 = dis.readUnsignedByte();
                            byte2 = dis.readUnsignedByte();
                            byte3 = dis.readUnsignedByte();
                            user.sltChar.updateXY(Util.getXYReal(byte1, byte2, byte3, MapManager.getInstance().getMapTemplates().get(user.sltChar.mapId).maxX));
                            service.updateMove(false);
                            continue;
                        case 123:
                            byte1 = dis.readUnsignedByte();
                            byte2 = dis.readUnsignedByte();
                            byte3 = dis.readUnsignedByte();
                            user.sltChar.updateXY(Util.getXYReal(byte1, byte2, byte3, MapManager.getInstance().getMapTemplates().get(user.sltChar.mapId).maxX));
                            service.updateMove(false);
                            continue;
                        case 124:
                            byte1 = dis.readByte();
                            user.sltChar.y += byte1;
                            service.updateMove(false);
                            continue;
                        case 125:
                            byte1 = dis.readByte();
                            user.sltChar.x += byte1;
                            service.updateMove(false);
                            continue;
                        case 61:
                            int indexSkill = dis.readByte();
                            user.sltChar.useSkill(indexSkill);
                            continue;
                        case -65:
                        case -60:
                            indexSkill = dis.readByte();
                            int idMob;
                            if (cmd == -65) {
                                idMob = dis.readByte();
                            } else {
                                idMob = dis.readShort();
                            }
                            user.sltChar.attackMob(indexSkill, idMob);
                            continue;
                        case -64:
                            dis.readByte();
                            dis.readByte();
                            continue;
                        case -87:
                            dis.readByte();
                            dis.readShort();
                            continue;
                        case -81:
                            dis.readByte();
                            dis.readInt();
                            continue;
                        case 20:
                            byte var = dis.readByte();
                            int var1 = dis.readInt();
                            user.sltChar.attackChar(var1, var);
                            continue;
                    }
                    message = readMessage(cmd);
                    if (message != null) {
                        try {
                            processMessage(message);
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
            closeMessage();
        }

        public Message readMessage(int cmd) {
            try {
                boolean isDeflate = false;
                int length = 0;
                switch (cmd) {
                    case -80:
                        cmd = dis.readByte();
                        length = (dis.readByte() & 255 >> 8) + (dis.readByte() & 255);
                        isDeflate = true;
                        break;
                    default:
                        length = dis.readUnsignedShort();
                        break;
                }
//                if (cmd == -80) {
//                    cmd = dis.readByte();
//                    length = (dis.readByte() & 255 >> 8) + (dis.readByte() & 255);
//                    isDeflate = true;
//                } else {
//                    length = dis.readUnsignedShort();
//                    isDeflate = false;
//                }
                byte[] data = new byte[length];
                int off = 0;

                while (length > 0) {
                    int len;
                    if (length - 2048 <= 0) {
                        len = length;
                    } else {
                        len = 2048;
                    }

                    int available = dis.available();
                    if (available == 0) {
                        Util.sleep(1l);
                    } else {
                        if (len > available) {
                            len = available;
                        }

                        dis.read(data, off, len);
                        off += len;
                        length -= len;
                    }
                }
                if (isDeflate) {
                    data = Util.inflateByteArray(data);
                }
                return new Message((byte) cmd, data);

            } catch (Exception e) {
//                e.printStackTrace();
                return null;
            }
        }
    }

}

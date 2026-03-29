package com.vdtt.model;

import com.vdtt.db.DbManager;
import com.vdtt.db.LoginDb;
import com.vdtt.network.Message;
import com.vdtt.network.Service;
import com.vdtt.network.Session;
import com.vdtt.server.Config;
import com.vdtt.server.ServerManager;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.File;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    public Session session;
    public Service service;
    public int id;
    public String username;
    public String password;
    public boolean isLoadFinish;
    public boolean isEntered;
    public boolean isCleaned;
    public ArrayList<String> IPAddress;
    public boolean isCreateChar;
    public boolean isAdmin;
    private boolean saving;
    public Char sltChar;
    private byte status;
    public byte activated;
    public Timestamp banUntil;
    public boolean isLoginFinish;
    public Vector<Char> chars = new Vector<>();
    public int coin;

    public User(Session client, String username, String password) {
        this.session = client;
        this.service = client.getService();
        this.username = username;
        this.password = password;
    }

    public HashMap<String, Object> getUserMap() {
        try (Connection conn = LoginDb.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT `status`,`coin`, `id`, `password`,`iscreatechar`,`iscreatechar_2`, `ip_address`, `online`, `ban_until`, `activated`,`activated_2`,`admin` FROM `users` WHERE `username` = ?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            ArrayList<HashMap<String, Object>> list;
            stmt.setString(1, this.username);
            ResultSet data = stmt.executeQuery();
            try {
                list = DbManager.getInstance().convertResultSetToList(data);
            } finally {
                data.close();
                stmt.close();
            }
            if (list.isEmpty()) {
                return null;
            }
            HashMap<String, Object> map = list.get(0);
            if (map != null) {
                String passwordHash = (String) map.get("password");
                if (!passwordHash.equals(password)) {
                    return null;
                }
            }
            return map;
        } catch (SQLException e) {
            Log.error("getUserMap() err", e);
        }
        return null;
    }

    private final static Object lock = new Object();

    public void login() {
        try {
            if (username.equals("-1") && password.equals("12345")) {
                service.serverDialog("Bạn hãy liên hệ admin để đăng kí");
                return;
            }
            Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
            Matcher m1 = p.matcher(username);
            if (!m1.find()) {
                service.serverDialog("Tên tài khoản có kí tự lạ.");
                return;
            }
            synchronized (lock) {
                HashMap<String, Object> map = getUserMap();
                if (Config.getInstance().getServerID() == 2) {
                    map = getUserMap();
                }
                if (map == null) {
                    service.serverDialog("Tài khoản hoặc mật khẩu không chính xác.");
                    return;
                }
                if (ServerManager.timeWaitLogin.containsKey(username)) {
                    if (System.currentTimeMillis() < ServerManager.timeWaitLogin.get(username)) {
                        service.serverDialog("Bạn chỉ có thể đăng nhập lại vào tài khoản sau " + (ServerManager.timeWaitLogin.get(username) - System.currentTimeMillis()) / 1000L + "s nữa");
                        return;
                    }
                    ServerManager.timeWaitLogin.remove(username);
                }
                this.id = (int) ((long) (map.get("id")));
                this.status = (byte) ((int) map.get("status"));
                int serverID = Config.getInstance().getServerID();
                String activatedKey = "activated";
                if (serverID != 1) {
                    activatedKey = "activated_" + serverID;
                }
                this.activated = (byte) ((int) map.get(activatedKey));
                this.isAdmin = ((int) map.get("admin")) == 1;
                this.coin = (int) map.get("coin");
//            if(activated == 0 || activated == 1){
//                service.serverDialog("Máy chủ open chinh thức vào 20h00");
//                return;
//            }
                if (!Config.getInstance().isEnableLogin() && !isAdmin) {
                    service.serverDialog(Files.readString(new File("login_notification.txt").toPath()));
                    return;
                }
                Object obj = map.get("ban_until");
                if (obj != null) {
                    this.banUntil = (Timestamp) obj;
                    long now = System.currentTimeMillis();
                    long timeRemaining = banUntil.getTime() - now;
                    if (timeRemaining > 0) {
                        service.serverDialog(String.format("Tài khoản bị khóa trong %s. Vui lòng liên hệ admin để biết thêm chi tiết.", Util.timeAgo((int) (timeRemaining / 1000))));
                        return;
                    }
                }
                this.isCreateChar = ((byte) (int) map.get("iscreatechar")) == 1;
                if (Config.getInstance().getServerID() == 2) {
                    this.isCreateChar = false;
                    this.isCreateChar = (int) (map.get("iscreatechar_2")) == 1;
                }

//            if (this.activated == 0) {
//                service.serverDialog("Tài khoản chưa được kích hoạt. Vui lòng truy cập nsoz.me để kích hoạt tài khoản.");
//                return;
//            } 
//            else if (this.status == 0) {
//                service.serverDialog("Tài khoản đã bị khoá. Vui lòng liên hệ admin để biết thêm chi tiết!");
//                return;
//            }

                this.IPAddress = new ArrayList<>();
                obj = map.get("ip_address");
                if (obj != null) {
                    String str = obj.toString();
                    if (!str.isEmpty()) {
                        JSONArray jArr = (JSONArray) JSONValue.parse(str);
                        for (Object o : jArr) {
                            IPAddress.add(o.toString());
                        }
                    }
                }
                if (!IPAddress.contains(session.IPAddress)) {
                    IPAddress.add(session.IPAddress);
                }
                synchronized (ServerManager.users) {
                    User u = ServerManager.findUserByUsername(this.username);
                    if (u != null && !u.isCleaned) {
                        service.serverDialog("Tài khoản đã có người đăng nhập.");
                        if (u.session != null && u.session.getService() != null) {
                            u.session.getService().serverDialog("Có người đăng nhập vào tài khoản của bạn.");
                        }
                        try {
                            if (!u.isCleaned) {
                                u.session.disconnect();
                            }
                        } catch (Exception e) {
                        } finally {
                            ServerManager.removeUser(u);
                        }
                        return;
                    }
                    ServerManager.addUser(this);
                }

                boolean isOnline = ((byte) map.get("online")) == 1;
                if (isOnline) {
                    service.serverDialog("Tài khoản đang có người đăng nhập (2)");
                    return;
                }

                this.isLoadFinish = true;
            }
        } catch (Exception ex) {
            Log.error("login err", ex);
        }
    }

    public void createCharacter(Message ms) {
        try (Connection conn = DbManager.getInstance().getConnection();) {
            ms.reader().readByte();
            byte idChar = ms.reader().readByte();
            String name = ms.readUTF();
            Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); // Cho phép ký tự chữ cái (viết thường hoặc viết hoa) và số
            Matcher m1 = p.matcher(name);
            if (!m1.find()) {
                service.serverDialog("Tên nhân vật không được chứa ký tự đặc biệt!");
                return;
            }
            if (name.length() < 6 || name.length() > 15) {
                service.serverDialog("Tên tài khoản chỉ cho phép từ 6 đến 15 ký tự!");
                return;
            }
//            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `players` WHERE `user_id` = ?;",
//                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
//            try {
//                stmt.setInt(1, this.id);
//                ResultSet check = stmt.executeQuery();
//                if (check.last()) {
//                    if (check.getRow() >= 1) {
//                        service.serverDialog("Bạn đã tạo tối đa số nhân vât!");
//                        return;
//                    }
//                }
//                check.close();
//            } finally {
//                stmt.close();
//            }
//
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `players` WHERE `name` = ? AND `players`.`server_id` = ?;", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            try {
                stmt.setString(1, name);
                stmt.setInt(2, Config.getInstance().getServerID());
                ResultSet check = stmt.executeQuery();
                if (check.last()) {
                    if (check.getRow() > 0) {
                        service.serverDialog("Tên nhân vật đã tồn tại!");
                        return;
                    }
                }
                check.close();
            } finally {
                stmt.close();
            }
            stmt = conn.prepareStatement(
                    "INSERT INTO players(`user_id`, `server_id`, `name`, `gender`,`class`, `skill`, `body`,`body2`, `bag`, `box`, `effect`, `friends`,`title`,`data`,`map`,`mail`,`rewards`) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            try {

                stmt.setInt(1, this.id);
                stmt.setInt(2, Config.getInstance().getServerID());
                stmt.setString(3, name);
                stmt.setByte(4, (byte) 0);
                stmt.setShort(5, idChar);
                stmt.setString(6, "[{\"level\":0,\"mpUsing\":0,\"timeCoolDown\":0,\"index\":0,\"rangeDoc\":0,\"coolDown\":0,\"maxTarget\":0,\"kiUpgrade\":0,\"isSkillTemplate\":50,\"strOption\":\"316,0\",\"id\":0,\"rangeNgang\":0,\"levelNeed\":0},{\"level\":0,\"mpUsing\":0,\"timeCoolDown\":0,\"index\":0,\"rangeDoc\":0,\"coolDown\":0,\"maxTarget\":0,\"kiUpgrade\":0,\"isSkillTemplate\":51,\"strOption\":\"315,0\",\"id\":101,\"rangeNgang\":0,\"levelNeed\":0},{\"level\":0,\"mpUsing\":0,\"timeCoolDown\":0,\"index\":0,\"rangeDoc\":0,\"coolDown\":0,\"maxTarget\":0,\"kiUpgrade\":0,\"isSkillTemplate\":52,\"strOption\":\"317,0\",\"id\":202,\"rangeNgang\":0,\"levelNeed\":0},{\"level\":0,\"mpUsing\":0,\"timeCoolDown\":0,\"index\":0,\"rangeDoc\":0,\"coolDown\":0,\"maxTarget\":0,\"kiUpgrade\":0,\"isSkillTemplate\":54,\"strOption\":\"319,0\",\"id\":404,\"rangeNgang\":0,\"levelNeed\":0},{\"level\":0,\"mpUsing\":0,\"timeCoolDown\":0,\"index\":0,\"rangeDoc\":0,\"coolDown\":0,\"maxTarget\":0,\"kiUpgrade\":0,\"isSkillTemplate\":53,\"strOption\":\"318,0\",\"id\":303,\"rangeNgang\":0,\"levelNeed\":0},{\"level\":1,\"mpUsing\":1,\"timeCoolDown\":0,\"index\":0,\"rangeDoc\":60,\"coolDown\":600,\"maxTarget\":1,\"kiUpgrade\":27,\"isSkillTemplate\":30,\"strOption\":\"314,3\",\"id\":506,\"rangeNgang\":60,\"levelNeed\":1}]");
                stmt.setString(7, "[]");
                stmt.setString(8, "[]");
                stmt.setString(9, "[]");
                stmt.setString(10, "[{\"isLock\":true,\"quantity\":1,\"upgrade\":0,\"expire\":-1,\"optionGhepCaiTrang\":\"\",\"index\":0,\"strOption\":\"\",\"id\":168,\"nameCreate\":\"\",\"sys\":0}]");
                stmt.setString(11, "[]");
                stmt.setString(12, "[]");
                stmt.setString(13, "[]");
                stmt.setString(14, "{\"pointUpgrageStarWeek\":0,\"pointKame\":0,\"pointEnthusiasticWeek\":0,\"pointUpgrageStar\":0,\"pointSuper\":0,\"pointEnthusiastic\":0,\"exp\":0,\"pointPhamChat\":0}");
                stmt.setString(15, "[86,500,471]");
                stmt.setString(16, "[]");
                stmt.setString(17, "[]");
                stmt.executeUpdate();
                isCreateChar = true;
            } finally {
                stmt.close();
            }
            saveData();
            initCharacterList();
            session.getService().sendTabSelectChar(this);
        } catch (Exception e) {
            Log.error("createCharacter err", e);
        }
    }

    public void initCharacterList() {
        try {
            Char pl = new Char();
            pl.user = this;
            pl.loadDisplay();
            chars.add(pl);
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveData() {
        try {
            if (isLoadFinish && !saving) {
                saving = true;
                try {
                    JSONArray list = new JSONArray();
                    for (String ip : IPAddress) {
                        list.add(ip);
                    }
                    String jList = list.toJSONString();
                    if (Config.getInstance().getServerID() == 2) {
                        try (Connection conn = DbManager.getInstance().getConnection();
                             PreparedStatement stmt = conn.prepareStatement(
                                     "UPDATE `users` SET `online` = ?, `ip_address` = ?, `iscreatechar_2` = ? WHERE `id` = ? LIMIT 1;")) {
                            stmt.setInt(1, 0);
                            stmt.setString(2, jList);
                            stmt.setInt(3, isCreateChar ? 1 : 0);
                            stmt.setInt(4, this.id);
                            stmt.executeUpdate();
                        }
                    } else {
                        try (Connection conn = DbManager.getInstance().getConnection();
                             PreparedStatement stmt = conn.prepareStatement(
                                     "UPDATE `users` SET `online` = ?, `ip_address` = ?, `iscreatechar` = ? WHERE `id` = ? LIMIT 1;")) {
                            stmt.setInt(1, 0);
                            stmt.setString(2, jList);
                            stmt.setInt(3, isCreateChar ? 1 : 0);
                            stmt.setInt(4, this.id);
                            stmt.executeUpdate();
                        }
                    }
                } finally {
                    saving = false;
                }
            }
        } catch (Exception e) {
            Log.error("save data user: " + username);
        }
    }

    public void cleanUp() {
        this.isCleaned = true;
        this.sltChar = null;
        this.session = null;
        this.service = null;
        Log.debug("clean user " + this.username);
    }

}

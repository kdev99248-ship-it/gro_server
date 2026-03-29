package com.vdtt.map.world.martialarts;

import com.vdtt.item.Item;
import com.vdtt.mail.Mail;
import com.vdtt.map.MapManager;
import com.vdtt.model.Char;
import com.vdtt.model.DanhHieu;
import com.vdtt.network.Message;
import com.vdtt.server.CaoThu;
import com.vdtt.server.GlobalService;
import com.vdtt.server.ServerManager;
import com.vdtt.util.Log;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public class MartialArtsConference {

    public static MartialArtsConference current;

    public List<Char> members;

    private Round currentRound;
    public int round;

    public MartialArtsConference() {
        GlobalService.getInstance().chat("Hệ thống", "c#redVõ đài vũ trụ đã bắt đầu hãy tới whis để tham gia", (byte) 1);
        current = this;
        members = new ArrayList<>();
        nextRound(-1, null);
    }

    public static boolean isRegistering() {
        return current == null || !(current.currentRound instanceof Qualifiers qualifiers) || qualifiers.isFighting();
    }

    public static void joinMap(Char player) {
        if (!MartialManager.isStart()) {
            player.serverDialog("Chưa tới giờ tham gia vui lòng quay lại sau");
            return;
        }
        if (findMember(player.name) == null) {
            player.serverDialog("Bạn không có trong danh sách báo danh vui lòng báo danh trước khi tham gia");
            return;
        }
        if (isRegistering()) {
            player.serverDialog("Đã hết thời gian tham gia đấu trường bạn đã tới trễ!");
            return;
        }
        if (current.round > 0) {
            player.serverDialog("Đã hết thời gian tham gia đấu trường bạn đã tới trễ!");
            return;
        }
        player.setXY(529, 473);
        player.achievements.increaseAchievementCount(4,1);
        current.currentRound.join(player);
    }

    public static void register(Char player) {
        if (MartialManager.getInstance().memberRegistered.contains(player.name)) {
            player.serverDialog("Bạn đã báo danh rồi!");
            return;
        }
        if (player.exp < 1000000) {
            player.serverDialog("Để tham gia phải có sức mạnh lớn hơn 1000000!");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour == 21 && minute <= 10) {
            player.serverDialog("Đã hết thời gian báo danh, vui lòng báo danh trước 21h vào đợt sau!");
            return;
        }
        
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY || dayOfWeek == Calendar.SATURDAY) {
            player.serverDialog("Chỉ mở vào 2 4 6");
            return;
        }
        if (player.user.activated == 1) {
            player.serverDialog("Tính năng cần mở thành viên ở bulma");
            return;
        }
        player.serverDialog("Báo danh thành công vui lòng chờ tới 21h10p để được vào đấu trường");
        MartialManager.getInstance().memberRegistered.add(player.name);
    }

    public static void cancelRegistration(Char player) {
        player.serverDialog("Hủy báo danh thành công");
        MartialManager.getInstance().memberRegistered.remove(player.name);
    }

    public static Char findMember(String namePl) {
        for (String name : MartialManager.getInstance().memberRegistered) {
            Char player = ServerManager.findCharByName(name);
            if (player != null && player.name.equals(namePl)) {
                return player;
            }
        }
        return null;
    }

    public void nextRound(int numberOfTeams, List<Char> members) {
        Log.debug("numberOfTeams " + numberOfTeams);
        switch (numberOfTeams) {
            case 16:
                currentRound = new RoundOf16(this);
                break;
            case 8:
                currentRound = new Quarterfinals(this);
                break;
            case 4:
                currentRound = new Semifinals(this);
                break;
            case 2:
                currentRound = new Finals(this);
                break;
            case 1:
                rewardWinner(members.get(0));
                if (currentRound != null) {
                    currentRound.doFinish();
                }
                break;
            default:
                currentRound = new Qualifiers(this);
                break;
        }
        if (numberOfTeams >= 2) {
            for (Char member : members) {
                currentRound.join(member);
                switch (numberOfTeams) {
                    case 16:
                        member.serverDialog("Bạn may mắn lọt vào vòng 16 đội vui lòng kiểm tra thư để nhận quà");
                        Item newItem = new Item(277, "vòng 16 đội");
                        newItem.setQuantity(5);
                        newItem.setLock(true);
                        Mail mail = member.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng vòng 16 đội", 0, 2000000, 0, newItem);
                        member.mailManager.addMail(mail);
                        break;
                    case 8:
                        member.serverDialog("Bạn may mắn lọt vào tứ kết vui lòng kiểm tra thư để nhận quà");
                        newItem = new Item(277, "vòng tứ kết");
                        newItem.setQuantity(10);
                        newItem.setLock(true);
                        mail = member.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng vòng tứ kết", 500000, 10000000, 10, newItem);
                        member.mailManager.addMail(mail);
                        break;
                    case 4:
                        member.serverDialog("Bạn may mắn lọt vào bán kết vui lòng kiểm tra thư để nhận quà");
                        newItem = new Item(277, "vòng bán kết");
                        newItem.setQuantity(10);
                        newItem.setLock(true);
                        mail = member.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng vòng bán kết", 1000000, 10000000, 20, newItem);
                        member.mailManager.addMail(mail);
                        break;
                    case 2:
                        member.serverDialog("Bạn may mắn lọt vào chung kết vui lòng kiểm tra thư để nhận quà");
                        newItem = new Item(428, "vòng chung kết");
                        newItem.setQuantity(10);
                        newItem.setLock(true);
                        mail = member.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng vòng chung kết", 2000000, 10000000, 50, newItem);
                        member.mailManager.addMail(mail);

                        newItem = new Item(161, "vòng chung kết");
                        newItem.setQuantity(10);
                        newItem.setLock(true);
                        mail = member.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng vòng chung kết", 0, 0, 0, newItem);
                        member.mailManager.addMail(mail);
                        break;
                }
            }
        }
    }

    public List<Char> getList() {
        return MartialArtsConference.current.members;
    }

    public void addMember(Char _char) {
        MartialArtsConference.current.members.add(_char);
    }

    public void removeMember(Char _char) {
        MartialArtsConference.current.members.remove(_char);
        Log.debug(String.format("remove %s playername: %s", this, _char.name));
    }

    public void rewardVeNhi(Char aChar) {
        Mail mail = aChar.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng hạng hai giải đấu sức mạnh", 5000000, 15000000, 100, null);
        aChar.mailManager.addMail(mail);

        Item newItem = new Item(161, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(20);
        newItem.setLock(true);
        mail = aChar.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng hạng hai giải đấu sức mạnh", 0, 0, 0, newItem);
        aChar.mailManager.addMail(mail);

        MapManager.getInstance().joinZone(aChar, 86, 0, 1);
        aChar.serverDialog("Bạn là người hạng hai hãy cố gắng lần sau chiến thắng nhén vui lòng kiểm tra thư để nhận quà");
    }

    private void rewardWinner(Char winner) {
        Item newItem = new Item(428, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(15);
        newItem.setLock(true);
        Mail mail = winner.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng chiến thắng võ đài vũ trụ", 10000000, 20000000, 200, newItem);
        winner.mailManager.addMail(mail);

        newItem = new Item(161, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(30);
        newItem.setLock(true);
        mail = winner.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng chiến thắng võ đài vũ trụ", 0, 0, 0, newItem);
        winner.mailManager.addMail(mail);

        DanhHieu dh = new DanhHieu();
        dh.title = "Chiến Binh Đệ Nhất";
        dh.setTime((int) TimeUnit.MILLISECONDS.toDays(2));
        dh.a();
        winner.danhHieu.add(dh);
        winner.getService().updateTitle();

        MapManager.getInstance().joinZone(winner, 86, 0, 1);
        winner.serverDialog("Bạn chiến thắng giải đấu vui lòng kiểm tra thư để nhận quà");
        GlobalService.getInstance().chat("Hệ thống", "c#red" + winner.name + "c#white đã là người chung cuộc chiến thắng giải đấu sức mạnh mọi người đều ngưỡng mộ", (byte) 1);
        close();
    }

    public void close() {
        round = 0;
        current = null;
        currentRound = null;
        members.clear();
        MartialManager.setStart(false);
    }

    
    private Vector<CaoThu> getTop() {
        Vector<CaoThu> caoThus = new Vector<>();
        List<Char> leaders = MartialArtsConference.current.getList().stream().filter(member -> member != null && member.zone instanceof ZMartialArts).sorted((o1, o2) -> o2.martialArtsPoint - o1.martialArtsPoint).toList();
        for (Char player : leaders) {
            if (player != null) {
                CaoThu ct = new CaoThu();
                ct.name = player.name;
                ct.num = player.exp;
                ct.point = (short) player.martialArtsPoint;
                if(player.clan != null) 
                    ct.clanName = player.clan.getName();
                caoThus.add(ct);
            }
        }
        
        return caoThus;
    }

    public void showTopMatch(Char pl) {
        try {
            int time = 0;
            if(MartialArtsConference.current != null && MartialArtsConference.current.currentRound != null)
                time = (int) (MartialArtsConference.current.currentRound.getTimeRemaining() / 1000);
            if(time < 0)
                time = 0;
            Vector<CaoThu> top = getTop();
            Message m = new Message((byte) -123);
            m.writer().writeByte(-16);
            m.writer().writeByte(1);
            m.writer().writeInt(time);
            m.writer().writeByte(top.size());
            for (int i = 0; i < top.size(); i++) {
                CaoThu caoThu = top.get(i);
                m.writeUTF(caoThu.name);
                m.writer().writeLong(caoThu.num);
                m.writer().writeShort(caoThu.point);
                m.writeUTF(caoThu.clanName == null ? "Chưa vào vũ trụ" : caoThu.clanName);
            }
            pl.getService().sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

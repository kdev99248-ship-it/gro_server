package com.vdtt.map.world.powerstation;

import com.vdtt.item.Item;
import com.vdtt.mail.Mail;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.martialarts.MartialArtsConference;
import com.vdtt.map.world.martialarts.ZMartialArts;
import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.party.Group;
import com.vdtt.server.CaoThu;
import com.vdtt.server.GlobalService;
import com.vdtt.util.Log;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.vdtt.map.world.powerstation.round.*;

@Getter
public class PowerStation {

    public ZPowerTournament zPowerTournament;

    public static PowerStation current;

    public List<Group> groups;

    private RoundPowerStation currentRound;
    public boolean start;

    public PowerStation() {
        GlobalService.getInstance().chat("Hệ thống", "c#redGiải đấu sức mạnh đã bắt đầu hãy tới whis để tham gia", (byte) 1);
        current = this;
        groups = new ArrayList<>();
        nextRound(-1, null);
    }

    public static boolean isRegistering() {
        return current == null || !(current.currentRound instanceof QualifiersPowerStation qualifiers) || qualifiers.isFighting();
    }

    public static void joinMap(Char player) {
        if (!PowerStationManager.isStart()) {
            player.serverDialog("Chưa tới giờ tham gia vui lòng quay lại sau");
            return;
        }
        if (player.getGroup() == null) {
            player.serverDialog("Bạn chưa tạo nhóm không thể tham gia");
            return;
        }
        if (findGroup(player.getGroup()) == null) {
            player.serverDialog("Nhóm của bạn không có trong danh sách báo danh vui lòng báo danh trước khi tham gia");
            return;
        }
        if (isRegistering()) {
            player.serverDialog("Đã hết thời gian tham gia đấu trường bạn đã tới trễ!");
            return;
        }
        if (current.start) {
            player.serverDialog("Đã hết thời gian tham gia đấu trường bạn đã tới trễ!");
            return;
        }
        player.achievements.increaseAchievementCount(5,1);
        joinMapAdmin(player);
    }

    public static void joinMapAdmin(Char player) {
        addGroup(player.getGroup());
        player.setXY(469, 408);
        current.currentRound.join(player);
    }

    public static void register(Char player) {
        if (PowerStationManager.getInstance().groupList.contains(player.getGroup())) {
            player.serverDialog("Nhóm của bạn đã báo danh rồi!");
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
        if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.FRIDAY) {
            player.serverDialog("Chỉ mở vào 3 5 7");
            return;
        }
        if (player.getGroup() == null) {
            player.serverDialog("Bạn chưa tạo nhóm không thể báo danh");
            return;
        }
        if (player.user.activated == 1) {
            player.serverDialog("Tính năng cần mở thành viên ở bulma");
            return;
        }
//        if (player.getGroup().getNumberMember() < 6) {
//            player.serverDialog("Không đủ 6 thành viên!");
//            return;
//        }
        if (player.getGroup().getIndexById(player.id) != 0) {
            player.serverDialog("Phải là trưởng nhóm mới có thể báo danh");
        } else {
            List<Char> chars = player.getGroup().getCharsInZone(player.zone.map.id, player.zone.id);
            if (chars.size() < player.getGroup().getNumberMember()) {
                player.serverDialog("Vui lòng tập hợp đủ thành viên trước khi báo danh");
                return;
            }
            Char old = null;
            for (Char _char : chars) {
                if (old != null && _char.clan != old.clan) {
                    player.serverDialog("Có thành viên trong nhóm không cùng vũ trụ");
                    return;
                }
                old = _char;
            }
        }
        player.serverDialog("Báo danh thành công vui lòng chờ tới 21h10p để được vào đấu trường");
        PowerStationManager.getInstance().groupList.add(player.getGroup());
    }

    public static void cancelRegistration(Char player) {
        if (player.getGroup() == null) {
            player.serverDialog("Bạn chưa tạo nhóm không thể hủy báo danh");
            return;
        }
        if (player.getGroup().getIndexById(player.id) != 0) {
            player.serverDialog("Phải là trưởng nhóm mới có thể hủy báo danh");
            return;
        }
        player.serverDialog("Hủy báo danh thành công");
        PowerStationManager.getInstance().groupList.add(player.getGroup());
    }

    public static Group findGroup(Group group) {
        for (Group group1 : PowerStationManager.getInstance().groupList) {
            if (group1 != null && group1.equals(group)) {
                return group1;
            }
        }
        return null;
    }

    public void nextRound(int numberOfTeams, List<Group> groups) {
        Log.debug("numberOfTeams " + numberOfTeams);
        try {
            if(groups != null)
                Log.debug("groups.size " + groups.size());
        } catch (Exception e) {
            Log.error(e);
        }
        
        if (numberOfTeams == 1) {
            if (current == null) return;
            currentRound.doFinish();
            for (Char player : groups.get(0).getChars()) {
                MapManager.getInstance().joinZone(player, 86, 0, 1);
                rewardWinner(player);
            }
            close();
            return;
        } else {
            currentRound = new QualifiersPowerStation(this);
        }
        if (groups != null) {
            StringBuilder name = new StringBuilder();
            for (Group group : groups) {
                for (Char player : group.getChars()) {
                    rewardVeNhi(player);
                    if (currentRound == null) return;
                    if(player.zone instanceof ZPowerTournament) {
                        currentRound.join(player);
                    }
                    if (group.isCheckLeader(player)) {
                        name.append(player.name);
                        name.append(", ");
                    }
                }
            }
            if (numberOfTeams > 1) {
                GlobalService.getInstance().chat("Giải đấu vũ trụ", "Các tổ đội may mắn lọt vào vòng sau trong giải đấu vũ trụ là: " + name, (byte) 1);
            }
        }
    }

    public List<Group> getList() {
        return current.groups;
    }

    public static void addGroup(Group group) {
        if (current.groups.contains(group)) return;
        current.groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }

    public void rewardVeNhi(Char aChar) {
        Item newItem = new Item(277, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(10);
        newItem.setLock(true);
        Mail mail = aChar.createMail("Hệ thống", "Thư của hệ thống", "Quà lọt vào vòng trong giải đấu sức mạnh", 0, 10000000, 50, newItem);
        aChar.mailManager.addMail(mail);

        newItem = new Item(176, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(50);
        newItem.setLock(true);
        mail = aChar.createMail("Hệ thống", "Thư của hệ thống", "Quà lọt vào vòng trong giải đấu sức mạnh", 0, 0, 0, newItem);
        aChar.mailManager.addMail(mail);
        aChar.serverDialog("Tổ đội của bạn đã lọt vào vòng trong vui lòng kiểm tra thư để nhận quà");
    }

    private void rewardWinner(Char winner) {
        Item newItem = new Item(277, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(5);
        newItem.setLock(true);
        Mail mail = winner.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng chiến thắng giải đấu sức mạnh", 0, 15000000, 100, newItem);
        winner.mailManager.addMail(mail);

        newItem = new Item(176, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(100);
        newItem.setLock(true);
        mail = winner.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng chiến thắng giải đấu sức mạnh", 0, 0, 0, newItem);
        winner.mailManager.addMail(mail);

        newItem = new Item(161, "chiến thắng giải đấu sức mạnh");
        newItem.setQuantity(10);
        newItem.setLock(true);
        mail = winner.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng chiến thắng giải đấu sức mạnh", 0, 0, 0, newItem);
        winner.mailManager.addMail(mail);

        winner.serverDialog("Tổ đội của bạn đã chiến thắng giải đấu vui lòng kiểm tra thư để nhận quà");
        GlobalService.getInstance().chat("Hệ thống", "c#red" + winner.name + "c#white đã là đội chung cuộc chiến thắng giải đấu sức mạnh mọi người đều ngưỡng mộ", (byte) 1);
        close();
    }

    public void close() {
        start = false;
        current = null;
        currentRound = null;
        groups.clear();
    }

    private Vector<CaoThu> getTop() {
        Vector<CaoThu> caoThus = new Vector<>();
        List<Group> grs= PowerStation.current.getGroups().stream().sorted((group, group1) -> group1.powerStationPoint - group.powerStationPoint).toList();
        for (Group group : grs) {
            if(!group.getChars().isEmpty()) {
                Char p = group.getChars().get(0);
                CaoThu ct = new CaoThu();
                ct.name = p.name;
                ct.num = p.exp;
                ct.point = (short) group.powerStationPoint;
                if(p.clan != null)
                    ct.clanName = p.clan.getName();
                caoThus.add(ct);
            }
        }
        return caoThus;
    }

    public void showTopMatch(Char pl) {
        try {
            int time = 0;
            if(PowerStation.current != null && PowerStation.current.currentRound != null)
                time = (int) (PowerStation.current.currentRound.getTimeRemaining() / 1000);
            if(time < 0)
                time = 0;
            Vector<CaoThu> tops = getTop();
            Message m = new Message((byte) -123);
            m.writer().writeByte(-16);
            m.writer().writeByte(1);
            m.writer().writeInt(time);
            m.writer().writeByte(tops.size());
            for (CaoThu caoThu : tops) {
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

package com.vdtt.map.world;

import com.vdtt.effect.Effect;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.ZTimeRoom;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class TimeRoom extends World {

    public int level;
    public byte type;
    private static final List<TimeRoom> roomList = new CopyOnWriteArrayList<>();

    public static void addZoom(TimeRoom zoom) {
        roomList.add(zoom);
    }

    public static void removeZoom(TimeRoom zoom) {
        roomList.remove(zoom);
    }

    public static TimeRoom getZoom(int level, byte type) {
        for (TimeRoom zoom : roomList) {
            if (!zoom.isClosed && zoom.level == level && zoom.type == type && zoom.zones.get(0).getChars().size() < 3) {
                return zoom;
            }
        }
        return null;
    }

    public synchronized static void join(Char p) {
        if (p.taskId == 9 && p.taskMain != null && p.taskMain.index == 0) {
            p.updateTaskCount(1);
            p.timeZoom = System.currentTimeMillis() + 120000;
            TimeRoom timeRoom = new TimeRoom(p.level(), (byte) 0);
            p.addWorld(timeRoom);
            timeRoom.joinZone(p);
            TimeRoom.addZoom(timeRoom);
            Effect effect = new Effect(116, 120000, 0);
            p.getEm().setEffect(effect);
            return;
        }
        if (p.timeZoom < System.currentTimeMillis()) {
            if (p.zeni < 110) {
                p.getService().warningMessage("Không đủ zeni");
                return;
            }
            if (p.countTimeRoom < 1) {
                p.getService().serverDialog("Hết lượt tham gia");
                return;
            }
            p.achievements.increaseAchievementCount(3, 1);
            p.addZeni(-110, true);
            p.countTimeRoom--;
            p.timeZoom = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(5);

            Effect effect = new Effect(116, TimeUnit.HOURS.toMillis(5), 0);
            p.getEm().setEffect(effect);
        }

        TimeRoom timeRoom = TimeRoom.getZoom(p.level(), (byte) 0);
        if (timeRoom == null) {
            timeRoom = new TimeRoom(p.level(), (byte) 0);
            p.addWorld(timeRoom);
            TimeRoom.addZoom(timeRoom);
        }
        timeRoom.joinZone(p);
    }


    public TimeRoom(int level, byte type) {
        setType(World.TIME_ROOM);
        this.name = "TimeRoom";
        generateId();
        setMaxTime((int) TimeUnit.DAYS.toMillis(1));
        this.level = level;
        this.type = type;
        Map map = MapManager.getInstance().find(84);
        ZTimeRoom zone = new ZTimeRoom(this.id, map.mapTemplate, map, level);
        zone.setWorld(this);
        zones.add(zone);
        this.initFinished = true;

    }

    public void joinZone(Char _char) {
        zones.get(0).join(_char, -1);
    }

    @Override
    public void doFinish() {

    }

    @Override
    public void doEnd() {
        List<Char> members = getMembers();
        for (Char _char : members) {
            try {
                if (_char.isCleaned) {
                    continue;
                }
                _char.addClanPoint(5);
                _char.addPointSoiNoi(50);
                _char.joinZone(56, 0, -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !(pre instanceof ZTimeRoom) && next instanceof ZTimeRoom;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre instanceof ZTimeRoom && !(next instanceof ZTimeRoom);
    }
}

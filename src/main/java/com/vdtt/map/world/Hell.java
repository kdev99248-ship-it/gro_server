package com.vdtt.map.world;

import com.vdtt.effect.Effect;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.HellArena;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Hell extends World {

    public Hell(int level) {
        setType(World.DUNGEON);
        this.name = "Dungeon";
        generateId();
        setMaxTime((int) TimeUnit.MINUTES.toMillis(35));
        Map map = MapManager.getInstance().find(19);
        HellArena z = new HellArena(this.id, map.mapTemplate, map, getLevel(level));
        z.worldMaxTime = maxTime;
        z.worldCreatedAt = createdAt;
        z.timeInMap = false;
        z.setWorld(this);
        addZone(z);
        this.initFinished = true;
    }
    public void addPointPB(int point) {
        List<Char> members = getMembers();
        for (Char _char : members) {
            try {
                if(!_char.isCleaned)
                _char.updatePointPB(point);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void addCountTaskFide() {
        List<Char> members = getMembers();
        for (Char _char : members) {
            try {
                if(!_char.isCleaned){
                    if(_char.getEm().findByID(162)!=null){
                        Effect effect = _char.getEm().findByID(162);
                        effect.param++;
                        if(effect.param >= 8){
                            effect.param = 8;
                        }
                        _char.zone.getService().playerAddEffect(_char, effect);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public int getLevel(int level) {
        return level == 1 ? 17 : level == 2 ? 27 : level == 3 ? 37 : level == 4 ? 47 : level == 5 ? 57 : 67;
    }

    public void joinZone(Char _char) {
        if(_char.taskId==14&&_char.taskMain != null&&_char.taskMain.index == 0){
            _char.updateTaskCount(101);
        }
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
                _char.serverMessage("Cửa đi ngục đã được khép lại.");
                _char.removeWorld(World.DUNGEON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !pre.map.isHell() && next.map.isHell();
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre.map.isHell() && !next.map.isHell();
    }
}

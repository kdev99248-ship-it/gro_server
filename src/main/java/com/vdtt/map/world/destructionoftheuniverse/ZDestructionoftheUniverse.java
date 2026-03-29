package com.vdtt.map.world.destructionoftheuniverse;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.World;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZDestructionoftheUniverse extends World {
    public int level;

    public ZDestructionoftheUniverse(int level) {
        setType(World.DESTRUCTION_UNIVERSE);
        this.name = "DESTRUCTION_UNIVERSE";
        this.level = level;
        generateId();
        setMaxTime((int) TimeUnit.MINUTES.toMillis(45));
        prepare();
        this.initFinished = true;
    }

    private void prepare() {
        createArea(9);
        createArea(10);
        createArea(11);
        createArea(12);
        createArea(13);
        createArea(14);
        createArea(15);
    }

    private void createArea(int mapId) {
        Map map = MapManager.getInstance().find(mapId);
        DestructionoftheUniverseArea area = new DestructionoftheUniverseArea(this.id, map.mapTemplate, map);
        area.setWorld(this);
        area.setData();
        area.worldMaxTime = maxTime;
        area.worldCreatedAt = createdAt;
        area.timeInMap = false;
        addZone(area);
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !(pre instanceof DestructionoftheUniverseArea) && next instanceof DestructionoftheUniverseArea;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre instanceof DestructionoftheUniverseArea && !(next instanceof DestructionoftheUniverseArea);
    }

    @Override
    public void doFinish() {
        List<Char> members = getMembers();
        for (Char c : members) {
            try {
                if (c.isCleaned) {
                    continue;
                }
                reward(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        close();
    }

    private void reward(Char p) {
//        Item item = new Item(1058,"boss");
//        item.isLock = true;
//        int optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
//        item.addOption(optionId, optionId == 469 ? 12 : 1);
//        if (p.addItemToBag(item)) {
//            p.getService().addItem(item);
//        }
//
//        item = new Item(1063,"boss");
//        item.isLock = true;
//        optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
//        item.addOption(optionId, optionId == 469 ? 12 : 1);
//        if (p.addItemToBag(item)) {
//            p.getService().addItem(item);
//        }
//
//        item = new Item(1068,"boss");
//        item.isLock = true;
//        optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
//        item.addOption(optionId, optionId == 469 ? 12 : 1);
//        if (p.addItemToBag(item)) {
//            p.getService().addItem(item);
//        }
//
//        item = new Item(1073,"boss");
//        item.isLock = true;
//        optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
//        item.addOption(optionId, optionId == 469 ? 12 : 1);
//        if (p.addItemToBag(item)) {
//            p.getService().addItem(item);
//        }
//        for (int i = 0; i < 25; i++) {
//            item = new Item(RandomItem.ITEM_NGUC_TU.next(),"boss");
//            item.isLock = true;
//            optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
//            item.addOption(optionId, optionId == 469 ? 12 : 1);
//            if (p.addItemToBag(item)) {
//                p.getService().addItem(item);
//            }
//        }
//
//        item = new Item(6,"boss");
//        item.isLock = true;
//        if (p.addItemToBag(item)) {
//            p.getService().addItem(item);
//        }
    }

    @Override
    public void doEnd() {
        List<Char> members = getMembers();
        for (Char c : members) {
            try {
                if (c.isCleaned) {
                    continue;
                }
                c.addClanPoint(50);
                c.addPointSoiNoi(150);
                c.setXY(1095, 389);
                c.joinZone(86, 0, -1);
                c.serverMessage("Nhiệm vụ hủy diệt vũ trụ kết thúc.");
                c.clan.removeWorld(World.DESTRUCTION_UNIVERSE);
                members.get(0).clan.countNvHd--;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void joinZone(Char _char) {
        _char.setXY(90, 336);
        zones.get(0).join(_char, -1);
    }
}

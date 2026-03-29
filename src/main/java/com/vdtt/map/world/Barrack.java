package com.vdtt.map.world;

import com.vdtt.handler.UseItemHandler;
import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.BarrackArea;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Barrack extends World {
    @Getter
    private int level;

    public Barrack(int level) {
        setType(World.BARRACK);
        this.name = "Barrack";
        this.level = level;
        generateId();
        setMaxTime((int) TimeUnit.MINUTES.toMillis(45));
        prepare();
        this.initFinished = true;
    }

    private void prepare() {
        createArea(106);
        createArea(107);
        createArea(108);
        createArea(109);
        createArea(110);
        createArea(111);
    }

    private void createArea(int mapId) {
        Map map = MapManager.getInstance().find(mapId);
        BarrackArea area = new BarrackArea(this.id, map.mapTemplate, map);
        area.setWorld(this);
        area.setData();
        area.worldMaxTime = maxTime;
        area.worldCreatedAt = createdAt;
        area.timeInMap = false;
        addZone(area);
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !(pre instanceof BarrackArea) && next instanceof BarrackArea;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre instanceof BarrackArea && !(next instanceof BarrackArea);
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
    }

    private void reward(Char p) {
        Item item = new Item(1058,"boss");
        item.isLock = true;
        int optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
        item.addOption(optionId, optionId == 469 ? 12 : 1);
        if (p.addItemToBag(item)) {
            p.getService().addItem(item);
        }

        item = new Item(1063,"boss");
        item.isLock = true;
        optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
        item.addOption(optionId, optionId == 469 ? 12 : 1);
        if (p.addItemToBag(item)) {
            p.getService().addItem(item);
        }

        item = new Item(1068,"boss");
        item.isLock = true;
        optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
        item.addOption(optionId, optionId == 469 ? 12 : 1);
        if (p.addItemToBag(item)) {
            p.getService().addItem(item);
        }

        item = new Item(1073,"boss");
        item.isLock = true;
        optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
        item.addOption(optionId, optionId == 469 ? 12 : 1);
        if (p.addItemToBag(item)) {
            p.getService().addItem(item);
        }

        item = new Item(6,"boss");
        item.isLock = true;
        if (p.addItemToBag(item)) {
            p.getService().addItem(item);
        }
    }

    @Override
    public void doEnd() {
        List<Char> members = getMembers();
        for (Char c : members) {
            try {
                if (c.isCleaned) {
                    continue;
                }
                c.addClanPoint(15);
                c.addPointSoiNoi(150);
                c.setXY(1095, 389);
                c.joinZone(75, 0, -1);
                c.serverMessage("Doanh trại đã khép lại.");
                c.removeWorld(World.DUNGEON);
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

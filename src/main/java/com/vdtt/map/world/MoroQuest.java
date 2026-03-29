package com.vdtt.map.world;

import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.PrimaryForest;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import com.vdtt.util.RandomCollection;
import com.vdtt.util.Util;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MoroQuest extends World {

    public MoroQuest() {
        setType(World.MORO_QUEST);
        this.name = "Moro Quest";
        generateId();
        setMaxTime((int) TimeUnit.MINUTES.toMillis(30));
        prepare();
        this.initFinished = true;
    }

    private void prepare() {
        createArea(88);
    }

    private void createArea(int mapId) {
        Map map = MapManager.getInstance().find(mapId);
        PrimaryForest area = new PrimaryForest(this.id, map.mapTemplate, map);
        area.setWorld(this);
        area.worldMaxTime = maxTime;
        area.worldCreatedAt = createdAt;
        area.timeInMap = false;
        addZone(area);
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !(pre instanceof PrimaryForest) && next instanceof PrimaryForest;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre instanceof PrimaryForest && !(next instanceof PrimaryForest);
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
        p.addClanPoint(10);
        RandomCollection<Integer> ITEM_DROP = new RandomCollection<>();
        ITEM_DROP.add(100, 562);
        ITEM_DROP.add(100, 564);
        ITEM_DROP.add(100, 566);
        ITEM_DROP.add(100, 354);
        ITEM_DROP.add(100, 277);
        ITEM_DROP.add(100, 5);
        ITEM_DROP.add(100, 697);
        ITEM_DROP.add(100, 704);
        ITEM_DROP.add(100, 705);
        ITEM_DROP.add(100, 706);
        ITEM_DROP.add(10, 555);
        int itemId = ITEM_DROP.next();
        Item newItem = new Item(itemId, "moro");
        newItem.setQuantity(Util.nextInt(1, 3));
        if (newItem.getId() == 555) {
            newItem.addOption(327, 88);
            newItem.addOptionRandom(379, 5, 12);
            newItem.addOptionRandom(2, 50, 160);
            newItem.addOptionRandom(0, 500, 1300);
        }
        newItem.expire = -1;
        newItem.isLock = false;
        if (p.addItemToBag(newItem)) {
            p.getService().addItem(newItem);
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
                c.serverMessage("Phó bản đã khép lại.");
                c.removeWorld(World.MORO_QUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void joinZone(Char _char) {
        _char.setXY(90, 416);
        zones.get(0).join(_char, -1);
    }
}

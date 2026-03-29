package com.vdtt.map.zones;

import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.item.ItemMap;
import com.vdtt.map.item.ItemMapFactory;
import com.vdtt.mob.Mob;
import com.vdtt.model.Char;
import com.vdtt.model.RandomItem;
import com.vdtt.server.GlobalService;
import com.vdtt.util.Util;

import java.util.HashMap;

public class NgucTu extends Zone {

    private java.util.Map<Integer,Integer[]> limitPoints = new HashMap<>();

    public NgucTu(int id, MapTemplate tilemap, Map map) {
        super(id, tilemap, map);
    }

    @Override
    public void join(Char p, int typeTau) {
        super.join(p, typeTau);
        p.setTypePk((byte) 2);
        p.getService().sendTimeNgucTu();
    }

    @Override
    public void createMonster() {
        if (map.id == 8) {
            for (Mob mob : map.mapTemplate.mobList) {
                if (mob != null) {
                    Mob mob2 = mob.clone();
                    mob2.idEntity = monsters.size();
                    if (mob2.id == 93) {
                        mob2.hp = mob2.hpFull = 10000;
                        mob2.kiNow = mob2.kiMax = 42;
                        mob2.dameMax = 198;
                    } else if (mob2.id >= 80 && mob2.id <= 85) {
                        mob2.levelBoss = 6;
                        mob2.hp = mob2.hpFull = 120 * 10000 + (10000 * (mob2.id - 80));
                        mob2.kiNow = mob2.kiMax = 110 * (100 + mob2.id - 80);
                        mob2.dameMax = 24 * (22 + mob2.id - 80);
                    }
                    mob2.zone = this;
                    monsters.add(mob2);
                }
            }
        } else if (map.id == 4) {
            for (Mob mob : map.mapTemplate.mobList) {
                if (mob != null) {
                    Mob mob2 = mob.clone();
                    mob2.idEntity = monsters.size();
                    if (mob2.id == 93) {
                        mob2.kiNow = mob.kiMax = 82;
                        mob2.dameMax = 300;
                    } else if (mob2.id >= 80 && mob2.id <= 85) {
                        mob2.kiNow = mob2.kiMax = 210 * (100 + mob2.id - 80);
                        mob2.dameMax = 44 * (22 + mob2.id - 80);
                    }
                    mob2.zone = this;
                    monsters.add(mob2);
                }
            }
        } else if (map.id == 2) {
            for (Mob mob : map.mapTemplate.mobList) {
                if (mob != null) {
                    Mob mob2 = mob.clone();
                    mob2.idEntity = monsters.size();
                    if (mob2.id == 93) {
                        mob2.hp = mob2.hpFull = 52000;
                        mob2.kiNow = mob.kiMax = 160;
                        mob2.dameMax = 600;
                    } else if (mob2.id >= 80 && mob2.id <= 85) {
                        mob2.levelBoss = 6;
                        mob2.hp = mob2.hpFull = 320 * 20000 + (10000 * (mob2.id - 80));
                        mob2.kiNow = mob2.kiMax = 410 * (100 + mob2.id - 80);
                        mob2.dameMax = 84 * (22 + mob2.id - 80);
                    }
                    mob2.zone = this;
                    monsters.add(mob2);
                }
            }
        } else if (map.id == 6) {
            for (Mob mob : map.mapTemplate.mobList) {
                if (mob != null) {
                    Mob mob2 = mob.clone();
                    mob2.idEntity = monsters.size();
                    if (mob2.id == 93) {
                        mob2.hp = mob2.hpFull = 82000;
                        mob2.kiNow = mob.kiMax = 260;
                        mob2.dameMax = 800;
                    } else if (mob2.id >= 80 && mob2.id <= 85) {
                        mob2.levelBoss = 6;
                        mob2.hp = mob2.hpFull = 420 * 20000 + (10000 * (mob2.id - 80));
                        mob2.kiNow = mob2.kiMax = 710 * (100 + mob2.id - 80);
                        mob2.dameMax = 124 * (22 + mob2.id - 80);
                    }
                    mob2.zone = this;
                    monsters.add(mob2);
                }
            }
        }
    }

    @Override
    public void mobDead(Mob mob, Char killer) {
        if (mob.id >= 80 && mob.id <= 85) {
            killer.addClanPoint(2);
            for (int i = 0; i < 20; i++) {
                Item item = new Item(RandomItem.ITEM_NGUC_TU.next(),"quai");
                item.setQuantity(1);
                item.isLock = false;
                ItemMap itemMap = ItemMapFactory.getInstance()
                        .builder()
                        .id(numberDropItem++)
                        .x((short) (mob.x + Util.nextInt(-30, 50)))
                        .y((short) (mob.y))
                        .build();
                itemMap.setOwnerID(killer.id);
                if (item != null) {
                    itemMap.setItem(item);
                    addItemMap(itemMap);
                    getService().addItemMap(itemMap, killer);
                }
            }
            killer.addPointSoiNoi(20);
            killer.addBallz(Util.nextInt(10000, 50000), true);
            GlobalService.getInstance().chat("Hệ thống", "Boss " + mob.getTemplate().name + " đã bị c#item" + killer.name + "c#white tiêu diệt tại " + map.mapTemplate.name + " khu " + id, (byte) 0);
            mob.recoveryTimeCount = 3600;
        }
    }

    public Integer[] getLimitPoint(int id){
      return limitPoints.computeIfAbsent(id, k -> new Integer[]{0, 0});
    }
}

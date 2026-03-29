package com.vdtt.map.zones;

import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.item.ItemMap;
import com.vdtt.map.item.ItemMapFactory;
import com.vdtt.map.world.Hell;
import com.vdtt.mob.Mob;
import com.vdtt.model.Char;
import com.vdtt.model.RandomItem;
import com.vdtt.util.Util;

public class HellArena extends ZWorld {
    public int level;
    public byte turn;

    public HellArena(int id, MapTemplate tilemap, Map map, int level) {
        super(id, tilemap, map);
        this.level = level;
    }

    @Override
    public void join(Char p, int typeTau) {
        super.join(p, -1);
    }

    @Override
    public void createMonster() {
        for (Mob mob : map.mapTemplate.mobList) {
            Mob m = mob.clone();
            m.idEntity = monsters.size();
            m.zone = this;
            m.hp = m.hpFull = 0;
            m.isDead = true;
            m.status = 4;
            m.level = level;
            m.attackDelay = 10000;
            monsters.add(m);
        }
    }

    public Mob getMonster(int id) {
        for (Mob mob : monsters) {
            if (mob.id == id) {
                return mob;
            }
        }
        return null;
    }

    @Override
    public void update() {
        super.update();
        refresh();
    }

    public void refresh() {
        if (getLivingMonsters().isEmpty()) {
            if (turn == 0) {
                turn = 1;
                for (int i = 0; i < 4; i++) {
                    Mob mob = getMonster(66 + i);
                    if (mob != null) {
                        mob.isDead = false;
                        mob.hp = mob.hpFull = level * 15000 + (i * 10000);
                        mob.kiNow = level * 42 + (i * 20);
                        mob.dameMax = level * 10;
                        mob.status = 2;
                        getService().recoveryMonster(mob);
                    }
                }
            } else if (turn == 1) {
                turn = 2;
                for (int i = 0; i < 3; i++) {
                    Mob mob = getMonster(75 + i);
                    if (mob != null) {
                        mob.isDead = false;
                        mob.hp = mob.hpFull = level * 15000 * 6 + (i * 10000 * turn);
                        mob.kiNow = level * 42 * 6 + (i * 20 * turn);
                        mob.dameMax = level * 10 * turn;
                        mob.status = 2;
                        getService().recoveryMonster(mob);
                        world.getService().serverMessage(mob.getTemplate().name + " đã xuất hiện");
                    }
                }
            } else if (turn == 2) {
                turn = 3;
                for (Mob mob : monsters) {
                    if (mob != null && mob.id == 70) {
                        mob.isDead = false;
                        mob.hp = mob.hpFull = level * 10000;
                        mob.kiNow = level * 24;
                        mob.dameMax = level * 10;
                        mob.status = 2;
                        getService().recoveryMonster(mob);
                    }
                }
            } else if (turn == 3) {
                turn = 4;
                Mob mob = getMonster(78);
                if (mob != null) {
                    mob.isDead = false;
                    mob.hp = mob.hpFull = level * 15000 * 6 * 2;
                    mob.kiNow = level * 42 * 6 * 2;
                    mob.dameMax = level * 10 * 2;
                    mob.status = 2;
                    getService().recoveryMonster(mob);
                    world.getService().serverMessage(mob.getTemplate().name + " đã xuất hiện");
                }
            } else if (turn == 4) {
                turn = 5;
                for (Mob mob : monsters) {
                    if (mob != null && mob.id == 72) {
                        mob.isDead = false;
                        mob.hp = mob.hpFull = level * 10000 * 2;
                        mob.kiNow = level * 24 * 2;
                        mob.dameMax = level * 10 * 2;
                        mob.status = 2;
                        getService().recoveryMonster(mob);
                    }
                }
            } else if (turn == 5) {
                turn = 6;
                Mob mob = getMonster(79);
                if (mob != null) {
                    mob.isDead = false;
                    mob.hp = mob.hpFull = level * 15000 * 6 * 4;
                    mob.kiNow = level * 42 * 6 * 4;
                    mob.dameMax = level * 10 * 4;
                    mob.status = 2;
                    getService().recoveryMonster(mob);
                    world.getService().serverMessage(mob.getTemplate().name + " đã xuất hiện");
                }
            } else if (turn == 6) {
                turn = 7;
                for (Mob mob : monsters) {
                    if (mob != null && mob.id == 64) {
                        mob.isDead = false;
                        mob.hp = mob.hpFull = level * 10000 * 4;
                        mob.kiNow = level * 24 * 4;
                        mob.dameMax = level * 10 * 4;
                        mob.status = 2;
                        getService().recoveryMonster(mob);
                    }
                }
            } else if (turn == 7) {
                turn = 8;
                Mob mob = getMonster(65);
                if (mob != null) {
                    mob.isDead = false;
                    mob.hp = mob.hpFull = level * 15000 * 6 * 6;
                    mob.kiNow = level * 42 * 6 * 6;
                    mob.dameMax = level * 10 * 6;
                    mob.status = 2;
                    getService().recoveryMonster(mob);
                    world.getService().serverMessage(mob.getTemplate().name + " đã xuất hiện");
                }
            } else if (turn == 8) {
                turn = 9;
                world.getService().serverMessage("Chúc mừng các chiến binh đã hoàn thành địa ngục, địa ngục sẽ tự động đóng cửa sau 10 giây");
                world.setRemaining(60_000L);
            }
        }
    }

    @Override
    public void mobDead(Mob mob, Char killer) {
        Hell hell = (Hell) world;
        if (mob.id == 66 || mob.id == 67 || mob.id == 68 || mob.id == 69) {
            killer.updatePointPB(10 + (mob.id - 66));
            hell.addPointPB(30);
            Item item = new Item(838,"hell");
            item.setQuantity(1);
            item.setLock(true);
            ItemMap itemMap = ItemMapFactory.getInstance()
                    .builder()
                    .id(numberDropItem++)
                    .x((short) (mob.x + Util.nextInt(-30, 50)))
                    .y((short) (mob.y))
                    .build();
            itemMap.setOwnerID(killer.id);
            itemMap.setItem(item);
            addItemMap(itemMap);
            getService().addItemMap(itemMap, killer);
        } else if (mob.id == 75 || mob.id == 76 || mob.id == 77 || mob.id == 78 || mob.id == 79) {
            killer.updatePointPB(20 + (mob.id - 75));
            killer.addPointSoiNoi(20);
            killer.addPointKame(2);
            hell.addPointPB(80);
            killer.addClanPoint(2);
            hell.addCountTaskFide();
            world.getService().chat("Hệ thống", "Boss " + mob.getTemplate().name + " đã bị chiến binh " + killer.name + " tiêu diệt tại " + template.name + " khu " + id, (byte) 1);
            for (int i = 0; i < 8; i++) {
                Item item = new Item(RandomItem.BOSS_HELL.next(),"boss");
                item.setQuantity(1);
                if (item.id == 838)
                    item.setLock(true);
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
        } else if (mob.id == 70 || mob.id == 72 || mob.id == 64) {
            killer.updatePointPB(5);
            hell.addPointPB(20);
            if (Util.nextInt(1, 15) < 5) {
                Item item = new Item(4,"hell");
                item.setLock(true);
                item.setQuantity(1);
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
        } else if (mob.id == 65) {
            killer.addClanPoint(2);
            killer.addPointSoiNoi(20);
            world.getService().chat("Hệ thống", "Boss " + mob.getTemplate().name + " đã bị chiến binh " + killer.name + " tiêu diệt tại " + template.name + " khu " + id, (byte) 1);
            for (int i = 0; i < 16; i++) {
                Item item = new Item(RandomItem.BOSS_HELL.next(),"hell");
                item.setQuantity(1);
                if (item.id == 838)
                    item.setLock(true);
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
        }
    }

}

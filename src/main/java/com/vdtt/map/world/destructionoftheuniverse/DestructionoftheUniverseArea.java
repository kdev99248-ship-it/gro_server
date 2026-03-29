package com.vdtt.map.world.destructionoftheuniverse;

import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.item.ItemMap;
import com.vdtt.map.item.ItemMapFactory;
import com.vdtt.map.zones.ZWorld;
import com.vdtt.mob.*;
import com.vdtt.model.Char;
import com.vdtt.model.RandomItem;
import com.vdtt.server.GlobalService;
import com.vdtt.util.Util;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DestructionoftheUniverseArea extends ZWorld {
    private static final int[] MONSTERS_ID = {123, 124, 125, 126, 127, 128, 129};
    private boolean spawnedBoss;
    private int idGenerator;
    private boolean finished;

    public DestructionoftheUniverseArea(int id, MapTemplate mapTemplate, Map map) {
        super(id, mapTemplate, map);
    }

    @Override
    public void join(Char p, int typeTau) {
        super.join(p, typeTau);
        world.getService().sendTimeInMap(world.createdAt, world.maxTime - System.currentTimeMillis() + world.createdAt, false);
    }

    public int[][][] xy = {
            { {473, 197}, {525, 197}, {566, 197}, {608, 197}, {654, 197}, {698, 198}, {732, 197}, {912, 284}, {960, 284}, {1016, 284}, {1052, 284}, {257, 283}, {292, 283}, {227, 283}, {175, 283}, {122, 283}, {138, 404}, {195, 404}, {250, 404}, {308, 404}, {378, 404}, {450, 404}, {517, 404}, {583, 404}, {641, 404}, {692, 404}, {752, 404}, {808, 404}, {859, 404}, {936, 404}, {1003, 404}, {1013, 404}},
            {{112, 394}, {163, 394}, {233, 394}, {272, 394}, {325, 394}, {381, 394}, {435, 394}, {488, 394}, {539, 394}, {587, 394}, {636, 394}, {675, 394}, {733, 394}, {806, 394}, {862, 394}, {908, 394}, {965, 394}, {1029, 394}, {1075, 394}, {1082, 279}, {1048, 279}, {996, 279}, {936, 279}, {879, 279}, {809, 279}, {768, 279}, {714, 279}, {443, 277}, {397, 277}, {335, 277}, {284, 277}, {229, 277}, {171, 277}, {131, 277}},
            {{128, 380}, {173, 380}, {238, 380}, {295, 380}, {350, 380}, {422, 380}, {473, 380}, {519, 380}, {577, 380}, {684, 340}, {751, 340}, {815, 340}, {882, 340}, {941, 340}, {996, 340}, {1058, 340}, {1133, 340}, {1185, 340}, {1255, 340}, {1308, 340}, {1375, 340}, {1431, 340}, {1493, 340}, {1539, 340}, {1620, 380}, {1685, 380}, {1756, 380}, {1796, 380}},
            {{120, 398}, {180, 398}, {248, 398}, {365, 345}, {431, 345}, {498, 345}, {549, 345}, {619, 345}, {668, 345}, {729, 345}, {790, 345}, {889, 380}, {973, 380}, {1039, 380}, {1106, 380}, {1170, 380}, {1231, 380}, {1288, 380}, {1359, 380}, {1427, 380}, {1492, 348}, {1575, 348}, {1649, 348}, {1719, 348}, {1779, 348} },
            {{97, 291}, {161, 291}, {318, 219}, {388, 219}, {497, 219}, {669, 219}, {768, 219}, {889, 220}, {1027, 302}, {1095, 302}, {1118, 404}, {1053, 404}, {979, 404}, {892, 404}, {800, 404}, {735, 404}, {649, 404}, {573, 404}, {490, 404}, {440, 404}, {365, 404}, {296, 404}, {227, 404}, {151, 404}},
            {{1859, 348}, {1770, 348}, {1650, 348}, {1506, 395}, {1407, 395}, {1333, 395}, {1237, 395}, {1150, 395}, {1078, 395}, {999, 395}, {834, 348}, {757, 348}, {674, 348}, {576, 348}, {504, 348}, {422, 348}, {352, 348}, {279, 348}, {212, 348}, {113, 348}},
            {{1845, 411}, {1797, 411}, {1716, 411}, {1630, 411}, {1561, 411}, {1494, 411}, {1289, 344}, {1092, 281}, {977, 281}, {904, 281}, {748, 349}, {571, 394}, {496, 394}, {407, 394}, {324, 394}, {247, 394}, {151, 394}, {93, 394}}
    };

    public void setData() {
        int[][] xy = this.xy[this.map.id - 9];
        for (int i = 0; i < xy.length - 1; i++) {
            int x = xy[i][0];
            int y = xy[i][1];
            int monsterId = MONSTERS_ID[Math.min(map.id - 9, MONSTERS_ID.length - 1)];
            Mob mob = new Mob(idGenerator++, monsterId, 10_000_000, 0, x, y, 100);
            mob.levelBoss = 0;
            mob.zone = this;
            addMob(mob);
        }
    }

    public void addPointPB(int point) {
        List<Char> members = getChars();
        for (Char _char : members) {
            try {
                if (!_char.isCleaned)
                    _char.updatePointPB(point);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mobDead(Mob mob, Char killer) {
        addPointPB(1);
        if (mob.hpFull >= 100_000_000) {
            killer.updatePointPB(10);
            killer.addClanPoint(2);
            for (int i = 0; i < 10; i++) {
                Item item = new Item(RandomItem.ITEM_NGUC_TU.next(), "boss");
                item.setQuantity(1);
                item.isLock = true;
                ItemMap itemMap = ItemMapFactory.getInstance().builder().id(numberDropItem++).x((short) (mob.x + Util.nextInt(-30, 50))).y((short) (mob.y)).build();
                itemMap.setOwnerID(killer.id);
                if (item != null) {
                    itemMap.setItem(item);
                    addItemMap(itemMap);
                    getService().addItemMap(itemMap, killer);
                }
            }
            killer.addPointSoiNoi(20);
            GlobalService.getInstance().chat("Hệ thống", "Boss " + mob.getTemplate().name + " đã bị c#item" + killer.name + "c#white tiêu diệt tại " + map.mapTemplate.name + " khu " + id, (byte) 0);
            mob.recoveryTimeCount = 3600;
        }
    }

    @Override
    public void update() {
        super.update();
        if (isAllMonstersDead()) {
            ZDestructionoftheUniverse zDestructionoftheUniverse = (ZDestructionoftheUniverse) world;
            if (!spawnedBoss) {
                int level = zDestructionoftheUniverse.level;
                if (map.id == 9) {
                    Mob mob = createBoss((short) 123, 100_000_000, (short) level, (short) 381, (short) 394);
                    mob.zone = this;
                    addMob(mob);
                } else if (map.id == 10) {
                    Mob mob = createBoss((short) 124, 100_000_000, (short) level, (short) 684, (short) 340);
                    mob.zone = this;
                    addMob(mob);
                } else if (map.id == 11) {
                    Mob mob = createBoss((short) 125, 100_000_000, (short) level, (short) 684, (short) 340);
                    mob.zone = this;
                    addMob(mob);
                } else if (map.id == 12) {
                    Mob mob = createBoss((short) 126, 100_000_000, (short) level, (short) 619, (short) 341);
                    mob.zone = this;
                    addMob(mob);
                } else if (map.id == 13) {
                    Mob mob = createBoss((short) 127, 100_000_000, (short) level, (short) 719, (short) 395);
                    mob.zone = this;
                    addMob(mob);
                } else if (map.id == 14) {
                    Mob mob = createBoss((short) 128, 100_000_000, (short) level, (short) 719, (short) 281);
                    mob.zone = this;
                    addMob(mob);
                } else if (map.id == 15) {
                    Mob mob = createBoss((short) 129, 100_000_000, (short) level, (short) 719, (short) 281);
                    mob.zone = this;
                    addMob(mob);
                }
                spawnedBoss = true;
                return;
            }
        }
        if (isAllMonstersDead() && spawnedBoss) {
            if (map.id == 15) {
                if (!finished) {
                    for (Char _char : this.getChars()) {
                        _char.getService().serverMessage("5s nữa sẽ kết thúc");
                        world.getService().sendTimeInMap(System.currentTimeMillis(), TimeUnit.SECONDS.toMillis(5), false);
                    }
                    Util.setTimeoutSchedule(() -> {
                        world.finish();
                    }, 5000);
                    finished = true;
                }
                return;
            }
            ZDestructionoftheUniverse zDestructionoftheUniverse = (ZDestructionoftheUniverse) world;
            if (!finished) {
                zDestructionoftheUniverse.level++;
                for (Char _char : getChars()) {
                    world.getService().sendTimeInMap(System.currentTimeMillis(), TimeUnit.SECONDS.toMillis(5), false);
                    _char.getService().serverMessage("5s nữa sẽ chuyển màn mới");
                }
                Util.setTimeoutSchedule(() -> {
                    for (Char _char : getChars()) {
                        _char.setXY(90, 336);
                        world.zones.get(((ZDestructionoftheUniverse) world).level).join(_char, -1);
                    }
                }, 5000);
                finished = true;
            }
        }
    }

    private Boss createBoss(short monsterId, int hp, short level, short x, short y) {
        Boss boss = createBoss(monsterId, hp, level, x, y, 10_000);
        boss.attackDelay = 1000;
        boss.zone = this;
        boss.dameMax = hp / 8000;
        return boss;
    }
}

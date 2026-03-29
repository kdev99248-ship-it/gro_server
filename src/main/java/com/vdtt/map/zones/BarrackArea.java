package com.vdtt.map.zones;

import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.world.Barrack;
import com.vdtt.mob.*;

public class BarrackArea extends ZWorld {
    private boolean spawnedBoss;

    public BarrackArea(int id, MapTemplate mapTemplate, Map map) {
        super(id, mapTemplate, map);
    }

    public void setData() {
        Barrack barrack = (Barrack) world;
        int level = barrack.getLevel();
        for (Mob mob : monsters) {
            mob.hp = mob.hpFull = mob.hpGoc = level * 10000;
            mob.level = level;
            mob.kiNow = mob.kiMax = level * 50;
            mob.dameMax = level * 15;
        }
    }

    @Override
    public void update() {
        super.update();
        if (isAllMonstersDead()) {
            if (!spawnedBoss) {
                Barrack barrack = (Barrack) world;
                int level = barrack.getLevel();
                if (map.id == 106) {
                    Mob mob = createBoss((short) 290, 100000 * level, (short) level, (short) 1560, (short) 338);
                    addMob(mob);
                } else if (map.id == 107) {
                    Mob mob = createBoss((short) 291, 100000 * level, (short) level, (short) 2215, (short) 341);
                    addMob(mob);
                } else if (map.id == 108) {
                    NinjaAoTim main = (NinjaAoTim) createBoss((short) 292, 100000 * level, (short) level, (short) 1326, (short) 342);
                    addMob(main);

                    NinjaAoTim mob = (NinjaAoTim) createBoss((short) 292, 100000 * level, (short) level, (short) 1356, (short) 342);
                    main.addClone(mob);
                    addMob(mob);

                    mob = (NinjaAoTim) createBoss((short) 292, 100000 * level, (short) level, (short) 1386, (short) 342);
                    main.addClone(mob);
                    addMob(mob);

                    mob = (NinjaAoTim) createBoss((short) 292, 100000 * level, (short) level, (short) 1406, (short) 342);
                    main.addClone(mob);
                    addMob(mob);


                    mob = (NinjaAoTim) createBoss((short) 292, 100000 * level, (short) level, (short) 1406, (short) 342);
                    main.addClone(mob);
                    addMob(mob);
                } else if (map.id == 109) {// 4
                    Wall wall = (Wall) createBoss((short) 297, 100_000, (short) level, (short) 2220, (short) 300);
                    addMob(wall);

                    Buyon buyon = (Buyon) createBoss((short) 293, 150000 * level, (short) level, (short) 2260, (short) 340);
                    buyon.setWall(wall);
                    addMob(buyon);
                } else if (map.id == 110) {
                    Gamma gamma1 = (Gamma) createBoss((short) 294, 200000 * level, (short) level, (short) 376, (short) 342);
                    addMob(gamma1);

                    Gamma gamma2 = (Gamma) createBoss((short) 295, 200000 * level, (short) level, (short) 376, (short) 342);
                    addMob(gamma2);

                    gamma1.setGamma(gamma2);
                    gamma2.setGamma(gamma1);
                } else if (map.id == 111) {
                    Mob mob = createBoss((short) 296, 250000 * level, (short) 10, (short) 1558, (short) 499);
                    addMob(mob);
                }
                spawnedBoss = true;
            } else {
                if (map.id == 111) {
                    world.finish();
                }
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

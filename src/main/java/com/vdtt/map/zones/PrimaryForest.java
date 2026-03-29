package com.vdtt.map.zones;

import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.mob.Boss;
import com.vdtt.mob.Mob;
import com.vdtt.model.Char;

public class PrimaryForest extends ZWorld {

    private static int[] MONSTERS_ID = {183, 277, 278, 279, 280};
    private int idGenerator;
    private boolean finished;


    private int level;

    public PrimaryForest(int id, MapTemplate tilemap, Map map) {
        super(id, tilemap, map);
        npcs.clear();
    }

    @Override
    public void createMonster() {
        int minX = 380;
        int maxX = 2750;
        int y = 416;
        int monsterId = MONSTERS_ID[level];
        for (int x = minX; x <= maxX; x += 48) {
            Mob mob = new Mob(idGenerator++, monsterId, 100_000 * (level + 1), 0, x, y, 100);
            mob.levelBoss = 0;
            mob.zone = this;
            addMob(mob);
        }
    }

    @Override
    public void requestChangeMap(Char p) {
        join(p, -1);
        p.serverMessage("Không thể rời khỏi đây khi chưa hoàn thành!");
    }

    @Override
    public synchronized void update() {
        super.update();
        if (isAllMonstersDead()) {
            if (level < 5) {
                level++;
                if (level == 5) {
                    Boss boss = createBoss((short) 88, 5_000_000, (short) 0, (short) 1500, (short) 416, 5000);
                    boss.idEntity = idGenerator++;
                    boss.zone = this;
                    addMob(boss);
                } else {
                    createMonster();
                }
            } else if (!finished) {
                finished = true;
                world.finish();
            }
        }
    }
}

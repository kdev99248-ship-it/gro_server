package com.vdtt.server;

import com.vdtt.data.DataCenter;
import com.vdtt.map.Map;
import com.vdtt.map.zones.Zone;
import com.vdtt.mob.Boss;
import com.vdtt.mob.Mob;
import com.vdtt.mob.MobTemplate;
import com.vdtt.util.Log;
import com.vdtt.util.RandomCollection;
import com.vdtt.util.Util;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class SpawnBoss {

    private int id;
    private Map map;
    private final RandomCollection<Integer> mobs = new RandomCollection<>();
    private Mob currMonster;
    private short x, y;
    private int hp;
    private int kiMax;

    public SpawnBoss(int id, Map map, short x, short y, int hp, int kiMax) {
        this.id = id;
        this.map = map;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.kiMax = kiMax;
    }

    public void add(int rate, int mobID) {
        mobs.add(rate, mobID);
    }

    public void spawn() {
        if (currMonster != null) {
            currMonster.die();
            currMonster = null;
        }
        int zoneId = Util.nextInt(0, 9);
        Zone zone = map.getZoneById(zoneId);
        int mobID = mobs.next();
        MobTemplate mobTemplate = DataCenter.gI().mobTemplate[mobID];
        Boss boss = zone.createBoss((short) mobID, hp, mobTemplate.getTimeThuHoach(), x, y, kiMax);
        boss.zone = zone;

        String text = "c#redBoss " + boss.getTemplate().name + " c#whiteđã xuất hiện tại " + map.mapTemplate.name + " khu " + zone.id;
        boss.setNotifyText(text);

        zone.addMob(boss);
        currMonster = boss;
        Log.info(text);

        SpawnBossManager.getInstance().addBoss(boss);
    }
}


package com.vdtt.map.zones;

import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.mob.Mob;
import com.vdtt.model.Char;

public class ZTimeRoom extends ZWorld {
    public int level;

    public ZTimeRoom(int id, MapTemplate tilemap, Map map, int level) {
        super(id, tilemap, map);
        this.level = level;
        create();
    }

    @Override
    public void join(Char p, int typeTau) {
        super.join(p, -1);
    }

    private void create() {
        try {
            monsters.clear();
            for (Mob mob : map.mapTemplate.mobList) {
                if (mob != null) {
                    Mob mob2 = mob.clone();
                    mob2.idEntity = monsters.size();
                    mob2.zone = this;
                    mob.level = level;
                    mob2.hp = mob2.hpFull = mob2.hpGoc = level * 200;
                    mob2.kiNow = mob2.kiMax = level * 15;
                    mob2.dameMax = level * 5;
                    monsters.add(mob2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

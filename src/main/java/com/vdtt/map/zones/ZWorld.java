package com.vdtt.map.zones;

import com.vdtt.map.Map;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.world.World;
import com.vdtt.map.world.destructionoftheuniverse.DestructionoftheUniverseArea;
import com.vdtt.model.Char;
import com.vdtt.model.WayPoint;
import lombok.Getter;
import lombok.Setter;

public class ZWorld extends Zone {

    @Getter
    @Setter
    protected World world;

    public ZWorld(int id, MapTemplate tilemap, Map map) {
        super(id, tilemap, map);
    }

    @Override
    public void join(Char p, int typeTau) {
        try {
            Zone preZone = p.zone;
            if (preZone != null) {
                if (!preZone.map.isWorld()) {
                    p.addMemberForWorld(preZone, this);
                }
                if (!(preZone instanceof DestructionoftheUniverseArea) && p.clan != null) {
                    p.clan.addMemberForWorld(preZone, this, p);
                }
            }
            super.join(p, -1);
            p.getService().sendTimeInMap(worldCreatedAt, worldMaxTime, timeInMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAllMonstersDead() {
        return monsters.stream().allMatch(mob -> mob.isDead);
    }


    @Override
    public void requestChangeMap(Char p) {
        if (this instanceof BarrackArea area) {
            if (!area.isAllMonstersDead()) {
                join(p, -1);
                p.serverMessage("Chưa tiêu diệt hết kẻ địch");
                return;
            }
        }
        if (!map.mapTemplate.listWayPoint.isEmpty()) {
            WayPoint way = null;
            for (WayPoint wayPoint : map.mapTemplate.listWayPoint) {
                if (way == null || wayPoint.getRange(p) <= way.getRange(p)) {
                    way = wayPoint;
                }
            }
            if (way != null && way.getRange(p) < 250) {
                int nextId = way.mapNext;
                Zone z = world.find(nextId);
                if (z != null) {
                    z.join(p, -1);
                } else {
                    p.changeMap(nextId);
                }
            }
        }

    }

}
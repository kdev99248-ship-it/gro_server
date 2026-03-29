package com.vdtt.map.world.planetblackstar.dragon;
import com.vdtt.mob.Boss;

public abstract class AbsDragon extends Boss {
    public AbsDragon(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
    }

    @Override
    public void die() {
        super.die();
        zone.removeMonster(this);
    }
}

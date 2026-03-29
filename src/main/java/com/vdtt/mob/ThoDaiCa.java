package com.vdtt.mob;

public class ThoDaiCa extends Boss {

    public static ThoDaiCa current;

    public ThoDaiCa(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
        current = this;
    }

    @Override
    public void die() {
        super.die();
        current = null;
        zone.removeMonster(this);
    }
}

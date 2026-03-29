package com.vdtt.mob;

public class CellMax extends Boss {

    public static CellMax current;

    public CellMax(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
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

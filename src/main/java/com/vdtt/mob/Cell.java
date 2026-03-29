package com.vdtt.mob;

public class Cell extends Boss {
    public Cell(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
    }

    @Override
    public void die() {
        this.hp = 0;
        this.status = 4;
        this.recoveryTimeCount = 54000;
        this.isDead = true;
        this.chars.clear();
        this.zone.getService().updateMobDie(this);
    }
}

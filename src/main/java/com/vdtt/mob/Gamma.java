package com.vdtt.mob;

import lombok.Getter;
import lombok.Setter;

public class Gamma extends Boss {

    @Setter
    @Getter
    private Gamma gamma;

    private long lastRecoveryTime;

    public Gamma(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
        this.lastRecoveryTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if (!isDead) {
            long now = System.currentTimeMillis();
            if (this.hp < gamma.hp && now - lastRecoveryTime > 5000L) {
                lastRecoveryTime = now;
                addHp(gamma.hp - this.hp);
            }
        }
        super.update();
    }
}

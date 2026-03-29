package com.vdtt.mob;

import java.util.ArrayList;
import java.util.List;

public class NinjaAoTim extends Boss {
    private List<NinjaAoTim> clones;

    public NinjaAoTim(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
        this.clones = new ArrayList<>();
    }

    public void addClone(NinjaAoTim ninjaAoTim) {
        this.clones.add(ninjaAoTim);
    }

    @Override
    public void die() {
        super.die();
        clones.forEach(ninjaAoTim -> {
            if (!ninjaAoTim.isDead) {
                ninjaAoTim.die();
            }
        });
    }
}

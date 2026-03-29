package com.vdtt.mob;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Buyon extends Boss {

    private Wall wall;

    public Buyon(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
    }

    public boolean isWallBroken() {
        return wall == null || wall.isDead;
    }
}

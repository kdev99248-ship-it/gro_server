package com.vdtt.map.world.planetblackstar.dragon;

import com.vdtt.map.world.planetblackstar.ZPlanetBlackStar;
import com.vdtt.model.Char;

public class Rong2Sao extends AbsDragon {
    public Rong2Sao(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
    }

    @Override
    public void dropItem(Char owner, byte type) {
        ((ZPlanetBlackStar) zone).createNewItemMapStar(this, owner, 2);
    }
}

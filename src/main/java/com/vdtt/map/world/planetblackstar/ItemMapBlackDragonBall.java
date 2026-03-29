package com.vdtt.map.world.planetblackstar;

import com.vdtt.effect.Effect;
import com.vdtt.map.item.ItemMap;
import com.vdtt.model.Char;

public class ItemMapBlackDragonBall extends ItemMap {
    public int star;

    public ItemMapBlackDragonBall(short id) {
        super(id);
    }

    public void pickBall(Char pl, short itemMapId) {
        pl.getService().serverDialog("Bạn đang giữ viên " + item.getTemplate().name);
        pl.itemMapBlackDragonBall = this;
        pl.lastTimePickBall = System.currentTimeMillis();
        pl.zone.removeItem(this);
        pl.zone.getService().removeItem(itemMapId);
        for (Char player : pl.clan.getOnlineMembers()) {
            if (player.zone instanceof ZPlanetBlackStar) {
                player.setTypePk((byte) 3);
            }
        }

        Effect effect = new Effect(132, PlanetBlackStar.TIME_WIN, star);
        pl.getEm().setEffect(effect);
    }
}

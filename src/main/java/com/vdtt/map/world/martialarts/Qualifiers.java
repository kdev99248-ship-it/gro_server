package com.vdtt.map.world.martialarts;

import com.vdtt.map.MapManager;
import com.vdtt.model.Char;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Qualifiers extends Round {

    public Qualifiers(MartialArtsConference conference) {
        super(conference, "Vòng loại", 20);
        setMaxTime((int) TimeUnit.MINUTES.toMillis(6));
        setPrepareTime((int) TimeUnit.MINUTES.toMillis(1));
    }

    @Override
    protected void consolationForLoser(List<Char> players) {
        for (Char player : players) {
            player.wakeUpFromDead();
            removeMember(player);
            MapManager.getInstance().joinZone(player, 86, 0, 1);
            player.serverDialog("Bạn đã thua vòng loại hẹn gặp lại bạn ở giải sau");
        }
    }
}

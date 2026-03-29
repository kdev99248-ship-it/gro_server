package com.vdtt.map.world.martialarts;

import com.vdtt.map.MapManager;
import com.vdtt.model.Char;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoundOf16 extends Round {
    public RoundOf16(MartialArtsConference conference) {
        super(conference, "Vòng 16 đội", 2);
        setMaxTime((int) TimeUnit.SECONDS.toMillis(200));
        setPrepareTime((int) TimeUnit.SECONDS.toMillis(20));
    }

    @Override
    protected void consolationForLoser(List<Char> players) {
        for (Char player : players) {
            player.wakeUpFromDead();
            removeMember(player);
            MapManager.getInstance().joinZone(player, 86, 0, 1);
            player.serverDialog("Bạn đã thua vòng" + name + " hẹn gặp lại bạn ở giải sau");
        }
    }
}

package com.vdtt.map.world.stadiums;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.zones.ZWorld;
import com.vdtt.model.Char;
import com.vdtt.server.GlobalService;
import com.vdtt.util.TimeUtils;
import com.vdtt.util.Util;

import java.util.List;

/**
 * @author BTH Cute phô mai que
 */
public class Zstadium extends ZWorld {
    private long lastTimeUpdate;
    private long lastTimeWait;

    public Zstadium(int id, MapTemplate tilemap, Map map) {
        super(id, tilemap, map);
        lastTimeWait = System.currentTimeMillis();
    }

    @Override
    public void updateChar() {
        super.updateChar();
        if (!TimeUtils.canDoWithTime(lastTimeUpdate, 5000)) {
            return;
        }

        lastTimeUpdate = System.currentTimeMillis();
        List<Char> chars = getChars();
        if (chars.isEmpty()) {
            return;
        }
        Char player1 = null;
        Char player2 = null;
        try {
            player1 = chars.get(0);
            player2 = chars.get(1);
        }
        catch (Exception e) {}

        if (player1 == null && player2 == null) {
            return;
        }

        if (player1 == null && lastTimeWait != -1 && TimeUtils.canDoWithTime(lastTimeWait, 60000)) {
            handlePlayerForfeit(player2);
            lastTimeWait = -1;
            return;
        }

        if (player2 == null && lastTimeWait != -1 && TimeUtils.canDoWithTime(lastTimeWait, 60000)) {
            handlePlayerForfeit(player1);
            lastTimeWait = -1;
            return;
        }

        if (player1 != null && player1.hp <= 0) {
            handlePlayerDefeat(player1, player2);
        } else if (player2 != null && player2.hp <= 0) {
            handlePlayerDefeat(player2, player1);
        }
    }

    private void handlePlayerForfeit(Char player) {
        player.serverDialog("Bạn đã thắng do người chơi còn lại bỏ cuộc");
        player.addBallz(player.stadium.getBallz(),true);
        schedulePlayerExit(player);
    }

    private void handlePlayerDefeat(Char loser, Char winner) {
        if (loser.stadium == null || loser.stadium.match == null) {
            return;
        }
        if (winner.stadium == null || winner.stadium.match == null) {
            return;
        }
        if (loser.ballZ < loser.stadium.getBallz()) {
            loser.serverDialog("Do 1 trong 2 bên đã tiêu hết Zeni nên cả 2 bên không được ghi nhận quà chiến thắng và thua cuộc");
            winner.serverDialog("Do 1 trong 2 bên đã tiêu hết Zeni nên cả 2 bên không được ghi nhận quà chiến thắng và thua cuộc");
        } else {
//            loser.addBallz(-loser.stadium.getBallz(), true);
            winner.addBallz((int) (loser.stadium.getBallz() * 1.8), true);
        }
        GlobalService.getInstance().chat("Hệ thống", "Chiến binh " + winner.name + " đã chiến thắng chiến binh " + loser.name + " trong lôi đài mức cược " + loser.stadium.getBallz() + " ballZ", (byte) 1);
        loser.serverDialog("Bạn đã thua người chơi " + winner.name);
        schedulePlayerExit(loser);

        winner.serverDialog("Bạn đã thắng người chơi " + loser.name + " và nhận được " + loser.stadium.getBallz() / 100 * 90 + " ballz");
        schedulePlayerExit(winner);
    }

    private void schedulePlayerExit(Char player) {
        Util.setTimeoutSchedule(() -> {
            player.wakeUpFromDead();
            MapManager.getInstance().joinZone(player, 86, 0, 1);
        }, 10000);
        player.stadium.close();
        player.stadium = null;
    }
}

package com.vdtt.map.world.stadiums;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.World;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author BTH Cute phô mai que
 */
public class Match extends World {

    private final Stadium stadium;
    private Zstadium zstadium;
    protected int maxPlayersInZone;

    @Setter
    private int prepareTime;

    @Getter
    private boolean fighting;

    public Match(Stadium stadium) {
        this.stadium = stadium;
        setMaxTime((int) TimeUnit.SECONDS.toMillis(200));
        setPrepareTime((int) TimeUnit.SECONDS.toMillis(20));
        generateId();
        this.maxPlayersInZone = 2;
        this.initFinished = true;
        this.members = new ArrayList<>();
        this.isClosed = false;
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return false;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return (pre instanceof Zstadium) && !(next instanceof Zstadium);
    }

    @Override
    public void doFinish() {
        this.isClosed = true;
    }

    @Override
    public void doEnd() {
        for (Char player : getMembers()) {
            player.wakeUpFromDead();
            MapManager.getInstance().joinZone(player, 86, 0, 1);
        }
        stadium.close();
        this.isClosed = true;
    }

    @Override
    public void update() {
        if (!isClosed && isTimeOut()) {
            doEnd();
        }
        if (!fighting && (System.currentTimeMillis() - createdAt > prepareTime)) {
            startFighting();
        }
    }

    private void startFighting() {
        Log.debug("start fighting");
        fighting = true;

        if (getMembers().isEmpty() && getMembers().size() <= 1) {
            Char player = getMembers().get(0);
            player.serverDialog("Không đủ người trận đấu bị hủy");
            doEnd();
            Util.setTimeoutSchedule(() -> {
                player.wakeUpFromDead();
                MapManager.getInstance().joinZone(player, 86, 0, 1);
            }, 10000);
            return;
        }

        for (Char player : getMembers()) {
            player.setTypePk((byte) 3);
        }
    }

    protected void createArea() {
        Map map = MapManager.getInstance().find(45);
        zstadium = new Zstadium(this.id, map.mapTemplate, map);
        zstadium.setWorld(this);
        zstadium.worldMaxTime = maxTime;
        zstadium.worldCreatedAt = createdAt;
        zstadium.timeInMap = false;
        addZone(zstadium);
    }

    public synchronized void join(Char player) {
        player.addWorld(this);
        player.setTypePk((byte) 0);
        player.wakeUpFromDead();
        zstadium.join(player, -1);
    }
}

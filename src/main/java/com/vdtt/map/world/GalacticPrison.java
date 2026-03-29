package com.vdtt.map.world;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.ZGalacticPrison;
import com.vdtt.map.zones.Zone;

import java.util.concurrent.TimeUnit;

public class GalacticPrison extends World {

    public GalacticPrison() {
        setType(World.GALACTIC_PRISON);
        this.name = "GalacticPrison";
        generateId();
        setMaxTime((int) TimeUnit.MINUTES.toMillis(45));
        prepare();
        this.initFinished = true;
    }

    private void prepare() {
        createArea(93);
        createArea(94);
        createArea(95);
        createArea(96);
        createArea(97);
    }


    private void createArea(int mapId) {
        Map map = MapManager.getInstance().find(mapId);
        ZGalacticPrison area = new ZGalacticPrison(this.id, map.mapTemplate, map);
        area.setWorld(this);
        area.worldMaxTime = maxTime;
        area.worldCreatedAt = createdAt;
        area.timeInMap = false;
        addZone(area);
    }

    @Override
    public void doFinish() {

    }

    @Override
    public void doEnd() {

    }
    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !(pre instanceof ZGalacticPrison) && next instanceof ZGalacticPrison;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre instanceof ZGalacticPrison && !(next instanceof ZGalacticPrison);
    }
}

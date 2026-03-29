package com.vdtt.map.world;

import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import com.vdtt.util.Log;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class World {
    public static final byte BARRACK = 1;
    public static final byte DUNGEON = 2;
    public static final byte GALACTIC_PRISON = 3;
    public static final byte MORO_QUEST = 4;
    public static final byte TIME_ROOM = 5;
    public static final byte DESTRUCTION_UNIVERSE = 6;

    public static int number = 0;

    @Getter
    protected int id;
    @Getter
    @Setter
    protected byte type;
    @Getter
    public long maxTime;
    @Getter
    protected boolean isClosed;
    protected boolean initFinished;
    protected List<Char> members;
    public List<Zone> zones;
    @Getter
    protected WorldService service;
    @Getter
    @Setter
    protected String name;
    public long createdAt;
    private boolean finished;

    public World() {
        this.name = "World";
        this.members = new CopyOnWriteArrayList<>();
        this.zones = new ArrayList<>();
        this.service = new WorldService(this);
        this.createdAt = System.currentTimeMillis();
        WorldManager.getInstance().addWorld(this);
    }

    public boolean isTimeOut() {
        return System.currentTimeMillis() - createdAt >= maxTime;
    }
    
    public long getTimeRemaining() {
        return this.createdAt + maxTime - System.currentTimeMillis();
    }

    public void generateId() {
        this.id = number++;
    }

    public List<Char> getMembers() {
        return members.stream().distinct().collect(Collectors.toList());
    }

    public void addMember(Char _char) {
        this.members.add(_char);
    }

    public void removeMember(Char _char) {
        this.members.remove(_char);
        Log.debug(String.format("remove %s playername: %s", this, _char.name));
    }

    public abstract boolean enterWorld(Zone pre, Zone next);

    public abstract boolean leaveWorld(Zone pre, Zone next);

    public void clearAllMember() {
        this.members.clear();
    }

    public void addZone(Zone z) {
        zones.add(z);
    }

    public void removeZone(Zone z) {
        zones.remove(z);
    }

    public void update() {
        if (!isClosed && isTimeOut()) {
            close();
            Log.error("close world: " + this);
        }
    }

    public void close() {
        this.isClosed = true;
        doEnd();
        clearAllMember();
        this.members = null;
        zones.forEach(Zone::close);
        this.zones = null;
    }

    public Zone find(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.id == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        service.sendTimeInMap(createdAt, maxTime, false);
    }

    public void setRemaining(long time) {
        setMaxTime((System.currentTimeMillis() - createdAt) + time);
    }

    @Override
    public String toString() {
        return String.format("%s[%d]", this.name, this.id);
    }

    public final void finish() {
        if (finished) {
            return;
        }
        finished = true;
        setRemaining(60_000L);
        doFinish();
    }

    public abstract void doFinish();
    public abstract void doEnd();
}


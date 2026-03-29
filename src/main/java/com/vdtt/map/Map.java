package com.vdtt.map;

import com.vdtt.map.world.planetblackstar.ZPlanetBlackStar;
import com.vdtt.map.zones.NgucTu;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Map {
    public short id;
    @Getter
    private List<Zone> zones = new ArrayList<>();
    public MapTemplate mapTemplate;
    public static boolean running = true;
    public Thread threadUpdateChar, threadUpdateOther;
    private ReadWriteLock lock;
    public int maxPlayer = 30;

    public Map(short id) {
        try {
            this.id = id;
            this.mapTemplate = MapManager.getInstance().getMapTemplates().get(id);
            this.mapTemplate.createWayPoint();
            this.mapTemplate.loadData();
            this.mapTemplate.loadData2();
            lock = new ReentrantReadWriteLock();
            initZone();
            update();
        } catch (Exception e) {
            Log.error("Map: " + id, e);
            e.printStackTrace();
        }
    }

    public void initZone() {
        zones.clear();
        for (int i = 0; i < 30; i++) {
            Zone z = null;
            if (id == 2 || id == 4 || id == 6 || id == 8) {
                z = new NgucTu((byte) i, this.mapTemplate, this);
            } else
                z = new Zone((byte) i, this.mapTemplate, this);
        }
    }

    public void addZone(Zone z) {
        lock.writeLock().lock();
        try {
            zones.add(z);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeZone(Zone z) {
        lock.writeLock().lock();
        try {
            zones.remove(z);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Zone getZoneById(int index) {
        return this.zones.get(index);
    }

    public void joinZone(Char _char, int zoneId, int typeTau) {
        try {
            Zone z = getZoneById(zoneId);
            if (z != null) {
                if (z.getChars().size() < getMaxPlayer(z)) {
                    z.join(_char, typeTau);
                } else {
                    for (Zone z1 : zones) {
                        if (z1.getChars().size() < getMaxPlayer(z1)) {
                            z1.join(_char, typeTau);
                            break;
                        }
                    }
                }
            } else {
                MapManager.getInstance().joinZone(_char, 86, 0, typeTau);
            }
        } catch (Exception e) {
            Log.error(String.format("Char: %s, Map: %s, Cleaned: %b", _char.name, this.id, _char.isCleaned), e);
            e.printStackTrace();
        }
    }

    public int getMaxPlayer(Zone zone) {
        if (zone.findBossInZone()) {
            return 999;
        } else {
            return 30;
        }
    }

    public void updateChar() {
        while (running) {
            try {
                long l1 = System.currentTimeMillis();
                lock.readLock().lock();
                try {
                    for (Zone zone : zones) {
                        if (!zone.isClosed()) {
                            zone.updateChar();
                        }
                    }
                } finally {
                    lock.readLock().unlock();
                }
                long l2 = System.currentTimeMillis() - l1;
                if (l2 >= 500L) {
                    continue;
                }
                try {
                    Thread.sleep(500L - l2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public void updateOther() {
        while (running) {
            try {
                long l1 = System.currentTimeMillis();
                ArrayList<Zone> list = new ArrayList<>();
                lock.readLock().lock();
                try {
                    for (Zone zone : zones) {
                        if (!zone.isClosed()) {
                            zone.update();
                        } else {
                            if(!(zone instanceof ZPlanetBlackStar)){
                                list.add(zone);
                            }
                        }
                    }
                } finally {
                    lock.readLock().unlock();
                }
                if (list.size() > 0) {
                    lock.writeLock().lock();
                    try {
                        zones.removeAll(list);
                    } finally {
                        lock.writeLock().unlock();
                    }
                }
                long l2 = System.currentTimeMillis() - l1;
                if (l2 >= 1000L) {
                    continue;
                }
                try {
                    Thread.sleep(1000L - l2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    public void update() {
        this.threadUpdateChar = new Thread(new Runnable() {
            @Override
            public void run() {
                updateChar();
            }

        });
        this.threadUpdateChar.start();
        this.threadUpdateOther = new Thread(new Runnable() {
            @Override
            public void run() {
                updateOther();
            }
        });
        this.threadUpdateOther.start();
    }

    public void close() {
        if (this.threadUpdateChar != null && this.threadUpdateChar.isAlive()) {
            this.threadUpdateChar.interrupt();
        }
        this.threadUpdateChar = null;
        if (this.threadUpdateOther != null && this.threadUpdateOther.isAlive()) {
            this.threadUpdateOther.interrupt();
        }
        this.threadUpdateOther = null;
    }

    public Zone rand() {
        return zones.get(Util.nextInt(zones.size()));
    }

    public boolean isWorld() {
        return isTrainZone() || isHell() || isNgucTu() || isBarrack();
    }

    public boolean isTrainZone() {
        return id == 84;
    }

    public boolean isHell() {
        return id == 19;
    }

    public boolean isBarrack() {
        return id >= 106 && id <= 111;
    }

    public boolean isNgucTu() {
        return id == 2 || id == 4 || id == 6 || id == 8;
    }
}

package com.vdtt.map.world;

import com.vdtt.util.Log;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldManager extends Thread {

    @Getter
    private static final WorldManager instance = new WorldManager();

    private List<World> worlds;
    private long delay;
    private boolean running;

    public WorldManager() {
        this.delay = 1000;
        this.running = true;
        this.worlds = new CopyOnWriteArrayList<>();
        setName(WorldManager.class.getName());
    }

    public void addWorld(World world) {
        this.worlds.add(world);
    }

    public void removeWorld(World world) {
        this.worlds.remove(world);
    }

    @Override
    public void run() {
        while (running) {
            long l1 = System.currentTimeMillis();
            update();
            long l2 = System.currentTimeMillis() - l1;
            if (l2 < 1000) {
                try {
                    Thread.sleep(delay - l2);
                } catch (Exception e) {
                    Log.error("update ex: " + e.getMessage(), e);
                }
            }
        }
    }

    public void update() {
        if (!worlds.isEmpty()) {
            List<World> list = new ArrayList<>();
            for (World world : worlds) {
                try {
                    if (world.initFinished && !world.isClosed) {
                        world.update();
                    } else if (world.isClosed) {
                        list.add(world);
                    }
                } catch (Exception e) {
                    Log.error("world update ex: " + e.getMessage(), e);
                }
            }
            if (!list.isEmpty()) {
                worlds.removeAll(list);
            }
        }
    }

    public void close() {
        this.running = false;
        worlds.clear();
    }
}

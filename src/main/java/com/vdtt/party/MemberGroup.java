package com.vdtt.party;

import com.vdtt.map.world.World;
import com.vdtt.model.Char;

import java.util.ArrayList;
import java.util.List;

public class MemberGroup {
    public int charId;
    public byte classId;
    public String name;
    private Char p;
    private final List<World> worlds = new ArrayList<>();

    public void add(World world) {
        synchronized (worlds) {
            worlds.add(world);
        }
    }

    public void remove(World world) {
        synchronized (worlds) {
            worlds.remove(world);
        }
    }

    public void setChar(Char p) {
        this.p = p;
    }

    public Char getChar() {
        return this.p;
    }
}

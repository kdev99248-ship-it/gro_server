package com.vdtt.map.world;

import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.util.Log;

import java.io.IOException;
import java.util.List;


public class WorldService {
    private World world;

    public WorldService(World world) {
        this.world = world;
    }

    public void sendTimeInMap(long createdAt, long maxTime, boolean bZ) {
        try {
            Message ms = new Message((byte) -123);
            ms.writer().writeByte(-80);
            ms.writer().writeLong(createdAt);
            ms.writer().writeInt((int) maxTime);
            ms.writer().writeBoolean(bZ);
            ms.writer().flush();
            sendMessage(ms);
        } catch (Exception e) {

        }
    }

    public void serverMessage(String message) {
        List<Char> members = world.getMembers();
        for (Char _char : members) {
            try {
                if(_char != null&&!_char.isCleaned)
                    _char.serverMessage(message);
            } catch (Exception e) {
                Log.error("worldService serverMessage ex: " + e.getMessage(), e);
            }
        }
    }

    public void sendMessage(Message ms) {
        List<Char> members = world.getMembers();
        for (Char _char : members) {
            try {
                _char.getService().sendMessage(ms);
            } catch (Exception e) {
                Log.error("worldService sendMessage ex: " + e.getMessage(), e);
            }
        }
    }

    public void chat(String name, String text2,byte type) {
        try {
            Message m = new Message((byte) 22);
            m.writer().writeByte(type);
            m.writeUTF(name);
            m.writeUTF(text2);
            sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }
}


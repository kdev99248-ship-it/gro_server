package com.vdtt.server;

import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.stall.Stall;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class GlobalService {
    @Getter
    private static final GlobalService instance = new GlobalService();

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
    public void raoBan(String name, String text2, byte type, Stall stall) {
        try {
            Message m = new Message((byte) 22);
            m.writer().writeByte(type);
            m.writeUTF(name);
            m.writeUTF(text2);
            m.writer().writeLong(stall.getId());
            m.writer().writeInt(stall.getPrice());
            stall.getItem().write(m);
            sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }
    public void sendMessage(Message ms) {
        List<Char> chars = ServerManager.getChars();
        for (Char _char : chars) {
            _char.getService().sendMessage(ms);
        }
    }
}

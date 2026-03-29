package com.vdtt.clan;

import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.util.Log;

import java.util.List;

public class ClanService {
    private Clan clan;

    public ClanService(Clan clan) {
        this.clan = clan;
    }

    public void chat(String name, String text) {
        try {
            Message mss = new Message((byte) 25);
            mss.writeUTF(name);
            mss.writeUTF(text);
            sendMessage(mss);
            mss.cleanup();
        } catch (Exception ex) {
            Log.error("chat err: " + ex.getMessage(), ex);
        }
    }

    public void serverMessage(String text) {
        try {
            Message ms = new Message((byte) -107);
            ms.writeUTF(text);
            sendMessage(ms);
        } catch (Exception ex) {
        }
    }

    public void sendMessage(Message ms) {
        List<Member> members = clan.memberDAO.getAll();
        synchronized (members) {
            for (Member mem : members) {
                if (mem != null) {
                    Char _char = mem.getChar();
                    if (_char != null && _char.user != null && !_char.isCleaned) {
                        _char.getService().sendMessage(ms);
                    }
                }
            }
        }
    }
}

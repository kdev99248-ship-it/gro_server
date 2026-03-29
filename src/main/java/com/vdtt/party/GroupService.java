package com.vdtt.party;

import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupService {
    private Group group;

    public GroupService(Group group) {
        this.group = group;
    }

    public void playerInParty() {
        try {
            Message ms = new Message((byte) 43);
            DataOutputStream ds = ms.writer();
            ds.writeBoolean(group.isLock);
            List<MemberGroup> partys = group.getMemberGroup();
            ms.writer().writeByte(partys.size());
            for (MemberGroup p : partys) {
                ms.writer().writeByte(p.classId);
                ms.writer().writeByte(p.classId);
                ms.writer().writeShort(p.getChar().level()); // ID ICON CHAR
                ms.writeUTF(p.name);
            }
            ds.flush();
            sendMessage(ms);
        } catch (IOException ex) {
            Log.error("playerInParty err:" + ex.getMessage(), ex);
        }
    }

    public void changeLeader(int index) {
        try {
            Message ms = new Message((byte) 1);
            DataOutputStream ds = ms.writer();
            ds.writeByte(index);
            ds.flush();
            sendMessage(ms);
        } catch (IOException ex) {
            Log.error("changeLeader err:" + ex.getMessage(), ex);
        }
    }

    public void chat(String name, String text) {
        try {
            Message mss = new Message((byte) 26);
            mss.writeUTF(name);
            mss.writeUTF(text);
            sendMessage(mss);
            mss.cleanup();
        } catch (IOException ex) {
            Log.error("chat err: " + ex.getMessage(), ex);
        }
    }



    public void sendMessage(Message ms) {
        List<Char> chars = group.getChars();
        for (Char _char : chars) {
            _char.getService().sendMessage(ms);
        }
    }
}

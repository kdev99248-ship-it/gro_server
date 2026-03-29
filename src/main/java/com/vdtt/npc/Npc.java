package com.vdtt.npc;

import com.vdtt.model.ObjectLive;
import com.vdtt.network.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Npc extends ObjectLive implements Cloneable {
    public int id;
    public int status;

    public void writeMessage(Message message) throws IOException {
        message.writer().writeByte(status);
        message.writer().writeShort(id);
        message.writer().writeShort(x);
        message.writer().writeShort(y);
    }

    public void readJson(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.status = json.getInt("status");
            this.x = (short) json.getInt("x");
            this.y = (short) json.getInt("y");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    public Npc clone() {
        try {
            return (Npc) super.clone();
        } catch (Exception var2) {
            return null;
        }
    }
    public boolean isNpcTau() {
        return this.id == 40 || this.id == 42 || this.id == 43 || this.id == 44 || this.id == 45 || this.id == 46;
    }
}

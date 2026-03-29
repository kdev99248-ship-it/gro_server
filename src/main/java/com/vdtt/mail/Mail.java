package com.vdtt.mail;

import com.google.gson.JsonObject;
import com.vdtt.item.Item;
import com.vdtt.network.Message;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

@Getter
@Setter
public class Mail {
    public int id;
    public boolean isRecived;
    public String title;
    public String content;
    public String sender;
    public int ballz;
    public int coin;
    public int zeni;
    public int zeniLock;
    public long exp;
    public int time;
    public Item item;
    public Mail() {
    }
    public Mail(JSONObject obj){
        id = Integer.parseInt(obj.get("id").toString());
        isRecived = Boolean.parseBoolean(obj.get("isRecived").toString());
        title = obj.get("title").toString();
        content = obj.get("content").toString();
        sender = obj.get("sender").toString();
        ballz = Integer.parseInt(obj.get("ballz").toString());
        coin = Integer.parseInt(obj.get("coin").toString());
        zeni = Integer.parseInt(obj.get("zeni").toString());
        zeniLock = Integer.parseInt(obj.get("zeniLock").toString());
        exp = Long.parseLong(obj.get("exp").toString());
        time = Integer.parseInt(obj.get("time").toString());
        if(obj.containsKey("item")) {
            item = new Item((JSONObject) obj.get("item"));
        }
    }
    public void write(Message m) throws IOException {
        m.writer().writeShort(id);
        m.writer().writeBoolean(isRecived);
        m.writeUTF(sender);
        m.writeUTF(title);
        m.writeUTF(content);
        m.writer().writeInt(ballz);
        m.writer().writeInt(coin);
        m.writer().writeInt(zeni);
        m.writer().writeInt(zeniLock);
        m.writer().writeLong(exp);
        m.writer().writeInt(time);
        if(item != null) {
            item.write(m);
        }else {
            m.writer().writeShort(-1);
        }
    }
    public JSONObject toJson(){
        JSONObject job = new JSONObject();
        job.put("id", id);
        job.put("isRecived", isRecived);
        job.put("title", title);
        job.put("content", content);
        job.put("sender", sender);
        job.put("ballz", ballz);
        job.put("coin", coin);
        job.put("zeni", zeni);
        job.put("zeniLock", zeniLock);
        job.put("exp", exp);
        job.put("time", time);
        if(item != null) {
            job.put("item", item.toJSONObject());
        }
        return job;
    }
}

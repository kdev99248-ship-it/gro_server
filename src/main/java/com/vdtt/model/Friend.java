package com.vdtt.model;

import com.vdtt.util.ParseData;
import org.json.simple.JSONObject;

public class Friend {
    public byte type;
    public String name;
    public boolean isFriend;
    public Friend(String name, byte type, boolean isOnline) {
        this.name = name;
        this.type = type;
        this.isFriend = isOnline;
    }

    public Friend(JSONObject obj) {
        load(obj);
    }

    private void load(JSONObject obj) {
        ParseData parse = new ParseData(obj);
        this.type = parse.getByte("type");
        this.name = parse.getString("name");
        this.isFriend = parse.getBoolean("isFriend");
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("name", this.name);
        obj.put("type", this.type);
        obj.put("isFriend", this.isFriend);
        return obj;
    }
}

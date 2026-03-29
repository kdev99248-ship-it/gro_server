package com.vdtt.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Reward {
    public short type;
    public List<Integer>list=new ArrayList<>();
    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("type",type);
        JSONArray arr = new JSONArray();
        for (int i : list) {
            arr.add(i);
        }
        obj.put("list",arr);
        return obj;
    }
    public void fromJson(JSONObject obj){
        type = Short.parseShort(obj.get("type").toString());
        JSONArray arr = (JSONArray) obj.get("list");
        for (Object o : arr) {
            list.add(Integer.parseInt(o.toString()));
        }
    }
}

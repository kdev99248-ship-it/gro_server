package com.vdtt.skill;

import com.vdtt.item.ItemOption;
import org.json.simple.JSONObject;

public class SkillClan {
    public int id;
    public String name;
    public byte levelNeed;
    public String strOptions;
    public int price;
    public int timeUse = -1;
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("strOptions", this.strOptions);
        obj.put("timeUse", this.timeUse);
        return obj;
    }
    public ItemOption[] getOptions() {
        if (this.strOptions != null && this.strOptions.length() > 0) {
            String[] var1;
            ItemOption[] var2 = new ItemOption[(var1 = this.strOptions.split(";")).length];

            for(int var3 = 0; var3 < var1.length; ++var3) {
                var2[var3] = new ItemOption(var1[var3]);
            }

            return var2;
        } else {
            return null;
        }
    }
}

package com.vdtt.skill;

import com.vdtt.item.ItemOption;
import com.vdtt.util.ParseData;
import com.vdtt.util.Util;
import org.json.simple.JSONObject;

public final class Skill implements Cloneable {
    public int index;
    public short id;
    public short isSkillTemplate;
    public byte level;
    public short levelNeed;
    public short mpUsing;
    public int coolDown;
    public short dy;
    public short dx;
    public byte maxTarget;
    public long kiUpgrade;
    public String options;
    public long timeCoolDown;
    public boolean isSkillCaiTrang;

    public Skill clone() {
        try {
            return (Skill) super.clone();
        } catch (Exception var2) {
            return null;
        }
    }
    public Skill() {
    }
    public ItemOption[] getOptions() {
        if (this.options != null && !this.options.isEmpty()) {
            String[] var1;
            ItemOption[] var2 = new ItemOption[(var1 = Util.split(this.options, ";")).length];

            for (int var3 = 0; var3 < var1.length; ++var3) {
                var2[var3] = new ItemOption(var1[var3]);
            }

            return var2;
        } else {
            return null;
        }
    }

    public boolean isCoolDown() {
        long currentTimeMillis = System.currentTimeMillis();
        long num = currentTimeMillis - timeCoolDown;
        return num <= 0;
    }

    public ItemOption[] getItemOption() {
        if (this.options != null && this.options.length() > 0) {
            String[] var1;
            ItemOption[] var2 = new ItemOption[(var1 = Util.split(this.options, ";")).length];

            for (int var3 = 0; var3 < var1.length; ++var3) {
                var2[var3] = new ItemOption(var1[var3]);
            }

            return var2;
        } else {
            return null;
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("isSkillTemplate", this.isSkillTemplate);
        obj.put("level", this.level);
        obj.put("levelNeed", this.levelNeed);
        obj.put("mpUsing", this.mpUsing);
        obj.put("coolDown", this.coolDown);
        obj.put("rangeDoc", this.dy);
        obj.put("rangeNgang", this.dx);
        obj.put("maxTarget", this.maxTarget);
        obj.put("kiUpgrade", this.kiUpgrade);
        obj.put("timeCoolDown", this.timeCoolDown);
        obj.put("strOption", this.options);
        return obj;
    }
    public Skill (JSONObject obj) {
        ParseData parse = new ParseData(obj);
        this.id = (short) parse.getInt("id");
        this.isSkillTemplate = (short) parse.getInt("isSkillTemplate");
        this.level = (byte) parse.getInt("level");
        this.levelNeed = (short) parse.getInt("levelNeed");
        this.mpUsing = (short) parse.getInt("mpUsing");
        this.coolDown = parse.getInt("coolDown");
        this.dy = (short) parse.getInt("rangeDoc");
        this.dx = (short) parse.getInt("rangeNgang");
        this.maxTarget = (byte) parse.getInt("maxTarget");
        this.kiUpgrade = parse.getLong("kiUpgrade");
        this.timeCoolDown = parse.getLong("timeCoolDown");
        this.options = parse.getString("strOption");
    }

    public SkillTemplate getSkillTemplate() {
        return SkillFactory.getInstance().getSkillTemplate(isSkillTemplate);
    }
}

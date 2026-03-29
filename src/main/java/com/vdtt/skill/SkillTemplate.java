package com.vdtt.skill;

import com.vdtt.network.Message;

public class SkillTemplate {
    public short id;
    public String name;
    public String detail;
    public short levelNeed;
    public byte idChar;
    public byte levelMax;
    public byte type;
    public short idIcon;
    public short i;

    public SkillTemplate(int var1) {
        this.id = (short)var1;
    }

    public void write(Message m) {
        try {
            m.writeUTF(name);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

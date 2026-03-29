package com.vdtt.mob;

import com.vdtt.util.Util;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobTemplate {
    public short id;
    public short width;
    public short height;
    public short indexData;
    public short idIcon;
    public short timeThuHoach;
    public short i;
    public short j;
    public String name;
    public String detail;
    public short rangeMove;
    public byte type;
    public byte speedMoveByte;
    public byte typeMob2;
    public int[] m;
    public int[] n;
    public final void a(String var1, String var2) {
        try {
            String[] var5 = Util.a(var1, ",");
            this.m = new int[var5.length];

            int var3;
            for(var3 = 0; var3 < var5.length; ++var3) {
                this.m[var3] = Integer.parseInt(var5[var3]);
            }

            var5 = Util.a(var2, ",");
            this.n = new int[var5.length];

            for(var3 = 0; var3 < var5.length; ++var3) {
                this.n[var3] = Integer.parseInt(var5[var3]);
            }

        } catch (Exception var4) {
        }
    }
}

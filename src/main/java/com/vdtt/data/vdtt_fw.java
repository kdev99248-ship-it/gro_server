package com.vdtt.data;

import com.vdtt.model.Position;
import com.vdtt.util.Util;

import java.util.Comparator;

public class vdtt_fw {
    public static final Comparator b = new vdtt_fx();
    public static final Comparator c = new vdtt_fy();
    public Position[] d;
    public short e;
    public short f;
    public short g;
    public short h;

    public vdtt_fw() {
    }

    public vdtt_fw(Position[] var1) {
        this.d = var1;
    }

    public final vdtt_fw a() {
        vdtt_fw var1;
        (var1 = new vdtt_fw()).e = this.e;
        var1.f = this.f;
        var1.g = this.g;
        var1.h = this.h;
        var1.d = new Position[this.d.length];

        for(int var2 = 0; var2 < this.d.length; ++var2) {
            var1.d[var2] = new Position();
            var1.d[var2].setXY(this.d[var2].x, this.d[var2].y);
        }

        return var1;
    }

    public final Position a(Position var1, Position var2) {
        for(int var4 = 0; var4 < this.d.length - 1; ++var4) {
            Position var3;
            if ((var3 = Util.a(var1, var2, this.d[var4], this.d[var4 + 1])) != null) {
                return var3;
            }
        }

        return null;
    }

    public final boolean a(int var1, int var2) {
        return a(var1, var2, this.d);
    }

    private static boolean a(int var0, int var1, Position[] var2) {
        boolean var5 = false;
        int var3 = 0;

        for(int var4 = var2.length - 1; var3 < var2.length; var4 = var3++) {
            if ((var2[var3].y <= var1 && var1 < var2[var4].y || var2[var4].y <= var1 && var1 < var2[var3].y) && var0 < (var2[var4].x - var2[var3].x) * (var1 - var2[var3].y) / (var2[var4].y - var2[var3].y) + var2[var3].x) {
                var5 = !var5;
            }
        }

        return var5;
    }


    public Object clone() {
        return this.a();
    }
}


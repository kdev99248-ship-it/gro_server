package com.vdtt.model;

public class WayPoint extends ObjectLive {

    public short mapId;
    public short mapNext;
    public short m;
    public short n;
    public short o;
    public short p;
    public int H;
    private String[] L = null;
    public int I = 0;
    public int J = 0;
    public int K;

    public WayPoint(int var1, int var2) {
        this.setXY(0, 0);
    }

    public void a(short var1, short var2, short var3, short var4, short var5, short var6, short var7, short var8) {
        this.mapId = var1;
        this.mapNext = var2;
        this.m = (short)(var3 - 5);
        this.n = (short)(var4 - 5);
        this.o = (short)(var5 + 5);
        this.p = (short)(var6 + 5);
        this.setXY(var3 + (var5 - var3) / 2, var6);
        String var9;
//        if ((var9 = DataCenter.gI().mapTemplate[var2].name).length() <= 11) {
//            this.L = new String[]{var9};
//        } else {
//            this.L = vdtt_dd.a(var9, 2);
//        }

        this.I = var7;
        this.J = var8;
    }

    public void a(short var1, short var2, short var3, short var4, short var5, short var6) {
        this.mapId = var1;
        this.mapNext = var2;
        this.m = var3;
        this.n = var4;
        this.o = var5;
        this.p = var6;
        this.setXY(var3 + (var5 - var3) / 2, var4 + (var6 - var4) / 2);
    }
}

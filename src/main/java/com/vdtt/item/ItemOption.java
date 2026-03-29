package com.vdtt.item;

import com.vdtt.data.DataCenter;
import com.vdtt.data.vdtt_gx;
import com.vdtt.util.Util;

import java.util.Comparator;

public class ItemOption {
    public static final Comparator a = new vdtt_gx();

    public int[] option;

    public ItemOption(String var1) {
        String[] var3 = Util.a(var1, ",");
        this.option = new int[var3.length];

        for (int var2 = 0; var2 < var3.length; ++var2) {
            this.option[var2] = Integer.parseInt(var3[var2]);
        }

    }

    public ItemOption(int id, int value) {
        this.option = new int[]{id, value};
    }

    public ItemOption(int id, int value, int value2) {
        this.option = new int[]{id, value, value2};
    }

    public ItemOption(int id) {
        this.option = new int[]{id, 0};
    }

    public ItemOption(short id, int value, int i, int lv) {
        this.option = new int[]{id, value, i, lv};
    }

    public final ItemOptionTemplate getItemOptionTemplate() {
        return DataCenter.gI().itemOptionTemplate[this.option[0]];
    }
    
    public final int d(int var1) {
        int[] var10000 = this.option;
        return var10000[1] += var1;
    }
    public final String[] b() {
        if (this.getItemOptionTemplate().id != 344 && this.getItemOptionTemplate().id != 369 && this.getItemOptionTemplate().id != 375 && this.getItemOptionTemplate().id != 267 && this.getItemOptionTemplate().id != 343 && this.getItemOptionTemplate().id != 368 && this.getItemOptionTemplate().id != 412) {
            return null;
        } else {
            String var1 = "" + (float) this.option[1] / 10.0F;
            if (this.option[1] % 10 == 0) {
                var1 = "" + this.option[1] / 10;
            }

            String var2 = "" + (float) this.f() / 10.0F;
            if (this.f() % 10 == 0) {
                var2 = "" + this.f() / 10;
            }

            return new String[]{var1, var2};
        }
    }

    public final String a(int var1) {
        if (this.option[0] == 310) {
            return Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, DataCenter.gI().skillTemplate[this.option[1]].name);
        } else if (this.getItemOptionTemplate().type == 54) {
            return Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, Util.j(this.option[1]));
        } else if (this.getItemOptionTemplate().type == 52) {
            String var5 = Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, "" + this.option[1]);

            for (int var3 = 0; var3 < DataCenter.gI().skillTemplate.length; ++var3) {
                if (DataCenter.gI().skillTemplate[var3].idChar == var1 && DataCenter.gI().skillTemplate[var3].levelNeed == this.getItemOptionTemplate().level) {
                    String var4 = DataCenter.gI().skillTemplate[var3].name;
                    return var5.replaceAll("@", var4);
                }
            }

            return null;
        } else if (this.getItemOptionTemplate().id == 98) {
            return Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, "" + (float) this.option[1] / 100.0F);
        } else {
            String[] var2;
            return (var2 = this.b()) != null ? Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, var2[0]) : Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, "" + this.option[1]);
        }
    }


    public final String e() {
        return this.option[0] == 349 ? Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, "") : Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, Util.c(this.option[1])).replaceAll("@", Util.c(this.f()));
    }

    public final String b(int var1) {
        var1 = var1;
        if (this.option.length > 3) {
            var1 += this.option[3];
        }

        if ((var1 = this.option[1] + var1 / 10) > this.f()) {
            var1 = this.f();
        }

        return Util.b(DataCenter.gI().itemOptionTemplate[this.option[0]].text, "" + var1).replaceAll("@", "" + this.f());
    }

    public final int setValue(int value) {
        return this.option[1] = value;
    }

    public final int addValue(int var1) {
        return this.option[1] += var1;
    }

    public final int e(int var1) {
        return this.option[2] = var1;
    }

    public final int f() {
        return this.option.length < 3 ? -1 : this.option[2];
    }

    public final String g() {
        String string = "";

        for (int i = 0; i < this.option.length; ++i) {
            string = string + this.option[i];
            if (i < this.option.length - 1) {
                string = string + ",";
            }
        }

        return string;
    }

    public final boolean a(boolean var1) {
        try {
            return this.option[3] >= (var1 ? 17 : 16);
        } catch (Exception var2) {
            return false;
        }
    }

    public static int f(int var0) {
        switch (var0) {
            case 406:
                return 199;
            case 407:
                return 200;
            case 408:
                return 201;
            case 409:
                return 202;
            case 410:
                return 203;
            case 411:
                return 204;
            case 412:
                return 205;
            case 413:
                return 206;
            default:
                return -1;
        }
    }

    public final int h() {
        switch (this.getItemOptionTemplate().type) {
            case 3:
                return 793;
            case 4:
                return 795;
            case 5:
                return 797;
            case 6:
                return 798;
            case 7:
            case 8:
            case 9:
            case 10:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            default:
                switch (this.option[0]) {
                    case 199:
                        return 406;
                    case 200:
                        return 407;
                    case 201:
                        return 408;
                    case 202:
                        return 409;
                    case 203:
                        return 410;
                    case 204:
                        return 411;
                    case 205:
                        return 412;
                    case 206:
                        return 413;
                    default:
                        return -1;
                }
            case 11:
                return 794;
            case 12:
                return 796;
            case 17:
                return 791;
            case 19:
                return 792;
        }
    }

    public final int i() {
        try {
            return this.option[3];
        } catch (Exception var1) {
            return 0;
        }
    }

    public static ItemOption g(int var0) {
        return new ItemOption(f(var0) + ",0,-1,0");
    }

    public final boolean j() {
        return this.getItemOptionTemplate().type == 19 || this.getItemOptionTemplate().type == 3 || this.getItemOptionTemplate().type == 11 || this.getItemOptionTemplate().type == 4 || this.getItemOptionTemplate().type == 12 || this.getItemOptionTemplate().type == 5 || this.getItemOptionTemplate().type == 6 || this.getItemOptionTemplate().type == 7 || this.getItemOptionTemplate().type == 10;
    }

    public final boolean k() {
        return this.getItemOptionTemplate().id == 349 || this.getItemOptionTemplate().id == 350 || this.getItemOptionTemplate().id == 298;
    }

    public final boolean type8() {
        return this.getItemOptionTemplate().type == 8;
    }

    public final boolean typeKichSao() {
        return this.getItemOptionTemplate().type == 19 || this.getItemOptionTemplate().type == 3 || this.getItemOptionTemplate().type == 11 || this.getItemOptionTemplate().type == 4 || this.getItemOptionTemplate().type == 12 || this.getItemOptionTemplate().type == 5 || (this.getItemOptionTemplate().type == 6 && this.getItemOptionTemplate().id > 39) || this.getItemOptionTemplate().type == 7;
    }

    public final boolean typeChiSoGoc(){
        return this.getItemOptionTemplate().type == 17;
    }

    public final boolean typeChiSoKham(){
        return this.getItemOptionTemplate().type == 8;
    }

    public final boolean m() {
        return this.getItemOptionTemplate().type == 2 || this.getItemOptionTemplate().type == 50 || this.n();
    }

    public final boolean n() {
        return this.getItemOptionTemplate().type == 7;
    }

    public int getId() {
        return this.option[0];
    }

    public int getParam() {
        return this.option[1];
    }

    public int getParam2() {
        return this.option[2];
    }

    public void setParam(int i) {
        this.option[1] = i;
    }

    public void addParam(int i) {
        this.option[1] += i;
    }
}

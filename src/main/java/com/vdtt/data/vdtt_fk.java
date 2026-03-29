package com.vdtt.data;

import com.vdtt.map.MapTemplate;
import com.vdtt.model.Position;
import com.vdtt.util.Util;

import java.util.Vector;

public final class vdtt_fk {
    public Vector a = new Vector();
    public MapTemplate mapTemplate;
    public vdtt_fk(vdtt_fw[] var1, MapTemplate mapTemplate) {
        for(int var2 = 0; var2 < var1.length; ++var2) {
            this.a.add(var1[var2]);
        }
        this.mapTemplate = mapTemplate;

    }

    public final boolean a(int var1, int var2) {
        for(int var3 = 0; var3 < this.a.size(); ++var3) {
            if (((vdtt_fw)this.a.get(var3)).a(var1, var2)) {
                return true;
            }
        }

        return false;
    }

    public final Position a(Position var1, Position var2) {
        for(int var4 = 0; var4 < this.a.size(); ++var4) {
            Position var3;
            if ((var3 = ((vdtt_fw)this.a.get(var4)).a(var1, var2)) != null) {
                return var3;
            }
        }

        return null;
    }

    public final Position b(int var1, int var2) {
        if (!this.a(var1, var2)) {
            return Position.create(var1, var2);
        } else {
            for(int var3 = 1; var3 < 1000; ++var3) {
                if (!this.a(var1 + var3, var2)) {
                    return Position.create(var1 + var3, var2);
                }

                if (!this.a(var1, var2 + var3)) {
                    return Position.create(var1, var2 + var3);
                }

                if (!this.a(var1 - var3, var2)) {
                    return Position.create(var1 - var3, var2);
                }

                if (!this.a(var1, var2 - var3)) {
                    return Position.create(var1, var2 - var3);
                }

                if (!this.a(var1 + var3, var2 + var3)) {
                    return Position.create(var1 + var3, var2 + var3);
                }

                if (!this.a(var1 - var3, var2 + var3)) {
                    return Position.create(var1 - var3, var2 + var3);
                }

                if (!this.a(var1 + var3, var2 - var3)) {
                    return Position.create(var1 + var3, var2 - var3);
                }

                if (!this.a(var1 - var3, var2 - var3)) {
                    return Position.create(var1 - var3, var2 - var3);
                }
            }

            return Position.create(var1, var2);
        }
    }

    public Position c(int i, int i2) {
        Position a2;
        int i3 = 16;
        try {
            int i4 = i > mapTemplate.maxX + -17 ? mapTemplate.maxX - 17 : i;
            if (i4 >= 16) {
                i3 = i4;
            }
            Position create = Position.create(i3, i2);
            Position create2 = Position.create(create.x, mapTemplate.maxY + 10);
            for (int i5 = 0; i5 < mapTemplate.X.size(); i5++) {
                if (((vdtt_fw) mapTemplate.X.get(i5)).d[0].y >= create.y && (a2 = ((vdtt_fw) mapTemplate.X.get(i5)).a(create, create2)) != null) {
                    return a2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Position[] d(int var0, int var1) {
        if (var0 > mapTemplate.maxX - 17) {
            var0 = mapTemplate.maxX - 17;
        }

        if (var0 < 16) {
            var0 = 16;
        }

        Position var2 = Position.create(var0, var1 - 5);
        Position var4 = Position.create(var0, var1 + mapTemplate.maxY);

        for(var1 = 0; var1 < mapTemplate.dM.size(); ++var1) {
            try {
                if (((vdtt_fw)mapTemplate.dM.get(var1)).d[0].y >= var2.y && Util.a(((vdtt_fw)mapTemplate.dM.get(var1)).d[0], ((vdtt_fw)mapTemplate.dM.get(var1)).d[1], var2, var4) != null) {
                    return ((vdtt_fw)mapTemplate.dM.get(var1)).d;
                }
            } catch (Exception var3) {
            }
        }

        return null;
    }

    public Position[] a(int var0, int var1, int var2, int var3) {
        if (var0 > mapTemplate.maxX - 17) {
            var0 = mapTemplate.maxX - 17;
        }

        if (var0 < 16) {
            var0 = 16;
        }

        if (var2 > mapTemplate.maxX - 17) {
            var2 = mapTemplate.maxX - 17;
        }

        if (var2 < 16) {
            var2 = 16;
        }

        Position var6 = Position.create(var0, var1);
        Position var5 = Position.create(var2, var3);

        for(var2 = 0; var2 < mapTemplate.dM.size(); ++var2) {
            try {
                if (((vdtt_fw)mapTemplate.dM.get(var2)).d[0].y >= var6.y && Util.a(((vdtt_fw)mapTemplate.dM.get(var2)).d[0], ((vdtt_fw)mapTemplate.dM.get(var2)).d[1], var6, var5) != null) {
                    return ((vdtt_fw)mapTemplate.dM.get(var2)).d;
                }
            } catch (Exception var4) {
            }
        }

        return null;
    }
}

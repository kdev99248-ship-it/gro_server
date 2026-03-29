package com.vdtt.map;

import com.vdtt.data.*;
import com.vdtt.mob.Mob;
import com.vdtt.model.WayPoint;
import com.vdtt.model.Position;
import com.vdtt.npc.Npc;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MapTemplate {
    public short id;
    public String name;
    public short typeArr;
    public byte type;
    public byte e;
    public byte[] arrMap;
    public short maxX;
    public short maxY;
    public ArrayList<WayPoint> listWayPoint = new ArrayList();
    public ArrayList<WayPoint> listWayPointIn = new ArrayList();
    public List<Mob> mobList = new ArrayList<>();
    public List<Npc> npcList = new ArrayList<>();
    public Vector Q = new Vector();
    public Vector R = new Vector();
    private int cN;
    public vdtt_fk V;
    public Vector X = new Vector();
    public Vector dM = new Vector();
    public Vector dN = new Vector();
    public Hashtable dL = new Hashtable();

    public MapTemplate(int var1) {
        this.id = (short) var1;
    }

    public static int dK = 120;


    public void load() {
        try {
            byte[] array = arrMap;
            if (array == null || array.length == 0) {
                return;
            }
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(array));

            check(dis);
            this.maxX = dis.readShort();
            this.maxY = dis.readShort();
            this.cN = 0;
            this.Q.clear();
            new Vector();

            vdtt_fw[] var2;
            (var2 = new vdtt_fw[dis.readUnsignedByte() + 4])[var2.length - 1] = b(-50, -50, this.maxX + 50, 50);
            var2[var2.length - 2] = b(-50, -50, 15, this.maxY + 50);
            var2[var2.length - 3] = b(-50, this.maxY - 15, this.maxX + 50, this.maxY + 50);
            var2[var2.length - 4] = b(this.maxX - 15, -50, this.maxX + 50, this.maxY + 50);

            int var3;
            for (var3 = 0; var3 < var2.length - 4; ++var3) {
                var2[var3] = new vdtt_fw();
                var2[var3].d = new Position[dis.readUnsignedByte()];

                for (int var4 = 0; var4 < var2[var3].d.length; ++var4) {
                    var2[var3].d[var4] = new Position();
                    var2[var3].d[var4].setXY(dis.readShort(), dis.readShort());
                    if (var4 == 0) {
                        var2[var3].e = var2[var3].g = var2[var3].d[var4].x;
                        var2[var3].f = var2[var3].h = var2[var3].d[var4].y;
                    } else {
                        if (var2[var3].d[var4].x < var2[var3].e) {
                            var2[var3].e = var2[var3].d[var4].x;
                        }

                        if (var2[var3].d[var4].x > var2[var3].g) {
                            var2[var3].g = var2[var3].d[var4].x;
                        }

                        if (var2[var3].d[var4].y < var2[var3].f) {
                            var2[var3].f = var2[var3].d[var4].y;
                        }

                        if (var2[var3].d[var4].y > var2[var3].h) {
                            var2[var3].h = var2[var3].d[var4].y;
                        }
                    }
                }
            }

            this.V = new vdtt_fk(var2, this);
            vdtt_fv[] var29 = new vdtt_fv[dis.readShort()];
            Vector var30 = new Vector();
            Vector var26 = new Vector();

            int var5;
            int var6;
            for (var5 = 0; var5 < var29.length; ++var5) {
                var29[var5] = new vdtt_fv();
                var29[var5].a = dis.readBoolean();
                if (var29[var5].a) {
                    var26.add(var29[var5]);
                } else {
                    var30.add(var29[var5]);
                }

                var29[var5].d = new Position[dis.readShort()];

                for (var6 = 0; var6 < var29[var5].d.length; ++var6) {
                    var29[var5].d[var6] = new Position();
                    var29[var5].d[var6].setXY(dis.readShort(), dis.readShort());
                }
            }

            this.dL.clear();
            this.dM.clear();
            this.dN.clear();

            vdtt_fv var32;
            for (var5 = 0; var5 < var26.size(); ++var5) {
                if ((var32 = (vdtt_fv) var26.get(var5)).d.length > 2) {
                    V.a.add(new vdtt_fw(var32.d));

                    try {
                        for (var3 = 0; var3 < var32.d.length; ++var3) {
                            if (var32.d[var3].y != var32.d[var3 + 1].y) {
                                if (var32.d[var3].y < var32.d[var3 + 1].y) {
                                    this.dN.add(new vdtt_fw(new Position[]{var32.d[var3], var32.d[var3 + 1]}));
                                } else {
                                    this.dN.add(new vdtt_fw(new Position[]{var32.d[var3 + 1], var32.d[var3]}));
                                }

                                ++var3;
                            }
                        }
                    } catch (Exception var23) {
                    }
                } else {
                    for (var3 = 0; var3 < var32.d.length; ++var3) {
                        Position var7 = var32.d[var3];
                        if (V.a(var7.x, var7.y)) {
                            Position var8 = V.b(var7.x, var7.y);
                            var32.d[var3].x = var8.x;
                            var32.d[var3].y = var8.y;
                        }
                    }
                }
            }

            var29 = new vdtt_fv[var30.size()];

            for (var5 = 0; var5 < var30.size(); ++var5) {
                var29[var5] = (vdtt_fv) var30.get(var5);
                if (Util.b(var29[var5].d[0].y - var29[var5].d[1].y) > dK) {
                    if (var29[var5].d[0].y > var29[var5].d[1].y) {
                        this.a(var29[var5].d[1], var29[var5].d[0], true, this.dL);
                    } else {
                        this.a(var29[var5].d[0], var29[var5].d[1], true, this.dL);
                    }
                } else {
                    this.a(var29[var5].d[0], var29[var5].d[1], true, this.dL);
                    this.a(var29[var5].d[1], var29[var5].d[0], true, this.dL);
                }
            }

            Iterator var33 = this.dL.keySet().iterator();

            while (var33.hasNext()) {
                String var34 = (String) var33.next();
                ((vdtt_gn) this.dL.get(var34)).b = new Vector();
                ((vdtt_gn) this.dL.get(var34)).c = new Vector();
                ((vdtt_gn) this.dL.get(var34)).d = new Vector();
                ((vdtt_gn) this.dL.get(var34)).e = new Vector();
                ((vdtt_gn) this.dL.get(var34)).f = new Vector();
                ((vdtt_gn) this.dL.get(var34)).e.addAll(((vdtt_gn) this.dL.get(var34)).a);
                ((vdtt_gn) this.dL.get(var34)).f.addAll(((vdtt_gn) this.dL.get(var34)).a);
                Collections.sort(((vdtt_gn) this.dL.get(var34)).e);
                Collections.sort(((vdtt_gn) this.dL.get(var34)).f, Collections.reverseOrder());

                for (var3 = ((vdtt_gn) this.dL.get(var34)).a.size() - 1; var3 >= 0; --var3) {
                    ((vdtt_gn) this.dL.get(var34)).b.add(((vdtt_gn) this.dL.get(var34)).a.get(var3));
                }

                ((vdtt_gn) this.dL.get(var34)).c.addAll(((vdtt_gn) this.dL.get(var34)).a);
                String var35;
                if (((vdtt_gn) this.dL.get(var34)).c.size() > 0) {
                    var35 = (String) ((vdtt_gn) this.dL.get(var34)).c.get(0);
                    ((vdtt_gn) this.dL.get(var34)).c.removeElementAt(0);
                    ((vdtt_gn) this.dL.get(var34)).c.add(var35);
                }

                ((vdtt_gn) this.dL.get(var34)).d.addAll(((vdtt_gn) this.dL.get(var34)).b);
                if (((vdtt_gn) this.dL.get(var34)).d.size() > 0) {
                    var35 = (String) ((vdtt_gn) this.dL.get(var34)).d.get(0);
                    ((vdtt_gn) this.dL.get(var34)).d.removeElementAt(0);
                    ((vdtt_gn) this.dL.get(var34)).d.add(var35);
                }
            }

            this.X.clear();

            for (var5 = 0; var5 < var26.size(); ++var5) {
                var32 = (vdtt_fv) var26.get(var5);

                try {
                    for (var3 = 0; var3 < var32.d.length; var3 += 2) {
                        vdtt_fw var36;
                        (var36 = new vdtt_fw()).d = new Position[2];
                        var36.d[0] = var32.d[var3];
                        var36.d[1] = var32.d[var3 + 1];
                        if (var36.d[0].y == var36.d[1].y && var36.d[0].y < this.maxY - 20) {
                            this.X.add(var36);
                        }
                    }
                } catch (Exception var22) {
                }
            }

            Collections.sort(this.dN, vdtt_fw.c);
            Collections.sort(this.dM, vdtt_fw.b);
            Collections.sort(this.X, vdtt_fw.b);
        } catch (Exception ex) {
            Log.debug("error load map " + id);
        }
    }

    public final void a(Position var1, Position var2, boolean var3, Hashtable var4) {
        if (!var3 || var1.x == var2.x || var1.y == var2.y || this.V.a(var1, var2) == null) {
            String var5 = var1.x + "_" + var1.y;
            String var6 = var2.x + "_" + var2.y;
            if (!var5.equals(var6)) {
                vdtt_gn var7;
                if ((var7 = (vdtt_gn) var4.get(var5)) == null) {
                    var7 = new vdtt_gn();
                }

                if (!var7.a.contains(var6)) {
                    var7.a.add(var6);
                    if (var3 && var1.y == var2.y) {
                        this.dM.add(new vdtt_fw(new Position[]{var1, var2}));
                    }
                }

                var4.put(var5, var7);
            }
        }
    }

    private static vdtt_fw b(int var0, int var1, int var2, int var3) {
        vdtt_fw var4;
        (var4 = new vdtt_fw()).d = new Position[5];
        var4.d[0] = Position.create(var0, var1);
        var4.d[1] = Position.create(var2, var1);
        var4.d[2] = Position.create(var2, var3);
        var4.d[3] = Position.create(var0, var3);
        var4.d[4] = Position.create(var0, var1);
        return var4;
    }

    public void check(DataInputStream dis) throws IOException {
        int size = dis.readUnsignedShort();
        for (int i = 0; i < size; i++) {
            dis.readShort();
            dis.readShort();
            dis.readByte();
            dis.readShort();
            dis.readShort();
            int size2 = dis.readUnsignedByte();
            for (int j = 0; j < size2; ++j) {
                int size3 = dis.readUnsignedByte();
                for (int k = 0; k < size3; ++k) {
                    int size4 = dis.readByte() / 2;
                    for (int l = 0; l < size4; ++l) {
                        dis.readShort();
                        dis.readShort();
                    }
                }
            }
        }
        size = dis.readUnsignedShort();
        for (int i = 0; i < size; i++) {
            dis.readShort();
            dis.readShort();
            int size2 = dis.readUnsignedByte();
            for (int j = 0; j < size2; ++j) {
                dis.readShort();
                dis.readShort();
                dis.readShort();
                dis.readByte();
            }
            size2 = dis.readUnsignedByte();
            for (int j = 0; j < size2; ++j) {
                int size3 = dis.readUnsignedByte();
                for (int k = 0; k < size3; ++k) {
                    int size4 = dis.readByte() / 2;
                    for (int l = 0; l < size4; ++l) {
                        dis.readShort();
                        dis.readShort();
                    }
                }
            }
        }

    }

    public void createWayPoint() {
        try {
            for (int var10 = 0; var10 < DataCenter.gI().dataWayPoint.length; ++var10) {
                WayPoint var11;
                if (DataCenter.gI().dataWayPoint[var10][0] == this.id) {
                    (var11 = new WayPoint(0, 0)).a(DataCenter.gI().dataWayPoint[var10][0], DataCenter.gI().dataWayPoint[var10][5], DataCenter.gI().dataWayPoint[var10][1], DataCenter.gI().dataWayPoint[var10][2], DataCenter.gI().dataWayPoint[var10][3], DataCenter.gI().dataWayPoint[var10][4], DataCenter.gI().dataWayPoint[var10][10], DataCenter.gI().dataWayPoint[var10][11]);
                    this.listWayPoint.add(var11);
                } else if (DataCenter.gI().dataWayPoint[var10][5] == this.id) {
                    (var11 = new WayPoint(0, 0)).a(DataCenter.gI().dataWayPoint[var10][5], DataCenter.gI().dataWayPoint[var10][0], DataCenter.gI().dataWayPoint[var10][6], DataCenter.gI().dataWayPoint[var10][7], DataCenter.gI().dataWayPoint[var10][8], DataCenter.gI().dataWayPoint[var10][9], DataCenter.gI().dataWayPoint[var10][12], DataCenter.gI().dataWayPoint[var10][13]);
                    this.listWayPoint.add(var11);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadData() {
        try {
            byte[] dataJsonMob = Util.read("data\\DataMap\\Mob\\" + this.id + ".bin");
            byte[] dataJsonNpc = Util.read("data\\DataMap\\Npc\\" + this.id + ".bin");
            if (dataJsonMob != null) {
                JSONArray jsonArray = new JSONArray(new String(dataJsonMob, "UTF-8"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    Mob mob = new Mob();
                    mob.readJson(json);
                    if (mob.id >= 199 && mob.id <= 203)
                        continue;
                    mobList.add(mob);
                }
            }
            if (dataJsonNpc != null) {
                JSONArray jsonArray = new JSONArray(new String(dataJsonNpc, "UTF-8"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    Npc npc = new Npc();
                    npc.readJson(json);
                    npcList.add(npc);
                }
            }
        } catch (Exception ex) {

        }

    }

    public void loadData2() {
        try {
            String path = "data/DataMap/Mob/" + id + ".json";
            if (!Files.exists(Paths.get(path)))
                return;
            String content = new String(Files.readAllBytes(Paths.get(path)));
            if (content == null || content.isEmpty()) {
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            mobList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                int id = json.getInt("id");
                int levelBoss = json.getInt("levelBoss");
                int status = json.getInt("status");
                int x = json.getInt("x");
                int y = json.getInt("y");
                boolean paintMiniMap = json.getBoolean("paintMiniMap");
                int hpFull = json.getInt("hpFull");
                int level = json.getInt("level");
                Mob mob = new Mob();
                mob.id = id;
                if (mob.id >= 199 && mob.id <= 203)
                    continue;
                mob.levelBoss = levelBoss;
                mob.status = (byte) status;
                mob.x = x;
                mob.y = y;
                mob.isPaint = paintMiniMap;
                mob.hp = mob.hpFull = hpFull;
                mob.level = level;
                mob.loadKiNow();
                mobList.add(mob);
            }
            path = "data/DataMap/Npc/" + id + ".json";
            if (!Files.exists(Paths.get(path)))
                return;
            content = new String(Files.readAllBytes(Paths.get(path)));
            if (content == null || content.isEmpty()) {
                return;
            }
            npcList.clear();
            jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                int id = json.getInt("id");
                int status = json.getInt("status");
                int x = json.getInt("cx");
                int y = json.getInt("cy");
                Npc npc = new Npc();
                npc.id = id;
                npc.status = status;
                npc.x = (short) x;
                npc.y = (short) y;
                npcList.add(npc);
                // Tiếp tục xử lý dữ liệu tại đây...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPhoBan() {
        return this.type == 4 || this.type == 14 || this.type == 15 || this.type == 5 || this.type == 7 || this.type == 8 || this.type == 6 || this.type == 9 || this.type == 10 || this.type == 12 || this.type == 13 || this.type == 16 || this.type == 17 || this.type == 19 || this.type == 20 || this.type == 21 || this.type == 23 || this.type == 24;
    }

}

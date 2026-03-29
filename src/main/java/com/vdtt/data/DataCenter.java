package com.vdtt.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vdtt.item.ItemOptionTemplate;
import com.vdtt.map.MapManager;
import com.vdtt.map.MapTemplate;
import com.vdtt.mob.Mob;
import com.vdtt.mob.MobTemplate;
import com.vdtt.model.Position;
import com.vdtt.model.WayPoint;
import com.vdtt.network.Message;
import com.vdtt.npc.Npc;
import com.vdtt.npc.NpcTemplate;
import com.vdtt.skill.Skill;
import com.vdtt.skill.SkillClan;
import com.vdtt.skill.SkillTemplate;
import com.vdtt.task.Step;
import com.vdtt.task.TaskTemplate;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DataCenter {
    private static DataCenter aU;
    public ItemOptionTemplate[] itemOptionTemplate;
    public TaskTemplate[] TaskTemplate;

    public static DataCenter gI() {
        if (DataCenter.aU == null) {
            DataCenter.aU = new DataCenter();
        }
        return DataCenter.aU;
    }

    public SkillClan[] skillClans;
    public short[][] dataWayPoint;
    public Skill[] skill;
    public SkillTemplate[] skillTemplate;
    public MobTemplate[] mobTemplate;
    public NpcTemplate[] npcTemplate;
    public int[][] dataQuaySo = {
            {1, 3, 10, 30, 100, 300, 1000},
            {10000, 30000, 100000, 300000, 1000000, 3000000, 10000000},
            {45000, 135000, 450000, 1350000, 4500000, 13500000, 45000000},
            {4, 12, 40, 120, 400, 1200, 4000},
            {1, 3, 10, 30, 100, 300, 1000},
            {1, 3, 10, 30, 100, 300, 1000},
            {10000, 30000, 100000, 300000, 1000000, 3000000, 10000000},
            {45000, 135000, 450000, 1350000, 4500000, 13500000, 45000000},
            {4, 12, 40, 120, 400, 1200, 4000},
            {1, 3, 10, 30, 100, 300, 1000}
    };
    public long[] UP_CRYSTAL = new long[]{1, 5, 25, 125, 625, 3125, 15625, 78125, 390625, 1953125};
    public long[] at = new long[]{0, 270, 630, 1980, 9400, 25920, 40800, 84480, 414720, 533760, 806400, 1620480, 12026880, 17031160, 26431480, 40831800, 45232120};
    public long[] au = new long[]{0, 270, 630, 1980, 9400, 25920, 40800, 84480, 414720, 533760, 806400, 1620480, 12026880, 17031160, 26431480, 40831800, 45232120};
    public long[] av = new long[]{0, 270, 630, 1980, 9400, 25920, 40800, 84480, 414720, 533760, 806400, 1620480, 12026880, 17031160, 26431480, 40831800, 45232120};
    public int[] am = new int[]{4, 5, 6, 7, 13, 14, 86, 87, 92, 211, 9, 10, 15, 16, 17, 18, 11, 126, 127, 149, 170, 171, 207, 208, 209, 210};
    public int[] COIN_UP_CRYSTAL = new int[]{0, 49, 240, 1176, 5762, 28233, 138341, 677870, 3321563, 16275658};
    public int[] ao = new int[]{0, 1512, 3528, 11088, 52640, 145152, 228480, 473088, 2322432, 2989056, 4515840, 9074688, 39216656, 52985856, 82231296, 120722176, 173408681};
    public int[] ap = new int[]{0, 1512, 3528, 11088, 52640, 145152, 228480, 473088, 2322432, 2989056, 4515840, 9074688, 39216656, 52985856, 82231296, 120722176, 173408681};
    public int[] aq = new int[]{0, 1512, 3528, 11088, 52640, 145152, 228480, 473088, 2322432, 2989056, 4515840, 9074688, 39216656, 52985856, 82231296, 120722176, 173408681};
    public int[] ar = new int[]{0, 250, 500, 750, 1000, 1250, 1500, 1750, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 6000, 7000, 8000};
    public int[] dataTypeBody = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    public long[] expTemp = new long[]{0, 300, 600, 900, 1200, 1500, 1800, 2100, 2400, 2700, 3000, 6000, 9000, 12000, 15000, 18000, 21000, 24000, 27000, 30000, 60000, 90000, 120000, 150000, 180000, 210000, 240000, 270000, 300000, 600000, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000, 20000000, 40000000, 60000000, 80000000, 100000000, 120000000, 140000000, 160000000, 180000000, 200000000, 500000000, 1000000000, 1500000000, 2000000000, 2500000000l, 3000000000l, 3500000000l, 4000000000l, 4500000000l, 5000000000l, 6000000000l, 7000000000l, 8000000000l, 9000000000l, 10000000000l, 11000000000l, 12000000000l, 13000000000l, 14000000000l, 15000000000l, 16000000000l, 17000000000l, 18000000000l, 19000000000l, 20000000000l, 21000000000l, 22000000000l, 23000000000l, 24000000000l, 25000000000l, 26000000000l, 27000000000l, 28000000000l, 29000000000l, 30000000000l, 31000000000l, 32000000000l, 33000000000l, 34000000000l, 35000000000l, 36000000000l, 37000000000l, 38000000000l, 39000000000l, 40000000000l, 41000000000l, 42000000000l, 43000000000l, 44000000000l, 45000000000l, 46000000000l};
    public byte[] data2;


    public static void exportTasks(TaskTemplate[] tasks) {
        JSONArray arr = new JSONArray();

        for (TaskTemplate t : tasks) {
            JSONObject obj = new JSONObject();

            obj.put("id", t.id);
            obj.put("name", t.name);
            obj.put("level_need", t.level_need);
            obj.put("npcid", t.npcid);
            obj.put("mapid", t.mapid);
            obj.put("x", t.x);
            obj.put("y", t.y);

            obj.put("str1", t.str1);
            obj.put("str2", t.str2);
            obj.put("str3", t.str3);

            obj.put("ext", t.ext);
            obj.put("ballz", t.ballz);
            obj.put("zeni", t.zeni);
            obj.put("xu", t.xu);

            obj.put("strItem", t.strItem);

            // 🔥 steps
            JSONArray stepsArr = new JSONArray();

            for (Step s : t.steps) {
                JSONObject stepObj = new JSONObject();

                stepObj.put("id", s.id);
                stepObj.put("name", s.name);
                stepObj.put("itemId", s.itemId);
                stepObj.put("npcId", s.npcId);
                stepObj.put("mobId", s.mobId);
                stepObj.put("mapId", s.mapId);
                stepObj.put("x", s.x);
                stepObj.put("y", s.y);
                stepObj.put("require", s.require);
                stepObj.put("str", s.str);
                stepObj.put("strItem", s.strItem);

                stepsArr.add(stepObj);
            }

            obj.put("steps", stepsArr);

            arr.add(obj);
        }

        saveJson(arr, "data/tasks.json");
        Log.info("Loaded [" + arr.size() + "] tasks");
    }


    public static void saveJson(JSONArray data, String fileName) {
        try (FileWriter file = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            file.write(data.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public void loadData() {
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(Util.c(Util.getFile("data/arr_data_game.bin"))));
            int size = dis.readByte();
            for (int i = 0; i < size; i++) {
                dis.readShort();
            }
            size = dis.readByte();
            for (int i = 0; i < size; i++) {
                readUTF(dis);
            }
            size = dis.readByte();
            for (int i = 0; i < size; i++) {
                readUTF(dis);
                dis.readByte();
                dis.readShort();
            }
            size = dis.readUnsignedByte();
            for (int i = 0; i < size; i++) {
                dis.readByte();
                readUTF(dis);
                dis.readInt();
                dis.readInt();
                dis.readInt();
                dis.readInt();
                dis.readInt();
                readUTF(dis);
            }
            size = dis.readShort();
            TaskTemplate = new TaskTemplate[size];
            for (int i = 0; i < size; i++) {
                TaskTemplate taskTemplate = new TaskTemplate();
                taskTemplate.id = i;
                taskTemplate.name = readUTF(dis);
                taskTemplate.level_need = dis.readShort();
                taskTemplate.npcid = dis.readShort();
                taskTemplate.mapid = dis.readShort();
                taskTemplate.x = dis.readShort();
                taskTemplate.y = dis.readShort();
                taskTemplate.str1 = readUTF(dis);
                taskTemplate.str2 = readUTF(dis);
                taskTemplate.str3 = readUTF(dis);
                taskTemplate.ext = dis.readInt();
                taskTemplate.ballz = dis.readInt();
                taskTemplate.zeni = dis.readInt();
                taskTemplate.xu = dis.readInt();
                taskTemplate.strItem = readUTF(dis);
                int size1 = dis.readByte();
                taskTemplate.steps = new ArrayList<>();
                for (int j = 0; j < size1; j++) {
                    Step step = new Step();
                    step.id = dis.readByte();
                    step.name = readUTF(dis);
                    step.itemId = dis.readShort();
                    step.npcId = dis.readShort();
                    step.mobId = dis.readShort();
                    step.mapId = dis.readShort();
                    step.x = dis.readShort();
                    step.y = dis.readShort();
                    step.require = dis.readShort();
                    step.str = readUTF(dis);
                    step.strItem = readUTF(dis);
                    taskTemplate.steps.add(step);
                }
                TaskTemplate[i] = taskTemplate;
            }
            exportTasks(TaskTemplate);
            size = dis.readUnsignedByte();
            for (int i = 0; i < size; i++) {
                dis.readByte();
                readUTF(dis);
                dis.readShort();
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                MapTemplate mapTemplate = new MapTemplate(i);
                mapTemplate.name = readUTF(dis);
                mapTemplate.typeArr = (short) dis.readUnsignedByte();
                mapTemplate.type = dis.readByte();
                mapTemplate.e = dis.readByte();
                byte[] arrMap = new byte[dis.readInt()];
                dis.read(arrMap);
                mapTemplate.arrMap = arrMap;
                mapTemplate.load();
                MapManager.getInstance().mapTemplates.add(mapTemplate);
            }
            exportMaps(MapManager.getInstance().mapTemplates);
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                ItemOptionTemplate itemOptionTemplate = new ItemOptionTemplate();
                itemOptionTemplate.text = readUTF(dis);
                itemOptionTemplate.type = dis.readByte();
                itemOptionTemplate.level = dis.readByte();
                itemOptionTemplate.strOption = readUTF(dis);
            }
            // effect
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                readUTF(dis);
                readUTF(dis);
                dis.readUnsignedByte();
                dis.readShort();
                dis.readShort();
            }
            size = dis.readShort();// itemtemplate
            for (int i = 0; i < size; i++) {
                readUTF(dis);
                readUTF(dis);
                dis.readBoolean();
                dis.readByte();
                dis.readByte();
                dis.readByte();
                dis.readShort();
                dis.readUnsignedByte();
                dis.readUnsignedShort();
                dis.readShort();
                dis.readShort();
            }
            size = dis.readShort();// mobtemplate
            mobTemplate = new MobTemplate[size];
            for (int i = 0; i < size; i++) {
                MobTemplate mobTemplate1 = new MobTemplate();
                mobTemplate1.timeThuHoach = dis.readShort();
                mobTemplate1.name = readUTF(dis);
                mobTemplate1.detail = readUTF(dis);
                mobTemplate1.rangeMove = dis.readShort();
                mobTemplate1.type = dis.readByte();
                mobTemplate1.speedMoveByte = dis.readByte();
                mobTemplate1.typeMob2 = dis.readByte();
                mobTemplate1.indexData = dis.readShort();
                mobTemplate1.i = dis.readShort();
                mobTemplate1.a(readUTF(dis), readUTF(dis));
                mobTemplate[i] = mobTemplate1;
            }
            size = dis.readShort();// NPC TEMPLATE
            npcTemplate = new NpcTemplate[size];
            for (int i = 0; i < size; i++) {
                NpcTemplate npcTemplate1 = new NpcTemplate();
                npcTemplate1.id = (short) i;
                npcTemplate1.name = readUTF(dis);
                npcTemplate1.detail = readUTF(dis);
                npcTemplate1.idDataIcon = dis.readShort();
                npcTemplate1.hp = dis.readInt();
                npcTemplate1.mp = dis.readInt();
                npcTemplate1.i = dis.readShort();
                npcTemplate[i] = npcTemplate1;
            }
            size = dis.readShort();// skill template
            skillTemplate = new SkillTemplate[size];
            for (int i = 0; i < size; i++) {
                SkillTemplate skillTemplate1 = new SkillTemplate(i);
                skillTemplate1.name = readUTF(dis);
                skillTemplate1.detail = readUTF(dis);
                skillTemplate1.levelNeed = dis.readShort();
                skillTemplate1.idChar = dis.readByte();
                skillTemplate1.levelMax = dis.readByte();
                skillTemplate1.type = dis.readByte();
                skillTemplate1.idIcon = dis.readShort();
                skillTemplate1.i = dis.readShort();
                skillTemplate[i] = skillTemplate1;
            }
            size = dis.readShort();// skill
            skill = new Skill[size];
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < size; i++) {
                Skill skill1 = new Skill();
                skill1.id = dis.readShort();
                skill1.isSkillTemplate = dis.readShort();
                skill1.level = dis.readByte();
                skill1.levelNeed = (short) dis.readUnsignedByte();
                skill1.mpUsing = dis.readShort();
                skill1.coolDown = dis.readInt();
                skill1.dy = dis.readShort();
                skill1.dx = dis.readShort();
                skill1.maxTarget = dis.readByte();
                skill1.kiUpgrade = dis.readLong();
                skill1.options = readUTF(dis);
                skill[i] = skill1;
                jsonArray.add(skill1.toJSONObject());
            }
            size = dis.readUnsignedByte();// skillclan teamplate
            skillClans = new SkillClan[size];
            for (int i = 0; i < size; i++) {
                SkillClan skillClan = new SkillClan();
                skillClan.id = i;
                skillClan.name = readUTF(dis);
                readUTF(dis);
                skillClan.levelNeed = (byte) dis.readUnsignedByte();
                skillClan.strOptions = readUTF(dis);
                dis.readShort();
                skillClan.price = dis.readInt();
                dis.readInt();
                skillClans[i] = skillClan;
            }
            size = dis.readByte();// datatybe itembody
            for (int i = 0; i < size; i++) {
                dis.readByte();
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                int size1 = dis.readShort();
                for (int j = 0; j < size1; j++) {
                    dis.readShort();
                    dis.readUnsignedByte();
                    dis.readUnsignedByte();
                    dis.readShort();
                    dis.readShort();
                }
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                dis.readShort();
                dis.readShort();
                dis.readShort();
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                int size1 = dis.readShort();
                for (int j = 0; j < size1; j++) {
                    dis.readShort();
                    dis.readUnsignedByte();
                    dis.readUnsignedByte();
                    dis.readShort();
                    dis.readShort();
                }
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
//                System.out.println("id: " + i);
//                System.out.println("idIcon: " + dis.readShort());
//                System.out.println("x: " + dis.readShort());
//                System.out.println("y: " + dis.readShort());
                dis.readShort();
                dis.readShort();
                dis.readShort();
            }
            size = dis.readUnsignedByte();
            for (int i = 0; i < size; i++) {
                dis.readByte();
                int size1 = dis.readUnsignedByte();
                for (int j = 0; j < size1; j++) {
                    dis.readShort();
                    dis.readShort();
                    dis.readShort();
                    dis.readByte();
                }
            }
            size = dis.readByte();
            for (int i = 0; i < size; i++) {
                int siz1 = dis.readByte();
                for (int j = 0; j < siz1; j++) {
                    dis.readByte();
                }
            }
//            ghép part
            dis.readShort();
            dis.readShort();
            int size2 = dis.readUnsignedByte();
            size = dis.readShort();
//            JsonArray items = new JsonArray();
//
//            try {
//                for (int i = 0; i < size; i++) {
//                    JsonObject item = new JsonObject();
//                    item.addProperty("id", i);
//                    item.addProperty("byte", dis.readByte()); // Đọc byte
//
//                    JsonArray icons = new JsonArray();
//                    for (int j = 0; j < size2; j++) {
//                        int var = dis.readShort();
//                        if (var != 0) {
//                            JsonObject icon = new JsonObject();
//                            icon.addProperty("idIcon", var);
//                            icon.addProperty("x1", dis.readByte());
//                            icon.addProperty("x2", dis.readShort());
//                            icon.addProperty("x3", dis.readShort());
//                            icon.addProperty("x4", dis.readShort());
//                            icon.addProperty("x5", dis.readBoolean());
//                            icons.add(icon);
//                        }
//                    }
//                    item.add("icons", icons);  // Thêm danh sách icons vào item
//                    items.add(item);           // Thêm item vào danh sách
//                }
//
//                Gson gson = new Gson();
//                try (FileWriter writer = new FileWriter("output_part.json")) {
//                    gson.toJson(items, writer);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            part
            for (int i = 0; i < size; i++) {
                dis.readByte();
                for (int j = 0; j < size2; j++) {
                    int var = dis.readShort();
                    if (var != 0) {
                        dis.readByte();
                        dis.readShort();
                        dis.readShort();
                        dis.readShort();
                        dis.readBoolean();
                    }
                }
            }
//            liên kết part
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                int size1 = dis.readByte();
                for (int j = 0; j < size1; j++) {
                    dis.readShort();
                }
                int size3 = dis.readByte();
                for (int j = 0; j < size3; j++) {
                    dis.readShort();
                }
            }
            size = dis.readShort();
//            effect
            for (int i = 0; i < size; i++) {
                String[] split2;
                String[] split = (split2 = readUTF(dis).split(";"))[0].split(",");
                readUTF(dis);
                readUTF(dis);
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                dis.readByte();
                dis.readByte();
                dis.readByte();
                dis.readByte();
                dis.readShort();
                dis.readBoolean();
                dis.readBoolean();
                dis.readShort();
                dis.readShort();
                dis.readShort();
                dis.readShort();
                int size1 = dis.readByte();
                for (int j = 0; j < size1; j++) {
                    int size3 = dis.readByte();
                    for (int k = 0; k < size3; k++) {
                        dis.readShort();
                    }
                }
                size1 = dis.readByte();
                for (int j = 0; j < size1; j++) {
                    int size3 = dis.readByte();
                    for (int k = 0; k < size3; k++) {
                        dis.readShort();
                    }
                }
            }
            size = dis.readShort();// kiem tra toi day
            for (int i = 0; i < size; i++) {
                dis.readByte();
                int size1 = dis.readUnsignedByte();
                for (int j = 0; j < size1; j++) {
                    dis.readShort();
                    dis.readShort();
                    dis.readShort();
                    dis.readShort();
                    boolean var = dis.readBoolean();
                }
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                dis.readByte();
                int size1 = dis.readUnsignedByte();
                for (int j = 0; j < size1; j++) {
                    dis.readShort();
                    dis.readByte();
                    dis.readShort();
                    dis.readShort();
                }
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                dis.readShort();
                dis.readShort();
                dis.readShort();
                dis.readByte();
                int size1 = dis.readUnsignedByte();
                for (int j = 0; j < size1; j++) {
                    int size3 = dis.readUnsignedByte();
                    for (int k = 0; k < size3; k++) {
                        dis.readUnsignedByte();
                        dis.readShort();
                        dis.readShort();
                        dis.readShort();
                        dis.readShort();
                        dis.readShort();
                        dis.readShort();
                    }
                }
            }
            short[][] var2 = new short[dis.readShort()][14];
            for (int var3 = 0; var3 < var2.length; ++var3) {
                var2[var3][0] = dis.readShort();
                var2[var3][1] = dis.readShort();
                var2[var3][2] = dis.readShort();
                var2[var3][3] = dis.readShort();
                var2[var3][4] = dis.readShort();
                var2[var3][10] = dis.readShort();
                var2[var3][11] = dis.readShort();

                var2[var3][5] = dis.readShort();
                var2[var3][6] = dis.readShort();
                var2[var3][7] = dis.readShort();
                var2[var3][8] = dis.readShort();
                var2[var3][9] = dis.readShort();
                var2[var3][12] = dis.readShort();
                var2[var3][13] = dis.readShort();
            }

            if (dataWayPoint == null || dataWayPoint.length == 0) {
                this.dataWayPoint = var2;
            }
            loadData2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportMaps(List<MapTemplate> mapTemplates) {
        JSONArray mapsJson = new JSONArray();
        for (MapTemplate mapTemplate : mapTemplates) {
            mapsJson.add(exportMap(mapTemplate));
        }
        saveJson(mapsJson, "data/maps.json");
        Log.info("Loaded [" + mapsJson.size() + "] maps");
    }

    public JSONObject exportMap(MapTemplate mapTemplate) {
        JSONObject mapJson = new JSONObject();
        mapJson.put("id", mapTemplate.id);
        mapJson.put("name", mapTemplate.name);
        mapJson.put("type", mapTemplate.type);
        mapJson.put("maxX", mapTemplate.maxX);
        mapJson.put("maxY", mapTemplate.maxY);

        // WayPoints
        JSONArray wpArray = new JSONArray();
        for (WayPoint wp : mapTemplate.listWayPoint) {
            JSONObject wpJson = new JSONObject();
            wpJson.put("fromMap", wp.mapId);        // từ mapId
            wpJson.put("toMap", wp.mapNext);        // đến mapNext
            wpJson.put("x", wp.x);                  // tọa độ trung tâm x
            wpJson.put("y", wp.y);                  // tọa độ trung tâm y
            wpJson.put("width", wp.o - wp.m);       // width = o - m
            wpJson.put("height", wp.p - wp.n);      // height = p - n
            wpJson.put("type", wp.I);               // tạm dùng I làm type
            wpArray.add(wpJson);
        }
        mapJson.put("waypoints", wpArray);

        // Mobs
        JSONArray mobArray = new JSONArray();
        for (Mob mob : mapTemplate.mobList) {
            JSONObject mobJson = new JSONObject();
            mobJson.put("id", mob.id);
            mobJson.put("level", mob.level);
            mobJson.put("hp", mob.hp);
            mobJson.put("x", mob.x);
            mobJson.put("y", mob.y);
            mobJson.put("isBoss", mob.levelBoss > 0);
            mobArray.add(mobJson);
        }
        mapJson.put("mobs", mobArray);

        // NPCs
        JSONArray npcArray = new JSONArray();
        for (Npc npc : mapTemplate.npcList) {
            JSONObject npcJson = new JSONObject();
            npcJson.put("id", npc.id);
            npcJson.put("status", npc.status);
            npcJson.put("x", npc.x);
            npcJson.put("y", npc.y);
            npcArray.add(npcJson);
        }
        mapJson.put("npcs", npcArray);

        // Collision / edges
        JSONArray collisionArray = new JSONArray();
        if (mapTemplate.V != null && mapTemplate.V.a != null) {
            for (Object edge : mapTemplate.V.a) {
                JSONArray edgePoints = new JSONArray();
                vdtt_fw ee = (vdtt_fw) edge;
                for (Position p : ee.d) {
                    JSONObject pos = new JSONObject();
                    pos.put("x", p.x);
                    pos.put("y", p.y);
                    edgePoints.add(pos);
                }
                collisionArray.add(edgePoints);
            }
        }
        mapJson.put("edges", collisionArray);
        return mapJson;
    }


    public void loadData2() {
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(Util.c(Util.getFile("data/arr_data_game2.bin"))));
            int size = dis.readShort();
            for (int i = 0; i < size; i++) {
                readUTF(dis);
                dis.readByte();
                dis.readByte();
                readUTF(dis);
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                readUTF(dis);
                readUTF(dis);
                dis.readUnsignedByte();
                dis.readShort();
                dis.readShort();
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                readUTF(dis);
                readUTF(dis);
                dis.readBoolean();
                dis.readByte();
                dis.readByte();
                dis.readByte();
                dis.readShort();
                dis.readUnsignedByte();
                dis.readUnsignedShort();
                dis.readShort();
                dis.readShort();
            }
            data2 = dis.readAllBytes();
        } catch (Exception e) {
            Log.error("loadData2 err: " + e.getMessage(), e);
        }
    }


    private static String readUTF(DataInputStream dis) throws IOException {
        short var1;
        if ((var1 = (short) dis.readUnsignedByte()) == 0) {
            return dis.readUTF();
        } else {
            String var2 = "";

            for (int var3 = 0; var3 < var1; ++var3) {
                var2 = var2 + Message.m.charAt(dis.readUnsignedByte());
            }
            return var2;
        }
    }

    public final long getLevel(int var1) {
        long var2 = 0L;

        for (int var4 = 0; var4 < var1 && var4 < this.expTemp.length; ++var4) {
            var2 += this.expTemp[var4];
        }

        return var2;
    }
}

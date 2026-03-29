package com.vdtt.model;

import com.google.gson.JsonObject;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemOption;
import com.vdtt.util.ParseData;
import com.vdtt.util.Util;
import org.json.simple.JSONObject;

public class DanhHieu {
    public String title;
    public int time = -1;
    public int color = -16711681;
    public int d = -1;
    public int e = 0;
    public int f = -1;
    public int g = 1;
    public String cs;
    public String infoCs;

    public final void a() {
        this.title = this.title.trim();
        this.infoCs = getItemOptionInfo(this.cs);
        String var1 = ("Danh hiệu" + " " + this.title).toLowerCase();

        for (int var2 = 0; var2 < ItemManager.getInstance().itemTemplates.size(); ++var2) {
            if (ItemManager.getInstance().itemTemplates.get(var2).name.toLowerCase().equals(var1)) {
                this.a(ItemManager.getInstance().itemTemplates.get(var2).detail);
                return;
            }
        }
    }

    public ItemOption[] getItemOption() {
        if (cs != null && !cs.isEmpty()) {
            String[] data = Util.split(cs, ";");
            ItemOption[] itemOption = new ItemOption[data.length];
            for (int i = 0; i < data.length; ++i) {
                try {
                    itemOption[i] = new ItemOption(data[i]);
                } catch (Exception ex) {

                }
            }
            return itemOption;
        } else {
            return null;
        }
    }

    public String getItemOptionInfo(String strOption) {
        if (strOption != null && !strOption.isEmpty()) {
            String[] data = Util.split(strOption, ";");
            StringBuilder dataStr = new StringBuilder(); // Sử dụng StringBuilder để hiệu quả hơn khi nối chuỗi
            for (int i = 0; i < data.length; ++i) {
                try {
                    ItemOption op = new ItemOption(data[i]);
                    String str = op.getItemOptionTemplate().text;
                    String param1 = String.valueOf(op.getParam());
                    str = str.replaceAll("#", param1);
                    dataStr.append(str).append(", ");
                } catch (Exception ex) {
                    // Bạn có thể log lỗi ở đây nếu muốn để kiểm tra vấn đề
                }
            }
            // Nếu dataStr có nội dung, loại bỏ dấu ", " ở cuối
            if (!dataStr.isEmpty()) {
                dataStr.setLength(dataStr.length() - 2); // Loại bỏ 2 ký tự cuối là ", "
            }
            return dataStr.toString();
        } else {
            return "";
        }
    }

    public void setTime(int day) {
        time = (int) ((System.currentTimeMillis() + 60 * 60 * 1000 * 24 * day) / 1000);
    }

    public void a(String var1) {
        try {
            String[] var2;
            if ((var2 = var1.split(";")).length < 2) {
                var2 = var1.split(",");
            }

            if (var2.length > 1) {
                this.f = Integer.parseInt(var2[0]);
                this.color = Integer.parseInt(var2[1]);
                this.d = Integer.parseInt(var2[2]);
                this.g = Integer.parseInt(var2[3]);
                if (this.g > this.title.length() - 3) {
                    this.g = this.title.length() - 3;
                }

                if (this.g <= 0) {
                    this.g = 1;
                }

                this.e = Integer.parseInt(var2[3]);
                if (this.e > 3) {
                    this.e = 3;
                    return;
                }
            } else {
                this.color = Integer.parseInt(var2[0]);
            }

        } catch (Exception var3) {
        }
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("title", this.title);
        obj.put("time", this.time);
        obj.put("color", this.color);
        obj.put("cs", this.cs);
        return obj;
    }

    public DanhHieu() {
    }

    public DanhHieu(JSONObject obj) {
        ParseData parse = new ParseData(obj);
        this.title = parse.getString("title");
        this.time = parse.getInt("time");
        this.color = parse.getInt("color");
        if(obj.containsKey("cs")){
            this.cs = parse.getString("cs");
            this.infoCs = this.getItemOptionInfo(this.cs);
        }
    }
}

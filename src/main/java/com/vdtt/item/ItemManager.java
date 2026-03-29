package com.vdtt.item;

import com.vdtt.data.DataCenter;
import com.vdtt.db.DbManager;
import com.vdtt.util.Log;
import com.vdtt.util.RandomCollection;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    @Getter
    private static final ItemManager instance = new ItemManager();

    public final ArrayList<ItemTemplate> itemTemplates = new ArrayList<>();
    public final ArrayList<ItemOptionTemplate> optionTemplates = new ArrayList<>();
    public List<Item> itemNapThe = new ArrayList<>();
    public List<Item> itemThuVanMay = new ArrayList<>();
    public List<Item> itemThuVanMayLow = new ArrayList<>();
    public RandomCollection<Item> itemThuVanMayRandom = new RandomCollection<>();
    public RandomCollection<Item> itemThuVanMayLowRandom = new RandomCollection<>();


    public void loadItem() {
        try (Connection conn = DbManager.getInstance().getConnection();) {

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `item`;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.last();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                try {
                    ItemTemplate item = new ItemTemplate();
                    item.id = resultSet.getShort("id");
                    item.name = resultSet.getString("name");
                    item.type = resultSet.getByte("type");
                    item.detail = resultSet.getString("description");
                    item.gioiTinh = resultSet.getByte("gender");
                    item.phamChatNeed = resultSet.getInt("rarity");
                    item.isXepChong = resultSet.getBoolean("stacking");
                    item.idIcon = resultSet.getShort("icon");
                    item.typeChar = resultSet.getByte("classId");
                    item.levelNeed = resultSet.getShort("level");
                    item.width = resultSet.getShort("monster");
                    item.height = resultSet.getShort("character");
                    add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resultSet.close();
            stmt.close();
            loadItemOption();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadItemThuVanMay() {
        try (Connection conn = DbManager.getInstance().getConnection();) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `thuvanmay`;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.last();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                try {
                    int type = resultSet.getInt("type");
                    JSONObject jsonObject = new JSONObject(resultSet.getString("item"));
                    int itemId = jsonObject.getInt("id");
                    Item item = new Item(itemId, "vm");
                    item.quantity = jsonObject.getInt("quantity");
                    item.expire = jsonObject.getLong("expire");
                    item.strOption = jsonObject.getString("strOption");
                    item.isLock = jsonObject.getBoolean("isLock");
                    item.quantity = jsonObject.getInt("quantity");
                    if (item.quantity <= 0) {
                        item.quantity = 1;
                    }
                    if (item.expire > 0) {
                        item.expire += System.currentTimeMillis();
                    }
                    if (type == 0) {
                        itemThuVanMay.add(item);
                    } else {
                        itemThuVanMayLow.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            loadItemThuVanMayRandom();
            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadItemThuVanMayRandom() {
        itemThuVanMayRandom.add(30, itemThuVanMay.get(0));
        itemThuVanMayRandom.add(5, itemThuVanMay.get(1));
        itemThuVanMayRandom.add(5, itemThuVanMay.get(2));
        itemThuVanMayRandom.add(2, itemThuVanMay.get(3));
        itemThuVanMayRandom.add(2, itemThuVanMay.get(4));
        itemThuVanMayRandom.add(15, itemThuVanMay.get(5));
        itemThuVanMayRandom.add(6, itemThuVanMay.get(6));
        itemThuVanMayRandom.add(1, itemThuVanMay.get(7));
        itemThuVanMayRandom.add(1, itemThuVanMay.get(8));
        itemThuVanMayRandom.add(9, itemThuVanMay.get(9));
        itemThuVanMayRandom.add(15, itemThuVanMay.get(10));
        itemThuVanMayRandom.add(30, itemThuVanMay.get(11));
        itemThuVanMayRandom.add(5, itemThuVanMay.get(12));
        itemThuVanMayRandom.add(5, itemThuVanMay.get(13));
        itemThuVanMayRandom.add(5, itemThuVanMay.get(14));
        itemThuVanMayRandom.add(5, itemThuVanMay.get(15));
        itemThuVanMayRandom.add(8, itemThuVanMay.get(16));
        itemThuVanMayRandom.add(2, itemThuVanMay.get(17));
        itemThuVanMayRandom.add(3, itemThuVanMay.get(18));
        itemThuVanMayRandom.add(5, itemThuVanMay.get(19));
        itemThuVanMayRandom.add(1, itemThuVanMay.get(20));
        itemThuVanMayRandom.add(1, itemThuVanMay.get(21));
        itemThuVanMayRandom.add(12, itemThuVanMay.get(22));
        itemThuVanMayRandom.add(1, itemThuVanMay.get(23));

        itemThuVanMayLowRandom.add(30, itemThuVanMayLow.get(0));
        itemThuVanMayLowRandom.add(5, itemThuVanMayLow.get(1));
        itemThuVanMayLowRandom.add(5, itemThuVanMayLow.get(2));
        itemThuVanMayLowRandom.add(2, itemThuVanMayLow.get(3));
        itemThuVanMayLowRandom.add(2, itemThuVanMayLow.get(4));
        itemThuVanMayLowRandom.add(15, itemThuVanMayLow.get(5));
        itemThuVanMayLowRandom.add(6, itemThuVanMayLow.get(6));
        itemThuVanMayLowRandom.add(1, itemThuVanMayLow.get(7));
        itemThuVanMayLowRandom.add(1, itemThuVanMayLow.get(8));
        itemThuVanMayLowRandom.add(9, itemThuVanMayLow.get(9));
        itemThuVanMayLowRandom.add(15, itemThuVanMayLow.get(10));
        itemThuVanMayLowRandom.add(30, itemThuVanMayLow.get(11));
        itemThuVanMayLowRandom.add(5, itemThuVanMayLow.get(12));
        itemThuVanMayLowRandom.add(5, itemThuVanMayLow.get(13));
        itemThuVanMayLowRandom.add(5, itemThuVanMayLow.get(14));
        itemThuVanMayLowRandom.add(5, itemThuVanMayLow.get(15));
        itemThuVanMayLowRandom.add(8, itemThuVanMayLow.get(16));
        itemThuVanMayLowRandom.add(2, itemThuVanMayLow.get(17));
        itemThuVanMayLowRandom.add(3, itemThuVanMayLow.get(18));
        itemThuVanMayLowRandom.add(5, itemThuVanMayLow.get(19));
        itemThuVanMayLowRandom.add(1, itemThuVanMayLow.get(20));
        itemThuVanMayLowRandom.add(1, itemThuVanMayLow.get(21));
        itemThuVanMayLowRandom.add(12, itemThuVanMayLow.get(22));
        itemThuVanMayLowRandom.add(1, itemThuVanMayLow.get(23));
    }

    public void loadItemOption() {
        try (Connection conn = DbManager.getInstance().getConnection();) {

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `option`;", ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.last();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                try {
                    ItemOptionTemplate item = new ItemOptionTemplate();
                    item.id = resultSet.getShort("id");
                    item.text = resultSet.getString("text");
                    item.type = resultSet.getByte("type");
                    item.level = resultSet.getByte("level");
                    item.strOption = resultSet.getString("strOption");
                    optionTemplates.add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            resultSet.close();
            stmt.close();
            Log.info("Load item option success [" + optionTemplates.size() + "].");
            DataCenter.gI().itemOptionTemplate = optionTemplates.toArray(new ItemOptionTemplate[getOptionSize()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadItemNap() {
        try {
            String path = "itemnap.json";
            if (!Files.exists(Paths.get(path)))
                return;
            String content = new String(Files.readAllBytes(Paths.get(path)));
            if (content == null || content.isEmpty()) {
                return;
            }
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                Item item = new Item(id, "im");
                item.quantity = jsonObject.getInt("quantity");
                item.expire = jsonObject.getLong("expire");
                item.strOption = jsonObject.getString("strData");
                itemNapThe.add(item);
            }
            Log.debug("Load item nap success [" + itemNapThe.size() + "].");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void add(ItemTemplate entry) {
        itemTemplates.add(entry);
    }

    public ItemTemplate getItemTemplate(int index) {
        return itemTemplates.get(index);
    }

    public int getOptionSize() {
        return this.optionTemplates.size();
    }

    public ItemOptionTemplate getItemOptionTemplate(int index) {
        return optionTemplates.get(index);
    }

    public List<ItemTemplate> createSet(int levelNeed, int idChar) {
        List<ItemTemplate> set = new ArrayList<>();
        for (ItemTemplate item : itemTemplates) {
            if ((item.type == 2 || item.type == 4 || item.type == 6 || item.type == 8)
                    && item.levelNeed == levelNeed && item.typeChar == idChar) {
                set.add(item);
            }
        }
        return set;
    }

    public int findByName(String name) {
        int id = -1;
        for (ItemOptionTemplate op : optionTemplates) {
            if (op.text.toLowerCase().equals(name.toLowerCase())) {
                return op.id;
            }
        }
        return id;
    }
}

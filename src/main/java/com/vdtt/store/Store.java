package com.vdtt.store;

import com.vdtt.db.DbManager;
import com.vdtt.item.Item;
import com.vdtt.model.Char;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class Store {

    private int type;
    private String name;
    private List<ItemStore> items;

    public Store(int type, String name) {
        this.type = type;
        this.name = name;
        this.items = new ArrayList<>();

    }

    public boolean load() {
        try (Connection conn = DbManager.getInstance().getConnection();) {

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM store WHERE typeShop = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, this.type);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.last();
            resultSet.beforeFirst();
            while (resultSet.next()) {
                try {
                    int id = resultSet.getInt("idBuy");
                    int itemID = resultSet.getInt("id");
                    boolean lock = resultSet.getBoolean("isLock");
                    int coin = resultSet.getInt("n");
                    int zeni = resultSet.getInt("zeni");
                    int ballz = resultSet.getInt("ballz");
                    int diamond = resultSet.getInt("diamond");
                    int coinClan = resultSet.getInt("l");
                    int require = resultSet.getInt("require");
                    long expire = resultSet.getLong("expiry");
                    String options = resultSet.getString("strData");
                    ItemStore item = new ItemStore(id, itemID, (byte) 0, coin, zeni, ballz, diamond, lock, expire, options, require);
                    item.setType((byte) this.type);
                    if (type == 39) {
                        String strItems = resultSet.getString("items");
                        JSONArray jsonArray = new JSONArray(strItems);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id1 = jsonObject.getInt("id");
                            int quantity = jsonObject.getInt("quantity");
                            boolean isLock = jsonObject.getBoolean("isLock");
                            Item item1 = new Item(id1, isLock, quantity,"store");
                            item1.expire = jsonObject.getLong("expire");
                            item1.strOption = jsonObject.getString("strData");
                            item1.nameCreate = jsonObject.getString("nameCreate");
                            item1.setUpgrade((byte) jsonObject.getInt("level"));
                            item.items.add(item1);
                        }
                    }
                    add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            resultSet.close();
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int count() {
        return items.size();
    }

    public void add(ItemStore item) {
        items.add(item);
    }

    public void remove(ItemStore item) {
        items.remove(item);
    }

    public ItemStore find(int itemID) {
        for (ItemStore item : items) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public ItemStore get(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    public Stream<ItemStore> stream() {
        return items.stream();
    }

    public void show(Char p) {
        List<ItemStore> stores = new ArrayList<>();
        for (ItemStore item : items) {
            if (item.getTemplate().typeChar == p.sys || (item.getTemplate().typeChar == 0 && type != 19)) {
                stores.add(item);
            }
        }
        p.getService().openUIShop((byte) type, stores);
    }
}

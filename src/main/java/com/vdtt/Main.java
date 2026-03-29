package com.vdtt;

import com.vdtt.db.DbManager;
import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.model.Char;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println(1000 * 60 * 60 * 24 * 31L + System.currentTimeMillis());
        System.out.println(System.currentTimeMillis() + 31L * (1000 * 60 * 60 * 24));
        ItemManager.getInstance().loadItem();
        ItemManager.getInstance().loadItemOption();
        try (Connection connection = DbManager.getInstance().getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM players";
            ResultSet data = statement.executeQuery(sql);
            while (data.next()) {
                Char c = new Char();
                c.numberCellBag = data.getShort("numberCellBag");
                c.numberCellBox = data.getShort("numberCellBox");
                c.KI = data.getLong("KI");
                c.bag = new Item[c.numberCellBag];
                JSONArray jArr = (JSONArray) JSONValue.parse(data.getString("bag"));
                int len = 0;
                if (jArr != null) {
                    len = jArr.size();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = (JSONObject) jArr.get(i);
                        Item item = new Item(obj);
                        item.index = i;
                        c.bag[i] = item;
                        int id = item.getTemplate().id;
                        if (id == 821 || id == 822 || id == 1019) {
                            if (item.getQuantity() > 100) {
                                System.err.println(id + data.getString("name") + " " + item.getTemplate().name + " " + item.getQuantity());
                            }
                        }
                    }
                }
                c.box = new Item[c.numberCellBox];
                try {
                    jArr = (JSONArray) JSONValue.parse(data.getString("box"));
                    if (jArr != null) {
                        len = jArr.size();
                        for (int i = 0; i < len; i++) {
                            JSONObject obj = (JSONObject) jArr.get(i);
                            Item item = new Item(obj);
                            item.index = i;
                            c.box[i] = item;
                            int id = item.getTemplate().id;
                            if (id == 821 || id == 822 || id == 1019) {
                                if (item.getQuantity() > 100) {
                                    System.err.println(id + data.getString("name") + " " + item.getTemplate().name + " " + item.getQuantity());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.vdtt.model;

import com.vdtt.db.DbManager;
import com.vdtt.item.Item;
import com.vdtt.mail.Mail;
import com.vdtt.util.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.*;

public class FanCung {

    public void used(Char player) {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM `fan_cung` WHERE `name` = ?",
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ps.setString(1, player.name);
            ResultSet rs = ps.executeQuery();
            if (!rs.first()) {
                player.getService().serverDialog("Bạn không có trong danh sách nhận quà loan tin");
                return;
            }
            int zeni = rs.getInt("zeni");
            int xu = rs.getInt("xu");
            int ballz = rs.getInt("ballz");
            JSONArray arrItem = (JSONArray) (new JSONParser().parse(rs.getString("items")));
            int size = arrItem.size();
            if (size > player.getSlotNull()) {
                player.getService().serverDialog("Bạn không đủ chỗ trống trong hành trang.");
                return;
            }
            Mail mail = player.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng loan tin", ballz, xu, zeni, null);
            player.mailManager.addMail(mail);
            for (int i = 0; i < size; i++) {
                JSONObject itemObj = (JSONObject) arrItem.get(i);
                Item newItem = new Item(itemObj);
                if (newItem.expire > 0) {
                    newItem.expire += System.currentTimeMillis();
                }
                Mail mail1 = player.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng loan tin", 0, 0, 0, newItem);
                player.mailManager.addMail(mail1);
            }
            player.getService().serverMessage("Nhận quà loan tin thành công vui lòng kiểm tra thư");
            PreparedStatement psDelete = conn.prepareStatement("DELETE FROM `fan_cung` WHERE `name` = ?");
            psDelete.setString(1, player.name);
            psDelete.executeUpdate();
        } catch (Exception e) {
            Log.error(e);
            player.getService().serverMessage("Có lỗi xảy ra vui lòng báo admin");
        }
    }

    public static FanCung getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        public static final FanCung INSTANCE = new FanCung();
    }
}

package com.vdtt.model;

import com.vdtt.data.DataCenter;
import com.vdtt.data.SQLStatement;
import com.vdtt.db.DbManager;
import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemTemplate;
import com.vdtt.mail.Mail;
import com.vdtt.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.*;

public class GiftCode {
    private static final GiftCode instance = new GiftCode();

    public static GiftCode getInstance() {
        return instance;
    }

    public void use(Char player, String code) {
        try (Connection conn = DbManager.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(
                SQLStatement.GET_GIFT_CODE, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            int lent = code.length();
            if (code.isEmpty() || lent < 5 || lent > 30) {
                player.getService().serverDialog("Mã quà tặng có chiều dài từ 5 đến 30 ký tự.");
                return;
            }
            stmt.setString(1, code);
            try (ResultSet res = stmt.executeQuery()) {
                if (!res.first()) {
                    player.getService().serverDialog("Mã quà tặng không tồn tại hoặc đã hết hạn.");
                    return;
                }
//                if (code.toLowerCase().contains("openté")) {
//                    if (player.user.activated == 1) {
//                        player.getService().serverDialog("Sau khi mở thành viên tại Bulma sẽ nhập được mã quà tặng này!");
//                        return;
//                    }
//                }
                int id = res.getInt("id");
                byte status = res.getByte("status");
                byte type = res.getByte("type");
                int used = res.getInt("used");
                int eventType = res.getInt("giftcode_event");
                if(eventType != -1 && isUsedTypeGiftCode(player, eventType)) {
                    player.getService().serverDialog("Bạn đã sử dụng giftcode của sự kiện này rồi!");
                    return;
                }
                if (type == 0 && player.user.activated == 1) {
                    player.getService().serverDialog(" Giftcode này chỉ dành cho tài khoản đã kích hoạt!");
                    return;
                }
                if (status == 1) {
                    player.getService().serverDialog("Mã quà tặng đã được sử dụng");
                    return;
                } else if (isUsedGiftCode(player, code)) {
                    player.getService().serverDialog("Mỗi người chỉ được sử dụng 1 lần.");
                    return;
                } else if (type == 1 && used == 0) {
                    player.getService().serverDialog("Đã hết số lần sử dụng mã quà tặng này.");
                    return;
                }
                int zeni = res.getInt("zeni");
                int xu = res.getInt("xu");
                int ballz = res.getInt("ballz");
                JSONArray arrItem = (JSONArray) (new JSONParser().parse(res.getString("items")));

                int size = arrItem.size();

                if (size > player.getSlotNull()) {
                    player.getService().serverDialog("Bạn không đủ chỗ trống trong hành trang.");
                    return;
                }
                Mail mail = player.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng mã quà tặng :" + code, ballz, xu, zeni, null);
                player.mailManager.addMail(mail);
//                if (code.toLowerCase().contains("danhhieu999")) {
//                    for(ItemTemplate tl : ItemManager.getInstance().itemTemplates) {
//                        if(tl.name.startsWith("Danh hiệu")) {
//                            Item newItem = new Item(tl.id,"gc");
//                            newItem.addOption(Util.nextInt(0,3),Util.nextInt(50,100));
//                            if (newItem.expire > 0) {
//                                newItem.expire += System.currentTimeMillis();
//                            }
//                            Mail mail1 = player.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng mã quà tặng :" + code, 0, 0, 0, newItem);
//                            player.mailManager.addMail(mail1);
//                        }
//                    }
//                } else {
                    for (int i = 0; i < size; i++) {
                        JSONObject itemObj = (JSONObject) arrItem.get(i);
                        Item newItem = new Item(itemObj);
                        if (newItem.expire > 0) {
                            newItem.expire += System.currentTimeMillis();
                        }
                        Mail mail1 = player.createMail("Hệ thống", "Thư của hệ thống", "Phần thưởng mã quà tặng :" + code, 0, 0, 0, newItem);
                        player.mailManager.addMail(mail1);
                    }
//                }

                player.user.session.addUseGiftCode();
                player.getService().serverMessage("Nhận mã quà tặng thành công vui lòng kiểm tra thư");
                addUsedGiftCode(player, code, type);

                if (used > 0 && type == 1) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    res.updateInt("used", used - 1);
                    res.updateTimestamp("updated_at", timestamp);
                    res.updateRow();
                } else if (type > 1) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    res.updateByte("status", (byte) 1);
                    res.updateTimestamp("updated_at", timestamp);
                    res.updateRow();
                }
            } finally {
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUsedGiftByType(Char player, int typeCode) {
        try (Connection conn = DbManager.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(
                SQLStatement.CHECK_EXIST_TYPE, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY);) {
            stmt.setInt(1, player.id);
            stmt.setInt(2, player.user.id);
            stmt.setInt(3, typeCode);
            ResultSet res = stmt.executeQuery();
            try {
                if (res.first()) {
                    return true;
                }
            } finally {
                res.close();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsedGiftCode(Char player, String giftCode) {
        try (Connection conn = DbManager.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(
                SQLStatement.CHECK_EXIST_USED_GIFT_CODE, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY);) {

            stmt.setString(1, giftCode);
            stmt.setInt(2, player.id);
            stmt.setInt(3, player.user.id);
            ResultSet res = stmt.executeQuery();
            try {
                if (res.first()) {
                    return true;
                }
            } finally {
                res.close();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsedTypeGiftCode(Char player, int type) {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     SQLStatement.CHECK_EXIST_TYPE_GIFT_CODE, ResultSet.TYPE_SCROLL_SENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);) {

            stmt.setInt(1, type);
            stmt.setInt(2, player.id);
            stmt.setInt(3, player.user.id);
            ResultSet res = stmt.executeQuery();
            try {
                if (res.first()) {
                    return true;
                }
            } finally {
                res.close();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUsedGiftCode(Char player, String giftCode, int typeCode) {
        try (Connection conn = DbManager.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(SQLStatement.INSERT_USED_GIFT_CODE);){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            stmt.setInt(1, player.id);
            stmt.setInt(2, player.user.id);
            stmt.setString(3, giftCode);
            stmt.setInt(4, typeCode);
            stmt.setTimestamp(5, timestamp);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

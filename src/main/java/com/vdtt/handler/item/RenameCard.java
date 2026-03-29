package com.vdtt.handler.item;

import com.vdtt.clan.Clan;
import com.vdtt.clan.Member;
import com.vdtt.data.CMDInputDialog;
import com.vdtt.db.DbManager;
import com.vdtt.events.points.EventPoint;
import com.vdtt.item.Item;
import com.vdtt.model.Char;
import com.vdtt.model.InputDialog;
import com.vdtt.stall.StallManager;
import com.vdtt.util.Log;
import com.vdtt.util.Util;

import java.sql.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameCard extends UseItem {
    @Override
    protected void use(Char player, Item item) {
        InputDialog inputDialog = new InputDialog(CMDInputDialog.EXECUTE, "Tên nhân vật");
        inputDialog.setRunnable(() -> {
            String name = inputDialog.getText();
            rename(player, name, item);
        });
        player.setInput(inputDialog);
        player.getService().showInputDialog();
    }

    private void rename(Char player, String newName, Item item) {
        if (!item.has()) {
            return;
        }
        if (StallManager.getInstance().isSeller(player.name)) {
            player.serverDialog("Vui lòng gỡ hết vật phẩm trên chợ trước khi đổi tên!");
            return;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m1 = p.matcher(newName);
        if (!m1.find()) {
            player.serverDialog("Tên nhân vật không hợp lệ.");
            return;
        }
        if (checkExist(newName)) {
            player.serverDialog("Tên nhân vật đã tồn tại!");
            return;
        }

        EventPoint eventPoint = player.getEventPoint();
        if (player.getEventPoint() != null) {
            eventPoint.getRewardPoints().forEach(rewardPoint -> {
                DbManager.getInstance().executeUpdate("UPDATE `event_points` SET `name` = ? WHERE `name` = ? LIMIT 1;", (statement) -> {
                    try {
                        statement.setString(1, player.name);
                        statement.setString(2, newName);
                    } catch (SQLException e) {
                        Log.error("Error when update event point", e);
                    }
                });
            });
        }

        Clan clan = player.clan;
        if (clan != null) {
            Member mem = clan.getMemberByName(player.name);
            mem.setName(newName);
            if (mem.getType() == Clan.TYPE_TOCTRUONG) {
                clan.setMainName(mem.getName());
            }
            clan.getClanService().serverMessage(player.name + "đã đổi tên thành " + newName);
        }
        player.removeItem(item.index, 1, true);
        DbManager.getInstance().renameCharacter(player.id, player.name, newName);
        player.serverDialog("Đổi tên thành công! Vui lòng thoát game để cập nhật lại");
        Util.setTimeoutSchedule(() -> player.user.session.disconnect(), 3000);
    }

    private boolean checkExist(String name) {
        AtomicBoolean isExist = new AtomicBoolean(false);
        DbManager.getInstance().executeQuery("SELECT * FROM `players` WHERE `name` = ?;", rs -> {
            try {
                isExist.set(rs.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, name);
        return isExist.get();
    }
}

package com.vdtt.server;

import com.vdtt.clan.Clan;
import com.vdtt.db.DbManager;
import com.vdtt.model.Char;
import com.vdtt.util.Log;
import com.vdtt.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

public class TaskResetManager {
    private static TaskResetManager instance;

    // Public method to provide access to the singleton instance
    public static synchronized TaskResetManager getInstance() {
        if (instance == null) {
            instance = new TaskResetManager();
        }
        return instance;
    }

    public void resetNewDay() {
        Util.schedule(() -> {
            resetNewDayAllPlayer();
            resetClanNewDay();
        }, 0, 0, 0);
    }

    public void resetNewDayAllPlayer() {
        List<Char> chars = ServerManager.getChars();
        for (Char pl : chars) {
            try {
                if (pl != null && !pl.isCleaned) {
                    pl.doCheckAndResetNewDay();
                }
            } catch (Exception e) {

            }
        }
    }

    public void resetClanNewDay() {
        try (Connection conn = DbManager.getInstance().getConnection();) {
            List<Clan> clans = Clan.getClanDAO().getAll();
            Date now = new Date();
            synchronized (clans) {
                for (Clan clan : clans) {
                    try {
                        clan.openDun = 1;
                        clan.countKick = 5;
                        clan.countInvite = 20;
                        clan.countNvHd = 1;
                        PreparedStatement stmt3 = conn.prepareStatement("UPDATE `clan` SET `open_dun` = 1,`countinvite` = 20,`countkick` = 5, `updated_at` = ? WHERE `id` = ? LIMIT 1;");
                        stmt3.setString(1, Util.dateToString(now, "yyyy-MM-dd"));
                        stmt3.setInt(2, clan.id);
                        stmt3.executeUpdate();
                        stmt3.close();
                    } catch (Exception e) {
                        Log.error("update err ", e);
                    }
                }
            }
        } catch (Exception e) {
            Log.error("resetClanNewDay err ", e);
        }
    }

}


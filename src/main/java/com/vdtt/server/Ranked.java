package com.vdtt.server;

import com.vdtt.clan.Clan;
import com.vdtt.clan.Member;
import com.vdtt.db.DbManager;
import com.vdtt.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Ranked {

    public static final Vector[] RANKED = new Vector[6];
    public static final List<CaoThu> topSoiNoi = new ArrayList<>();
    public static final List<CaoThu> topNapThang = new ArrayList<>();
    public static final List<CaoThu> topLamBanh = new ArrayList<>();
    public static final List<CaoThu> topThaLongDen = new ArrayList<>();

    public static void init() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        };
        long delay = 10 * 60 * 1000;
        Timer timer = new Timer("Ranked");
        timer.schedule(timerTask, 0, delay);
    }

    public static void refresh() {
        initTopCaoThu();
        initTopVuTru();
        initTopNap();
        initTopPhamChat();
        initTopSoiNoi();
        initTopNapThang();
//        initTopLamBanh();
//        initTopThaLongDen();
        Log.info("Refresh ranked success [6].");
    }

    private static void initTopSoiNoi() {
        topSoiNoi.clear();
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT `name`,`clan`,`class`, CAST(JSON_EXTRACT(data, \"$.pointEnthusiasticWeek\") AS UNSIGNED) AS `pointEnthusiasticWeek` FROM players where `server_id` = ? ORDER BY `pointEnthusiasticWeek` DESC LIMIT 100;");) {
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                CaoThu rank = new CaoThu();
                rank.num = res.getLong("pointEnthusiasticWeek");
                rank.name = res.getString("name");
                int clan = res.getInt("clan");
                if (clan != 0) {
                    Optional<Clan> g = Clan.getClanDAO().get(clan);
                    if (g != null && g.isPresent()) {
                        Member mem = g.get().getMemberByName(rank.name);
                        if (mem != null) {
                            rank.clanName = g.get().getName();
                        }
                    }
                }
                rank.classId = (byte) (res.getByte("class") + 1);
                topSoiNoi.add(rank);
            }
            order(topSoiNoi);
            res.close();
        } catch (SQLException ex) {
            Log.error("init top pham chat", ex);
        }
    }

    private static void initTopLamBanh() {
        topLamBanh.clear();
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT `name`,`clan`,`class`, `pointMakeCake` FROM players where `server_id` = ? AND `pointMakeCake` > 0 ORDER BY `pointMakeCake` DESC LIMIT 100;");) {
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                CaoThu rank = new CaoThu();
                rank.num = res.getLong("pointMakeCake");
                rank.name = res.getString("name");
                int clan = res.getInt("clan");
                if (clan != 0) {
                    Optional<Clan> g = Clan.getClanDAO().get(clan);
                    if (g != null && g.isPresent()) {
                        Member mem = g.get().getMemberByName(rank.name);
                        if (mem != null) {
                            rank.clanName = g.get().getName();
                        }
                    }
                }
                rank.classId = (byte) (res.getByte("class") + 1);
                topLamBanh.add(rank);
            }
            order(topLamBanh);
            res.close();
        } catch (SQLException ex) {
            Log.error("init top lam banh", ex);
        }
    }

    private static void initTopThaLongDen() {
        topThaLongDen.clear();
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT `name`,`clan`,`class`, `pointThaLongDen` FROM players where `server_id` = ? AND `pointThaLongDen` > 0 ORDER BY `pointThaLongDen` DESC LIMIT 100;");) {
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                CaoThu rank = new CaoThu();
                rank.num = res.getLong("pointThaLongDen");
                rank.name = res.getString("name");
                int clan = res.getInt("clan");
                if (clan != 0) {
                    Optional<Clan> g = Clan.getClanDAO().get(clan);
                    if (g != null && g.isPresent()) {
                        Member mem = g.get().getMemberByName(rank.name);
                        if (mem != null) {
                            rank.clanName = g.get().getName();
                        }
                    }
                }
                rank.classId = (byte) (res.getByte("class") + 1);
                topThaLongDen.add(rank);
            }
            order(topThaLongDen);
            res.close();
        } catch (SQLException ex) {
            Log.error("init top tha long den", ex);
        }
    }

    public static void initTopCaoThu() {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT `name`,`clan`,`class`, CAST(JSON_EXTRACT(data, \"$.exp\") AS UNSIGNED) AS `exp` FROM players where `server_id` = ? ORDER BY `exp` DESC LIMIT 100;");) {
            Vector<CaoThu> ranked = new Vector<>();
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            List<CaoThu> list = new ArrayList<>();
            while (res.next()) {
                CaoThu rank = new CaoThu();
                rank.num = res.getLong("exp");
                rank.name = res.getString("name");
                int clan = res.getInt("clan");
                if (clan != 0) {
                    Optional<Clan> g = Clan.getClanDAO().get(clan);
                    if (g != null && g.isPresent()) {
                        Member mem = g.get().getMemberByName(rank.name);
                        if (mem != null) {
                            rank.clanName = g.get().getName();
                        }
                    }
                }
                rank.classId = (byte) (res.getByte("class") + 1);
                list.add(rank);
            }
            order(list);
            ranked.addAll(list);
            res.close();
            stmt.close();
            RANKED[0] = ranked;
        } catch (SQLException ex) {
            Log.error("init top cao thu", ex);
        }
    }

    public static void initTopPhamChat() {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT `name`,`clan`,`class`, CAST(JSON_EXTRACT(data, \"$.pointPhamChat\") AS UNSIGNED) AS `pointPhamChat` FROM players where `server_id` = ? ORDER BY `pointPhamChat` DESC LIMIT 100;");) {
            Vector<CaoThu> ranked = new Vector<>();
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            List<CaoThu> list = new ArrayList<>();
            while (res.next()) {
                CaoThu rank = new CaoThu();
                rank.num = res.getLong("pointPhamChat");
                rank.name = res.getString("name");
                int clan = res.getInt("clan");
                if (clan != 0) {
                    Optional<Clan> g = Clan.getClanDAO().get(clan);
                    if (g != null && g.isPresent()) {
                        Member mem = g.get().getMemberByName(rank.name);
                        if (mem != null) {
                            rank.clanName = g.get().getName();
                        }
                    }
                }
                rank.classId = (byte) (res.getByte("class") + 1);
                list.add(rank);
            }
            order(list);
            ranked.addAll(list);
            res.close();
            stmt.close();
            RANKED[1] = ranked;
        } catch (SQLException ex) {
            Log.error("init top pham chat", ex);
        }
    }

    public static void initTopNap() {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT `name`,`clan`,`class`, CAST(JSON_EXTRACT(data, \"$.mocNap\") AS UNSIGNED) AS `mocNap` FROM players where `server_id` = ? ORDER BY `mocNap` DESC LIMIT 100;");) {
            Vector<CaoThu> ranked = new Vector<>();
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            List<CaoThu> list = new ArrayList<>();
            while (res.next()) {
                CaoThu rank = new CaoThu();
                rank.num = res.getLong("mocNap");
                rank.name = res.getString("name");
                int clan = res.getInt("clan");
                if (clan != 0) {
                    Optional<Clan> g = Clan.getClanDAO().get(clan);
                    if (g != null && g.isPresent()) {
                        Member mem = g.get().getMemberByName(rank.name);
                        if (mem != null) {
                            rank.clanName = g.get().getName();
                        }
                    }
                }
                rank.classId = (byte) (res.getByte("class") + 1);
                list.add(rank);
            }
            order(list);
            ranked.addAll(list);
            res.close();
            stmt.close();
            RANKED[2] = ranked;
        } catch (Exception ex) {
            Log.error("init top nap", ex);
        }
    }

    public static void initTopNapThang() {
        topNapThang.clear();

        if (Config.getInstance().getServerID() == 1) {
            String sql = "SELECT players.`name`, players.`clan`, players.`class`, users.napthang " +
                    "FROM players " +
                    "JOIN users ON players.user_id = users.id " +
                    "WHERE players.`server_id` = ? " +
                    "ORDER BY users.napthang DESC " +
                    "LIMIT 100;";
            try (Connection conn = DbManager.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);) {
                Vector<CaoThu> ranked = new Vector<>();
                stmt.setInt(1, Config.getInstance().getServerID());
                ResultSet res = stmt.executeQuery();
                while (res.next()) {
                    CaoThu rank = new CaoThu();
                    rank.num = res.getLong("napthang");
                    rank.name = res.getString("name");
                    int clan = res.getInt("clan");
                    if (clan != 0) {
                        Optional<Clan> g = Clan.getClanDAO().get(clan);
                        if (g != null && g.isPresent()) {
                            Member mem = g.get().getMemberByName(rank.name);
                            if (mem != null) {
                                rank.clanName = g.get().getName();
                            }
                        }
                    }
                    rank.classId = (byte) (res.getByte("class") + 1);
                    topNapThang.add(rank);
                }
                order(topNapThang);
                res.close();
                stmt.close();
            } catch (Exception ex) {
                Log.error("init top nap", ex);
            }
        } else {
            String sql = "SELECT players.`name`, players.`clan`, players.`class`, users.napthang2 " +
                    "FROM players " +
                    "JOIN users ON players.user_id = users.id " +
                    "WHERE players.`server_id` = ? " +
                    "ORDER BY users.napthang2 DESC " +
                    "LIMIT 100;";
            try (Connection conn = DbManager.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);) {
                Vector<CaoThu> ranked = new Vector<>();
                stmt.setInt(1, Config.getInstance().getServerID());
                ResultSet res = stmt.executeQuery();
                while (res.next()) {
                    CaoThu rank = new CaoThu();
                    rank.num = res.getLong("napthang2");
                    rank.name = res.getString("name");
                    int clan = res.getInt("clan");
                    if (clan != 0) {
                        Optional<Clan> g = Clan.getClanDAO().get(clan);
                        if (g != null && g.isPresent()) {
                            Member mem = g.get().getMemberByName(rank.name);
                            if (mem != null) {
                                rank.clanName = g.get().getName();
                            }
                        }
                    }
                    rank.classId = (byte) (res.getByte("class") + 1);
                    topNapThang.add(rank);
                }
                order(topNapThang);
                res.close();
                stmt.close();
            } catch (Exception ex) {
                Log.error("init top nap", ex);
            }
        }
    }

    public static void initTopVuTru() {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement("SELECT `id` FROM `clan` WHERE clan.`server_id` = ? ORDER BY `level` DESC , `exp` DESC LIMIT 100;");) {
            Vector<Clan> ranked = new Vector<>();
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                Optional<Clan> g = Clan.getClanDAO().get(id);
                if (g != null && g.isPresent()) {
                    Clan clan = g.get();
                    ranked.add(clan);
                }
            }
            ranked.sort((o1, o2) -> {
                int level1 = o1.getLevel();
                int level2 = o2.getLevel();
                int comp = Integer.compare(level2, level1);
                if (comp != 0) {
                    return comp;
                }
                long exp1 = o1.getExp();
                long exp2 = o2.getExp();
                return Long.compare(exp2, exp1);
            });
            res.close();
            stmt.close();
            RANKED[3] = ranked;
        } catch (SQLException ex) {
            Log.error("init top gia toc", ex);
        }
    }

    private static void order(List<CaoThu> ranks) {
        Collections.sort(ranks, (Comparator) (o1, o2) -> {

            Long level1 = ((CaoThu) o1).num;
            Long level2 = ((CaoThu) o2).num;
            return level2.compareTo(level1);
        });
    }
}

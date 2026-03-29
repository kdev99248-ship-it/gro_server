package com.vdtt.clan;

import com.vdtt.data.Dao;
import com.vdtt.db.DbManager;
import com.vdtt.item.Item;
import com.vdtt.server.Config;
import com.vdtt.skill.SkillClan;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ClanDAO implements Dao<Clan> {
    private List<Clan> clans = new ArrayList<>();

    public boolean checkExist(String name) {
        synchronized (clans) {
            for (Clan clan : clans) {
                if (clan.name.toLowerCase().equals(name.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Optional<Clan> get(long id) {
        return clans.stream().filter(clan -> clan.id == id).findFirst();
    }

    public Optional<Clan> get(String name) {
        return clans.stream().filter(clan -> clan.name.contains(name)).findFirst();
    }

    @Override
    public List<Clan> getAll() {
        return this.clans;
    }

    @Override
    public void save(Clan clan) {
        try (Connection conn = DbManager.getInstance().getConnection();) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO `clan` (`name`, `main_name`, `log`, `box`, `alert`, `skill`,`hour`,`min`,`server_id`) VALUES (?, ?, ?, ?, ?, '[]',?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = null;
            try {
                ps.setString(1, clan.name);
                ps.setString(2, clan.main_name);
                ps.setString(3, clan.getLog());
                ps.setString(4, "[]");
                ps.setString(5, "");
                ps.setShort(6, clan.hour);
                ps.setShort(7, clan.min);
                ps.setInt(8, Config.getInstance().getServerID());
                ps.executeUpdate();
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    clan.id = rs.getInt(1);
                }
            } finally {
                ps.close();
                if (rs != null) {
                    rs.close();
                }
            }
            clans.add(clan);
        } catch (SQLException ex) {
            Log.error("save err: " + ex.getMessage(), ex);
        }
    }

    public void load() {
        try (Connection conn = DbManager.getInstance().getConnection();) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `clan` WHERE `server_id` = ?");
            stmt.setInt(1, Config.getInstance().getServerID());
            java.util.Date now = new java.util.Date();
            try {
                ResultSet res = stmt.executeQuery();
                while (res.next()) {
                    JSONArray jArr;
                    Clan clan = new Clan();
                    clan.id = res.getInt("id");
                    clan.name = res.getString("name");
                    clan.main_name = res.getString("main_name");
                    clan.alert = res.getString("alert");
                    clan.level = res.getByte("level");
                    clan.coin = res.getInt("coin");
                    clan.exp = res.getInt("exp");
                    clan.countInvite = res.getByte("countinvite");
                    clan.countKick = res.getByte("countkick");
                    clan.openDun = res.getByte("open_dun");
                    clan.reg_date = res.getDate("reg_date");
                    clan.log = res.getString("log");
                    clan.hour = res.getByte("hour");
                    clan.min = res.getByte("min");
                    jArr = (JSONArray) JSONValue.parse(res.getString("box"));
                    if (jArr != null) {
                        int len = jArr.size();
                        for (int i = 0; i < len; i++) {
                            JSONObject obj = (JSONObject) jArr.get(i);
                            Item item = new Item(obj);
                            item.index = item.getTemplate().type;
                            clan.items[i] = item;
                        }
                    }
                    Date updated_at = res.getDate("updated_at");
                    jArr = (JSONArray) JSONValue.parse(res.getString("skill"));
                    int len = jArr.size();
                    if (jArr != null) {
                        for (int i = 0; i < len; i++) {
                            JSONObject obj = (JSONObject) jArr.get(i);
                            SkillClan skillClan = new SkillClan();
                            skillClan.id = Integer.parseInt(obj.get("id").toString());
                            skillClan.strOptions = obj.get("strOptions").toString();
                            skillClan.timeUse = Integer.parseInt(obj.get("timeUse").toString());
                            clan.skillClans.add(skillClan);
                        }
                    }
                    jArr.clear();

                    if (!DateUtils.isSameDay(now, updated_at)) {
                        clan.openDun = 1;
                        clan.countInvite = 20;
                        clan.countKick = 5;
                        clan.countNvHd = 1;
                        PreparedStatement stmt3 = conn.prepareStatement(
                                "UPDATE `clan` SET `open_dun` = 1,`countinvite` = 20,`countkick` = 5, `updated_at` = ? WHERE `id` = ? LIMIT 1;");
                        stmt3.setString(1, Util.dateToString(now, "yyyy-MM-dd"));
                        stmt3.setInt(2, clan.id);
                        stmt3.executeUpdate();
                        stmt3.close();
                    }
                    clan.memberDAO.load();
                    clans.add(clan);
                }
                res.close();
            } finally {
                stmt.close();
            }
            Log.info("Load clan data successfully [" + clans.size() + "].");
        } catch (SQLException ex) {
            Log.error("load fail", ex);
        }
    }

    @Override
    public void update(Clan clan) {
        if (!clan.isSaving()) {
            clan.setSaving(true);
            try (Connection conn = DbManager.getInstance().getConnection();) {
                JSONArray skill = new JSONArray();
                for (SkillClan skillClan : clan.skillClans) {
                    skill.add(skillClan.toJSONObject());
                }
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE `clan` SET `coin` = ?, `level` = ?, `exp` = ?, `open_dun` = ?, `box` = ?, `log` = ?, `skill` = ?,`countinvite` = ?,`countkick` = ?,`hour` = ?,`min` = ? WHERE `id` = ? LIMIT 1;");
                try {
                    stmt.setInt(1, clan.coin);
                    stmt.setInt(2, clan.level);
                    stmt.setInt(3, clan.exp);
                    stmt.setInt(4, clan.openDun);
                    JSONArray boxs = new JSONArray();
                    for (Item itm : clan.items) {
                        if (itm == null) {
                            continue;
                        }
                        boxs.add(itm.toJSONObject());
                    }
                    stmt.setString(5, boxs.toJSONString());
                    stmt.setString(6, clan.log);
                    stmt.setString(7, skill.toJSONString());
                    stmt.setInt(8, clan.countInvite);
                    stmt.setInt(9, clan.countKick);
                    stmt.setInt(10, clan.hour);
                    stmt.setInt(11, clan.min);
                    stmt.setInt(12, clan.id);
                    stmt.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stmt.close();
                }
                try {
                    List<Member> members = clan.memberDAO.getAll();
                    synchronized (members) {
                        for (Member member : members) {
                            clan.memberDAO.update(member);
                        }
                    }
                } catch (Exception e) {

                }
            } catch (SQLException ex) {
                Log.error("update clan fail", ex);
            } finally {
                clan.setSaving(false);
            }
        }
    }

    @Override
    public void delete(Clan clan) {
        try (Connection conn = DbManager.getInstance().getConnection();) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM `clan` WHERE `id` = ?;");
            try {
                ps.setInt(1, clan.id);
                ps.executeUpdate();
            } finally {
                ps.close();
            }
            get(clan.id).ifPresent(exist -> clans.remove(exist));
        } catch (SQLException ex) {
            Log.error("delete clan err", ex);
        }
    }
}


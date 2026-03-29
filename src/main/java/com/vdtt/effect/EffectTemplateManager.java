package com.vdtt.effect;

import com.vdtt.db.DbManager;
import com.vdtt.util.Log;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EffectTemplateManager {

    @Getter
    private static final EffectTemplateManager instance = new EffectTemplateManager();

    @Getter
    private final List<EffectTemplate> list = new ArrayList<>();

    public void init() {
        load();
    }

    public void load() {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `effect`;");
             ResultSet resultSet = stmt.executeQuery();) {
            while (resultSet.next()) {
                EffectTemplate eff = new EffectTemplate();
                eff.id = (short) resultSet.getInt("id");
                eff.type = resultSet.getShort("type");
                eff.name = resultSet.getString("name");
                eff.detail = resultSet.getString("detail");
                eff.idIcon = (short) resultSet.getInt("idIcon");
                eff.idAura = (short) resultSet.getInt("idAura");
                add(eff);
            }
        } catch (SQLException ex) {
            Log.error(ex.getMessage(), ex);
        }
    }


    public int size() {
        return list.size();
    }

    public void add(EffectTemplate template) {
        list.add(template);
    }

    public void remove(EffectTemplate template) {
        list.remove(template);
    }

    public EffectTemplate get(int index) {
        return list.get(index);
    }

    public EffectTemplate find(int id) {
        for (EffectTemplate eff : list) {
            if (eff.id == id) {
                return eff;
            }
        }
        return null;
    }
}


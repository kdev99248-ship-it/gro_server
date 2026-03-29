package com.vdtt.reward;

import com.vdtt.db.DbManager;
import com.vdtt.item.Item;
import com.vdtt.util.Log;
import com.vdtt.util.ParseData;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RewardManager {
    private static final RewardManager instance = new RewardManager();

    public static RewardManager gI() {
        return instance;
    }

    public List<Reward> rewards;

    public void init() {
        rewards = new ArrayList<>();
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `welfare`;");
             ResultSet resultSet = stmt.executeQuery();) {
            while (resultSet.next()) {
                Reward reward = new Reward();
                reward.id = resultSet.getInt("id");
                reward.setName(resultSet.getString("name"));
                reward.type = resultSet.getInt("type");
                JSONObject itemJson = (JSONObject) JSONValue.parse(resultSet.getString("item"));
                ParseData parse = new ParseData(itemJson);
                int itemID = parse.getInt("id");
                int quantity = parse.getInt("quantity");
                boolean isLock = parse.getBoolean("isLock");
                Item item = new Item(itemID, "rw");
                item.setQuantity(quantity);
                item.setLock(isLock);
                item.strOption = parse.getString("strOption");
                item.expire = parse.getLong("expire");
                reward.item = item;
                reward.required = resultSet.getInt("required");
                rewards.add(reward);
            }
            Log.debug("Loaded [" + rewards.size() + "] daily rewards.");
        } catch (Exception ex) {
            Log.error(ex.getMessage(), ex);
        }
    }

    public Reward find(int id) {
        for (Reward reward : rewards) {
            if (reward.id == id) {
                return reward;
            }
        }
        return null;
    }

    public List<Reward> getListByType(int type) {
        List<Reward> rewardList = new ArrayList<>();
        for (Reward reward : rewards) {
            if (reward.type == type) {
                rewardList.add(reward);
            }
        }
        return rewardList;
    }
}

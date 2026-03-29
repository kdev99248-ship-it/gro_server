package com.vdtt.events;

import com.vdtt.events.points.KeyPoint;
import com.vdtt.item.Item;
import com.vdtt.leaderboard.LeaderboardRace;
import com.vdtt.leaderboard.LeaderboardRaceManager;
import com.vdtt.reward.Reward;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventRace extends LeaderboardRace {

    private final AtomicInteger idGenerator;
    @Getter
    private final KeyPoint key;

    public EventRace(KeyPoint key, String content, LocalDateTime startDate, LocalDateTime endDate) {
        super(1000, "Đua Top " + key.getName(), content, startDate, endDate);
        this.key = key;
        this.idGenerator = new AtomicInteger(9999);
        LeaderboardRaceManager manager = LeaderboardRaceManager.getInstance();
        setId(manager.genId());
        manager.addLeaderboardRace(this);
    }

    @Override
    public void addReward(Reward reward) {
        reward.setId(idGenerator.getAndIncrement());
        super.addReward(reward);
    }

    public void addRewardTop(int minTop, int maxTop, List<Item> items, ItemCreator creator) {
        for (Item item : items) {
            String rewardName = "Top " + (minTop == maxTop ? minTop : minTop + "-" + maxTop) + " " + key.getName();
            creator.create(item);
            addReward(new EventReward(rewardName, minTop, maxTop, item));
        }
    }

    public interface ItemCreator {
        void create(Item item);
    }
}

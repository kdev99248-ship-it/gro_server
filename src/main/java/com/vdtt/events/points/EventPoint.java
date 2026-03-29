package com.vdtt.events.points;

import com.vdtt.db.DbManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventPoint {

    private List<RewardPoint> rewardPoints;
    private String playerName;
    private int playerID;
    private int eventID;
    private byte classId;
    private String clanName;

    public EventPoint() {
        this.rewardPoints = new ArrayList<>();
    }

    public void addIfMissing(List<KeyPoint> keys) {
        for (KeyPoint key : keys) {
            RewardPoint p = find(key);
            if (p == null) {
                p = new RewardPoint(0, key, 0);
                add(p);
                DbManager.getInstance().insertRewardPoint(playerID, classId, clanName, eventID, p);
            }
        }
    }

    public void add(RewardPoint rewardPoint) {
        this.rewardPoints.add(rewardPoint);
    }

    public void remove(RewardPoint rewardPoint) {
        this.rewardPoints.remove(rewardPoint);
    }

    public boolean addPoint(KeyPoint key) {
        return addPoint(key, 1);
    }

    public boolean addPoint(KeyPoint key, int point) {
        RewardPoint p = find(key);
        if (p != null) {
            p.addPoint(point);
            return true;
        }
        return false;
    }

    public boolean Point(KeyPoint key, int point) {
        RewardPoint p = find(key);
        if (p != null) {
            p.Point(point);
            return true;
        }
        return false;
    }

    public void erasePoint(KeyPoint key) {
        RewardPoint p = find(key);
        if (p != null) {
            p.setPoint(0);
        }
    }

    public boolean subtractPoint(KeyPoint key, int point) {
        RewardPoint p = find(key);
        if (p != null) {
            p.subPoint(point);
            return true;
        }
        return false;
    }

    public boolean setPoint(KeyPoint key, int point) {
        RewardPoint p = find(key);
        if (p != null) {
            p.setPoint(point);
            return true;
        }
        return false;
    }

    public int getPoint(KeyPoint key) {
        RewardPoint p = find(key);
        if (p != null) {
            return p.getPoint();
        }
        return 0;
    }

    public RewardPoint find(KeyPoint key) {
        for (RewardPoint p : rewardPoints) {
            if (p.getKey() != null && p.getKey().equals(key)) {
                return p;
            }
        }
        return null;
    }

}


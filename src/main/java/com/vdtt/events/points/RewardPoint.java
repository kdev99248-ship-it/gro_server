package com.vdtt.events.points;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewardPoint {

    private int id;
    private KeyPoint key;
    private int point;

    public RewardPoint(int id, KeyPoint key, int point) {
        this.id = id;
        this.key = key;
        this.point = point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void Point(int point) {
        this.point = point;
    }

    public void subPoint(int point) {
        this.point -= point;
    }
}


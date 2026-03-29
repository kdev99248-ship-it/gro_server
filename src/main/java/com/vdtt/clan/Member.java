package com.vdtt.clan;

import com.vdtt.map.world.planetblackstar.RewardDataBlackBall;
import com.vdtt.model.Char;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class Member {
    private int id;
    private int classId;
    private int level;
    private int type;
    private String name;
    private int pointClan;
    private int pointClanWeek;
    private boolean online;
    private Char p;

    @Setter
    @Getter
    private List<RewardDataBlackBall> rewardDataBlackBall;

    @Getter
    @Setter
    private boolean saving;

    @Builder
    public Member(int id, int classId, int level, int type, String name, int pointClan, int pointClanWeek, List<RewardDataBlackBall> rewardDataBlackBall) {
        this.id = id;
        this.classId = classId;
        this.level = level;
        this.type = type;
        this.name = name;
        this.pointClan = pointClan;
        this.pointClanWeek = pointClanWeek;
        this.rewardDataBlackBall = rewardDataBlackBall;
    }

    public void setChar(Char p) {
        this.p = p;
    }

    public Char getChar() {
        return p;
    }

    public void addPointClan(int point) {
        this.pointClan += point;
    }

    public void addPointClanWeek(int point) {
        this.pointClanWeek += point;
    }
}

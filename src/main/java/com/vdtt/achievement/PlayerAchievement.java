package com.vdtt.achievement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PlayerAchievement {


    private int id;
    @Setter(AccessLevel.NONE)
    private int count;
    private boolean received;

    @Getter(AccessLevel.NONE)
    private transient Achievement achievement;

    public Achievement getTemplate() {
        if (achievement == null) {
            achievement = AchievementList.getInstance().find(id);
        }
        return achievement;
    }

    public boolean isFinished() {
        Achievement achievement = getTemplate();
        return count >= achievement.getAmount();
    }

    public void addCount(int add) {
        this.count += add;
        Achievement achievement = getTemplate();
        if (this.count > achievement.getAmount()) {
            this.count = achievement.getAmount();
        }
    }

    public void setCount(int count) {
        this.count = count;
        Achievement achievement = getTemplate();
        if (this.count > achievement.getAmount()) {
            this.count = achievement.getAmount();
        }
    }
}

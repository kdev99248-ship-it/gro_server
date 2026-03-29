package com.vdtt.events;

import com.vdtt.constans.ColorName;
import com.vdtt.item.Item;
import com.vdtt.reward.Reward;
import lombok.Getter;

@Getter
public class EventReward extends Reward {

    private final int minTop;
    private final int maxTop;

    public EventReward(String name, int minTop, int maxTop, Item item) {
        super(-1, name, 0, item, 0);
        this.minTop = minTop;
        this.maxTop = maxTop;
    }

    @Override
    public String getName() {
        return ColorName.COLORS[minTop] + super.getName();
    }

    public String getRealName() {
        return super.getName();
    }
}

package com.vdtt.map.world.planetblackstar;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class RewardDataBlackBall {
    public RewardDataBlackBall(EnumBuffBlackBall enumBuffBlackBall, long time) {
        buff = new HashMap<>();
        buff.put(enumBuffBlackBall, time);
    }

    private final HashMap<EnumBuffBlackBall, Long> buff;
}

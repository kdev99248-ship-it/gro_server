package com.vdtt.events.points;

import lombok.Getter;

@Getter
public enum KeyPoint {

    EAT_CREAM("Ăn kem", true), KILL_CELL("Tiêu diệt Cell", true), NAP_ZENI("Nạp Zeni Tháng 9", false), MAKE_CAKE("Làm bánh", true), THA_LONG_DEN("Thả lồng đèn", true), BANH_CHUNG("Bánh chưng", true),LOI_DAI("lôi Đài", true), BANH_GIAY("Bánh giầy", true), SAN_BOSS_DRA("Săn Boss", true);

    private final String name;
    private final boolean race;

    KeyPoint(String name, boolean race) {
        this.name = name;
        this.race = race;
    }
}

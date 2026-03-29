package com.vdtt.events;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum EventType {

   // SUMMER_PARTY(1, "Đại tiệc mùa hè", SummerParty.class, LocalDateTime.of(2023, 4, 01, 19, 0, 0), LocalDateTime.of(2025, 8, 22, 0, 0, 0));
//TRUNG_THU_2024(1, "Trung thu đoàn viên", TrungThu2024.class, LocalDateTime.of(2023, 7, 20, 0, 0, 0), LocalDateTime.of(2023, 8, 22, 0, 0, 0));
    TET_NGUYEN_DAN_2025(1, "Tết Ất Tỵ", TetNguyenDan.class,  LocalDateTime.of(2023, 4, 01, 19, 0, 0), LocalDateTime.of(2025, 8, 22, 0, 0, 0));
    private final int id;
    private final String name;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Class<? extends AbsEvent> clazz;

    EventType(int id, String name, Class<? extends AbsEvent> clazz, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

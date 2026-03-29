package com.vdtt.achievement;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AchievementList {
    @Getter
    private static AchievementList instance;
    @Getter
    private final List<Achievement> achievements;

    private AchievementList() {
        this.achievements = new ArrayList<>();
    }

    public void addAchievement(Achievement achievement) {
        this.achievements.add(achievement);
    }

    public Achievement find(int id) {
        return achievements.stream().filter(achievement -> achievement.getId() == id).findFirst().orElse(null);
    }

    public static void init() {
        AchievementList achievementList = new AchievementList();
        achievementList.addAchievement(new Achievement(0, "Chiến đấu ở địa ngục", 100, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(1, "Chiến đấu trên Hành Tinh Ngục Tù", 100, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(2, "Chiến đấu ở Nhà Tù Ngân Hà", 100, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(3, "Tập luyện ở Phòng Thời Gian", 10, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(4, "Tham gia Võ Đài Vũ Trụ", 30, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(5, "Tham gia Giải Đấu Sức Mạnh", 30, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(6, "Hoàn thành nhiệm vụ Học Trò Roshi", 100, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(7, "Hoàn thành nhiệm vụ Học Trò Roshi", 1000, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(8, "Chat loa loa", 35, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(9, "Chat thế giới", 120, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(10, "Nâng cây đậu thần lên cấp 15", 1, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(11, "Nhận thưởng mỗi tuần", 10, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(12, "Nhận thưởng vũ trụ", 25, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(13, "Tiêu diệt Siêu Quái", 50, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(14, "Tiêu diệt Siêu Quái", 500, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(15, "Tiêu diệt Tinh Anh", 100, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(16, "Tiêu diệt Tinh Anh", 1000, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(17, "Tiêu diệt Thủ Lĩnh", 50, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(18, "Tiêu diệt Thủ Lĩnh", 500, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(19, "Đánh bại Boss Cao thủ", 10, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(20, "Đánh bại Boss Cao thủ", 100, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(21, "Sử dụng 1 tiện ích vĩnh viễn hoặc 20 cái loại HSD", 20, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(22, "Sử dụng 1 capsule phi thuyền vĩnh viễn hoặc 20 cái loại HSD", 20, 0, 0, 0, 100_000, "7@True@-1@1@0@0@"));
        achievementList.addAchievement(new Achievement(23, "Nhận thưởng các nhiệm vụ thành tựu", 23, 0, 0, 0, 5_000_000, "513@True@-1@1@0@0@160,1,1;149,1,1;398,10,10;327,75,75"));

        instance = achievementList;
    }
}

package com.vdtt.leaderboard;

import com.vdtt.events.EventRace;
import com.vdtt.events.points.KeyPoint;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaderboardRaceManager {

    @Getter
    private final static LeaderboardRaceManager instance = new LeaderboardRaceManager();
    @Getter
    private final List<LeaderboardRace> leaderboardRaces;
    private final AtomicInteger idGenerator = new AtomicInteger(1000);

    public LeaderboardRaceManager() {
        this.leaderboardRaces = new ArrayList<>();
        prepare();
    }

    private void prepare() {
        addLeaderboardRace(new LeaderboardRace(0, "Đua Top sức mạnh", "Điểm sức mạnh: trong quá trình luyện tập, đánh quái điểm sức mạnh của nhân vật sẽ được tăng lên. Nhân vật đạt Top 1-5 trên bảng xếp hạng sức mạnh sẽ nhận được phần thưởng có giá trị.", LocalDateTime.of(2025, 3, 29, 22, 0, 0), LocalDateTime.of(2025, 4, 8, 23, 59, 59)));
        addLeaderboardRace(new LeaderboardRace(1, "Đua Top phẩm chất", "Điểm phẩm chất: là tổng chỉ số của các vật phẩm trên người và dự phòng của nhân vật. Nhân vật đạt Top 1-10 trên bảng xếp hạng phẩm chất sẽ nhận được phần thưởng có giá trị.", LocalDateTime.of(2025, 3, 29, 22, 0, 0), LocalDateTime.of(2025, 4, 8, 23, 59, 59)));
        addLeaderboardRace(new LeaderboardRace(2, "Đua Top nạp Zeni", "Khi quy đổi Coin sang Zeni sẽ được điểm đua top. Nhân vật đạt Top 1-5 trên bảng xếp hạng Nạp Zeni sẽ nhận được phần thưởng có giá trị.", LocalDateTime.of(2025, 3, 29, 22, 0, 0), LocalDateTime.of(2025, 4, 8, 23, 59, 59)));
        addLeaderboardRace(new LeaderboardRace(3, "Đua Top Vũ Trụ", "Top Vũ Trụ: được sắp xếp theo cấp và kinh nghiệm của Vũ Trụ. Vũ Trụ đạt Top 1-5 trên bảng xếp hạng Vũ Trụ sẽ nhận được phần thưởng của Vũ Trụ (tất cả các thành viên trong Vũ Trụ đều nhận được phần thưởng)", LocalDateTime.of(2025, 3, 29, 22, 0, 0), LocalDateTime.of(2025, 4, 8, 23, 59, 59)));
//        addLeaderboardRace(new LeaderboardRace(4, "Đua Top nhi đồng sức mạnh", "Là đua top sức mạnh nhưng chỉ tính các nhân vật được tạo trong thời gian diễn ra sự kiện. Nhân vật đạt Top 1-5 trên bảng xếp hạng nhi đồng sẽ nhận được phần thưởng có giá trị.", LocalDateTime.of(2025, 3, 8, 19, 0, 0), LocalDateTime.of(2025, 3, 24, 23, 59, 59)));
//        addLeaderboardRace(new LeaderboardRace(5, "Đua Top nhi đồng phẩm chất", "Tương tự như đua top phẩm chất, nhưng chỉ dành riêng cho những nhân vật được tạo trong khoảng thời gian diễn ra đua top nhi đồng.", LocalDateTime.of(2025, 3, 8, 19, 0, 0), LocalDateTime.of(2025, 3, 24, 23, 59, 59)));
//        addLeaderboardRace(new LeaderboardRace(6, "Đua Top nạp Zeni (tuần)", "Điểm sôi nổi (tuần) tương tự như điểm sôi nổi nhưng mỗi tuần sẽ bị reset về 0. Mỗi tuần nhân vật đạt Top 1-3 trên bảng xếp hạng sôi nổi (tuần) sẽ nhận được phần thưởng có giá trị. Lưu ý: bảng xếp hạng chỉ tính đến 23h ngày cuối cùng của tuần.", LocalDateTime.of(2025, 3, 8, 19, 0, 0), LocalDateTime.of(2025, 3, 24, 23, 59, 59)));
//        addLeaderboardRace(new LeaderboardRace(7, "Đua Top ép sao (tuần)", "Điểm ép sao (tuần) được tính khi nhân vật ép sao thành công trang bị, cấp ép sao càng cao thì điểm càng nhiều, khi qua tuần mới điểm ép sao tuần sẽ bị reset về 0. Mỗi tuần nhân vật đạt Top 1-3 trên bảng xếp hạng ép sao (tuần) sẽ nhận được phần thưởng có giá trị. Lưu ý: bảng xếp hạng chỉ tính đến 23h ngày cuối cùng của tuần.", LocalDateTime.of(2025, 3, 8, 19, 0, 0), LocalDateTime.of(2025, 3, 24, 23, 59, 59)));
//        addLeaderboardRace(new LeaderboardRace(8, "Đua Top cống hiến (tuần)", "Điểm cống hiến Vũ Trụ (tuần): mỗi tuần Vũ Trụ có điểm cống hiến tuần nhiều nhất sẽ nhận được phần thưởng có giá trị. Lưu ý: chỉ Vũ Trụ đạt Top 1 mới nhận được phần thưởng.", LocalDateTime.of(2025, 3, 8, 19, 0, 0), LocalDateTime.of(2025, 3, 24, 23, 59, 59)));
//    }
}

    public int genId() {
        return idGenerator.getAndIncrement();
    }

    public void addLeaderboardRace(LeaderboardRace leaderboardRace) {
        this.leaderboardRaces.add(leaderboardRace);
    }

    public void removeLeaderboardRace(LeaderboardRace leaderboardRace) {
        this.leaderboardRaces.remove(leaderboardRace);
    }

    public EventRace getEventRaceByKey(KeyPoint key) {
        return (EventRace) leaderboardRaces.stream().filter(leaderboardRace -> leaderboardRace instanceof EventRace eventRace && eventRace.getKey() == key).findFirst().orElse(null);
    }
}

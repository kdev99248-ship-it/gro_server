package com.vdtt.leaderboard;

import com.vdtt.clan.Clan;
import com.vdtt.clan.ClanDAO;
import com.vdtt.clan.Member;
import com.vdtt.clan.MemberDAO;
import com.vdtt.db.DbManager;
import com.vdtt.events.EventRace;
import com.vdtt.events.EventReward;
import com.vdtt.events.points.EventPoint;
import com.vdtt.mail.Mail;
import com.vdtt.mail.MailManager;
import com.vdtt.model.Char;
import com.vdtt.reward.Reward;
import com.vdtt.reward.RewardManager;
import com.vdtt.server.CaoThu;
import com.vdtt.server.ServerManager;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
public class LeaderboardRace {
    @Setter
    private int id;
    private final String name;
    private final String content;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final List<Reward> rewardList;

    private boolean isPrizeGiven;
    private static final String STATUS_FILE = "eventRaceStatus.properties"; // Đường dẫn tệp lưu trạng thái
    
    @Builder
    public LeaderboardRace(int id, String name, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        int type;
        if (id < 1000) {
            if (id == 0) {
                type = 23;
            } else {
                type = 24 + id;
                if (id >= 5) {
                    type += 1;
                }
                if (id > 5) {
                    type += 2;
                }
            }
            this.rewardList = RewardManager.gI().getListByType(type);
        } else  {
            this.rewardList = new ArrayList<>();
        }

        loadPrizeStatus();
    }

    private void loadPrizeStatus() {
        Properties properties = new Properties();

        File statusFile = new File(STATUS_FILE);
        if (!statusFile.exists()) {
//            System.out.println("Tệp trạng thái chưa tồn tại. Tạo tệp mới...");
            return;  // Không cần làm gì nếu tệp không tồn tại
        }

        try (FileInputStream fis = new FileInputStream(STATUS_FILE)) {
            properties.load(fis);
            String status = properties.getProperty(String.valueOf(this.id));
            if (status != null) {
                this.isPrizeGiven = Boolean.parseBoolean(status);
            }
        } catch (IOException e) {
            System.out.println("Không thể đọc trạng thái từ tệp: " + e.getMessage());
        }
    }

    private void savePrizeStatus() {
        Properties properties = new Properties();

        File statusFile = new File(STATUS_FILE);
        if (statusFile.exists()) {
            try (FileInputStream fis = new FileInputStream(STATUS_FILE)) {
                properties.load(fis);  // Đọc các trạng thái đã có từ tệp
            } catch (IOException e) {
                System.out.println("Không thể đọc tệp trạng thái khi lưu: " + e.getMessage());
            }
        }

        properties.setProperty(String.valueOf(this.id), String.valueOf(this.isPrizeGiven));

        try (FileOutputStream fos = new FileOutputStream(STATUS_FILE)) {
            properties.store(fos, null);  // Lưu trạng thái vào tệp
        } catch (IOException e) {
            System.out.println("Không thể lưu trạng thái vào tệp: " + e.getMessage());
        }
    }

    public boolean isEnded() {
        return LocalDateTime.now().isAfter(this.endDate);
    }

    public void givePrize(Vector<?> top) {
        if (isEnded() && !isPrizeGiven) {
            isPrizeGiven = true;
            List<Reward> rewards = getRewardList();

            Map<String, List<Reward>> groupedRewards = rewards.stream()
                    .collect(Collectors.groupingBy(
                            Reward::getName, 
                            TreeMap::new, 
                            Collectors.toList() 
                    ));

            Map<String, List<Reward>> sortedGroupedRewards = groupedRewards.entrySet().stream()
                    .sorted((entry1, entry2) -> {
                        int num1 = extractNumber(entry1.getKey());
                        int num2 = extractNumber(entry2.getKey());
                        return Integer.compare(num1, num2); 
                    })
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new // Dùng LinkedHashMap để duy trì thứ tự sau khi sắp xếp
                    ));
            
            AtomicInteger atomicIndex = new AtomicInteger(0); // Sử dụng AtomicInteger để thay thế index

            sortedGroupedRewards.forEach((name, rewardList) -> {
                if (atomicIndex.get() < top.size()) {
                    if(this.id == 3) {
                        Clan cl = (Clan) top.get(atomicIndex.get());
                        ClanDAO clanDAO = Clan.getClanDAO();
                        Optional<Clan> opclan = clanDAO.get(cl.name);
                        if(opclan.isPresent()) {
                            Clan clan = opclan.get();
                            for(Member mb : clan.memberDAO.getAll()) {
                                for (Reward reward : rewardList) {
                                    sendTopRaceRewardToMail(mb.getName(), reward);
                                }
                            }
                        }
                    } else {
                        CaoThu caoThu = (CaoThu) top.get(atomicIndex.get());
                        for (Reward reward : rewardList) {
                            sendTopRaceRewardToMail(caoThu.name, reward);
                        }
                    }
                }
                atomicIndex.incrementAndGet();
            });
            savePrizeStatus();
            System.out.println("Đã Trao Quà " + this.name);
        } else if (isPrizeGiven) {
            System.out.println("Quà đã được trao rồi.");
        } else {
            System.out.println("Sự kiện chưa kết thúc, không thể trao quà.");
        }
    }

    private static int extractNumber(String str) {
        Pattern pattern = Pattern.compile("\\d+"); // Biểu thức chính quy để tìm số
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group()); // Trả về số đầu tiên tìm được
        }
        return Integer.MAX_VALUE; // Trả về số lớn nhất nếu không tìm thấy số
    }


    private void sendTopRaceRewardToMail(String namePlayer, Reward reward) {
        Char player = ServerManager.findCharByName(namePlayer);
        if (player != null) {
            sendTopRaceRewardToMail(player.mailManager, reward);
        } else {
            MailManager mailManager = DbManager.getInstance().getMailManager(namePlayer);
            if (mailManager != null) {
                sendTopRaceRewardToMail(mailManager, reward);
                DbManager.getInstance().saveMailManager(namePlayer, mailManager);
            }
        }
    }

    private void sendTopRaceRewardToMail(MailManager mailManager, Reward reward) {
        Mail mail = new Mail();
        if (mailManager.size() == 0) {
            mail.id = mailManager.size();
        } else {
            mail.id = mailManager.getID();
        }
        mail.title = reward.getName();
        mail.content = "Quà " + this.name;
        mail.sender = "Hệ thống";
        mail.time = (int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000);
        mail.item = reward.item.clone();
        if (mail.item.expire > 0) {
            mail.item.expire += System.currentTimeMillis();
        }
        mailManager.add(mail);
        if (mailManager.player != null) {
            mailManager.player.getService().updateMail();
        }
    }

    public void addReward(Reward reward) {
        this.rewardList.add(reward);
    }
}

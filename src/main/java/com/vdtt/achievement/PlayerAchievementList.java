package com.vdtt.achievement;

import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemTemplate;
import com.vdtt.model.Char;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerAchievementList {

    @Getter
    private List<PlayerAchievement> achievements;
    @Setter
    private transient Char owner;

    public PlayerAchievementList() {
        this.achievements = new ArrayList<>();
    }

    public void addAll(Collection<PlayerAchievement> achievements) {
        this.achievements.addAll(achievements);
    }

    public void synchronizeWithTemplates() {
        List<Achievement> allAchievementTemplates = AchievementList.getInstance().getAchievements();
        if (achievements.size() < allAchievementTemplates.size()) {
            for (Achievement template : allAchievementTemplates) {
                boolean isAchievementExist = false;
                for (PlayerAchievement achievement : achievements) {
                    if (achievement.getId() == template.getId()) {
                        isAchievementExist = true;
                        break;
                    }
                }
                if (!isAchievementExist) {
                    PlayerAchievement newAchievement = new PlayerAchievement();
                    newAchievement.setId(template.getId());
                    achievements.add(newAchievement);
                }
            }
        }
        for (PlayerAchievement achievement : achievements) {
            if (achievement.getCount() > achievement.getTemplate().getAmount()) {
                achievement.setCount(achievement.getTemplate().getAmount());
            }
        }
    }

    public PlayerAchievement find(int id) {
        for (PlayerAchievement achievementPlayer : achievements) {
            if (achievementPlayer.getId() == id) {
                return achievementPlayer;
            }
        }
        return null;
    }

    public void updateAchievementCount(int id, int count) {
        PlayerAchievement achievementToUpdate = find(id);
        if (achievementToUpdate != null) {
            achievementToUpdate.setCount(count);
        }
    }

    public void increaseAchievementCount(int id, int countToAdd) {
        PlayerAchievement achievementToUpdate = find(id);
        if (achievementToUpdate != null) {
            achievementToUpdate.addCount(countToAdd);
        }
    }

    public PlayerAchievement get(int index) {
        if (index >= 0 && index < achievements.size()) {
            return achievements.get(index);
        }
        return null;
    }

    public void claimReward(byte index) {
        PlayerAchievement playerAchievement = get(index);
        if (playerAchievement != null) {
            if (playerAchievement.isFinished() && !playerAchievement.isReceived()) {
                Achievement achievement = playerAchievement.getTemplate();
                if (achievement.getGold() > 0) {
                    owner.addZeni(achievement.getGold(), true);
                }
                if (achievement.getSilver() > 0) {
                    owner.addBallz(achievement.getSilver(), true);
                }
                if (achievement.getBronze() > 0) {
                    owner.addCoin(achievement.getBronze(), true);
                }
                Item item = achievement.toItem();
                if (item != null) {
                    if (!item.strOption.isEmpty()) {
                        ItemTemplate template = item.getTemplate();
                        if (template.gioiTinh != 2 && template.gioiTinh != owner.gender) {
                            List<ItemTemplate> items = ItemManager.getInstance().itemTemplates;
                            for (ItemTemplate itemTemplate : items) {
                                if (itemTemplate.gioiTinh == owner.gender && itemTemplate.getType() == template.getType() && itemTemplate.levelNeed== template.levelNeed) {
                                    item.id = itemTemplate.id;
                                    item.template = itemTemplate;
                                    break;
                                }
                            }
                        }

                        if (item.getTemplate().getType() == 13) {
                            item.sys = owner.sys;
                            switch(item.sys) {
                                case 1:
                                    item.strOption = "54,0,500;62,0,500";
                                    break;
                                case 2:
                                    item.strOption = "55,0,500;58,0,500";
                                    break;
                                case 3:
                                    item.strOption = "56,0,500;59,0,500";
                                    break;
                                case 4:
                                    item.strOption = "57,0,500;60,0,500";
                                    break;
                                case 5:
                                    item.strOption = "53,0,500;61,0,500";
                                    break;
                            }
                        }
                    }
                    owner.addItemToBag(item);
                    owner.getService().addItem(item);
                }
                playerAchievement.setReceived(true);
                owner.getService().sendAchievementInfo();
            }
        }
    }
}

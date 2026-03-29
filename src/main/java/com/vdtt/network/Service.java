package com.vdtt.network;

import com.vdtt.achievement.PlayerAchievement;
import com.vdtt.achievement.PlayerAchievementList;
import com.vdtt.clan.Clan;
import com.vdtt.clan.Member;
import com.vdtt.data.Caption;
import com.vdtt.data.DataCenter;
import com.vdtt.db.DbManager;
import com.vdtt.effect.Effect;
import com.vdtt.effect.EffectTemplate;
import com.vdtt.effect.EffectTemplateManager;
import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemOptionTemplate;
import com.vdtt.item.ItemTemplate;
import com.vdtt.leaderboard.LeaderboardRace;
import com.vdtt.leaderboard.LeaderboardRaceManager;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.Zone;
import com.vdtt.mob.Mob;
import com.vdtt.model.*;
import com.vdtt.party.Group;
import com.vdtt.party.MemberGroup;
import com.vdtt.reward.Reward;
import com.vdtt.reward.RewardManager;
import com.vdtt.server.CaoThu;
import com.vdtt.stall.Stall;
import com.vdtt.store.ItemStore;
import com.vdtt.task.TaskOrder;
import com.vdtt.util.Log;
import com.vdtt.util.Util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.*;

public class Service {

    private Session session;
    private Char player;

    public Service(Session session) {
        this.session = session;
    }

    public void setChar(Char pl) {
        this.player = pl;
    }

    public void sendTimeInMap(long createdAt, long maxTime, boolean bZ) {
        try {
            Message ms = new Message((byte) -123);
            ms.writer().writeByte(-80);
            ms.writer().writeLong(createdAt);
            ms.writer().writeInt((int) maxTime);
            ms.writer().writeBoolean(bZ);
            ms.writer().flush();
            sendMessage(ms);
        } catch (Exception e) {

        }
    }

    public void serverDialog(String s) {
        try {
            Message m = new Message((byte) -110);
            m.writeUTF(s);
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateData() {
        try {
            Message m = new Message((byte) -122);
            m.writer().writeByte(-113);
            m.writer().writeShort(DataCenter.gI().itemOptionTemplate.length);
            for (int i = 0; i < DataCenter.gI().itemOptionTemplate.length; i++) {
                ItemOptionTemplate itemOptionTemplate = DataCenter.gI().itemOptionTemplate[i];
                m.writeUTF(itemOptionTemplate.text);
                m.writer().writeByte(itemOptionTemplate.type);
                m.writer().writeByte(itemOptionTemplate.level);
                m.writeUTF(itemOptionTemplate.strOption);
            }
            List<EffectTemplate> effectTemplates = EffectTemplateManager.getInstance().getList();
            m.writer().writeShort(effectTemplates.size());
            for (EffectTemplate eff : effectTemplates) {
                m.writeUTF(eff.name);
                m.writeUTF(eff.detail);
                m.writer().writeByte(eff.type);
                m.writer().writeShort(eff.idIcon);
                m.writer().writeShort(eff.idAura);
            }
            m.writer().writeShort(ItemManager.getInstance().itemTemplates.size());
            for (int i = 0; i < ItemManager.getInstance().itemTemplates.size(); i++) {
                ItemTemplate item = ItemManager.getInstance().getItemTemplate(i);
                m.writeUTF(item.name);
                m.writeUTF(item.detail);
                m.writer().writeBoolean(item.isXepChong);
                m.writer().writeByte(item.gioiTinh);
                m.writer().writeByte(item.type);
                m.writer().writeByte(item.typeChar);
                m.writer().writeShort(item.idIcon);
                m.writer().writeByte(item.levelNeed);
                m.writer().writeShort(item.phamChatNeed);
                m.writer().writeShort(item.width);
                m.writer().writeShort(item.height);
            }
            m.writer().write(DataCenter.gI().data2);
            m.inflate = true;
            session.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTabSelectChar(User user) {
        try {
            Message m = new Message((byte) -122);
            m.writer().writeByte(-128);
            m.writer().writeByte(user.isCreateChar ? 1 : 0);
            if (user.isCreateChar) {
                user.initCharacterList();
                Char pl = user.chars.get(0);
                m.writer().writeInt(pl.id);
                m.writer().writeByte(pl.status);//status
                m.writeUTF(pl.name);//name
                m.writer().writeByte(pl.idCharSys);//idCharsystem
                m.writer().writeByte(pl.gender);// gioi tinh
                m.writer().writeByte(pl.sys);//sys
                m.writer().writeByte(pl.m);//m
                m.writer().writeByte(pl.m);//l
                m.writer().writeShort(pl.speed);//type pk
                m.writer().writeInt(pl.hp);//hp
                m.writer().writeInt(pl.maxHP);//hp full
                m.writer().writeInt(pl.mp);//mp
                m.writer().writeInt(pl.maxMP);//mp full
                m.writer().writeLong(pl.exp);//exp
                m.writer().writeShort(pl.x);//cx
                m.writer().writeShort(pl.y);//cy
                m.writer().writeByte(pl.lvPK);//level pk
                m.writeUTF("");//guild
                pl.writeItemBody(m);
                pl.getEm().write(m);
                pl.showDanhHieu(m);
                m.writer().writeByte(pl.rank);//rank
                m.writer().writeByte(pl.bp);//bp
                m.writer().writeByte(0);//
                m.writer().writeBoolean(false);
            }
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void openGUIPoPo() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(113);
            m.writer().writeByte(15);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void selectChar(User user) {
        try {
            setChar(user.sltChar);
            sendInfo(user);
            player.setService(this);
            user.isLoginFinish = true;
            player.lastLoginTime = System.currentTimeMillis();
            try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE `players` SET `online` = ?, `last_login_time` = ? WHERE `id` = ? LIMIT 1;")) {
                stmt.setInt(1, 1);
                stmt.setLong(2, player.lastLoginTime);
                stmt.setInt(3, player.id);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTabLogin() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-126);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfo(User user) {
        try {
            Message m = new Message((byte) -122);
            m.writer().writeByte(-127);
            m.writeUTF(user.username);
            m.writer().writeInt(player.id);
            m.writeUTF(player.name);
            m.writer().writeByte(player.gender);
            m.writer().writeByte(player.idCharSys);
            m.writer().writeByte(0);
            m.writer().writeByte(player.sys);
            m.writer().writeByte(player.m);
            m.writer().writeByte(player.lvPK);
            m.writer().writeInt(9999);//an?
            m.writer().writeShort(player.speed);
            m.writer().writeInt(player.maxHP);
            m.writer().writeInt(player.hp);
            m.writer().writeInt(player.maxMP);
            m.writer().writeInt(player.mp);
            m.writer().writeLong(player.exp);
            m.writer().writeInt(player.ballZ);
            m.writer().writeInt(player.coin);
            m.writer().writeInt(player.zeni);
            m.writer().writeInt(player.kimCuong);
            m.writer().writeShort(player.taskId);
            if (player.taskMain != null) {
                m.writer().writeByte(player.taskMain.index);
                m.writer().writeShort(player.taskMain.count);
            } else {
                m.writer().writeByte(-1);
                m.writer().writeShort(0);
            }
            m.writer().writeInt(player.mocNap);// moc nap
//            (int)((System.currentTimeMillis() / 1000) + 604800)
            m.writer().writeInt(player.TimeChatColor);//time chat color
            m.writer().writeShort(player.numberCellBag);
            m.writer().writeByte(0);//size gi do
            player.writeItemBody(m);
            player.writeItemBody2(m);//body2
            List<Item> bag = Util.cleanArrayItem(player.bag);
            m.writer().writeShort(bag.size());// bag
            for (int i = 0; i < bag.size(); i++) {
                bag.get(i).write(m);
            }
            player.getEm().write(m);
            player.mailManager.write(m);// size letter
            Friend[] friends = player.getFriends();
            m.writer().writeShort(friends.length);//size friend
            for (int i = 0; i < friends.length; i++) {
                Friend friend = friends[i];
                m.writeUTF(friend.name);
                m.writer().writeByte(friend.type);
                m.writer().writeBoolean(friend.isFriend);
            }
            m.writer().writeShort(0);//size enemy
            m.writer().writeLong(player.KI);// ki
            m.writer().writeShort(0);//id skill
            m.writer().writeShort(30);//skill select
            m.writer().writeShort(player.skills.size());//size skill
            for (int i = 0; i < player.skills.size(); i++) {
                m.writer().writeShort(player.skills.get(i).id);
                m.writer().writeLong(player.skills.get(i).timeCoolDown);
            }
            if (player.clan != null) {
                m.writeUTF(player.clan.name);
                m.writeUTF(player.clan.main_name);
                m.writer().writeByte(player.clan.getMemberByName(player.name).getType());
                m.writer().writeShort(0);
            } else {
                m.writeUTF("");//guild
            }
            player.showDanhHieu(m);
            m.writer().writeInt(player.timeTienIch);//khong ro
            m.writer().writeByte(player.rank);
            m.writer().writeByte(player.bp);
            m.writer().writeByte(player.levelBean);// cap cay dau
            m.writer().writeInt(player.timeReciveBean);//time cay dau
            m.writer().writeInt(player.timeChangeSkin);//cW
            m.writer().writeInt(player.timeWaitChangeSkin);//cX
            m.writer().writeLong(0);//dk
            m.writer().writeByte(0);//cy
            m.writer().writeInt(0);//hp max de tu
            m.writer().writeInt(0);//hp de tu
            m.writer().writeInt(0);//mp max de tu
            m.writer().writeInt(0);//mp de tu
            m.writer().writeByte(0);//type de
            m.writer().writeByte(player.sachChienDau);
            m.writer().writeByte(4);
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendIntoGame(Char pl) {
        try {
            Message m = new Message((byte) -104);
            m.writer().writeByte(-1);
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateTienIch() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-72);
            m.writer().writeInt(player.timeTienIch);
            m.writer().writeByte(player.rank);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void notifyNoUpKi() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-1);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateMove(boolean isStop) {
        try {
            int[] byteArray = Util.getXYFake(player.x, player.y, MapManager.getInstance().getMapTemplates().get(player.mapId).maxX);
            Message m = new Message(isStop ? (byte) -84 : (byte) 123);
            DataOutputStream dos = m.writer();
            dos.writeInt(player.id);
            dos.writeByte(byteArray[0]);
            dos.writeByte(byteArray[1]);
            dos.writeByte(byteArray[2]);
            dos.flush();
            session.sendMessage(m);
            player.zone.getService().sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void setColorDanhHieu(String nameDanhHieu, int color1, int color2) {
        try {
            Message message = new Message((byte) -121);
            message.writer().writeByte(0);
            message.writeUTF(nameDanhHieu);
            message.writer().writeInt(Integer.MIN_VALUE);
            message.writer().writeInt(Integer.MIN_VALUE);
            message.writer().writeInt(Integer.MIN_VALUE);
            message.writer().writeInt(color1);
            message.writer().writeInt(color2);
            sendMessage(message);
        } catch (Exception ex) {

        }
    }

    public void sendMessage(Message ms) {
        if (this.session != null && !player.isCleaned) {
            this.session.sendMessage(ms);
        }
    }

    public void sendZone(Zone zone, int typeTau) {
        try {
            Message m = new Message((byte) -103);
            DataOutputStream dos = m.writer();
            dos.writeShort(zone.id);
            dos.writeShort(zone.template.id);
            dos.writeShort(player.x);
            dos.writeShort(player.y);
            zone.writeItemMap(m);
            zone.writeChar(m);
            zone.writeMob(m);
            zone.writeNpc(m);
            dos.writeByte(0);
            if (zone.map.mapTemplate.isPhoBan()) {
                dos.writeLong(zone.worldCreatedAt);
                dos.writeInt((int) zone.worldMaxTime);
                dos.writeBoolean(zone.timeInMap);
                if (zone.map.mapTemplate.type != 19 && zone.map.mapTemplate.type != 20 && zone.map.mapTemplate.type != 15) {
                    if (zone.map.mapTemplate.type == 8) {
                        dos.writeByte(1);
                    }
                } else {
                    dos.writeByte(1);
                }
//                dos.writeByte(4);
            }
            dos.writeBoolean(false);
            dos.writeByte(typeTau);
            dos.writeBoolean(false);
            dos.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showPet() {
        try {
            Message m = new Message((byte) -123);
            DataOutputStream dos = m.writer();
            dos.writeByte(6);
            Pet pet = player.pet;
            dos.writeInt(pet.maxHP);
            dos.writeInt(pet.hp);
            dos.writeInt(pet.maxMP);
            dos.writeInt(pet.mp);
            dos.writeInt(pet.damage);
            dos.writeInt(pet.damegeMonster);
            dos.writeShort(pet.exactly);
            dos.writeShort(pet.miss);
            dos.writeShort(pet.crit);
            dos.writeShort(pet.armor);
            dos.writeShort(pet.reduceDame);
            dos.writeShort(pet.reduceCrit);
            dos.writeShort(pet.reduceSuyGiam);
            dos.writeShort(pet.reduceChiuDon);
            dos.writeShort(pet.reduceStun);
            dos.writeShort(pet.suyGiam);
            dos.writeShort(pet.chiuDon);
            dos.writeShort(pet.stun);
            dos.writeShort(pet.randomDame);
            dos.writeShort(pet.speed);
            dos.writeShort(pet.counterDame);
            dos.writeShort(pet.skipMiss);
            dos.writeShort(pet.armorPenetration);
            dos.writeShort(pet.criticalHitPower);
            dos.writeShort(pet.reduceCritHitPower);
            dos.writeInt(0);// time hoi sinh
            dos.writeByte(pet.typePet);// type pet
            dos.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateSkillCaiTrang() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(25);
            m.writer().writeShort(player.options[327]);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateSkill() {
        try {
            Message m = new Message((byte) 126);
            DataOutputStream dos = m.writer();
            dos.writeLong(player.KI);
            dos.writeShort(0);
            dos.writeShort(30);
            dos.writeShort(player.skills.size());
            for (int i = 0; i < player.skills.size(); i++) {
                dos.writeShort(player.skills.get(i).id);
                dos.writeLong(player.skills.get(i).timeCoolDown);
            }
            dos.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateSkillNoCoolDown() {
        try {
            Message m = new Message((byte) 126);
            DataOutputStream dos = m.writer();
            dos.writeLong(player.KI);
            dos.writeShort(0);
            dos.writeShort(30);
            dos.writeShort(player.skills.size());
            for (int i = 0; i < player.skills.size(); i++) {
                dos.writeShort(player.skills.get(i).id);
                dos.writeLong(0);
            }
            dos.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void mobMove(int id, short x, short y) {
        try {
            Message m = new Message((byte) -1);
            DataOutputStream dos = m.writer();
            dos.writeShort(id);
            dos.writeShort(x);
            dos.writeShort(y);
            dos.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void showInfo2(Char player) {
        try {
            Message m = new Message((byte) -123);
            DataOutputStream dos = m.writer();
            dos.writeByte(-73);
            dos.writeInt(player.maxHP);
            dos.writeInt(player.hp);
            dos.writeInt(player.maxMP);
            dos.writeInt(player.mp);
            dos.writeByte(player.lvPK);
            dos.writeShort(player.increaseXu);
            int increaseKI = player.increaseKI;
            Effect eff = player.getEm().findByID(42);
            if (eff != null) {
                increaseKI += (short) (eff.getParam() * 10);
            }
            eff = player.getEm().findByID(205);
            if (eff != null) {
                increaseKI += (short) (eff.getParam() * 10);
            }
            eff = player.getEm().findByID(206);
            if (eff != null) {
                increaseKI += (short) (eff.getParam() * 10);
            }
            eff = player.getEm().findByID(207);
            if (eff != null) {
                increaseKI += (short) (eff.getParam() * 10);
            }
            dos.writeShort(increaseKI);
            dos.writeByte(player.increaseExtra);
            dos.writeInt(player.pointPhamChat);
            dos.writeShort(player.pointEnthusiastic);
            dos.writeShort(player.pointEnthusiasticWeek);
            dos.writeInt(player.pointUpgrageStar);
            dos.writeInt(player.pointUpgrageStarWeek);
            dos.writeShort(player.pointKame);
            dos.writeInt(player.pointSuper);
            dos.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDame(Char player) {
        try {
            Message m = new Message((byte) 63);
            DataOutputStream dos = m.writer();
            dos.writeByte(player.typeSelectOutChar);
            dos.writeInt(player.damage);
            dos.writeInt(player.damegeMonster);
            dos.writeShort(player.exactly);
            dos.writeShort(player.miss);
            dos.writeShort(player.crit);
            dos.writeShort(player.armor);
            dos.writeShort(player.reduceDame);
            dos.writeShort(player.reduceCrit);
            dos.writeShort(player.reduceSuyGiam);
            dos.writeShort(player.reduceChiuDon);
            dos.writeShort(player.reduceStun);
            dos.writeShort(player.suyGiam);
            dos.writeShort(player.chiuDon);
            dos.writeShort(player.stun);
            dos.writeShort(player.randomDame);
            dos.writeShort(player.speed);
            dos.writeShort(player.counterDame);
            dos.writeShort(player.skipMiss);
            dos.writeShort(player.armorPenetration);
            dos.writeShort(player.criticalHitPower);
            dos.writeShort(player.reduceCritHitPower);
            dos.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItem(byte type, int index) {
        try {
            Message m = new Message((byte) -16);
            m.writer().writeByte(type);
            m.writer().writeShort(index);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNullItemBag(int index) {
        try {
            Message m = new Message((byte) 110);
            m.writer().writeShort(index);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void useItem(Item item) {
        try {
            Message m = new Message((byte) 116);
            m.writer().writeShort(item.index);
            m.writer().writeBoolean(item.isLock);
            if (!item.isTrangBi()) {
                m.writer().writeInt(item.getQuantity());
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void themItem(Item item) {
        try {
            Message m = new Message((byte) 120);
            m.writer().writeShort(1);
            item.write(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItem(Item item) {
        try {
            Message m = new Message((byte) -4);
            item.write(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addListItem(List<Item> items) {
        try {
            Message m = new Message((byte) 120);
            m.writer().writeShort(items.size());
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                item.write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemBodyToBag(short indexUI, int i) {
        try {
            Message m = new Message((byte) 113);
            m.writer().writeByte(indexUI);
            m.writer().writeShort(i);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serverMessage(String message) {
        try {
            Message m = new Message((byte) -107);
            m.writeUTF(message);
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void warningMessage(String message) {
        try {
            Message m = new Message((byte) -105);
            m.writeUTF(message);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void yellowMessage(String message) {
        try {
            Message m = new Message((byte) -106);
            m.writeUTF(message);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showKI() {
        try {
            Message m = new Message((byte) -56);
            m.writer().writeLong(player.KI);
            m.writer().writeLong(player.exp);
            m.writer().writeInt(player.id);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showKI(Char pl) {
        try {
            Message m = new Message((byte) -62);
            m.writer().writeLong(pl.KI);
            m.writer().writeLong(pl.exp);
            m.writer().writeInt(pl.id);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMob(short id) {
        try {
            Mob mob = player.zone.findMoLivebByID(id);
            Message m = new Message((byte) 13);
            m.writer().writeShort(mob.idEntity);
            m.writer().writeInt(mob.kiNow * 10);
            m.writer().writeShort(mob.dameMax);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openUIMenu(int id) {
        try {
            ArrayList<Menu> menus = player.getMenus();
            Message m = new Message((byte) 54);
            m.writer().writeShort(id);
            StringBuilder sb = new StringBuilder();
            for (Menu menu : menus) {
                sb.append(menu.getName()).append(";");
            }
            sb.deleteCharAt(sb.length() - 1);
            m.writeUTF(sb.toString());
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void menu(int id) {
        try {
            Message m = new Message((byte) 54);
            m.writer().writeShort(id);
            m.writeUTF("MENU ID: " + id);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateTitle() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-75);
            m.writer().writeInt(player.id);
            player.showDanhHieu(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openTabZone() {
        try {
            Message m = new Message((byte) -6);
            Map map = MapManager.getInstance().getMaps().get(player.mapId);
            List<Zone> zones = map.getZones();
            m.writer().writeByte(zones.size());
            m.writer().writeByte(player.zone.id);
            m.writer().writeByte(player.zone.getNumberChar());
            m.writer().writeByte(zones.size());
            for (Zone zone : zones) {
                m.writer().writeByte(zone.id);
                m.writer().writeByte(map.maxPlayer);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openBox() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(50);
            m.writer().writeShort(player.numberCellBox);
            List<Item> items = Util.cleanArrayItem(player.box);
            m.writer().writeShort(items.size());
            for (Item item : items) {
                item.write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void itemBagToBox(int indexM, int i) {
        try {
            Message m = new Message((byte) 115);
            m.writer().writeShort(indexM);
            m.writer().writeShort(i);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void itemBoxToBag(int indexM, int i) {
        try {
            Message m = new Message((byte) 114);
            m.writer().writeShort(indexM);
            m.writer().writeShort(i);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openGUI(int id) {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(id);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openMaBaoVe(Char _myChar) {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(97);
            m.writer().writeBoolean(!_myChar.MaBaoVe.isEmpty());
            m.writer().writeBoolean(_myChar.isUnlock());
            m.writer().writeInt(_myChar.TimeReset);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTextNPC(String str, String str2) {
        try {
            Message m = new Message((byte) 5);
            m.writeUTF(str);
            m.writeUTF(str2);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendLevelBean() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-26);
            m.writer().writeByte(player.levelBean);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendTimeReciveBean() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-27);
            m.writer().writeInt(player.timeReciveBean);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateHP() {
        try {
            Message m = new Message((byte) 66);
            m.writer().writeInt(player.hp);
            if (player.hp <= 0) {
                m.writer().writeShort(player.x);
                m.writer().writeShort(player.y);
                m.writeUTF("");
            }
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void updateHPFull() {
        try {
            Message m = new Message((byte) 67);
            m.writer().writeInt(player.maxHP);
            m.writer().writeInt(player.hp);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void npcAttackMe(Mob mob) {
        try {
            Message m = new Message((byte) 50);
            m.writer().writeShort(mob.idEntity);
            m.writer().writeInt(player.id);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }
    
    public void openPopupSoLuong(byte type) {
        try {
            player.typePopup = type;
            Message message = new Message((byte) 122);
            message.writer().writeByte(52);
            player.getService().sendMessage(message);
        }
        catch (Exception e) {}
    }

    public void openPopupIdGame(byte type) {
        try {
            player.typePopup = type;
            Message message = new Message((byte) 122);
            message.writer().writeByte(51);
            player.getService().sendMessage(message);
        }
        catch (Exception e) {}
    }

    public void openUIShop(byte type, List<ItemStore> items) {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(type);
            m.writer().writeShort(items.size());
            for (int i = 0; i < items.size(); i++) {
                ItemStore item = items.get(i);
                m.writer().writeShort(item.getId());
                m.writer().writeShort(item.getItemID());
                m.writer().writeBoolean(item.isLock());
                m.writer().writeLong(item.getExpire());
                m.writeUTF(item.getStrOptions());
                m.writer().writeInt(item.getDiamond());
                m.writer().writeInt(item.getZeni());
                m.writer().writeInt(item.getCoin());
                m.writer().writeInt(item.getCoinClan());
                m.writer().writeInt(item.getBallz());
                m.writer().writeInt(item.getRequire());
                if (type == 39) {
                    com.vdtt.model.Reward reward = player.findReward(item.getItemID());
                    m.writer().writeBoolean(reward != null);
                    for (int j = 0; j < 3; j++) {
                        Item it = item.getItems().get(j);
                        it.write(m);
                    }
                } else if (type == 40) {
                    m.writer().writeInt(0);
                    m.writer().writeInt(0);
                }
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateBallz(boolean isUpdate) {
        try {
            Message m = new Message((byte) 90);
            m.writer().writeInt(player.ballZ);
            m.writer().writeBoolean(isUpdate);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateCoin(boolean isUpdate) {
        try {
            Message m = new Message((byte) 91);
            m.writer().writeInt(player.coin);
            m.writer().writeBoolean(isUpdate);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateZeni(boolean isUpdate) {
        try {
            Message m = new Message((byte) 92);
            m.writer().writeInt(player.zeni);
            m.writer().writeBoolean(isUpdate);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateZeniLock(boolean isUpdate) {
        try {
            Message m = new Message((byte) 93);
            m.writer().writeInt(player.zeniLock);
            m.writer().writeBoolean(isUpdate);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void buyItem(List<Item> items) {
        try {
            Message m = new Message((byte) 121);
            m.writer().writeInt(player.ballZ);
            m.writer().writeInt(player.coin);
            m.writer().writeInt(player.zeni);
            m.writer().writeInt(player.kimCuong);
            m.writer().writeShort(items.size());
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                item.write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void addItem(Item item) {
        try {
            Message m = new Message((byte) 109);
            item.write(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMP() {
        try {
            Message m = new Message((byte) 64);
            m.writer().writeInt(player.mp);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void updateMPFull() {
        try {
            Message m = new Message((byte) 64);
            m.writer().writeInt(player.mp);
            m.writer().writeInt(player.maxMP);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void sendTimeItem(short type) {
        try {
            Message m = new Message((byte) 3);
            m.writer().writeInt(player.id);
            m.writer().writeShort(type);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void sortItem(byte type) {
        try {
            Message m = new Message((byte) 117);
            m.writer().writeByte(type);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void upgrade(boolean isUpgrade, boolean isBua, List<Item> da, Item item, short index, int type) {
        try {
            Message m = new Message((byte) 107);
            m.writer().writeBoolean(isUpgrade);
            m.writer().writeBoolean(isBua);
            m.writer().writeInt(player.ballZ);
            m.writer().writeInt(player.coin);
            m.writer().writeByte(da.size());
            for (int i = 0; i < da.size(); i++) {
                m.writer().writeShort(da.get(i).index);
            }
            if (!isBua) {
                m.writer().writeShort(-1);
            } else {
                m.writer().writeShort(index);
            }
            item.write(m);
            m.writer().writeByte(type);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendSachChienDau() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-67);
            m.writer().writeByte(player.sachChienDau);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void thongBao(Message mss) {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(111);
            m.writeUTF("TEST");
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void showMenuItem(int index, String str, String menu2) {
        try {
            Message m = new Message((byte) -25);
            m.writer().writeShort(index);
            m.writeUTF(str);
            m.writeUTF(menu2);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void showCaiTrangGhep(Item item, int size) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-47);
            item.write(m);
            m.writer().writeInt(200000 * size);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void ghepCaiTrang(byte size, byte[] typeTemp, short[] indexArr, Item item) {
        try {
            Message m = new Message((byte) -50);
            m.writer().writeByte(size);
            for (int i = 0; i < size; i++) {
                m.writer().writeByte(typeTemp[i]);
                if (i == 0) {
                    item.write(m);
                } else {
                    m.writer().writeShort(indexArr[i]);
                }
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateItemBody2(int index) {
        try {
            Message m = new Message((byte) 36);
            m.writer().writeShort(index);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void itemBody2ToBag(byte indexUI, int i) {
        try {
            Message m = new Message((byte) 37);
            m.writer().writeByte(indexUI);
            m.writer().writeShort(i);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateItemBodyChange() {
        try {
            Message m = new Message((byte) 35);
            player.writeItemBody(m);
            player.writeItemBody2(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void showCaiTrangTach(List<Item> items) {
        try {
            Message m = new Message((byte) -52);
            m.writer().writeByte(items.size());
            for (int i = 0; i < items.size(); i++) {
                items.get(i).write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void tachCaiTrang(byte type, short index, Item itemGhep) {
        try {
            Message m = new Message((byte) -51);
            m.writer().writeByte(type);
            m.writer().writeShort(index);
            itemGhep.write(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void upPearl(Vector<Item> crystals, Item da) {
        try {
            Message m = new Message((byte) 108);
            m.writer().writeInt(player.ballZ);
            m.writer().writeInt(player.coin);
            m.writer().writeByte(crystals.size());
            for (int i = 0; i < crystals.size(); i++) {
                m.writer().writeShort(crystals.get(i).index);
            }
            da.write(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void ducLoItem(byte type, short index, Item item) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-42);
            m.writer().writeByte(type);
            m.writer().writeShort(index);
            item.write(m);
            m.writer().writeBoolean(false);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void khamNgoc(Vector<Item> ngoc, Item item, int type) {
        try {
            Message m = new Message((byte) -46);
            for (int i = 0; i < ngoc.size(); i++) {
                m.writer().writeShort(ngoc.get(i).index);
                if (i == 0) {
                    item.write(m);
                    m.writer().writeByte(type);
                }
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void hopNhatItem(boolean success, Item item, Item item2, int type, int type2) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-25);
            m.writer().writeBoolean(success);
            if (success) {
                item.write(m);
                m.writer().writeByte(type);
                item2.write(m);
                m.writer().writeByte(type2);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItemHopNhat(boolean isWin, Item delete, int typeDelete, Item item, int typeItem) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-25);
            m.writer().writeBoolean(isWin);
            if (isWin) {
                delete.write(m);
                m.writer().writeByte(typeDelete);
                item.write(m);
                m.writer().writeByte(typeItem);
            }
            sendMessage(m);
        } catch (IOException e) {

        }
    }

    public void tachKham(Item item, byte type) {
        try {
            Message m = new Message((byte) -47);
            item.write(m);
            m.writer().writeByte(type);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void convertUpgrade(int type, Item item, int type2, Item item2, int index) {
        try {
            Message m = new Message((byte) 104);
            item.write(m);
            m.writer().writeByte(type);
            item2.write(m);
            m.writer().writeByte(type2);
            m.writer().writeShort(index);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }

    }

    public void splitItem(byte type, Item item1) {
        try {
            Message m = new Message((byte) 105);
            item1.write(m);
            m.writer().writeByte(type);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void hopNhatItem() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(108);
            m.writer().writeByte(0);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void showShopNoItem(byte type) {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(type);
            m.writer().writeShort(0);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openNapLanDau() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(89);
            for (int i = 0; i < 9; i++) {
                Reward reward = RewardManager.gI().find(477 + i);
                Item item = reward.item;
                item.write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void updateMail() {
        try {
            Message m = new Message((byte) 97);
            player.mailManager.write(m);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void splitItemBag(short index, int quantity, short index2, int quantity2) {
        try {
            Message m = new Message((byte) 118);
            m.writer().writeShort(index);
            m.writer().writeInt(quantity);
            m.writer().writeShort(index2);
            m.writer().writeInt(quantity2);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void saleItem(short index) {
        try {
            Message m = new Message((byte) 119);
            m.writer().writeInt(player.coin);
            m.writer().writeInt(player.ballZ);
            m.writer().writeShort(index);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openRewardDaily() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(88);
            m.writer().writeInt(player.timeOnline);// Time online
            m.writer().writeByte(player.ngayOnline);// So ngay online
            m.writer().writeInt(player.mocNap);// tich luy nap
            m.writer().writeInt(player.napNgay);// nap ngay
            m.writer().writeLong(player.totalTimeOnline);// online goi
            m.writer().writeShort(player.totalTA);// so tinh anh
            m.writer().writeShort(player.totalTL);// so thu linh
            m.writer().writeShort(player.totalSQ);// so sieu quai
            m.writer().writeByte(player.totalKiNang);// So ky nang da hoc
            m.writer().writeBoolean(player.packOnline);
            m.writer().writeBoolean(player.packTinhAnh);
            m.writer().writeBoolean(player.packThuLinh);
            m.writer().writeBoolean(player.packSieuQuai);
            m.writer().writeLong(-1);
            m.writer().writeLong(-1);
            m.writer().writeInt(0);
            m.writer().writeShort(player.countVongQuay);
            m.writer().writeShort(RewardManager.gI().rewards.size());
            for (int i = 0; i < RewardManager.gI().rewards.size(); i++) {
                Reward reward = RewardManager.gI().rewards.get(i);
                m.writer().writeShort(reward.id);
                m.writeUTF(reward.getName());
                m.writer().writeShort(reward.type);
                reward.item.write(m);
                m.writer().writeByte(player.checkReward(reward));// co the nhan hay khong
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePhucLoi(short id) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-70);
            m.writer().writeShort(id);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void drawRewardDaily(int id, int quantity) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-60);
            m.writer().writeShort(player.countVongQuay);
            m.writer().writeByte(id);
            m.writer().writeInt(quantity);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadRewarDaily() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-61);
            m.writer().writeInt(player.timeOnline);// Time online
            m.writer().writeByte(player.ngayOnline);// So ngay online
            m.writer().writeInt(player.mocNap);// tich luy nap
            m.writer().writeInt(player.napNgay);// nap ngay
            m.writer().writeLong(player.totalTimeOnline);// online goi
            m.writer().writeShort(player.totalTA);// so tinh anh
            m.writer().writeShort(player.totalTL);// so thu linh
            m.writer().writeShort(player.totalSQ);// so sieu quai
            m.writer().writeByte(player.totalKiNang);// So ky nang da hoc
            m.writer().writeBoolean(player.packOnline);
            m.writer().writeBoolean(player.packTinhAnh);
            m.writer().writeBoolean(player.packThuLinh);
            m.writer().writeBoolean(player.packSieuQuai);
            m.writer().writeLong(-1);
            m.writer().writeLong(-1);
            m.writer().writeInt(0);
            m.writer().writeShort(player.countVongQuay);
            m.writer().writeShort(RewardManager.gI().rewards.size());
            for (int i = 0; i < RewardManager.gI().rewards.size(); i++) {
                Reward reward = RewardManager.gI().rewards.get(i);
                m.writer().writeShort(reward.id);
                m.writeUTF(reward.getName());
                m.writer().writeShort(reward.type);
                reward.item.write(m);
                m.writer().writeByte(player.checkReward(reward));// co the nhan hay khong
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSlotBag() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-22);
            m.writer().writeShort(player.numberCellBag);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetScreen() {
        try {
            Message m = new Message((byte) -14);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendDuaTop() {
        try {
            List<LeaderboardRace> list = LeaderboardRaceManager.getInstance().getLeaderboardRaces();
            Message m = new Message((byte) -123);
            m.writer().writeByte(-57);
            m.writer().writeByte(list.size());
            for (LeaderboardRace leaderboardRace : list) {
                LocalDateTime startDate = leaderboardRace.getStartDate();
                LocalDateTime endDate = leaderboardRace.getEndDate();

                m.writeUTF(leaderboardRace.getName());
                m.writeUTF(leaderboardRace.getContent());
                m.writer().writeLong(Util.toEpochMilli(startDate));// start time
                m.writer().writeLong(Util.toEpochMilli(endDate));// end time
                List<Reward> rewardList = leaderboardRace.getRewardList();
                m.writer().writeByte(rewardList.size());
                for (Reward rw : rewardList) {
                    m.writeUTF(rw.getName());
                    rw.item.write(m);
                }
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outParty() {
        try {
            Message m = new Message((byte) 43);
            m.writer().writeBoolean(false);
            m.writer().writeByte(0);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFindParty(HashMap<String, Group> groups) {
        try {
            Message m = new Message((byte) 45);
            DataOutputStream ds = m.writer();
            ds.writeByte(groups.size());
            for (Group g : groups.values()) {
                MemberGroup party = g.memberGroups.get(0);
                ds.writeBoolean(g.isTuVao);
                ds.writeByte(g.memberGroups.size());
                ds.writeByte(party.classId);
                ds.writeByte(party.classId);
                ds.writeShort(party.getChar().level());
                m.writeUTF(party.name);
            }
            ds.flush();
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void partyInvite(String name) {
        try {
            Message m = new Message((byte) 41);
            m.writeUTF(name);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pleaseInputParty(String name) {
        try {
            Message m = new Message((byte) 39);
            m.writeUTF(name);
            session.sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void sendTimeNgucTu() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-80);
            m.writer().writeLong(System.currentTimeMillis());
            m.writer().writeInt((int) (player.timeNgucTu - System.currentTimeMillis()));
            m.writer().writeBoolean(false);
            sendMessage(m);
        } catch (IOException e) {

        }
    }

    public void sendTaskInfo() {
        try {
            Message m = new Message((byte) 103);
            m.writer().writeShort(player.taskId);
            if (player.taskMain != null) {
                m.writer().writeByte(player.taskMain.index);
                m.writer().writeShort(player.taskMain.count);
            } else {
                m.writer().writeByte(0);
                m.writer().writeShort(0);
            }
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void updateTaskCount(short count) {
        try {
            Message m = new Message((byte) 103);
            m.writer().writeShort(player.taskId);
            m.writer().writeByte(player.taskMain.index);
            m.writer().writeShort(count);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void sendTaskStep(int idStep) {
        Message m = new Message((byte) 12);
        try {
            m.writer().writeByte(idStep);
            sendMessage(m);
        } catch (Exception ex) {

        }
    }

    public void sendSTRtask() {
        Message m = new Message((byte) 12);
        try {
            m.writer().writeByte(-2);
            sendMessage(m);
        } catch (Exception ex) {
        }
    }

    public void xemThongTin(Char pl) {
        try {
            Clan clan = pl.clan;
            Message m = new Message((byte) 34);
            m.writeUTF(pl.name);
            m.writer().writeLong(pl.exp);
            m.writer().writeByte(pl.idCharSys);
            m.writer().writeByte(0);
            m.writer().writeByte(pl.sys);
            m.writer().writeByte(pl.gender);
            pl.writeItemBody(m);
            pl.writeItemBody2(m);
            m.writer().writeShort(pl.skills.size());//size skill
            for (int i = 0; i < pl.skills.size(); i++) {
                m.writer().writeShort(pl.skills.get(i).id);
            }
            if (clan == null) {
                m.writeUTF("");
            } else {
                m.writeUTF(clan.name);
                m.writeUTF(clan.main_name);
                m.writer().writeByte(clan.getMemberByName(pl.name).getType());
                m.writer().writeShort(0);
            }
            pl.showDanhHieu(m);
            m.writer().writeByte(pl.bp);
            m.writer().writeBoolean(false);
            m.writer().writeBoolean(false);
            m.writer().writeByte(pl.sachChienDau);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chatPrivate(String to, String name, String text) {
        try {
            Message m = new Message((byte) 28);
            m.writeUTF(to);
            m.writeUTF(name);
            m.writeUTF(text);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {

        }
    }

    public void openQuaySo() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(86);
            if (player.itemQuaySo != null) {
                m.writer().writeBoolean(true);// co dang quay khong ?
                m.writer().writeByte(player.itemQuaySo.getType()); // type
                m.writer().writeByte(player.itemQuaySo.getQuantity());// so sao
                m.writer().writeByte(player.itemQuaySo.getId());// index item
                m.writer().writeByte(player.itemQuaySo.getIdItem());
                if (player.itemQuaySo.getIdItem() == 177) {
                    m.writer().writeBoolean(true);
                } else {
                    m.writer().writeBoolean(false);
                }
            } else {
                m.writer().writeBoolean(false);// co dang quay khong ?
                m.writer().writeByte(0); // type
                m.writer().writeByte(0);// so sao
                m.writer().writeByte(0);// index item
                m.writer().writeByte(0);
                m.writer().writeBoolean(false);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSpin(List<Item> items) {
        try {
            Message m = new Message((byte) 72);
            m.writer().writeByte(player.itemQuaySo.getQuantity());//index
            m.writer().writeByte(player.itemQuaySo.getId());// index item
            m.writer().writeByte(player.itemQuaySo.getIdItem()); // 0- zeni 1-ballz- 2 xu-- 3kim cuong
            if (player.itemQuaySo.getIdItem() == 177) {
                m.writer().writeBoolean(true);
            } else {
                m.writer().writeBoolean(false);
            }
            m.writer().writeShort(items.size());
            for (int i = 0; i < items.size(); i++) {
                items.get(i).write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spin() {
        try {
            Message m = new Message((byte) 73);
            m.writer().writeByte(player.itemQuaySo.getQuantity());//index
            m.writer().writeByte(player.itemQuaySo.getId());// index item
            m.writer().writeByte(player.itemQuaySo.getIdItem()); // 0- zeni 1-ballz- 2 xu-- 3kim cuong
            if (player.itemQuaySo.getIdItem() == 177) {
                m.writer().writeBoolean(true);
            } else {
                m.writer().writeBoolean(false);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spinLoss(int index, int id) {
        try {
            Message m = new Message((byte) 73);
            m.writer().writeByte(-1);//So sao
            m.writer().writeByte(index);// vi tri
            m.writer().writeByte(id); //item
            m.writer().writeBoolean(false);
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetSpin() {
        try {
            Message m = new Message((byte) 74);
            m.writer().writeByte(0);//So sao
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void openThuVanMay(List<Item> itemTVM) {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(74);
            m.writer().writeShort(itemTVM.size());
            for (int i = 0; i < itemTVM.size(); i++) {
                itemTVM.get(i).write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void thuVanMay(int id, int quantity) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-85);
            m.writer().writeShort(id);
            m.writer().writeInt(quantity);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateIndexBag() {
        try {
            Message m = new Message((byte) 83);
            m.writer().writeInt(player.ballZ);
            List<Item> bag = Util.cleanArrayItem(player.bag);
            m.writer().writeShort(bag.size());// bag
            for (int i = 0; i < bag.size(); i++) {
                bag.get(i).write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListClan() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(91);
            m.writer().writeShort(0);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendInfoVuTru() {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(54);
            Clan clan = player.clan;
            m.writeUTF(clan.name);
            m.writeUTF(clan.title == null ? " " : clan.title);
            m.writer().writeLong(clan.getRegDate());
            m.writer().writeShort(clan.getLevel());
            m.writer().writeInt(clan.getExp());
            m.writer().writeInt(clan.getExpNext());
            m.writer().writeInt(clan.getCongHien());
            m.writer().writeInt(clan.getCoin());
            m.writeUTF(clan.alert);
            m.writer().writeByte(clan.openDun);
            m.writer().writeShort(0);// logo vu tru
            m.writer().writeInt((int) (System.currentTimeMillis() / 1000));// time logo
            m.writer().writeShort(clan.countInvite);
            m.writer().writeBoolean(false);
            m.writer().writeBoolean(false);
            m.writer().writeByte(clan.hour);// hour dun
            m.writer().writeByte(clan.min);// min dun
            List<Member> members = clan.memberDAO.getAll();
            synchronized (members) {
                m.writer().writeShort(members.size());// sl thanh vien
                for (Member mem : members) {
                    m.writer().writeByte(mem.getType());
                    m.writer().writeByte(mem.getClassId() + 1);
                    m.writer().writeByte(mem.getClassId()); // id icon
                    m.writer().writeShort(mem.getLevel());
                    m.writeUTF((mem.getName()));
                    m.writer().writeInt(mem.getPointClan());
                    m.writer().writeInt(mem.getPointClanWeek());
                    m.writer().writeBoolean(mem.isOnline());
                    m.writer().writeBoolean(false);
                }
            }
            String[] str = clan.getLog().split("\n");
            m.writer().writeShort(str.length);// size gi do
            for (int i = 0; i < str.length; i++) {
                m.writeUTF(str[i]);
            }
//            int var25 = 0;
//            for (int i = 0; i < clan.items.length; i++) {
//                if (clan.items[i] != null) {
//                    var25++;
//                }
//            }
//            Item[] itemss = new Item[var25];
//            int var26 = 0;
//            for (int i = 0; i < clan.items.length; i++) {
//                if (clan.items[i] != null) {
//                    itemss[var26] = clan.items[i];
//                    var26++;
//                }
//            }
//            if (var25 == 0) {
//                m.writer().writeShort(var25);// item
//                for (int i = 0; i < itemss.length; i++) {
//                    Item item = itemss[i];
//                    if (item != null && item.id >= 0) {
//                        m.writer().writeShort(item.id);
//                        m.writer().writeBoolean(item.isLock);
//                        m.writer().writeLong(item.expire);
//                        if (!item.isTrangBi() && !item.ai()) {
//                            m.writer().writeInt(item.getQuantity());
//                        } else {
//                            m.writer().writeByte(item.sys);
//                            m.writer().writeByte(item.upgrade);
//                            m.writeUTF(item.strOption);
//                            m.writeUTF(item.nameCreate);
//                        }
//                        if (item.aA()) {
//                            m.writeUTF(item.strOption);
//                        }
//                        m.writer().writeShort(item.index);
//                    }
//                }
//            } else {
//                m.writer().writeShort(clan.items.length);// item
//                for (int i = 0; i < clan.items.length; i++) {
//                    Item item = clan.items[i];
//                    if (item != null && item.id >= 0) {
//                        m.writer().writeShort(item.id);
//                        m.writer().writeBoolean(item.isLock);
//                        m.writer().writeLong(item.expire);
//                        if (!item.isTrangBi() && !item.ai()) {
//                            m.writer().writeInt(item.getQuantity());
//                        } else {
//                            m.writer().writeByte(item.sys);
//                            m.writer().writeByte(item.upgrade);
//                            m.writeUTF(item.strOption);
//                            m.writeUTF(item.nameCreate);
//                        }
//                        if (item.aA()) {
//                            m.writeUTF(item.strOption);
//                        }
//                        m.writer().writeShort(item.index);
//                    }
//                }
//            }
            
            Item[] clanItem = clan.getItems();
            short lit = (short)clanItem.length;
            m.writer().writeShort(lit);// item
            for (int i = 0; i < lit; i++) {
                if (clanItem[i] != null) {
                    clanItem[i].write(m);
                }
            }
            m.writer().writeByte(clan.skillClans.size());
            for (int i = 0; i < clan.skillClans.size(); i++) {
                m.writer().writeByte(clan.skillClans.get(i).id);
                m.writer().writeInt(clan.skillClans.get(i).timeUse);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListVuTru() {
        try {
            List<Clan> clans = Clan.getClanDAO().getAll();
            Message m = new Message((byte) 122);
            m.writer().writeByte(91);
            m.writer().writeShort(clans.size());
            for (Clan clan : clans) {
                int percent = (int) (clan.getExp() / (float) clan.getExpNext() * 100);
                m.writeUTF(clan.getMainName());//
                m.writer().writeInt(clan.getLevel()); // level
                m.writer().writeInt(percent); // % exp
                m.writer().writeInt(clan.getNumberMember()); // thành viên
                m.writer().writeInt(clan.getMemberMax()); // max thành viên
                m.writeUTF(clan.name); //
            }
            session.sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void clanInvite(String player, int type, String giatoc) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-105);
            m.writeUTF(player);
            m.writeUTF(giatoc);
            m.writer().writeByte(5);
            sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public void requestJoinClan(Char pl) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-104);
            m.writeUTF(pl.name);
            m.writer().writeShort(pl.level());
            m.writer().writeInt(1);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openSettingClan() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-10);
            sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public void menuUocRong(String[] menu, byte type) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(type);
            m.writer().writeByte(menu.length);
            for (int i = 0; i < menu.length; i++) {
                m.writeUTF(menu[i]);
            }
            sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void effectDragon(short effId) throws IOException {
        player.typeUocRong = effId;
        Message m = new Message((byte) -45);
        m.writer().writeShort(effId);
        m.writer().writeShort(player.x);
        m.writer().writeShort(player.y);
        m.writer().writeByte(-24);
        sendMessage(m);
    }

    public void addEXPPet(int ki) {
        try {
            Message m = new Message((byte) -41);
            m.writer().writeInt(ki);
            sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItemBody(Item item) {
        try {
            Message m = new Message((byte) -21);
            item.write(m);
            sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openUITrade(String name) {
        try {
            Message m = new Message((byte) 122);
            m.writer().writeByte(59);
            m.writeUTF(name);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tradeInvite(String name) {
        try {
            Message m = new Message((byte) 86);
            m.writeUTF(name);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tradeItemLock(Trader trader) {
        try {
            Message m = new Message((byte) 82);
            m.writer().writeInt(trader.coinTradeOrder);
            m.writer().writeByte(trader.itemTradeOrder.size());
            for (int i = 0; i < trader.itemTradeOrder.size(); i++) {
                trader.itemTradeOrder.get(i).write(m);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tradeAccept() {
        try {
            Message ms = new Message((byte) 81);
            sendMessage(ms);
        } catch (Exception ex) {
            Log.error("trade ok err: " + ex.getMessage(), ex);
        }
    }

    public void tradeCancel() {
        try {
            Message ms = new Message((byte) 83);
            ms.writer().writeInt(player.ballZ);
            List<Item> bag = Util.cleanArrayItem(player.bag);
            ms.writer().writeShort(bag.size());// bag
            for (int i = 0; i < bag.size(); i++) {
                bag.get(i).write(ms);
            }
            sendMessage(ms);
        } catch (Exception ex) {
            Log.error("trade cancel  err: " + ex.getMessage(), ex);
        }
    }

    public void tradeOk() {
        try {
            Message ms = new Message((byte) 80);
            ms.writer().writeInt(player.ballZ);
            List<Item> bag = Util.cleanArrayItem(player.bag);
            ms.writer().writeShort(bag.size());// bag
            for (int i = 0; i < bag.size(); i++) {
                bag.get(i).write(ms);
            }
            sendMessage(ms);
        } catch (Exception ex) {
            Log.error("trade ok err: " + ex.getMessage(), ex);
        }
    }

    public void showTopCaoThu(Vector<CaoThu> top, boolean isOpen) {
        try {
            Message m = new Message((byte) -22);
            m.writer().writeBoolean(isOpen);
            m.writer().writeByte(top.size());
            for (int i = 0; i < top.size(); i++) {
                CaoThu caoThu = top.get(i);
                m.writer().writeByte(i);
                m.writeUTF(caoThu.name);
                m.writer().writeLong(caoThu.num);
                m.writer().writeLong(0);
                m.writer().writeByte(caoThu.classId);
                m.writeUTF(caoThu.clanName == null ? "Chưa vào vũ trụ" : caoThu.clanName);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopNap(Vector<CaoThu> top, boolean isOpen) {
        try {
            Message m = new Message((byte) -22);
            m.writer().writeBoolean(isOpen);
            m.writer().writeByte(top.size());
            for (int i = 0; i < top.size(); i++) {
                CaoThu caoThu = top.get(i);
                m.writer().writeByte(i);
                m.writeUTF(caoThu.name);
                m.writer().writeLong(0);
                m.writer().writeLong(caoThu.num);
                m.writer().writeByte(caoThu.classId);
                m.writeUTF(caoThu.clanName == null ? "Chưa vào vũ trụ" : caoThu.clanName);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopVuTru(Vector<Clan> top, boolean isOpen) {
        try {
            Message m = new Message((byte) -30);
            m.writer().writeBoolean(isOpen);
            m.writer().writeByte(top.size());
            for (int i = 0; i < top.size(); i++) {
                Clan clan = top.get(i);
                m.writeUTF(clan.main_name);
                int percent = (int) (clan.getExp() / (float) clan.getExpNext() * 100);
                m.writer().writeInt(clan.getLevel());
                m.writer().writeInt(percent);
                m.writer().writeInt(clan.getNumberMember());
                m.writer().writeInt(clan.getMemberMax());
                m.writeUTF(clan.getName());
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInputDialog() {
        try {
            InputDialog input = player.getInput();
            if (input != null) {
                Message m = new Message((byte) 122);
                m.writer().writeByte(73);
                m.writeUTF(input.getTitle());
                m.writer().writeShort(input.getId());
                sendMessage(m);
                m.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTextClient() {
        try {
            Message m = new Message((byte) -122);
            m.writer().writeByte(-111);
            Caption.readForWriter(m);
            session.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updatePointPB() {
        try {
            Message m = new Message((byte) -31);
            m.writer().writeInt(player.pointHell);
            sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void npcTalk(String str) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(3);
            m.writeUTF(str);
            m.writer().writeShort(-1);
            m.writer().writeByte(-1);
            m.writer().writeByte(-1);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateHinhAnh(byte type) {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(-58);
            m.writer().writeInt(player.id);
            m.writer().writeByte(type);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTaskQuyLao() {
        try {
            Message m = new Message((byte) -49);
            m.writer().writeByte(player.countTaskQuyLao);
            m.writer().writeInt(player.mocNap);
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetTaskQuyLao() {
        try {
            Message m = new Message((byte) -5);
            m.writer().writeByte(0);
            m.writer().writeByte(-1);
            session.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public void updateTaskOrder(TaskOrder task) {
        try {
            Message m = new Message((byte) -5);
            m.writer().writeByte(0);
            m.writer().writeByte(1);
            m.writer().writeShort(task.count);
            m.writer().writeShort(0);
            m.writer().writeShort(0);
            m.writer().writeShort(task.killId);
            m.writer().writeShort(task.getMapId());
            m.writer().writeShort(0);
            m.writer().writeShort(0);
            m.writer().writeShort(task.maxCount);
            m.writeUTF("");
            m.writeUTF("");
            m.writeUTF(task.getDescription());
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void openUIStall(List<Stall> stallList, short index) {
        try {
            Message m = new Message((byte) 101);
            m.writer().writeShort(index);
            m.writer().writeShort(stallList.size());
            for (int i = 0; i < stallList.size(); i++) {
                Stall stall = stallList.get(i);
                m.writer().writeLong(stall.getId());
                m.writeUTF(stall.getName());
                m.writer().writeInt(stall.getPrice());
                m.writer().writeInt(stall.getTime());
                stall.getItem().write(m);
            }
            sendMessage(m);
        } catch (IOException e) {

        }
    }

    public void sellMarket(short index) {
        try {
            Message m = new Message((byte) 99);
            m.writer().writeInt(player.coin);
            m.writer().writeShort(index);
            session.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public void openUIMeSell(List<Stall> stallList) {
        try {
            Message m = new Message((byte) 100);
            m.writer().writeShort(stallList.size());
            for (int i = 0; i < stallList.size(); i++) {
                Stall stall = stallList.get(i);
                m.writer().writeLong(stall.getId());
                m.writer().writeInt(stall.getPrice());
                m.writer().writeInt(stall.getTime());
                stall.getItem().write(m);
            }
            sendMessage(m);
        } catch (IOException e) {

        }
    }

    public void buyStall() {
        try {
            Message m = new Message((byte) 98);
            m.writer().writeInt(player.ballZ);
            session.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {

        }
    }

    public void addFriend(String name, int i, boolean isBan) {
        try {
            Message m = new Message((byte) 79);
            m.writeUTF(name);
            m.writer().writeByte(i);
            m.writer().writeBoolean(isBan);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void removeFriend(String name) {
        try {
            Message m = new Message((byte) 76);
            m.writeUTF(name);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void inviteFriend(String name, int i) {
        try {
            Message m = new Message((byte) 77);
            m.writeUTF(name);
            m.writer().writeByte(i);
            sendMessage(m);
        } catch (Exception e) {

        }
    }

    public void sendAchievementInfo() {
        try {
            PlayerAchievementList achievementList = player.achievements;
            achievementList.updateAchievementCount(10, player.levelBean);
            achievementList.updateAchievementCount(23, (int) achievementList.getAchievements().stream().filter(PlayerAchievement::isReceived).count());
            Message message = new Message((byte) 122);
            message.writer().writeByte(72);
            for (PlayerAchievement achievement : achievementList.getAchievements()) {
                int amount = achievement.getCount();
                if (achievement.isReceived()) {
                    amount += 1;
                }
                switch (achievement.getId()) {
                    case 7:
                    case 14:
                    case 16:
                    case 18:
                        message.writer().writeShort(amount);
                        break;
                    default:
                        message.writer().writeByte(amount);
                }
            }
            message.writer().flush();
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCurrentTab() {
        try {
            Message m = new Message((byte) -123);
            m.writer().writeByte(17);
            sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTopPhamChat(Vector<CaoThu> top) {
        try {
            Message m = new Message((byte) -22);
            m.writer().writeBoolean(true);
            m.writer().writeByte(top.size());
            for (int i = 0; i < top.size(); i++) {
                CaoThu caoThu = top.get(i);
                m.writer().writeByte(i);
                m.writeUTF(caoThu.name);
                m.writer().writeLong(0);
                m.writer().writeLong(caoThu.num);
                m.writer().writeByte(caoThu.classId);
                m.writeUTF(caoThu.clanName == null ? "Chưa vào vũ trụ" : caoThu.clanName);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showTopSoiNoi(List<CaoThu> top) {
        try {
            Message m = new Message((byte) -22);
            m.writer().writeBoolean(true);
            m.writer().writeByte(top.size());
            for (int i = 0; i < top.size(); i++) {
                CaoThu caoThu = top.get(i);
                m.writer().writeByte(i);
                m.writeUTF(caoThu.name);
                m.writer().writeLong(0);
                m.writer().writeLong(caoThu.num);
                m.writer().writeByte(caoThu.classId);
                m.writeUTF(caoThu.clanName == null ? "Chưa vào vũ trụ" : caoThu.clanName);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public void showTopnhidong(List<CaoThu> top) {
        try {
            Message m = new Message((byte) -22);
            m.writer().writeBoolean(true);
            m.writer().writeByte(top.size());
            for (int i = 0; i < top.size(); i++) {
                CaoThu caoThu = top.get(i);
                m.writer().writeByte(i);
                m.writeUTF(caoThu.name);
                m.writer().writeLong(0);
                m.writer().writeLong(caoThu.num);
                m.writer().writeByte(caoThu.classId);
                m.writeUTF(caoThu.clanName == null ? "Chưa vào vũ trụ" : caoThu.clanName);
            }
            sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
    
    
}

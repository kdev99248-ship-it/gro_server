package com.vdtt.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdtt.ability.AbilityFromEquip;
import com.vdtt.achievement.PlayerAchievement;
import com.vdtt.achievement.PlayerAchievementList;
import com.vdtt.admin.AdminService;
import com.vdtt.clan.Clan;
import com.vdtt.clan.ClanDAO;
import com.vdtt.clan.Member;
import com.vdtt.data.*;
import com.vdtt.db.DbManager;
import com.vdtt.effect.Effect;
import com.vdtt.effect.EffectManager;
import com.vdtt.events.AbsEvent;
import com.vdtt.events.EventHandler;
import com.vdtt.events.points.EventPoint;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.handler.PvPHandler;
import com.vdtt.handler.UseItemHandler;
import com.vdtt.item.*;
import com.vdtt.mail.Mail;
import com.vdtt.mail.MailManager;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.item.ItemMap;
import com.vdtt.map.world.*;
import com.vdtt.map.world.destructionoftheuniverse.DestructionoftheUniverseArea;
import com.vdtt.map.world.destructionoftheuniverse.ZDestructionoftheUniverse;
import com.vdtt.map.world.martialarts.MartialArtsConference;
import com.vdtt.map.world.martialarts.Qualifiers;
import com.vdtt.map.world.martialarts.ZMartialArts;
import com.vdtt.map.world.planetblackstar.*;
import com.vdtt.map.world.planetblackstar.dragon.AbsDragon;
import com.vdtt.map.world.powerstation.PowerStation;
import com.vdtt.map.world.powerstation.ZPowerTournament;
import com.vdtt.map.world.powerstation.round.QualifiersPowerStation;
import com.vdtt.map.world.stadiums.Stadium;
import com.vdtt.map.world.stadiums.Zstadium;
import com.vdtt.map.zones.*;
import com.vdtt.mixer.*;
import com.vdtt.mob.*;
import com.vdtt.model.question.Answer;
import com.vdtt.model.question.Question;
import com.vdtt.model.question.QuestionManager;
import com.vdtt.network.Message;
import com.vdtt.network.Service;
import com.vdtt.npc.Npc;
import com.vdtt.party.Group;
import com.vdtt.party.MemberGroup;
import com.vdtt.reward.Reward;
import com.vdtt.reward.RewardManager;
import com.vdtt.server.*;
import com.vdtt.skill.Skill;
import com.vdtt.skill.SkillClan;
import com.vdtt.skill.SkillFactory;
import com.vdtt.stall.Stall;
import com.vdtt.stall.StallManager;
import com.vdtt.store.ItemStore;
import com.vdtt.store.Store;
import com.vdtt.store.StoreManager;
import com.vdtt.task.*;
import com.vdtt.util.*;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.DataOutputStream;
import java.lang.reflect.Type;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Char extends ObjectLive {

    public int typeUocRong;
    public int id;
    public byte typePopup;
    public User user;
    public String name;
    public byte gender;
    public byte idCharSys;
    public byte sys;
    public long exp;
    public byte status;
    public short speed = 800;
    public int hp, maxHP;
    public int mp, maxMP;
    public byte m, l;
    public byte lvPK;
    public int damage;
    public Item[] bag, box, body, body2, extend, sellSpeed;
    public List<DanhHieu> danhHieu;
    public byte rank;
    public int timeTienIch = (int) (System.currentTimeMillis() / 1000);
    public byte selectDanhhieu;
    public byte bp;
    public Vector<Skill> skills;
    public short numberCellBag, numberCellBox;
    public int zeni, ballZ, coin, kimCuong;
    public HashMap<String, Friend> friends, enemies;
    public byte sachChienDau;
    public boolean isCleaned;
    public int timeWaitChangeSkin;
    public int timeChangeSkin;
    public int idTask;
    public int mocNap, tieuNgay, tieuTuan;
    public int typePK;
    public int typeSelectOutChar;
    public int damegeMonster;
    public short exactly, crit, miss, armor;
    public short reduceDame, reduceCrit, reduceSuyGiam, reduceChiuDon, reduceStun;
    public short suyGiam, chiuDon, stun, randomDame;
    public short counterDame, skipMiss, armorPenetration, criticalHitPower;
    public short reduceCritHitPower;
    public short increaseXu, increaseKI;
    public short increaseExtra = 10;
    public int pointPhamChat, pointEnthusiastic, pointEnthusiasticWeek,
            pointUpgrageStar, pointUpgrageStarWeek, pointKame, pointSuper;
    public int zeniLock;
    public int[] options;
    public boolean[] haveOptions;
    public int[] optionsSupportSkill;
    public long lastLoginTime;
    public long timeSendZeni;
    public short countVongQuay;
    public boolean isLuongLongNhatThe;
    public boolean IsTyVo;
    public boolean IsAnCuuSat;
    public boolean IsCuuSat;
    public int IdTyVo;
    public int IdCharMoiCuuSat;
    public int IdCuuSat;
    public int martialArtsPoint;

    public long lastTimeDoneTask;
    public long delayDoneTask = 5000;
    public int TimeChatColor = 0;
    public String MaBaoVe = "";
    public boolean IsDaMoKhoa;
    public int TimeReset;
    public long resetTimeTichLuy;
    public boolean autoSellItem = false;
    @Getter
    @Setter
    private Service service;
    public Zone zone;
    public short mapId = 86;
    public boolean isDead;
    public long KI;
    public Lock lock = new ReentrantLock();
    public Lock lockItem = new ReentrantLock();
    @Setter
    private AbilityFromEquip abilityStrategy;
    protected boolean saving;
    @Getter
    @Setter
    private boolean isLoadFinish;
    private long lastLogoutTime;
    @Getter
    public ArrayList<Menu> menus = new ArrayList<>();
    @Getter
    public short subMenuId;
    public byte levelBean;
    public int timeReciveBean;
    public long timeUseBean;
    public Item sach;
    @Getter
    @Setter
    protected EffectManager em;
    public boolean isSuperSayan;
    public boolean isProtected;
    public byte maxBean = 24;
    public byte nhanMocNap;
    public MailManager mailManager;
    public long timeUpgradeBean;
    public List<com.vdtt.model.Reward> rewardList = new ArrayList<>();
    public int timeOnline;
    public byte ngayOnline;
    public long totalTimeOnline;
    public short totalTA;
    public short totalTL;
    public short totalSQ;
    public boolean packTinhAnh;
    public boolean packThuLinh;
    public boolean packSieuQuai;
    public boolean packOnline;
    public byte totalKiNang;
    public int napNgay;
    public short countTui1;
    public short countTui2;
    public short countTui3;
    @Getter
    private Group group;
    public Invite invite;
    public List<World> worlds;// danh sách world đang tham gia
    public long timeZoom;
    public short taskId = 0;
    public Task taskMain;
    public int countQuaSinhLuc;
    public ChatGlobal chatGlobal;
    @Getter
    @Setter
    private QuaySo quaySo;
    public Stadium stadium;
    public ItemQuaySo itemQuaySo;
    public List<Item> itemTVM = new ArrayList<>();
    public boolean isDCTT;
    public boolean resetRewarNap;
    public Clan clan;
    public long timeOutClan;
    public Pet pet;
    public Trade trade;
    public Trader myTrade;
    public Trader partnerTrade;
    private boolean taskPet;
    public int pointHell;
    @Getter
    @Setter
    private InputDialog input;
    public boolean lockEXP;
    public int countTuiDo;
    public byte countHell = 1;
    public byte countTimeRoom = 1;
    public byte countBarrack = 1;
    public byte countUseBarrackCard = 5;
    public byte countTheHell;
    public long timeGiapLuyenTap;
    public byte countTaskQuyLao = 40;
    public TaskOrder taskOrders;
    public short answer;
    public Skill selectSkill;
    public byte countDead;
    public long timeNgucTu;
    public byte countNgucTu = 2;
    public byte countUseItemNgucTu;
    public byte countAddPointNgucTu;
    public long timeRaoBan;
    public byte countTaskFailed;
    public PlayerAchievementList achievements;
    public long timeCoolDownSkillCaiTrang;
    public int typeThuVanMay;
    @Setter
    @Getter
    private EventPoint eventPoint;
    @Setter
    @Getter
    private long lastTimeQuaCauDen;
    public int countWakeUpFromDead;
    public ItemMapBlackDragonBall itemMapBlackDragonBall;
    public long lastTimePickBall;
    public int diemBTTBangHoa = 0;
    public boolean removeItemRac = false;
    public int pointMakeCake;
    public int pointThaLongDen;
     public int pointloidai;
    public String nameZeni;

    public Char() {
        hp = mp = 1;
        this.em = new EffectManager(this);
        this.mailManager = new MailManager(this);
        invite = new Invite();
        this.worlds = new ArrayList<>();
        this.chatGlobal = new ChatGlobal(this);
        this.quaySo = new QuaySo();
        this.danhHieu = new ArrayList<>();
        this.friends = new HashMap<>();
    }

    public boolean isUnlock() {
        return !this.MaBaoVe.isEmpty() && this.IsDaMoKhoa;
    }

    public void requestChangeMap() {
        if (this.isDead) {
            return;
        }
        zone.requestChangeMap(this);
    }

    public void preLoad() {
        try {
            for (int i = bag.length - 1; i >= 0; i--) {
                Item item = bag[i];
                if (item != null) {
                    if (item.isExpired()) {
                        bag[i] = null;
                    }
                }
            }
            for (int i = body.length - 1; i >= 0; i--) {
                Item item = body[i];
                if (item != null) {
                    if (item.isExpired()) {
                        body[i] = null;
                    }
                }
            }
            for (int i = body2.length - 1; i >= 0; i--) {
                Item item = body2[i];
                if (item != null) {
                    if (item.isExpired()) {
                        body2[i] = null;
                    }
                }
            }
            for (int i = box.length - 1; i >= 0; i--) {
                Item item = box[i];
                if (item != null) {
                    if (item.isExpired()) {
                        box[i] = null;
                    }
                }
            }
            getService().updateItemBodyChange();
            getService().updateIndexBag();
            setAbility();
            if (!resetRewarNap) {
                resetRewarNap = true;
                com.vdtt.model.Reward rw2 = findReward(5);
                if (rw2 != null) {
                    rw2.list.clear();
                }
            }

            if (mocNap >= 1 && nhanMocNap == 0) {
                nhanMocNap = 1;
                for (int i = 0; i < 3; i++) {
                    Item item = RewardManager.gI().find(477 + i).item;
                    Mail mail = new Mail();
                    if (mailManager.size() == 0) {
                        mail.id = mailManager.size();
                    } else {
                        mail.id = mailManager.getID();
                    }
                    mail.title = "Nạp lần đầu";
                    mail.content = "Quà nạp lần đầu";
                    mail.sender = "Hệ thống";
                    mail.time = (int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000);
                    mail.item = item.clone();
                    if (mail.item.expire > 0) {
                        mail.item.expire += System.currentTimeMillis();
                    }
                    mailManager.addMail(mail);
                }
            }
            if (mocNap >= 1000 && nhanMocNap == 1) {
                nhanMocNap = 2;
                for (int i = 3; i < 6; i++) {
                    Item item = RewardManager.gI().find(477 + i).item;
                    Mail mail = new Mail();
                    if (mailManager.size() == 0) {
                        mail.id = mailManager.size();
                    } else {
                        mail.id = mailManager.getID();
                    }
                    mail.title = "Nạp lần đầu";
                    mail.content = "Quà nạp lần đầu";
                    mail.sender = "Hệ thống";
                    mail.time = (int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000);
                    mail.item = item.clone();
                    if (mail.item.expire > 0) {
                        mail.item.expire += System.currentTimeMillis();
                    }
                    mailManager.addMail(mail);
                }
            }
            if (mocNap >= 2000 && nhanMocNap == 2) {
                nhanMocNap = 3;
                for (int i = 6; i < 9; i++) {
                    Item item = RewardManager.gI().find(477 + i).item;
                    Mail mail = new Mail();
                    if (mailManager.size() == 0) {
                        mail.id = mailManager.size();
                    } else {
                        mail.id = mailManager.getID();
                    }
                    mail.title = "Nạp lần đầu";
                    mail.content = "Quà nạp lần đầu";
                    mail.sender = "Hệ thống";
                    mail.time = (int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000);
                    mail.item = item.clone();
                    if (mail.item.expire > 0) {
                        mail.item.expire += System.currentTimeMillis();
                    }
                    mailManager.addMail(mail);
                }
            }
            if (clan != null) {
                Member mem = clan.getMemberByName(name);
                if (mem != null) {
                    mem.setOnline(true);
                    mem.setChar(this);
                }
            }
            if (increaseExtra < 10) {
                increaseExtra = 10;
            }
            getService().sendTaskQuyLao();
            if (taskOrders != null) {
                getService().updateTaskOrder(taskOrders);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAbility() {
        try {
            if (abilityStrategy != null) {
                abilityStrategy.setAbility(this);
                if (zone != null) {
                    zone.getService().updateHP(this);
                    zone.getService().updateStatus(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takingTask() {
        try {
            if (taskMain == null) {
                getService().sendTaskStep(-1);
                updateTaskLevelUp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTaskNpcId() {
        try {
            if (taskMain != null) {
                if (taskMain.index > taskMain.vStep.size() - 1 || taskMain.index < 0) {
                    return taskMain.template.npcid;
                } else {
                    return taskMain.vStep.get(taskMain.index).npcId;
                }

            } else {
                if (taskId >= DataCenter.gI().TaskTemplate.length) {
                    return -1;
                }
                return DataCenter.gI().TaskTemplate[taskId].npcid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getIdItemTask(int mobId) {
        try {
            if (taskMain != null) {
                if (taskMain.index < 0 || taskMain.index > taskMain.vStep.size() - 1) {
                    return -1;
                }
                int idMob = taskMain.vStep.get(taskMain.index).mobId;
                int itemId = taskMain.vStep.get(taskMain.index).itemId;
                if (mobId == idMob) {
                    return itemId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateTaskLevelUp() {
//        if (taskMain != null && taskMain.index == 0 && taskMain.template.getLeveRequire() > 0
//                && this.level >= taskMain.template.getLeveRequire()) {
//            taskNext();
//        }
    }

    public void updateTaskPickItem(Item item) {
        if (taskMain != null) {
            if (taskMain.index < 0 || taskMain.index > taskMain.vStep.size() - 1) {
                return;
            }
            int itemId = taskMain.vStep.get(taskMain.index).itemId;
            if (item.id == itemId) {
                updateTaskCount(1);
            }
        }
    }

    public void updateTaskKillMonster(Mob mob) {
        if (taskMain != null) {
            if (taskMain.index < 0 || taskMain.index > taskMain.vStep.size() - 1) {
                return;
            }
            Step step = taskMain.vStep.get(taskMain.index);
            int modId = step.mobId;
            if (mob.id == modId && getIdItemTask(mob.id) == -1) {
                if (taskId == 27 || taskId == 28 || taskId == 29) {
                    if (taskId == 27 && mob.levelBoss != 1) {
                        return;
                    }
                    if (taskId == 28 && mob.levelBoss != 2) {
                        return;
                    }
                    if (taskId == 29 && mob.levelBoss != 10) {
                        return;
                    }
                    taskMain.count = taskMain.vStep.get(taskMain.index).require;
                }
                updateTaskCount(1);
            }
        }
    }

    public int getItemStepTask() {
        try {
            if (taskMain != null) {
                if (taskMain.index < 0 || taskMain.index > taskMain.vStep.size() - 1) {
                    return -1;
                }
                return taskMain.vStep.get(taskMain.index).itemId;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateTaskCount(int count) {
        if (taskMain != null) {
            try {
                taskMain.count += (short) count;
                getService().updateTaskCount(taskMain.count);
                if (taskMain.count >= taskMain.vStep.get(taskMain.index).require) {
                    taskNext();
                }
            } catch (Exception ex) {
                Log.error("err: " + ex.getMessage(), ex);
            }
        }
    }

    public void taskNext() {
        try {
            if (taskMain != null) {
                taskMain.index++;
                taskMain.count = 0;
                getService().sendTaskInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateTask() {
        if (taskMain != null) {
            rewardTask();
            taskId++;
            if (taskId < DataCenter.gI().TaskTemplate.length) {
                taskMain = TaskFactory.getInstance().createTask(taskId, (byte) 0, (short) 0);
                if (taskId == 9) {
                    updateTask();
                    return;
                }
            } else {
                taskMain = null;
            }
            getService().sendTaskInfo();
        }
    }

    public void rewardTask() {
        try {
            TaskTemplate taskTemplate = taskMain.template;
            addEXP(taskTemplate.getExt());
            addCoin(taskTemplate.getXu(), true);
            if (taskTemplate.strItem != null && !taskTemplate.strItem.isEmpty()) {
                String[] split = taskTemplate.strItem.split("@");
                Item var2 = new Item(Short.parseShort(split[0]), "task");
                var2.isLock = Boolean.parseBoolean(split[1]);
                var2.expire = Long.parseLong(split[2]);
                var2.setQuantity(Integer.parseInt(split[3]));
                //            var2. = Byte.parseByte(var1[4]);
                var2.upgrade = Byte.parseByte(split[5]);
                //            if (var1.length > 6) {
                //                var2.strOption = var1[6];
                //                if (var2.getTemplate().gioiTinh != 2) {
                //                    for (int var3 = 0; var3 < ItemManager.getInstance().itemTemplates.size(); ++var3) {
                //                        ItemTemplate var5;
                //                        if ((var5 = ItemManager.getInstance().itemTemplates.get(var3)).type == var2.getTemplate().type && var5.levelNeed == var2.getTemplate().levelNeed) {
                //                            var2.id = var5.id;
                //                            break;
                //                        }
                //                    }
                //                }
                //            }
                if (var2.id == 832) {
                    Skill skill = switch (sys) {
                        case 1 ->
                            SkillFactory.getInstance().getSkill(8, 0);
                        case 2 ->
                            SkillFactory.getInstance().getSkill(3, 0);
                        case 3 ->
                            SkillFactory.getInstance().getSkill(15, 0);
                        case 4 ->
                            SkillFactory.getInstance().getSkill(21, 0);
                        case 5 ->
                            SkillFactory.getInstance().getSkill(24, 0);
                        default ->
                            null;
                    };
                    if (skill != null) {
                        totalKiNang++;
                        addSkill(skill);
                    }
                } else if (var2.id == 831) {
                    Skill skill = switch (sys) {
                        case 1 ->
                            SkillFactory.getInstance().getSkill(6, 0);
                        case 2 ->
                            SkillFactory.getInstance().getSkill(0, 0);
                        case 3 ->
                            SkillFactory.getInstance().getSkill(12, 0);
                        case 4 ->
                            SkillFactory.getInstance().getSkill(18, 0);
                        case 5 ->
                            SkillFactory.getInstance().getSkill(27, 0);
                        default ->
                            null;
                    };
                    if (skill != null) {
                        totalKiNang++;
                        addSkill(skill);
                    }
                } else if (var2.id == 830) {
                    Skill skill = SkillFactory.getInstance().getSkill(31, 0);
                    if (skill != null) {
                        totalKiNang++;
                        addSkill(skill);
                    }
                } else {
                    if (var2.getTemplate().isXepChong) {
                        if (var2.getQuantity() <= 0) {
                            var2.setQuantity(1);
                        }
                        addItemToBag(var2);
                        getService().addItem(var2);
                    } else {
                        for (int i = 0; i < var2.getQuantity(); i++) {
                            Item item = var2.clone();
                            item.setQuantity(1);
                            addItemToBag(item);
                            getService().addItem(item);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeMap(int id) {
        try {
            if (id == 49 || id == 47) {
                MapManager.getInstance().joinZone(this, 86, 0, 1);
                return;
            }
            int zoneId = Util.randomZoneId(id);
            this.mapId = (short) id;
            if (zoneId == -1) {
                service.serverDialog("Không tìm thấy nơi này!");
                return;
            }
            outZone();
            joinZone(id, zoneId, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorld(World world) {
        synchronized (worlds) {
            if (worlds.stream().noneMatch(w -> w.getType() == world.getType())) {
                worlds.add(world);
                Log.debug("add worldType: " + world.getType());
            }
        }
    }

    public void removeWorld(byte type) {
        synchronized (worlds) {
            worlds.removeIf((t) -> t.getType() == type);
            Log.debug("remove worldType: " + type);
        }
    }

    public World findWorld(byte type) {
        synchronized (worlds) {
            for (World world : worlds) {
                if (world.getType() == type) {
                    return world;
                }
            }
            return null;
        }
    }

    public com.vdtt.model.Reward findReward(int type) {
        for (com.vdtt.model.Reward reward : rewardList) {
            if (reward.type == type) {
                return reward;
            }
        }
        return null;
    }

    public void outZone() {
        if (trade != null) {
            trade.closeUITrade();
        }
        if (zone != null) {
            zone.out(this);
        }
        if (this.group != null && this.group.memberGroups.size() <= 1) {
            outParty();
        }
    }

    public void removeMemberFromWorld(Zone pre, Zone now) {
        List<Byte> worldTypes = new ArrayList<>();
        synchronized (worlds) {
            worlds.forEach((t) -> {
                if (t.leaveWorld(pre, now)) {
                    t.removeMember(this);
                    worldTypes.add(t.getType());
                }
            });
        }
        try {
            if (worldTypes.isEmpty()) {
                return;
            }
            worldTypes.forEach((t) -> {
                removeWorld(t);
            });
        } catch (Exception e) {

        }
    }

    public void addMemberForWorld(Zone pre, Zone now) {
        synchronized (worlds) {
            worlds.forEach((t) -> {
                if (t != null && !t.isClosed() && t.enterWorld(pre, now)) {
                    t.addMember(this);
                }
            });
        }
    }

    public void joinZone(int map, int zoneId, int typeTau) {
        if (taskId == 10) {
            switch (taskMain.index) {
                case 1:
                    if (map == 57) {
                        updateTaskCount(1);
                    }
                    break;
                case 2:
                    if (map == 62) {
                        updateTaskCount(1);
                    }
                    break;
            }
        } else if (taskId == 19 && taskMain.index > 0) {
            switch (taskMain.index) {
                case 1, 2, 3:
                    if (map == taskMain.vStep.get(taskMain.index).mapId) {
                        updateTaskCount(1);
                    }
                    break;
            }
        } else if (taskId == 25 && taskMain.index > 0) {
            switch (taskMain.index) {
                case 1, 2, 3, 4:
                    if (map == taskMain.vStep.get(taskMain.index).mapId) {
                        updateTaskCount(1);
                    }
                    break;
            }
        } else if (taskId == 31 && taskMain.index > 0) {
            switch (taskMain.index) {
                case 1, 2, 3, 4:
                    if (map == taskMain.vStep.get(taskMain.index).mapId) {
                        updateTaskCount(1);
                    }
                    break;
            }
        } else if (taskId == 37 && taskMain.index > 0) {
            switch (taskMain.index) {
                case 1, 2, 3, 4:
                    if (map == taskMain.vStep.get(taskMain.index).mapId) {
                        updateTaskCount(1);
                    }
                    break;
            }
        }
        MapManager.getInstance().joinZone(this, map, zoneId, typeTau);
    }

    public void writeData(DataOutputStream dos, Message m) {
        try {
            dos.writeByte(status);
            m.writeUTF(name);
            dos.writeByte(idCharSys);
            dos.writeByte(gender);
            dos.writeByte(sys);
            dos.writeByte(speed);
            dos.writeByte(lvPK);
            dos.writeShort(speed);
            dos.writeInt(hp);
            dos.writeInt(maxHP);
            dos.writeInt(mp);
            dos.writeInt(maxMP);
            dos.writeLong(exp);
            dos.writeShort(x);
            dos.writeShort(y);
            dos.writeByte(0);
            m.writeUTF("");
            writeItemBody(m);
            dos.writeByte(0);
            showDanhHieu(m);
            dos.writeByte(rank);//rank
            writeTypeSelectOutChar(m);

            writeTypeMoveMap(m, 0);
            dos.writeBoolean(false);
        } catch (Exception e) {

        }
    }

    public void writeTypeSelectOutChar(Message message) throws Exception {
        message.writer().writeByte(0);
    }

    public void writeTypeMoveMap(Message message, int tau) throws Exception {
        message.writer().writeByte(tau);
    }

    public void chatGlobal(Message ms) {
        if (chatGlobal != null) {
            chatGlobal.read(ms);
            chatGlobal.wordFilter();
            chatGlobal.send();
        }
    }

    public void chatPublic(Message mss) {
        try {
            String str = mss.readUTF();
            if (str.startsWith("quatang")) {
                try {
                    String[] arr = str.split(" ");
                    String code = arr[1];
                    GiftCode.getInstance().use(this, code);
                } catch (Exception e) {
                    getService().serverDialog("Nhập sai cu pháp rồi");
                }
            }
//            if (str.equalsIgnoreCase("locdo")) {
//                for (int i = 0; i < bag.length; i++) {
//                    Item item = bag[i];
//                    if (item != null && !item.isLock) {
//                        ItemTemplate template = item.getTemplate();
//                        byte type = template.type;
//                        if (type == 2 || type == 4 || type == 6 || type == 8) {
//                            ItemOption[] options = item.options();
//                            if (options == null || options.length < 10) {
//                                removeItem(i, 1, true);
//                            }
//                        }
//                    }
//                }
//            }
            if (user.isAdmin) {
                if (str.startsWith("map ")) {
                    String[] arr = str.split(" ");
                    int mapid = Integer.parseInt(arr[1]);
                    MapManager.getInstance().joinZone(this, mapid, 0, 1);
                } else if (str.contains("count")) {
                    updateTaskCount(300);
                } else if (str.contains("jr")) {
                    Item item = new Item(458, "admin");
                    item.strOption = "327,81";
                    item.addOptionRandom(379, 5, 12);
                    item.addOptionRandom(2, 150, 160);
                    item.addOptionRandom(0, 1100, 1300);
                    addItemToBag(item);
                    getService().addItem(item);
                } else if (str.contains("task")) {
                    getService().sendTaskQuyLao();
                    TaskOrder task = TaskFactory.getInstance().createTaskOrder((byte) Util.nextInt(0, 4), this);
                    getService().updateTaskOrder(task);
                } else if (str.contains("vip")) {
                    questionAndAnswer();
                } else if (str.startsWith("eff")) {
                    short idEff = Short.parseShort(str.split(" ")[1]);
                    Effect eff = new Effect(idEff, 500000, 10);
                    getEm().setEffect(eff);
                } else if (str.contains("check")) {
                    service.serverDialog("Map: " + mapId + " X: " + x + " Y: " + y);
                } else if (str.contains("map")) {
                    service.serverDialog("Map " + mapId);
                } else if (str.startsWith("i")) {
                    String[] arr = str.split(" ");
                    short id = Short.parseShort(arr[1]);
                    short quantity = Short.parseShort(arr[2]);
                    Item item = new Item(id, "admin");
                    item.setQuantity(quantity);
                    addItemToBag(item);
                    getService().addItem(item);
                } else if (str.startsWith("skill ")) {
                    String[] arr = str.split(" ");
                    short id = Short.parseShort(arr[1]);
                    Skill skill = SkillFactory.getInstance().getSkill(id, 1);
                    addSkill(skill);
                    service.updateSkill();
                } else if (str.startsWith("sm")) {
                    String[] arr = str.split(" ");
                    long var = Long.parseLong(arr[1]);
                    KI += var;
                    exp += var;
                    service.showKI();
                    service.showKI(this);
                } else if (str.contains("clear")) {
                    bag = new Item[numberCellBag];
                    getService().updateIndexBag();
                } else if (str.contains("resetskill")) {
                    skills.clear();
                    skills = SkillFactory.getInstance().getSkillByClass();
                    service.updateSkill();
                } else if (str.contains("itemm")) {
                    Item item = new Item(2, true, 1, "admin");
                    ItemMap itemMap = new ItemMap((short) 1);
                    itemMap.setItem(item);
                    itemMap.setX(x);
                    itemMap.setY(y);
                    itemMap.setOwnerID(id);
                    zone.addItemMap(itemMap);
                    zone.getService().addItemMap(itemMap, this);
                } else if (str.startsWith("setlv")) {
                    String arr[] = str.split(" ");
                    int lv = Integer.parseInt(arr[1]);
                    exp = DataCenter.gI().getLevel(lv);
                    service.showKI();
                    service.showKI(this);
                } else if (str.contains("zoom")) {
                    TimeRoom timeRoom = new TimeRoom(level(), (byte) 0);
                    timeRoom.joinZone(this);
                    addWorld(timeRoom);
                } else if (str.contains("sizemob")) {
                    serverDialog("Size mob: " + zone.getMonsters().size());
                } else if (str.contains("boss")) {
                    SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.FIDE);
                    SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.TUONG_LAI);
                    SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.VAMPA);
                    SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.NAMEC);
                    SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.YARDAT);
                    SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.ZAMASU);
                    SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.MORO);
                     SpawnBossManager.getInstance().spawnRandom(SpawnBossManager.fu);
                 
                } else if (str.contains("detu")) {
                    Item item = new Item(872, "admin");
                    Vector<ItemOption> options = new Vector<>();
                    options.add(new ItemOption(285, 100, 40));
                    options.add(new ItemOption(407, 0, 1));
                    options.add(new ItemOption(0, 200));
                    options.add(new ItemOption(152, 10));
                    options.add(new ItemOption(327, 50, 1));
                    options.add(new ItemOption(327, 53, 1));
                    options.add(new ItemOption(327, 54, 1));
                    options.add(new ItemOption(327, 30, 1));
                    options.add(new ItemOption(327, 3, -1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    options.add(new ItemOption(327, -1, 1));
                    item.strOption = Item.creatOption(options);
                    addItemToBag(item);
                    getService().addItem(item);
                } else if (str.contains("mailz")) {
                    serverDialog("size" + DbManager.getInstance().getMail(name).size());
                } else if (str.startsWith("upsm")) {
                    long var = Long.parseLong(str.split(" ")[1]);
                    pet.addEXP(var);
                } else if (str.contains("time")) {
                    Message m = new Message((byte) -123);
                    m.writer().writeByte(-80);
                    m.writer().writeLong(System.currentTimeMillis());
                    m.writer().writeInt((int) TimeUnit.MINUTES.toMillis(50));
                    m.writer().writeBoolean(false);
                    getService().sendMessage(m);
                } else if (str.startsWith("addmob")) {
                    short id = Short.parseShort(str.split(" ")[1]);
                    Mob mob = zone.createBoss(id, 1000, (short) 10, x, y, 1000);
                    mob.zone = zone;
                    zone.addMob(mob);
                } else if (str.startsWith("uoc ")) {
                    short idUoc = Short.parseShort(str.split(" ")[1]);
                    RuntimeServer.gI().uocRongALL(this, (byte) 1);
                } else if (str.startsWith("gui")) {
                    byte type = Byte.parseByte(str.split(" ")[1]);
                    Log.debug("type: " + type);
                    Message m = new Message((byte) 122);
                    m.writer().writeByte(type);
                    m.writeUTF("test");
                    m.writer().writeShort(4);
                    getService().sendMessage(m);
                    m.cleanup();
                } else if (str.contains("hell")) {
                    Hell hell = new Hell(getTypeSucManh());
                    addWorld(hell);
                    hell.joinZone(this);
                } else if (str.contains("moro")) {
                    MoroQuest moroQuest = new MoroQuest();
                    addWorld(moroQuest);
                    moroQuest.joinZone(this);
//                } else if (str.contains("cellmax")) {
//                    if (EventHandler.isEvent(SummerParty.class)) {
//                        SummerParty summerParty = (SummerParty) EventHandler.getEvent();
//                        summerParty.spawnCellMax();
//                    }
                } else if (str.contains("sucvat")) {
                    World world = clan.findWorld(World.DESTRUCTION_UNIVERSE);
                    if (world != null) {
                        this.setXY(90, 336);
                        ZDestructionoftheUniverse zDestructionoftheUniverse = ((ZDestructionoftheUniverse) world);
                        zDestructionoftheUniverse.zones.get(zDestructionoftheUniverse.level).join(this, -1);
                        return;
                    }
                    if (clan.countNvHd <= 0) {
                        getService().serverDialog("Hết lượt tham gia");
                        return;
                    }
                    ZDestructionoftheUniverse zDestructionoftheUniverse = new ZDestructionoftheUniverse(0);
                    clan.addWorld(zDestructionoftheUniverse);
                    zDestructionoftheUniverse.joinZone(this);
                } else if (str.contains("ma")) {
                    new MartialArtsConference();
                    for (Char c : this.zone.getChars()) {
                        MartialArtsConference.joinMap(c);
                    }
                } else if (str.contains("test1")) {
                    new PowerStation();
                } else if (str.contains("test2")) {
                    for (Char c : this.zone.getChars()) {
                        PowerStation.joinMapAdmin(c);
                    }
                } else if (str.contains("test3")) {
                    for (int i = 911; i <= 917; i++) {
                        Item item = new Item(i, "abc");
                        item.setQuantity(1);
                        item.isLock = true;
                        clan.addItem(item);
                    }
                    Effect effect = new Effect(133, 604800016, 1);
                    this.getEm().setEffect(effect);
                } else if (str.contains("test4")) {
                    PlanetBlackStarManager.setStart(true);
                    PlanetBlackStar.getInstance().createArea();
                } else if (str.contains("maxplayer")) {
                    serverDialog("Max player: " + zone.map.getMaxPlayer(zone));
                }
            }
            Message m = new Message((byte) 21);
            m.writeUTF(name);
            m.writeUTF(str);
            zone.getService().sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void questionAndAnswer() {
        subMenuId = CMDMenu.QUY_LAO;
        Question question = QuestionManager.instance.random();
        StringJoiner joiner = new StringJoiner(";");
        for (Answer answer : question.getAnswers()) {
            joiner.add(answer.getAnswer());
        }
        String concatenatedOptions = joiner.toString();
        answer = (short) question.getCorrectAnswer();
        getService().sendTextNPC(question.getQuestion(), concatenatedOptions);
    }

    public void pickItem(Message ms) {
        lockItem.lock();
        try {
            if (isDead) {
                return;
            }
            if (trade != null) {
                warningTrade();
                return;
            }
            short itemMapId = ms.reader().readShort();
            ItemMap itemMap = zone.findItemMapById(itemMapId);
            if (itemMap != null) {
                itemMap.lock.lock();
                try {
                    if (itemMap.isPickedUp()) {
                        return;
                    }
                    int distance = Util.getRange(this.x, this.y, itemMap.getX(), itemMap.getY());
                    if (distance < 150) {
                        Item item = itemMap.getItem();
                        if (item == null) {
                            return;
                        }

                        if (itemMap instanceof ItemMapBlackDragonBall itemMapBlackDragonBall) {
                            itemMapBlackDragonBall.pickBall(this, itemMapId);
                            return;
                        }

                        int ownerID = itemMap.getOwnerID();
                        if (ownerID == -1 || this.id == ownerID || itemMap.isCanPickup()) {
                            if (ownerID != -1 && this.id != ownerID) {
                                service.serverMessage("Vật phẩm của người khác");
                                return;
                            }
                            int index2 = -1;
                            int requireItemID = itemMap.getRequireItemID();

                            if (item.template.id != 163) {
                                int num = getSlotNull();
                                if (item.template.isXepChong) {
                                    int index = getIndexItemByIdInBag(item.id, item.isLock);
                                    if (index == -1 && num == 0) {
                                        warningBagFull();
                                        return;
                                    }
                                } else {
                                    if (num == 0) {
                                        warningBagFull();
                                        return;
                                    }
                                }
                                if (requireItemID != -1 && index2 != -1) {
                                    removeItem(index2, 1, true);
                                }
                                if (item.id == getItemStepTask()) {
                                    updateTaskCount(1);
                                }

                                itemMap.setPickedUp(true);

//                                if (isTask) {
//                                    item.isLock = true;
//                                }
                                addItemToBag(item);
                                getService().addItem(item);
//                                service.closeCurrentTab();
                            } else {
                                itemMap.setPickedUp(true);
                                long sum = (long) this.coin + (long) item.getQuantity();
                                if (sum > Integer.MAX_VALUE) {
                                    this.coin = Integer.MAX_VALUE;
                                } else {
                                    addCoin(item.getQuantity(), true);
                                }
                            }
                            zone.getService().pickItem(this, itemMapId, item);
                            zone.removeItem(itemMap);
                            getService().updateIndexBag();
                        } else {
                            getService().serverMessage("Vật phẩm của người khác");
                        }
                    }
                } finally {
                    itemMap.lock.unlock();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void learnSkill(Message mss) {
        try {
            short idskill = mss.reader().readShort();
            Skill skill = findSkillWithIdSkillTemplate(idskill);
            if (skill != null) {
                Skill skillNext = SkillFactory.getInstance().getSkill(skill.isSkillTemplate, skill.level + 1);
                if (skillNext != null) {
                    if (KI < skillNext.kiUpgrade) {
                        service.serverDialog("Không đủ KI");
                        return;
                    }
                    if (level() < skillNext.levelNeed) {
                        service.serverDialog("Không đủ level");
                        return;
                    }
                    if (taskId == 1 && taskMain != null && taskMain.index == 0) {
                        updateTaskCount(1);

                    }
                    if (taskId == 5 && taskMain != null && taskMain.index == 3) {
                        updateTaskCount(1);
                    }
                    KI -= skillNext.kiUpgrade;
                    int index = this.skills.indexOf(skill);
                    this.skills.set(index, skillNext);
                    service.showKI();
                    service.updateSkill();
                    setAbility();
                    service.updateHPFull();
                    service.updateMPFull();
                    getService().updateSkillNoCoolDown();
                } else {
                    service.serverDialog("Không tìm thấy skill next");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Skill findSkillWithIdSkillTemplate(short idSkillTemplate) {
        for (Skill skill : skills) {
            if (skill != null && SkillFactory.getInstance().getSkillTemplate(skill.isSkillTemplate).id == idSkillTemplate) {
                return skill;
            }
        }
        return null;
    }

    public void getInfo2(Message ms) {
        try {
            String name = ms.readUTF();
            if (this.name.contains(name)) {
                this.service.showInfo2(this);
            } else {
                Char pl = ServerManager.findCharByName(name);
                if (pl != null) {
                    service.showInfo2(pl);
                } else {
                    this.service.serverDialog("Không tìm thấy người chơi");
                }
            }
        } catch (Exception e) {

        }
    }

    public void showDanhHieu(Message m) throws Exception {
        try {
            m.writer().writeByte(danhHieu.size());//size danh hieu
            for (int i = 0; i < danhHieu.size(); i++) {
                DanhHieu dh = danhHieu.get(i);
                if (dh != null) {
                    m.writeUTF(dh.title);
                    m.writer().writeInt(dh.time);
//                    if(this.user.session != null && this.user.session.ver > 3)
                    m.writeUTF(dh.infoCs);
                }
            }
            m.writer().writeByte(selectDanhhieu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDame(Message mss) {
        try {
            String name = mss.readUTF();
            if (this.name.contains(name)) {
                this.service.showDame(this);
            } else {
                Char pl = ServerManager.findCharByName(name);
                if (pl != null) {
                    this.service.showDame(pl);
                } else {
                    this.service.serverDialog("Không tìm thấy người chơi");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addItemToBag(Item item) {
        try {
            if (item == null) {
                return false;
            }
            if (item.getQuantity() <= 0) {
                item.setQuantity(1);
            }
            if (item.getTemplate().id == 177) {
                item.setLock(true);
            }
            if (item.getTemplate().isXepChong) {
                for (int i = 0; i < bag.length; i++) {
                    if (bag[i] != null && bag[i].id == item.id && item.expire == bag[i].expire && item.isLock == bag[i].isLock) {
                        item.setQuantity(bag[i].getQuantity() + item.getQuantity());
                        if (item.id == 724) {
                            if (item.getQuantity() > maxBean) {
                                item.setQuantity(maxBean);
                            }
                        }
                        item.index = (short) i;
                        bag[i] = item;
                        return true;
                    }
                }
                for (int i = 0; i < bag.length; i++) {
                    if (bag[i] == null) {
                        if (item.id == 724) {
                            if (item.getQuantity() > maxBean) {
                                item.setQuantity(maxBean);
                            }
                        }
                        item.index = (short) i;
                        bag[i] = item;
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < bag.length; i++) {
                    if (bag[i] == null) {
                        item.index = (short) i;
                        bag[i] = item;
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeItem(int index, int quantity, boolean isUpdate) {
        try {
            if (bag[index] != null) {
                bag[index].reduce(quantity);
                if (!bag[index].has()) {
                    bag[index] = null;
                    if (isUpdate) {
                        getService().removeItem((byte) 0, index);
                    }
                } else if (isUpdate) {
                    getService().useItem(bag[index]);
                }
            }
        } catch (Exception ex) {
            Log.error("remove item: " + ex.getMessage(), ex);
        }
    }

    public int getIndexItemByIdInBag(int id, boolean isLock) {
        try {
            for (Item item : this.bag) {
                if (item != null && item.id == id && item.isLock == isLock) {
                    return item.index;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void attackMob(int indexSkill, int idMob) {
        try {
            if (this.isDead) {
                return;
            }
            if (taskId < 2) {
                getService().serverMessage("Tới nhiệm vụ thu thập thịt khủng long mới có thể dùng kỹ năng");
                return;
            }
            if (indexSkill < 0 || indexSkill >= skills.size()) {
                return;
            }

            long currentTimeMillis = System.currentTimeMillis();
            Skill skill = this.skills.get(indexSkill);
            if (skill == null) {
                return;
            }
            if (exp < skill.kiUpgrade) {
                this.skills.set(indexSkill, null);
                return;
            }
            if (skill.isCoolDown()) {
//                Effect eff = getEm().findByID(136);
//                if (eff == null) {
                return;
//                }
            }

            if (skill.isSkillTemplate == 2 || skill.isSkillTemplate == 17) {
                useSkill(indexSkill);
                return;
            }
            Mob mob = zone.findMoLivebByID(idMob);
            if (mob == null) {
                return;
            }
            if (mob.hp > 0) {
                int add = 100;
                if (skill.isSkillTemplate == 11) {
                    isDCTT = true;
                    add = 300;
                }
                if (Math.abs(x - mob.x) <= skill.dx + mob.getTemplate().rangeMove + add && Math.abs(y - mob.y) <= skill.dy + mob.getTemplate().rangeMove + add || mob.zone.map.id == 105 || mob.zone.map.id == 103) {
                    skill.timeCoolDown = currentTimeMillis + skill.coolDown;
                    if (getEm().statusWithID(189) || getEm().statusWithID(136)) {
                        if (skill.getSkillTemplate().name.contains("Đá")
                                || skill.getSkillTemplate().name.contains("Đấm")) {
                            skill.timeCoolDown -= skill.coolDown * 20 / 100;
                        }
                    }
                    if (skill.isSkillTemplate == 5 || skill.isSkillTemplate == 22 || skill.isSkillTemplate == 87) {
                        int num = skill.isSkillTemplate == 5 ? skill.getItemOption()[0].getParam()
                                : skill.isSkillTemplate == 22 ? 6 : 2;
                        for (int i = 0; i < num; i++) {
                            attackMonster(mob, skill);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {

                            }
                        }
                    } else {
                        attackMonster(mob, skill);
                    }
                    addMp(-skill.mpUsing);
                    if (pet != null && pet.typePet == 1) {
                        pet.attackMonster(this, mob);
                    }
                } else {
                    zone.getService().updatePostion(id, this.x, this.y);
                    service.serverMessage("Khoảng cách quá xa");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attackMonster(Mob mob, Skill skill) {
        if (this.isDead) {
            return;
        }
        if (taskId < 2) {
            getService().serverMessage("Tới nhiệm vụ thu thập thịt khủng long mới có thể dùng kỹ năng");
            return;
        }
        mob.lock.lock();
        try {
            if (!mob.isDead) {
                if (mob.hp > 0) {
                    selectSkill = skill;
                    setAbility();
                    if (taskId == 30 && taskMain.index == 0 && mob.id == 201) {
                        updateTaskCount(1);
                    }
                    if (taskId == 36 && taskMain.index == 0 && mob.id == 202) {
                        updateTaskCount(1);
                    }
                    int countDame = damage + damegeMonster;
                    int dame2 = countDame * 90 / 100;
                    int dameBoss = 0;
                    int dameHit = Util.nextInt(dame2, countDame);
                    int preHP = mob.hp;
                    if (taskId == 3 && taskMain != null && taskMain.index == 1 && mob.id == 239) {
                        updateTaskCount(1);
                    }
                    boolean isCrit = false;
                    if (Util.nextInt(100) < (crit / 100)) {
                        isCrit = true;
                        dameHit += dameHit * (criticalHitPower + 50) / 100;
                    }
                    if (skill.isSkillTemplate == 5) {
                        dameHit *= skill.getOptions()[0].getParam();
                    }
                    if (skill.isSkillTemplate == 6 && isDCTT) {
                        isDCTT = false;
                        dameHit *= 2;
                    }
                    boolean isChiuDon = Util.nextInt(0, 400) < chiuDon;
                    boolean isSuyGiam = Util.nextInt(0, 400) < suyGiam;
                    boolean isStun = Util.nextInt(0, 400) < stun;
                    if (isChiuDon) {
                        if (!mob.isEffect((short) 11)) {
                            Effect eff = new Effect(11, 1500, 0);
                            mob.effects.put(eff.template.id, eff);
                            zone.getService().addEffectMob(mob, eff);
                        }
                    }
                    if (isSuyGiam) {
                        if (!mob.isEffect((short) 8)) {
                            Effect eff = new Effect(8, 1500, 0);
                            mob.effects.put(eff.template.id, eff);
                            zone.getService().addEffectMob(mob, eff);
                        }
                    }
                    if (isStun) {
                        if (!mob.isEffect((short) 12)) {
                            Effect eff = new Effect(12, 1500, 0);
                            mob.effects.put(eff.template.id, eff);
                            zone.getService().addEffectMob(mob, eff);
                        }
                    }
                    if (skill.isSkillTemplate == 26) {
                        Effect eff = new Effect(55, options[417], options[254]);
                        mob.effects.put(eff.template.id, eff);
                        zone.getService().addEffectMob(mob, eff);
                        setLastTimeQuaCauDen(System.currentTimeMillis());
                    }
                    if (skill.isSkillTemplate == 29) {
                        Effect eff = new Effect(161, options[447], 0);
                        mob.effects.put(eff.template.id, eff);
                        zone.getService().addEffectMob(mob, eff);
                    }
                    if (skill.isSkillTemplate == 20) {
                        Effect eff = new Effect(160, options[446], 0);
                        mob.effects.put(eff.template.id, eff);
                        zone.getService().addEffectMob(mob, eff);
                    }
                    if (mob.isEffect((short) 8)) {
                        dameHit += dameHit * 50 / 100;
                    }
                    if (mob.isEffect((short) 11)) {
                        dameHit *= 2;
                    }
                    int pLevel = level();
                    int dLevel = Math.abs(mob.getTemplate().timeThuHoach - pLevel);
                    if (mob instanceof CellMax || mob instanceof AbsDragon || zone instanceof PrimaryForest || mob instanceof ThoDaiCa || mob instanceof Dracubin || zone instanceof DestructionoftheUniverseArea) {
                        dLevel = 0;
                    }
                    if (mob instanceof Cell) {
                        dLevel = 0;
                        Item item = getItemInBag(990);
                        if (item == null) {
                            dameHit = 0;
                        } else if (item.isExpired()) {
                            removeItem(item.index, 1, true);
                        }
                    }
                    if (zone instanceof BarrackArea) {
                        if (mob instanceof Buyon buyon && !buyon.isWallBroken()) {
                            dameHit = 0;
                        } else if (mob instanceof TrungUyThep trungUyThep) {
                            int phamVi = skill.dx;
                            if (trungUyThep.palmStrike < 30 && phamVi < 150) {
                                dameHit = 1;
                            }
                            if (phamVi >= 150) {
                                trungUyThep.palmStrike++;
                            }
                        }
                    } else if (mob.levelBoss == 7 && dLevel > 8) {
                        dameBoss = dameHit;
                        dameHit = 1;
                    }
                    if (mob.id == 273) {
                        if (taskPet) {
                            dameHit = dameBoss;
                        } else {
                            dameHit = 0;
                            MapManager.getInstance().joinZone(this, 86, 0, 1);
                            serverMessage("Bạn chưa nhận nhiệm vụ đệ tử nên không thể tấn công boss");
                        }
                    }
                    if (mob.id == 221 && taskOrders != null && taskOrders.taskId == TaskOrder.TASK_DI_CHUYEN_DA) {
                        if (!taskOrders.isComplete()) {
                            taskOrders.updateTask(1);
                            getService().updateTaskOrder(taskOrders);
                        }
                    }
                    if (skill.isSkillTemplate == 22) {
                        dameHit *= 6;
                    }
                    if (skill.isSkillTemplate == 18 || skill.isSkillTemplate == 14) {
                        if (skill.maxTarget > 1) {
                            try {
                                List<Mob> mobs = zone.getMobInZone(skill.maxTarget);
                                for (Mob mob1 : mobs) {
                                    if (mob1 != null && !mob1.isDead) {
                                        mob1.addHp(-dameHit);
                                        if (mob1.hp < 0) {
                                            mob1.hp = 0;
                                            if (mob1.id == 239 || mob1.id == 213 || mob1.id == 154) {
                                                mob1.hp = mob1.hpFull;
                                            }

                                        }
                                        if (mob1.hp <= 0) {
                                            mob1.die();
                                        }
                                        if (mob1.isDead) {
                                            mob1.dead(this, false);
                                        } else {
                                            if (mob1.id != 0) {
                                                mob1.addCharId(this.id);
                                            }
                                        }
                                        zone.sendAttackMob(mob1.idEntity, mob1.hp, isCrit);
                                    }
                                }
                            } catch (Exception e) {
                                Log.error("skill lan: " + e.getMessage(), e);
                            }
                        }
                    }

                    if (mob.id == 134 && dameHit > 10000) {
                        if (getEm().statusWithID(147)) {
                            dameHit = 15000;
                        } else if (getEm().statusWithID(151)) {
                            dameHit = 30000;
                        } else {
                            dameHit = 10000;
                        }
                    }
                    if (mob.levelBoss == 6 || mob.levelBoss == 7) {
                        int hpFull = mob.hpFull;
                        float percent = ((float) mob.hp / hpFull * 100);
                        if (percent < 10) {
                            int kiAdd = dameHit / 100;
                            kiAdd = Math.min(kiAdd, mob.kiNow);
                            mob.kiNow = Math.max(0, mob.kiNow - kiAdd);
                            kiAdd *= 2;
                            kiAdd += kiAdd * (increaseKI / 10) / 100;
                            addEXP(kiAdd);
                            Effect effect = getEm().findByID(142);
                            if (effect != null) {
                                kiAdd += kiAdd * 80 / 100;
                            }
                            if (pet != null) {
                                pet.addEXP(kiAdd);
                            }
                            if (this.getGroup() != null) {
                                long expGroup = kiAdd / 5;
                                List<Char> chars = this.getGroup().getCharsInZone(zone.map.id, zone.id);
                                for (Char _char : chars) {
                                    if (_char != null && !_char.isCleaned && _char != this && !_char.isDead) {
                                        _char.addEXP(expGroup);
                                    }
                                }
                            }
                            if (mob.kiNow <= 0) {
                                mob.kiNow = 0;
                                mob.isDead = true;
                                mob.addHp(-mob.hp);
                            }
                            if (mob.isDead) {
                                mob.dead(this, false);
                            } else {
                                if (mob.id != 0) {
                                    mob.addCharId(this.id);
                                }
                            }
                            zone.sendAttackMob(mob.idEntity, mob.hp - dameHit, isCrit);
                            zone.getService().updateHPMob(mob.idEntity, mob.hpFull, mob.hp, (byte) mob.levelBoss);
                            zone.setSkillPaint_1(mob, this, skill.id);
                            return;
                        }
                    }
                    mob.addHp(-dameHit);
                    if (mob.hp < 0) {
                        mob.hp = 0;
                        if (mob.id == 239 || mob.id == 213 || mob.id == 154 || mob.id == 221) {
                            mob.hp = mob.hpFull;
                        }

                    }
                    if (mob.id == 239) {
                        addHp(-(hp / 5));
                    }
                    int nextHP = mob.hp;
                    int hp = preHP - nextHP;
                    if (mob.hp <= 0) {
                        mob.die();
                    }
                    if (hp > 0 && mob.id != 154 && !mob.isDead) {
                        if (Util.nextInt(10) < 3) {
                            float percent = (float) hp / (mob.hpFull * (mob.levelBoss == 1 ? 40 : mob.levelBoss == 2 ? 200 : mob.levelBoss == 10 ? 100 : 1)) * 100;
                            int kiAdd = (int) (mob.kiNow * percent / 100);
                            kiAdd = Math.min(kiAdd, mob.kiNow);
                            mob.kiNow = Math.max(0, mob.kiNow - kiAdd);
                            kiAdd *= 2;
                            kiAdd += kiAdd * (increaseKI / 10) / 100;
                            byte isAddKi = (byte) Math.abs(mob.getTemplate().timeThuHoach - level());
                            if (isAddKi > 8 && !zone.map.isWorld() && !(zone instanceof PrimaryForest) && !(mob instanceof Cell || mob instanceof CellMax || mob instanceof ThoDaiCa || mob instanceof Dracubin) && mob.id != 273) { // không được nhận ki
                                service.notifyNoUpKi();
                            } else {
                                addEXP(kiAdd);
                                if (pet != null) {
                                    pet.addEXP(kiAdd);
                                }
                                if (this.getGroup() != null) {
                                    long expGroup = kiAdd / 5;
                                    List<Char> chars = this.getGroup().getCharsInZone(zone.map.id, zone.id);
                                    for (Char _char : chars) {
                                        if (_char != null && !_char.isCleaned && _char != this && !_char.isDead) {
                                            _char.addEXP(expGroup);
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if (mob.id == 134 || mob.id == 245) { // add Top dame Thỏ đại ca
                        mob.addDameBoss(this, dameHit);
                    }
                    if (mob.isDead) {
                        mob.dead(this, false);
                    } else {
                        if (mob.id != 0) {
                            mob.addCharId(this.id);
                        }
                    }
                    zone.sendAttackMob(mob.idEntity, mob.hp, isCrit);
                    zone.setSkillPaint_1(mob, this, skill.id);
                }
            }
        } catch (Exception E) {
            E.printStackTrace();
        } finally {
            mob.lock.unlock();
            selectSkill = null;
            setAbility();
        }
    }

    public void addEXP(long ki) {
        try {
            if (exp >= 2500000000000L) {
                exp = 2500000000000L;
                return;
            }
            Effect effect = em.findByID(42);
            int param = 0;
            if (effect != null) {
                param += effect.getParam();
            }
            effect = em.findByID(205);
            if (effect != null) {
                param += effect.getParam();
            }
            effect = em.findByID(206);
            if (effect != null) {
                param += effect.getParam();
            }
            effect = em.findByID(207);
            if (effect != null) {
                param += effect.getParam();
            }
            ki += ki * param / 100;
            if (zone.map.isTrainZone() || zone.map.isBarrack()) {
                ki *= 2;
            }
            if (zone instanceof HellArena) {
                ki += ki / 2;
            }
            ki *= Config.getInstance().getExpRate();
            if (!lockEXP) {
                exp += ki;
            }
            KI += ki / 10;
//            getService().showKI();
            getService().showKI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int level() {
        long var1 = this.exp;

        int var3;
        for (var3 = 0; var3 < DataCenter.gI().expTemp.length && var1 >= DataCenter.gI().expTemp[var3]; ++var3) {
            var1 -= DataCenter.gI().expTemp[var3];
        }

        return var3;
    }

    public synchronized void addSkill(Skill skill) {
        if (skill != null) {
            skills.add(skill);
            service.updateSkill();
        }
    }

    public void useSkill(int indexSkill) {
        try {
            Log.debug("use skill: " + indexSkill);
            if (indexSkill < 0 || indexSkill >= skills.size()) {
                return;
            }
            Skill skill = skills.get(indexSkill);
            if (skill == null) {
                return;
            }
            if (exp < skill.kiUpgrade) {
                this.skills.set(indexSkill, null);
                return;
            }
            Log.debug("use skill: " + skill.id + " level: " + skill.level + " mp: " + skill.mpUsing);
            if (skill != null) {
                if (skill.isCoolDown()) {
//                    Effect eff = getEm().findByID(136);
//                    if (eff == null) {
                    getService().serverMessage("Kỹ năng đang hồi");
                    return;
//                    }
                }
                if (mp < skill.mpUsing) {
                    getService().serverMessage("Không đủ MP");
                    return;
                }
                switch (skill.isSkillTemplate) {
                    case 3:
                    case 8:
                    case 15:
                    case 21:
                    case 24:
                        ItemOption[] itemOptions = skill.getOptions();
                        if (itemOptions != null && itemOptions.length > 2) {
                            int param = itemOptions[1].getParam();
                            Effect effect = new Effect(86 + skill.level, 90000 + (30000 * skill.level), param);
                            em.setEffect(effect);
                        }
                        break;
                    case 23:
                        itemOptions = skill.getOptions();
                        addHp(maxHP * itemOptions[0].getParam() / 100);
                        if (getGroup() != null) {
                            List<Char> chars = getGroup().getCharsInZone(zone.map.id, zone.id);
                            if (chars != null && !chars.isEmpty()) {
                                for (Char _char : chars) {
                                    if (_char != null && !_char.isCleaned && _char != this && !_char.isDead) {
                                        _char.addHp(_char.maxHP * itemOptions[1].getParam() / 100);
                                    }
                                }
                            }
                        }
                        break;
                    case 2:
                        itemOptions = skill.getOptions();
                        if (itemOptions != null && itemOptions.length > 2) {
                            int time = itemOptions[2].getParam();
                            int param = itemOptions[0].getParam();
                            Effect effect = new Effect(85, time, param);
                            em.setEffect(effect);
                        }
                        break;
                    case 17:
                        itemOptions = skill.getOptions();
                        if (itemOptions != null && itemOptions.length > 0) {
                            int param = itemOptions[0].getParam();
                            Effect effect = new Effect(113, 15000, param);
                            em.setEffect(effect);
                        }
                        break;
                    case 10:
                        itemOptions = skill.getOptions();
                        if (itemOptions != null && itemOptions.length > 2) {
                            int time = itemOptions[2].getParam();
                            int param = itemOptions[0].getParam();
                            Effect effect = new Effect(111, time, param);
                            em.setEffect(effect);
                            addHp(-(itemOptions[1].getParam() * maxHP / 100));
                            if (hp <= 0) {
                                startDie();
                            }
                        }
                        break;
                    case 39:
                        Effect effect = new Effect(99, 60000, 0);
                        em.setEffect(effect);
                        break;
                    case 40:
                        effect = new Effect(100, 60000, 0);
                        em.setEffect(effect);
                        break;
                    case 41:
                        effect = new Effect(101, 60000, 0);
                        em.setEffect(effect);
                        break;
                    case 46:
                        effect = new Effect(106, 30000, 0);
                        em.setEffect(effect);
                        break;
                    case 47:
                        effect = new Effect(107, 30000, skill.getOptions()[1].getParam());
                        em.setEffect(effect);
                        break;
                    case 48:
                        effect = new Effect(108, 5000, 0);
                        em.setEffect(effect);
                        break;
                    case 55:
                        effect = new Effect(102, 60000, skill.getOptions()[1].getParam());
                        em.setEffect(effect);
                        break;
                    case 93:
                        effect = new Effect(171 + skill.level, 30000, 0);
                        em.setEffect(effect);
                        break;
                    case 62:
                        itemOptions = skill.getOptions();
                        if (itemOptions != null && itemOptions.length > 0) {
                            int time = itemOptions[0].getParam() * 1000;
                            effect = new Effect(skill.level == 2 ? 189 : 136, time, 0);
                            em.setEffect(effect);
                        }
                        break;
                    case 82:
                        itemOptions = skill.getOptions();
                        if (itemOptions != null && itemOptions.length > 1) {
                            int time = itemOptions[0].getParam();
                            int param = itemOptions[1].getParam() / 10;
                            effect = new Effect(59, time, param);
                            Effect effect1 = new Effect(60, time, param);
                            em.setEffect(effect);
                            em.setEffect(effect1);
                        }
                        break;
                }
                addMp(-skill.mpUsing);
                skill.timeCoolDown = System.currentTimeMillis() + skill.coolDown;
                if (skill.isSkillCaiTrang) {
                    timeCoolDownSkillCaiTrang = System.currentTimeMillis() + skill.coolDown;
                }
                setAbility();
                getService().updateHPFull();
                getService().updateMPFull();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEveryHalfSecond() {
        try {
            int hpBuff = 0;
            int mpBuff = 0;
            try {
                hpBuff = IntStream.of(26, 136, 143, 200, 256, 295, 347).map(i -> options[i]).sum();
                mpBuff = IntStream.of(27, 137, 257, 286, 256, 296, 348, 402).map(i -> options[i]).sum();
            } catch (Exception e) {

            }

            if (em != null) {
                em.update();
                List<Effect> effects = em.filter(e -> {
                    int id = e.template.id;
                    int type = e.template.type;
                    if ((id >= 85 && id <= 130) || (id >= 172 && id <= 175)) {
                        return false;
                    }
                    return true;
                });
                if (!effects.isEmpty()) {
                    for (Effect eff : effects) {
                        if (eff.template.type == 0 || eff.template.id == 167) {
                            hpBuff += eff.getParam();
                            mpBuff += eff.getParam();
                        }
                    }
                }

                if (getEm().statusWithID(9)) {
                    addHp(-(maxHP / 100));
                }
            }
            if (hpBuff > 0 && !isDead) {
                addHp(hpBuff);
                getService().updateHP();
            }
            if (mpBuff > 0 && !isDead) {
                addMp(mpBuff);
                getService().updateMP();
            }
            if (pet != null && !pet.isDead && (pet.typePet == 1 || pet.typePet == 2)) {
                int buffHpPet = 0;
                try {
                    buffHpPet = IntStream.of(26, 136, 143, 200, 256, 295, 347).map(i -> pet.options[i]).sum();
                } catch (Exception e) {
                }

                if (buffHpPet > 0) {
                    pet.addHp(buffHpPet);
                }
                if (mpBuff > 0) {
                    pet.addMp(mpBuff);
                }
            }
            if (timeUpgradeBean > 0 && timeUpgradeBean < System.currentTimeMillis()) {
                timeUpgradeBean = 0;
                levelBean++;
                getService().sendLevelBean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.debug("update every half second");
    }

    public void updateEverySecond() {
        if (invite != null) {
            invite.update();
        }
        if (removeItemRac) {
            updateRemoveItemRac();
        }
        int timeCurrent = (int) (System.currentTimeMillis() / 1000);
        if (TimeReset != 0 && TimeReset < timeCurrent) {
            TimeReset = 0;
            MaBaoVe = "";
            IsDaMoKhoa = false;
        }
        if (pet != null && pet.typePet == 2) {
            Mob mob = zone.findMobAbs(this);
            if (mob == null || mob.hp <= 0) {
                return;
            }
            pet.attackMonster(this, mob);
        }
    }

    public void updateEveryFiveSecond() {
        if (zone.map.isNgucTu()) {
            if (isDead) {
                wakeUpFromDead();
            }
            if (timeNgucTu < System.currentTimeMillis()) {
                joinZone(54, 0, 0);
            }
        }
//        Log.debug("update every five second");
    }

    public void updateEveryMinute() {
        try {
            timeOnline += 60_000;
            totalTimeOnline += 60_000;
            if (timeZoom < System.currentTimeMillis() && zone.map.isTrainZone()) {
                World world = findWorld(World.TIME_ROOM);
                if (world != null) {
                    removeWorld(World.TIME_ROOM);
                }
                joinZone(56, 0, -1);
            }
            if (body[12] != null) {
                timeGiapLuyenTap += 60000;
            }
            this.updatePoint(KeyPoint.MAKE_CAKE, this.pointMakeCake);
            this.updatePoint(KeyPoint.THA_LONG_DEN, this.pointThaLongDen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRemoveItemRac() {
        try {
            if (bag != null) {
                for (Item item : bag) {
                    if (item != null) {
                        int chiSoGoc = totalNumChiSoGoc(item);
                        int kichEpSao = totalNumKichSao(item);
                        if (item.template.type == 2 || item.template.type == 4 || item.template.type == 6 || item.template.type == 8) {
                            if (!item.isDoThanLinh() && item.upgrade == 0 && (chiSoGoc <= 3 || kichEpSao <= 6)) {
                                int quantity = item.getQuantity();
                                int price = item.getPrice() * quantity;
                                addCoin(price, true);
                                Util.writing("loggame/saleitem/" + name + ".txt", "Bán vật phẩm: " + item.getTemplate().name + " số lượng: " + item.getQuantity() + "\n");
                                removeItem(item.index, quantity, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int totalNumKichSao(Item item) {
        int i = 0;
        if (item.options() != null) {
            for (ItemOption io : item.options()) {
                if (io.typeKichSao()) {
                    i++;
                }
            }
        }
        return i;
    }

    public int totalNumChiSoGoc(Item item) {
        int i = 0;
        if (item.options() != null) {
            for (ItemOption io : item.options()) {
                if (io.typeChiSoGoc()) {
                    i++;
                }
            }
        }
        return i;
    }

    private int getNumBean() {
        int var0 = 5 + levelBean;
        int var1;
        if ((var1 = (int) ((System.currentTimeMillis() / 1000 - timeReciveBean) / 60)) > var0) {
            var1 = var0;
        }

        return var1;
    }

    public void useItem(Message ms) {
        lockItem.lock();
        try {
            if (isDead) {
                return;
            }
            if (trade != null) {
                warningTrade();
                return;
            }
            short indexUI = ms.reader().readShort();
            if (indexUI >= 0 && indexUI <= numberCellBag) {
                Item item = bag[indexUI];
                if (item != null && item.has()) {
                    UseItemHandler.useItem(this, item);
                } else {
                    Log.debug("item null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void updateAddSlotBag() {
        Item[] item = new Item[numberCellBag];
        System.arraycopy(bag, 0, item, 0, bag.length);
        bag = item;
        getService().updateSlotBag();
        getService().updateIndexBag();
    }

    public Item getItemInBag(int id) {
        for (Item item : bag) {
            if (item != null && item.id == id) {
                return item;
            }
        }
        return null;
    }

    public void itemBodyToBag(Message ms) {
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            byte indexUI = ms.reader().readByte();
            Item item = body[indexUI];
            if (item != null) {
                if (item.getTemplate().type == 0) {
                    if (body[3] != null || body[5] != null || body[7] != null || body[9] != null || body[11] != null) {
                        getService().serverDialog("Không thể tháo đệ tử khi đang mang trang bị");
                        return;
                    }
                }
                if (item.getTemplate().type == 3 || item.getTemplate().type == 5 || item.getTemplate().type == 5 || item.getTemplate().type == 7) {
                    if (pet != null) {
                        pet.setAbility();
                    }
                }
                for (int i = 0; i < numberCellBag; i++) {
                    if (bag[i] == null) {
                        item.setLock(true);
                        item.index = i;
                        bag[i] = item;
                        body[indexUI] = null;
                        getService().itemBodyToBag(indexUI, i);
                        if (item.isExpired()) {
                            removeItem(i, 1, true);
                        }
                        if (item.getTemplate().type == 14) {
                            int index = findIndexSkillWithCT();
                            if (index != -1) {
                                skills.remove(index);
                                getService().updateSkill();
                            }
                        }
                        if (item.getTemplate().type == 12) {
                            if (item.id == 345) {
                                Effect eff = new Effect(150, timeGiapLuyenTap, 0);
                                getEm().setEffect(eff);
                                timeGiapLuyenTap = 0;
                            } else if (item.id == 1090) {
                                Effect eff = new Effect(188, timeGiapLuyenTap, 0);
                                getEm().setEffect(eff);
                                timeGiapLuyenTap = 0;
                            }
                        }
                        setAbility();
                        getService().updateHP();
                        getService().updateMP();
                        zone.getService().updateItemBody(this);
                        if (item.getTemplate().type == 0) {
                            pet = null;
                        }
                        return;
                    }
                }
                warningBagFull();
            } else {
                Log.debug("item null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Item[] checkBag(int var1) {
        return switch (var1) {
            case 0 ->
                bag;
            case 1 ->
                box;
            case 2 ->
                body;
            case 3 ->
                body2;
            case 4 ->
                extend;
            default ->
                null;
        };
    }

    public void upgradeItem(Message ms) {
        lockItem.lock();
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            byte type = ms.reader().readByte();
            short equipIndex = ms.reader().readShort();
            int indexBua = ms.reader().readShort();
            byte size = ms.reader().readByte();
            Item item = checkBag(type)[equipIndex];
            if (item == null) {
                return;
            }
            if ((!item.isTypeClothe() && !item.isTypePet() && !item.isTypeWeapon())) {
                serverDialog("Vật phẩm không thể nâng cấp");
                return;
            }
            if (item.upgrade >= 20) {
                serverDialog("Trang bị đã đạt cấp tối đa");
                return;
            }
            int numberCrystal = 0;
            Vector<Item> crystals = new Vector<>();
            boolean[] list = new boolean[numberCellBag];
            while (ms.reader().available() > 0) {
                short index = ms.reader().readShort();
                if (index < 0 || index >= numberCellBag) {
                    continue;
                }
                if (!list[index]) {
                    list[index] = true;
                    if (bag[index] != null && (bag[index].id <= 11 || bag[index].template.type == 28) && bag[index].getQuantity() == 1) {
                        numberCrystal++;
                        crystals.add(bag[index]);
                    }
                }
            }
            if (crystals.size() > 20) {
                return;
            }
            if (numberCrystal == 0) {
                serverDialog("Hãy chọn đá nâng cấp!");
                return;
            }
            long temp = 0;
            int percent = 0;
            int price = 0;
            for (Item itm : crystals) {
                if (itm != null && itm.template.type == 21) {
                    temp += DataCenter.gI().UP_CRYSTAL[itm.id];
                }
            }
            if (item.isTypePet()) {
                price = DataCenter.gI().aq[item.upgrade + 1];
                percent = (int) DataCenter.gI().av[item.upgrade + 1];
            }
            if (item.isTypeClothe()) {
                price = DataCenter.gI().ap[item.upgrade + 1];
                percent = (int) DataCenter.gI().au[item.upgrade + 1];
            }
            if (item.isTypeWeapon()) {
                price = DataCenter.gI().ao[item.upgrade + 1];
                percent = (int) DataCenter.gI().at[item.upgrade + 1];
            }
            percent = (int) (temp * 100 / percent);
            int maxPercent = 60;
            if (percent > maxPercent) {
                percent = maxPercent;
            }
            if (this.coin < price) {
                serverDialog("Không đủ xu");
                return;
            }

            boolean isBaoHiem = indexBua != -1;

            int rand = Util.nextInt(0, 100);
            boolean isUpgrade = false;
            if (isBaoHiem) {
                removeItem(indexBua, 1, true);
            }
            if (rand < percent) {
                if (taskId == 5 && taskMain != null && taskMain.index == 1) {
                    updateTaskCount(1);
                }
                addCoin(-price, true);
                removeByUpgrade(crystals);
                isUpgrade = true;
                item.isLock = true;
                item.upgradeItem(item.getUpgrade() + 1);
                if (item.getUpgrade() >= 10) {
                    int level = item.getUpgrade() % 2 == 0 ? item.getUpgrade() / 2 : item.getUpgrade() / 2 + 1;
                    GlobalService.getInstance().chat("Hệ thống", "Chiến binh c#item" + name + "c#white vừa ép sao thành công " + item.getTemplate().name + " lên " + level + " , thật khủng khiếp", (byte) 0);
                }
            } else if (!(isBaoHiem && percent == maxPercent)) {
                addCoin(-price, true);
                removeByUpgrade(crystals);
                if (item.getUpgrade() >= 14) {
                    int level = (item.getUpgrade() + 1) % 2 == 0 ? item.getUpgrade() / 2 : item.getUpgrade() / 2 + 1;
                    GlobalService.getInstance().chat("Hệ thống", "Chiến binh c#item" + name + "c#white vừa nâng cấp thất bại " + item.getTemplate().name + " lên " + level + " , thật đen đủi mà còn không dùng bảo hiểm", (byte) 0);
                }
            }
            if (isBaoHiem) {
                if (item.getUpgrade() >= 14) {
                    int level = (item.getUpgrade() + 1) % 2 == 0 ? item.getUpgrade() / 2 : item.getUpgrade() / 2 + 1;
                    GlobalService.getInstance().chat("Hệ thống", "Chiến binh c#item" + name + "c#white vừa nâng cấp thất bại " + item.getTemplate().name + " lên " + level + " , thật may mắn là có dùng bảo hiểm", (byte) 0);
                }
            }
            getService().upgrade(isUpgrade, isBaoHiem, crystals, item, (short) indexBua, type);
            getService().updateIndexBag();
        } catch (Exception ex) {
            Log.error("upgrade item err: " + ex.getMessage(), ex);
        } finally {
            lockItem.unlock();
        }

    }

    private void removeByUpgrade(Vector<Item> crystals) {
        for (Item itm : crystals) {
            if (itm != null && itm.template.type == 21) {
                removeItem(itm.index, itm.getQuantity(), false);
            }
        }
    }

    public void warningBagFull() {
        getService().warningMessage("Túi đồ đầy");
    }

    public void showKIChar(Message mss) {
        try {
            String name = mss.readUTF();
            if (this.name.contains(name)) {
                this.service.showKI(this);
            } else {
                Char pl = ServerManager.findCharByName(name);
                if (pl != null) {
                    this.service.showKI(pl);
                } else {
                    this.service.serverDialog("Không tìm thấy người chơi");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeZone(Message ms) {
        try {
            byte zoneId = ms.reader().readByte();
            Map map = zone.map;
            List<Zone> zones = map.getZones();
            if (zone instanceof ZWorld || zone.map.isNgucTu()) {
                zone.join(this, -1);
                serverMessage("Không thể chuyển khu vực");
                return;
            }
            if (zone instanceof ZPlanetBlackStar && this.itemMapBlackDragonBall != null) {
                serverMessage("Không thể chuyển khu vực khi đang cầm ngọc rồng");
                return;
            }
            if (zoneId < 0 || zoneId >= zones.size()) {
                return;
            }
            if (zones.get(zoneId).findBossInZone()) {
                if (clan == null && user.activated == 1) {
                    MapManager.getInstance().joinZone(this, 86, zoneId, 0);
                    service.serverMessage("Bạn phải tham gia vũ trụ hoặc MTV để tham gia tiêu diệt boss");
                    return;
                }
            }
            if (zones.get(zoneId).getNumberChar() >= map.getMaxPlayer(zones.get(zoneId))) {
                zone.join(this, -1);
                getService().serverDialog("Khu vực đã đầy!");
                return;
            }
            outZone();
            joinZone(this.mapId, zoneId, -1);
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void addClanPoint(int point) {
        if (this.clan != null) {
            Member mem = this.clan.getMemberByName(this.name);
            if (mem != null) {
                mem.addPointClanWeek(point);
                mem.addPointClan(point);
                this.clan.addExp(point);
                this.serverMessage("Bạn nhận được " + point + " điểm cống hiến vũ trụ.");
            }
        }
    }

    public void loadDisplay() {
        try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(
                "SELECT `players`.`id`,`players`.`name`, `players`.`gender`, `players`.`class`, `players`.`last_logout_time`, `players`.`body`, `players`.`online`,`data` FROM `players` WHERE `players`.`user_id` = ? AND `players`.`server_id` = ?");) {
            ps.setInt(1, user.id);
            ps.setInt(2, Config.getInstance().getServerID());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    loadDisplay(rs);
                }
            }
        } catch (Exception ex) {
            Log.error("load display err: " + ex.getMessage(), ex);
        }
    }

    public void loadDisplay(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            gender = rs.getByte("gender");
            idCharSys = rs.getByte("class");
            sys = (byte) (idCharSys + 1);
            body = new Item[16];
            JSONArray jArr = (JSONArray) JSONValue.parse(rs.getString("body"));
            if (jArr != null) {
                int len = jArr.size();
                for (int i = 0; i < len; i++) {
                    JSONObject obj = (JSONObject) jArr.get(i);
                    Item item = new Item(obj);
                    item.index = item.getTemplate().type;
                    body[item.index] = item;
                }
            }
            JSONObject json = (JSONObject) JSONValue.parse(rs.getString("data"));
            ParseData parse = new ParseData(json);
            exp = parse.getLong("exp");
        } catch (SQLException ex) {
            Log.error("load display err: " + ex.getMessage(), ex);
        }
    }

    public void warningTrade() {
        getService().serverMessage("Không thể thực hiện khi đang giao dịch.");
        getService().tradeCancel();
        cleanTrade();
    }

    public void close() {
        try {
            if (isLoadFinish()) {
                if (trade != null) {
                    trade.closeUITrade();
                }
//                if (arenaT != null) {
//                    arenaT.setWin(false);
//                }
                if (worlds != null) {
                    synchronized (worlds) {
                        worlds.forEach((t) -> {
                            if (!t.isClosed()) {
                                t.removeMember(this);
                            }
                        });
                    }
                }
                if (this.group != null) {
                    outParty();
                }
                if (zone != null) {
                }
//                    if (zone.tilemap.isDauTruong()) {
//                        TalentShow tls = MapManager.getInstance().talentShow;
//                        tls.removePlayer(this);
//                    }
            }
            if (this.clan != null) {
                Member mem = this.clan.getMemberByName(name);
                if (mem != null) {
                    mem.setOnline(false);
                    mem.setChar(null);
                }
//                    clan.getClanService().requestClanMember();
            }

            try {

            } catch (Exception e) {
                Log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            Log.error("err: " + e.getMessage(), e);
        }
    }

    public void cleanTrade() {
        this.trade = null;
        this.myTrade = null;
        this.partnerTrade = null;
    }

    public void cleanUp() {
        this.isCleaned = true;
        this.bag = null;
        this.body = null;
        this.box = null;
        this.body2 = null;
        this.friends = null;
        this.skills = null;

    }

    public synchronized void saveData() {
        if (isLoadFinish() && !saving) {
            try {
                saving = true;
                this.lastLogoutTime = System.currentTimeMillis();
                JSONArray bags = new JSONArray();
                for (int i = 0; i < this.numberCellBag; i++) {
                    try {
                        if (this.bag[i] != null) {
                            bags.add(this.bag[i].toJSONObject());

                        }
                    } catch (Exception e) {
                    }
                }
                JSONArray boxs = new JSONArray();
                for (int i = 0; i < this.numberCellBox; i++) {
                    try {
                        if (this.box[i] != null) {
                            this.box[i].index = i;
                            boxs.add(this.box[i].toJSONObject());
                        }
                    } catch (Exception e) {
                    }
                }
                JSONArray body = new JSONArray();
                for (int i = 0; i < 16; i++) {
                    try {
                        if (this.body[i] != null) {
                            body.add(this.body[i].toJSONObject());
                        }
                    } catch (Exception e) {
                    }
                }
                JSONArray body2 = new JSONArray();
                for (int i = 0; i < 16; i++) {
                    try {
                        if (this.body2[i] != null) {
                            body2.add(this.body2[i].toJSONObject());
                        }
                    } catch (Exception e) {
                    }
                }
                JSONArray skill = new JSONArray();
                if (this.skills != null) {
                    try {
                        for (Skill s : this.skills) {
                            if (s.getSkillTemplate().i != 1) {
                                skill.add(s.toJSONObject());
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                JSONArray friends = new JSONArray();
                if (this.friends != null) {
                    Friend[] fr = getFriends();
                    for (Friend friend : fr) {
                        friends.add(friend.toJSONObject());
                    }
                }
                JSONArray title = new JSONArray();
                JSONArray effects = em.toJSONArray();
                JSONArray mail = mailManager.toJson();
                if (this.danhHieu != null) {
                    try {
                        for (DanhHieu danhHieu : this.danhHieu) {
                            if (danhHieu != null) {
                                title.add(danhHieu.toJSONObject());
                            }
                        }
                    } catch (Exception e) {

                    }
                }
                JSONArray rewards = new JSONArray();
                for (com.vdtt.model.Reward rw : this.rewardList) {
                    rewards.add(rw.toJson());
                }
                JSONObject data = new JSONObject();
                data.put("exp", this.exp);
                data.put("pointPhamChat", this.pointPhamChat);
                data.put("pointEnthusiastic", this.pointEnthusiastic);
                data.put("pointEnthusiasticWeek", this.pointEnthusiasticWeek);
                data.put("pointUpgrageStar", this.pointUpgrageStar);
                data.put("pointUpgrageStarWeek", this.pointUpgrageStarWeek);
                data.put("pointKame", this.pointKame);
                data.put("pointSuper", this.pointSuper);
                data.put("lvPK", this.lvPK);
                data.put("speed", this.speed);
                data.put("levelBean", this.levelBean);
                data.put("timeReciveBean", this.timeReciveBean);
                data.put("zeniLock", this.zeniLock);
                data.put("sachChienDau", this.sachChienDau);
                data.put("timeTienIch", this.timeTienIch);
                data.put("rank", this.rank);
                data.put("nhanMocNap", this.nhanMocNap);
                data.put("mocNap", this.mocNap);
                data.put("tieuNgay", this.tieuNgay);
                data.put("tieuTuan", this.tieuTuan);
                data.put("mocNap", this.mocNap);
                data.put("timeUpgradeBean", this.timeUpgradeBean);
                data.put("totalTimeOnline", this.totalTimeOnline);
                data.put("timeOnline", this.timeOnline);
                data.put("totalKiNang", this.totalKiNang);
                data.put("maxBean", this.maxBean);
                data.put("totalTA", this.totalTA);
                data.put("totalTL", this.totalTL);
                data.put("totalSQ", this.totalSQ);
                data.put("ngayOnline", this.ngayOnline);
                data.put("napNgay", this.napNgay);
                data.put("packOnline", this.packOnline);
                data.put("packTinhAnh", this.packTinhAnh);
                data.put("packThuLinh", this.packThuLinh);
                data.put("packSieuQuai", this.packSieuQuai);
                data.put("countVongQuay", this.countVongQuay);
                data.put("countTui1", this.countTui1);
                data.put("countTui2", this.countTui2);
                data.put("countTui3", this.countTui3);
                data.put("timeZoom", this.timeZoom);
                data.put("countQuaSinhLuc", this.countQuaSinhLuc);
                data.put("taskPet", this.taskPet);
                data.put("pointHell", this.pointHell);
                data.put("increaseExtra", this.increaseExtra);
                data.put("lockEXP", this.lockEXP);
                data.put("countTuiDo", this.countTuiDo);
                data.put("countHell", this.countHell);
                data.put("countTimeRoom", this.countTimeRoom);
                data.put("countBarrack", this.countBarrack);
                data.put("countUseBarrackCard", this.countUseBarrackCard);
                data.put("countTheHell", this.countTheHell);
                data.put("timeGiapLuyenTap", this.timeGiapLuyenTap);
                data.put("countTaskQuyLao", this.countTaskQuyLao);
                data.put("countNgucTu", this.countNgucTu);
                data.put("countUseItemNgucTu", this.countUseItemNgucTu);
                data.put("diemBTTBangHoa", this.diemBTTBangHoa);
                data.put("TimeChatColor", this.TimeChatColor);
                data.put("timeChangeSkin", this.timeChangeSkin);
                data.put("timeWaitChangeSkin", this.timeWaitChangeSkin);
                data.put("MaBaoVe", this.MaBaoVe);
                data.put("TimeReset", this.TimeReset);
                data.put("resetTimeTichLuy", this.resetTimeTichLuy);
                TaskOrder task1 = this.taskOrders;
                if (task1 != null) {
                    JSONObject obj = new JSONObject();
                    obj.put("taskId", task1.taskId);
                    obj.put("count", task1.count);
                    obj.put("maxCount", task1.maxCount);
                    obj.put("killId", task1.killId);
                    obj.put("mapId", task1.mapId);
                    obj.put("failed", task1.failed);
                    data.put("taskOrder", obj);
                }
                if (itemQuaySo != null) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", itemQuaySo.getId());
                    obj.put("quantity", itemQuaySo.getQuantity());
                    obj.put("iditem", itemQuaySo.getIdItem());
                    obj.put("type", itemQuaySo.getType());
                    data.put("itemQuaySo", obj);
                }
                if (sach != null) {
                    data.put("sach", sach.toJSONObject());
                }
                data.put("isClearCT", this.resetRewarNap);
                data.put("timeOutClan", this.timeOutClan);

                List<PlayerAchievement> achievementList = achievements.getAchievements();
                Gson gson = new Gson();
                JSONArray array = (JSONArray) JSONValue.parse(gson.toJson(achievementList));
                data.put("achievements", array);

                JSONArray map = new JSONArray();
                map.add(this.mapId);
                map.add(this.x);
                map.add(this.y);
                String task = "";
                if (this.taskMain != null) {
                    JSONObject t = new JSONObject();
                    t.put("id", this.taskMain.taskId);
                    t.put("index", this.taskMain.index);
                    t.put("count", this.taskMain.count);
                    task = t.toJSONString();
                }
                String jBag = bags.toJSONString();
                String jBox = boxs.toJSONString();
                String jBody = body.toJSONString();
                String jBody2 = body2.toJSONString();
                String jSkill = skill.toJSONString();
                String jTitle = title.toJSONString();
                String jData = data.toJSONString();
                String jMap = map.toJSONString();
                try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE `players` SET `zeni` = ?,`ballz` = ?,`coin` = ?,`kimCuong` = ?,`KI` = ?,`numberCellBag` = ?,`numberCellBox` = ?, `data` = ?, `bag` = ?, `box` = ?, `body` = ?, `body2` = ?, `skill` = ?, `title` = ?, `map` = ?,`effect` = ?,`mail` = ?,`rewards` = ?,`task` = ?,`taskId` = ?,`friends` = ?, `last_logout_time` = ?, `pointMakeCake` = ?, `pointThaLongDen` = ? WHERE `id` = ?;")) {
                    stmt.setLong(1, this.zeni);
                    stmt.setInt(2, this.ballZ);
                    stmt.setInt(3, this.coin);
                    stmt.setInt(4, this.kimCuong);
                    stmt.setLong(5, this.KI);
                    stmt.setShort(6, this.numberCellBag);
                    stmt.setShort(7, this.numberCellBox);
                    stmt.setString(8, jData);
                    stmt.setString(9, jBag);
                    stmt.setString(10, jBox);
                    stmt.setString(11, jBody);
                    stmt.setString(12, jBody2);
                    stmt.setString(13, jSkill);
                    stmt.setString(14, jTitle);
                    stmt.setString(15, jMap);
                    stmt.setString(16, effects.toJSONString());
                    stmt.setString(17, mail.toJSONString());
                    stmt.setString(18, rewards.toJSONString());
                    stmt.setString(19, task);
                    stmt.setShort(20, this.taskId);
                    stmt.setString(21, friends.toJSONString());
                    stmt.setLong(22, this.lastLogoutTime);
                    stmt.setInt(23, this.pointMakeCake);
                    stmt.setInt(24, this.pointThaLongDen);
                    stmt.setInt(25, this.id);
                    stmt.executeUpdate();
                }
                Log.debug("save data " + this.name + " time: " + this.lastLogoutTime);
                if (eventPoint != null) {
                    DbManager.getInstance().storeEventPoint(eventPoint);
                }
            } catch (Exception e) {
                Log.error("saveData charName: " + this.name + " ex: " + e.getMessage(), e);
            } finally {
                saving = false;
            }
        }
    }

    public synchronized boolean load() {
        try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `players` WHERE `user_id` = ? AND `server_id` = ?;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            stmt.setInt(1, user.id);
            stmt.setInt(2, Config.getInstance().getServerID());
            try (ResultSet data = stmt.executeQuery()) {
                if (!data.next()) {
                    return false;
                }
                this.id = data.getInt("id");
                this.name = data.getString("name");
                gender = data.getByte("gender");
                idCharSys = data.getByte("class");
                sys = (byte) (idCharSys + 1);
                ballZ = data.getInt("ballz");
                zeni = data.getInt("zeni");
                coin = data.getInt("coin");
                if (ballZ < 0) {
                    ballZ = 0;
                }
                kimCuong = data.getInt("kimcuong");
                numberCellBag = data.getShort("numberCellBag");
                numberCellBox = data.getShort("numberCellBox");
                KI = data.getLong("KI");
                bag = new Item[numberCellBag];
                int clan = data.getInt("clan");
                if (clan != 0) {
                    Optional<Clan> g = Clan.getClanDAO().get(clan);
                    if (g != null && g.isPresent()) {
                        this.clan = g.get();
                        Member mem = this.clan.getMemberByName(this.name);
                        if (mem == null) {
                            this.clan = null;
                        }
                    }
                }
                JSONArray jArr = (JSONArray) JSONValue.parse(data.getString("bag"));
                int len = 0;
                if (jArr != null) {
                    len = jArr.size();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = (JSONObject) jArr.get(i);
                        Item item = new Item(obj);
                        item.index = i;
                        if (item.isBug) {
                        //    DbManager.getInstance().banuntil_account(user.username, 9999);
                       //     user.session.disconnect();
                        }
                        bag[i] = item;
                    }
                }
                box = new Item[numberCellBox];
                try {
                    jArr = (JSONArray) JSONValue.parse(data.getString("box"));
                    if (jArr != null) {
                        len = jArr.size();
                        for (int i = 0; i < len; i++) {
                            JSONObject obj = (JSONObject) jArr.get(i);
                            Item item = new Item(obj);
                            item.index = i;
                            if (item.isBug) {
                        //        DbManager.getInstance().banuntil_account(user.username, 9999);
                         //       user.session.disconnect();
                            }
                            box[i] = item;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                body = new Item[16];
                jArr = (JSONArray) JSONValue.parse(data.getString("body"));
                if (jArr != null) {
                    len = jArr.size();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = (JSONObject) jArr.get(i);
                        Item item = new Item(obj);
                        item.index = item.getTemplate().type;
                        body[item.index] = item;
                    }
                }
                body2 = new Item[16];
                jArr = (JSONArray) JSONValue.parse(data.getString("body2"));
                if (jArr != null) {
                    len = jArr.size();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = (JSONObject) jArr.get(i);
                        Item item = new Item(obj);
                        item.index = item.getTemplate().type;
                        body2[item.index] = item;
                    }
                }
                JSONArray effects = (JSONArray) JSONValue.parse(data.getString("effect"));
                int size2 = effects.size();
                for (int i = 0; i < size2; i++) {
                    try {
                        JSONObject obj = (JSONObject) effects.get(i);
                        int id = Integer.parseInt(obj.get("id").toString());
                        int param = Integer.parseInt(obj.get("param").toString());
                        long startAt = 0;
                        long endAt = 0;
                        long timeLength = 0;
                        if (obj.containsKey("timeStart")) {
                            int timeStart = Integer.parseInt(obj.get("timeStart").toString());
                            int timeLength2 = Integer.parseInt(obj.get("timeLength").toString());
                            startAt = System.currentTimeMillis();
                            endAt = startAt + ((timeLength2 - timeStart) * 1000);

                        } else {
                            timeLength = Long.parseLong(obj.get("time_length").toString());

                            startAt = Long.parseLong(obj.get("start_at").toString());
                            endAt = Long.parseLong(obj.get("end_at").toString());

                            long timeRemaining = timeLength;
                            endAt = System.currentTimeMillis() + timeRemaining;
                            startAt = System.currentTimeMillis();
                        }
                        if (System.currentTimeMillis() < endAt) {
                            Effect effect = getEm().findByID(id);
                            if (effect != null) {
                                continue;
                            }
                            Effect eff = new Effect(id, startAt, endAt, param);
                            em.effect(eff, true);
                            em.add(eff);
                        }
                    } catch (Exception e) {
                        Log.error("err: " + e.getMessage(), e);
                    }
                }
                JSONArray friends = (JSONArray) JSONValue.parse(data.getString("friends"));
                int size = friends.size();
                this.friends = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    Friend fr = new Friend((JSONObject) friends.get(i));
                    this.friends.put(fr.name, fr);
                }
                JSONArray mail = (JSONArray) JSONValue.parse(data.getString("mail"));
                if (mail != null) {
                    size2 = mail.size();
                    for (int i = 0; i < size2; i++) {
                        try {
                            JSONObject obj = (JSONObject) mail.get(i);
                            Mail mail1 = new Mail(obj);
                            if (mail1.time > (System.currentTimeMillis() / 1000)) {
                                mailManager.add(mail1);
                            }
                        } catch (Exception e) {
                            Log.error("err: " + e.getMessage(), e);
                        }
                    }
                }
                skills = new Vector();
                jArr = (JSONArray) JSONValue.parse(data.getString("skill"));
                if (jArr != null) {
                    len = jArr.size();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = (JSONObject) jArr.get(i);
                        Skill skill = new Skill(obj);
                        if (skill.isSkillTemplate == 1 || skill.isSkillTemplate == 7 || skill.isSkillTemplate == 13 || skill.isSkillTemplate == 19 || skill.isSkillTemplate == 25) {
                            skill = SkillFactory.getInstance().getSkill(skill.isSkillTemplate, 1);
                        }
                        skills.add(skill);
                    }
                    if (skills.isEmpty()) {
                        skills = SkillFactory.getInstance().getSkillByClass();
                    }
                }
                jArr = (JSONArray) JSONValue.parse(data.getString("rewards"));
                if (jArr != null) {
                    len = jArr.size();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = (JSONObject) jArr.get(i);
                        com.vdtt.model.Reward rw = new com.vdtt.model.Reward();
                        rw.fromJson(obj);
                        if (rw != null) {
                            rewardList.add(rw);
                        }
                    }

                }
                JSONObject json = (JSONObject) JSONValue.parse(data.getString("data"));
                ParseData parse = new ParseData(json);
                exp = parse.getLong("exp");
                pointPhamChat = parse.getInt("pointPhamChat");
                pointEnthusiastic = parse.getInt("pointEnthusiastic");
                pointEnthusiasticWeek = parse.getInt("pointEnthusiasticWeek");
                pointUpgrageStar = parse.getInt("pointUpgrageStar");
                pointUpgrageStarWeek = parse.getInt("pointUpgrageStarWeek");
                pointKame = parse.getInt("pointKame");
                pointSuper = parse.getInt("pointSuper");
                if (parse.containsKey("lvPK")) {
                    lvPK = (byte) parse.getInt("lvPK");
                }
                if (parse.containsKey("speed")) {
                    speed = (short) parse.getInt("speed");
                }
                if (parse.containsKey("levelBean")) {
                    levelBean = (byte) parse.getInt("levelBean");
                }
                if (parse.containsKey("timeReciveBean")) {
                    timeReciveBean = parse.getInt("timeReciveBean");
                }
                if (parse.containsKey("zeniLock")) {
                    zeniLock = parse.getInt("zeniLock");
                }
                if (parse.containsKey("sachChienDau")) {
                    sachChienDau = parse.getByte("sachChienDau");
                }
                if (parse.containsKey("timeTienIch")) {
                    timeTienIch = parse.getInt("timeTienIch");
                }
                if (parse.containsKey("rank")) {
                    rank = parse.getByte("rank");
                    if (timeTienIch >= 0 && (long) ((long) timeTienIch * 1000) < System.currentTimeMillis()) {
                        rank = 0;
                    }
                }
                if (parse.containsKey("nhanMocNap")) {
                    nhanMocNap = parse.getByte("nhanMocNap");
                }
                if (parse.containsKey("mocNap")) {
                    mocNap = parse.getInt("mocNap");
                }
                if (parse.containsKey("tieuNgay")) {
                    tieuNgay = parse.getInt("tieuNgay");
                }
                if (parse.containsKey("tieuTuan")) {
                    tieuTuan = parse.getInt("tieuTuan");
                }
                if (parse.containsKey("timeUpgradeBean")) {
                    timeUpgradeBean = parse.getLong("timeUpgradeBean");
                }
                if (parse.containsKey("totalTimeOnline")) {
                    totalTimeOnline = parse.getLong("totalTimeOnline");
                }
                if (parse.containsKey("timeOnline")) {
                    timeOnline = parse.getInt("timeOnline");
                }
                if (parse.containsKey("totalKiNang")) {
                    totalKiNang = parse.getByte("totalKiNang");
                }
                if (parse.containsKey("maxBean")) {
                    maxBean = parse.getByte("maxBean");
                }
                if (parse.containsKey("totalTA")) {
                    totalTA = parse.getShort("totalTA");
                }
                if (parse.containsKey("totalTL")) {
                    totalTL = parse.getShort("totalTL");
                }
                if (parse.containsKey("totalSQ")) {
                    totalSQ = parse.getShort("totalSQ");
                }
                if (parse.containsKey("ngayOnline")) {
                    ngayOnline = parse.getByte("ngayOnline");
                }
                if (parse.containsKey("napNgay")) {
                    napNgay = parse.getInt("napNgay");
                }
                if (parse.containsKey("packOnline")) {
                    packOnline = parse.getBoolean("packOnline");
                }
                if (parse.containsKey("packTinhAnh")) {
                    packTinhAnh = parse.getBoolean("packTinhAnh");
                }
                if (parse.containsKey("packThuLinh")) {
                    packThuLinh = parse.getBoolean("packThuLinh");
                }
                if (parse.containsKey("packSieuQuai")) {
                    packSieuQuai = parse.getBoolean("packSieuQuai");
                }
                if (parse.containsKey("countVongQuay")) {
                    countVongQuay = parse.getShort("countVongQuay");
                }
                if (parse.containsKey("countTui1")) {
                    countTui1 = parse.getShort("countTui1");
                }
                if (parse.containsKey("countTui2")) {
                    countTui2 = parse.getShort("countTui2");
                }
                if (parse.containsKey("countTui3")) {
                    countTui3 = parse.getShort("countTui3");
                }
                if (parse.containsKey("timeZoom")) {
                    timeZoom = parse.getLong("timeZoom");
                }
                if (parse.containsKey("countQuaSinhLuc")) {
                    countQuaSinhLuc = parse.getShort("countQuaSinhLuc");
                }
                if (parse.containsKey("sach")) {
                    JSONObject obj = (JSONObject) JSONValue.parse(parse.getString("sach"));
                    sach = new Item(obj);
                }
                if (parse.containsKey("timeOutClan")) {
                    this.timeOutClan = parse.getLong("timeOutClan");
                }
                if (parse.containsKey("taskPet")) {
                    taskPet = parse.getBoolean("taskPet");
                }
                if (parse.containsKey("pointHell")) {
                    pointHell = parse.getInt("pointHell");
                }
                if (parse.containsKey("increaseExtra")) {
                    increaseExtra = parse.getShort("increaseExtra");
                }
                if (parse.containsKey("lockEXP")) {
                    lockEXP = parse.getBoolean("lockEXP");
                }
                if (parse.containsKey("countTuiDo")) {
                    countTuiDo = parse.getByte("countTuiDo");
                }
                if (parse.containsKey("countHell")) {
                    countHell = parse.getByte("countHell");
                }
                if (parse.containsKey("countBarrack")) {
                    countBarrack = parse.getByte("countBarrack");
                } else {
                    countBarrack = 1;
                }
                if (parse.containsKey("countUseBarrackCard")) {
                    countUseBarrackCard = parse.getByte("countUseBarrackCard");
                } else {
                    countUseBarrackCard = 5;
                }
                if (parse.containsKey("countTimeRoom")) {
                    countTimeRoom = parse.getByte("countTimeRoom");
                } else {
                    countTimeRoom = 1;
                }
                if (parse.containsKey("countTheHell")) {
                    countTheHell = parse.getByte("countTheHell");
                }
                if (parse.containsKey("timeGiapLuyenTap")) {
                    timeGiapLuyenTap = parse.getLong("timeGiapLuyenTap");
                }
                if (parse.containsKey("countTaskQuyLao")) {
                    countTaskQuyLao = parse.getByte("countTaskQuyLao");
                }
                if (parse.containsKey("countNgucTu")) {
                    countNgucTu = parse.getByte("countNgucTu");
                }
                if (parse.containsKey("countUseItemNgucTu")) {
                    countUseItemNgucTu = parse.getByte("countUseItemNgucTu");
                }
                if (parse.containsKey("diemBTTBangHoa")) {
                    diemBTTBangHoa = parse.getByte("diemBTTBangHoa");
                }
                if (parse.containsKey("TimeChatColor")) {
                    TimeChatColor = parse.getInt("TimeChatColor");
                }
                if (parse.containsKey("timeChangeSkin")) {
                    timeChangeSkin = parse.getInt("timeChangeSkin");
                }
                if (parse.containsKey("timeWaitChangeSkin")) {
                    timeWaitChangeSkin = parse.getInt("timeWaitChangeSkin");
                }
                if (parse.containsKey("MaBaoVe")) {
                    MaBaoVe = parse.getString("MaBaoVe");
                }
                if (parse.containsKey("TimeReset")) {
                    TimeReset = parse.getInt("TimeReset");
                }
                if (parse.containsKey("resetTimeTichLuy")) {
                    resetTimeTichLuy = parse.getLong("resetTimeTichLuy");
                }
                if (parse.containsKey("taskOrder")) {
                    ParseData task = parse.getParseData("taskOrder");
                    if (task != null) {
                        this.taskOrders = new TaskOrder(this, task.getByte("taskId"), task.getInt("count"),
                                task.getInt("maxCount"), task.getInt("killId"), task.getInt("mapId"), task.getBoolean("failed"));
                    }

                }
                if (parse.containsKey("isClearCT")) {
                    this.resetRewarNap = parse.getBoolean("isClearCT");
                }
                if (parse.containsKey("itemQuaySo")) {
                    JSONObject obj = (JSONObject) JSONValue.parse(parse.getString("itemQuaySo"));
                    int id = Integer.parseInt(obj.get("id").toString());
                    int quantity = Integer.parseInt(obj.get("quantity").toString());
                    int iditem = Integer.parseInt(obj.get("iditem").toString());
                    byte type = Byte.parseByte(obj.get("type").toString());
                    itemQuaySo = ItemQuaySo.builder().id(id).quantity(quantity).idItem(iditem).type(type).build();
                }
                achievements = new PlayerAchievementList();
                achievements.setOwner(this);
                if (parse.containsKey("achievements")) {
                    String jAchievements = parse.getString("achievements");
                    Gson gson = new Gson();
                    Type listType = TypeToken.getParameterized(List.class, PlayerAchievement.class).getType();
                    List<PlayerAchievement> achievementList = gson.fromJson(jAchievements, listType);
                    achievements.addAll(achievementList);
                }
                achievements.synchronizeWithTemplates();
                jArr = (JSONArray) JSONValue.parse(data.getString("title"));
                if (jArr != null) {
                    len = jArr.size();
                    danhHieu = new ArrayList<>();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = (JSONObject) jArr.get(i);
                        DanhHieu dh = new DanhHieu(obj);
                        danhHieu.add(dh);
                    }
                }
                taskId = data.getShort("taskId");
                String tt = data.getString("task");
                if (tt != null && !tt.isEmpty()) {
                    ParseData pd = new ParseData((JSONObject) JSONValue.parse(tt));
                    short taskID = pd.getShort("id");
                    byte taskIndex = pd.getByte("index");
                    short taskCount = pd.getShort("count");
                    TaskTemplate task = Task.getTaskTemplate(taskID);
                    if (task != null) {
                        taskMain = TaskFactory.getInstance().createTask(taskID, taskIndex, taskCount);
                    }
                }
                if (taskMain == null && taskId == 0) {
                    taskMain = TaskFactory.getInstance().createTask((short) 0, (byte) 0, (short) 0);
                }
                jArr = (JSONArray) JSONValue.parse(data.getString("map"));
                if (jArr != null) {
                    mapId = ((Long) jArr.get(0)).shortValue();
                    x = ((Long) jArr.get(1)).shortValue();
                    y = ((Long) jArr.get(2)).shortValue();
                    if (mapId == 39) { // xu ly login neu map la black ball star
                        mapId = 86;
                        x = 1430;
                        y = 430;
                    }
                }
                pointMakeCake = data.getInt("pointMakeCake");
                pointThaLongDen = data.getInt("pointThaLongDen");
                setAbilityStrategy(new AbilityFromEquip());
                if (EventHandler.isEvent()) {
                    DbManager.getInstance().loadEventPoint(this);
                }
                doCheckAndResetNewDay();
            }
        } catch (Exception e) {
            Log.error("load char: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public void doCheckAndResetNewDay() {
        Timestamp timestamp = DbManager.getInstance().getLastResetNewDay(id);
        boolean isNewDay = Util.isNewDay(timestamp);
        if (isNewDay) {
            resetNewDay();
            DbManager.getInstance().saveResetNewDay(id, System.currentTimeMillis());
        }

        Timestamp timestampweek = DbManager.getInstance().getLastResetNewWeek(id);
        boolean isNewWeek = Util.isNewWeek(timestampweek);
        if (isNewWeek) {
            resetNewWeek();
            DbManager.getInstance().saveResetNewWeek(id, System.currentTimeMillis());
        }

        if (resetTimeTichLuy == 0) {
            resetTimeTichLuy = Config.getInstance().getTimeResetMoc();
        }

        if (Config.getInstance().getTimeResetMoc() != resetTimeTichLuy) {
            mocNap = 0;
            resetTimeTichLuy = Config.getInstance().getTimeResetMoc();
            com.vdtt.model.Reward rwtl = findReward(21);
            if (rwtl != null) {
                rwtl.list.clear();
            }
        }

    }

    private void resetNewWeek() {
        this.tieuTuan = 0;

        com.vdtt.model.Reward rw = findReward(8);
        if (rw != null) {
            rw.list.clear();
        }
    }

    private void resetNewDay() {
        this.taskOrders = null;
        this.countTaskQuyLao = 40;
        this.countNgucTu = 2;
        this.countUseItemNgucTu = 0;
        this.timeOnline = 0;
        this.ngayOnline++;
        this.napNgay = 0;
        this.countQuaSinhLuc = 0;
        this.countTuiDo = 0;
        this.countHell = 1;
        this.countTimeRoom = 1;
        this.countBarrack = 1;
        this.countUseBarrackCard = 5;
        this.countTheHell = 0;
        this.tieuNgay = 0;
        if (this.ngayOnline > 7) {
            this.ngayOnline = 1;
            com.vdtt.model.Reward rw = findReward(1);
            if (rw != null) {
                rw.list.clear();
            }
        }
        com.vdtt.model.Reward rw2 = findReward(5);
        if (rw2 != null) {
            rw2.list.clear();
        }
        com.vdtt.model.Reward rw = findReward(0);
        if (rw != null) {
            rw.list.clear();
        }

        com.vdtt.model.Reward rw3 = findReward(6);
        if (rw3 != null) {
            rw3.list.clear();
        }
    }

    public void menu(Message ms) {
        try {
            short npcTemplateId = ms.reader().readShort();
            byte menuId = ms.reader().readByte();
            byte optionId = 0;
            int npcId = 0;
            if (ms.reader().available() > 0) {
                optionId = ms.reader().readByte();
            }
            if (npcTemplateId != 0) {
                Npc npc = zone.getNpc(npcTemplateId);
                if (npc == null) {
                    service.serverDialog("Không tìm thấy NPC này");
                    return;
                }
                npcId = npc.id;
            } else {
                if (menus.isEmpty()) {
                    return;
                }
            }
            Menu menuNew = null;
            if (menus.isEmpty()) {
                initMenu(npcId, npcTemplateId);
            }
            if (!menus.isEmpty() && menuId < menus.size()) {
                menuNew = menus.get(menuId);
                menus.clear();
            }
            if (menuNew != null) {
                menu(menuNew, npcTemplateId);
            }
            Log.debug("menu: " + npcTemplateId + " menuId: " + menuId + " optionId: " + optionId);
        } catch (Exception e) {
            Log.error("err: " + e.getMessage(), e);
        }
    }

    private void menu(Menu menu, int npc) {
        switch (menu.getId()) {
            case CMDMenu.EXECUTE:
                menu.confirm();
                break;
        }
    }

    public void openMenu(Message ms) {
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            short npcId = ms.reader().readShort();
            Npc npc = zone.getNpc(npcId);
            if (npc == null) {
                service.serverDialog("Không tìm thấy NPC này");
                return;
            }
            initMenu(npc.id, npcId);
            if (menus.size() > 0) {
                getService().openUIMenu(npcId);
            } else {
                getService().menu(npcId);
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    private void initMenu(int npcTemplateId, int identifer) {
        try {
            menus.clear();
            Log.debug("init menu: " + npcTemplateId);
            if (npcTemplateId == getTaskNpcId()) {
                if (taskMain == null) {
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nhận nhiệm vụ", () -> {
                        takingTask();
                    }));
                    return;
                }
                if ((taskId == 6 && taskMain.index == 0)
                        || (taskId == 7 && taskMain.index == 0)
                        || (taskId == 8 && (taskMain.index == 0 || taskMain.index == 1))
                        || (taskId == 10 && taskMain.index == 0)
                        || (taskId == 19 && taskMain.index == 0)
                        || (taskId == 25 && taskMain.index == 0)
                        || (taskId == 31 && taskMain.index == 0)
                        || (taskId == 37 && taskMain.index == 0)) {
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nói chuyện", () -> getService().sendTaskStep(0)));
                    return;
                }
                if (taskId == 8 && taskMain.index == 2) {
                    menus.add(new Menu(CMDMenu.EXECUTE, "Về nhà thôi", () -> {
                        Mob mob = new Mob();
                        mob.idEntity = zone.getMonsters().size();
                        mob.id = 227;
                        mob.nameCall = name;
                        mob.hp = mob.hpFull = 1000;
                        mob.x = x;
                        mob.y = y;
                        mob.level = level();
                        mob.status = 2;
                        mob.levelBoss = 0;
                        mob.zone = zone;
                        mob.timeRemove = System.currentTimeMillis() + 200000;
                        zone.addMob(mob);
                        long timeStart = System.currentTimeMillis() + 180000;
                        int i = 0;
                        while (timeStart > System.currentTimeMillis() && !isCleaned) {
                            try {
                                Thread.sleep(3000l);
                            } catch (InterruptedException e) {
                            }
                            String[] segments = taskMain.vStep.get(taskMain.index).strItem.split("\\{");
                            String segment = segments[i].trim().replace("}", "");
                            if (!segment.isEmpty()) {
                                String[] xy = segment.split(",");
                                short x = Short.parseShort(xy[0]);
                                short y = Short.parseShort(xy[1]);
                                mob.setXY(x, y);
                                zone.getService().mobMove(mob.idEntity, x, y);
                            }
                            i++;
                            if (i >= segments.length) {
                                updateTaskCount(1);
                                zone.removeMonster(mob);
                                return;
                            }
                        }
                        zone.removeMonster(mob);
                    }));
                    return;
                }
            }
            switch (npcTemplateId) {
                case 27:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi áo thần linh", () -> {
                        if (getSlotNull() < 1) {
                            warningBagFull();
                            return;
                        }
                        IngredientContainer ingredientContainer = new IngredientContainer();
                        ingredientContainer.addIngredient(new Ingredient(354, 1500));
                        ingredientContainer.addIngredient(new Zeni(5000));
                        if (ingredientContainer.validate(this, 1)) {
                            ingredientContainer.deductAmounts(this, 1);

                            Item item = new Item(923, "doi do");
                            item.addOptionRandom(338, 250, 300);
                            item.addOptionRandom(335, 750, 1000);
                            item.addOptionRandom(342, 25, 50);
                            item.addOption(414, 1);
                            item.addOption(424, 2);
                            addItemToBag(item);
                            getService().addItem(item);
                        }
                    }));

                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi găng thần linh", () -> {
                        if (getSlotNull() < 1) {
                            warningBagFull();
                            return;
                        }
                        IngredientContainer ingredientContainer = new IngredientContainer();
                        ingredientContainer.addIngredient(new Ingredient(562, 1500));
                        ingredientContainer.addIngredient(new Zeni(5000));
                        if (ingredientContainer.validate(this, 1)) {
                            ingredientContainer.deductAmounts(this, 1);

                            Item item = new Item(924, "doi do");
                            item.addOptionRandom(337, 250, 300);
                            item.addOptionRandom(336, 750, 1000);
                            item.addOptionRandom(342, 25, 50);
                            item.addOption(414, 1);
                            item.addOption(420, 2);
                            addItemToBag(item);
                            getService().addItem(item);
                        }
                    }));

                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi quần thần linh", () -> {
                        if (getSlotNull() < 1) {
                            warningBagFull();
                            return;
                        }
                        IngredientContainer ingredientContainer = new IngredientContainer();
                        ingredientContainer.addIngredient(new Ingredient(564, 1500));
                        ingredientContainer.addIngredient(new Zeni(5000));
                        if (ingredientContainer.validate(this, 1)) {
                            ingredientContainer.deductAmounts(this, 1);

                            Item item = new Item(925, "doi do");
                            item.addOptionRandom(339, 250, 300);
                            item.addOptionRandom(335, 750, 1000);
                            item.addOptionRandom(342, 25, 50);
                            item.addOption(414, 1);
                            item.addOption(422, 2);
                            addItemToBag(item);
                            getService().addItem(item);
                        }
                    }));

                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi giày thần linh", () -> {
                        if (getSlotNull() < 1) {
                            warningBagFull();
                            return;
                        }
                        IngredientContainer ingredientContainer = new IngredientContainer();
                        ingredientContainer.addIngredient(new Ingredient(566, 1500));
                        ingredientContainer.addIngredient(new Zeni(5000));
                        if (ingredientContainer.validate(this, 1)) {
                            ingredientContainer.deductAmounts(this, 1);

                            Item item = new Item(926, "doi do");
                            item.addOptionRandom(341, 250, 300);
                            item.addOptionRandom(335, 750, 1000);
                            item.addOptionRandom(342, 25, 50);
                            item.addOption(414, 1);
                            item.addOption(421, 2);
                            addItemToBag(item);
                            getService().addItem(item);
                        }
                    }));
                  
//                        menus.add(new Menu(CMDMenu.EXECUTE, "Đổi áo Hủy Diệt", () -> {
//                        if (getSlotNull() < 1) {
//                            warningBagFull();
//                            return;
//                        }
//                        IngredientContainer ingredientContainer = new IngredientContainer();
//                        ingredientContainer.addIngredient(new Ingredient(354, 5000));
//                        ingredientContainer.addIngredient(new Zeni(10000));
//                        if (ingredientContainer.validate(this, 1)) {
//                            ingredientContainer.deductAmounts(this, 1);
//
//                            Item item = new Item(1159, "doi do");
//                            item.addOptionRandom(338, 500, 800);
//                            item.addOptionRandom(335, 900, 1550);
//                            item.addOptionRandom(342, 50, 120);
//                            item.addOption(414, 3);
//                            item.addOption(424, 5);
//                            item.addOption(350, 2);
//                             item.addOption(347, 20);
//                             item.addOption(27, 20);
//                              item.addOption(273, 205);
//                              item.addOption(351, 104);
//                              item.addOption(353, 18);
//                              item.addOption(357, 7);
//                              item.addOption(358, 5);
//                              
//                            addItemToBag(item);
//                            getService().addItem(item);
//                        }
//                    }));
//
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi găng Hủy Diệt", () -> {
//                        if (getSlotNull() < 1) {
//                            warningBagFull();
//                            return;
//                        }
//                        IngredientContainer ingredientContainer = new IngredientContainer();
//                        ingredientContainer.addIngredient(new Ingredient(562, 5000));
//                        ingredientContainer.addIngredient(new Zeni(10000));
//                        if (ingredientContainer.validate(this, 1)) {
//                            ingredientContainer.deductAmounts(this, 1);
//
//                            Item item = new Item(1160, "doi do");
//                            item.addOptionRandom(337, 350, 600);
//                            item.addOptionRandom(336, 850, 1200);
//                            item.addOptionRandom(342, 25, 70);
//                            item.addOption(414, 1);
//                            item.addOption(420, 2);
//                            
//                            
//                            
//                            
//                            
//                            
//                            
//                            
//                            
//                            
//                            
//                            
//                            addItemToBag(item);
//                            getService().addItem(item);
//                        }
//                    }));
//
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi quần Hủy Diệt", () -> {
//                        if (getSlotNull() < 1) {
//                            warningBagFull();
//                            return;
//                        }
//                        IngredientContainer ingredientContainer = new IngredientContainer();
//                        ingredientContainer.addIngredient(new Ingredient(564, 5000));
//                        ingredientContainer.addIngredient(new Zeni(10000));
//                        if (ingredientContainer.validate(this, 1)) {
//                            ingredientContainer.deductAmounts(this, 1);
//
//                            Item item = new Item(1161, "doi do");
//                            item.addOptionRandom(339, 350, 600);
//                            item.addOptionRandom(335, 850, 1200);
//                            item.addOptionRandom(342, 25, 70);
//                            item.addOption(414, 1);
//                            item.addOption(422, 2);
//                            addItemToBag(item);
//                            getService().addItem(item);
//                        }
//                    }));
//
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi giày Hủy Diệt", () -> {
//                        if (getSlotNull() < 1) {
//                            warningBagFull();
//                            return;
//                        }
//                        IngredientContainer ingredientContainer = new IngredientContainer();
//                        ingredientContainer.addIngredient(new Ingredient(566, 5000));
//                        ingredientContainer.addIngredient(new Zeni(10000));
//                        if (ingredientContainer.validate(this, 1)) {
//                            ingredientContainer.deductAmounts(this, 1);
//
//                            Item item = new Item(1162, "doi do");
//                            item.addOptionRandom(341, 350, 600);
//                            item.addOptionRandom(335, 850, 1200);
//                            item.addOptionRandom(342, 25, 70);
//                            item.addOption(414, 1);
//                            item.addOption(421, 2);
//                            addItemToBag(item);
//                            getService().addItem(item);
//                        }
//                    }));
//                    break;
                    
                    
                case 32:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Giải đấu sức mạnh", () -> {
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Báo Danh", () -> PowerStation.register(this)));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Hủy Báo Danh", () -> PowerStation.cancelRegistration(this)));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Tham Gia Đấu Trường", () -> PowerStation.joinMap(this)));
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Võ đài vũ trụ", () -> {
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Báo Danh", () -> MartialArtsConference.register(this)));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Hủy Báo Danh", () -> MartialArtsConference.cancelRegistration(this)));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Tham Gia Đấu Trường", () -> MartialArtsConference.joinMap(this)));
                        getService().openUIMenu(identifer);
                    }));

                    menus.add(new Menu(CMDMenu.EXECUTE, "Nhiệm vụ hủy diệt vũ trụ", () -> {
                        if (clan == null) {
                            getService().serverDialog("Không có vũ trụ");
                            return;
                        }
                        World world = clan.findWorld(World.DESTRUCTION_UNIVERSE);
                        if (world != null) {
                            this.setXY(90, 336);
                            ZDestructionoftheUniverse zDestructionoftheUniverse = ((ZDestructionoftheUniverse) world);
                            zDestructionoftheUniverse.zones.get(zDestructionoftheUniverse.level).join(this, -1);
                            return;
                        }
                        int cType = clan.getMemberByName(name).getType();
                        if (cType != Clan.TYPE_TOCTRUONG) {
                            getService().serverDialog("Không phải thần hủy diệt không thể mở map");
                            return;
                        }
                        if (clan.countNvHd <= 0) {
                            getService().serverDialog("Hết lượt tham gia");
                            return;
                        }
                        ZDestructionoftheUniverse zDestructionoftheUniverse = new ZDestructionoftheUniverse(0);
                        clan.addWorld(zDestructionoftheUniverse);
                        zDestructionoftheUniverse.joinZone(this);
                    }));

//                    menus.add(new Menu(CMDMenu.EXECUTE, "Dọn rác dưới 12 dòng siêu tốc", () -> {
//                        menus.clear();
//                        menus.add(new Menu(CMDMenu.EXECUTE, "Đồng ý không hối hận", () -> {
//                            for (Item item : bag) {
//                                if (item != null
//                                        && (item.getTemplate().name.toLowerCase().contains("áo") || item.getTemplate().name.toLowerCase().contains("quần") ||
//                                        item.getTemplate().name.toLowerCase().contains("giày") || item.getTemplate().name.toLowerCase().contains("bao tay") ||
//                                        item.getTemplate().name.toLowerCase().contains("đai lưng")) && !item.getTemplate().name.toLowerCase().contains("đệ tử")
//                                        && item.options() != null && item.options().length <= 12) {
//                                    removeItem(item.index, 1, true);
//                                    sellSpeed[item.index] = item;
//                                }
//                            }
//                            serverDialog("Dọn thành công");
//                        }));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "Đéo", () -> {
//                        }));
//                        getService().openUIMenu(identifer);
//                    }));
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Lấy lại đồ bán nhầm (nếu thoát game sẽ mất tất cả)", () -> {
//                        menus.clear();
//                        menus.add(new Menu(CMDMenu.EXECUTE, "Nhận hết", () -> {
//                            for (Item item : sellSpeed) {
//                                if (item != null) {
//                                    addItemToBag(item);
//                                    sellSpeed[item.index] = null;
//                                }
//                            }
//                            serverDialog("Nhận thành công");
//                        }));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "Đéo", () -> {
//                        }));
//                        getService().openUIMenu(identifer);
//                    }));
                    break;
                case 98:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Trận chiến Ngục Tù,Tham gia (còn " + countNgucTu + " lượt)", () -> {
                        if (countNgucTu == 0) {
                            serverDialog("Hết lượt tham gia");
                            return;
                        }
                        if (level() < 25) {
                            serverDialog("Không đủ sức mạnh");
                            return;
                        }
                        countNgucTu--;
                        countAddPointNgucTu = 0;
                        timeNgucTu = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(50);
                        int type = getExpJoinNgucTu();
                        switch (type) {
//                            case 0:
                            case 1:
//                            case 2:
                                joinZone(2, 0, 1);
                                break;
                            case 2:
//                            case 4:
                                joinZone(4, 0, 1);
                                break;
//                            case 5:
                            case 3:
                                joinZone(6, 0, 1);
                                break;
//                            case 7:
                            case 4:
                                joinZone(8, 0, 1);
                                break;
                        }
                        addPointSuper(2);
                        addPointKame(2);
                        addClanPoint(2);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {

                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nhiệm vụ Fu cấp 1,Nhận nhiệm vụ (trừ 1.000 Zeni)", () -> {

                    }));
                    break;
                case 102:
                    if (taskOrders == null) {
                        menus.add(new Menu(CMDMenu.EXECUTE, ":-?Nhiệm vụ học trò quy lão " + (40 - countTaskQuyLao) + "/40", () -> {
                            if (countTaskQuyLao <= 0) {
                                return;
                            }
                            getService().sendTaskQuyLao();
                            this.taskOrders = TaskFactory.getInstance().createTaskOrder((byte) Util.nextInt(0, 4), this);
                            getService().updateTaskOrder(taskOrders);
                            if (taskOrders.taskId == TaskOrder.TASK_MAI_RUA) {
                                Effect eff = new Effect(117, TimeUnit.MINUTES.toMillis(5), 150);
                                getEm().setEffect(eff);
                                setAbility();
                            }
                        }));
                    } else {
                        if (taskOrders.taskId == TaskOrder.TASK_ANSWER) {
                            menus.add(new Menu(CMDMenu.EXECUTE, ":-?Trả lời câu hỏi", () -> {
                                if (taskOrders.failed) {
                                    return;
                                }
                                questionAndAnswer();
                            }));
                        }
                        menus.add(new Menu(CMDMenu.EXECUTE, ":-!Hoàn thành nhiệm vụ", () -> {
                            if (!taskOrders.isComplete()) {
                                return;
                            }
                            if (getSlotNull() < 3) {
                                serverDialog("Hãy để trống 3 ô trong túi");
                                return;
                            }
                            countTaskFailed = 0;
                            if (countTaskQuyLao == 1 || countTaskQuyLao == 11 || countTaskQuyLao == 21 || countTaskQuyLao == 31) {
                                if (getSlotNull() < 5) {
                                    serverDialog("Hãy để trống 5 ô trong túi");
                                    return;
                                }
                                addClanPoint(2);

                                Item bag = new Item(164, "nv quy lao");
                                bag.setLock(true);
                                bag.setQuantity(2);
                                addItemToBag(bag);
                                getService().addItem(bag);

                                Item diamond = new Item(160, "nv quy lao");
                                diamond.setLock(true);
                                diamond.setQuantity(Util.nextInt(5, 10));
                                addItemToBag(diamond);
                                getService().addItem(diamond);
                            }
                            taskOrders = null;
                            addCoin(5000, true);
                            addPointSuper(1);
                            addPointKame(1);

                            Item nts = new Item(838, "nv quy lao");
                            nts.setQuantity(1);
                            nts.setLock(true);
                            addItemToBag(nts);
                            getService().addItem(nts);

                            Item da = new Item(2, "nv quy lao");
                            da.setQuantity(1);
                            da.setLock(true);
                            addItemToBag(da);
                            getService().addItem(da);
                            addClanPoint(1);
                            addPointSoiNoi(10);
                            countTaskQuyLao--;
                            getService().sendTaskQuyLao();
                            getService().resetTaskQuyLao();
                        }));
                    }
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi nhiệm vụ dễ (trừ 6 zeni)", () -> {
                        if (taskOrders == null) {
                            serverDialog("Chưa nhận nhiệm vụ thì làm sao mà đổi bạn ơi");
                            return;
                        }
                        if (zeni < 6) {
                            service.serverDialog("Không đủ zeni");
                            return;
                        }
                        addZeni(-6, true);
                        int[] percent = new int[]{35, 35, 30};
                        byte[] type = new byte[]{0, 3, 4};
                        int index = Util.randomWithRate(percent, 100);
                        this.taskOrders = TaskFactory.getInstance().createTaskOrder(type[index], this);
                        getService().updateTaskOrder(taskOrders);
                        if (taskOrders.taskId == TaskOrder.TASK_MAI_RUA) {
                            Effect eff = new Effect(117, TimeUnit.MINUTES.toMillis(5), 150);
                            getEm().setEffect(eff);
                            setAbility();
                        }
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Khoá sức mạnh(-50 zeni)", () -> {
                        menus.clear();
                        if (!lockEXP) {
                            menus.add(new Menu(CMDMenu.EXECUTE, "Đồng ý", () -> {
                                if (exp < 10_000) {
                                    serverDialog("Chưa đủ sức mạnh, cần 10.000 sức mạnh trở lên");
                                    return;
                                }
                                if (zeni < 50) {
                                    service.serverDialog("Không đủ zeni");
                                    return;
                                }
                                addZeni(-50, true);
                                lockEXP = true;
                                serverMessage("Khoá sức mạnh thành công");
                            }));
                        } else {
                            menus.add(new Menu(CMDMenu.EXECUTE, "Mở khoá", () -> {
                                lockEXP = false;
                                serverMessage("Mở khoá sức mạnh thành công");
                            }));
                        }
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nhiệm vụ đệ tử", () -> {
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Nhận (-100 kim cương)", () -> {
                            if (removeItems(160, 100)) {
                                taskPet = true;
                                service.serverDialog("Nhận nhiệm vụ thành công");
                            } else {
                                service.serverDialog("Không đủ kim cương");
                            }
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    break;
                case 73:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Thử vận may (1000 Zenni)", () -> {
                        itemTVM = new ArrayList<>(ItemManager.getInstance().itemThuVanMayLow);
                        Collections.shuffle(itemTVM);
                        getService().openThuVanMay(itemTVM);
                        typeThuVanMay = 1;
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Thử vận may VIP", () -> {
                        itemTVM = new ArrayList<>(ItemManager.getInstance().itemThuVanMay);
                        Collections.shuffle(itemTVM);
                        getService().openThuVanMay(itemTVM);
                        typeThuVanMay = 0;
                    }));
                    AbsEvent event = EventHandler.getEvent();
                    if (event != null) {
                        event.createMenu(this, identifer, menus);
                    }
//                    if (MartialArtsConference.isRegistering()) {
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Đại hội võ thuật", () -> {
//                        MartialArtsConference.joinMap(this);
//                        menus.clear();
//                        menus.add(new Menu(CMDMenu.EXECUTE, "10.000 Coin = 300 Zeni", () -> exchangeCoinToZeni(10_000, 300)));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "19.999 Coin = 600 Zeni", () -> exchangeCoinToZeni(19_999, 600)));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "50.000 Coin = 1.500 Zeni", () -> exchangeCoinToZeni(50_000, 1_500)));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "100.000 Coin = 3.000 Zeni", () -> exchangeCoinToZeni(100_000, 3_000)));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "200.000 Coin = 6.200 Zeni", () -> exchangeCoinToZeni(200_000, 6_200)));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "500.000 Coin = 16.000 Zeni", () -> exchangeCoinToZeni(500_000, 16_000)));
//                        menus.add(new Menu(CMDMenu.EXECUTE, "1.000.000 Coin = 33.000 Zeni", () -> exchangeCoinToZeni(1_000_000, 33_000)));
//                        getService().openUIMenu(identifer);
//                    }));
//                    }
                    break;
                case 22:
                    if (user.isAdmin) {
                        AdminService.getInstance().openUIAdmin(this, identifer);
                    }
                    menus.add(new Menu(CMDMenu.EXECUTE, "Mở thành viên 100k Coin", () -> {
                        if (getSlotNull() < 5) {
                            warningBagFull();
                            return;
                        }
                        if (user.activated == 0) {
                            serverDialog("Bạn đã mở thành viên");
                            return;
                        }
                        if (user.coin < 100000) {
                            serverDialog("Yêu cầu tối thiểu 100.000 coin, phí kích hoạt 100k Coin.");
                            return;
                        }
                        user.coin -= 100000;
                        DbManager.getInstance().active_account(user.username);
                        DbManager.getInstance().updateCoin(user.username, 100000);
                        user.activated = 0;
//                        Item item = new Item(891, "detuvip199");
//                        item.setQuantity(1);
//                        item.setLock(false);
//                        addItemToBag(item);
//                        getService().addItem(item);
//
//                        // Tạo vật phẩm Rương Thời Trang Đệ Tử
//                        Item item1 = new Item(942, "ruongthoitrangdetu");
//                        item1.setQuantity(1);
//                        item1.setLock(false);
//                        addItemToBag(item1);
//                        getService().addItem(item1);
//                        // Tạo vật phẩm Rương Thời Trang Đệ Tử
//                        Item item2 = new Item(177, "ruongda");
//                        item2.setQuantity(10);
//                        item2.setLock(true);
//                        addItemToBag(item2);
//                        getService().addItem(item2);
//                        addBallz(200000, true);
//                        serverDialog("Tặng Đệ ! Tặng Thêm 200k ballz + 1 Rương Thời Trang Đệ Tử + 10 Rương Đá Vip");
              
                    }));
                    
                    menus.add(new Menu(CMDMenu.EXECUTE, "Số Dư Coin Nạp", () -> {
                        user.coin = DbManager.getInstance().getCoin(user.username);
                        serverDialog("Số Dư Coin Nạp: " + Util.getCurrency(user.coin));
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi Zeni", () -> {
                        menus.clear();
                        boolean onKhuyenMai = Config.getInstance().isKhuyenMai();
                        int finalKhuyenMai = Config.getInstance().getPercentKhuyenMai();
                        if (onKhuyenMai) {
                            menus.add(new Menu(CMDMenu.EXECUTE, "10.000 Coin = 150 Zeni + (Khuyến mãi 10%)", () -> exchangeCoinToZeni(10_000, 150 + (150 * 10 / 100))));
                            menus.add(new Menu(CMDMenu.EXECUTE, "19.999 Coin = 300 Zeni + (Khuyến mãi 20%)", () -> exchangeCoinToZeni(19_999, 300 + (300 * 20 / 100))));
                            menus.add(new Menu(CMDMenu.EXECUTE, "50.000 Coin = 750 Zeni + (Khuyến mãi 30%)", () -> exchangeCoinToZeni(50_000, 750 + (750 * 30 / 100))));
                            menus.add(new Menu(CMDMenu.EXECUTE, "100.000 Coin = 1.500 Zeni + (Khuyến mãi 40%)", () -> exchangeCoinToZeni(100_000, 1_500 + (1_500 * 40 / 100))));
                            menus.add(new Menu(CMDMenu.EXECUTE, "200.000 Coin = 3.200 Zeni + (Khuyến mãi 50%)", () -> exchangeCoinToZeni(200_000, 3_200 + (3_200 * 50 / 100))));
                            menus.add(new Menu(CMDMenu.EXECUTE, "500.000 Coin = 8.000 Zeni + (Khuyến mãi 70%)", () -> exchangeCoinToZeni(500_000, 8_000 + (8_000 * 70 / 100))));
                            menus.add(new Menu(CMDMenu.EXECUTE, "1.000.000 Coin = 17.000 Zeni + (Khuyến mãi 100%)", () -> exchangeCoinToZeni(1_000_000, 17_000 + (17_000 * 100 / 100))));
                        } else {
                            menus.add(new Menu(CMDMenu.EXECUTE, "10.000 Coin = 150 Zeni", () -> exchangeCoinToZeni(10_000, 150)));
                            menus.add(new Menu(CMDMenu.EXECUTE, "19.999 Coin = 300 Zeni", () -> exchangeCoinToZeni(19_999, 300)));
                            menus.add(new Menu(CMDMenu.EXECUTE, "50.000 Coin = 750 Zeni", () -> exchangeCoinToZeni(50_000, 750)));
                            menus.add(new Menu(CMDMenu.EXECUTE, "100.000 Coin = 1.500 Zeni", () -> exchangeCoinToZeni(100_000, 1_500)));
                            menus.add(new Menu(CMDMenu.EXECUTE, "200.000 Coin = 3.200 Zeni", () -> exchangeCoinToZeni(200_000, 3_200)));
                            menus.add(new Menu(CMDMenu.EXECUTE, "500.000 Coin = 8.000 Zeni", () -> exchangeCoinToZeni(500_000, 8_000)));
                            menus.add(new Menu(CMDMenu.EXECUTE, "1.000.000 Coin = 17.000 Zeni", () -> exchangeCoinToZeni(1_000_000, 17_000)));
                        }
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nhập Code", () -> {
                        InputDialog input = new InputDialog(CMDInputDialog.EXECUTE, "Nhập Code:", () -> {
                            getService().resetScreen();
                            GiftCode.getInstance().use(this, this.input.getText());
                        });
                        setInput(input);
                        getService().showInputDialog();
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Mua Gói Tân Thủ[1M Coin]", () -> {
                        if (getSlotNull() < 5) {
                            warningBagFull();
                            return;
                        }
                        if (user.activated == 1) {
                            serverDialog("Bạn Chưa Mở Thành Viên 100k Coin Tức 20k Vnđ Vui Lòng Mở Bên Trên");
                            return;
                        }
                        if (user.coin < 1000000) {
                            serverDialog("Yêu cầu tối thiểu 1000k coin, phí Mua 1000k Coin.Tặng 500k Ballz+ 10k zenni+ 100M xu Và 10 Rương Đá Vip");
                            return;
                        }

                        user.coin -= 1000000;
                        DbManager.getInstance().updateCoin(user.username, 1000000);

//                        // Tạo vật phẩm Đệ Tử VIP
//                        Item item = new Item(891, "detuvip199");
//                        item.setQuantity(1);
//                        item.setLock(false);
//                        addItemToBag(item);
//                        getService().addItem(item);
//
//                        // Tạo vật phẩm Rương Thời Trang Đệ Tử
//                        Item item1 = new Item(942, "ruongthoitrangdetu");
//                        item1.setQuantity(1);
//                        item1.setLock(false);
//                        addItemToBag(item1);
//                        getService().addItem(item1);

                        // Tạo vật phẩm Rương Thời Trang Đệ Tử
                        Item item2 = new Item(177, "ruongda");
                        item2.setQuantity(10);
                        item2.setLock(true);
                        addItemToBag(item2);
                        getService().addItem(item2);

                        // 🎁 Tặng thêm 100.000 Ballz
                        addBallz(500000, true);
                         // 🎁 Tặng thêm 100.000 Ballz
                        addZeni(10000, true);
                        
                          addCoin(100000000, true);

                        serverDialog("Mua Gói Tân thủ Thành Công ! Tặng Thêm 500k ballz + 100m xu +10k zenni+ 10 Rương Đá Vip");
                    }));

                    menus.add(new Menu(CMDMenu.EXECUTE, "Soi  Boss", () -> {
                        List<Boss> bosses = SpawnBossManager.getInstance().getBosses();
                        Collections.reverse(bosses);

                        List<Boss> aliveBosses = bosses.stream()
                                .filter(boss -> !boss.isDead)
                                .limit(10)
                                .toList();
                        StringJoiner joiner = new StringJoiner("\n");
                        aliveBosses.forEach(boss -> joiner.add(boss.getNotifyText()));
                        getService().sendTextNPC(joiner.toString(), "");
                    }));
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi điểm sôi nôi (10 điểm = 1 KC)", () -> {
//                        IngredientContainer ingredientContainer = new IngredientContainer();
//                        ingredientContainer.addIngredient(new Enthusiastic(10));
//
//                        ProductContainer productContainer = new ProductContainer();
//                        productContainer.addProduct(new Product(160, 1, true));
//
//                        Mixer.mix(this, ingredientContainer, productContainer, true);
//                    }));

                     menus.add(new Menu(CMDMenu.EXECUTE, "Đổi điểm sôi nôi (1000 Điểm = 10k Ballz + 1000k xu zenni)", () -> {
                       if (pointEnthusiasticWeek < 1000) {
                            serverDialog("Phải có ít nhất 1000 điểm sôi nổi tuần để đổi Ballz-xu,zenni Báu Vật 1-1000 Zenni");
                            return;
                        }
                         IngredientContainer ingredientContainer = new IngredientContainer();
                         ingredientContainer.addIngredient(new Enthusiastic(500));
                         
                        ProductContainer productContainer = new ProductContainer();
                        productContainer.addProduct(new Product(1156, 1, true));
                          productContainer.addProduct(new Product(1157, 1, true));
                           productContainer.addProduct(new Product(1158, 1, true));
                        Mixer.mix(this, ingredientContainer, productContainer, true);
                    })); 
                    
                    
                    
                    
                    
                    
                    
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi ballz (1000.000 ballz = 1 Thỏi Vàng)", () -> {
                        if (pointEnthusiasticWeek < 1000) {
                            serverDialog("Phải có ít nhất 1000 điểm sôi nổi tuần để đổi thỏi vàng");
                            return;
                        }
                        IngredientContainer ingredientContainer = new IngredientContainer();
                        ingredientContainer.addIngredient(new Ballz(1000000));

                        ProductContainer productContainer = new ProductContainer();
                        productContainer.addProduct(new Product(1019, 1, true));
                        Mixer.mix(this, ingredientContainer, productContainer, true);
                    }));
                  //  menus.add(new Menu(CMDMenu.EXECUTE, "Nhận Quà Loan Tin", () -> FanCung.getInstance().used(this)));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Xoá hành trang", () -> {
                        for (int i = 0; i < bag.length; i++) {
                            bag[i] = null;
                            getService().removeItem((byte) 0, i);
                        }
                        getService().serverMessage("Đã xoá hành trang thành công");
                    }));
                    break;
                case 47:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Rương đồ", () -> service.openBox()));
                    break;
                case 49:
                    int numBean = getNumBean();
                    menus.add(new Menu(CMDMenu.EXECUTE, "Thu hoạch " + numBean + "/" + (levelBean + 5), () -> {
                        if (getSlotNull() < 1) {
                            warningBagFull();
                            return;
                        }
                        if (taskId == 0 && taskMain != null && taskMain.index == 1) {
                            updateTaskCount(1);
                        }
                        if (taskId == 6 && taskMain != null && taskMain.index == 1) {
                            updateTaskCount(1);
                        }
                        if (numBean > 0) {
                            Item item = new Item(724, "dau");
                            item.isLock = true;
                            item.setQuantity(numBean);
                            addItemToBag(item);
                            getService().addItem(item);
                            timeReciveBean = (int) (System.currentTimeMillis() / 1000);
                            getService().sendTimeReciveBean();
                        } else {
                            service.serverDialog("Đã hết lượt nhận đậu");
                        }
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Thu hoạch nhanh (100 zeni)", () -> {
                        if (zeni < 100) {
                            service.warningMessage("Không đủ zeni");
                            return;
                        }
                        if (getSlotNull() < 1) {
                            warningBagFull();
                            return;
                        }
                        addZeni(-1, true);
                        Item item = new Item(724, "dau");
                        item.isLock = true;
                        item.setQuantity(levelBean + 5);
                        addItemToBag(item);
                        getService().addItem(item);
                        timeReciveBean = (int) (System.currentTimeMillis() / 1000);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Thu hoạch thần tốc (100 zeni)", () -> {
                        if (zeni < 100) {
                            service.warningMessage("Không đủ zeni");
                            return;
                        }
                        if (getSlotNull() < 1) {
                            warningBagFull();
                            return;
                        }
                        addZeni(-10, true);
                        Item item = new Item(724, "dau");
                        item.isLock = true;
                        item.setQuantity(99);
                        addItemToBag(item);
                        getService().addItem(item);
                        timeReciveBean = (int) (System.currentTimeMillis() / 1000);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nâng lên cấp " + (levelBean + 2), () -> {
                        if (levelBean >= 12) {
                            service.sendTextNPC("Đã đạt cấp tối đa", "");
                            return;
                        }
                        menus.clear();
                        if (timeUpgradeBean > 0) {
                            menus.add(new Menu(CMDMenu.EXECUTE, "Hủy (Thời gian hoàn thành " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timeUpgradeBean)) + ")", () -> {
                                int price = Config.getInstance().upgradeLevels[levelBean][1];
                                timeUpgradeBean = 0;
                                addCoin(price, true);
                            }));
                        } else {
                            menus.add(new Menu(CMDMenu.EXECUTE, "Xác nhận (" + Config.getInstance().upgradeLevels[levelBean][1] + ") xu", () -> {
                                int price = Config.getInstance().upgradeLevels[levelBean][1];
                                if (coin < price) {
                                    service.warningMessage("Không đủ xu");
                                    return;
                                }
                                if (taskId == 5 && taskMain != null && taskMain.index == 2) {
                                    updateTaskCount(1);
                                }
                                timeUpgradeBean = System.currentTimeMillis() + Config.getInstance().upgradeLevels[levelBean][2];
                                addCoin(-price, true);
                            }));
                            menus.add(new Menu(CMDMenu.EXECUTE, "Hủy", () -> {

                            }));
                        }
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nâng nhanh lên cấp " + (levelBean + 2), () -> {
                        if (levelBean >= 12) {
                            service.sendTextNPC("Đã đạt cấp tối đa", "");
                            return;
                        }
                        if (timeUpgradeBean > 0) {
                            service.warningMessage("Đang nâng cấp");
                            return;
                        }
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Xác nhận (" + Config.getInstance().upgradeLevels[levelBean][0] + ") zeni", () -> {
                            int price = Config.getInstance().upgradeLevels[levelBean][0];
                            if (zeni < price) {
                                service.warningMessage("Không đủ zeni");
                                return;
                            }
                            addZeni(-price, true);
                            levelBean++;
                            getService().sendLevelBean();
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {
                        getService().sendTextNPC("Chỉ có thể đem theo tối đa 24 đậu thần. Nếu lượng đậu thu nhập nhiều hơn số đậu giới hạn thì số đậu dư sẽ mất", "");
                    }));
                    break;
                case 92:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Kỹ năng", () -> {
                        requestItem((byte) 37);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đồ xịn", () -> {
                        requestItem((byte) 38);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Trang phục", () -> {
                        requestItem((byte) 19);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Cải trang", () -> {
                        requestItem((byte) 30);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Thời trang", () -> {
                        requestItem((byte) 35);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Khuyến mãi", () -> {
                        requestItem((byte) 18);
                    }));
                    break;
                    
                    
                case 28:
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Fix lỗi mất item Hợp nhất", this::updateBagandBody));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Ghép đá", () -> {
                        service.openGUI(81);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Ép sao", () -> {
                        service.openGUI(82);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hợp nhất", () -> {
                        service.hopNhatItem();
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đục lỗ khảm", () -> {
                        service.openGUI(102);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Khảm pha lê", () -> {
                        service.openGUI(87);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Tách pha lê", () -> {
                        service.openGUI(90);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Ghép cải trang", () -> {
                        service.openGUI(94);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Tách cải trang", () -> {
                        service.openGUI(95);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Dịch chuyển sao", () -> {
                        service.openGUI(85);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Tách đá ép sao", () -> {
                        service.openGUI(84);
                    }));
                    break;
                case 21:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Lôi đài", () -> {
                        if (user.activated == 2) {
                            serverDialog("Bug Con Cặc Cốc Chết Cụ Mày Gio");
                            return;
                        }
                        if (!isActiveAction()) {
                            serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                            return;
                        }
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Mời đối thủ", () -> {
                            menus.clear();
                            stadium = new Stadium();
                            stadium.register(this);
                        }
                        ));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Tham gia", () -> {
                            if (stadium == null || stadium.match == null) {
                                serverDialog("Bạn không có lời mời nào lôi đài");
                                return;
                            }
                            stadium.joinMap(this);
                        }
                        ));
                        getService().openUIMenu(identifer);
                    }));
                    break;
                case 40:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh ngục tù", () -> {
                        joinZone(54, 0, TypeTau.TAU_TRAI_DAT_VIP);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh God", () -> {
                        joinZone(53, 0, TypeTau.TAU_TRAI_DAT_VIP);

                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh Sao đen", () -> {
                        menus.clear();
                        if (PlanetBlackStarManager.isStart()) {
                            menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh sao đen", () -> {
                                joinZone(39, 0, TypeTau.TAU_TRAI_DAT_VIP);
                            }));
                        }
                        menus.add(new Menu(CMDMenu.EXECUTE, "Gọi rồng thần sao đen", () -> {
                            if (clan == null) {
                                getService().serverDialog("Chưa vào vũ trụ");
                                return;
                            }
                            int cType = clan.getMemberByName(name).getType();
                            if (cType != Clan.TYPE_TOCTRUONG) {
                                getService().serverDialog("Không phải thần hủy diệt không thể gọi rồng thần");
                                return;
                            }
                            boolean[] stars = new boolean[7];
                            StringBuilder missingItems = new StringBuilder("Vũ trụ còn thiếu: ");
                            for (Item item : clan.items) {
                                if (item == null) {
                                    continue;
                                }
                                int itemId = switch (item.getTemplate().id) {
                                    case 917 ->
                                        1;
                                    case 916 ->
                                        2;
                                    case 915 ->
                                        3;
                                    case 914 ->
                                        4;
                                    case 913 ->
                                        5;
                                    case 912 ->
                                        6;
                                    case 911 ->
                                        7;
                                    default ->
                                        -1;
                                };

                                if (itemId != -1) {
                                    stars[itemId - 1] = true;
                                }
                            }
                            boolean allItemsPresent = true;
                            for (boolean star : stars) {
                                if (!star) {
                                    allItemsPresent = false;
                                    break;
                                }
                            }

                            if (allItemsPresent) {
                                for (int i = 0; i < clan.items.length; i++) {
                                    Item item = clan.items[i];
                                    if (item != null) {
                                        int itemId = item.getTemplate().id;
                                        if (itemId >= 911 && itemId <= 917) {
                                            clan.removeItem(i, 1);
                                        }
                                    }
                                }
                                RuntimeServer.gI().uocRongALL(this, (byte) 2);
                            } else {
                                if (!stars[0]) {
                                    missingItems.append("Ngọc rồng đen 1 sao, ");
                                }
                                if (!stars[1]) {
                                    missingItems.append("Ngọc rồng đen 2 sao, ");
                                }
                                if (!stars[2]) {
                                    missingItems.append("Ngọc rồng đen 3 sao, ");
                                }
                                if (!stars[3]) {
                                    missingItems.append("Ngọc rồng đen 4 sao, ");
                                }
                                if (!stars[4]) {
                                    missingItems.append("Ngọc rồng đen 5 sao, ");
                                }
                                if (!stars[5]) {
                                    missingItems.append("Ngọc rồng đen 6 sao, ");
                                }
                                if (!stars[6]) {
                                    missingItems.append("Ngọc rồng đen 7 sao, ");
                                }
                                getService().serverDialog(missingItems.substring(0, missingItems.length() - 2));
                            }
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nhà tù ngân hà", () -> {

                    }));
                    break;
                case 42:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh FIDE", () -> {
                        joinZone(57, 0, TypeTau.TAU_FIDE);
                    }));
                    break;
                case 43:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh Tương lai", () -> {
                        joinZone(55, 0, TypeTau.TAU_TUONG_LAI);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đại chiến MajinBuu", () -> {

                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Truy tìm ngọc rồng namec", () -> {

                    }));
                    break;
                case 44:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh Vampa", () -> {
                        joinZone(88, 0, TypeTau.TAU_XAYDA);
                    }));
                    break;
                case 45:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh Namec", () -> {
                        joinZone(77, 0, TypeTau.TAU_NAMEC);
                    }));
                    break;
                case 46:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hành tinh Yardat", () -> {
                        joinZone(81, 0, TypeTau.TAU_YARDRAT);
                    }));
                    break;
                case 61:
                    if (sys == 5) {
                        menus.add(new Menu(CMDMenu.EXECUTE, "Nói chuyện", () -> getService().npcTalk("Ngươi đã trở thành tộc người Frieze")));
                        return;
                    }
                    Effect eff = getEm().findByID(162);
                    if (eff != null) {
                        menus.add(new Menu(CMDMenu.EXECUTE, "Hoàn thành nhiệm vụ", () -> {
                            if (eff.getParam() >= 8) {
                                for (Item it : body) {
                                    if (it != null) {
                                        serverDialog("Cần phải tháo hết trang bị và đệ tử ra mới có thể hoàn thành nhiệm vụ");
                                        return;
                                    }
                                }
                                for (Item it : body2) {
                                    if (it != null) {
                                        serverDialog("Cần phải tháo hết trang bị và đệ tử ra mới có thể hoàn thành nhiệm vụ");
                                        return;
                                    }
                                }
                                idCharSys = 4;
                                sys = 5;
                                for (int i = 0; i < body.length; i++) {
                                    Item item = body[i];
                                    if (item != null && item.getTemplate().typeChar != 0 && item.getTemplate().typeChar != sys) {
                                        body[i] = null;
                                    }
                                }
                                for (int i = 0; i < body2.length; i++) {
                                    Item item = body2[i];
                                    if (item != null && item.getTemplate().typeChar != 0 && item.getTemplate().typeChar != sys) {
                                        body2[i] = null;
                                    }
                                }
                                Item item = new Item(998, "htnv");
                                item.index = item.getTemplate().type;
                                item.creatItemOptionTrangBi();
                                body[item.index] = item;

                                item = new Item(999, "htnv");
                                item.index = item.getTemplate().type;
                                item.creatItemOptionTrangBi();
                                body[item.index] = item;

                                item = new Item(1000, "htnv");
                                item.index = item.getTemplate().type;
                                item.creatItemOptionTrangBi();
                                body[item.index] = item;

                                item = new Item(1001, "htnv");
                                item.index = item.getTemplate().type;
                                item.creatItemOptionTrangBi();
                                body[item.index] = item;

                                getEm().removeEffect(eff);
                                KI = exp / 10;
                                skills = SkillFactory.getInstance().getSkillByClass();
                                if (taskId > 3) {
                                    Skill skill = SkillFactory.getInstance().getSkill(24, 0);
                                    skills.add(skill);
                                }
                                if (taskId > 9) {
                                    Skill skill = SkillFactory.getInstance().getSkill(27, 0);
                                    skills.add(skill);
                                }
                                if (taskId > 15) {
                                    Skill skill = SkillFactory.getInstance().getSkill(31, 0);
                                    skills.add(skill);
                                }
                                setAbility();
                                getService().sendInfo(user);
                                zone.getService().playerAdd(this);
                                try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE `players` SET `class` = ? WHERE `id` = ? LIMIT 1;")) {
                                    stmt.setInt(1, idCharSys);
                                    stmt.setInt(2, this.id);
                                    stmt.executeUpdate();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                saveData();
                            } else {
                                getService().npcTalk("61@Hiện tại đã đánh bại " + eff.getParam() + "/8 Fide ở địa ngục hoặc 1 Golden Fide thì mới có thể hoàn thành nhiệm vụ.");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Hoàn thành nhanh (200 zeni)", () -> {
                            if (zeni < 200) {
                                service.warningMessage("Không đủ zeni");
                                return;
                            }
                            if (eff.getParam() >= 8) {
                                serverDialog("Nhiệm vụ đã hoàn thành");
                                return;
                            }
                            addZeni(-200, true);
                            eff.param = 8;
                            zone.getService().playerAddEffect(this, eff);
                        }));
                    } else {
                        menus.add(new Menu(CMDMenu.EXECUTE, "Nhận nhiệm vụ fide", () -> {
                            if (exp > 815_000_000) {
                                service.serverDialog("Yêu cầu sức mạnh nhỏ hơn 815tr");
                                return;
                            }
                            getService().npcTalk(StringMenu.TASK_FIDE);
                            Effect effect = new Effect(162, TimeUnit.DAYS.toMillis(10), 0);
                            getEm().setEffect(effect);
                        }));
                    }
                    break;
                case 59:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Phòng thời gian (110 zeni)", () -> {
//                        LocalTime currentTime = LocalTime.now();
//                        LocalTime startTime = LocalTime.of(1, 0);
//                        LocalTime endTime = LocalTime.of(23, 0);

//                        if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                        TimeRoom.join(this);
//                        } else {
//                            System.out.println("Phòng thời gian chỉ mở từ 1h sáng đến 23h tối.");
//                        }
                    }));
//                    menus.add(new Menu(CMDMenu.EXECUTE, "Phòng thời gian (220 zeni)", () -> {
//                        if (timeZoom > System.currentTimeMillis()) {
//                            TimeZoom zoom = TimeZoom.getZoom(level(), (byte) 0);
//                            if (zoom != null) {
//                                zoom.joinZone(this);
//                            } else {
//                                TimeZoom zoom1 = new TimeZoom(level(), (byte) 0);
//                                addWorld(zoom1);
//                                zoom1.joinZone(this);
//                                TimeZoom.addZoom(zoom1);
//                            }
//                            return;
//                        }
//                        if (zeni < 220) {
//                            service.warningMessage("Không đủ zeni");
//                            return;
//                        }
//                        addZeni(-220, true);
//                        timeZoom = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(10);
//                        TimeZoom zoom1 = new TimeZoom(level(), (byte) 0);
//                        addWorld(zoom1);
//                        zoom1.joinZone(this);
//                        TimeZoom.addZoom(zoom1);
//                        Effect effect = new Effect(116, TimeUnit.HOURS.toMillis(10), 0);
//                        getEm().setEffect(effect);
//                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Địa ngục", () -> {
//                        serverDialog("Địa ngục đang chờ update !!!! Nếu nhiệm vụ tới đây thì hãy đi cày tiếp.");
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Tham gia (còn " + countHell + " lượt)", () -> {
                            if (countHell < 1) {
                                service.serverDialog("Hết lượt tham gia");
                                return;
                            }
                            if (exp < 17850) {
                                service.serverDialog("Cần ít nhất 17.850 sức mạnh để tham gia");
                                return;
                            }
                            if (getGroup() == null) {
                                countHell--;
                                Hell hell = new Hell(getTypeSucManh());
                                addWorld(hell);
                                addClanPoint(2);
                                hell.joinZone(this);
                                achievements.increaseAchievementCount(0, 1);
                            } else {
                                if (this.group.getIndexById(this.id) != 0) {
                                    serverDialog("Phải là trưởng nhóm mới có thể mở phó bản");
                                } else {
                                    List<Char> chars = this.getGroup().getCharsInZone(zone.map.id, zone.id);
                                    if (chars.size() < getGroup().getNumberMember()) {
                                        serverDialog("Vui lòng tập hợp đủ thành viên trước khi tham gia");
                                        return;
                                    }
                                    for (Char _char : chars) {
                                        if (_char != null && !_char.isCleaned && _char != this && !_char.isDead) {
                                            if (_char.countHell < 1) {
                                                serverDialog("Người chơi " + _char.name + " không còn lượt tham gia");
                                                return;
                                            }
                                            if (_char.getTypeSucManh() != getTypeSucManh()) {
                                                serverDialog("Người chơi " + _char.name + " chênh lệch sức mạnh quá lớn");
                                                return;
                                            }
                                        }
                                    }
                                    Hell hell = new Hell(getTypeSucManh());
                                    for (Char _char : chars) {
                                        try {
                                            if (_char != null && !_char.isCleaned && !_char.isDead) {
                                                _char.countHell--;
                                                _char.achievements.increaseAchievementCount(0, 1);
                                                _char.addWorld(hell);
                                                _char.addClanPoint(2);
                                                hell.joinZone(_char);
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Thưởng tích luỹ (" + pointHell + " điểm)", () -> {
                            if (pointHell == 0) {
                                service.serverDialog("Không có điểm thưởng");
                                return;
                            }
                            addCoin(pointHell * 5000, true);
                            if (pointHell > 200) {
                                Item item = new Item(697, "hell");
                                item.setQuantity(pointHell / 200);
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            }
                            pointHell = 0;
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {

                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi ngọc rồng trái đất", () -> {
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 1 sao", () -> {
                            if (ballZ < 100000) {
                                getService().sendTextNPC("Cần 20.000 Ballz, 200 ngọc thô sơ đổi được ngọc rồng 2 sao", "");
                                return;
                            }
                            if (ballZ > 100000 && removeItems(838, 100)) {
                                addBallz(-100000, true);
                                Item item = new Item(839, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 100.000 Ballz, 100 ngọc thô sơ đổi được ngọc rồng 1 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 2 sao", () -> {
                            if (ballZ < 200000) {
                                getService().sendTextNPC("Cần 200.000 Ballz, 200 ngọc thô sơ đổi được ngọc rồng 2 sao", "");
                                return;
                            }
                            if (removeItems(838, 200)) {
                                addBallz(-200000, true);
                                Item item = new Item(840, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 200.000 Ballz, 200 ngọc thô sơ đổi được ngọc rồng 2 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 3 sao", () -> {
                            if (ballZ < 300000) {
                                getService().sendTextNPC("Cần 30.000 Ballz, 300 ngọc thô sơ đổi được ngọc rồng 3 sao", "");
                                return;
                            }
                            if (removeItems(838, 300) && ballZ > 300000) {
                                addBallz(-300000, true);
                                Item item = new Item(841, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 300.000 Ballz, 300 ngọc thô sơ đổi được ngọc rồng 3 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 4 sao", () -> {
                            if (ballZ < 400000) {
                                getService().sendTextNPC("Cần 400.000 Ballz, 400 ngọc thô sơ đổi được ngọc rồng 4 sao", "");
                                return;
                            }
                            if (removeItems(838, 400) && ballZ > 400000) {
                                addBallz(-400000, true);
                                Item item = new Item(842, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 40.000 Ballz, 400 ngọc thô sơ đổi được ngọc rồng 4 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 5 sao", () -> {
                            if (ballZ < 500000) {
                                getService().sendTextNPC("Cần 500.000 Ballz, 500 ngọc thô sơ đổi được ngọc rồng 5 sao", "");
                                return;
                            }
                            if (removeItems(838, 500) && ballZ > 500000) {
                                addBallz(-500000, true);
                                Item item = new Item(843, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 50.000 Ballz, 500 ngọc thô sơ đổi được ngọc rồng 5 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 6 sao", () -> {
                            if (ballZ < 600000) {
                                getService().sendTextNPC("Cần 60.000 Ballz, 600 ngọc thô sơ đổi được ngọc rồng 6 sao", "");
                                return;
                            }
                            if (removeItems(838, 600) && ballZ > 600000) {
                                addBallz(-600000, true);
                                Item item = new Item(844, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 600.000 Ballz, 600 ngọc thô sơ đổi được ngọc rồng 6 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 7 sao", () -> {
                            if (ballZ < 700000) {
                                getService().sendTextNPC("Cần 70.000 Ballz, 700 ngọc thô sơ đổi được ngọc rồng 7 sao", "");
                                return;
                            }
                            if (removeItems(838, 700) && ballZ > 700000) {
                                addBallz(-700000, true);
                                Item item = new Item(845, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 700.000 Ballz, 700 ngọc thô sơ đổi được ngọc rồng 7 sao", "");
                            }
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi ngọc sao đen", () -> {

                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi ngọc rồng Cerealian", () -> {
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng Cerealian 1 sao", () -> {
                            if (ballZ < 100000) {
                                getService().sendTextNPC("Cần 100.000 Ballz, 100 ngọc thô sơ", "");
                                return;
                            }
                            if (removeItems(838, 100) && ballZ > 100000) {
                                addBallz(-100000, true);
                                Item item = new Item(1045, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 100.000 Ballz, 100 ngọc thô sơ", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng Cerealian 2 sao", () -> {
                            if (ballZ < 200000) {
                                getService().sendTextNPC("Cần 200.000 Ballz, 200 ngọc thô sơ", "");
                                return;
                            }
                            if (removeItems(838, 200) && ballZ > 200000) {
                                addBallz(-200000, true);
                                Item item = new Item(1046, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 200.000 Ballz, 200 ngọc thô sơ", "");
                            }
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Đổi ngọc rồng Namếc", () -> {
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 1 sao", () -> {
                            if (ballZ < 1000000) {
                                getService().sendTextNPC("Cần 1000.000 Ballz, 500 ngọc thô sơ đổi được ngọc rồng 2 sao", "");
                                return;
                            }
                            if (ballZ > 1000000 && removeItems(838, 500)) {
                                addBallz(-1000000, true);
                                Item item = new Item(901, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 1000.000 Ballz, 500 ngọc thô sơ đổi được ngọc rồng 1 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 2 sao", () -> {
                            if (ballZ < 1200000) {
                                getService().sendTextNPC("Cần 1200.000 Ballz, 600 ngọc thô sơ đổi được ngọc rồng 2 sao", "");
                                return;
                            }
                            if (removeItems(838, 600) && ballZ > 1200000) {
                                addBallz(-1200000, true);
                                Item item = new Item(902, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 1200.000 Ballz, 600 ngọc thô sơ đổi được ngọc rồng 2 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 3 sao", () -> {
                            if (ballZ < 1400000) {
                                getService().sendTextNPC("Cần 1400.000 Ballz, 700 ngọc thô sơ đổi được ngọc rồng 3 sao", "");
                                return;
                            }
                            if (removeItems(838, 700) && ballZ > 1400000) {
                                addBallz(-1400000, true);
                                Item item = new Item(903, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 1400.000 Ballz, 700 ngọc thô sơ đổi được ngọc rồng 3 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 4 sao", () -> {
                            if (ballZ < 1600000) {
                                getService().sendTextNPC("Cần 1600.000 Ballz, 800 ngọc thô sơ đổi được ngọc rồng 4 sao", "");
                                return;
                            }
                            if (removeItems(838, 800) && ballZ > 1600000) {
                                addBallz(-1600000, true);
                                Item item = new Item(904, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 160.000 Ballz, 800 ngọc thô sơ đổi được ngọc rồng 4 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 5 sao", () -> {
                            if (ballZ < 1800000) {
                                getService().sendTextNPC("Cần 1800.000 Ballz, 900 ngọc thô sơ đổi được ngọc rồng 5 sao", "");
                                return;
                            }
                            if (removeItems(838, 900) && ballZ > 1800000) {
                                addBallz(-1800000, true);
                                Item item = new Item(905, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 1800.000 Ballz, 900 ngọc thô sơ đổi được ngọc rồng 5 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 6 sao", () -> {
                            if (ballZ < 2000000) {
                                getService().sendTextNPC("Cần 2000.000 Ballz, 1000 ngọc thô sơ đổi được ngọc rồng 6 sao", "");
                                return;
                            }
                            if (removeItems(838, 1000) && ballZ > 2000000) {
                                addBallz(-2000000, true);
                                Item item = new Item(906, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 2000.000 Ballz, 1000 ngọc thô sơ đổi được ngọc rồng 6 sao", "");
                            }
                        }));
                        menus.add(new Menu(CMDMenu.EXECUTE, "Ngọc rồng 7 sao", () -> {
                            if (ballZ < 2100000) {
                                getService().sendTextNPC("Cần 2100.000 Ballz, 1100 ngọc thô sơ đổi được ngọc rồng 7 sao", "");
                                return;
                            }
                            if (removeItems(838, 1100) && ballZ > 2100000) {
                                addBallz(-2100000, true);
                                Item item = new Item(907, "doinr");
                                item.setLock(true);
                                addItemToBag(item);
                                getService().addItem(item);
                            } else {
                                getService().sendTextNPC("Cần 2100.000 Ballz, 1100 ngọc thô sơ đổi được ngọc rồng 7 sao", "");
                            }
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    break;
                case 60:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Nhiệm vụ PôPô cấp", () -> {
                        menus.clear();
                        menus.add(new Menu(CMDMenu.EXECUTE, "Nhận nhiệm vụ(1000 kim cương)", () -> {
                            if (kimCuong < 1000) {
                                service.warningMessage("Không đủ kim cương");
                                return;
                            }
                        }));
                        getService().openUIMenu(identifer);
                    }));
                    menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {
                        service.openGUI(111);
                        service.openGUIPoPo();
                    }));
                    break;

                case 62:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Doanh trại độc nhãn (còn " + countBarrack + " lượt)", () -> {
                        if (countBarrack < 1) {
                            service.serverDialog("Hết lượt tham gia");
                            return;
                        }
                        if (exp < 50_739_850) {
                            service.serverDialog("Cần ít nhất 50.739.850 sức mạnh để tham gia");
                            return;
                        }
                        if (getGroup() == null) {
                            countBarrack--;
                            Barrack barrack = new Barrack(level());
                            addWorld(barrack);
                            barrack.joinZone(this);
                        } else {
                            if (this.group.getIndexById(this.id) != 0) {
                                serverDialog("Phải là trưởng nhóm mới có thể mở phó bản");
                            } else {
                                List<Char> chars = this.getGroup().getCharsInZone(zone.map.id, zone.id);
                                if (chars.size() < getGroup().getNumberMember()) {
                                    serverDialog("Vui lòng tập hợp đủ thành viên trước khi tham gia");
                                    return;
                                }
                                int level = 0;
                                for (Char _char : chars) {
                                    if (_char != null && !_char.isCleaned && _char != this && !_char.isDead) {
                                        if (_char.countBarrack < 1) {
                                            serverDialog("Người chơi " + _char.name + " không còn lượt tham gia");
                                            return;
                                        }
                                        if (_char.exp < 50_739_850) {
                                            serverDialog("Người chơi " + _char.name + " chưa đạt 50.739.850 sức mạnh");
                                            return;
                                        }
                                        level += _char.level();
                                    }
                                }
                                level /= chars.size();
                                Barrack barrack = new Barrack(level);
                                for (Char _char : chars) {
                                    if (_char != null && !_char.isCleaned && !_char.isDead) {
                                        _char.countBarrack--;
                                        _char.addWorld(barrack);
                                        barrack.joinZone(_char);
                                    }
                                }
                            }
                        }
                    }));
                    break;
                case 23:
                    menus.add(new Menu(CMDMenu.EXECUTE, "Xóa vật phẩm rác: " + (this.removeItemRac ? "bật" : "tắt"), () -> {
                        this.removeItemRac = !this.removeItemRac;
                        this.getService().serverDialog("Chức năng xóa vật phẩm dưới 10 dòng chỉ số " + (this.removeItemRac ? "bật" : "tắt"));
                    }));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mail createMail(String sender, String title, String content, int ballZ, int coin, int zeni, Item item) {
        Mail mail = new Mail();
        mail.id = mailManager.getID();
        mail.setSender(sender);
        mail.setTitle(title);
        mail.setContent(content);
        mail.setBallz(ballZ);
        mail.setCoin(coin);
        mail.setZeni(zeni);
        if (item != null) {
            mail.setItem(item);
        }
        mail.setTime((int) ((System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)) / 1000));
        return mail;
    }

    public void writeItemBody(Message m) {
        try {
            List<Item> items = Util.cleanArrayItem(body);
            m.writer().writeByte(items.size());
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeBoolean(item.isLock);
                    m.writer().writeLong(item.expire);
                    m.writer().writeByte(item.sys);
                    m.writer().writeByte(item.upgrade);
                    m.writeUTF(item.strOption);
                    m.writeUTF(item.nameCreate);
                }
            }
        } catch (Exception e) {
        }

    }

    public void writeItemBody2(Message m) {
        try {
            List<Item> items = Util.cleanArrayItem(body2);
            m.writer().writeByte(items.size());
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item != null) {
                    m.writer().writeShort(item.id);
                    m.writer().writeBoolean(item.isLock);
                    m.writer().writeLong(item.expire);
                    m.writer().writeByte(item.sys);
                    m.writer().writeByte(item.upgrade);
                    m.writeUTF(item.strOption);
                    m.writeUTF(item.nameCreate);
                }
            }
        } catch (Exception e) {

        }

    }

    public void itemBagToBox(Message ms) {
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            short indexUI = ms.reader().readShort();
            if (indexUI < 0 || indexUI > this.numberCellBag) {
                return;
            }
            Item item = this.bag[indexUI];
            if (item == null) {
                return;
            }
            if (item.id == 724) {
                service.serverDialog("Không thể chuyển đậu thần vào rương");
                return;
            }
            if (item.isExpired()) {
                removeItem(item.index, 1, true);
                return;
            }
            itemBagToBox(item);
        } catch (Exception ex) {
            Log.error("item bag to box err: " + ex.getMessage(), ex);
            getService().serverDialog(ex.getMessage());
        }
    }

    public int getIndexItemByIdInBox(int id, boolean isLock) {
        for (Item item : this.box) {
            if (item != null && item.id == id && item.isLock == isLock) {
                return item.index;
            }
        }
        return -1;
    }

    public int getQuantityItemById(int id) {
        int number = 0;
        try {
            for (byte i = 0; i < this.bag.length; i++) {
                Item item = this.bag[i];
                if (item != null && item.id == id) {
                    number += item.getQuantity();
                }
            }
        } catch (Exception e) {
        }
        return number;
    }

    public void itemBagToBox(Item item) {
        try {
            int index = getIndexItemByIdInBox(item.id, item.isLock);
            if (index == -1 || !box[index].getTemplate().isXepChong || box[index].hasExpire()
                    || item.isLock != box[index].isLock) {
                for (int i = 0; i < this.numberCellBox; i++) {
                    if (box[i] == null) {
                        int indexM = item.index;
                        item.index = i;
                        box[i] = item;
                        bag[indexM] = null;
                        getService().itemBagToBox(indexM, i);
                        return;
                    }
                }
                serverDialog("Rương đồ không đủ chỗ trống");
                return;
            } else {
                box[index].add(item.getQuantity());
                getService().itemBagToBox(item.index, index);
                bag[item.index] = null;
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serverDialog(String str) {
        getService().serverDialog(str);
    }

    public void itemBoxToBag(Message ms) {
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            short indexUI = ms.reader().readShort();
            if (indexUI < 0 || indexUI > this.numberCellBox) {
                return;
            }
            Item item = this.box[indexUI];
            if (item == null) {
                return;
            }
            if (item.isExpired()) {
                box[indexUI] = null;
                return;
            }
            if (taskId == 0 && getItemStepTask() == item.id) {
                updateTaskCount(1);
            }
            itemBoxToBag(item);
        } catch (Exception e) {
            Log.error("item box to bag err: " + e.getMessage(), e);
            getService().serverDialog(e.getMessage());
        }
    }

    public void itemBoxToBag(Item item) {
        try {
            int index = getIndexItemByIdInBag(item.id, item.isLock);
            if (index == -1 || !bag[index].getTemplate().isXepChong || bag[index].hasExpire()
                    || item.isLock != bag[index].isLock) {
                for (int i = 0; i < this.numberCellBag; i++) {
                    if (bag[i] == null) {
                        int indexM = item.index;
                        item.index = i;
                        bag[i] = item;
                        box[indexM] = null;
                        getService().itemBoxToBag(indexM, i);
                        return;
                    }
                }
                warningBagFull();
                return;
            } else {
                bag[index].add(item.getQuantity());
                box[item.index] = null;
                getService().itemBoxToBag(item.index, index);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wakeUpFromDead(Message ms) {
        try {
            if (!this.isDead) {
                return;
            }
            if (zone == null || zone.map == null) {
                return;
            }
            if (zone instanceof ZMartialArts martialArts && !(martialArts.getWorld() instanceof Qualifiers)) {
                getService().serverMessage("Không thể hồi sinh tại nơi này!");
                return;
            }
            if (zone instanceof Zstadium) {
                getService().serverMessage("Không thể hồi sinh tại nơi này!");
                return;
            }
            if (!zone.map.isTrainZone()) { // dua ve khi close zone.isClosed()
                if (!removeDiamond(1)) {
                    getService().serverMessage("Không đủ kim cương");
                    return;
                }
            }
            wakeUpFromDead();
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void wakeUpFromDead() {
        try {
            if (!this.isDead) {
                return;
            }
            this.isDead = false;
            this.hp = this.maxHP;
            this.mp = this.maxMP;
            zone.getService().meLive(this);
            if (zone.map.isNgucTu()) {
                countDead++;
                if (countDead >= 5) {
                    countDead = 0;
                    joinZone(54, 0, 1);
                }
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public boolean removeDiamond(int num) {
        kimCuong = getQuantityItemById(160);
        if (kimCuong < num || kimCuong < 1) {
            return false;
        }
        kimCuong -= num;
        removeItems(160, num);
        getService().serverMessage("Bạn bị -" + num + " kim cương");
        return true;
    }

    public void addHp(int number) {
        if (number < 0) {
            if (haveOptions[412]) {
                if (pet != null && !pet.isDead && (pet.typePet == 1 || pet.typePet == 2) && getEm().findByID(144) == null) {
                    int num = number * (options[412] / 10) / 100;
                    pet.addHp(num);
//                    zone.getService().updatePet(id, pet.typePet, pet.hp, pet.maxHP, pet.mp, pet.maxMP);
//                    if (pet.hp <= 0) {
//                        pet.startDie();
//                        pet.typePet = 0;
//                        zone.getService().updatePet(id, pet.typePet, pet.hp, pet.maxHP, pet.mp, pet.maxMP);
//                    }
                    number = number - num;
                }
            }
        }
        this.hp += number;
        if (this.hp > this.maxHP) {
            this.hp = this.maxHP;
        }
        getService().updateHP();
        zone.getService().updateHP(this);
    }

    public void addMp(int number) {
        this.mp += number;
        if (this.mp > this.maxMP) {
            this.mp = this.maxMP;
        }
        getService().updateMP();
        zone.getService().updateMP(this);
    }

    public void startDie() {
        if (trade != null) {
            trade.closeUITrade();
        }
        hp = 0;
        isDead = true;
        if (zone.findBossInZone()) {

        }
        if (zone instanceof ZPowerTournament || zone instanceof ZMartialArts) {
            if (countWakeUpFromDead < 10) {
                Util.setTimeoutSchedule(() -> {
                    countWakeUpFromDead++;
                    wakeUpFromDead();
                }, 5000);
            } else {
                wakeUpFromDead();
                MapManager.getInstance().joinZone(this, 86, 0, 1);
            }
        }
        if (zone instanceof ZPlanetBlackStar) {
            if (countWakeUpFromDead < 2) {
                Util.setTimeoutSchedule(() -> {
                    countWakeUpFromDead++;
                    wakeUpFromDead();
                }, 5000);
            } else {
                wakeUpFromDead();
                MapManager.getInstance().joinZone(this, 86, 0, 1);
            }
        }
        if (zone instanceof ZPlanetBlackStar zPlanetBlackStar) {
            zPlanetBlackStar.dropBall(this);
        }
        try {
            if (IsCuuSat) {
                Char cAnCuuSat = zone.findCharById(IdCuuSat);
                if (cAnCuuSat != null) {
                    cAnCuuSat.getService().sendMessage(PvPHandler.MsgCloseCuuSat(id, true));
                    cAnCuuSat.CleanUpCuuSat();
                }

                getService().sendMessage(PvPHandler.MsgCloseCuuSat(id, true));
                CleanUpCuuSat();
            }

            if (IsAnCuuSat) {
                Char cAnCuuSat = zone.findCharById(IdCharMoiCuuSat);
                if (cAnCuuSat != null) {
                    if (cAnCuuSat.lvPK < 80) {
                        cAnCuuSat.lvPK++;
                    }
                    cAnCuuSat.getService().sendMessage(PvPHandler.MsgCloseCuuSat(cAnCuuSat.id, true));
                    cAnCuuSat.CleanUpCuuSat();
                }

                getService().sendMessage(PvPHandler.MsgCloseCuuSat(IdCharMoiCuuSat, true));
                CleanUpCuuSat();
            }
            if (IsTyVo) {
                Char cTyVo = zone.findCharById(IdTyVo);
                if (cTyVo != null) {
                    cTyVo.CleanUpTyvo();
                }

                for (Char c : zone.getChars()) {
                    if (c != null && c.user != null && c.user.session.isConnected()) {
                        c.getService().sendMessage(PvPHandler.MsgCloseTyVo((byte) 2, IdTyVo, id));
                    }
                }

                CleanUpTyvo();
            } else {
                if (lvPK > 0) {
                    lvPK--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void CleanUpTyvo() {
        IdTyVo = -1;
        IsTyVo = false;
    }

    public void CleanUpCuuSat() {
        IdCuuSat = -1;
        IdCharMoiCuuSat = -1;

        IsCuuSat = false;
        IsAnCuuSat = false;
    }

    public void requestItem(byte type) {
        try {
            Store storeData = StoreManager.getInstance().find(type);
            if (storeData != null) {
                storeData.show(this);
            } else {
                getService().showShopNoItem(type);
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public int getSlotNull() {
        int number = 0;
        for (int i = 0; i < this.numberCellBag; i++) {
            if (bag[i] == null) {
                number++;
            }
        }
        return number;
    }

    public void sortItem(Message ms) {
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            byte type = ms.reader().readByte();
            if (type == 0) {
                sortItemBag();
            } else if (type == 1) {
                sortItemBox();
            }
            getService().sortItem(type);
//            getService().closeCurrentTab();
        } catch (Exception e) {
            Log.error("err: " + e.getMessage(), e);
        }
    }

    public void sortItemBag() {
        try {
            Item[] var0 = bag;
            Vector<Item> var1 = new Vector<>();
            for (Item item : var0) {
                if (item != null) {
                    var1.add(item);
                }
            }
            for (int var4 = 0; var4 < var1.size(); var4++) {
                Item var7 = var1.get(var4);
                if (var7.getTemplate().isXepChong) {
                    for (int var5 = var1.size() - 1; var5 > var4; var5--) {
                        Item var3 = var1.get(var5);
                        if (var3.id == var7.id && var3.isLock == var7.isLock && var3.expire == var7.expire) {
                            var7.setQuantity(var7.getQuantity() + var3.getQuantity());
                            var1.remove(var5);
                        }
                    }
                }
            }
            for (int i = 0; i < var1.size(); i++) {
                var0[i] = var1.get(i);
                var0[i].index = i;
            }
            for (int i = var1.size(); i < var0.length; i++) {
                var0[i] = null;
            }

            getService().updateIndexBag();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sortItemBox() {
        try {
            Item[] var0 = box;
            Vector var1 = new Vector();

            for (int var2 = 0; var2 < var0.length; ++var2) {
                if (var0[var2] != null) {
                    var1.add(var0[var2]);
                }

                var0[var2] = null;
            }

            for (int var4 = 0; var4 < var1.size(); ++var4) {
                Item var7;
                if ((var7 = (Item) var1.get(var4)).getTemplate().isXepChong) {
                    for (int var5 = var1.size() - 1; var5 > var4; --var5) {
                        Item var3;
                        if ((var3 = (Item) var1.get(var5)).id == var7.id && var3.isLock == var7.isLock && var3.expire == var7.expire) {
                            var7.setQuantity(var7.getQuantity() + var3.getQuantity());
                            var1.remove(var5);
                        }
                    }
                }
            }

            for (short var8 = 0; var8 < var1.size(); var0[var8].index = var8++) {
                var0[var8] = (Item) var1.get(var8);
            }
        } catch (Exception var6) {
        }
    }

    public boolean isActiveAction() {
        return this.isUnlock() || this.MaBaoVe.isEmpty();
    }

    public void buyItem(Message ms) {
        try {
            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục.");
                return;
            }

            if (trade != null) {
                warningTrade();
                return;
            }
            short id = ms.reader().readShort();
            int quantity = ms.reader().readShort();
            if (quantity < 1 || quantity > 10000) {
                serverDialog("chỉ có thể mua Số lượng lớn hơn 0 và nhỏ hơn 10000!");
                return;
            }
            Log.debug("buy item: " + id + " quantity: " + quantity);
            ItemStore itemBuy = StoreManager.getInstance().findItemStore(id);
            if (itemBuy == null) {
                serverDialog("Không tìm thấy vật phẩm");
                return;
            }
            if (itemBuy.getType() == 38) {
                if (itemBuy.getRequire() > pointKame) {
                    serverDialog("Không đủ điểm Kame");
                    return;
                }
            }
            ItemTemplate template = itemBuy.getTemplate();
            List<Item> items1 = itemBuy.items;
            int slotNull = getSlotNull();
            if ((template.isXepChong && slotNull == 0) || (!template.isXepChong && slotNull < quantity) || (items1.size() > 0 && slotNull < items1.size())) {
                warningBagFull();
                return;
            }
            int giaCoin = itemBuy.getCoin() * quantity;
            int giaBallz = itemBuy.getBallz() * quantity;
            int giaKimCuong = itemBuy.getDiamond() * quantity;
            int giaZeni = itemBuy.getZeni() * quantity;
            int giaCoinClan = itemBuy.getCoinClan() * quantity;
            if (giaCoin < 0 || giaBallz < 0 || giaKimCuong < 0 || giaZeni < 0 || giaCoinClan < 0) {
                return;
            }
            if (giaCoin > 0) {
                if (coin < giaCoin) {
                    serverDialog("Không đủ tiền");
                    return;
                }
            } else if (giaBallz > 0) {
                if (ballZ < giaBallz) {
                    serverDialog("Không đủ tiền");
                    return;
                }
            } else if (giaZeni > 0) {
                if (zeni < giaZeni) {
                    serverDialog("Không đủ tiền");
                    return;
                }
            } else if (giaCoinClan > 0) {
//                if (coinClan < giaCoinClan) {
//                    serverDialog("Không đủ tiền");
//                    return;
//                }
            }

            addBallz(-giaBallz, false);
            addZeni(-giaZeni, false);
            addCoin(-giaCoin, false);
            if (giaKimCuong > 0) {
                if (!removeItems(160, giaKimCuong)) {
                    serverDialog("Không đủ kim cương");
                    return;
                }
            }

            int n = quantity;
            if (itemBuy.getTemplate().isXepChong) {
                n = 1;
            }
            if (itemBuy.getId() == 23) {
                Item it = new Item(838, "shop");
                it.setLock(true);
                it.setQuantity(10 * quantity);
                addItemToBag(it);
                getService().addItem(it);
                getService().updateIndexBag();
                return;
            }
            List<Item> items = new ArrayList<>();
            if (itemBuy.items.size() > 0) {
                com.vdtt.model.Reward reward = findReward(itemBuy.getItemID());
                if (reward != null) {
                    serverDialog("Bạn đã mua vật phẩm này rồi");
                    return;
                } else {
                    com.vdtt.model.Reward rw = new com.vdtt.model.Reward();
                    rw.type = (short) itemBuy.getItemID();
                    rewardList.add(rw);
                }
                for (int i = 0; i < items1.size(); i++) {
                    Item item1 = items1.get(i);
                    Item item = new Item(items1.get(i).id, "shop");
                    item.isLock = item1.isLock;
                    item.setQuantity(item1.getQuantity());
                    item.setStrOption(item1.getStrOption());
                    if (item1.expire > 0) {
                        item.expire += System.currentTimeMillis() + item1.expire;
                    }
                    if (item.getQuantity() <= 0) {
                        item.setQuantity(1);
                    }
                    item.setUpgrade(item1.getUpgrade());
                    item.createOption();
                    addItemToBag(item);
                    items.add(item);
                }
            } else {
                for (int i = 0; i < n; i++) {
                    Item item = new Item(itemBuy.getItemID(), "shop");
                    item.setLock(itemBuy.isLock());
                    item.setStrOption(itemBuy.getStrOptions());
                    if (itemBuy.getExpire() > 0) {
                        item.expire = itemBuy.getExpire() + System.currentTimeMillis();
                    }
                    if (item.getTemplate().isXepChong) {
                        item.setQuantity(quantity);
                    } else {
                        item.setQuantity(1);
                    }
                    if (item.getQuantity() <= 0) {
                        item.setQuantity(1);
                    }
                    item.isLock = true;
                    item.createOption();
                    addItemToBag(item);
                    items.add(item);
                }
            }
            for (Item item : items) {
                if (item.template.id == 1146) {
                    item.isLock = false;
                }
            }
            getService().buyItem(items);
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void addBallz(int number, boolean isUpdate) {
        if (trade != null) {
            trade.closeUITrade();
        }
        long l = ((long) ballZ) + ((long) number);
        if (l > Integer.MAX_VALUE) {
            l = Integer.MAX_VALUE;
        }
        this.ballZ = (int) l;
        getService().updateBallz(isUpdate);

    }

    public void addZeni(int number, boolean isUpdate) {
        long l = ((long) zeni) + ((long) number);
        if (l > Integer.MAX_VALUE) {
            l = Integer.MAX_VALUE;
        }

        if (number < 0) {
            tieuNgay += number;
            tieuTuan += number;
        }

        this.zeni = (int) l;
        getService().updateZeni(isUpdate);

    }

    public long getZeniSpentDay() {
        return Math.abs(tieuNgay);
    }

    public long getZeniSpentWeek() {
        return Math.abs(tieuTuan);
    }

    public void addCoin(int number, boolean isUpdate) {
        long l = ((long) coin) + ((long) number);
        if (l > Integer.MAX_VALUE) {
            l = Integer.MAX_VALUE;
        }
        this.coin = (int) l;
        getService().updateCoin(isUpdate);
    }

    public void addZeniLock(int number, boolean isUpdate) {
        long l = ((long) zeniLock) + ((long) number);
        if (l > Integer.MAX_VALUE) {
            l = Integer.MAX_VALUE;
        }
        this.zeniLock = (int) l;
        getService().updateZeniLock(isUpdate);
    }

    public void returnFromDead() {
        try {
            if (isDead) {
                if (zone instanceof ZMartialArts martialArts) {
                    if (martialArts.getWorld() instanceof Qualifiers) {
                        wakeUpFromDead();
                        zone.join(this, -1);
                    } else {
                        getService().serverMessage("Không thể quay về ngay lúc này");
                    }
                    return;
                }
                if (zone instanceof ZPowerTournament zPowerTournament) {
                    if (zPowerTournament.getWorld() instanceof QualifiersPowerStation) {
                        wakeUpFromDead();
                        zone.join(this, -1);
                    } else {
                        getService().serverMessage("Không thể quay về ngay lúc này");
                    }
                    return;
                }
                wakeUpFromDead();
                MapManager.getInstance().joinZone(this, 86, 0, -1);
            }
        } catch (Exception e) {
            Log.error("err: " + e.getMessage(), e);
        }
    }

    public int findIndexSkillWithCT() {
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            if (skill.getSkillTemplate().i == 1) {
                return i;
            }
        }
        return -1;
    }

    public void confirmMenuItem(Message ms) {
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            short index = ms.reader().readShort();
            byte type = ms.reader().readByte();
            byte option = 0;
            try {
                option = ms.reader().readByte();
            } catch (Exception e) {

            }
            if (index < 0 || index >= bag.length) {
                return;
            }
            Item item = bag[index];
            if (item == null || item.getQuantity() < 1) {
                return;
            }
            switch (item.id) {
                case 799:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    Item one = new Item(1038, "cs");
                    one.setLock(true);
                    one.setQuantity(1);
                    switch (type) {
                        case 0:
                            one.strOption = "346," + Util.nextInt(50, 100);
                            break;
                        case 1:
                            one.strOption = "347," + Util.nextInt(10, 25);
                            break;
                        case 2:
                            one.strOption = "348," + Util.nextInt(10, 25);
                            break;
                    }
                    addItemToBag(one);
                    getService().addItem(one);
                    break;
                case 800:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    one = new Item(1039, "cs");
                    one.setLock(true);
                    one.setQuantity(1);
                    switch (type) {
                        case 0:
                            one.strOption = "17," + Util.nextInt(10, 50);
                            break;
                        case 1:
                            one.strOption = "26," + Util.nextInt(10, 50);
                            break;
                        case 2:
                            one.strOption = "27," + Util.nextInt(10, 50);
                            break;
                        case 3:
                            one.strOption = "48," + Util.nextInt(10, 50);
                            break;
                        case 4:
                            one.strOption = "49," + Util.nextInt(10, 50);
                            break;
                        case 5:
                            one.strOption = "50," + Util.nextInt(10, 50);
                            break;
                        case 6:
                            one.strOption = "51," + Util.nextInt(10, 50);
                            break;
                        case 7:
                            one.strOption = "52," + Util.nextInt(10, 50);
                            break;

                    }
                    addItemToBag(one);
                    getService().addItem(one);
                    break;
                case 801:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    one = new Item(1040, "cs");
                    one.setLock(true);
                    one.setQuantity(1);
                    switch (type) {
                        case 0:
                            one.strOption = "273," + Util.nextInt(50, 200);
                            break;
                        case 1:
                            one.strOption = "345," + Util.nextInt(50, 200);
                            break;
                        case 2:
                            one.strOption = "359," + Util.nextInt(50, 200);
                            break;

                    }
                    addItemToBag(one);
                    getService().addItem(one);
                    break;
                case 802:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    one = new Item(1041, "cs");
                    one.setLock(true);
                    one.setQuantity(1);
                    switch (type) {
                        case 0:
                            one.strOption = "28," + Util.nextInt(50, 200);
                            break;
                        case 1:
                            one.strOption = "351," + Util.nextInt(50, 200);
                            break;
                        case 2:
                            one.strOption = "352," + Util.nextInt(50, 200);
                            break;

                    }
                    addItemToBag(one);
                    getService().addItem(one);
                    break;
                case 803:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    one = new Item(1042, "cs");
                    one.setLock(true);
                    one.setQuantity(1);
                    switch (type) {
                        case 0:
                            one.strOption = "353," + Util.nextInt(20, 40);
                            break;
                        case 1:
                            one.strOption = "354," + Util.nextInt(20, 40);
                            break;
                        case 2:
                            one.strOption = "355," + Util.nextInt(20, 40);
                            break;
                    }
                    addItemToBag(one);
                    getService().addItem(one);
                    break;
                case 804:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    one = new Item(1043, "cs");
                    one.setLock(true);
                    one.setQuantity(1);
                    switch (type) {
                        case 0:
                            one.strOption = "356," + Util.nextInt(5, 10);
                            break;
                        case 1:
                            one.strOption = "357," + Util.nextInt(5, 10);
                            break;

                    }
                    addItemToBag(one);
                    getService().addItem(one);
                    break;
                case 805:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    one = new Item(1044, "cs");
                    one.setLock(true);
                    one.setQuantity(1);
                    switch (type) {
                        case 0:
                            one.strOption = "34," + Util.nextInt(50, 100);
                            break;
                        case 1:
                            one.strOption = "41," + Util.nextInt(50, 100);
                            break;

                    }
                    addItemToBag(one);
                    getService().addItem(one);
                    break;
                case 1037:
                    ItemOption gemOption = item.getItemOption()[0];
                    if (gemOption == null) {
                        return;
                    }
                    switch (type) {
                        case 0:
                            if (body[2] == null || body[2].strOption == null || body[2].strOption.isEmpty()) {
                                return;
                            }
                            if (body[2].replaceOption(this, gemOption, option)) {
                                getService().updateItemBodyChange();
                                zone.getService().updateItemBody(this);
                                setAbility();
                                removeItem(index, 1, true);
                            }
                            break;
                        case 1:
                            if (body[4] == null || body[4].strOption == null || body[4].strOption.isEmpty()) {
                                return;
                            }
                            if (body[4].replaceOption(this, gemOption, option)) {
                                getService().updateItemBodyChange();
                                zone.getService().updateItemBody(this);
                                setAbility();
                                removeItem(index, 1, true);
                            }
                            break;
                        case 2:
                            if (body[6] == null || body[6].strOption == null || body[6].strOption.isEmpty()) {
                                return;
                            }
                            if (body[6].replaceOption(this, gemOption, option)) {
                                getService().updateItemBodyChange();
                                zone.getService().updateItemBody(this);
                                setAbility();
                                removeItem(index, 1, true);
                            }
                            break;
                        case 3:
                            if (body[8] == null || body[8].strOption == null || body[8].strOption.isEmpty()) {
                                return;
                            }
                            if (body[8].replaceOption(this, gemOption, option)) {
                                getService().updateItemBodyChange();
                                zone.getService().updateItemBody(this);
                                setAbility();
                                removeItem(index, 1, true);
                            }
                            break;
                    }
                    break;

                case 1038:
                case 1039:
                case 1040:
                case 1041:
                case 1042:
                case 1043:
                case 1044:
                    gemOption = item.getItemOption()[0];
                    if (gemOption == null) {
                        return;
                    }
                    int idType = gemOption.getItemOptionTemplate().type;
                    int[] bodyIndexs = new int[]{2, 4, 6, 8};
                    List<Item> bds = new ArrayList<>();
                    for (int i : bodyIndexs) {
                        if (!(body[i] == null || body[i].strOption == null || body[i].strOption.isEmpty())) {
                            if (body[i].checkOptionByType(idType)) {
                                bds.add(body[i]);
                            }
                        }
                    }

                    if (bds.isEmpty()) {
                        return;
                    }

                    Item ibody = bds.get(type);
                    if (ibody == null || ibody.strOption == null || ibody.strOption.isEmpty()) {
                        return;
                    }
                    ibody.replaceOption(gemOption);
                    getService().updateItemBodyChange();
                    zone.getService().updateItemBody(this);
//                    switch (type) {
//                        case 0:
//                            if (body[2] == null || body[2].strOption == null || body[2].strOption.isEmpty()) {
//                                return;
//                            }
//                            body[2].replaceOption(gemOption);
//                            getService().updateItemBodyChange();
//                            zone.getService().updateItemBody(this);
//                            break;
//                        case 1:
//                            if (body[4] == null || body[4].strOption == null || body[4].strOption.isEmpty()) {
//                                return;
//                            }
//                            body[4].replaceOption(gemOption);
//                            getService().updateItemBodyChange();
//                            zone.getService().updateItemBody(this);
//                            break;
//                        case 2:
//                            if (body[6] == null || body[6].strOption == null || body[6].strOption.isEmpty()) {
//                                return;
//                            }
//                            body[6].replaceOption(gemOption);
//                            getService().updateItemBodyChange();
//                            zone.getService().updateItemBody(this);
//                            break;
//                        case 3:
//                            if (body[8] == null || body[8].strOption == null || body[8].strOption.isEmpty()) {
//                                return;
//                            }
//                            body[8].replaceOption(gemOption);
//                            getService().updateItemBodyChange();
//                            zone.getService().updateItemBody(this);
//                    }
                    setAbility();
                    removeItem(index, 1, true);
                    break;
                case 833:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    Item it = new Item(1037, "cs");
                    it.setLock(true);
                    it.setQuantity(1);
                    switch (type) {
                        case 0:
                            it.strOption = "335," + Util.nextInt(100, 400);
                            break;
                        case 1:
                            it.strOption = "336," + Util.nextInt(100, 400);
                            break;
                        case 2:
                            it.strOption = "337," + Util.nextInt(25, 100);
                            break;
                        case 3:
                            it.strOption = "338," + Util.nextInt(25, 100);
                            break;
                        case 4:
                            it.strOption = "339," + Util.nextInt(25, 100);
                            break;
                        case 5:
                            it.strOption = "340," + Util.nextInt(25, 100);
                            break;
                        case 6:
                            it.strOption = "341," + Util.nextInt(15, 50);
                            break;
                        case 7:
                            it.strOption = "342," + Util.nextInt(30, 100);
                            break;
                        case 8:
                            it.strOption = "343," + Util.nextInt(15, 50);
                            break;
                        case 9:
                            it.strOption = "344," + Util.nextInt(15, 50);
                            break;
                    }
                    addItemToBag(it);
                    getService().addItem(it);
                    break;
                case 168:
                    switch (type) {
                        case 0:
                            MapManager.getInstance().joinZone(this, 86, 0, 6);
                            break;
                        case 1:
                            MapManager.getInstance().joinZone(this, 67, 0, 6);
                            break;
                        case 2:
                            MapManager.getInstance().joinZone(this, 56, 0, 6);
                            break;
                        case 3:
                            MapManager.getInstance().joinZone(this, 87, 0, 6);
                            break;
                        case 4:
                            MapManager.getInstance().joinZone(this, 83, 0, 6);
                            break;
                    }
                    break;
                case 569:
                case 716:
                case 717:
                case 718:
                case 719:
                case 720:
                    if (this.itemMapBlackDragonBall != null) {
                        if (zone instanceof ZPlanetBlackStar zPlanetBlackStar) {
                            zPlanetBlackStar.dropBall(this);
                        }
                    }
                    int typeTau = 0;
                    if (item.id == 569) {
                        typeTau = TypeTau.TAU_TRAI_DAT_VIP;
                    } else if (item.id == 716) {
                        typeTau = TypeTau.TAU_FIDE;
                    } else if (item.id == 717) {
                        typeTau = TypeTau.TAU_TUONG_LAI;
                    } else if (item.id == 718) {
                        typeTau = TypeTau.TAU_XAYDA;
                    } else if (item.id == 719) {
                        typeTau = TypeTau.TAU_NAMEC;
                    } else if (item.id == 720) {
                        typeTau = TypeTau.TAU_YARDRAT;
                    }
                    switch (type) {
                        case 0:
                            int mapId = findMapIdByLevel();
                            if (mapId > 0) {
                                MapManager.getInstance().joinZone(this, mapId, 0, typeTau);
                            }
                            break;
                        case 1:
                            MapManager.getInstance().joinZone(this, 86, 0, typeTau);
                            break;
                        case 2:
                            MapManager.getInstance().joinZone(this, 67, 0, typeTau);
                            break;
                        case 3:
                            MapManager.getInstance().joinZone(this, 56, 0, typeTau);
                            break;
                        case 4:
                            MapManager.getInstance().joinZone(this, 87, 0, typeTau);
                            break;
                        case 5:
                            MapManager.getInstance().joinZone(this, 54, 0, typeTau);
                            break;
                        case 6:
                            MapManager.getInstance().joinZone(this, 53, 0, typeTau);
                            break;
                    }
                    break;
                case 828:
                case 808:
                    int id = switch (type) {
                        case 0 ->
                            569;
                        case 1 ->
                            716;
                        case 2 ->
                            717;
                        case 3 ->
                            718;
                        case 4 ->
                            719;
                        case 5 ->
                            720;
                        default ->
                            0;
                    };
                    if (id > 0) {
                        removeItem(index, 1, true);
                        achievements.increaseAchievementCount(22, 1);
                        Item item1 = new Item(id, "");
                        if (item.id == 808) {
                            item1.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
                        }
                        addItemToBag(item1);
                        getService().addItem(item1);
                    }
                    break;
                case 1106:
                case 1107:
                case 1108:
                case 1109:
                case 1110:
                case 1111:
                case 1112:
                case 1113:
                case 1150:
                    if (getSlotNull() < 1) {
                        warningBagFull();
                        return;
                    }
                    removeItem(index, 1, true);
                    Item star = new Item(getStarId(item.id), "cs");
                    star.setLock(true);
                    star.setQuantity(1);
                    star.strOption = generateStarOption(item.id, type);
                    addItemToBag(star);
                    getService().addItem(star);
                    break;
            }
            Log.debug("confirm menu item: " + item.id + " type: " + type + " option: " + option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getStarId(int caseId) {
        return switch (caseId) {
            case 1106 ->
                1038;
            case 1107 ->
                1039;
            case 1108 ->
                1040;
            case 1109 ->
                1041;
            case 1110 ->
                1042;
            case 1111 ->
                1043;
            case 1112 ->
                1044;
            case 1113, 1150 ->
                1037;
            default ->
                -1;
        };
    }

    private String generateStarOption(int itemId, int type) {
        int optionId, min;
        switch (itemId) {
            case 1106:
                optionId = switch (type) {
                    case 0 ->
                        346;
                    case 1 ->
                        347;
                    default ->//2
                        348;
                };
                min = (type == 0) ? 100 : 25;
                break;
            case 1107:
                optionId = switch (type) {
                    case 0 ->
                        17;
                    case 1 ->
                        26;
                    case 2 ->
                        27;
                    case 3 ->
                        48;
                    case 4 ->
                        49;
                    case 5 ->
                        50;
                    case 6 ->
                        51;
                    case 7 ->
                        52;
                    default ->
                        52;
                };
                min = 50;
                break;
            case 1108:
                optionId = new int[]{273, 345, 359}[type];
                min = 200;
                break;
            case 1109:
                optionId = switch (type) {
                    case 0 ->
                        28;
                    case 1 ->
                        351;
                    default ->//2
                        352;
                };
                min = 200;
                break;
            case 1110:
                optionId = 353 + type;
                min = 40;
                break;
            case 1111:
                optionId = 356 + type;
                min = 10;
                break;
            case 1112:
                optionId = (type == 0) ? 34 : 41;
                min = 100;
                break;
            case 1113:
                optionId = 335 + type;
                if (type == 0 || type == 1) {
                    min = 400;
                } else if (type == 2 || type == 3 || type == 4 || type == 5 || type == 7) {
                    min = 100;
                } else {
                    min = 50;
                }
                break;
            case 1150:
                optionId = 335 + type;
                if (type == 0 || type == 1) {
                    min = 400;
                } else if (type == 2 || type == 3 || type == 4 || type == 5 || type == 7) {
                    min = 100;
                } else {
                    min = 50;
                }

                int d = (int) (min * 1.1);
                if (type == 8 || type == 9) {
                    d = 60;
                }
                return optionId + "," + d;
            default:
                return "";
        }
        return optionId + "," + Util.nextInt(min, (int) (min * 1.1));
    }

    private int findMapIdByLevel() {
        for (MapTemplate mapTemplate : MapManager.getInstance().getMapTemplates()) {
            if (mapTemplate.mobList.size() < 1) {
                continue;
            }
            for (Mob mob : mapTemplate.mobList) {
                int lv = Math.abs(level() - mob.getTemplate().timeThuHoach);
                if (lv <= 3) {
                    return mapTemplate.id;
                }
            }
        }
        return -1;
    }

    public void showInfoGhepCT(Message ms) {
        lockItem.lock();
        try {
            byte size = ms.reader().readByte();
            int numCT = 0;
            Vector<Item> caiTrang = new Vector<>();
            boolean[][] list = new boolean[4][numberCellBag];

            while (ms.reader().available() > 0) {
                byte type = ms.reader().readByte();
                short index = ms.reader().readShort();
                if (index >= 0 && index < checkBag(type).length && !list[type][index]) {
                    list[type][index] = true;
                    Item item = checkBag(type)[index];
                    if (item != null && item.getTemplate().type == 14) {
                        caiTrang.add(item);
                        numCT++;
                    }
                }
            }

            if (numCT == 0) {
                serverDialog("Hãy chọn cải trang!");
                return;
            }
            if (numCT > 8) {
                serverDialog("Chỉ được chọn 8 cải trang!");
                return;
            }

            Item itemShow = caiTrang.get(0).clone();
            List<ItemOption> options1 = new ArrayList<>();
            List<ItemOption> nameCT = new ArrayList<>();
            List<ItemOption> nameOLD = new ArrayList<>();
            Set<Integer> existingIds = new HashSet<>();

            for (Item item : caiTrang) {
                if (item != null && item.strOption != null && !item.strOption.isEmpty()) {
                    for (ItemOption op : item.getItemOption()) {
                        if (op.getId() == 327 && item.id != itemShow.id) {
                            continue;
                        } else if (op.getItemOptionTemplate().type == 14) {
                            nameOLD.add(op);
                        } else {
                            options1.add(op);
                        }
                    }
                    if (item.id != itemShow.id) {
                        int id = ItemManager.getInstance().findByName(item.getTemplate().name);
                        if (id > -1) {
                            nameCT.add(new ItemOption(id));
                            existingIds.add(id);
                        }
                    }
                }
            }
            // Check for existing combinations
            List<ItemOption> summedOptions = options1.stream()
                    .collect(Collectors.groupingBy(ItemOption::getId, Collectors.summingInt(ItemOption::getParam)))
                    .entrySet().stream()
                    .map(entry -> new ItemOption(entry.getKey(), entry.getValue()))
                    .sorted((o1, o2) -> (o1.getId() == 327) ? -1 : ((o2.getId() == 327) ? 1 : 0))
                    .collect(Collectors.toList());
            int count = 0;
            for (ItemOption op : nameOLD) {
                count++;
                summedOptions.add(op);
            }
            for (ItemOption op : nameCT) {
                count++;
                summedOptions.add(op);
            }
            int level = count;
            int upgradePoints = (level / 3) * 2 + (level % 3 == 2 ? 1 : 0); // Adding remaining points for incomplete sets
            itemShow.setUpgrade((byte) (upgradePoints));
            itemShow.strOption = Item.creatOption(summedOptions.toArray(new ItemOption[0]));
            getService().showCaiTrangGhep(itemShow, caiTrang.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void itemBagToBody2(Message mss) {
        lockItem.lock();
        try {
            short indexUI = mss.reader().readShort();
            if (indexUI < 0 || indexUI >= numberCellBag) {
                return;
            }
            Item item = bag[indexUI];
            if (item != null && item.isTrangBi()) {
                byte index = item.getTemplate().type;
                if (item.getTemplate().typeChar == sys || item.getTemplate().typeChar == 0) {
                    if (body2[index] != null) {
                        Item it = body2[index].clone();
                        it.index = indexUI;
                        it.isLock = true;
                        item.isLock = true;
                        body2[index] = item;
                        bag[indexUI] = it;
                    } else {
                        body2[index] = item;
                        bag[indexUI] = null;
                    }
                } else {
                    getService().serverMessage("Trang bị không phù hợp với nhân vật");
                    return;
                }
                Log.debug(increaseExtra);
                setAbility();
                getService().updateMPFull();
                getService().updateHPFull();
                getService().showInfo2(this);
                getService().updateItemBody2(indexUI);
                getService().updateIndexBag();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void itemBody2ToBag(Message ms) {
        lockItem.lock();
        try {
            byte indexUI = ms.reader().readByte();
            Item item = body2[indexUI];
            if (item != null) {
                for (int i = 0; i < numberCellBag; i++) {
                    if (bag[i] == null) {
                        item.isLock = true;
                        item.index = i;
                        bag[i] = item;
                        body2[indexUI] = null;
                        getService().itemBody2ToBag(indexUI, i);
                        if (item.isExpired()) {
                            removeItem(i, 1, true);
                        }
                        setAbility();
                        getService().updateHP();
                        getService().updateMP();
                        return;
                    }
                }
                warningBagFull();
            } else {
                Log.debug("item null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }

    }

    public void itemBodyChange(Message ms) {
        try {
            int indexChangeItemBody = ms.reader().readByte();
            int indexChange = -1;
            if (indexChangeItemBody == 0) {
                indexChange = 2;
            } else if (indexChangeItemBody == 1) {
                indexChange = 4;
            } else if (indexChangeItemBody == 2) {
                indexChange = 6;
            } else if (indexChangeItemBody == 3) {
                indexChange = 8;
            } else if (indexChangeItemBody == 4) {
                indexChange = 14;
            }
            if (indexChange == -1) {
                return;
            }
            if (body[indexChange] == null || body2[indexChange] == null) {
                return;
            }
            Item item1 = body[indexChange].clone();
            Item item2 = body2[indexChange].clone();
            if (item1 != null && item2 != null) {
                body[indexChange] = item2;
                body2[indexChange] = item1;
                getService().updateItemBodyChange();
                setAbility();
                if (item2.getTemplate().type == 14) {
                    if (this.options[327] > 0) {
                        int point = 1;
//                        if (this.options[327] == 93) {
//                            point = 3;
//                        }
//                        if (this.options[327] == 62) {
//                            point = 2;
//                        }
                        Skill skill = SkillFactory.getInstance().getSkill(this.options[327], point);
                        if (skill != null) {
                            int indexSkillWithCT = this.findIndexSkillWithCT();
                            if (indexSkillWithCT != -1) {
                                this.skills.remove(indexSkillWithCT);
                            }
                            skill.isSkillCaiTrang = true;
                            skill.timeCoolDown = this.timeCoolDownSkillCaiTrang;
                            this.addSkill(skill);
                            this.getService().updateSkillCaiTrang();
                            this.getService().updateSkill();
                        }
                    } else {
                        int indexSkillWithCT = this.findIndexSkillWithCT();
                        if (indexSkillWithCT != -1) {
                            this.skills.remove(indexSkillWithCT);
                            this.getService().updateSkill();
                        }

                    }
                }
                getService().updateHP();
                getService().updateMP();
                getService().showInfo2(this);
                zone.getService().updateItemBody(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCaiTrangTach(Message mss) {
        try {
            byte type = mss.reader().readByte();
            short index = mss.reader().readShort();
            Item item = checkBag(type)[index];
            if (item != null) {
                if (item.getOptionGhepCaiTrang() != null && !item.getOptionGhepCaiTrang().isEmpty()) {
                    String[] itemData = item.getOptionGhepCaiTrang().split("&");
                    List<Item> items = new ArrayList<>();
                    for (String str : itemData) {
                        String[] data = str.split("@");
                        Item item1 = new Item(Integer.parseInt(data[0]), "tach");
                        item1.setStrOption(data[1]);
                        items.add(item1);
                    }
                    getService().showCaiTrangTach(items);
                }
            }
        } catch (Exception e) {
            Log.debug("error showCaiTrangTach: " + e.getMessage());
        }
    }

    public void upPearl(Message ms) {
        lockItem.lock();
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            boolean isCoin = ms.reader().readBoolean();
            Set<Short> indexes = new HashSet<>();
            int size = ms.reader().readByte();

            long exp = 0;
            for (int i = 0; i < size; i++) {
                short index = ms.reader().readShort();
                if (index < 0 || index >= numberCellBag) {
                    continue;
                }
                indexes.add(index);
            }
            Vector<Item> crystals = new Vector<>();
            for (int index : indexes) {
                if (bag[index] != null && bag[index].id <= 11) {
                    if (bag[index].id == 9) {
                        serverDialog("Max cấp đá");
                        return;
                    }
                    exp += DataCenter.gI().UP_CRYSTAL[bag[index].id];
                    crystals.add(bag[index]);
                }
            }
            if (size < 2) {
                serverDialog("Chọn ít nhất 2 viên đá");
                return;
            }
            int currentId = 0;
            if (exp > 0) {
                for (currentId = DataCenter.gI().UP_CRYSTAL.length - 1; currentId >= 0; currentId--) {
                    if (exp > DataCenter.gI().UP_CRYSTAL[currentId]) {
                        break;
                    }
                }
            }
            if (currentId >= DataCenter.gI().UP_CRYSTAL.length - 1) {
                currentId = DataCenter.gI().UP_CRYSTAL.length - 2;
            }
            int nextId = currentId + 1;
            int percent = (int) (exp * 100 / DataCenter.gI().UP_CRYSTAL[nextId]);
            if (percent < 20) {
                serverDialog("Tỉ lệ trên 60% mới có thể luyện");
                return;
            }
            int coin = DataCenter.gI().COIN_UP_CRYSTAL[nextId];
            int ballz = DataCenter.gI().COIN_UP_CRYSTAL[nextId] / 3;
            if (isCoin) {
                if (coin > this.coin) {
                    serverDialog("Không đủ Xu");
                    return;
                }
                addCoin(-coin, true);
            } else {
                if (ballz > this.ballZ) {
                    serverDialog("Không đủ Ballz");
                    return;
                }
                addBallz(-ballz, true);
            }
            for (Item item : crystals) {
                removeItem(item.index, 1, true);
            }
            Item da = null;
            if (Util.nextInt(100) <= percent) {
                da = new Item(nextId, "dau");
                da.isLock = true;
            } else {
                da = new Item(currentId, "dau");
                da.isLock = true;
            }
            addItemToBag(da);
            getService().upPearl(crystals, da);
            getService().updateIndexBag();
        } catch (Exception ex) {
            Log.error("luyen da err: " + ex.getMessage(), ex);
        } finally {
            lockItem.unlock();
        }
    }

    public void ducLoKham(Message ms) {
        try {
            byte type = ms.reader().readByte();
            short index = ms.reader().readShort();
            byte slot = ms.reader().readByte();
            Item item = checkBag(type)[index];
            if (item == null) {
                return;
            }
            ItemOption[] options = item.getItemOption();
            if (options == null || options.length == 0) {
                return;
            }
            short var14 = 349;
            byte var4 = 4;
            byte var5 = 3;
            ItemOption var6 = null;
            ItemOption var7 = null;
            ItemOption optionKham = null;
            switch (slot) {
                case 1:
                    var14 = 350;
                    var4 = 7;
                    var5 = 6;
                    break;
                case 2:
                    var14 = 298;
                    var4 = 5;
                    var5 = 4;
            }
            Vector var8 = new Vector();
            int var9;
            if (options != null) {
                for (var9 = 0; var9 < options.length; var9++) {
                    ItemOption op = options[var9];
                    if (op.option[0] == 349) {
                        var7 = op;
                    }

                    if (op.option[0] == var14) {
                        optionKham = op;
                    }

                    ItemOption var10;
                    if ((var10 = op).option[0] == 159 || var10.option[0] == 165 || var10.option[0] == 163 || var10.option[0] == 164 || var10.option[0] == 148) {
                        var6 = op;
                    } else {
                        var8.add(op);
                    }
                }

            }
            if (optionKham == null) {
                optionKham = new ItemOption(var14 + ",0,1");
                if (slot == 0) {
                    var8.insertElementAt(optionKham, 0);
                } else if (slot == 1) {
                    if (var7 == null) {
                        var8.insertElementAt(optionKham, 0);
                    } else {
                        for (int var15 = var9 = var8.indexOf(var7); var15 < options.length; ++var15) {
                            if (options[var15].getItemOptionTemplate().type == 17) {
                                var9 = var15;
                            }
                        }

                        ++var9;
                        var8.insertElementAt(optionKham, var9);
                    }
                } else if (slot == 2) {
                    var8.add(optionKham);
                }
            } else if (optionKham.f() < var4) {
                optionKham.e(optionKham.f() + 1);
            } else {
                optionKham = null;
            }
            if (var6 != null) {
                var8.add(var6);
            }
            int coint = 0;
            if (optionKham != null && optionKham.f() < var5) {
                coint = 250000;
            } else {
                coint = 250000;
            }

            if (coin < coint) {
                serverDialog("Không đủ Xu");
                return;
            }
            addCoin(-coint, true);
            item.strOption = Item.creatOption(var8);
            getService().ducLoItem(type, index, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void khamNgoc(Message m) {
        lockItem.lock();
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            byte type = m.reader().readByte();
            short index = m.reader().readShort();
            Vector<Item> ngoc = new Vector<>();
            boolean[] list = new boolean[numberCellBag];
            while (m.reader().available() > 0) {
                int indexNgoc = m.reader().readShort();
                if (!list[indexNgoc]) {
                    list[indexNgoc] = true;
                    if (indexNgoc < numberCellBag) {
                        list[indexNgoc] = true;
                        Item item = bag[indexNgoc];
                        if (item != null) {
                            ngoc.add(item);
                        }
                    }
                }
            }
            Item item = checkBag(type)[index].clone();
            if (item == null) {
                return;
            }
//            if (UpgradeHandler.isGodEquipment(item)) {
//                serverDialog("Không thể khảm vào trang bị này!");
//                return;
//            }
            ItemOption[] options = item.getItemOption();
            if (options == null || options.length == 0) {
                return;
            }

            int quantity = 0;
            Vector<ItemOption> gems;
            ItemOption var6;
            int maxGems;
            if (ngoc.get(0).getTemplate().type == 35) {

            } else if (ngoc.get(0).getTemplate().type == 36) {

            } else {
                boolean var1 = true;
                int var2 = -1;
                int var3;
                for (var3 = 0; var3 < ngoc.size(); var3++) {
                    if (ngoc.get(var3) != null) {
                        if (var2 == -1) {
                            var2 = ngoc.get(var3).id;
                        } else if (var2 != ngoc.get(var3).id) {
                            var1 = false;
                            break;
                        }
                        quantity += ngoc.get(var3).M();
                    }
                }
                gems = new Vector<>();
                ItemOption var4;
                if ((var4 = item.a(gems, var2)) != null) {
                    if (var4.a(item.u())) {
                        var1 = false;
                    }
                } else {
                    maxGems = 0;
                    var6 = null;
                    ItemOption[] var17 = item.options();
                    Vector<ItemOption> var7 = new Vector<>();
                    int var8;
                    if (var17 != null) {
                        for (var8 = 0; var8 < var17.length; ++var8) {
                            var7.add(var17[var8]);
                            if (var17[var8].option[0] == 298) {
                                var6 = var17[var8];
                                maxGems = var17[var8].f();
                            }
                        }
                    }

                    if (gems.size() >= maxGems) {
                        var1 = false;
                    } else if (var2 >= 0) {
                        var7.insertElementAt(ItemOption.g(var2), var7.indexOf(var6) + 1);
                        var8 = 0;

                        for (var3 = 0; var3 < var7.size(); ++var3) {
                            if (var7.get(var3).getItemOptionTemplate().type == 8) {
                                ++var8;
                            }
                        }

                        var6.setValue(var8);
                        item.strOption = Item.creatOption(var7);
                    }
                }
                if (!var1) {
                    return;
                }
                if (quantity > 0) {
                    item.a(quantity, var2);
                }
                for (Item value : ngoc) {
                    removeItem(value.index, value.getQuantity(), true);
                }
                item.isLock = true;
                checkBag(type)[index] = item;
                getService().khamNgoc(ngoc, item, type);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void tachKham(Message ms) {
        lockItem.lock();
        try {
            byte type = ms.reader().readByte();
            short index = ms.reader().readShort();
            byte select = ms.reader().readByte();

            Item[] items = checkBag(type);
            if (index < 0 || index >= items.length) {
                return;
            }

            Item item = items[index];
            if (item == null) {
                return;
            }

            Vector<ItemOption> R = new Vector<>();
            List<Item> itemTach = new ArrayList<>();
            ItemOption[] var11 = item.options();
            if (var11 != null) {
                for (ItemOption itemOption : var11) {
                    if (itemOption.type8()) {
                        R.add(itemOption);
                    }
                }
            }

            int zeni = 0;
            Vector<ItemOption> var2 = new Vector<>();
            Vector<ItemOption> newItemOptions = new Vector<>();

            if (var11 != null) {
//                for (ItemOption itemOption : var11) {
//                    if (itemOption.m()) {
//                        newItemOptions.add(itemOption);
//                    }
//                }

                ItemOption var13 = null;
                ItemOption var5 = null;
                ItemOption var6 = null;

                if (select == 0) {
                    var2.addAll(R);

                    for (ItemOption itemOption : var11) {
                        if (itemOption.k()) {
                            itemOption.setValue(0);
                            newItemOptions.add(itemOption);
                        } else if (!itemOption.type8()) {
                            newItemOptions.add(itemOption);
                        }
                    }
                } else {
                    if (select > 0 && select <= R.size()) {
                        var2.add(R.get(select - 1));
                    } else {
                        return;
                    }

                    int countSelected = 0;
                    for (ItemOption itemOption : var11) {
                        if (itemOption.getItemOptionTemplate().id == 349) {
                            var13 = itemOption;
                        } else if (itemOption.getItemOptionTemplate().id == 350) {
                            var5 = itemOption;
                        } else if (itemOption.getItemOptionTemplate().id == 298) {
                            var6 = itemOption;
                        }

                        if (itemOption.type8()) {
                            countSelected++;
                            if (countSelected != select) {
                                newItemOptions.add(itemOption);
                            } else {
                                if (var13 != null && itemOption.getItemOptionTemplate().type == 17) {
                                    var13.setValue(var13.option[1] - 1);
                                }
                                if (var5 != null && itemOption.j()) {
                                    var5.setValue(var5.option[1] - 1);
                                }
                                if (var6 != null && itemOption.getItemOptionTemplate().type == 8) {
                                    var6.setValue(var6.option[1] - 1);
                                }
                            }
                        } else if (!newItemOptions.contains(itemOption)) {
                            newItemOptions.add(itemOption);
                        }
                    }
                }

                for (ItemOption itemOption : var11) {
                    if ((itemOption.option[0] == 370 || itemOption.getItemOptionTemplate().type == 18) && !newItemOptions.contains(itemOption)) {
                        newItemOptions.add(itemOption);
                    }
                }

                Vector<Item> var18 = new Vector<>();
                for (ItemOption itemOption : var2) {
                    int var1 = itemOption.h();
                    if (var1 >= 0) {
                        if (itemOption.getItemOptionTemplate().type != 17 && !itemOption.j()) {
                            if (itemOption.getItemOptionTemplate().type == 8) {
                                int var4 = itemOption.option[3];
                                int var15 = 0;
                                for (int var16 = 0; var16 <= var4; ++var16) {
                                    var15 += DataCenter.gI().ar[var16];
                                }
                                var18.add(new Item(var1, true, var15, "tach"));
                                Item var10 = new Item(var1, "tach");
                                var10.setQuantity(var15);
                                itemTach.add(var10);
                            }
                        } else {
                            Item var14 = new Item(var1, true, "tach");
                            var14.strOption = itemOption.g();
                            var18.add(var14);
                        }
                    }
                }

                int var8 = 0;
                for (int i = 0; i < var18.size(); ++i) {
                    Item var12 = var18.get(i);
                    if (var12.getTemplate().type == 32) {
                        zeni += var12.M();
                    } else {
                        var8 += var12.M();
                    }

                    if (i >= 16) {
                        break;
                    }
                }
                zeni = zeni / 200 + var8;
                if (this.zeni < zeni) {
                    serverDialog("Không đủ Zeni");
                    return;
                }
                if (getSlotNull() < itemTach.size()) {
                    warningBagFull();
                    return;
                }
                addZeni(-zeni, true);
                item.strOption = Item.creatOption(newItemOptions);
                for (Item item1 : itemTach) {
                    addItemToBag(item1);
                    getService().addItem(item1);
                }
                getService().tachKham(item, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void convertUpgrade(Message ms) {
        lockItem.lock();
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            byte type = ms.reader().readByte();
            short index = ms.reader().readShort();
            byte _type = ms.reader().readByte();
            short _index = ms.reader().readShort();
            short indexConvert = ms.reader().readShort();
            Item item = checkBag(type)[index];
            Item item2 = checkBag(_type)[_index];
            Item convert = bag[indexConvert];
            if (item == null || item2 == null || convert == null) {
                Log.debug("item null");
                return;
            }
            if (item.getTemplate().type != item2.getTemplate().type) {
                service.serverMessage("Không thể dịch chuyển 2 trang bị khác loại");
                return;
            } else if (item.getUpgrade() > 14 && convert.id != 158) {
                service.serverMessage("Trang bị từ +15 trở lên phải dùng Bùa Dịch Chuyển (cao cấp)");
                return;
            } else if (item.getUpgrade() > 10 && convert.id != 158 && convert.id != 157) {
                service.serverMessage("Trang bị từ +11 trở lên phải dùng Bùa Dịch Chuyển (trung cấp).");
                return;
            }
            if (convert.getTemplate().type == 25) {
                byte upgrade = item.upgrade;
                byte upgrade2 = item2.upgrade;
                item2.upgrade = 0;
                item2.upgradeItem(upgrade);
                item2.isLock = true;
                item.upgradeItem(upgrade2);
                item.isLock = true;
                removeItem(convert.index, convert.getQuantity(), true);
                getService().convertUpgrade(type, item, _type, item2, indexConvert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void splitItem(Message ms) {
        lockItem.lock();
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            byte type = ms.reader().readByte();
            short index = ms.reader().readShort();
            Item item = checkBag(type)[index];
            if (item == null) {
                return;
            }
            if (item.getQuantity() < 1) {
                getService().serverMessage("Không thể tách vật phẩm này!");
                return;
            }
            if (!item.isTrangBi()) {
                getService().serverMessage("Vật phẩm không phải trang bị!");
                return;
            }
            Item item1 = item.clone();
            item1.upgradeItem(0);
            item1.isLock = item.isLock;
            long exp = 0L;
            int var3 = 0;
            int var4;
            List<Item> da = new ArrayList<>();
            if (item.isTypePet()) {
                for (var4 = item.getUpgrade(); var4 > 0; --var4) {
                    var3 += DataCenter.gI().ao[var4];
                    exp += DataCenter.gI().at[var4];
                }
            } else if (item.isTypeClothe()) {
                for (var4 = item.getUpgrade(); var4 > 0; --var4) {
                    var3 += DataCenter.gI().ap[var4];
                    exp += DataCenter.gI().au[var4];
                }
            } else if (item.isTypeWeapon()) {
                for (var4 = item.getUpgrade(); var4 > 0; --var4) {
                    var3 += DataCenter.gI().aq[var4];
                    exp += DataCenter.gI().av[var4];
                }
            }

            ItemOption[] options = item1.options();
            if (options != null) {
                for (ItemOption itemOption : options) {
                    if (itemOption.type8()) {
                        item1.isLock = true;
                        break;
                    }
                }
            }

            exp /= 3L;
            Vector var8 = new Vector();

            for (var4 = DataCenter.gI().UP_CRYSTAL.length - 1; var4 >= 0 && exp > 0L; --var4) {
                if (exp >= DataCenter.gI().UP_CRYSTAL[var4]) {
                    exp -= DataCenter.gI().UP_CRYSTAL[var4];
                    Item crystal = new Item(var4, "tach");
                    crystal.id = var4;
                    crystal.expire = -1L;
                    crystal.isLock = true;
                    var8.add(crystal);
                    var4 = DataCenter.gI().UP_CRYSTAL.length;
                    if (var8.size() >= 16) {
                        break;
                    }
                }
            }

            for (var4 = 0; var4 < var8.size(); ++var4) {
                Item item2 = (Item) var8.elementAt(var4);
                da.add(item2);
            }
            if (getSlotNull() < da.size()) {
                warningBagFull();
                return;
            }
            int coin = var3 / 3;
            addCoin(coin, true);
            for (Item item2 : da) {
                addItemToBag(item2);
                getService().addItem(item2);
            }
            checkBag(type)[index] = item1;
            getService().splitItem(type, item1);
            getService().updateIndexBag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void removeMail(Message ms) {
        try {
            short size = ms.reader().readShort();
            for (int i = 0; i < size; i++) {
                short id = ms.reader().readShort();
                mailManager.removeMail(id);
            }
        } catch (Exception e) {

        }
    }

    public void reciveAllMail() {
        mailManager.receiveAll();
    }

    public void throwItem(Message ms) {
        lockItem.lock();
        try {
            if (isDead) {
                return;
            }
            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            if (trade != null) {
                warningTrade();
                return;
            }
            short indexUI = ms.reader().readShort();
            if (indexUI < 0 || indexUI >= this.numberCellBag || this.bag[indexUI] == null || this.bag[indexUI].isLock) {
                return;
            }
            Item item = bag[indexUI];
            if (item != null) {
                Util.writing("loggame/dropitem/" + name + ".txt", "Vứt vật phẩm: " + item.getTemplate().name + " số lượng: " + item.getQuantity() + "\n");
                removeItem(indexUI, item.getQuantity(), true);// bo ra dat mat luon vat pham
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        } finally {
            lockItem.unlock();
        }

    }

    public void inputNumberSplit(Message ms) {
        try {
            if (trade != null) {
                warningTrade();
                return;
            }
            short indexItem = ms.reader().readShort();
            int numSplit = ms.reader().readInt();
            if (numSplit < 1) {
                return;
            }
            if (bag[indexItem] != null && bag[indexItem].template.isXepChong) {
                int quantity = bag[indexItem].getQuantity();
                if (numSplit >= quantity) {
                    return;
                }
                for (int i = 0; i < this.numberCellBag; i++) {
                    if (bag[i] == null) {
                        bag[i] = bag[indexItem].clone();
                        bag[i].index = i;
                        bag[i].setQuantity(numSplit);
                        getService().themItem(bag[i]);
                        bag[indexItem].reduce(numSplit);
                        getService().splitItemBag((short) indexItem, bag[indexItem].getQuantity(), (short) i, numSplit);
                        getService().updateIndexBag();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void saleItem(Message ms) {
        try {
            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            if (trade != null) {
                warningTrade();
                return;
            }
            short index = ms.reader().readShort();
            boolean var = ms.reader().readBoolean();
            if (index < 0 || index >= numberCellBag || bag[index] == null) {
                return;
            }
            Item item = bag[index];
            if (item == null) {
                return;
            }
            if (item.getUpgrade() > 0) {
                service.serverMessage("Không thể bán trang bị đã nâng cấp");
                return;
            }
            int quantity = item.getQuantity();
            int price = item.getPrice() * quantity;
//            if (item.isLock) {
            addCoin(price, true);
//            } else {
//                addBallz(price, true);
//            }
            Util.writing("loggame/saleitem/" + name + ".txt", "Bán vật phẩm: " + item.getTemplate().name + " số lượng: " + item.getQuantity() + "\n");
            removeItem(index, quantity, true);
            getService().saleItem(index);
        } catch (Exception e) {
        }
    }

    public int checkReward(Reward reward) {
        com.vdtt.model.Reward rw = findReward(reward.type);
        switch (reward.type) {
            case 0:
                int time = timeOnline / 60_000;
                if (reward.required > time) {
                    return 0;
                } else {
                    if (rw != null) {
                        if (rw.list.contains(reward.id)) {
                            return 2;
                        }
                    }
                    return 1;
                }
            case 1:
                if (reward.required > ngayOnline) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 2:
                if (reward.required > totalKiNang) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 3:
                if (!packSieuQuai) {
                    return 0;
                }
                if (reward.required > totalSQ) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 5:
                if (reward.required > napNgay) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 15:
                if (!packOnline) {
                    return 0;
                }
                time = (int) (totalTimeOnline / 60000);
                if (reward.required > time) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 16:
                if (!packTinhAnh) {
                    return 0;
                }
                if (reward.required > totalTA) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 17:
                if (!packThuLinh) {
                    return 0;
                }
                if (reward.required > totalTL) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 21:
                if (reward.required > mocNap) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 6:
                if (reward.required > getZeniSpentDay()) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
            case 8:
                if (reward.required > getZeniSpentWeek()) {
                    return 0;
                }
                if (rw != null) {
                    if (rw.list.contains(reward.id)) {
                        return 2;
                    }
                }
                return 1;
        }
        return 0;
    }

    public void claimedReward(Message ms) {
        try {
            short id = ms.reader().readShort();
            Reward dailyReward = RewardManager.gI().find(id);
            if (dailyReward != null) {
                if (getSlotNull() < 1) {
                    warningBagFull();
                    return;
                }
                if (checkReward(dailyReward) == 1) {
                    com.vdtt.model.Reward reward = findReward(dailyReward.type);
                    if (reward != null) {
                        if (reward.list.contains(dailyReward.id)) {
                            return;
                        }
                        reward.list.add(dailyReward.id);
                    } else {
                        com.vdtt.model.Reward rw = new com.vdtt.model.Reward();
                        rw.type = (short) dailyReward.type;
                        rw.list.add(dailyReward.id);
                        rewardList.add(rw);
                    }
                    Item item = dailyReward.item.clone();
                    if (item.expire > 0) {
                        item.expire += System.currentTimeMillis();
                    }
                    if (item.id == 163) {
                        addCoin(item.getQuantity(), true);
                    } else if (item.id == 191) {
                        addBallz(item.getQuantity(), true);
                    } else {
                        addItemToBag(item);
                        getService().addItem(item);
                    }
                    getService().updatePhucLoi(id);
                }
            }
        } catch (Exception e) {

        }

    }

    public void drawRewardDaily() {
        if (countVongQuay < 1) {
            service.serverMessage("Không đủ lượt quay");
            return;
        }
        countVongQuay--;
        int id = RandomItem.VONG_QUAY_PHUC_LOI.next();
        long time = System.currentTimeMillis() + 2000;
        getService().drawRewardDaily(id, 0);
        while (time > System.currentTimeMillis()) {

        }
        switch (id) {
            case 0:
                Item item = new Item(23, "dayli");
                item.setLock(true);
                item.setQuantity(30);
                addItemToBag(item);
                getService().addItem(item);
                break;
            case 1:
                item = new Item(5000, "dayli");
                item.setLock(true);
                item.setQuantity(60);
                addItemToBag(item);
                getService().addItem(item);
                break;
            case 2:
                addBallz(900000, true);
                break;
            case 3:
                addCoin(4000000, true);
                break;
            case 4:
                item = new Item(160, "dayli");
                item.setLock(true);
                item.setQuantity(600);
                addItemToBag(item);
                getService().addItem(item);
                break;
            case 5:
                item = new Item(160, "dayli");
                item.setLock(true);
                item.setQuantity(725);
                addItemToBag(item);
                getService().addItem(item);
                break;
            case 6:
                item = new Item(160, "dayli");
                item.setLock(true);
                item.setQuantity(850);
                addItemToBag(item);
                getService().addItem(item);
                break;
            case 7:
                item = new Item(160, "dayli");
                item.setLock(true);
                item.setQuantity(1000);
                addItemToBag(item);
                getService().addItem(item);
                break;
        }
    }

    public void buyPackReward(Message ms) {
        try {
            byte id = ms.reader().readByte();
            if (zeni < 5000) {
                service.serverDialog("Bạn Cần 5k zenni Để Đầu Tư");
                return;
            }
            addZeni(-5000, true);
            switch (id) {
                case 0:
                    packOnline = true;
                    break;
                case 1:
                    packTinhAnh = true;
                    break;
                case 2:
                    packThuLinh = true;
                    break;
                case 3:
                    packSieuQuai = true;
                    break;
            }
            getService().reloadRewarDaily();
        } catch (Exception e) {

        }
    }

    public void extendBox() {
        if (trade != null) {
            warningTrade();
            return;
        }
        if (zeni < 90) {
            service.serverMessage("Không đủ Zeni");
            return;
        }
        if (numberCellBox >= 108) {
            serverDialog("Không thể mở rộng thêm");
            return;
        }
        addZeni(-90, true);
        numberCellBox += 9;
        Item[] items = new Item[numberCellBox];
        for (int i = 0; i < box.length; i++) {
            items[i] = box[i];
        }
        box = items;
        getService().openBox();
        getService().serverMessage("Mở rộng thêm 9 ô thành công");
    }

    public void createGroup() {
        try {
            if (this.group != null) {
                serverDialog("Không thể tạo nhóm");
                return;
            }
            Group group = new Group();
            MemberGroup party = new MemberGroup();
            party.charId = this.id;
            party.classId = this.idCharSys;
            party.name = this.name;
            party.setChar(this);
            group.add(party);
            this.group = group;
            this.group.getGroupService().playerInParty();
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void outParty() {
        if (this.group != null) {
            synchronized (group) {
                if (this.group.memberGroups.size() > 1) {
                    int index = this.group.getIndexById(this.id);
                    if (index != -1) {
                        this.group.removeParty(index);
                    }
                }
                this.group = null;
                getService().outParty();
            }
        }
    }

    public void lockParty() {
        try {
            if (this.group != null && this.group.memberGroups.get(0).charId == this.id) {
                this.group.isLock = !this.group.isLock;
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void openFindParty() {
        if (this.group == null) {
            try {
                HashMap<String, Group> groups = new HashMap<>();
                List<Char> chars = zone.getChars();
                for (Char _char : chars) {
                    if (_char != null && _char.group != null) {
                        groups.put(_char.group.memberGroups.get(0).name, _char.group);
                    }
                }
                getService().openFindParty(groups);
            } catch (Exception ex) {
                Log.error("err: " + ex.getMessage(), ex);
            }
        }
    }

    public void addParty(Message msg) {
        try {
            if (isDead) {
                return;
            }
            String name = msg.readUTF();
            if (this.name.equals(name)) {
                return;
            }
            Char _char = ServerManager.findCharByName(name);
            if (_char != null) {
                if (this.group == null) {
                    createGroup();
                } else {
                    MemberGroup party = this.group.memberGroups.get(0);
                    if (party.charId != this.id) {
                        service.serverMessage("Bạn không phải trưởng nhóm");
                        return;
                    }
                }
                if (_char.group != null) {
                    service.serverMessage("Người này đã gia nhập nhóm khác");
                    return;
                }
                Invite.PlayerInvite p = _char.invite.findCharInvite(Invite.NHOM, this.id);
                Invite.PlayerInvite c = this.invite.findCharInvite(Invite.XIN_VAO_NHOM, _char.id);
                if (c != null) {
                    acceptPleaseParty(name);
                    return;
                }
                if (p != null) {
                    service.serverDialog("Không thể mời vào nhóm liên tục. Vui lòng thử lại sau 30s nữa.");
                    return;
                }
                _char.invite.addCharInvite(Invite.NHOM, this.id, 30);
                _char.getService().partyInvite(this.name);
            } else {
                service.serverMessage("Người này không tồn tại hoặc không online.");
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void addPartyAccept(Message msg) {
        try {
            if (this.group == null) {
                String name = msg.readUTF();
                Char _char = ServerManager.findCharByName(name);
                if (_char != null) {
                    if (_char.group == null) {
                        service.serverMessage("Nhóm này không còn tồn tại");
                        return;
                    }
                    MemberGroup p = _char.group.memberGroups.get(0);
                    if (p.charId != _char.id) {
                        service.serverMessage("Người này không phải nhóm trưởng");
                        return;
                    }
                    Invite.PlayerInvite c = this.invite.findCharInvite(Invite.NHOM, _char.id);
                    if (c == null) {
                        pleaseInputParty(name);
                        return;
                    }
                    MemberGroup party = new MemberGroup();
                    party.charId = this.id;
                    party.classId = this.idCharSys;
                    party.name = this.name;
                    party.setChar(this);
                    _char.group.add(party);
                    this.group = _char.group;
                    this.group.getGroupService().playerInParty();
                } else {
                    service.serverMessage("Hiện tại người này không online.");
                }
            } else {
                service.serverMessage("Bạn đã gia nhập nhóm khác");
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void acceptPleaseParty(String name) {
        if (this.group == null) {
            return;
        }
        if (this.group.memberGroups.size() < 6) {
            if (this.group.memberGroups.get(0).charId == this.id) {
                try {
                    Char _char = ServerManager.findCharByName(name);
                    if (_char != null) {
                        if (_char.group == null) {
                            Invite.PlayerInvite c = this.invite.findCharInvite(Invite.XIN_VAO_NHOM, _char.id);
                            if (c == null) {
                                service.serverDialog("Đã hết thời gian chấp nhận yêu cầu tham gia nhóm.");
                                return;
                            }
                            _char.group = this.group;
                            MemberGroup p = new MemberGroup();
                            p.charId = _char.id;
                            p.classId = _char.idCharSys;
                            p.name = _char.name;
                            p.setChar(_char);
                            _char.group.add(p);
                        } else {
                            service.serverDialog("Người này đã gia nhập nhóm khác");
                        }
                    } else {
                        service.serverDialog("Người này không online");
                    }
                } catch (Exception ex) {
                    Log.error("err: " + ex.getMessage(), ex);
                }
            }
        } else {
            service.serverDialog("Nhóm đã đủ thành viên");
        }
    }

    public void pleaseInputParty(String name) {
        try {
            if (this.group != null) {
                return;
            }
            if (this.name.equals(name)) {
                return;
            }
            Char _char = ServerManager.findCharByName(name);
            if (_char != null) {
                if (_char.group != null) {
                    if (_char.group.isLock) {
                        service.serverMessage("Nhóm này đã khóa, không thể xin gia nhập");
                        return;
                    }
                    if (_char.group.memberGroups.size() == 6) {
                        service.serverMessage("Nhóm đã đủ thành viên");
                        return;
                    }
                    if (_char.group.memberGroups.get(0).charId != _char.id) {
                        return;
                    }
                    Invite.PlayerInvite p = _char.invite.findCharInvite(Invite.XIN_VAO_NHOM, this.id);
                    if (p != null) {
                        service.serverMessage("Không thể xin vào nhóm liên tục. Vui lòng thử lại sau 30s nữa.");
                        return;
                    }
                    _char.invite.addCharInvite(Invite.XIN_VAO_NHOM, this.id, 30);
                    _char.getService().pleaseInputParty(this.name);
                } else {
                    service.serverMessage("Nhóm này không tồn tại");
                }
            } else {
                service.serverMessage("Người này không online hoặc không tồn tại");
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void moveMember(Message ms) {
        try {
            if (this.group != null) {
                if (this.group.memberGroups.get(0).charId == this.id) {

                    String name = ms.readUTF();
                    if (this.name.equals(name)) {
                        service.serverMessage("Không thể kick bản thân");
                    }
                    List<MemberGroup> partys = this.group.getMemberGroup();
                    Char _char = null;
                    for (MemberGroup p : partys) {
                        if (p.name.equals(name)) {
                            _char = p.getChar();
                        }
                    }
                    if (_char != null) {
                        _char.outParty();
                        _char.service.serverMessage("Bạn đã bị đuổi khỏi nhóm");
                    }
                    // _char.sendMessage(new Message(83));
                    // this.group.removeParty(index);
                } else {
                    service.serverMessage("Bạn không phải trưởng nhóm");
                }
            }

        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void changeTeamLeader(Message ms) {
        try {
            if (isDead) {
                return;
            }
            if (this.group != null) {
                if (this.group.memberGroups.get(0).charId == this.id) {
                    String name = ms.readUTF();
                    if (this.name.equals(name)) {
                        service.serverMessage("Không thể nhường cho người này");
                    }
                    int index = this.group.getIndexByName(name);
                    if (index > 0) {
                        this.group.changeLeader(index);
                    }
                } else {
                    service.serverMessage("Bạn không phải trưởng nhóm");
                }
            }

        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public synchronized void checkDoneTask() {
        if (taskMain != null && taskMain.isComplete()) {
            updateTask();
        }
    }

    public void checkInfo(Message mss) {
        try {
            if (isDead) {
                return;
            }
            Char _char = ServerManager.findCharByName(mss.readUTF());
            if (_char != null) {
                if (_char.id == this.id) {
                    return;
                }
                service.xemThongTin(_char);
                _char.service.serverMessage(name + " đã xem thông tin của bạn");
            } else {
                service.serverMessage("Người chơi này không online");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chatPrivate(Message mss) {
        try {
            String str = mss.readUTF();
            String[] parts = str.split(":"); // Tách chuỗi dựa vào ký tự ':'
            if (parts.length < 2) {
                getService().serverMessage("Nội dung không đúng");
                return;
            }

            // Kiểm tra xem chuỗi có đủ dài để thực hiện substring(1) hay không
            if (parts[0].length() < 2) {
                getService().serverMessage("Tên người nhận không hợp lệ");
                return;
            }

            String name = parts[0].substring(1); // Loại bỏ ký tự '/' đầu tiên và lấy phần còn lại
            Char _char = ServerManager.findCharByName(name);
            if (_char == null || this.name.equals(name)) {
                getService().serverMessage("Đối phương đã offline");
                return;
            }

            _char.getService().chatPrivate(this.name, parts[1], name);
            getService().chatPrivate(this.name, parts[1], name);
        } catch (Exception e) {

        }
    }

    public void setTypePk(byte typePk) {
        this.typePK = typePk;
        if (zone != null) {
            zone.getService().changePk(this);
        }
    }

    public void attackChar(int id, byte index) {
        try {
            if (taskId < 2) {
                getService().serverMessage("Tới nhiệm vụ thu thập thịt khủng long mới có thể dùng kỹ năng");
                return;
            }
            boolean isPhoBan = false;
            if (zone != null) {
                isPhoBan = zone.map.mapTemplate.isPhoBan();
            }
            if (!isPhoBan && isProtected) {
                return;
            }
            Char pl = ServerManager.findCharById(id);
            if (pl != null) {
                if (!isPhoBan && pl.isProtected) {
                    return;
                }
                if (zone instanceof ZPowerTournament && pl.getGroup().equals(this.getGroup())) {
                    getService().serverMessage("Cùng nhóm mà bạn ơi");
                    return;
                }
                if (zone instanceof ZPlanetBlackStar && pl.clan.equals(this.clan)) {
                    getService().serverMessage("Cùng vũ trụ mà bạn ơi");
                    return;
                }
                if (pet != null && pet.typePet == 1 && !pet.isDead) {
                    pet.attackChar(id);
                }
                Skill selectedSkill = skills.get(index);
                if (selectedSkill == null) {
                    return;
                }
                //Log.debug("selectedSkill: " + selectedSkill.getSkillTemplate().name);
                if (selectedSkill.isCoolDown()) {
//                    Effect eff = getEm().findByID(136);
//                    if (eff == null) {
                    return;
//                    }
                }
                if (mp < selectedSkill.mpUsing) {
                    getService().serverMessage("Không đủ MP");
                    return;
                }
                selectedSkill.timeCoolDown = System.currentTimeMillis();
                if (getEm().statusWithID(189) || getEm().statusWithID(136)) {
                    if (selectedSkill.getSkillTemplate().name.contains("Đá")
                            || selectedSkill.getSkillTemplate().name.contains("Đấm")) {
                        selectedSkill.timeCoolDown -= selectedSkill.coolDown * 20 / 100;
                    }
                }
                addMp(-selectedSkill.mpUsing);
                int skillTemplateId = selectedSkill.getSkillTemplate().id;

                zone.getService().setSkillPaint_2(this.id, selectedSkill.id, pl.id);
                if (skillTemplateId == 5 || skillTemplateId == 22 || skillTemplateId == 87) {
                    int num = skillTemplateId == 5 ? selectedSkill.getItemOption()[0].getParam()
                            : skillTemplateId == 22 ? 6 : 2;
                    for (int i = 0; i < num; i++) {
                        attackChar(pl, selectedSkill);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {

                        }
                    }
                } else {
                    attackChar(pl, selectedSkill);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attackChar(Char pl, Skill selectedSkill) {
        pl.lock.lock();
        try {
            if (!pl.isDead && !pl.isCleaned) {
                selectSkill = selectedSkill;
                setAbility();
                int damage2 = damage * 90 / 100;
                if (selectedSkill.isSkillTemplate == 26) {
                    Effect eff = new Effect(55, options[417], options[254]);
                    pl.getEm().setEffect(eff);
                    setLastTimeQuaCauDen(System.currentTimeMillis());
                }
                if (selectedSkill.isSkillTemplate == 29) {
                    Effect eff = new Effect(161, options[447], 0);
                    pl.getEm().setEffect(eff);

                }
                if (selectedSkill.isSkillTemplate == 20) {
                    Effect eff = new Effect(160, options[446], 0);
                    pl.getEm().setEffect(eff);
                }

                if (selectedSkill.isSkillTemplate == 89) {
                    Effect eff = new Effect(76, 5000, 320);
                    pl.getEm().setEffect(eff);
                }
                if (selectedSkill.isSkillTemplate == 85) {
                    Effect eff = new Effect(197, 5000, 320);
                    pl.getEm().setEffect(eff);
                }
                if (selectedSkill.isSkillTemplate == 101) {
                    Effect eff1 = new Effect(207, 15000, options[494]);
                    Effect eff2 = new Effect(208, 15000, options[494]);
                    getEm().setEffect(eff1);
                    pl.getEm().setEffect(eff2);
                }
                if (selectedSkill.isSkillTemplate == 91) {
                    Effect eff = new Effect(152, 15000, 100);
                    pl.getEm().setEffect(eff);
                }
                if (selectedSkill.isSkillTemplate == 43) {
                    if (body[15] != null && body[15].id == 848) {
                        serverMessage("Đối phương có kính chống thái dương hạ san");
                    } else {
                        Effect eff = new Effect(76, 5000, 300);
                        pl.getEm().setEffect(eff);
                    }
                }

//                        int tieuHaoKp = options[304];
//                        if (tieuHaoKp > 0) {
//                            pl.addMp(-(pl.mp * tieuHaoKp / 100));
//                        }
                int dameHit = Util.nextInt(damage2, this.damage);
                dameHit /= 3;
                dameHit -= pl.reduceDame;
                int pois = options[49] + options[69] + options[124] + options[169] - pl.options[289];
                int lamCham = options[50] + options[70] + options[125] + options[170] - pl.options[290];
                boolean isChiuDon = Util.nextInt(0, 1000) < chiuDon - pl.reduceChiuDon;
                boolean isSuyGiam = Util.nextInt(0, 1000) < suyGiam - pl.reduceSuyGiam;
                boolean isStun = Util.nextInt(0, 1000) < stun - pl.reduceStun;
                boolean isPois = Util.nextInt(0, 300) < pois;
                boolean isLamCham = Util.nextInt(0, 300) < lamCham;

                short _crit = (short) (crit - pl.reduceCrit);
                float percentCrit = _crit / 100f;
                boolean isCrit = false;
                if (selectedSkill.getSkillTemplate().name.contains("Đá")
                        || selectedSkill.getSkillTemplate().name.contains("Đấm")) {
                    if (getEm().findByID(149) != null) {
                        Effect eff = getEm().findByID(149);
                        percentCrit = 80;
                        eff.param--;
                        if (eff.param <= 0) {
                            getEm().removeEffect(eff);
                            zone.getService().playerRemoveEffect(this, eff);
                        } else {
                            zone.getService().playerAddEffect(this, eff);
                        }
                    }
                }
                if (Util.nextInt(0, 80) < percentCrit) {
                    short _critHit = (short) ((criticalHitPower - pl.reduceCritHitPower));
                    dameHit += dameHit * (50 + _critHit) / 100;
                    isCrit = true;
                }
                if (pl.getEm().findByID(113) != null) {
                    Effect eff = pl.getEm().findByID(113);
                    int percent = dameHit / pl.maxHP * 100;
                    if (percent > eff.param) {
                        em.removeEffect(eff);
                    }
                    dameHit = 0;
                }
                int pMiss = (pl.miss - exactly - (pl.miss * skipMiss / 100)) / 55;
//                        boolean isNotMiss = Util.nextInt(100) < skipMiss / 100;
//                        boolean isMiss = Util.nextInt(100) < pMiss;
                boolean isMiss = Util.nextInt(0, 80) < pMiss;

                if (isMiss) {
                    dameHit = 0;
                } else {
                    if (isChiuDon) {
                        if (pl.getEm().findByID(11) == null) {
                            Effect eff = new Effect(11, 3000, 0);
                            pl.getEm().setEffect(eff);
                        }
                    }
                    if (isSuyGiam) {
                        if (pl.getEm().findByID(8) == null) {
                            Effect eff = new Effect(8, 3000, 0);
                            pl.getEm().setEffect(eff);
                        }
                    }
                    if (isStun) {
                        if (pl.getEm().findByID(12) == null) {
                            Effect eff = new Effect(12, 3000, 0);
                            pl.getEm().setEffect(eff);
                        }
                    }
                    if (isPois) {
                        if (pl.getEm().findByID(9) == null) {
                            Effect eff = new Effect(9, 3000, 0);
                            pl.getEm().setEffect(eff);
                        }
                    }
                    if (isLamCham) {
                        if (pl.getEm().findByID(10) == null) {
                            Effect eff = new Effect(10, 3000, 0);
                            pl.getEm().setEffect(eff);
                        }
                    }
                    if (pl.getEm().findByID(11) != null) {
                        dameHit *= 2;
                    }
                    if (pl.getEm().findByID(8) != null) {
                        dameHit += dameHit / 2;
                    }
                    if (pl.getEm().statusWithID(55)) {
                        dameHit += dameHit * Util.nextInt(10, 25) / 100;
                    }
                }

//                        if(selectedSkill.isSkillTemplate == 89 ) {
//                            Effect eff = pl.getEm().findByID(169);
//                            if(eff != null)
//                                dameHit = 0;
//                        }
                float percentArmor = (float) (pl.armor - pl.armor * armorPenetration / 100) / 50;
                dameHit -= (int) (dameHit * percentArmor / 100);
                if (percentArmor > 60) {
                    dameHit -= 500;
                }
                if (dameHit < 0) {
                    dameHit = 0;
                }

                if (selectedSkill.isSkillTemplate == 22) {
                    Effect eff = new Effect(36, options[323], 0);
                    pl.getEm().setEffect(eff);
                    dameHit = options[373];
                }
                if (pl.counterDame > 0) {
                    if (Util.nextInt(0, 100) < pl.counterDame) {
                        addHp(-dameHit);
                        dameHit = 0;
                    }
                }
                if (pl.getEm().findByID(109) != null) {
                    Effect eff = pl.getEm().findByID(109);
                    pl.getEm().removeEffect(eff);
                    pl.zone.getService().playerRemoveEffect(pl, eff);
                }

                if (selectedSkill.isSkillTemplate == 37) {
                    Effect eff = new Effect(109, 12000, 0);
                    pl.getEm().setEffect(eff);
                    dameHit = 0;
                }
                if (selectedSkill.isSkillTemplate == 28) {
                    pl.addMp(-(pl.maxMP * options[304] / 100));
                }

                if (selectedSkill.isSkillTemplate == 68) {
                    dameHit = (Util.nextInt(10, 40) - pl.options[487]) * pl.hp / 100;
                }
                if (pl.getEm().findByID(108) != null) {
                    dameHit = 0;
                }
                if (selectedSkill.isSkillTemplate == 4) {
                    dameHit = options[371] * pl.maxHP / 100;
                }
                if (selectedSkill.isSkillTemplate == 83) {
                    dameHit = pl.hp * 8 / 100;
                    addHp(dameHit);
                }
                if (selectedSkill.isSkillTemplate == 108) {
                    dameHit = 20 * pl.hp / 100;
                    Effect effect = new Effect(214, 20000, 2000);
                    getEm().setEffect(effect);
                }

                if (selectedSkill.isSkillTemplate == 56) {
                    int hp_old = this.hp;
                    this.hp = pl.hp;
                    pl.hp = hp_old;
                    if (this.hp > maxHP) {
                        this.hp = maxHP;
                    }
                    if (pl.hp > pl.maxHP) {
                        pl.hp = pl.maxHP;
                    }
                    getService().updateHP();
                    zone.getService().updateHP(this);
                    dameHit = 0;
                }

//                        if (this.options[371] > 0) {
//                            dameHit = pl.maxHP * this.options[371] / 100; // fix big bang
//                        }
                pl.hp -= dameHit;
                zone.getService().attackCharacter(pl.id, pl.mp, pl.hp, isCrit, name, pl.x, pl.y);
                if (dameHit > 0) {
                    if (pl.hp <= 0) {
                        if (zone instanceof NgucTu ngucTu) {
                            Integer[] limitPoint = ngucTu.getLimitPoint(this.id);
                            if (limitPoint[0] < 5) {
                                addClanPoint(5);
                                limitPoint[0]++;
                            }
                            if (limitPoint[1] < 5) {
                                addPointSoiNoi(10);
                                limitPoint[1]++;
                            }
                            countAddPointNgucTu++;
                        }
                        if (zone instanceof ZMartialArts) {
                            martialArtsPoint++;
                            if (martialArtsPoint % 10 == 0) {
                                getService().serverMessage("Đang có :" + martialArtsPoint + " điểm");
                            }
                        }
                        if (zone instanceof ZPowerTournament) {
                            this.group.powerStationPoint++;
                            if (this.group.powerStationPoint % 10 == 0) {
                                getService().serverMessage("Tổ đội của con đang có :" + this.group.powerStationPoint + " điểm");
                            }
                        }
                        pl.startDie();
                    }
                }
            }
        } finally {
            pl.lock.unlock();
            selectSkill = null;
            setAbility();
        }
    }

    public void addPointSoiNoi(int point) {
        pointEnthusiastic += point;
        pointEnthusiasticWeek += point;
        serverMessage("Bạn đã nhận được " + point + " điểm sôi nổi");
    }

    public boolean removeItems(int id, int quantityNeeded) {
        int quantity = getQuantityItemById(id);

        if (quantity < quantityNeeded) {
            return false;
        }

        for (int i = 0; i < bag.length && quantityNeeded > 0; i++) {
            Item item = bag[i];
            if (item != null && item.id == id) {
                int currentQuantity = item.getQuantity();
                if (currentQuantity <= quantityNeeded) {
                    quantityNeeded -= currentQuantity;
                    item.setExpire(0);
                    getService().updateItem(item);
                    bag[i] = null;
                } else {
                    item.setQuantity(currentQuantity - quantityNeeded);
                    quantityNeeded = 0;
                    getService().updateItem(bag[i]);
                }
            }
        }

        return true;
    }

    public void claimQuaySo(Message mss) {
        lockItem.lock();
        try {
            if (isDead) {
                return;
            }
            if (itemQuaySo == null) {
                return;
            }
            if (getSlotNull() < 1) {
                warningBagFull();
                return;
            }
            boolean isRuong = false;
            int type = -1;
            if (mss.reader().available() == 0) {
                isRuong = true;
            } else {
                type = mss.reader().readByte();
            }
            byte typeQuay = itemQuaySo.getType();
            typeQuay = (byte) (typeQuay == 0 ? 1 : typeQuay == 1 ? 5 : typeQuay == 2 ? 25 : 50);
            int amount = itemQuaySo.getQuantity();
            int itemid = itemQuaySo.getIdItem();
            itemQuaySo = null;
            if (isRuong) {
                amount = typeQuay;
                Item item = new Item(itemid, "qs");
                item.setQuantity(amount);
                item.setLock(true);
                addItemToBag(item);
                getService().addItem(item);
            } else {
                if (type == 0) {
                    switch (itemid) {
                        case 0:
                            int num = DataCenter.gI().dataQuaySo[itemid][amount] * typeQuay;
                            addZeni(num, true);
                            break;
                        case 1:
                            num = DataCenter.gI().dataQuaySo[itemid][amount] * typeQuay;
                            addBallz(num, true);
                            break;
                        case 2:
                            num = DataCenter.gI().dataQuaySo[itemid][amount] * typeQuay;
                            addCoin(num, true);
                            break;
                        case 3:
                            num = DataCenter.gI().dataQuaySo[itemid][amount] * typeQuay;
                            Item item = new Item(160, "qs");
                            item.setQuantity(num);
                            item.setLock(true);
                            addItemToBag(item);
                            getService().addItem(item);
                            break;
                    }
                } else if (type == 1) {
                    int num = DataCenter.gI().dataQuaySo[4][amount] * typeQuay;
                    Item item = new Item(5000, "qs");
                    item.setQuantity(num);
                    item.setLock(true);
                    addItemToBag(item);
                    getService().addItem(item);
                }
            }
            Util.writing("loggame/quayso/" + name + ".txt", "Nhận quà quay số: " + amount + " loại: " + type + " id: " + itemid + "\n");
            getService().resetSpin();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void thuVanMay(Message ms) {
        try {
            if (isDead) {
                return;
            }
            if (typeThuVanMay == 0) {
                if (!removeItems(936, 1)) {
                    getService().serverMessage("Không đủ vé may mắn");
                    return;
                }
                if (getSlotNull() < 1) {
                    warningBagFull();
                    return;
                }
                Item item = ItemManager.getInstance().itemThuVanMayRandom.next().clone();
                addItemToBag(item);
                getService().addItem(item);
                getService().thuVanMay(item.id, item.getQuantity());
            } else {
                if (zeni < 1000) {
                    getService().serverMessage("Không đủ Zeni");
                    return;
                }
                if (getSlotNull() < 1) {
                    warningBagFull();
                    return;
                }
                Item item = ItemManager.getInstance().itemThuVanMayLowRandom.next().clone();
                addZeni(-1000, true);
                if (item.id == 163) {
                    addCoin(item.getQuantity(), true);
                } else if (item.id == 191) {
                    addBallz(item.getQuantity(), true);
                } else {
                    addItemToBag(item);
                    getService().addItem(item);
                }
                getService().thuVanMay(item.id, item.getQuantity());
            }
        } catch (Exception e) {

        }
    }

    public void createClan(Message ms) {
        try {
            String nameClan = ms.readUTF().trim();
            int index = getIndexItemByIdInBag(301, true);
            if (index == -1) {
                service.serverMessage("Không có hạt giống vũ trụ");
                return;
            }
            if (nameClan.length() < 4 || nameClan.length() > 12) {
                service.serverMessage("Tên vũ trụ phải từ 4 đến 12 ký tự");
                return;
            }
            if (clan != null) {
                service.serverMessage("Bạn đã có vũ trụ");
                return;
            }
            if (nameClan.equals("")) {
                service.serverMessage("Tên vũ trụ không hợp lệ");
                return;
            }
            Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); // Cho phép ký tự chữ cái (viết thường hoặc viết hoa) và số
            Matcher m1 = p.matcher(nameClan);
            if (!m1.find()) {
                service.serverMessage("Tên vũ trụ không hợp lệ");
                return;
            }
            ClanDAO clanDAO = Clan.getClanDAO();
            if (clanDAO.checkExist(nameClan)) {
                service.serverMessage("Tên vũ trụ đã tồn tại");
                return;
            }
            removeItem(index, 1, true);
            Clan clan = new Clan();
            clan.setName(nameClan);
            clan.setMainName(this.name);
            clan.setAlert("");
            clan.setLevel((byte) 1);
            clan.setCoin(0);
            clan.setExp(0);
            clan.setCountClan(20);
            clan.setRegDate(new Date());
            clan.setOpenDun((byte) 1);
            this.clan = clan;
            Clan.getClanDAO().save(clan);
            Member mem = Member.builder()
                    .classId(this.idCharSys)
                    .level(level())
                    .type(Clan.TYPE_TOCTRUONG)
                    .name(this.name)
                    .pointClan(0)
                    .pointClanWeek(0)
                    .rewardDataBlackBall(null)
                    .build();
            mem.setChar(this);
            mem.setOnline(true);
            clan.memberDAO.save(mem);
            service.resetScreen();
            service.serverDialog("Tạo vũ trụ thành công");
            zone.getService().sendInfoVuTru(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clanInvite(Message ms) {
        if (this.clan != null) {
            int typeClan = this.clan.getMemberByName(this.name).getType();
            if (typeClan == Clan.TYPE_TOCTRUONG || typeClan == Clan.TYPE_TOCPHO || typeClan == Clan.TYPE_TRUONGLAO) {
                try {
                    String name = ms.readUTF();
                    Char _char = zone.findCharName(name);
                    if (_char != null) {
                        if (_char == this) {
                            return;
                        }
                        if (_char.clan != null) {
                            return;
                        }
                        if (_char.user.activated == 1) {
                            serverDialog("Đối phương cần mở thành viên ở bulma");
                            return;
                        }
                        if (clan.getNumberMember() >= clan.getMemberMax()) {
                            serverDialog("Vũ trụ đã có đủ thành viên");
                            return;
                        }
                        if (_char.timeOutClan > System.currentTimeMillis()) {
                            serverMessage("Người này vừa rời vũ trụ, chỉ có thể mời lại sau 24h.");
                            return;
                        }
                        Invite.PlayerInvite p = _char.invite.findCharInvite(Invite.GIA_TOC, this.id);
                        if (p != null) {
                            service.serverDialog("Không thể mời vào vào liên tục. Vui lòng thử lại sau 30s nữa.");
                            return;
                        }
                        _char.invite.addCharInvite(Invite.GIA_TOC, this.id, 30);
                        _char.getService().clanInvite(this.name, typeClan, clan.getName());
                    }
                } catch (Exception ex) {
                    Log.error("err: " + ex.getMessage(), ex);
                }
            } else {
                service.serverDialog("Bạn không có quyền này.");
            }
        } else {
            service.serverDialog("Bạn không có vũ trụ.");
        }
    }

    public void acceptInviteClan(Message ms) {
        try {
            if (clan != null) {
                return;
            }
            if (user.activated == 1) {
                serverDialog("Tính năng cần mở thành viên ở bulma");
                return;
            }
            String nameInvite = ms.readUTF();
            Char _char = zone.findCharName(nameInvite);
            if (_char != null && _char.clan != null) {
                Invite.PlayerInvite c = this.invite.findCharInvite(Invite.GIA_TOC, _char.id);
                if (c == null) {
                    serverDialog("Đã hết thời gian chấp nhận vào vũ trụ.");
                    return;
                }
                if (_char.clan.getNumberMember() >= _char.clan.getMemberMax()) {
                    serverDialog("Vũ trụ đã có đủ thành viên");
                    return;
                }
                Clan clan = _char.clan;
                if (clan.countInvite < 1) {
                    serverDialog("Vũ trụ đã hết lượt tham gia trong ngày.");
                    return;
                }
                List<Member> members = clan.memberDAO.getAll();
                if (members.size() < clan.getMemberMax()) {
                    this.clan = clan;
                    Member mem = Member.builder()
                            .classId(this.idCharSys)
                            .level(this.level())
                            .type(Clan.TYPE_NORMAl)
                            .name(this.name)
                            .pointClan(0)
                            .pointClanWeek(0)
                            .rewardDataBlackBall(clan.getMemberByName(_char.name).getRewardDataBlackBall())
                            .build();
                    mem.setChar(this);
                    mem.setOnline(true);
                    clan.memberDAO.save(mem);
                    zone.getService().sendInfoVuTru(this);
                    clan.countInvite--;
                    service.serverMessage("Bạn đã gia nhập vào vũ trụ " + this.clan.getName());
                } else {
                    serverDialog("Vũ trụ đã đủ thành viên.");
                }
            } else {
                serverDialog("Người mời đã rời khỏi khu vực.");
            }
        } catch (Exception ex) {
            Log.error("accept ivite clan err: " + ex.getMessage(), ex);
        }
    }

    public void declineIntiveClan(Message ms) {
        try {
            String nameIntive = ms.readUTF();
            Char pl = ServerManager.findCharByName(nameIntive);
            if (pl != null) {
                pl.serverDialog("Chiến binh " + name + " đã từ chối lời mời gia nhập vũ trụ của bạn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestJoinClan(Message ms) {
        try {
            String nameRequest = ms.readUTF();
            Char pl = zone.findCharName(nameRequest);
            if (pl != null) {
                if (pl.clan == null) {
                    return;
                }
                if (user.activated == 1) {
                    serverDialog("Cần mở thành viên ở bulma");
                    return;
                }
                if (pl.clan.countInvite < 1) {
                    serverDialog("Vũ trụ đã hết lượt gia nhập trong ngày");
                    return;
                }
                if (pl.clan.getNumberMember() >= pl.clan.getMemberMax()) {
                    serverDialog("Vũ trụ đã có đủ thành viên");
                    return;
                }
                int type = pl.clan.getMemberByName(pl.name).getType();
                if (type == 3 || type == 4 || type == 5) {
                    pl.getService().requestJoinClan(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptRequestJoinClan(Message ms) {
        try {
            if (clan == null) {
                return;
            }
            if (clan.getNumberMember() >= clan.getMemberMax()) {
                serverDialog("Vũ trụ đã có đủ thành viên");
                return;
            }
            if (user.activated == 1) {
                serverDialog("Tính năng này cần mở thành viên ở bulma để sử dụng");
                return;
            }
            String nameJoin = ms.readUTF();
            Char pl = ServerManager.findCharByName(nameJoin);
            if (pl != null) {
                if (pl.clan != null) {
                    return;
                }
                if (pl.user.activated == 1) {
                    serverDialog("Đối phương chưa mở thành viên");
                    return;
                }
                int typeClan = this.clan.getMemberByName(this.name).getType();
                if (typeClan == Clan.TYPE_TOCTRUONG || typeClan == Clan.TYPE_TOCPHO || typeClan == Clan.TYPE_TRUONGLAO) {
                    List<Member> members = clan.memberDAO.getAll();
                    if (clan.countInvite < 1) {
                        serverDialog("Vũ trụ đã hết lượt tham gia trong ngày.");
                        return;
                    }
                    if (members.size() < clan.getMemberMax()) {
                        pl.clan = clan;
                        Member mem = Member.builder()
                                .classId(pl.idCharSys)
                                .level(pl.level())
                                .type(Clan.TYPE_NORMAl)
                                .name(pl.name)
                                .pointClan(0)
                                .pointClanWeek(0)
                                .rewardDataBlackBall(clan.getMemberByName(this.name).getRewardDataBlackBall())
                                .build();
                        mem.setChar(pl);
                        mem.setOnline(true);
                        clan.memberDAO.save(mem);
                        clan.countInvite--;
                        pl.zone.getService().sendInfoVuTru(pl);
                        pl.service.serverMessage("Bạn đã gia nhập vào vũ trụ " + this.clan.getName());
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void changeClanType(Message ms) {
        try {
            String name_input = ms.readUTF();
            byte type = ms.reader().readByte();
            if (this.clan != null) {
                int cType = clan.getMemberByName(name).getType();
                if (cType == Clan.TYPE_TOCTRUONG) {
                    Member mem = this.clan.getMemberByName(name_input);
                    if (mem != null) {
                        if (type == Clan.TYPE_TOCPHO || type == Clan.TYPE_TRUONGLAO) {
                            if (type == Clan.TYPE_TOCPHO && this.clan.getNumberSameType(type) >= 1) {
                                serverMessage("Chức thiên sứ đã đủ chỗ.");
                                return;
                            }
                            if (type == Clan.TYPE_TRUONGLAO && this.clan.getNumberSameType(type) >= 5) {
                                serverMessage("Chức thần sáng tạo đã đủ chỗ.");
                                return;
                            }

                            mem.setType(type);
                            try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE `clan_member` SET `type` = ? WHERE `id` = ? LIMIT 1;");) {
                                stmt.setInt(1, mem.getType());
                                stmt.setInt(2, mem.getId());
                                stmt.executeUpdate();
                            }
                            Char _char = ServerManager.findCharByName(name_input);
                            if (_char != null) {
                                _char.zone.getService().sendInfoVuTru(_char);
                            }
                            service.sendInfoVuTru();
                            clan.getClanService().serverMessage(name_input + " được bổ nhiệm làm "
                                    + ((type == Clan.TYPE_TOCPHO) ? "thiên sứ" : "thần sáng tạo"));

                        } else {
                            if (type == 5) {
                                if (clan.getCoin() < 1000000) {
                                    service.serverMessage("Ngân sách không đủ 1 triệu ballz");
                                    return;
                                }
                                clan.addCoin(-1000000);
                                clan.writeLog("Đã nhường chức thần huỷ diệt cho " + mem.getName() + " ngân sách trừ " + Util.formatMoney(1000000) + " Ballz");
                                Member old = this.clan.getMemberByName(this.name);
                                old.setType(Clan.TYPE_NORMAl);

                                try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt1 = conn
                                        .prepareStatement("UPDATE `clan_member` SET `type` = ? WHERE `id` = ? LIMIT 1;");) {
                                    stmt1.setInt(1, old.getType());
                                    stmt1.setInt(2, old.getId());
                                    stmt1.executeUpdate();
                                }
                                clan.setMainName(mem.getName());
                                service.sendInfoVuTru();
                                mem.setType(Clan.TYPE_TOCTRUONG);

                                try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn
                                        .prepareStatement("UPDATE `clan_member` SET `type` = ? WHERE `id` = ? LIMIT 1;");) {
                                    stmt.setInt(1, mem.getType());
                                    stmt.setInt(2, mem.getId());
                                    stmt.executeUpdate();
                                }
                                Char _char = ServerManager.findCharByName(name_input);
                                if (_char != null) {
                                    _char.zone.getService().sendInfoVuTru(_char);
                                }
                                zone.getService().sendInfoVuTru(this);
                                service.sendInfoVuTru();
                                service.serverMessage("Nhường chức thần huỷ diệt thành công");
                                return;
                            }
                            int preType = mem.getType();
                            mem.setType(Clan.TYPE_NORMAl);

                            try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn
                                    .prepareStatement("UPDATE `clan_member` SET `type` = ? WHERE `id` = ? LIMIT 1;");) {
                                stmt.setInt(1, mem.getType());
                                stmt.setInt(2, mem.getId());
                                stmt.executeUpdate();
                            }
                            Char _char = ServerManager.findCharByName(name_input);
                            if (_char != null) {
                                _char.zone.getService().sendInfoVuTru(this);
                            }
                            service.sendInfoVuTru();
                            clan.getClanService().serverMessage(name_input + " đã bị bãi nhiệm");
                        }
                    } else {
                        serverMessage("Thành viên không tồn tại.");
                    }
                } else {
                    serverMessage("Bạn không phải thần huỷ diệt.");
                }
            } else {
                serverMessage("Bạn không có vũ trụ.");
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void serverMessage(String str) {
        getService().serverMessage(str);
    }

    public void setTimeDunClan(Message ms) {
        try {
            if (clan == null) {
                return;
            }
            byte hour = ms.reader().readByte();
            byte min = ms.reader().readByte();
            if (hour < 0 || min < 0) {
                return;
            }
            int typeClan = this.clan.getMemberByName(this.name).getType();
            if (typeClan == Clan.TYPE_TOCTRUONG || typeClan == Clan.TYPE_TOCPHO || typeClan == Clan.TYPE_TRUONGLAO) {
                clan.hour = hour;
                clan.min = min;
                service.sendInfoVuTru();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outClan() {
        if (this.clan != null) {
            Member mem = this.clan.getMemberByName(this.name);
            if (mem != null) {
                int cType = mem.getType();
                if (cType != Clan.TYPE_TOCTRUONG) {
                    try {
                        int coin = 10000;
                        switch (cType) {
                            case Clan.TYPE_TOCPHO:
                                coin = 100000;
                                break;

                            case Clan.TYPE_TRUONGLAO:
                                coin = 50000;
                                break;

                            case Clan.TYPE_UUTU:
                                coin = 20000;
                                break;
                        }
                        if (this.ballZ < coin) {
                            service.serverMessage("Bạn không đủ Ballz.");
                            return;
                        }
                        this.timeOutClan = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24);
                        addBallz(-coin, true);
                        this.clan.memberDAO.delete(mem);
                        this.clan.getClanService().serverMessage(this.name + " đã rời vũ trụ");
                        this.clan = null;
                        service.resetScreen();
                        zone.getService().sendInfoVuTru(this);
                        service.serverMessage("Bạn đã rời vũ trụ thành công.");
                    } catch (Exception ex) {
                        Log.error("out clan err: " + ex.getMessage(), ex);
                    }
                } else {
                    service.serverMessage("Bạn là thần huỷ diệt  nên không thể rời.");
                }
            }
        } else {
            service.serverMessage("Bạn không trong vũ trụ.");
        }
    }

    public void moveOutClan(Message ms) {
        try {
            String name = ms.readUTF();
            if (name.equals(this.name)) {
                service.serverDialog("Bạn không thể tự trục xuất chính mình.");
                return;
            }
            if (this.clan != null) {
                int cType = this.clan.getMemberByName(this.name).getType();
                if (cType == Clan.TYPE_TOCTRUONG || cType == Clan.TYPE_TOCPHO) {
                    Member mem = this.clan.getMemberByName(name);
                    if (mem == null) {
                        service.serverMessage("Thành viên này không tồn tại.");
                        return;
                    }
                    int memType = mem.getType();
                    if (memType >= cType) {
                        service.serverMessage("Bạn không có quyền trục xuất người này.");
                        return;
                    }
                    if (this.clan.countKick <= 0) {
                        service.serverMessage("Clan đã hết lượt kick vào ngày hôm nay.");
                        return;
                    }

                    int coin = 10000;
                    switch (memType) {

                        case Clan.TYPE_TOCPHO:
                            coin = 100000;
                            break;

                        case Clan.TYPE_TRUONGLAO:
                            coin = 50000;
                            break;

                        case Clan.TYPE_UUTU:
                            coin = 20000;
                            break;
                    }
                    if (this.clan.getCoin() < coin) {
                        service.serverMessage("Ngân quỹ không đủ.");
                        return;
                    }
                    clan.memberDAO.delete(mem);
                    clan.writeLog(this.name + " đã kích người chơi " + name + " ngân sách trừ " + Util.formatMoney(coin) + " Ballz");
                    this.clan.addCoin(-coin);
                    this.clan.countKick--;
                    Char _char = ServerManager.findCharByName(name);
                    if (_char != null) {
                        _char.clan = null;
                        _char.zone.getService().sendInfoVuTru(_char);
                        _char.timeOutClan = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24);
                    }
                    service.sendInfoVuTru();
                    this.clan.getClanService().serverMessage(this.name + " đã trục xuất " + name + ", ngân quỹ trừ "
                            + coin + " Ballz");
                } else {
                }
            } else {
                service.serverMessage("Bạn không có vũ trụ");
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void inputCoinClan(Message ms) {
        try {
            if (clan == null) {
                return;
            }
            if (trade != null) {
                warningTrade();
                return;
            }
            if (user.activated == 1) {
                serverDialog("Tính năng này cần mtv ở bulma để sử dụng");
                return;
            }
            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            int coin = ms.reader().readInt();
            if (coin < 1000 || coin > 100000000) {
                serverDialog("Vui lòng nhập trong khoảng từ 1.000 Ballz đến 100.000.000 Ballz.");
                return;
            }
            if (coin > this.ballZ) {
                serverDialog("Bạn không đủ Ballz.");
                return;
            }

            clan.writeLog(this.name + " đã đóng góp " + Util.formatMoney(coin) + " Ballz vào ngân sách vũ trụ");
            clan.addCoin(coin);
            addBallz(-coin, true);
            service.sendInfoVuTru();
            clan.getClanService().serverMessage(name + " đóng góp vào ngân quỹ " + Util.getCurrency(coin) + " Ballz");
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void uocRongCerealian(Message ms) {
        try {
            if (typeUocRong != 478) {
                return;
            }
            byte select = ms.reader().readByte();
            RuntimeServer.gI().resetEffectUoc();
            Item item;
            switch (select) {
                case 0:
                    item = new Item(1113, "gr");
                    break;
                case 1:
                    item = new Item(1106, "gr");
                    break;
                case 2:
                    item = new Item(1107, "gr");
                    break;
                case 3:
                    item = new Item(1108, "gr");
                    break;
                case 4:
                    item = new Item(1109, "gr");
                    break;
                case 5:
                    item = new Item(1110, "gr");
                    break;
                case 6:
                    item = new Item(1111, "gr");
                    break;
                case 7:
                    item = new Item(1112, "gr");
                    break;
                default:
                    return;
            }
            item.setLock(true);
            addItemToBag(item);
            getService().addItem(item);
            Util.writing("loggame/uocrong/" + name + ".txt", "Ước rồng 2 bi : " + select + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void learnSkillPet(Message ms) {
        try {
            if (pet == null) {
                return;
            }
            byte index = ms.reader().readByte();
            if (index < 0 || index >= pet.skillPet.length) {
                return;
            }
            byte var8 = 0;
            short var9 = 0;
            switch (index) {
                case 0:
                case 1:
                case 2:
                case 3:
                    break;
                case 4:
                    var9 = 750;
                    var8 = 5;
                    break;
                case 5:
                    var9 = 750;
                    var8 = 10;
                    break;
                case 6:
                    var9 = 100;
                    var8 = 15;
                    break;
                case 7:
                    var9 = 200;
                    var8 = 20;
                    break;
                case 8:
                    var9 = 300;
                    var8 = 25;
                    break;
                case 9:
                    var9 = 750;
                    var8 = 30;
                    break;
                case 10:
                    var9 = 400;
                    var8 = 35;
                    break;
                case 11:
                    var9 = 750;
                    var8 = 40;
                    break;
                case 12:
                    var9 = 500;
                    var8 = 45;
                    break;
                case 13:
                    var9 = 750;
                    var8 = 50;
                    break;
                default:
                    return;
            }

            Skill skill = pet.skillPet[index];
            if (skill == null) {
                if (!removeItems(160, var9)) {
                    getService().warningMessage("Không đủ kim cương");
                    return;
                }
                if (index == 4) {
                    Skill skill1 = null;
                    switch (sys) {
                        case 1:
                            skill1 = SkillFactory.getInstance().getSkill(8, 0);
                            break;
                        case 2:
                            skill1 = SkillFactory.getInstance().getSkill(3, 0);
                            break;
                        case 3:
                            skill1 = SkillFactory.getInstance().getSkill(15, 0);
                            break;
                        case 4:
                            skill1 = SkillFactory.getInstance().getSkill(21, 0);
                            break;
                        case 5:
                            skill1 = SkillFactory.getInstance().getSkill(24, 0);
                            break;

                    }
                    pet.skillPet[index] = skill1;
                } else {
                    List<Skill> skills1 = SkillFactory.getInstance().getSkillByLevelNeed(var8);
                    Skill skill1 = skills1.get(Util.nextInt(0, skills1.size() - 1));
                    pet.skillPet[index] = skill1;
                }
            } else {
                if (skill != null) {
                    Skill skillNext = SkillFactory.getInstance().getSkill(skill.isSkillTemplate, skill.level + 1);
                    if (skillNext != null) {
                        if (pet.KI < skillNext.kiUpgrade) {
                            service.serverDialog("Không đủ KI");
                            return;
                        }
                        pet.KI -= skillNext.kiUpgrade;
                        pet.skillPet[index] = skillNext;
                    }
                }
            }

            pet.updateSkillPet();
            pet.setAbility();
            getService().updateItemBody(body[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final int s() {
        Item var1;
        if ((var1 = this.body[0]) != null) {
            ItemOption[] var4 = var1.options();

            for (int var2 = 0; var2 < var4.length; ++var2) {
                if (var4[var2].option[0] == 285) {
                    return var4[var2].f();
                }
            }
        }

        return 0;
    }

    public void addPointKame(int point) {
        pointKame += point;
        getService().serverMessage("Bạn nhận được " + point + " điểm Kame");
    }

    public void addPointSuper(int point) {
        pointSuper += point;
        getService().serverMessage("Bạn nhận được " + point + " điểm Super");
    }

    public void petManager(Message ms) {
        try {
            int type = ms.reader().readByte();
            Log.debug("type: " + type);
            if (isLuongLongNhatThe) {
                serverDialog("Đang trong trạng thái hợp thể");
                return;
            }
            switch (type) {
                case 0:
                    pet.typePet = 0;
                    pet.startDie();
                    zone.getService().updatePet(id, pet.typePet, pet.hp, pet.maxHP, pet.mp, pet.maxMP);
                    break;
                case 1:
//                    if (zeni < 1) {
//                        serverDialog("Không đủ zeni");
//                        return;
//                    }
//                    addZeni(-1, true);
                    pet.typePet = 1;
                    if (pet.isDead) {
                        pet.hp = pet.mp = 1;
                    }
                    pet.isDead = false;
                    zone.getService().updatePet(id, pet.typePet, pet.hp, pet.maxHP, pet.mp, pet.maxMP);
                    break;
                case 2:
                    if (zeni < 10) {
                        serverDialog("Không đủ zeni");
                        return;
                    }
                    addZeni(-10, true);
                    pet.typePet = 2;
                    if (pet.isDead) {
                        pet.hp = pet.mp = 1;
                    }
                    pet.isDead = false;
                    zone.getService().updatePet(id, pet.typePet, pet.hp, pet.maxHP, pet.mp, pet.maxMP);
                    break;
                case 3:
                    if (pet.isDead || pet.typePet == 0) {
                        serverDialog("Đệ tử phải còn sống và đang theo sau");
                        return;
                    }
                    pet.typePet = 3;
                    zone.getService().updatePet(id, pet.typePet, pet.hp, pet.maxHP, pet.mp, pet.maxMP);
                    Effect effect = new Effect(140, TimeUnit.MINUTES.toMillis(30), 200);
                    getEm().setEffect(effect);
                    break;
                case 5:
                    if (zeni < 150) {
                        serverDialog("Không đủ zeni");
                        return;
                    }
                    if (body[3] != null || body[5] != null || body[7] != null || body[9] != null) {
                        serverDialog("Tháo trang bị trước khi mở khoá đệ tử");
                        return;
                    }
                    if (body[0] == null) {
                        return;
                    }
                    addZeni(-150, true);
                    Item detu = body[0].clone();
                    detu.setLock(false);
                    body[0] = null;
                    pet = null;
                    getService().updateItemBodyChange();
                    zone.getService().updateItemBody(this);
                    getService().resetScreen();
                    addItemToBag(detu);
                    getService().addItem(detu);
                    serverMessage("Mở khoá đệ tử thành công");
                    break;
                case 6:
                    int var2 = s();
                    int var26 = 500000;
                    if (var2 == 57) {
                        var26 = 4000000;
                    } else if (var2 == 55) {
                        var26 = 2000000;
                    } else if (var2 == 50) {
                        var26 = 1000000;
                    }
                    if (ballZ < var26) {
                        getService().serverDialog("Không đủ ballz");
                        return;
                    }
                    if (var2 == 59) {
                        getService().serverDialog("Đã tới giới hạn có thể mở ra");
                        return;
                    }
                    addBallz(-var26, true);
                    if (var2 == 40) {
                        body[0].updateGioiHan(10);
                    } else if (var2 == 50) {
                        body[0].updateGioiHan(5);
                    } else if (var2 == 55) {
                        body[0].updateGioiHan(2);
                    } else if (var2 == 57) {
                        body[0].updateGioiHan(1);
                    }
                    getService().updateItemBody(body[0]);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tradeInvite(Message ms) {
        try {
            if (isDead) {
                return;
            }

            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục.");
                return;
            }

            if (user.activated == 1) {
                serverDialog("Tính năng này cần mtv ở bulma để sử dụng");
                return;
            }
            String nameInvite = ms.readUTF();
            Char _char = zone.findCharName(nameInvite);

            if (_char != null) {
                if (_char == this) {
                    return;
                }
                int distance = Util.getRange(_char.x, _char.y, this.x, this.y);
                if (distance > 100) {
                    serverDialog("Khoảng cách quá xa!");
                    return;
                }
                if (_char.user.activated == 1) {
                    serverDialog("Đối phương chưa mở thành viên");
                    return;
                }
                if (!_char.isActiveAction()) {
                    serverDialog("Đối phương chưa mở mã bảo vệ.");
                    return;
                }
                if (_char.trade != null) {
                    serverDialog("Người này đang giao dịch với người khác.");
                    return;
                }
                Invite.PlayerInvite p = _char.invite.findCharInvite(Invite.GIAO_DICH, this.id);
                if (p != null) {
                    serverDialog("Không thể mời giao dịch liên tục. Vui lòng thử lại sau 30s nữa.");
                    return;
                }
                _char.invite.addCharInvite(Invite.GIAO_DICH, this.id, 30);
                _char.getService().tradeInvite(name);
                Trade trade = new Trade();
                this.trade = trade;
                _char.trade = trade;

                this.myTrade = trade.traders[0] = new Trader(this);
                _char.myTrade = trade.traders[1] = new Trader(_char);

                this.partnerTrade = _char.myTrade;
                _char.partnerTrade = this.myTrade;

            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void tradeItemLock(Message ms) {
        try {
            if (trade == null || myTrade == null) {
                return;
            }
            int xu = ms.reader().readInt();
            byte itemLength = ms.reader().readByte();
            if (xu > 0 && xu <= this.ballZ) {
                this.myTrade.coinTradeOrder = xu;
            }
            Char partner = partnerTrade.getChar();
            String errFormat = "";
            boolean isMyMissTake = true;
            if (this.ballZ < xu) {
                errFormat = "%s không đủ ballz để giao dịch";
            } else if (xu > 500000000) {
                errFormat = "%s đã giao dịch quá giới hạn 500.000.000 ballz";
            } else if (itemLength > partner.getSlotNull()) {
                isMyMissTake = false;
                errFormat = "%s không đủ chỗ trống trong hành trang";
            } else if ((long) partner.ballZ + (long) xu > Integer.MAX_VALUE) {
                isMyMissTake = false;
                errFormat = "%s đã đạt giới hạn chứa ballz, vui lòng giao dịch ít hơn";
            }

            if (!errFormat.equals("")) {
                tradeClose();
                String partner_name_new = partner.name;
                String name_new = this.name;
                serverMessage(String.format(errFormat, isMyMissTake ? "Bạn" : partner_name_new));
                partner.serverMessage(String.format(errFormat, isMyMissTake ? name_new : "Bạn"));
                return;
            }
            ArrayList<Integer> list = new ArrayList<>();
            this.myTrade.itemTradeOrder = new Vector<>();
            for (int i = 0; i < itemLength; i++) {
                int index = ms.reader().readShort();
                if (index < 0 || index >= this.numberCellBag) {
                    continue;
                }
                if (bag[index] != null) {
                    if (bag[index].template.isBlackListItemTrade() || bag[index].isLock()) {
                        tradeClose();
                        serverDialog("Không thể giao dịch vật phẩm này.");
                        return;
                    }
                    if (Util.checkExist(list, index)) {
                        continue;
                    }
                    this.myTrade.itemTradeOrder.add(bag[index]);
                    list.add(index);
                }

            }
            trade.tradeItemLock(myTrade);
            myTrade.isLock = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tradeAccept() {
        if (trade == null || myTrade == null) {
            return;
        }

        if (!isActiveAction()) {
            serverDialog("Vui lòng mở mã bảo vệ để tiếp tục.");
            return;
        }
        if (!myTrade.accept && myTrade.isLock) {
            myTrade.accept = true;
            (myTrade == trade.traders[0] ? trade.traders[1] : trade.traders[0]).player.getService().tradeAccept();
        }
        if (trade.traders[0].accept && trade.traders[1].accept) {
            try {
                trade.update();
            } catch (Exception ex) {
                Log.error("err: " + ex.getMessage(), ex);
            }
        }
    }

    public void tradeClose() {
        try {
            if (this.trade != null) {
                trade.closeUITrade();
            } else {
                getService().tradeCancel();
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public byte getTypeSucManh() {
        return (byte) (level() / 10);
    }

    public int getExpJoinNgucTu() {
        if (exp >= 770000 && exp <= 2390000) {
            return 4;
        } else if (exp >= 2390001 && exp <= 357000000) {
            return 2;
        } else if (exp >= 357000001 && exp <= 8150000000L) {
            return 1;
        } else if (exp >= 8150000001L) {
            return 3;
        }
        return 1;
    }

    public void uocRongNamec(Message ms) {
        try {
            if (typeUocRong != 450) {
                return;
            }
            RuntimeServer.gI().resetEffectUoc();
            byte select = ms.reader().readByte();
            switch (select) {
                case 0:
                    Item dcq = new Item(829, "nr");
                    dcq.setLock(true);
                    dcq.setQuantity(200);
                    addItemToBag(dcq);
                    getService().addItem(dcq);
                    break;
                case 1:
                    Item item = new Item(276, "nr");
                    item.setLock(true);
                    item.strOption = "0,600,900;1,600,900;2,60,90;5,60,90;167,60,90;258,60,90;414,2,2";
                    item.createOption();
                    item.strOption = "413,0,60000;" + item.strOption;
                    item.upgradeItem(item.getUpgrade() + 1);
                    addItemToBag(item);
                    getService().addItem(item);
                    break;
            }
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
            String strDate = formatter.format(date);
            Util.writing("loggame/uocrong/" + name + ".txt", name + " đã ước rồng namec lấy " + StringMenu.Namec[select] + " vào lúc " + strDate + "\n-----------------\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uocRongEath(Message ms) {
        try {
            if (typeUocRong != 441) {
                return;
            }
            RuntimeServer.gI().resetEffectUoc();
            byte select = ms.reader().readByte();
            switch (select) {
                case 0:
                    addBallz(10000000, true);
                    break;
                case 1:
                    Item detu = new Item(891, "nr");
                    detu.setLock(true);
                    addItemToBag(detu);
                    getService().addItem(detu);
                    break;
                case 2:
                    Item item = new Item(189, "nr");
                    item.setLock(true);
                    item.strOption = "0,300,600;1,300,600;2,30,60;5,30,60;167,30,60;258,30,60;414,1,1";
                    item.createOption();
//                    item.strOption = "413,0,30000;" + item.strOption;
                    addItemToBag(item);
                    getService().addItem(item);
                    break;
                case 3:
                    if (increaseExtra >= 20) {
                        serverDialog("Đã tăng cường tối đa");
                        return;
                    }
                    increaseExtra++;
                    break;
            }
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
            String strDate = formatter.format(date);
            Util.writing("loggame/uocrong/" + name + ".txt", name + " đã ước rồng trái đất lấy " + (select == 0 ? "giàu có" : select == 1 ? " thẻ đệ tử" : select == 2 ? " cân đẩu vân" : " 1% chỉ số phụ") + " vào lúc " + strDate + "\n-----------------\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uocRongDen(Message ms) {
        try {
            if (typeUocRong != 449) {
                return;
            }
//            "8000 đồng tiền vũ trụ", "Điều ước may rủi", "Tăng 250 chí mạng ( 1 tuần )", "Tăng 150 tấn công ( 1 tuần )", "Tăng 1.500 Hp ( 1 tuần )"
            RuntimeServer.gI().resetEffectUoc();

            byte select = ms.reader().readByte();
            switch (select) {
                case 0:
                    for (Char pl : clan.getOnlineMembers()) {
                        Item item = new Item(711, "nrd");
                        item.setQuantity(8000);
                        pl.addItemToBag(item);
                        pl.getService().addItem(item);
                        pl.getService().serverDialog("Bạn vừa nhận được 8000 đồng tiền vũ trụ từ điều ước ngọc rồng đen");
                    }
                    break;
                case 1:
                    RandomCollection<Integer> mayRui = new RandomCollection<>();
                    mayRui.add(98, 1);
                    mayRui.add(2, 2);
                    int index = mayRui.next();
                    switch (index) {
                        case 0: {
                            for (Char pl : clan.getOnlineMembers()) {
                                Item item = new Item(711, "nrd");
                                item.setQuantity(1);
                                item.setLock(true);
                                pl.addItemToBag(item);
                                pl.getService().addItem(item);
                                pl.getService().serverDialog("Bạn vừa nhận được cải trang ginyu từ điều ước ngọc rồng đen");
                            }
                            break;
                        }
                        case 1: {
                            for (Char pl : clan.getOnlineMembers()) {
                                int xu = Util.nextInt(1000000, 5000000);
                                pl.addCoin(xu, true);
                                pl.getService().serverDialog("Bạn vừa nhận được " + xu + " xu từ điều ước may rủi ngọc rồng đen");
                            }
                            break;
                        }
                    }
                    break;
                case 2:
                    for (Member member : clan.memberDAO.getAll()) {
                        for (RewardDataBlackBall rewardDataBlackBall : member.getRewardDataBlackBall()) {
                            HashMap<EnumBuffBlackBall, Long> buff = rewardDataBlackBall.getBuff();
                            if (buff != null) {
                                buff.remove(EnumBuffBlackBall.CRIT);
                            }
                        }
                        member.getRewardDataBlackBall().add(new RewardDataBlackBall(EnumBuffBlackBall.CRIT, System.currentTimeMillis()));
                        member.getChar().setAbility();
                        clan.memberDAO.update(member);
                        member.getChar().getService().serverDialog("Bạn vừa nhận được " + EnumBuffBlackBall.CRIT.param + " chí mạng từ điều ước ngọc rồng đen");
                        Effect effect = new Effect(133, 604800016, 1);
                        member.getChar().getEm().setEffect(effect);
                    }
                    break;
                case 3:
                    for (Member member : clan.memberDAO.getAll()) {
                        for (RewardDataBlackBall rewardDataBlackBall : member.getRewardDataBlackBall()) {
                            HashMap<EnumBuffBlackBall, Long> buff = rewardDataBlackBall.getBuff();
                            if (buff != null) {
                                buff.remove(EnumBuffBlackBall.DAME);
                            }
                        }
                        member.getRewardDataBlackBall().add(new RewardDataBlackBall(EnumBuffBlackBall.DAME, System.currentTimeMillis()));
                        member.getChar().setAbility();
                        clan.memberDAO.update(member);
                        member.getChar().getService().serverDialog("Bạn vừa nhận được " + EnumBuffBlackBall.DAME.param + " damage từ điều ước ngọc rồng đen");
                        Effect effect = new Effect(133, 604800016, 1);
                        member.getChar().getEm().setEffect(effect);
                    }
                    break;
                case 4:
                    for (Member member : clan.memberDAO.getAll()) {
                        for (RewardDataBlackBall rewardDataBlackBall : member.getRewardDataBlackBall()) {
                            HashMap<EnumBuffBlackBall, Long> buff = rewardDataBlackBall.getBuff();
                            if (buff != null) {
                                buff.remove(EnumBuffBlackBall.HP);
                            }
                        }
                        member.getRewardDataBlackBall().add(new RewardDataBlackBall(EnumBuffBlackBall.HP, System.currentTimeMillis()));
                        member.getChar().setAbility();
                        clan.memberDAO.update(member);
                        member.getChar().getService().serverDialog("Bạn vừa nhận được " + EnumBuffBlackBall.HP.param + " hp từ điều ước ngọc rồng đen");
                        Effect effect = new Effect(133, 604800016, 1);
                        member.getChar().getEm().setEffect(effect);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void input(Message ms) {
        if (trade != null) {
            warningTrade();
            return;
        }
        try {
            String text = ms.readUTF();
            short inputID = ms.reader().readShort();
            InputDialog input = getInput();

            if (input == null) {
                return;
            }
            if (inputID != input.getId()) {
                return;
            }
            input.setText(text);
            if (input.getId() == CMDInputDialog.EXECUTE) {
                input.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePointPB(int point) {
        this.pointHell += point;
        getService().updatePointPB();
    }

    public void submenu(Message mss) {
        try {
            byte select = mss.reader().readByte();
            if (subMenuId == CMDMenu.QUY_LAO) {
                if (select == answer && !taskOrders.isComplete()) {
                    taskOrders.updateTask(1);
                    questionAndAnswer();
                    return;
                }
                countTaskFailed++;
                if (countTaskFailed >= 2) {
                    serverMessage("Bạn đã trả lời sai quá 2 lần");
                    taskOrders.setFailed(true);

                    countTaskFailed = 0;
                    taskOrders = null;
                    getService().sendTaskQuyLao();
                    getService().resetTaskQuyLao();
                } else {
                    questionAndAnswer();
                    serverMessage("Bạn đã trả lời sai rồi, hãy thử lại");
                    return;
                }
            }
            subMenuId = -1;
            getService().sendMessage(new Message((byte) 5));
        } catch (Exception e) {

        }
    }

    public void learnSkillClan(Message ms) {
        try {
            if (clan == null) {
                return;
            }
            byte index = ms.reader().readByte();
            if (index < 0) {
                return;
            }
            if (index >= 10 && index <= 12) {
                serverMessage("Chưa thể học kỹ năng này");
                return;
            }
            SkillClan skillClan = DataCenter.gI().skillClans[index];
            if (skillClan == null) {
                return;
            }
            if (clan.skillClans.contains(skillClan)) {
                serverMessage("Kĩ năng đã học");
                return;
            }
            if (skillClan.price > this.clan.getCoin()) {
                service.serverMessage("Không đủ Ballz");
                return;
            }
            if (clan.getMemberByName(name).getType() != Clan.TYPE_TOCTRUONG) {
                service.serverMessage("Chỉ thần huỷ diệt mới có thể học kĩ năng vũ trụ");
                return;
            }
            clan.addCoin(-skillClan.price);
            clan.writeLog(name + " đã học kĩ năng " + skillClan.name + " ngân sách trừ " + Util.formatMoney(skillClan.price) + " Ballz");
            clan.skillClans.add(skillClan);
            getService().sendInfoVuTru();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeSkin() {
        try {
//            byte idCaiTrang = ms.reader().readByte();
//            System.out.println("idCT: " + idCaiTrang);
            if (zeni < 60) {
                getService().serverMessage("Bạn không đủ zeni để kích hoạt");
                return;
            }
            addZeni(-60, true);
            int timeCurrent = (int) (System.currentTimeMillis() / 1000);
            timeWaitChangeSkin = timeCurrent;
            timeChangeSkin = timeCurrent + 604800;
            Message message = new Message((byte) -123);
            message.writer().writeByte(-24);
            message.writer().writeInt(timeChangeSkin);
            message.writer().writeInt(timeWaitChangeSkin);
            getService().sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidItems(Item item1, Item item2) {
        if (item1 == null || item2 == null) {
            return false;
        }
        if (item1.getTemplate().type != item2.getTemplate().type) {
            return false;
        }
        if (!item1.isTypeClothe() || !item2.isTypeClothe()) {
            return false;
        }
        if (item1.strOption.isEmpty() || item2.strOption.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean hasRequiredFusionStones(int indexDa) {
        return indexDa != -1 && bag[indexDa].getQuantity() >= 150;
    }

    public int getIndexItemByIdInBag(int id) {
        for (Item item : this.bag) {
            if (item != null && item.id == id) {
                return item.index;
            }
        }
        return -1;
    }

    public void hopNhatItem(Message ms) {
        lockItem.lock();
        try {
            byte type_1 = ms.reader().readByte();
            short index_1 = ms.reader().readShort();
            byte type_2 = ms.reader().readByte();
            short index_2 = ms.reader().readShort();
            Item item1 = checkBag(type_1)[index_1];
            Item item2 = checkBag(type_2)[index_2];

            if (!isValidItems(item1, item2)) {
                serverMessage("Các món đồ không hợp lệ để hợp nhất");
                return;
            }

            if (item1.isKham() || item2.isKham()) {
                serverDialog(
                        "Không thể hợp nhất trang bị đã khảm. Vui lòng tách khảm trước khi hợp nhất tránh việc mất đá khảm");
                return;
            }
            if (item1.isHopNhat() || item2.isHopNhat()) {
                serverDialog("Không thể hợp nhất trang bị đã hợp nhất");
                return;
            }

            int indexDa = getIndexItemByIdInBag(829);
            Item dhn = getItemInBag(958);
            int levelhn = 0;
            if (!hasRequiredFusionStones(indexDa)) {
                serverMessage("Không đủ đá hợp nhất cần 150 " + bag[indexDa].getTemplate().name);
                return;
            }
            ItemOption[] itemOptions = item1.getItemOption();
            if (itemOptions == null) {
                return;
            }
            Vector<ItemOption> itemOptions1 = new Vector<>();
            Vector<ItemOption> itemOptions2 = new Vector<>();

            ItemOption var5 = null;
            for (int i = 0; i < itemOptions.length; i++) {
                ItemOption op = itemOptions[i];
                if (op.option[0] == 370) {
                    op.option[1] += 1;
                    levelhn = op.option[1];
                    var5 = op;
                }
                if (op.getItemOptionTemplate().type == 18) {
                    itemOptions1.add(op);
                } else {
                    itemOptions2.add(op);
                }
            }
            if (itemOptions1.size() == 0) {
                var5 = new ItemOption("370,1,-1");
                itemOptions1.add(var5);
            } else if (dhn == null && levelhn > 1) {
                serverMessage("Không đủ đá hợp nhất");
                return;
            }
            itemOptions = item2.getItemOption();
            if (itemOptions == null) {
                return;
            }
            if (levelhn > 1) {
                removeItem(dhn.index, 1, true);
            }

            Vector var10 = new Vector();
            int var9;
            for (var9 = 0; var9 < DataCenter.gI().itemOptionTemplate.length; ++var9) {
                if (DataCenter.gI().itemOptionTemplate[var9].type == 18) {
                    var10.add(DataCenter.gI().itemOptionTemplate[var9]);
                }
            }

            for (int i = 0; i < itemOptions.length; i++) {
                ItemOption op = itemOptions[i];
                if (op.option[0] == 370) {
                    var5.option[1] += op.option[1];
                }
                if (op.getItemOptionTemplate().type == 17) {
                    for (int var7 = 0; var7 < var10.size(); ++var7) {
                        if (((ItemOptionTemplate) var10.get(var7)).text.equals(op.getItemOptionTemplate().text)) {
                            Item.a(itemOptions1, new ItemOption(
                                    ((ItemOptionTemplate) var10.get(var7)).id + "," + op.option[1] + ",-1"));
                            break;
                        }
                    }
                } else if (op.getItemOptionTemplate().type == 18) {
                    Item.a(itemOptions1, op);
                }
            }
            itemOptions2.addAll(itemOptions1);
            removeItem(indexDa, 150, true);
            boolean isThanhCong = false;
            if (Util.nextInt(0, 100) < 20) {
                item1.strOption = Item.creatOption(itemOptions2);
                checkBag(type_2)[index_2] = null;
                isThanhCong = true;
            }
            getService().updateItemHopNhat(isThanhCong, item2, type_2, item1, type_1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockItem.unlock();
        }
    }

    public void hopNhatTrangBi(Message ms) {
        lockItem.lock();
        try {
            byte type = ms.reader().readByte();
            short index = ms.reader().readShort();
            byte type2 = ms.reader().readByte();
            short index2 = ms.reader().readShort();
            Item itemGoc = checkBag(type)[index];
            Item itemHopNhat = checkBag(type2)[index2];

            if (type == type2 && index == index2 || itemGoc == null || itemHopNhat == null) {
                return;
            }
            ItemOption op1 = itemGoc.getOption(370);
            ItemOption op2 = itemHopNhat.getOption(370);
            Item dhn = getItemInBag(958);
            int levelHN = 1;
            if (op1 != null || op2 != null) {
//                serverDialog("Trang bị chỉ có thể hợp nhất một lần!");
//                return;

                if (op1 != null && op2 != null || (op1 != null && op1.getParam() >= 2 || op2 != null && op2.getParam() >= 2)) {
                    serverDialog("Trang bị đạt giới hạn!");
                    return;
                }

                if (dhn == null) {
                    serverDialog("Thiếu đá hợp nhất!");
                    return;
                }

                levelHN = 2;
            }

            Item dcq = getItemInBag(829);

            if (dcq == null || dcq.getQuantity() < 150) {
                serverDialog("Thiếu đá cực quang xanh!");
                return;
            }

            if (getSlotNull() <= (listOptionKham(itemHopNhat).size() - 1)) {
                serverDialog("Hành trang đã đầy!");
                return;
            }
            if (Util.nextInt(0, 100) <= 20) {
                if (levelHN == 1) {
                    itemGoc.addOption(370, levelHN);
                } else {
                    ItemOption ohn = itemGoc.getOption(370);
                    ohn.setValue(levelHN);
                    itemGoc.replaceOptionById(ohn);
                }
                if (itemHopNhat.isDoThanLinh()) {
                    for (int i = (listOptionGoc(itemHopNhat).size() - 4); i < listOptionGoc(itemHopNhat).size(); i++) {
//                        itemGoc.addOption(convertNewIdItemOption(listOptionGoc(itemHopNhat).get(i).getId()), listOptionGoc(itemHopNhat).get(i).getParam());
                        int idOption = convertNewIdItemOption(listOptionGoc(itemHopNhat).get(i).getId());
                        int param = listOptionGoc(itemHopNhat).get(i).getParam();
                        ItemOption o = itemGoc.getOption(idOption);
                        if (o == null) {
                            itemGoc.addOption(idOption, param);
                        } else {
                            o.addValue(param);
                            itemGoc.replaceOptionById(o);
                        }
                    }
                } else {
                    for (int i = 0; i < listOptionGoc(itemHopNhat).size(); i++) {
                        int idOption = convertNewIdItemOption(listOptionGoc(itemHopNhat).get(i).getId());
                        int param = listOptionGoc(itemHopNhat).get(i).getParam();
                        ItemOption o = itemGoc.getOption(idOption);
                        if (o == null) {
                            itemGoc.addOption(idOption, param);
                        } else {
                            o.addValue(param);
                            itemGoc.replaceOptionById(o);
                        }
                    }
                }
                for (ItemOption io : listOptionKham(itemHopNhat)) {
                    int var1 = io.h();
                    if (var1 >= 0) {
                        int level = io.option[3];
                        int soLuongKham = 0;
                        for (int i = 0; i <= level; ++i) {
                            soLuongKham += DataCenter.gI().ar[i];
                        }
                        Item daKham = new Item(convertOtpKhamToIdItem(io.getId()), true, soLuongKham, "refund hop nhat");
                        daKham.setQuantity(soLuongKham);
                        addItemToBag(daKham);
                        getService().addItem(daKham);
                    } else {
                        Item daKham = new Item(convertOtpKhamToIdItem(io.getId()), true, "refund hop nhat");
                        addItemToBag(daKham);
                        getService().addItem(daKham);
                    }
                }
                getService().hopNhatItem(true, itemHopNhat, itemGoc, type, type2);
                checkBag(type2)[index2] = null;
                removeItem(itemHopNhat.index, 1, true);
                removeItem(dcq.index, 150, true);
                if (levelHN > 1) {
                    removeItem(dhn.index, 1, true);
                }
                updateBagandBody();
            } else {
                getService().hopNhatItem(false, itemGoc, itemHopNhat, type, type2);
                removeItem(dcq.index, 150, true);
                if (levelHN > 1) {
                    removeItem(dhn.index, 1, true);
                }
                updateBagandBody();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lockItem.unlock();
//            updateBagandBody();
        }
    }

    public void updateBagandBody() {
        getService().updateItemBodyChange();
        this.getService().showInfo2(this);
        sortItemBag();
        getService().sortItem((byte) 0);
        getService().sendInfo(this.user);
    }

    public List<ItemOption> listOptionGoc(Item item) {
        List<ItemOption> listOTP = new ArrayList<>();
        if (item.options() != null) {
            for (ItemOption io : item.options()) {
                if (io.typeChiSoGoc()) {
                    listOTP.add(io);
                }
            }
        }
        return listOTP;
    }

    public List<ItemOption> listOptionKham(Item item) {
        List<ItemOption> listOTP = new ArrayList<>();
        if (item.options() != null) {
            for (ItemOption io : item.options()) {
                if (io.typeChiSoKham()) {
                    listOTP.add(io);
                }
            }
        }
        return listOTP;
    }

    public int convertNewIdItemOption(int id) {
        if (id >= 335 && id <= 344) {
            return id + 25;
        } else if (id == 402) {
            return 438;
        }
        return -1;
    }

    public int convertOtpKhamToIdItem(int id) {
        if (id >= 199 && id <= 206) {
            return id + 207;
        }
        return -1;
    }

    public void sendMail(Message ms) {
        lockItem.lock();
        try {
            String receiver = ms.readUTF();
            String title = ms.readUTF();
            String content = ms.readUTF();
            int ballzSend = ms.reader().readInt();
            int index = ms.reader().readShort();
            if (user.activated == 1) {
                serverDialog("Tính năng này cần mtv ở Bulma để sử dụng");
                return;
            }
            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            if (receiver == null || receiver.isEmpty() || receiver.equals(name)) {
                return;
            }

            if (ballzSend < 0) {
                return;
            }

            Char pl = ServerManager.findCharByName(receiver);
            if (pl == null) {
                serverDialog("Người nhận không tồn tại");
                return;
            }

            if (pl == this) {
                serverDialog("Không thể gửi thư cho chính mình");
                return;
            }

            if (pl.user.activated == 1) {
                serverDialog("Người chơi bạn gửi chưa mở tài khoản");
                return;
            }
            int fee = 10;
            if ((ballzSend + fee) > ballZ) {
                serverDialog("Không đủ Ballz");
                return;
            }

            Item item = null;
            String str = " ";
            if (index >= 0 && index < bag.length) {
                if (bag[index] == null) {
                    serverDialog("Vật phẩm không tồn tại");
                    return;
                }
                if (bag[index].template.isBlackListItem() || bag[index].isLock) {
                    serverDialog("Không thể gửi thư vật phẩm này.");
                    return;
                }
                item = bag[index].clone();
                str = item.getTemplate().name + " số lượng: " + item.getQuantity();
                removeItem(index, item.getQuantity(), true);
            }
            addBallz(-fee, true);
            addBallz(-ballzSend, true);
            ballzSend -= ballzSend / 100;
            Mail mail = pl.createMail(name, title, content, ballzSend, 0, 0, item);
            pl.mailManager.addMail(mail);
            getService().resetScreen();
            getService().updateIndexBag();
            serverDialog("Thư đã được gửi đến " + receiver);
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
            String strDate = formatter.format(date);
            Util.writing("loggame/guithu/" + name + ".txt", "Người nhận " + receiver + " Gửi đi: " + ballzSend + " Ballz và Item :" + str + " thời gian: " + strDate + "\n");
            insertLogToDataBase(this.name, pl.name, item, ballzSend);
        } catch (Exception e) {
            Log.error("Lỗi khi gửi thư: " + e.getMessage(), e);
        } finally {
            lockItem.unlock();
        }
    }

    public void insertLogToDataBase(String name1, String name2, Item item, int coin) {
        try (Connection conn = DbManager.getInstance().getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO historysendmail (playerSend, playerRevice, itemSend, ballzSend) VALUES (?, ?, ?, ?)");
            try {
                stmt.setString(1, name1);
                stmt.setString(2, name2);
                StringBuilder item_1 = new StringBuilder();
                if (item != null) {
                    int quantity = item.getQuantity();
                    item_1.append(item.getTemplate().name).append(" với số lượng ").append(quantity).append("\n");
                }
                stmt.setString(3, item_1.toString());
                stmt.setInt(4, coin);
                stmt.executeUpdate();
            } finally {
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openUIStall(Message mss) {
        try {
            byte var = mss.reader().readByte();
            byte type = mss.reader().readByte();
            short index = mss.reader().readShort();
            StallManager.getInstance().openUI(this, var, type, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saleItemToStall(Message mss) {
        lockItem.lock();
        try {
            if (user.activated == 1) {
                serverDialog("Tính năng này cần mtv ở Bulma để sử dụng");
                return;
            }
            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }

            short index = mss.reader().readShort();
            byte type = mss.reader().readByte();
            int price = mss.reader().readInt();
            if (index < 0 || index >= bag.length) {
                return;
            }
            if (price < 0) {
                return;
            }
            if (bag[index] == null) {
                serverDialog("Vật phẩm không tồn tại");
                return;
            }
            int time = type == 0 ? 8 : type == 1 ? 16 : type == 2 ? 24 : type == 3 ? 48 : 72;
            Item item = bag[index];
            if (price > 999999999) {
                serverDialog("Giá bán không được quá 99.999.999 Ballz");
                return;
            }
            if (item.getTemplate().id == 177) {
                serverDialog("Không thể treo bán vật phẩm đã khoá");
                return;
            }
            if (item.isLock) {
                serverDialog("Không thể treo bán vật phẩm đã khoá");
                return;
            }
            if (item.template.isBlackListItem() || item.isLock) {
                serverDialog("Không thể bán vật phẩm này.");
                return;
            }
            int fee = 10;
            if (ballZ < fee) {
                serverDialog("Không đủ phí để treo bán");
                return;
            }
            addBallz(-fee, true);
            bag[index] = null;
            StallManager.getInstance().themItem(this, item, price, (int) TimeUnit.HOURS.toMillis(time));
            getService().sellMarket(index);
            getService().updateIndexBag();
            StallManager.getInstance().openUIMeSell(this);
        } catch (Exception e) {

        } finally {
            lockItem.unlock();
        }
    }

    public void buyItemStall(Message mss) {
        try {
            if (user.activated == 1) {
                serverDialog("Tính năng này cần mtv ở Bulma để sử dụng");
                return;
            }
            if (!isActiveAction()) {
                serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
                return;
            }
            long id = mss.reader().readLong();
            StallManager.getInstance().buyItem(this, id);
        } catch (Exception e) {

        }
    }

    public void raoBan(Message mss) {
        try {
            if (timeRaoBan > System.currentTimeMillis()) {
                serverMessage("Vui lòng đợi 15p sau khi rao bán mới có thể rao tiếp");
                return;
            }
            if (ballZ < 500) {
                getService().warningMessage("Không đủ Ballz");
                return;
            }
            addBallz(-500, true);
            timeRaoBan = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15);
            long id = mss.reader().readLong();
            Stall item = StallManager.getInstance().find(id);
            if (item != null) {
                GlobalService.getInstance().raoBan(name, "Cần bán c#item" + item.getItem().getTemplate().name + "c#white giá " + Util.formatMoney(item.getPrice()) + " Ballz", (byte) 0, item);
            }
        } catch (Exception e) {
        }

    }

    public void addFriend(Message ms) {
        try {
            String nameInvite = ms.readUTF();
            Char _char = ServerManager.findCharByName(nameInvite);

            if (_char == null) {
                service.serverMessage("Người này không online hoặc không tồn tại!");
                return;
            }
            if (_char == this) {
                return;
            }
            Friend friend = friends.get(nameInvite);
            if (friend != null) {
                service.serverMessage(nameInvite + " đã có trong danh sách bạn bè.");
                return;
            }
            Friend me = _char.friends.get(name);
            if (me != null) {
                me.type = 1;
                friends.put(_char.name, new Friend(_char.name, (byte) 1, true));
                getService().addFriend(nameInvite, 1, true);
                _char.getService().removeFriend(name);
                getService().removeFriend(nameInvite);
                _char.getService().addFriend(name, 1, true);
                return;
            } else {
                friends.put(_char.name, new Friend(_char.name, (byte) 0, false));
            }
            getService().addFriend(nameInvite, 0, false);
            _char.getService().inviteFriend(name, 2);
            serverMessage("Đã gửi lời mời kết bạn đến " + nameInvite);
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }
    }

    public void removeFriend(Message ms) {
        try {
            String nameInvite = ms.readUTF();
            Char _char = ServerManager.findCharByName(nameInvite);
            if (this.friends.get(nameInvite) != null) {
                if (_char != null) {
                    this.friends.remove(nameInvite);
                    getService().removeFriend(nameInvite);
                    if (_char.friends.get(name) != null) {
                        _char.friends.remove(name);
                    }
                    _char.getService().removeFriend(name);
                }
            } else {
                getService().removeFriend(nameInvite);
                if (_char != null) {
                    _char.getService().removeFriend(name);
                    if (_char.friends.get(name) != null) {
                        _char.friends.remove(name);
                    }
                }
            }
        } catch (Exception ex) {
            Log.error("err: " + ex.getMessage(), ex);
        }

    }

    public Friend[] getFriends() {
        if (this.friends == null) {
            return new Friend[0];
        }
        return this.friends.values().toArray(new Friend[this.friends.size()]);
    }

    public void exchangeCoinToZeni(int coin, int zeni) {
        user.coin = DbManager.getInstance().getCoin(user.username);
        if (user.coin < coin) {
            serverDialog("Yêu cầu tối thiểu " + Util.getCurrency(coin) + " coin.");
            return;
        }
        user.coin -= coin;
        DbManager.getInstance().updateCoin(user.username, coin);
        addZeni(zeni, true);
        int old = mocNap;
        mocNap += zeni;
        napNgay += zeni;

        String sql = "UPDATE `users` SET `napthang` = `napthang` + ? WHERE `username` = ?";
        try (Connection conn = DbManager.getInstance().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zeni);
            stmt.setString(2, user.username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.error("updateNapthang() EXCEPTION: " + e.getMessage(), e);
        }
        countVongQuay += (short) ((mocNap / 1000) - (old / 1000));
        preLoad();
    }

    public List<Item> getBagListById(int itemID) {
        List<Item> items = new ArrayList<>();
        for (Item item : bag) {
            if (item != null && item.id == itemID) {
                items.add(item);
            }
        }
        return items;

    }

    public void addEventPoint(KeyPoint key, int point) {
        Optional.ofNullable(eventPoint).ifPresent(eventPoint -> eventPoint.addPoint(key, point));
    }

    public void updatePoint(KeyPoint key, int point) {
        Optional.ofNullable(eventPoint).ifPresent(eventPoint -> eventPoint.Point(key, point));
    }

    public boolean isContainsTitle(String title) {
        return danhHieu.stream().anyMatch(dh -> dh.title.equals(title));
    }
}

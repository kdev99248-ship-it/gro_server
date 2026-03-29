package com.vdtt.mob;

import com.vdtt.data.DataCenter;
import com.vdtt.effect.Effect;
import com.vdtt.events.EventHandler;
import com.vdtt.events.SummerParty;
import com.vdtt.events.TrungThu2024;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.handler.UseItemHandler;
import com.vdtt.handler.item.KICloud;
import com.vdtt.item.Item;
import com.vdtt.map.item.ItemMap;
import com.vdtt.map.item.ItemMapFactory;
import com.vdtt.map.world.destructionoftheuniverse.DestructionoftheUniverseArea;
import com.vdtt.map.world.planetblackstar.ZPlanetBlackStar;
import com.vdtt.map.zones.*;
import com.vdtt.model.Char;
import com.vdtt.model.ObjectLive;
import com.vdtt.model.RandomItem;
import com.vdtt.network.Message;
import com.vdtt.server.GlobalService;
import com.vdtt.task.TaskOrder;
import com.vdtt.util.Log;
import com.vdtt.util.TimeUtils;
import com.vdtt.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mob extends ObjectLive implements Cloneable {

    public static final byte XU = 0;
    public static final byte ITEM = 1;
    public static final byte ITEM_TASK = 2;
    public static final byte BOSS = 4;
    public static final byte EQUIP = 5;
    public static final byte BOSS_BARRACK = 6;
    public int id;
    public boolean isPaint;
    public int level;
    public int hpGoc;
    public int hpFull;
    public int levelBoss;
    public byte status;
    public long timeReSpawn;
    public int kiNow = 0;
    public Mob mob2;
    public int x;
    public int y;
    public boolean isDead;
    public int recoveryTimeCount;
    public Zone zone;
    public Vector<Integer> chars = new Vector<>();
    public boolean isCantRespawn;
    public Lock lock = new ReentrantLock();
    public int hp;
    public long lastTimeAttack;
    public int attackDelay = 20000;
    private int dameMin = 1;
    public int dameMax = 1;
    public int kiMax;
    public String nameCall = "";
    public long timeRemove = -1;
    public ConcurrentHashMap<Short, Effect> effects = new ConcurrentHashMap<>();
    public HashMap<Char, Integer> topDameBoss = new HashMap<>();

    public Mob() {
    }

    public Mob(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        this.idEntity = idEntity;
        this.id = id;
        this.hp = hp;
        this.hpFull = hp;
        this.level = level;
        this.x = x;
        this.y = y;
        this.hpGoc = hp;
        this.levelBoss = 7;
        this.status = 0;
        this.kiMax = kiNow = kiMax;
        dameMax = kiMax / 50;
        if (id == 273) {
            dameMax = 500;
        }
        dameMin = dameMax * 90 / 100;

    }

    public void readJson(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.isPaint = json.getBoolean("isPaint");
            this.level = json.getInt("level");
            this.hpFull = json.getInt("hpGoc");
            this.hp = json.getInt("hpFull");
            this.levelBoss = json.getInt("typeMob");
            this.x = (short) json.getInt("x");
            this.y = (short) json.getInt("y");
            this.status = 2;
            loadKiNow();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void mobReload() {
        kiNow = DataCenter.gI().mobTemplate[this.id].timeThuHoach * 15;
        if (kiMax > 0) {
            kiNow = kiMax;
        }
    }

    public void loadKiNow() {
        kiNow = DataCenter.gI().mobTemplate[this.id].timeThuHoach * 15;
        dameMax = DataCenter.gI().mobTemplate[this.id].timeThuHoach * 5;
        dameMin = dameMax * 90 / 100;
    }

    public Mob clone() {
        try {
            return (Mob) super.clone();
        } catch (Exception var2) {
            return null;
        }
    }

    public void write(Message m) {
        try {
            m.writer().writeShort(this.idEntity);
            m.writer().writeBoolean(isPaint);
            m.writeUTF(nameCall);
            m.writer().writeShort(id);
            m.writer().writeShort(x);
            m.writer().writeShort(y);

            m.writer().writeShort(level);
            m.writer().writeByte(0);
            m.writer().writeByte(getStatus());
            m.writer().writeInt(hp);
//            if(levelBoss == 1||levelBoss == 2||levelBoss == 10){
//                m.writer().writeInt(hp);
//            }else
            m.writer().writeInt(hpFull);
            m.writer().writeByte(levelBoss);
            m.writer().writeByte(0);
        } catch (IOException e) {

        }
    }

    public MobTemplate getTemplate() {
        return DataCenter.gI().mobTemplate[this.id];
    }

    private void attack() {
        Vector<Char> list = new Vector<>();
        Vector<Char> chars = getChars();
        for (Char _char : chars) {
            if (_char.isCleaned) {
                continue;
            }
            if (_char.isDead) {
                continue;
            }
            if (Math.abs(this.x - _char.x) > 150 || Math.abs(this.y - _char.y) > 150) {
                continue;
            }
            list.add(_char);
        }
        if (list.isEmpty()) {
            return;
        }
        int rand = Util.nextInt(list.size());
        Char pl = list.get(rand);
        attack(pl, null);
    }

    public void attack(Char pl, Char owner) {
        if (pl != null && !pl.isDead) {
            pl.lock.lock();
            try {
                int dameHp = Util.nextInt(dameMin, dameMax);
                int level = this.level;
                if (zone.map.isHell()) {
                    dameHp *= 2;
                }
                // level = this.level > 0 ? this.level : 1;
                int exactly = Util.nextInt((level * 100) + 100);
                if (pl.miss > 2) {
                    int miss = Util.nextInt(pl.miss);
                    boolean isMiss = exactly < miss;
                    if (isMiss && !zone.map.isHell()) {
                        dameHp = -1;
                    } else {
                        if (dameHp <= 0) {
                            dameHp = 1;
                        }
                    }
                }
                if (pl.getEm().findByID(113) != null) {
                    dameHp = 0;
                }
                attack(pl);
                dameHp -= pl.reduceDame;
                if (dameHp > 0) {
                    pl.hp -= dameHp;
                    if (pl.hp <= 0) {
                        pl.startDie();
                    }
                    zone.getService().npcAttackPlayer(this, pl, pl.mp, pl.hp, false);
                }
            } finally {
                pl.lock.unlock();
            }
        }
    }

    public void attack(Char p) {
        try {
            zone.getService().npcAttackPlayer(this, p);
        } catch (Exception e) {
            Log.error("mob attack er", e);
        }
    }

    public boolean checkExist(int charId) {
        for (int id : chars) {
            if (id == charId) {
                return true;
            }
        }
        return false;
    }

    public Vector<Char> getChars() {
        Vector<Char> chars = new Vector<>();
        Vector<Integer> clone = (Vector<Integer>) this.chars.clone();
        for (int id : clone) {
            Char _char = zone.findCharById(id);
            if (_char != null) {
                chars.addElement(_char);
            }
        }
        return chars;
    }

    public void die() {
        this.hp = 0;
        this.status = 4;
        this.recoveryTimeCount = 12;
        this.isDead = true;
        this.chars.clear();
        this.zone.getService().updateMobDie(this);
    }

    public int getStatus() {
        return !isPaint && hp > 0 ? 0 : 4;
    }
    
    private int countDropSCD = 500;
    private long timeResetItem = System.currentTimeMillis();

    public void dead(Char killer, boolean isDeTu) {
        try {
            if (kiNow < 0) {
                kiNow = 0;
            }
            if (isDeTu) {
                if (killer != null) {
                    killer.addEXP(kiNow);
                }
            } else if (killer != null) {
                if (zone instanceof ZPlanetBlackStar) {
                    dropItem(killer, ITEM);
                    zone.mobDead(this, killer);
                    return;
                }
                float percentXu = 1 + ((float) (killer.increaseXu / 10) / 100);
                int kiAdd = kiNow * 2 + kiNow * (killer.increaseKI / 10) / 100;
                if (killer.lvPK > 0) {
                    kiAdd -= kiAdd * killer.lvPK / 100;
                }
                byte dLevel = (byte) Math.abs(getTemplate().timeThuHoach - killer.level());
                if (dLevel > 8 && !zone.map.isWorld() && !(zone instanceof PrimaryForest) && !(this instanceof Cell || this instanceof CellMax || this instanceof ThoDaiCa || this instanceof Dracubin) && levelBoss != 7) {
                    killer.getService().notifyNoUpKi();
                } else {
                    killer.addEXP(kiAdd);
                    Effect effect = killer.getEm().findByID(142);
                    if (effect != null) {
                        kiAdd += kiAdd * 80 / 100;
                    }
                    if (killer.pet != null) {
                        killer.pet.addEXP(kiAdd);
                    }
                    if (killer.getGroup() != null) {
                        long expGroup = kiAdd / 5;
                        List<Char> chars = killer.getGroup().getCharsInZone(zone.map.id, zone.id);
                        for (Char _char : chars) {
                            if (_char != null && !_char.isCleaned && _char != killer && !_char.isDead) {
                                _char.addEXP(expGroup);
                            }
                        }
                    }
                    
                    if(Util.nextInt(100) < 1)
                        KICloud.upPower(killer, levelBoss == 0 ? 1 : levelBoss);
                    
                    if(TimeUtils.canDoWithTime(timeResetItem,36000000)) {
                        countDropSCD = 500;
                        timeResetItem = System.currentTimeMillis();
                    }
                    
                 if (Util.nextInt(1000) == 0 && countDropSCD > 0) { // 0.1% (1/1000 cơ hội rơi)
                        countDropSCD--;
                        Item item = new Item(434, "mob");
                        item.setQuantity(1);
                        item.isLock = false;
                        ItemMap itemMap = ItemMapFactory.getInstance()
                                .builder()
                                .id(zone.numberDropItem++)
                                .x((short) (x + Util.nextInt(-30, 50)))
                                .y((short) (y))
                                .build();
                        itemMap.setOwnerID(killer.id);
                        itemMap.setItem(item);
                        zone.addItemMap(itemMap);
                        zone.getService().addItemMap(itemMap, killer);
                    }
                }
                if (Util.nextInt(100) < 10) {
                    if (this.zone.map.id == 63 || this.zone.map.id == 64 || this.zone.map.id == 65 || this.zone.map.id == 55
                            || this.zone.map.id == 61 || this.zone.map.id == 57 || this.zone.map.id == 62 || this.zone.map.id == 66) {
                        zone.addMobForWaitingListRespawn(this);
                        zone.mobDead(this, killer);
                        return;
                    }
                }
                if (isDropItem(killer.exp)) {
                    if (dLevel <= 8) {
                        int[] percents = {4, 6, 2, 88};
                        byte[] types = {Mob.XU, Mob.ITEM, Mob.EQUIP, -1};
                        int index = Util.randomWithRate(percents, 100);
                        byte type = types[index];
                        if (type != -1 && type != Mob.XU && !zone.map.isTrainZone()) {
                            dropItem(killer, type);
                        } else if (type == Mob.XU) {
                            killer.addCoin((int) (Util.nextInt(10000) * percentXu), true);
                        }
                        if (EventHandler.isEvent()) {
                            EventHandler.dropItem(killer, this);
                        }
                        if (this.levelBoss == 1) {
                            killer.addCoin((int) (Util.nextInt(10000) * percentXu), true);
                            Item item = new Item(838, "mob");
                            item.setQuantity(1);
                            item.isLock = false;
                            ItemMap itemMap = ItemMapFactory.getInstance()
                                    .builder()
                                    .id(zone.numberDropItem++)
                                    .x((short) (x + Util.nextInt(-30, 50)))
                                    .y((short) (y))
                                    .build();
                            itemMap.setOwnerID(killer.id);
                            itemMap.setItem(item);
                            zone.addItemMap(itemMap);
                            zone.getService().addItemMap(itemMap, killer);
                            killer.totalTA++;
                            killer.achievements.increaseAchievementCount(15, 1);
                            killer.achievements.increaseAchievementCount(16, 1);
                        } else if (this.levelBoss == 2) {
                            for (int i = 0; i < 6; i++) {
                                Item item = null;
                                if (killer.exp < 2_000_000) {
                                    item = new Item(RandomItem.ITEM_THU_LINH_DOWN200.next(), "mob");
                                } else {
                                    item = new Item(RandomItem.ITEM_THU_LINH.next(), "mob");
                                }
                                item.setQuantity(1);
                                item.isLock = false;
                                ItemMap itemMap = ItemMapFactory.getInstance()
                                        .builder()
                                        .id(zone.numberDropItem++)
                                        .x((short) (x + Util.nextInt(-30, 50)))
                                        .y((short) (y))
                                        .build();
                                itemMap.setOwnerID(killer.id);
                                itemMap.setItem(item);
                                zone.addItemMap(itemMap);
                                zone.getService().addItemMap(itemMap, killer);
                            }
                            killer.addCoin((int) (Util.nextInt(10000) * percentXu), true);
                            killer.totalTL++;
                            killer.achievements.increaseAchievementCount(17, 1);
                            killer.achievements.increaseAchievementCount(18, 1);
                        } else if (this.levelBoss == 10) {
                            Item item = null;
                            if (killer.exp < 2_000_000) {
                                item = new Item(RandomItem.ITEM_THU_LINH_DOWN200.next(), "mob");
                            } else {
                                item = new Item(RandomItem.ITEM_THU_LINH.next(), "mob");
                            }
                            item.setQuantity(1);
                            item.isLock = false;
                            ItemMap itemMap = ItemMapFactory.getInstance()
                                    .builder()
                                    .id(zone.numberDropItem++)
                                    .x((short) (x + Util.nextInt(-30, 50)))
                                    .y((short) (y))
                                    .build();
                            itemMap.setOwnerID(killer.id);
                            itemMap.setItem(item);
                            zone.addItemMap(itemMap);
                            zone.getService().addItemMap(itemMap, killer);
                            killer.totalSQ++;
                            killer.achievements.increaseAchievementCount(13, 1);
                            killer.achievements.increaseAchievementCount(14, 1);
                        }
                    }
                }
                if (killer.taskOrders != null && !killer.taskOrders.isComplete()) {
                    TaskOrder task = killer.taskOrders;
                    if (task.killId == id) {
                        if (task.taskId == TaskOrder.TASK_NORMAL) {
                            task.updateTask(1);
                        }
                        if (task.taskId == TaskOrder.TASK_TA_TL) {
                            if (this.levelBoss == 1 || this.levelBoss == 2 || this.levelBoss == 10) {
                                task.updateTask(1);
                            }
                        }
                    }

                }
                if (this instanceof CellMax || this instanceof ThoDaiCa || this instanceof  Dracubin) {
                    killer.addClanPoint(50);
                }
                if (this instanceof Cell) {
                    if (EventHandler.isEvent(SummerParty.class)) {
                        killer.addEventPoint(KeyPoint.KILL_CELL, 1);
                        killer.addCoin((int) ((Util.nextInt(50, 100) * 1000) * percentXu), false);
                        List<Integer> list = new ArrayList<>();
                        int[] numbers = {171, 172, 173, 355, 356, 357, 2, 3, 4};
                        for (int number : numbers) {
                            int count = Util.nextInt(1, 2);
                            for (int i = 0; i < count; i++) {
                                list.add(number);
                            }
                        }
                        if (Util.nextInt(100) < 2) {
                            list.add(374);
                        }
                        for (int i : list) {
                            Item newItem = new Item(i, "cell");
                            if (newItem.getId() == 374) {
                                newItem.strOption = "327,89";
                                newItem.addOptionRandom(379, 5, 12);
                                newItem.addOptionRandom(2, 150, 160);
                                newItem.addOptionRandom(0, 1100, 1300);
                            } else {
                                newItem.setQuantity(1);
                                newItem.expire = -1;
                            }
                            newItem.isLock = false;

                            ItemMap itemMap = ItemMapFactory.getInstance()
                                    .builder()
                                    .id(zone.numberDropItem++)
                                    .x((short) (x + Util.nextInt(-30, 50)))
                                    .y((short) (y))
                                    .build();
                            itemMap.setOwnerID(killer.id);
                            itemMap.setItem(newItem);
                            zone.addItemMap(itemMap);
                            zone.getService().addItemMap(itemMap, killer);
                        }
                    }
                } else if (levelBoss == 7) {
                    if (zone.map.isBarrack()) {
                        for (int i = 0; i < 10; i++) {
                            dropItem(killer, Mob.BOSS_BARRACK);
                        }
                        Item item = new Item(Util.nextInt(1058, 1077), "mob");
                        item.setQuantity(Util.nextInt(1, 3));
                        item.expire = -1;
                        item.isLock = false;
                        int optionId = UseItemHandler.getOptionIdForItemKichHoat(item);
                        item.addOption(optionId, optionId == 469 ? 12 : 1);
                        ItemMap itemMap = ItemMapFactory.getInstance()
                                .builder()
                                .id(zone.numberDropItem++)
                                .x((short) (x + Util.nextInt(-30, 50)))
                                .y((short) (y))
                                .build();
                        itemMap.setOwnerID(killer.id);
                        itemMap.setItem(item);
                        zone.addItemMap(itemMap);
                        zone.getService().addItemMap(itemMap, killer);
                    } else if (zone instanceof PrimaryForest) {
//                        for (int i = 0; i < 10; i++) {
//                            Item item = new Item(RandomItem.ITEM_EVENT_VIP.next(), "mob");
//                            item.setQuantity(1);
//                            item.expire = -1;
//                            item.isLock = false;
//                            ItemMap itemMap = ItemMapFactory.getInstance()
//                                    .builder()
//                                    .id(zone.numberDropItem++)
//                                    .x((short) (x + Util.nextInt(-30, 50)))
//                                    .y((short) (y))
//                                    .build();
//                            itemMap.setOwnerID(killer.id);
//                            itemMap.setItem(item);
//                            zone.addItemMap(itemMap);
//                            zone.getService().addItemMap(itemMap, killer);
//                        }

                        killer.addEventPoint(KeyPoint.KILL_CELL, 100);
                        killer.addCoin((int) ((Util.nextInt(50, 100) * 1000) * percentXu), false);
                        List<Integer> list = new ArrayList<>();
                        int[] numbers = {358, 359, 360, 701, 702, 703, 704, 705, 706, 277, 5, 6, 406, 407, 408, 409, 410, 411, 412, 413, 562, 564, 566, 354, 562, 564, 566, 354};
                        for (int number : numbers) {
                            int count = Util.nextInt(1, 3);
                            for (int i = 0; i < count; i++) {
                                list.add(number);
                            }
                        }
                        if (Util.nextInt(0, 100) < 2) {
                            list.add(374);
                        }
                        for (int i : list) {
                            Item newItem = new Item(i, "cellmax");
                            if (newItem.getId() == 374) {
                                newItem.strOption = "327,89";
                                newItem.addOptionRandom(379, 5, 12);
                                newItem.addOptionRandom(2, 150, 160);
                                newItem.addOptionRandom(0, 1100, 1300);
                            } else {
                                newItem.setQuantity(Util.nextInt(1, 3));
                                newItem.expire = -1;
                            }
                            newItem.isLock = false;
                            ItemMap itemMap = ItemMapFactory.getInstance()
                                    .builder()
                                    .id(zone.numberDropItem++)
                                    .x((short) (x + Util.nextInt(-30, 50)))
                                    .y((short) (y))
                                    .build();
                            itemMap.setOwnerID(killer.id);
                            itemMap.setItem(newItem);
                            zone.addItemMap(itemMap);
                            zone.getService().addItemMap(itemMap, killer);
                        }
                    } else if (id != 273) {
                        GlobalService.getInstance().chat("Hệ thống", "Boss " + getTemplate().name + " đã bị chiến binh " + killer.name + " tiêu diệt tại " + zone.template.name + " khu " + zone.id, (byte) 1);
                        for (int i = 0; i < 45; i++) {
                            dropItem(killer, Mob.BOSS);
                        }
                        killer.pointKame += 2;
                        killer.pointSuper += 2;
                        killer.addClanPoint(2);
                        killer.getService().serverMessage("Bạn đã nhận được 2 điểm Kame");
                        killer.getService().serverMessage("Bạn đã nhận được 2 điểm Super");
                        killer.addBallz(Util.nextInt(20000), true);
                        if (killer.getGroup() != null) {
                            List<Char> chars = killer.getGroup().getCharsInZone(zone.map.id, zone.id);
                            for (Char _char : chars) {
                                if (_char != null && !_char.isCleaned && _char != killer && !_char.isDead) {
                                    _char.pointKame += 2;
                                    _char.getService().serverMessage("Bạn đã nhận được 2 điểm Kame");
                                    _char.pointSuper += 2;
                                    _char.getService().serverMessage("Bạn đã nhận được 2 điểm Super");
                                }
                            }
                        }
                        if (id == 199 && killer.getEm().findByID(162) != null) {
                            Effect effect = killer.getEm().findByID(162);
                            effect.param = 8;
                            killer.zone.getService().playerAddEffect(killer, effect);
                        }
                    } else {
                        GlobalService.getInstance().chat("Hệ thống", "Boss " + getTemplate().name + " đã bị chiến binh " + killer.name + " tiêu diệt", (byte) 1);
                        Item detu = new Item(891, "mob");
                        detu.setQuantity(1);
                        detu.expire = -1;
                        detu.isLock = true;
                        ItemMap itemMap = ItemMapFactory.getInstance()
                                .builder()
                                .id(zone.numberDropItem++)
                                .x((short) (x + Util.nextInt(-30, 50)))
                                .y((short) (y))
                                .build();
                        itemMap.setOwnerID(killer.id);
                        if (detu != null) {
                            itemMap.setItem(detu);
                            zone.addItemMap(itemMap);
                            zone.getService().addItemMap(itemMap, killer);
                        }
                    }
                }
                if (this instanceof ThoDaiCa && !this.getTopDameBoss().isEmpty()) { // xử lý nếu là thỏ đại ca
                    Char[] topChars = new Char[3];
                    for (int i = 0; i < Math.min(3, this.getTopDameBoss().size()); i++) {
                        try {
                            topChars[i] = this.getCharById(this.getTopDameBoss().get(i).id);
                        } catch (Exception e) {
                            // nếu lượng top không đủ thì bỏ qua
                        }
                    }
                    if (topChars[0] != null) {
                        Item banhTrungThu = new Item(1138, false, TrungThu2024.hopBanhTopDame[0], "Thỏ đại ca");
                        topChars[0].addItemToBag(banhTrungThu);
                        topChars[0].getService().addItem(banhTrungThu);
                    }
                    if (topChars[1] != null) {
                        Item banhTrungThu = new Item(1138, false, TrungThu2024.hopBanhTopDame[1], "Thỏ đại ca");
                        topChars[1].addItemToBag(banhTrungThu);
                        topChars[1].getService().addItem(banhTrungThu);
                    }
                    if (topChars[2] != null) {
                        Item banhTrungThu = new Item(1138, false, TrungThu2024.hopBanhTopDame[2], "Thỏ đại ca");
                        topChars[2].addItemToBag(banhTrungThu);
                        topChars[2].getService().addItem(banhTrungThu);
                    }
                    for (Char charz : zone.getChars()) {
                        if (charz != topChars[0] && charz != topChars[1] && charz != topChars[2]) {
                            Item banhTrungThu = new Item(1138, true, TrungThu2024.hopBanhTopDame[3], "Thỏ đại ca");
                            charz.addItemToBag(banhTrungThu);
                            charz.getService().addItem(banhTrungThu);
                        }
                    }
                }
                if (this instanceof Dracubin && !this.getTopDameBoss().isEmpty()) { // xử lý nếu là thỏ đại ca
                    Char[] topChars = new Char[3];
                    for (int i = 0; i < Math.min(3, this.getTopDameBoss().size()); i++) {
                        try {
                            topChars[i] = this.getCharById(this.getTopDameBoss().get(i).id);
                        } catch (Exception e) {
                            // nếu lượng top không đủ thì bỏ qua
                        }
                    }
                    if (topChars[0] != null) {
                        Item banhChung = new Item(950, false, 15, "Thỏ đại ca");
                        topChars[0].addItemToBag(banhChung);
                        topChars[0].getService().addItem(banhChung);
                    }
                    if (topChars[1] != null) {
                        Item banhChung = new Item(950, false, 10, "Thỏ đại ca");
                        topChars[1].addItemToBag(banhChung);
                        topChars[1].getService().addItem(banhChung);
                    }
                    if (topChars[2] != null) {
                        Item banhChung = new Item(950, false, 5, "Thỏ đại ca");
                        topChars[2].addItemToBag(banhChung);
                        topChars[2].getService().addItem(banhChung);
                    }
                    int totalDrop = 15;
                    for(int i = 0; i < totalDrop; i++) {
                        Item item = new Item(328, "mob");
                        item.setQuantity(1);
                        item.expire = -1;
                        item.isLock = false;
                        ItemMap itemMap = ItemMapFactory.getInstance()
                                .builder()
                                .id(zone.numberDropItem++)
                                .x((short) (x + Util.nextInt(-30, 50)))
                                .y((short) (y))
                                .build();
                        itemMap.setOwnerID(killer.id);
                        itemMap.setItem(item);
                        zone.addItemMap(itemMap);
                        zone.getService().addItemMap(itemMap, killer);
                    }
                    
                    if(Util.nextInt(1000) < 1) {
                        int ratio = Util.nextInt(100);
                        int id = -1;
                        if(ratio < 1) {
                            id = 297;
                        } else if(ratio < 10) {
                            id = 296;
                        } else 
                            id = 295;

                        Item item = new Item(id, "mob");
                        item.setQuantity(1);
                        item.expire = -1;
                        item.isLock = false;
                        ItemMap itemMap = ItemMapFactory.getInstance()
                                .builder()
                                .id(zone.numberDropItem++)
                                .x((short) (x + Util.nextInt(-30, 50)))
                                .y((short) (y))
                                .build();
                        itemMap.setOwnerID(killer.id);
                        itemMap.setItem(item);
                        zone.addItemMap(itemMap);
                        zone.getService().addItemMap(itemMap, killer);
                    }
                    
                }
                if (killer.taskMain != null) {
                    killer.updateTaskKillMonster(this);
                    dropItem(killer, ITEM_TASK);
                }
                try {
                    if (killer.getGroup() != null) {
                        List<Char> chars = killer.getGroup().getCharsInZone(zone.map.id, zone.id);
                        for (Char _char : chars) {
                            if (_char != null && !_char.isCleaned && _char != killer) {
                                if (_char.taskMain != null) {
                                    _char.updateTaskKillMonster(this);
                                }
                                if (_char.taskOrders != null && !_char.taskOrders.isComplete()) {
                                    TaskOrder task = _char.taskOrders;
                                    if (task.killId == id) {
                                        if (task.taskId == TaskOrder.TASK_NORMAL) {
                                            task.updateTask(1);
                                        }
                                        if (task.taskId == TaskOrder.TASK_TA_TL) {
                                            if (this.levelBoss == 1 || this.levelBoss == 2 || this.levelBoss == 10) {
                                                task.updateTask(1);
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {

                }
                if (zone != null) {
                    zone.mobDead(this, killer);
                }
                if (id == 202 || id == 85) {// fu & 73
                    killer.addClanPoint(5);
                    killer.addPointSoiNoi(50);
                }
            }
            if (zone != null) {
                if (levelBoss != 7 && !(zone instanceof HellArena) && !(zone instanceof BarrackArea) && !(zone instanceof PrimaryForest) && !(zone instanceof DestructionoftheUniverseArea)) {
                    zone.addMobForWaitingListRespawn(this);
                }
                if (EventHandler.isEvent(SummerParty.class) && this instanceof Cell) {
                    zone.addMobForWaitingListRespawn(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isDropItem(long ki) {
        if (ki <= 200000) {
            return Util.nextInt(100) < 20;
        }
        return true;
    }

    public void dropItem(Char owner, byte type) {
        try {
            if (zone.getNumberItem() > 100) {
                return;
            }
            int itemId = 0;
            if (type == 2) {
                itemId = owner.getIdItemTask(id);
                if (itemId == -1) {
                    return;
                }
            } else if (type == EQUIP) {
                itemId = RandomItem.ITEM_EQUIP.next();
            } else if (type == ITEM) {
                if (owner.exp < 2_000_000) {
                    itemId = RandomItem.STAR_DOWN200.next();
                } else {
                    itemId = RandomItem.SAO_PHA_LE.next();
                }
            } else if (type == BOSS) {
                itemId = RandomItem.BOSS.next();
            } else if (type == BOSS_BARRACK) {
                itemId = RandomItem.BOSS_BARRACK.next();
            }
            Item item = new Item(itemId, "mob");
            if (type == EQUIP) {
                int percent = Util.nextInt(1, 7);
                item.createOptionRandom(Util.nextInt(1, 4), percent);
            }
            item.setQuantity(1);
            item.expire = -1;
            item.isLock = false;
            ItemMap itemMap = ItemMapFactory.getInstance()
                    .builder()
                    .id(zone.numberDropItem++)
                    .x((short) (x + Util.nextInt(-30, 50)))
                    .y((short) (y))
                    .build();
            itemMap.setOwnerID(owner.id);
            itemMap.setItem(item);
            zone.addItemMap(itemMap);
            zone.getService().addItemMap(itemMap, owner);

        } catch (Exception e) {
            Log.error("dropItem", e);
        }
    }

    public void update() {
        if (!this.isDead && id != 154 && id != 221 && id != 213 && this.hp > 0) {
            List<Char> list = zone.getChars();
            if (list.size() > 0) {
                int add = getTemplate().rangeMove;
                if (levelBoss == 10) {
                    add += 100;
                }
                for (Char _char : list) {
                    if (_char.isDead) {
                        continue;
                    }
                    if (Math.abs(this.x - _char.x) < 100 + add && Math.abs(this.y - _char.y) < 100 + add && !chars.contains(_char.id)) {
                        addCharId(_char.id);
                    }
                }
            }
            if (!chars.isEmpty()) {
                long now = System.currentTimeMillis();
                if (now - this.lastTimeAttack > this.attackDelay) {
                    this.lastTimeAttack = now;
                    attack();
                }
            }
        }
        Vector<Short> removeEffect = new Vector<>();
        for (Map.Entry<Short, Effect> entry : this.effects.entrySet()) {
            Effect eff = entry.getValue();
            if (eff == null || eff.isExpired()) {
                removeEffect.add(entry.getKey());
            }
        }
        for (short b : removeEffect) {
            this.effects.remove(b);
            zone.getService().removeEffectMob(idEntity, b);
        }
    }

    public boolean isEffect(short id) {
        return this.effects.containsKey(id);
    }

    public void recovery() {
//        this.itemMap = null;
        this.isDead = false;
        status = 0;
        mobReload();
        setHP();
        this.effects.clear();
    }

    protected void setHP() {

        if (this.levelBoss == 1) {
            this.hpFull = this.hpFull / 40;
            this.kiNow /= 20;
        } else if (this.levelBoss == 10) {
            this.hpFull = this.hpFull / 100;
            this.kiNow /= 20;
        } else if (this.levelBoss == 2) {
            this.hpFull = this.hpFull / 200;
            this.kiNow /= 100;
        }

        int rand = Util.nextInt(5000);
        if (rand <= 5) {
            levelBoss = 2;
        } else if (rand <= 10) {
            levelBoss = 10;
        } else if (rand <= 32) {
            levelBoss = 1;
        } else {
            levelBoss = 0;
        }
        if (this.levelBoss == 1) {
            this.hpFull = this.hpFull * 40;
            this.kiNow *= 20;
        } else if (this.levelBoss == 10) {
            this.hpFull = this.hpFull * 100;
            this.kiNow *= 20;
        } else if (this.levelBoss == 2) {
            this.hpFull = this.hpFull * 200;
            this.kiNow *= 100;
        }
        this.hp = this.hpFull;
    }

    public void addHp(int add) {
        this.hp += add;
    }

    public void addCharId(int charId) {
        if (!chars.contains(charId)) {
            chars.add(charId);
        }
    }

    public void addDameBoss(Char player, int dame) {
        if (this.topDameBoss.containsKey(id)) {
            this.topDameBoss.put(player, this.topDameBoss.get(id) + dame);
        } else {
            this.topDameBoss.put(player, dame);
        }
    }

    public List<Char> getTopDameBoss() { // xử lý list Top dame
        List<Map.Entry<Char, Integer>> sortedEntries = new ArrayList<>(this.topDameBoss.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        List<Char> sortedPlayers = new ArrayList<>();
        for (Map.Entry<Char, Integer> entry : sortedEntries) {
            sortedPlayers.add(entry.getKey());
        }
        return sortedPlayers;
    }

    public Char getCharById(int id) {
        for (Char charz : this.zone.getChars()) {
            if (charz.id == id) {
                return charz;
            }
        }
        return null;
    }
}

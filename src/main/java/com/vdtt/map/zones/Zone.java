package com.vdtt.map.zones;

import com.vdtt.handler.PvPHandler;
import com.vdtt.map.Map;
import com.vdtt.map.MapService;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.item.ItemMap;
import com.vdtt.map.world.planetblackstar.dragon.*;
import com.vdtt.mob.*;
import com.vdtt.model.Char;
import com.vdtt.model.WayPoint;
import com.vdtt.network.Message;
import com.vdtt.npc.Npc;
import com.vdtt.task.MobInfo;
import com.vdtt.task.TaskFactory;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zone {

    @Getter
    private boolean isClosed;
    private List<Char> players;
    private List<ItemMap> itemMaps;
    @Getter
    public List<Mob> monsters;
    private List<ItemMap> listRemoveItem;
    @Getter
    protected List<Npc> npcs;
    public short numberDropItem;
    public int id;
    public byte numberElitez, numberChief;
    public MapTemplate template;
    public Map map;
    public List<Mob> waitingListRecoveries;
    public List<Mob> waitingListDelete;
    public List<Mob> listRecoveries;
    public long lastUpdateEverySecond;
    public long lastUpdateEveryHalfSecond;
    public long lastUpdateEveryFiveSecond;
    public long lastUpdateEveryMinute;
    private ReadWriteLock lockChar;
    private ReadWriteLock lockItem;
    public boolean isOpened;
    public long worldCreatedAt;
    public long worldMaxTime;
    public boolean timeInMap;

    @Getter
    private MapService service;

    //    @Getter
//    private MobFactory mobFactory;
    @Builder
    public Zone(int id, MapTemplate tilemap, Map map) {
        this.id = id;
        this.template = tilemap;
        this.map = map;
        players = new ArrayList<>();
        monsters = new CopyOnWriteArrayList<>();
        itemMaps = new ArrayList<>();
        npcs = new ArrayList<>();
        listRemoveItem = new ArrayList<>();
        waitingListRecoveries = new ArrayList<>();
        waitingListDelete = new ArrayList<>();
        listRecoveries = new ArrayList<>();
        lockChar = new ReentrantReadWriteLock();
        lockItem = new ReentrantReadWriteLock();
        map.addZone(this);
        init();
    }

    public void addNpc(Npc npc) {
        this.npcs.add(npc);
    }


    public void init() {
        this.service = new MapService(this);
        this.numberChief = 0;
        this.numberElitez = 0;
        createMonster();
        createNpc();
    }

    public void createMonster() {
        try {
            for (Mob mob : map.mapTemplate.mobList) {
                if (mob != null) {
                    Mob mob2 = mob.clone();
                    mob2.idEntity = monsters.size();
                    mob2.zone = this;
                    if (map.id == 19) {
                        mob2.hp = mob2.hpFull = 100000;
                    }
                    if (!map.mapTemplate.isPhoBan()) {
                        MobInfo mobInfo = MobInfo.builder().mapID(map.id).mobID(mob2.id).level(mob2.getTemplate().timeThuHoach).name(mob2.getTemplate().name).build();
                        TaskFactory.getInstance().addMobInfoTaskDay(mobInfo);
                    }
                    monsters.add(mob2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNpc() {
        try {
            for (Npc npc : map.mapTemplate.npcList) {
                if (npc != null) {
                    Npc npc2 = npc.clone();
                    npc2.idEntity = npcs.size();
                    npcs.add(npc2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Char findCharById(int id) {
        lockChar.readLock().lock();
        try {
            for (Char c : players) {
                if (c != null && c.id == id) {
                    return c;
                }
            }
        } finally {
            lockChar.readLock().unlock();
        }
        return null;
    }

    public Char findCharName(String name) {
        lockChar.readLock().lock();
        try {
            for (Char c : players) {
                if (c.user != null && c.user.session != null && c.name.equals(name)) {
                    return c;
                }
            }
        } finally {
            lockChar.readLock().unlock();
        }
        return null;
    }

    public void addChar(Char _char) {
        if (players != null) {
            lockChar.writeLock().lock();
            try {
                this.players.add(_char);
            } finally {
                lockChar.writeLock().unlock();
            }
        }
    }

    public List<Char> getChars() {
        ArrayList<Char> chars = new ArrayList<>();
        lockChar.readLock().lock();
        try {
            if (players != null) {
                for (Char c : players) {
                    if (c == null || c.isCleaned) {
                        continue;
                    }
                    chars.add(c);
                }
            }
        } finally {
            lockChar.readLock().unlock();
        }
        return chars;
    }

    public void join(Char p, int typeTau) {
        try {
            Zone preZone = p.zone;
            p.mapId = this.template.id;
            if (preZone != null) {
                if (preZone.map.isWorld() && !map.isWorld()) {
                    p.removeMemberFromWorld(preZone, this);
                }
                preZone.out(p);
                if (preZone.map.id != this.map.id) {
                    int[] xy;
                    if (typeTau == -1) {
                        xy = getArrayXYNext(preZone.map.id);
                    } else {
                        xy = getArrayXYRND();
                    }
                    p.updateXY2(xy);
                }
            }
            p.zone = this;
            if (p.pet != null) {
                p.pet.zone = this;
            }
            // thêm player vào khu cho người chơi khác
            p.getService().sendZone(this, typeTau);
            getService().playerAdd(p);
            getService().sendInfoVuTru(p);
            p.getEm().displayAllEffect(this.getService(), p);
            addChar(p);
            loadMap(p);// load người trong map
            getService().changePk(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void out(Char p) {
        try {
            removeChar(p);
            getService().playerRemove(p.id);
            if (p.IsCuuSat)
            {
                Char cAnCuuSat = findCharById(p.IdCuuSat);
                if (cAnCuuSat != null)
                {
                    cAnCuuSat.getService().sendMessage(PvPHandler.MsgCloseCuuSat(p.id, true));
                    cAnCuuSat.CleanUpCuuSat();
                }
                p.CleanUpCuuSat();
                p.getService().sendMessage(PvPHandler.MsgCloseCuuSat(p.id, true));

            }
            if (p.IsAnCuuSat)
            {
                Char cAnCuuSat = findCharById(p.IdCharMoiCuuSat);
                if (cAnCuuSat != null)
                {
                    cAnCuuSat.getService().sendMessage(PvPHandler.MsgCloseCuuSat(cAnCuuSat.id, true));
                    cAnCuuSat.CleanUpCuuSat();
                }
                p.CleanUpCuuSat();
                p.getService().sendMessage(PvPHandler.MsgCloseCuuSat(p.IdCharMoiCuuSat, true));
            }
            if (p.IsTyVo)
            {
                Char cTyVo = this.findCharById(p.IdTyVo);
                if (cTyVo != null)
                {
                    for (Char c : cTyVo.zone.getChars()) {
                        if(c != null && c.user != null && c.user.session.isConnected()) {
                            c.getService().sendMessage(PvPHandler.MsgCloseTyVo((byte) 4, cTyVo.id, p.id));
                        }
                    }
                    cTyVo.getService().serverMessage(p.name + " đã bỏ chạy khi tỷ võ với " + cTyVo.name);
                    cTyVo.CleanUpTyvo();
                }
                p.CleanUpTyvo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(Char _char) {
        List<Char> chars = getChars();
        for (Char _char2 : chars) {
            try {
                if (_char != _char2) {
//                    _char.getService().playerAdd(_char2);
                    _char2.getEm().displayAllEffect(getService(), _char2);
                    getService().changePk(_char2);
                    getService().sendInfoVuTru(_char2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeItem(ItemMap item) {
        if (itemMaps != null) {
            lockItem.writeLock().lock();
            try {
                itemMaps.remove(item);
            } finally {
                lockItem.writeLock().unlock();
            }
        }
    }

    public void removeChar(Char _char) {
        if (players != null) {
            lockChar.writeLock().lock();
            try {
                players.remove(_char);
            } finally {
                lockChar.writeLock().unlock();
            }
        }

    }

    public void removeMonster(Mob mob) {
        if (monsters != null) {
            monsters.remove(mob);
            getService().removeMonster(mob);
        }
    }

    public void removeItem() {
        try {
            for (ItemMap item : listRemoveItem) {
                try {
                    removeItem(item);
                    getService().removeItem(item.getId());
                } catch (Exception ex) {
                    Logger.getLogger(Zone.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            listRemoveItem.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeChar(Message m) {
        try {
            List<Char> chars = getChars();
            m.writer().writeByte(chars.size());
            for (int i = 0; i < chars.size(); i++) {
                Char pl = chars.get(i);
                m.writer().writeInt(pl.id);
                pl.writeData(m.writer(), m);
            }
        } catch (Exception e) {

        }
    }

    public void writeMob(Message m) {
        try {
            int size = monsters.size();
            m.writer().writeShort(size);
            for (int i = 0; i < size; i++) {
                Mob mob = monsters.get(i);
                mob.write(m);
            }
        } catch (Exception e) {

        }
    }

    public void writeNpc(Message m) {
        try {
            m.writer().writeShort(npcs.size());
            for (Npc npc : npcs) {
                npc.writeMessage(m);
            }
        } catch (Exception e) {

        }
    }

    public void requestChangeMap(@NotNull Char p) {
        try {
            if (!map.mapTemplate.listWayPoint.isEmpty()) {
                WayPoint way = null;
                for (WayPoint wayPoint : map.mapTemplate.listWayPoint) {
                    if (way == null || wayPoint.getRange(p) <= way.getRange(p)) {
                        way = wayPoint;
                    }
                }
                if (way == null || way.getRange(p) > 250) {
                    p.joinZone(p.mapId, 0, 1);
                } else {
                    p.changeMap(way.mapNext);
                }
            } else {
                Log.debug("Không thể chuyển map vì không có waypoint");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getNumberChar() {
        if (!isClosed) {
            return players.size();
        }
        return 0;
    }

    public Mob findMoLivebByID(int idMob) {
        List<Mob> mobs = monsters;
        for (Mob mob : mobs) {
            if (mob.idEntity == idMob && !mob.isDead) {
                return mob;
            }
        }
        return null;
    }

    public Mob findMobAbs(Char _myChar) {
        List<Mob> mobs = monsters;
        for (Mob mob : mobs) {
            if ((Math.abs(_myChar.x - mob.x) <= (mob.getTemplate().rangeMove + 100) && Math.abs(_myChar.y - mob.y) <= (mob.getTemplate().rangeMove + 100) || mob.zone.map.id == 105 || mob.zone.map.id == 103) && !mob.isDead) {
                return mob;
            }
        }
        return null;
    }

    public boolean findBossInZone() {
        List<Mob> mobs = monsters;
        for (Mob mob : mobs) {
            if (!mob.isDead && mob.levelBoss == 7 && mob.id == 273) {
                return true;
            }
        }
        return false;
    }

    public List<Mob> getLivingMonsters() {
        List<Mob> mobs = new ArrayList<>();
        for (Mob mob : monsters) {
            if (!mob.isDead) {
                mobs.add(mob);
            }
        }
        return mobs;
    }

    public void sendAttackMob(int idEntity, int hp, boolean isCrit) {
        try {
            Message message;
            if (idEntity < 127 && hp < Short.MAX_VALUE) {
                message = new Message((byte) 8);
                message.writer().writeByte(idEntity);
                message.writer().writeShort(hp);
            } else {
                message = new Message((byte) 52);
                message.writer().writeShort(idEntity);
                message.writer().writeInt(hp);
            }
            if (isCrit) {
                message.writer().writeBoolean(true);
            } else {
                message.writer().writeBoolean(false);
            }
            getService().sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int[] getArrayXYNext(int mapOLD) {
        try {
            if ((mapOLD == 58 || mapOLD == map.id) && map.id == 56) {
                return new int[]{70, 310};
            }
            if (mapOLD == 56 && map.id == 58) {
                return new int[]{180, 50};
            } else {
                if (map.id == 56) {
                    return new int[]{489, 309};
                }
            }
            if (map.id == 19) {
                return new int[]{140, 230};
            }

            MapTemplate map = this.map.mapTemplate;

            for (int i = 0; i < map.listWayPoint.size(); i++) {
                if (map.listWayPoint.get(i).mapNext == mapOLD) {
                    return new int[]{(map.listWayPoint.get(i).x < 200 ? 35 : map.maxX - 35), map.V.c(map.listWayPoint.get(i).x < 200 ? 35 : map.maxX - 35, map.listWayPoint.get(i).y).y - 1};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new int[]{0, 0};
    }

    public ItemMap findItemMapById(short id) {
        lockItem.readLock().lock();
        try {
            for (ItemMap item : itemMaps) {
                if (item.getId() == id) {
                    return item;
                }
            }
        } finally {
            lockItem.readLock().unlock();
        }
        return null;
    }

    public void addItemMap(ItemMap item) {
        if (itemMaps != null) {
            lockItem.writeLock().lock();
            try {
                this.itemMaps.add(item);
            } finally {
                lockItem.writeLock().unlock();
            }
        }
    }

    public int[] getArrayXYRND() {
        MapTemplate map = this.map.mapTemplate;
        int x = Util.nextInt(50, map.maxX - 200);
        int y = 0;
        try {
            y = map.V.c(x, Util.nextInt(50, map.maxY - 200)).y;
        } catch (Exception e) {
            y = map.maxY - 200;
        }
        return new int[]{x, y};
    }

    public void setSkillPaint_1(Mob mob, Char _char, short skillId) {
        try {
            int idMob = mob.idEntity;
            Message message = new Message((byte) 61);
            message.writer().writeInt(_char.id);
            message.writer().writeShort(skillId);
            message.writer().writeShort(idMob);
            getService().sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {

        }
    }

    public void addMob(Mob mob) {
        try {
            if (monsters == null) {
                return;
            }
            monsters.add(mob);
            getService().addMob(mob);
        } catch (Exception ex) {
            Logger.getLogger(Zone.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getNumberItem() {
        return itemMaps.size();
    }

    public void updateItemMap() {
        try {
            if (!this.itemMaps.isEmpty()) {
                lockItem.readLock().lock();
                try {
                    for (ItemMap item : itemMaps) {
                        try {
                            if (item.isExpired()) {
                                listRemoveItem.add(item);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    lockItem.readLock().unlock();
                }
                if (!listRemoveItem.isEmpty()) {
                    removeItem();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateChar() {
        try {
            if (!this.players.isEmpty()) {
                long now = System.currentTimeMillis();
                boolean isUpdateEverySecond = ((now - this.lastUpdateEverySecond) >= 1000);
                if (isUpdateEverySecond) {
                    this.lastUpdateEverySecond = now;
                }
                boolean isUpdateEveryHalfSecond = ((now - this.lastUpdateEveryHalfSecond) >= 500);
                if (isUpdateEveryHalfSecond) {
                    this.lastUpdateEveryHalfSecond = now;
                }
                boolean isUpdateEveryFiveSecond = ((now - this.lastUpdateEveryFiveSecond) >= 5000);
                if (isUpdateEveryFiveSecond) {
                    this.lastUpdateEveryFiveSecond = now;
                }
                boolean isUpdateEveryMinute = ((now - this.lastUpdateEveryMinute) >= 60000);
                if (isUpdateEveryMinute) {
                    this.lastUpdateEveryMinute = now;
                }
                if (isUpdateEveryHalfSecond || isUpdateEverySecond || isUpdateEveryFiveSecond || isUpdateEveryMinute) {
                    List<Char> mChars = getChars();
                    for (Char _char : mChars) {
                        try {
                            if (isUpdateEveryHalfSecond) {
                                _char.updateEveryHalfSecond();
                            }
                            if (isUpdateEverySecond) {
                                _char.updateEverySecond();
                            }
                            if (isUpdateEveryFiveSecond) {
                                _char.updateEveryFiveSecond();
                            }
                            if (isUpdateEveryMinute) {
                                _char.updateEveryMinute();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMobForRespawnList(Mob mob) {
        synchronized (listRecoveries) {
            listRecoveries.add(mob);
        }
    }

    public void recoveryAllMonsters() {
        monsters.clear();
        int id = 0;
        for (Mob mob : map.mapTemplate.mobList) {
            Mob monster = mob.clone();
            monster.idEntity = id++;
            monster.zone = this;
            addMob(monster);
        }
    }

    public void reloadMob() {
        for (Mob mob : monsters) {
            getService().addMob(mob);
        }
    }

    public void recoveryMonster() {
        synchronized (this.listRecoveries) {
            for (Mob mob : this.listRecoveries) {
                getService().recoveryMonster(mob);
                this.waitingListRecoveries.remove(mob);

            }
            this.listRecoveries.clear();
        }
    }

    public void updateRecovery() {
        synchronized (waitingListRecoveries) {
            if (!this.waitingListRecoveries.isEmpty()) {
                for (Mob mob : this.waitingListRecoveries) {
                    mob.recoveryTimeCount--;
                    if (mob.recoveryTimeCount <= 0) {
                        mob.recovery();
                        addMobForRespawnList(mob);
                    }
                }
                if (!listRecoveries.isEmpty()) {
                    recoveryMonster();
                }
            }
        }
    }

    public void updateMob() {
        try {
            if (!monsters.isEmpty()) {
                for (Mob mob : monsters) {
                    try {
                        if (mob.timeRemove != -1 && mob.timeRemove < System.currentTimeMillis()) {
                            waitingListDelete.add(mob);
                        }
                        mob.update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (!waitingListDelete.isEmpty()) {
                        for (Mob mob : waitingListDelete) {
                            removeMonster(mob);
                        }
                        waitingListDelete.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!isClosed) {
            updateItemMap();
            updateRecovery();
            updateMob();
        }
    }

    public void close() {
        if (!isClosed) {
            this.isClosed = true;
            this.map = null;
            this.players = null;
            this.itemMaps = null;
            this.monsters = null;
            this.listRecoveries = null;
            this.listRemoveItem = null;
            this.waitingListRecoveries = null;
        }
    }

    public void addMobForWaitingListRespawn(Mob mob) {
        synchronized (waitingListRecoveries) {
            if (!mob.isCantRespawn) {
                waitingListRecoveries.add(mob);
            }
        }
    }

    public Npc getNpc(int id) {
        for (Npc npc : npcs) {
            if (npc.idEntity == id) {
                return npc;
            }
        }
        return null;
    }

    public List<ItemMap> getItemMaps() {
        List<ItemMap> list = new ArrayList<>();

        lockItem.readLock().lock();
        try {
            for (ItemMap itemMap : itemMaps) {
                list.add(itemMap);
            }
        } finally {
            lockItem.readLock().unlock();
        }
        return list;
    }

    public void writeItemMap(Message m) throws IOException {
        List<ItemMap> itemMaps1 = getItemMaps();
        m.writer().writeShort(itemMaps1.size());
        for (ItemMap itemMap : itemMaps1) {
            itemMap.write(m);
        }

    }

    public Boss createBoss(short id, int hp, short level, short x, short y, int kimax) {
        int uniqueId = monsters.size();
        if (id == 291) {
            return new TrungUyThep(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 292) {
            return new NinjaAoTim(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 293) {
            return new Buyon(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 294 || id == 295) {
            return new Gamma(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 297) {
            return new Wall(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 77) {
            return new Cell(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 296) {
            return new CellMax(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 134) {
            return new ThoDaiCa(uniqueId, id, hp, level, x, y, kimax);
        } else if (id == 245) {
            return new Dracubin(uniqueId, id, hp, level, x, y, kimax);
        }else if(id == 69){
            return new Phuc(uniqueId, id, hp, level, x, y, kimax);
        }
        return new Boss(uniqueId, id, hp, level, x, y, kimax);
    }

    public void mobDead(Mob mob, Char killer) {

    }

    public List<Mob> getMobInZone(byte maxTarget) {
        List<Mob> mobs = new ArrayList<>();
        for (Mob mob : monsters) {
            if (mob.isDead) {
                continue;
            }
            mobs.add(mob);
            if (mobs.size() >= maxTarget) {
                break;
            }
        }
        return mobs;
    }
}

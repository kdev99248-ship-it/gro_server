package com.vdtt.clan;


import com.vdtt.item.Item;
import com.vdtt.map.world.World;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import com.vdtt.skill.SkillClan;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Clan {
    public static final int TYPE_NORMAl = 0;
    public static final int TYPE_TOCPHO = 4;
    public static final int TYPE_TOCTRUONG = 5;
    public static final int TYPE_TRUONGLAO = 3;
    public static final int TYPE_UUTU = 2;
    public static final int UP_LEVEL = 5;
    public static final int MOVE_INPUT_MONEY = 2;
    public static final int MOVE_OUT_MEM = 1;
    public static final int CREATE_CLAN = 0;
    private static final ClanDAO clanDAO = new ClanDAO();
    public static Map<String, Clan> mapClan = new HashMap<>();
    public List<World> worlds ;// danh sách world đang tham gia


    public void addWorld(World world) {
        synchronized (worlds) {
            if (worlds.stream().noneMatch(w -> w.getType() == world.getType())) {
                worlds.add(world);
                Log.debug("add worldType: " + world.getType());
            }
        }
    }

    public void addMemberForWorld(Zone pre, Zone now,Char _char) {
        synchronized (worlds) {
            worlds.forEach((t) -> {
                if (t != null && !t.isClosed() && t.enterWorld(pre, now)) {
                    t.addMember(_char);
                }
            });
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
    public static boolean running;
    public String title;

    public static ClanDAO getClanDAO() {
        return clanDAO;
    }

    public static void start() {
        running = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    long l1 = System.currentTimeMillis();
                    updateClan();
                    long l2 = System.currentTimeMillis();
                    if (l2 - l1 < 1000) {
                        try {
                            Thread.sleep(1000 - (l2 - l1));
                        } catch (InterruptedException ex) {
                            Log.error("clan update err");
                        }
                    }
                }
            }
        }
        );
        thread.start();

    }

    public static void updateClan() {
        List<Clan> clans = clanDAO.getAll();
        mapClan.clear();
        mapClan = clans.stream().collect(Collectors.toMap(Clan::getName, Function.identity()));
        synchronized (clans) {
            for (Clan clan : clans) {
                try {
//                    clan.update();
                } catch (Exception e) {
                    Log.error("update err 1");
                }
            }
        }
    }

    public int id;
    public String name;
    public String main_name;
    public byte openDun;
    public byte level;
    public int exp;
    public int congHien;
    public int coin;
    public byte countKick = 5;
    public int countNvHd;//so luong nv hien tai
    public String alert;
    public Date reg_date;
    public String log;
    public Item[] items;
    public byte countInvite;
    public Vector<SkillClan>skillClans;
    public MemberDAO memberDAO;
    public byte hour;
    public byte min;
    @Setter
    @Getter
    private boolean saving;

    @Getter
    private ClanService clanService;
    public Clan() {
        this.items = new Item[45];
        this.log = "";
        this.clanService = new ClanService(this);
        this.memberDAO = new MemberDAO(this);
        this.skillClans = new Vector<>();
        this.worlds = new ArrayList<>();
        countNvHd = 1;
    }
    public Item[] getItems() {
        Vector<Item> items = new Vector<>();
        for (Item item : this.items) {
            if (item != null) {
                items.add(item);
            }
        }
        return items.toArray(new Item[items.size()]);
    }
    public int getIndexItem(Item item) {
        int index = -1;
        Item[] items = getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].id == item.id) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int getIndexItem(int itemID) {
        int index = -1;
        Item[] items = getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].id == itemID) {
                index = i;
                break;
            }
        }
        return index;
    }

    private boolean isExist(Item item) {
        boolean isExist = false;
        Item[] items = getItems();
        for (Item i : items) {
            if (i.id == item.id) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    public void addItem(Item item) {
        try {
            int index = getIndexItem(item);
            if (index > -1) {
                this.items[index].add(item.getQuantity());
            } else {
                for (int i = 0; i < this.items.length; i++) {
                    if (this.items[i] == null) {
                        this.items[i] = item;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void removeItem(Item item, int quantity) {
        for (int i = 0; i < this.items.length; i++) {
            if (this.items[i] == item) {
                item.reduce(quantity);
                if (!item.has()) {
                    this.items[i] = null;
                    break;
                }
            }
        }
    }

    public void removeItem(int index, int quantity) {
        if (items[index] != null) {
            items[index].reduce(quantity);
            if (!items[index].has()) {
                items[index] = null;
            }

        }
    }
    public void addExp(int exp) {
        this.exp += exp;
        if(this.exp >= getExpNext()){
            this.exp=this.exp-getExpNext();
            this.level++;
        }
        Clan.getClanDAO().update(this);
    }

    public void loadItem(JSONArray jArr) {
    }

    public String getLog() {
        return log;
    }

    public void writeLog(String str) {
        this.log += str +" vào lúc "+ Util.dateToString(Date.from(Instant.now()), "hh:mm:ss dd/MM/yyyy") + "\n";
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public int getCongHien() {
        return congHien;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainName() {
        return main_name;
    }

    public void setMainName(String main_name) {
        this.main_name = main_name;
    }


    public byte getOpenDun() {
        return openDun;
    }

    public void setOpenDun(byte openDun) {
        this.openDun = openDun;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getExpNext() {
        int expNext = 2000;
        for (int i = 1; i < level; i++) {
            if (i == 1) {
                expNext = 3720;
            } else {
                if (i < 10) {
                    expNext = ((expNext / i) + 310) * (i + 1);
                } else if (i < 20) {
                    expNext = ((expNext / i) + 620) * (i + 1);
                } else {
                    expNext = ((expNext / i) + 930) * (i + 1);
                }
            }
        }
        return expNext;
    }

    public void addCoin(int coin) {
        this.coin += coin;
        //Clan.getClanDAO().update(this);
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
    public String getAlert() {
        return "Thông báo: " + alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public void setCountClan(int i) {this.countInvite= (byte) i;}

    public long getRegDate() {
        return reg_date.getTime();
    }

    public void setRegDate(Date reg_date) {
        this.reg_date = reg_date;
    }

    public Member getMemberByName(String name) {
        List<Member> members = memberDAO.getAll();
        synchronized (members) {
            for (Member mem : members) {
                if (mem.getName().equals(name)) {
                    return mem;
                }
            }
        }
        return null;
    }

    public int getNumberMember() {
        return memberDAO.getAll().size();
    }

    public int getMemberMax() {
        return this.level +20;
    }

    public int getNumberSameType(int type) {
        int number = 0;
        List<Member> members = memberDAO.getAll();
        synchronized (members) {
            for (Member mem : members) {
                if (mem != null && mem.getType() == type) {
                    number++;
                }
            }
        }
        return number;
    }

    public List<Char> getOnlineMembers() {
        List<Char> chars = new ArrayList<>();
        List<Member> members = memberDAO.getAll();
        synchronized (members) {
            for (Member mem : members) {
                if (mem != null && mem.isOnline() && mem.getChar() != null) {
                    chars.add(mem.getChar());
                }
            }
        }
        return chars;
    }

}

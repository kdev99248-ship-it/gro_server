package com.vdtt.events;

import com.vdtt.data.CMDMenu;
import com.vdtt.data.Caption;
import com.vdtt.db.DbManager;
import com.vdtt.events.points.EventPoint;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.events.points.RewardPoint;
import com.vdtt.leaderboard.LeaderboardRace;
import com.vdtt.leaderboard.LeaderboardRaceManager;
import com.vdtt.mail.Mail;
import com.vdtt.mail.MailManager;
import com.vdtt.model.Char;
import com.vdtt.model.Menu;
import com.vdtt.network.Message;
import com.vdtt.reward.Reward;
import com.vdtt.server.ServerManager;
import com.vdtt.util.Log;
import com.vdtt.util.RandomCollection;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class AbsEvent {

    public final static String[] TOP_EVENT = new String[]{"Nhi đồng", "Luyện tập", "Nạp Zeni T-3", "Nạp nhiều"};

    @Setter
    protected int id;
    @Setter
    protected String name;
    @Setter
    protected EventType type;
    protected List<KeyPoint> keys;
    protected final Map<Integer, EventPoint> eventPoints;
    protected Map<KeyPoint, List<EventPoint>> topEvent = new HashMap<>();
    protected final RandomCollection<Integer> dropList;
    @Setter
    protected int dropRate;

    @Setter
    private boolean rewardToSendWhenRaceEnds;// khi kết thúc sự kiện sẽ tự động gửi quà vào mail


    public AbsEvent() {
        this.keys = new ArrayList<>();
        this.eventPoints = new ConcurrentHashMap<>();
        this.dropList = new RandomCollection<>();
    }

    private void prepare() {
        setKeys();
        addKey(KeyPoint.NAP_ZENI);
        loadPoints();
        createRewardForUseItemEvent();
        createDropList();
        createRace();
        onPrepare();
    }

    private void loadPoints() {
        DbManager.getInstance().loadEventPoint(this);
    }

    protected void addKey(KeyPoint key) {
        this.keys.add(key);
    }

    protected void addDrop(double weight, int itemId) {
        this.dropList.add(weight, itemId);
    }

    public void createMenu(Char player, int npcId, List<Menu> menuList) {
        menuList.add(new Menu(CMDMenu.EXECUTE, name, () -> {
            menuList.clear();
            onCreateMenu(player, npcId, menuList);
            player.getService().openUIMenu(npcId);
        }));
    }


    public EventPoint createEventPoint() {
        EventPoint eventPoint = new EventPoint();
        eventPoint.setEventID(this.id);
        return eventPoint;
    }


    public void addEventPoint(EventPoint eventPoint) {
        synchronized (eventPoints) {
            eventPoints.putIfAbsent(eventPoint.getPlayerID(), eventPoint);
        }
    }

    public void removeEventPoint(EventPoint eventPoint) {
        synchronized (eventPoints) {
            eventPoints.remove(eventPoint.getPlayerID());
        }
    }


    public EventPoint findEventPointByPlayerID(int playerID) {
        synchronized (eventPoints) {
            return eventPoints.getOrDefault(playerID, null);
        }
    }

    private List<EventPoint> getSortedEventPointPoints(KeyPoint key) {
        List<EventPoint> sorted = new ArrayList<>();
        synchronized (eventPoints) {
            eventPoints.values().forEach(ev -> {
                RewardPoint p = ev.find(key);
                if (p != null) {
                    sorted.add(ev);
                }
            });
        }
        return sorted.stream().filter(eventPoint -> eventPoint.getPoint(key) > 0).sorted((o1, o2) -> {
            RewardPoint p1 = o1.find(key);
            RewardPoint p2 = o2.find(key);
            return p2.getPoint() - p1.getPoint();
        }).toList();
    }

    private List<EventPoint> getSortedEventPointPoints(KeyPoint key, int limit) {
        List<EventPoint> sorted = new ArrayList<>();
        synchronized (eventPoints) {
            eventPoints.values().forEach(ev -> {
                RewardPoint p = ev.find(key);
                if (p != null) {
                    sorted.add(ev);
                }
            });
        }
        return getSortedEventPointPoints(key).stream().limit(limit).toList();
    }


    private void createRace() {
        List<KeyPoint> keys = getKeys();
        List<KeyPoint> races = keys.stream().filter(KeyPoint::isRace).toList();
        String[] raceNames = new String[races.size() + TOP_EVENT.length];
        System.arraycopy(TOP_EVENT, 0, raceNames, 0, TOP_EVENT.length);
        for (int i = 0; i < races.size(); i++) {
            raceNames[i + TOP_EVENT.length] = races.get(i).getName();
        }
        Caption.TOP_EVENT = raceNames;
        doCreateRace();
    }

    private void destroyRace() {
        Caption.TOP_EVENT = TOP_EVENT;
        LeaderboardRaceManager manager = LeaderboardRaceManager.getInstance();
        List<LeaderboardRace> leaderboardRaces = manager.getLeaderboardRaces();
        leaderboardRaces.removeIf(leaderboardRace -> leaderboardRace.getId() >= 1000);
    }

    protected void start() {
        prepare();
        onStart();
        List<Char> players = ServerManager.getChars();
        players.forEach(p -> DbManager.getInstance().loadEventPoint(p));
        EventHandler.setEvent(this);
    }

    protected void end() {
        if (rewardToSendWhenRaceEnds) {
            sendTopRaceReward();
        }
        onEnd();
        List<Char> players = ServerManager.getChars();
        players.forEach(p -> {
            DbManager.getInstance().storeEventPoint(p.getEventPoint());
            p.setEventPoint(null);
        });
        destroyRace();
        EventHandler.setEvent(null);
    }

    private void sendTopRaceReward() {
        Log.info("Gui qua dua top su kien");
        for (KeyPoint key : keys) {
            if (key.isRace()) {
                sendTopRaceReward(key);
            }
        }
    }

    private void sendTopRaceReward(KeyPoint key) {
        List<EventPoint> leaders = getSortedEventPointPoints(key);
        EventRace eventRace = LeaderboardRaceManager.getInstance().getEventRaceByKey(key);
        if (eventRace != null) {
            List<Reward> rewards = eventRace.getRewardList();
            for (Reward reward : rewards) {
                if (reward instanceof EventReward eventReward) {
                    int minTop = eventReward.getMinTop();
                    int maxTop = eventReward.getMaxTop();
                    for (int i = minTop - 1; i < maxTop; i++) {
                        if (i < leaders.size()) {
                            EventPoint leader = leaders.get(i);
                            sendTopRaceRewardToMail(leader.getPlayerID(), eventReward);
                        }
                    }
                }
            }
        }
    }

    private void sendTopRaceRewardToMail(int charId, EventReward reward) {
        Char player = ServerManager.findCharById(charId);
        if (player != null) {
            sendTopRaceRewardToMail(player.mailManager, reward);
        } else {
            MailManager mailManager = DbManager.getInstance().getMailManager(charId);
            if (mailManager != null) {
                sendTopRaceRewardToMail(mailManager, reward);
                DbManager.getInstance().saveMailManager(charId, mailManager);
            }
        }
    }

    private void sendTopRaceRewardToMail(MailManager mailManager, EventReward reward) {
        Mail mail = new Mail();
        if (mailManager.size() == 0) {
            mail.id = mailManager.size();
        } else {
            mail.id = mailManager.getID();
        }
        mail.title = reward.getRealName();
        mail.content = "Quà sự kiện";
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

    private long timeLoadTop;

    public void loadTopEvent() {
//        if (timeLoadTop > System.currentTimeMillis()) {
//            return;
//        }
//        timeLoadTop = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);
        AbsEvent event = EventHandler.getEvent();
        List<KeyPoint> keys = event.getKeys().stream().filter(KeyPoint::isRace).toList();
        for (KeyPoint key : keys) {
            topEvent.put(key, getSortedEventPointPoints(key, 10));
        }
    }

    public void viewRace(Char player, KeyPoint key) {
        loadTopEvent();
        List<EventPoint> eventPoints = topEvent.get(key);
        try {
            Message m = new Message((byte) -22);
            m.writer().writeBoolean(true);
            m.writer().writeByte(eventPoints.size());
            for (int i = 0; i < eventPoints.size(); i++) {
                EventPoint eventPoint = eventPoints.get(i);
                m.writer().writeByte(i);
                m.writeUTF(eventPoint.getPlayerName());
                m.writer().writeLong(0);
                m.writer().writeLong(eventPoint.getPoint(key));
                m.writer().writeByte(eventPoint.getClassId() + 1);
                m.writeUTF(Objects.toString(eventPoint.getClanName(), "Chưa vào vũ trụ"));
            }
            player.getService().sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int randomItemDrop() {
        return dropList.next();
    }

    protected abstract void onPrepare();

    protected abstract void createRewardForUseItemEvent();

    protected abstract void doCreateRace();

    protected abstract void onStart();

    protected abstract void onEnd();

    protected abstract void setKeys();

    protected abstract void onCreateMenu(Char player, int npcId, List<Menu> menuList);

    protected abstract void createDropList();

}

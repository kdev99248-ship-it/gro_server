package com.vdtt.map.world.martialarts;

import com.vdtt.model.Char;
import com.vdtt.server.ServerManager;
import com.vdtt.util.Log;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MartialManager {
    @Getter
    @Setter
    public static boolean start;
    private static final long DAILY_DELAY = 24 * 60 * 60 * 1000;
    private static final long MAX_TIME = 60 * 60 * 1000;
    private long remainingTime;
    private final int scheduledHour;
    private final int scheduledMinute;
    private final int scheduledSecond;
    protected MartialArtsConference martialArtsConference;
    public List<String> memberRegistered = new ArrayList<>();

    public MartialManager() {
        this.scheduledHour = 21;
        this.scheduledMinute = 10;
        this.scheduledSecond = 0;
    }

    private void execute() {
        startBattleFiled();
        try {
            Thread.sleep(remainingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        endBattleFiled();
    }

    public void startBattleFiled() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.FRIDAY) {
            Log.info("Start Martial");
            start = true;
            martialArtsConference = new MartialArtsConference();
            for (String name : MartialManager.getInstance().memberRegistered) {
                Char player = ServerManager.findCharByName(name);
                if (player != null) {
                    MartialArtsConference.joinMap(player);
                }
            }
        }
    }

    public void endBattleFiled() {
        start = false;
        memberRegistered.clear();
        martialArtsConference.close();
        Log.info("End Martial");
    }

    public void start() {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        int hour = zonedNow.getHour();
        int minute = zonedNow.getMinute();
        int second = zonedNow.getSecond();
        int toMillis1 = ((hour * 60 * 60) + (minute * 60) + second) * 1000;
        int toMillis2 = ((8 * 60 * 60) + (scheduledMinute * 60) + scheduledSecond) * 1000;
        ZonedDateTime zonedNext5;
//        if (toMillis1 > toMillis2 && toMillis1 < toMillis2 + MAX_TIME) {
//            remainingTime = toMillis2 + MAX_TIME - toMillis1;
//            zonedNext5 = zonedNow.withHour(hour).withMinute(minute).withSecond(second);
//        } else {
            remainingTime = MAX_TIME;
            zonedNext5 = zonedNow.withHour(scheduledHour).withMinute(scheduledMinute).withSecond(scheduledSecond);
//        }
        if (zonedNow.compareTo(zonedNext5) > 0) {
            zonedNext5 = zonedNext5.plusDays(1);
        }

        Duration duration = Duration.between(zonedNow, zonedNext5);
        long initialDelay = duration.toMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::execute, initialDelay, DAILY_DELAY, TimeUnit.MILLISECONDS);
    }

    public static MartialManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        public static final MartialManager INSTANCE = new MartialManager();
    }
}

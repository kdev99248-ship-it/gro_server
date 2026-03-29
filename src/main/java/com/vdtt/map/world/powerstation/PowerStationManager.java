package com.vdtt.map.world.powerstation;

import com.vdtt.map.world.martialarts.MartialArtsConference;
import com.vdtt.model.Char;
import com.vdtt.party.Group;
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
public class PowerStationManager {
    public List<Group> groupList = new ArrayList<>();

    @Getter
    @Setter
    public static boolean start;
    private static final long DAILY_DELAY = 24 * 60 * 60 * 1000;
    private static final long MAX_TIME = 60 * 60 * 1000;
    private long remainingTime;
    private final int scheduledHour;
    private final int scheduledMinute;
    private final int scheduledSecond;
    protected PowerStation powerStation;

    public PowerStationManager() {
        this.scheduledHour = 21;
        this.scheduledMinute = 10;
        this.scheduledSecond = 0;
    }

    private void execute() {
        startPowerStation();
        try {
            Thread.sleep(remainingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        endPowerStation();
    }

    public void startPowerStation() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY || dayOfWeek == Calendar.SATURDAY) {
            Log.info("Start PowerStation");
            start = true;
            powerStation = new PowerStation();
            for (Group group : groupList) {
                for (Char player : group.getChars()) {
                    PowerStation.joinMap(player);
                }
            }
        }
    }

    public void endPowerStation() {
        start = false;
        groupList.clear();
        powerStation.close();
        Log.info("End PowerStation");
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
    public static PowerStationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        public static final PowerStationManager INSTANCE = new PowerStationManager();
    }
}


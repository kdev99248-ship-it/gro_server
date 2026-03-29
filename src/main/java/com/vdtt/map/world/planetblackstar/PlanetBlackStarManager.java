package com.vdtt.map.world.planetblackstar;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import com.vdtt.util.Log;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PlanetBlackStarManager {
    @Getter
    @Setter
    public static boolean start;
    private static final long DAILY_DELAY = 24 * 60 * 60 * 1000;
    private static final long MAX_TIME = 24 * 60 * 1000;
    private long remainingTime;
    private final int scheduledHour;
    private final int scheduledMinute;
    private final int scheduledSecond;

    public PlanetBlackStarManager() {
        this.scheduledHour = 19;
        this.scheduledMinute = 35;
        this.scheduledSecond = 0;
    }

    private void execute() {
        Log.info("Start PlanetBlackStarManager");
        running();
        try {
            Thread.sleep(remainingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.info("End PlanetBlackStarManager");
        close();
    }

    public void running() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY || dayOfWeek == Calendar.SATURDAY) {
            start = true;
            PlanetBlackStar.getInstance().createArea();
        }
    }

    public void close() {
        start = false;
        Map map = MapManager.getInstance().find(39);
        for(Zone zone : map.getZones()) {
            for (Char player : zone.getChars()) {
                player.wakeUpFromDead();
                MapManager.getInstance().joinZone(player, 86, 0, 1);
                player.getService().serverDialog("Ngọc rồng đen kết thúc");
            }
        }
        map.getZones().clear();
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
        if (toMillis1 > toMillis2 && toMillis1 < toMillis2 + MAX_TIME) {
            remainingTime = toMillis2 + MAX_TIME - toMillis1;
            zonedNext5 = zonedNow.withHour(hour).withMinute(minute).withSecond(second);
        } else {
            remainingTime = MAX_TIME;
            zonedNext5 = zonedNow.withHour(scheduledHour).withMinute(scheduledMinute).withSecond(scheduledSecond);
        }
        if (zonedNow.compareTo(zonedNext5) > 0) {
            zonedNext5 = zonedNext5.plusDays(1);
        }

        Duration duration = Duration.between(zonedNow, zonedNext5);
        long initialDelay = duration.toMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::execute, initialDelay, DAILY_DELAY, TimeUnit.MILLISECONDS);
    }

    public static PlanetBlackStarManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        public static final PlanetBlackStarManager INSTANCE = new PlanetBlackStarManager();
    }
}

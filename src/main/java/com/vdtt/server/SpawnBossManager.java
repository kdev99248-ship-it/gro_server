package com.vdtt.server;

import com.vdtt.map.MapManager;
import com.vdtt.mob.Boss;
import com.vdtt.util.Util;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpawnBossManager {
    public static final byte ALL = 0;
    public static final byte RANDOM = 1;
    public static final String FIDE = "fide";
    public static final String TUONG_LAI = "tuonglai";
    public static final String VAMPA = "vampa";
    public static final String NAMEC = "namec";
    public static final String YARDAT = "yardat";
    public static final String ZAMASU = "zamasu";
    public static final String MORO = "Moro";
      public static final String fu = "fu";

    @Getter
    private static final SpawnBossManager instance = new SpawnBossManager();

    private final HashMap<String, List<SpawnBoss>> spawnBosses = new HashMap<>();
    @Getter
    private final List<Boss> bosses = new ArrayList<>();

    public void init() {
        final List<SpawnBoss> mapFIDE = new ArrayList<>();
        final List<SpawnBoss> mapTUONGLAI = new ArrayList<>();
        final List<SpawnBoss> mapVAMPA = new ArrayList<>();
        final List<SpawnBoss> mapYARDAT = new ArrayList<>();
        final List<SpawnBoss> mapNAMEC = new ArrayList<>();
        final List<SpawnBoss> zamasu = new ArrayList<>();
        final List<SpawnBoss> moro = new ArrayList<>();
         final List<SpawnBoss> fu = new ArrayList<>();
        

        SpawnBoss spawnBoss24 = create(1, 57, (short) 910, (short) 362, 2000000000, 500000);
        spawnBoss24.add(1, 273);
        zamasu.add(spawnBoss24);
        SpawnBoss spawnBoss25 = create(2, 62, (short) 1755, (short) 418, 2000000000, 500000);
        spawnBoss25.add(1, 273);
        zamasu.add(spawnBoss25);
        SpawnBoss spawnBoss26 = create(3, 66, (short) 353, (short) 486, 2000000000, 500000);
        spawnBoss26.add(1, 273);
        zamasu.add(spawnBoss26);
        SpawnBoss spawnBoss27 = create(4, 61, (short) 2377, (short) 418, 2000000000, 50000);
        spawnBoss27.add(1, 273);
        zamasu.add(spawnBoss27);
        SpawnBoss spawnBoss28 = create(5, 55, (short) 1569, (short) 429, 2000000000, 50000);
        spawnBoss28.add(1, 273);
        zamasu.add(spawnBoss28);
        SpawnBoss spawnBoss29 = create(6, 64, (short) 1820, (short) 433, 2000000000, 50000);
        spawnBoss29.add(1, 273);
        zamasu.add(spawnBoss29);
        SpawnBoss spawnBoss30 = create(7, 65, (short) 874, (short) 489, 2000000000, 50000);
        spawnBoss30.add(1, 273);
        zamasu.add(spawnBoss30);

        SpawnBoss spawnBoss31 = create(8, 63, (short) 2294, (short) 440, 2000000000, 50000);
        spawnBoss31.add(1, 273);
        zamasu.add(spawnBoss31);

        SpawnBoss spawnBoss4 = create(1, 57, (short) 910, (short) 362, 10000000, 1000);
        spawnBoss4.add(1, 199);
        mapFIDE.add(spawnBoss4);
        SpawnBoss spawnBoss5 = create(2, 62, (short) 1755, (short) 418, 10000000, 1000);
        spawnBoss5.add(1, 199);
        mapFIDE.add(spawnBoss5);
        SpawnBoss spawnBoss6 = create(3, 66, (short) 353, (short) 486, 10000000, 1000);
        spawnBoss6.add(1, 199);
        mapFIDE.add(spawnBoss6);
        SpawnBoss spawnBoss7 = create(4, 61, (short) 2377, (short) 418, 10000000, 1000);
        spawnBoss7.add(1, 199);
        mapFIDE.add(spawnBoss7);
        SpawnBoss spawnBoss8 = create(5, 55, (short) 1569, (short) 429, 55000000, 5000);
        spawnBoss8.add(1, 200);
        mapTUONGLAI.add(spawnBoss8);
        SpawnBoss spawnBoss9 = create(6, 64, (short) 1820, (short) 433, 55000000, 5000);
        spawnBoss9.add(1, 200);
        mapTUONGLAI.add(spawnBoss9);
        SpawnBoss spawnBoss10 = create(7, 65, (short) 874, (short) 489, 55000000, 5000);
        spawnBoss10.add(1, 200);
        mapTUONGLAI.add(spawnBoss10);
        SpawnBoss spawnBoss11 = create(8, 63, (short) 2294, (short) 440, 55000000, 5000);
        spawnBoss11.add(1, 200);
        mapTUONGLAI.add(spawnBoss11);
        SpawnBoss spawnBoss12 = create(9, 88, (short) 303, (short) 416, 150000000, 15000);
        spawnBoss12.add(1, 201);
        mapVAMPA.add(spawnBoss12);
        SpawnBoss spawnBoss13 = create(10, 74, (short) 1476, (short) 450, 150000000, 15000);
        spawnBoss13.add(1, 201);
        mapVAMPA.add(spawnBoss13);
        SpawnBoss spawnBoss14 = create(11, 76, (short) 2278, (short) 459, 150000000, 15000);
        spawnBoss14.add(1, 201);
        mapVAMPA.add(spawnBoss14);
        SpawnBoss spawnBoss15 = create(12, 73, (short) 2355, (short) 404, 150000000, 15000);
        spawnBoss15.add(1, 201);
        mapVAMPA.add(spawnBoss15);
        SpawnBoss spawnBoss16 = create(13, 77, (short) 260, (short) 416, 250000000, 25000);
        spawnBoss16.add(1, 202);
        mapNAMEC.add(spawnBoss16);
        SpawnBoss spawnBoss17 = create(14, 71, (short) 269, (short) 409, 250000000, 25000);
        spawnBoss17.add(1, 202);
        mapNAMEC.add(spawnBoss17);
        SpawnBoss spawnBoss18 = create(15, 72, (short) 1772, (short) 489, 250000000, 25000);
        spawnBoss18.add(1, 202);
        mapNAMEC.add(spawnBoss18);
        SpawnBoss spawnBoss19 = create(16, 70, (short) 2268, (short) 480, 250000000, 25000);
        spawnBoss19.add(1, 202);
        mapNAMEC.add(spawnBoss19);
        SpawnBoss spawnBoss20 = create(17, 81, (short) 799, (short) 415, 350000000, 35000);
        spawnBoss20.add(1, 203);
        mapYARDAT.add(spawnBoss20);
        SpawnBoss spawnBoss21 = create(18, 80, (short) 1599, (short) 420, 350000000, 35000);
        spawnBoss21.add(1, 203);
        mapYARDAT.add(spawnBoss21);
        SpawnBoss spawnBoss22 = create(19, 79, (short) 419, (short) 424, 350000000, 35000);
        spawnBoss22.add(1, 203);
        mapYARDAT.add(spawnBoss22);
        SpawnBoss spawnBoss23 = create(20, 78, (short) 1654, (short) 398, 350000000, 35000);
        spawnBoss23.add(1, 203);
        mapYARDAT.add(spawnBoss23);

         SpawnBoss spawnBoss244 = create(25, 70, (short) 1654, (short) 398, 2000000000, 70000);
        spawnBoss244.add(1, 296);
         mapNAMEC.add(spawnBoss244); 
        

        spawnBosses.put(FIDE, mapFIDE);
        spawnBosses.put(TUONG_LAI, mapTUONGLAI);
        spawnBosses.put(VAMPA, mapVAMPA);
        spawnBosses.put(NAMEC, mapNAMEC);
        spawnBosses.put(YARDAT, mapYARDAT);
        spawnBosses.put(ZAMASU, zamasu);
        spawnBosses.put(MORO, moro);
         spawnBosses.put(MORO, fu);
        
    }

    public SpawnBoss create(int id, int mapID, short x, short y, int hp, int kiMax) {
        return new SpawnBoss(id, MapManager.getInstance().find(mapID), x, y, hp, kiMax);
    }

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public List<SpawnBoss> getListSpawnBoss(String key) {
        return spawnBosses.get(key);
    }

    public void spawnRandom(String key) {
        List<SpawnBoss> list = getListSpawnBoss(key);
        int rand = Util.nextInt(list.size());
//        int rand = 0;
        SpawnBoss sp = list.get(rand);
        sp.spawn();
    }

    private void spawnAll(String key) {
        List<SpawnBoss> list = getListSpawnBoss(key);
        for (SpawnBoss spawn : list) {
            spawn.spawn();
        }
    }

    public void spawn(int hours, int minutes, int seconds, String key, byte type) {
        Util.schedule(() -> {
            if (type == ALL) {
                spawnAll(key);
            }
            if (type == RANDOM) {
                spawnRandom(key);
            }
        }, hours, minutes, seconds);
    }

    public void spawnRepeat(String key, int hourlyDelay, byte type) {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        int hours = zonedNow.getHour() + 1;
        if (hours % hourlyDelay != 0) {
            hours = ((hours / hourlyDelay + 1) * hourlyDelay);
        }
        if (hours >= 24) {
            hours = 0;
        }
        ZonedDateTime zonedNext5 = zonedNow.withHour(hours).withMinute(0).withSecond(0);
        if (zonedNow.compareTo(zonedNext5) > 0) {
            zonedNext5 = zonedNext5.plusDays(1);
        }

        Duration duration = Duration.between(zonedNow, zonedNext5);
        long initialDelay = duration.getSeconds();
        Runnable runnable = () -> {
            if (type == ALL) {
                spawnAll(key);
            }
            if (type == RANDOM) {
                spawnRandom(key);
            }
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(runnable, initialDelay, hourlyDelay * 60 * 60, TimeUnit.SECONDS);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }
}

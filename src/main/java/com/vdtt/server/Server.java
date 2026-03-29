package com.vdtt.server;

import com.vdtt.achievement.AchievementList;
import com.vdtt.clan.Clan;
import com.vdtt.cronjob.CronJobSetup;
import com.vdtt.data.Caption;
import com.vdtt.data.DataCenter;
import com.vdtt.db.DbManager;
import com.vdtt.effect.EffectTemplateManager;
import com.vdtt.events.EventHandler;
import com.vdtt.handler.UseItemHandler;
import com.vdtt.item.ItemManager;
import com.vdtt.leaderboard.LeaderboardRaceManager;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.WorldManager;
import com.vdtt.map.world.martialarts.MartialManager;
import com.vdtt.map.world.planetblackstar.PlanetBlackStarManager;
import com.vdtt.map.world.powerstation.PowerStationManager;
import com.vdtt.model.Char;
import com.vdtt.model.RandomItem;
import com.vdtt.model.User;
import com.vdtt.network.Session;
import com.vdtt.reward.RewardManager;
import com.vdtt.stall.StallManager;
import com.vdtt.store.StoreManager;
import com.vdtt.util.Log;
import com.vdtt.util.Util;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

public class Server {
    public static ServerSocket server;
    public static boolean start;
    public static int id;
    private static boolean isStop;

    public static void start() {
//        JFrame frame = new JFrame("Hiii");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        Panel panel = new Panel();
//        frame.getContentPane().add(panel);
//        frame.setSize(200, 100);
//        frame.setVisible(true);
        setOffline();
        WorldManager.getInstance().start();
        try {
            InetSocketAddress bindSocket = new InetSocketAddress(Config.getInstance().getHost(), Config.getInstance().getPort());
            server = new ServerSocket();
            server.bind(bindSocket);
            start = true;
            id = 0;
            Thread runtime = new Thread(RuntimeServer.gI());
            runtime.setName("RuntimeServer");
            runtime.start();
            Thread threadStall = new Thread(StallManager.getInstance());
            threadStall.setName("Gian hàng");
            threadStall.start();
            SpawnBossManager.getInstance().init();
            SpawnBossManager.getInstance().spawnRepeat(SpawnBossManager.FIDE, 1, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawnRepeat(SpawnBossManager.TUONG_LAI, 1, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawnRepeat(SpawnBossManager.VAMPA, 1, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawnRepeat(SpawnBossManager.NAMEC, 1, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawnRepeat(SpawnBossManager.YARDAT, 1, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawnRepeat(SpawnBossManager.MORO, 1, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawnRepeat(SpawnBossManager.fu, 1, SpawnBossManager.ALL);
            SpawnBossManager.getInstance().spawn(12, 0, 0, SpawnBossManager.ZAMASU, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(18, 0, 0, SpawnBossManager.ZAMASU, SpawnBossManager.RANDOM);

            MartialManager.getInstance().start();
            PowerStationManager.getInstance().start();
            PlanetBlackStarManager.getInstance().start();

            TaskResetManager.getInstance().resetNewDay();
            UseItemHandler.init();
            EventHandler.init();
            LeaderboardRaceManager.getInstance();
            Log.info("Server started port [" + server.getLocalPort() + "].");
            Log.info("Thread Start [" + Thread.activeCount() + "].");
            acceptClient();
        } catch (IOException e) {

        }
    }

    public static void acceptClient() throws IOException {
        while (start) {
            Socket client = server.accept();
            if (GameServer.isStop) {
                client.close();
                continue;
            }
            boolean banIp = false;
            SpamCheck ipspam = ServerManager.ipSpamMsg.get(String.valueOf(client.getInetAddress()));
            if (ipspam != null) {
                long time = ipspam.getTimeLock();
                if (time > 0L) {
                    client.close();
                    banIp = true;
                }
            }
            if (!banIp) {
                String ip = client.getInetAddress().getHostAddress();
                Session cl = new Session(client, ++id);
                cl.IPAddress = ip;
                ServerManager.add(ip);
            }
        }
    }

    public static void setOffline() {
        try (Connection conn = DbManager.getInstance().getConnection();) {

            PreparedStatement stmt = conn.prepareStatement("UPDATE `users` SET `online` = ?;");
            stmt.setInt(1, 0);
            stmt.executeUpdate();
            stmt.close();

            stmt = conn.prepareStatement("UPDATE `players` SET `online` = ?;");
            stmt.setInt(1, 0);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Config.getInstance().load();
        if (!DbManager.getInstance().start()) {
            return;
        }
        Caption.a();
        checkAndResetNewWeek();
        ItemManager.getInstance().loadItem();
        ItemManager.getInstance().loadItemThuVanMay();
        DataCenter.gI().loadData();
        MapManager.getInstance().init();
        StoreManager.getInstance().init();
        EffectTemplateManager.getInstance().init();
        ItemManager.getInstance().loadItemNap();
        RewardManager.gI().init();
        RandomItem.init();
        Clan.getClanDAO().load();
        Ranked.init();
        AchievementList.init();
        CronJobSetup cronJobSetup = CronJobSetup.getInstance();
        cronJobSetup.setup();
        cronJobSetup.start();
    }

    private static void checkAndResetNewWeek() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDateTime lastResetNewWeekTime = getLastResetNewWeekTime();
        boolean isSameWeek = Util.isSameWeek(now, lastResetNewWeekTime);
        if (!isSameWeek) {
            Log.info("reset new week");
            DbManager.getInstance().executeUpdate("UPDATE players SET data = JSON_SET(data, '$.pointEnthusiastic', 0);");
            DbManager.getInstance().executeUpdate("UPDATE players SET data = JSON_SET(data, '$.pointEnthusiasticWeek', 0);");
            saveLastResetNewWeekTime(now);
        }
    }

    private static LocalDateTime getLastResetNewWeekTime() {
        byte[] data = Util.getFile("last_reset_week.dat");
        if (data == null) {
            return LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Util.byteArrayToLong(data)), ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    private static void saveLastResetNewWeekTime(LocalDateTime localDateTime) {
        try {
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
            Files.write(Path.of("last_reset_week.dat"), Util.longToByteArray(zonedDateTime.toInstant().toEpochMilli()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAll() {
//        try {
//            StallManager.getInstance().save();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            List<Clan> clans = Clan.getClanDAO().getAll();
            synchronized (clans) {
                for (Clan clan : clans) {
                    Clan.getClanDAO().update(clan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Char> chars = ServerManager.getChars();
            for (Char _char : chars) {
                try {
                    if (!_char.isCleaned) {
                        _char.saveData();
                        if (!_char.user.isCleaned) {
                            _char.user.saveData();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            Log.info("Làm mới bảng xếp hạng");
//            Ranked.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        isStop = true;
        Clan.running = false;
        CronJobSetup.getInstance().shutdown();
        WorldManager.getInstance().close();
//        MapManager.getInstance().close();
        StallManager.getInstance().stop();
        List<Clan> clans = Clan.getClanDAO().getAll();
        synchronized (clans) {
            for (Clan clan : clans) {
                Clan.getClanDAO().update(clan);
            }
        }
        try {
            List<User> users = ServerManager.getUsers();
            for (User user : users) {
                if (!user.isCleaned) {
                    user.session.closeMessage();
                }
            }
            server.close();
            server = null;
            Log.info("End socket");
        } catch (IOException e) {
        }
    }

    public static void maintenance(byte type) {
        try {
            GameServer.isStop = true;
            Log.info("Chuẩn bị đóng máy chủ.");
            String name = "Hệ thống";
            String text2 = "Máy chủ bảo trì sau 1 phút, vui lòng thoát game để tránh mất dữ liệu!";
            GlobalService.getInstance().chat(name, text2, (byte) 1);
            Log.info("Hệ thống Đóng sau 30 s.");
            Thread.sleep(1);
            Log.info("Hệ thống Bắt đầu đóng máy chủ.");
            try {
                ProcessBuilder pb;
                String os = System.getProperty("os.name").toLowerCase();

                if (os.contains("win")) {
                    pb = new ProcessBuilder("cmd.exe", "/c", "run.bat");
                } else {
                    pb = new ProcessBuilder("/bin/bash", "-c", "./run.sh");
                }

                pb.directory(new File(System.getProperty("user.dir")));
                pb.inheritIO();
                pb.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

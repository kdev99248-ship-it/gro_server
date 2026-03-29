package com.vdtt.server;

import com.vdtt.util.Log;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Getter
public class Config {

    @Getter
    private static final Config instance = new Config();
    private boolean showLog;
    private String host;
    // Server
    private int serverID;
    private int port;
    public boolean isKhuyenMai;
    public int percentKhuyenMai;
    public long timeResetMoc = 0;

    public int expRate = 1;
    public int[][] upgradeLevels = {
            {2, 200000, 5 * 60 * 1000},           // Cấp 1 -> 2: 5 phút
            {4, 400000, 10 * 60 * 1000},          // Cấp 2 -> 3: 10 phút
            {8, 800000, 30 * 60 * 1000},          // Cấp 3 -> 4: 30 phút
            {16, 1600000, 60 * 60 * 1000},          // Cấp 4 -> 5: 1 giờ
            {32, 3200000, 2 * 60 * 60 * 1000},     // Cấp 5 -> 6: 2 giờ
            {64, 6400000, 4 * 60 * 60 * 1000},     // Cấp 6 -> 7: 4 giờ
            {128, 12800000, 8 * 60 * 60 * 1000},     // Cấp 7 -> 8: 8 giờ
            {160, 20000000, 16 * 60 * 60 * 1000},   // Cấp 8 -> 9: 16 giờ
            {180, 40000000, 24 * 60 * 60 * 1000},   // Cấp 9 -> 10: 1 ngày
            {200, 60000000, 2 * 24 * 60 * 60 * 1000}, // Cấp 10 -> 11: 2 ngày
            {250, 80000000, 7 * 24 * 60 * 60 * 1000}, // Cấp 11 -> 12: 7 ngày
            {300, 100000000, 14 * 24 * 60 * 60 * 1000}, // Cấp 12 -> 13: 14 ngày
            {340, 120000000, 28 * 24 * 60 * 60 * 1000}, // Cấp 12 -> 13: 14 ngày
            {340, 120000000, 28 * 24 * 60 * 60 * 1000}, // Cấp 12 -> 13: 14 ngày
            {340, 120000000, 28 * 24 * 60 * 60 * 1000}, // Cấp 12 -> 13: 14 ngày
            {340, 120000000, 28 * 24 * 60 * 60 * 1000} // Cấp 13 -> 14: 28 ngày
    };

    // Game
    private String event;
    private boolean enableLogin;
    //


    public boolean load() {
        try {
            FileInputStream input = new FileInputStream("config.properties");
            Properties props = new Properties();
            props.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            showLog = Boolean.parseBoolean(props.getProperty("server.log.display"));
            serverID = Integer.parseInt(props.getProperty("server.id"));
            port = Integer.parseInt(props.getProperty("server.port"));
            enableLogin = Boolean.parseBoolean(props.getProperty("server.enableLogin"));
            isKhuyenMai = Boolean.parseBoolean(props.getProperty("isSale"));
            percentKhuyenMai = Integer.parseInt(props.getProperty("percentSale"));

            if (props.containsKey("game.event")) {
                event = props.getProperty("game.event");
            }
            if (props.containsKey("game.expRate")) {
                expRate = Integer.parseInt(props.getProperty("game.expRate"));
            }
            if (props.containsKey("server.host")) {
                host = props.getProperty("server.host");
            }else {
                host = "127.0.0.1";
            }

            String dateResetMocString = props.getProperty("dateResetMoc");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (dateResetMocString != null) {
                timeResetMoc = sdf.parse(dateResetMocString).getTime();
            }
        } catch (IOException | NumberFormatException ex) {
            Log.error("load config err: " + ex.getMessage(), ex);
            return false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}


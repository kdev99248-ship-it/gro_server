package com.vdtt.db;

import com.vdtt.data.SQLStatement;
import com.vdtt.events.AbsEvent;
import com.vdtt.events.EventHandler;
import com.vdtt.events.points.EventPoint;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.events.points.RewardPoint;
import com.vdtt.mail.Mail;
import com.vdtt.mail.MailManager;
import com.vdtt.model.Char;
import com.vdtt.server.Config;
import com.vdtt.util.Log;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class DbManager {

    private static DbManager instance = null;
    protected HikariDataSource dataSource;

    public static DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }

    public DbManager() {
        dataSource = new HikariDataSource(new HikariConfig("mysql.properties"));
    }

    public boolean start() {
        return true;
    }

    public ArrayList<HashMap<String, Object>> convertResultSetToList(ResultSet rs) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        try {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int count = resultSetMetaData.getColumnCount();
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<>();
                int index = 1;
                while (index <= count) {
                    int type = resultSetMetaData.getColumnType(index);
                    String name = resultSetMetaData.getColumnName(index);
                    switch (type) {
                        case -5: {
                            map.put(name, rs.getLong(index));
                            break;
                        }
                        case 6: {
                            map.put(name, rs.getFloat(index));
                            break;
                        }
                        case 12, 1: {
                            map.put(name, rs.getString(index));
                            break;
                        }
                        case 4: {
                            map.put(name, rs.getInt(index));
                            break;
                        }
                        case 16: {
                            map.put(name, rs.getBoolean(index));
                            break;
                        }
                        case -7: {
                            map.put(name, rs.getByte(index));
                            break;
                        }

                        default: {
                            map.put(name, rs.getObject(index));
                            break;
                        }
                    }
                    ++index;
                }
                list.add(map);
            }
            rs.close();
        } catch (SQLException sQLException) {
            Log.error("convertResultSetToList ex: " + sQLException.getMessage());
        }
        return list;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdown() {

    }

    public int update(String sql, Object... params) {
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {

            try {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                int result = stmt.executeUpdate();
                return result;
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            Log.error("update() EXCEPTION: " + e.getMessage(), e);
        }
        return -1;
    }

    public void active_account(String username) {
        String stmt = switch (Config.getInstance().getServerID()) {
            case 1:
                yield SQLStatement.ACTIVE_ACCOUNT;
            case 2:
                yield SQLStatement.ACTIVE_ACCOUNT2;
            default:
                throw new IllegalStateException("Unexpected active_account server: " + Config.getInstance().getServerID());
        };
        update(stmt, 0, username);
    }

//    public void lock_account(String username) {
//      //  update(SQLStatement.LOCK_ACCOUNT, 0, username);
//    }

//    public void banuntil_account(String username, int time) {
//    //    update(SQLStatement.BANUNI_ACCOUNT, LocalDateTime.now().plusHours(time), username);
//     //   update(SQLStatement.LOCK_ACCOUNT, 1, username);
//    }

    public int getCoin(String username) {
        int coin = 0;
        String sql = SQLStatement.GET_COIN; // Ensure this is a constant or a properly parameterized SQL query
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    coin = rs.getInt("coin");
                }
            }
        } catch (SQLException e) {
            Log.error("getCoin() EXCEPTION: " + e.getMessage(), e);
        }
        return coin;
    }

    public void updateCoin(String username, int coin) {
        String sql = "UPDATE `users` SET `coin` = `coin` - ? WHERE `username` = ?";
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coin);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.error("updateCoin() EXCEPTION: " + e.getMessage(), e);
        }
    }

    public List<Mail> getMail(String name) {
        List<Mail> list = new ArrayList<>();
        String sql = "SELECT `mail` FROM `players` WHERE `name` = ?";
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    JSONArray mail = (JSONArray) JSONValue.parse(rs.getString("mail"));
                    if (mail != null) {
                        int size2 = mail.size();
                        for (Object o : mail) {
                            try {
                                JSONObject obj = (JSONObject) o;
                                Mail mail1 = new Mail(obj);
                                if (mail1.time > (System.currentTimeMillis() / 1000)) {
                                    list.add(mail1);
                                }
                            } catch (Exception e) {
                                Log.error("err: " + e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Log.error("getMail() EXCEPTION: " + e.getMessage(), e);
        }
        return list;
    }

    public void updateMail(String name, List<Mail> mail) {
        String sql = "UPDATE `players` SET `mail` = ? WHERE `name` = ?";
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            JSONArray array = new JSONArray();
            for (Mail mail1 : mail) {
                array.add(mail1.toJson());
            }
            stmt.setString(1, array.toJSONString());
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.error("updateMail() EXCEPTION: " + e.getMessage(), e);
        }
    }

    public void executeQuery(String sqlStatement, Consumer<ResultSet> func, Object... params) {
        executeQuery(sqlStatement, rs -> {
            func.accept(rs);
            return null;
        }, params);
    }

    public Object executeQuery(String sqlStatement, Function<ResultSet, Object> func, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement,
                     ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            int index = 1;
            for (Object param : params) {
                preparedStatement.setObject(index, param);
                ++index;
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return func.apply(resultSet);
            } catch (SQLException e) {
                Log.error("Error when processing the result set.", e);
            }
        } catch (SQLException e) {
            Log.error("Error when executing the query.", e);
        }
        return null;
    }

    public int executeUpdate(String sqlStatement, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
            int index = 1;
            for (Object param : params) {
                preparedStatement.setObject(index, param);
                ++index;
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException sQLException) {
            Log.error("update ex: ", sQLException);
        }
        return -1;
    }

    public int executeUpdate(String sqlStatement, Consumer<PreparedStatement> consumer) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
            consumer.accept(preparedStatement);
            return preparedStatement.executeUpdate();
        } catch (SQLException sQLException) {
            Log.error("update ex: ", sQLException);
        }
        return -1;
    }

    public int executeUpdate(String sqlStatement, Consumer<PreparedStatement> consumer,
                             Consumer<SQLException> exception) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
            consumer.accept(preparedStatement);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exception.accept(e);
        }
        return -1;
    }

    public int executeInsertAndReturnID(String sqlStatement, Consumer<PreparedStatement> consumer) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlStatement,
                     Statement.RETURN_GENERATED_KEYS)) {
            consumer.accept(statement);
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } catch (SQLException sQLException) {
            Log.error("insert ex: ", sQLException);
            return -1;
        }
    }


    public void loadEventPoint(Char player) {
        AbsEvent event = EventHandler.getEvent();
        EventPoint eventPoint = event.findEventPointByPlayerID(player.id);
        if (eventPoint == null) {
            eventPoint = event.createEventPoint();
            eventPoint.setPlayerID(player.id);
            eventPoint.setPlayerName(player.name);
            eventPoint.setClassId(player.idCharSys);
            if (player.clan != null) {
                eventPoint.setClanName(player.clan.name);
            }
            eventPoint.addIfMissing(event.getKeys());
            event.addEventPoint(eventPoint);
        } else {
            eventPoint.setClassId(player.idCharSys);
            if (player.clan != null) {
                eventPoint.setClanName(player.clan.name);
            }
            eventPoint.addIfMissing(event.getKeys());
        }
        player.setEventPoint(eventPoint);
    }

    public void insertRewardPoint(int playerId, byte classId, String clanName, int eventId, RewardPoint rewardPoint) {
        int id = executeInsertAndReturnID(INSERT_EVENT_POINT,
                (statement) -> {
                    try {
                        statement.setInt(1, eventId);
                        statement.setInt(2, playerId);
                        statement.setByte(3, classId);
                        if (clanName != null) {
                            statement.setString(4, clanName);
                        } else {
                            statement.setNull(4, Types.VARCHAR);
                        }
                        statement.setString(5, rewardPoint.getKey().name());
                        statement.setInt(6, rewardPoint.getPoint());
                        statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
                        statement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                        statement.setInt(9, Config.getInstance().getServerID());
                    } catch (SQLException e) {
                        Log.error("Error when insert event point", e);
                    }
                });
        rewardPoint.setId(id);
    }

    public void storeEventPoint(EventPoint eventPoint) {
        eventPoint.getRewardPoints().forEach(rewardPoint -> {
            executeUpdate(UPDATE_EVENT_POINT, (statement) -> {
                try {
                    statement.setByte(1, eventPoint.getClassId());
                    if (eventPoint.getClanName() != null) {
                        statement.setString(2, eventPoint.getClanName());
                    } else {
                        statement.setNull(2, Types.VARCHAR);
                    }
                    statement.setInt(3, rewardPoint.getPoint());
                    statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    statement.setInt(5, Config.getInstance().getServerID());
                    statement.setInt(6, rewardPoint.getId());
                } catch (SQLException e) {
                    Log.error("Error when update event point", e);
                }
            });
        });
    }

    public void loadEventPoint(AbsEvent event) {
        List<KeyPoint> keys = event.getKeys();
        Map<Integer, EventPoint> points = event.getEventPoints();
        points.clear();
        executeQuery(LOAD_EVENT_POINT, (rs) -> {
            try {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int playerID = rs.getInt("player_id");
                    int server_id = rs.getInt("server_id");
                    String name = rs.getString("name");
                    byte classId = rs.getByte("class_id");
                    String clanName = rs.getString("clan_name");
                    String keyPoint = rs.getString("key_point");
                    int point = rs.getInt("point");
                    if (server_id != Config.getInstance().getServerID()) {
                        continue;
                    }
                    EventPoint eventPoint = points.get(playerID);
                    if (eventPoint == null) {
                        eventPoint = event.createEventPoint();
                        eventPoint.setPlayerID(playerID);
                        eventPoint.setPlayerName(name);
                        eventPoint.setClassId(classId);
                        eventPoint.setClanName(clanName);
                        points.putIfAbsent(playerID, eventPoint);
                    }
                    KeyPoint key = KeyPoint.valueOf(keyPoint);
                    RewardPoint rewardPoint = eventPoint.find(key);
                    if (rewardPoint != null) {
                        rewardPoint.setPoint(point);
                    } else {
                        eventPoint.add(new RewardPoint(id, key, point));
                    }
                }
            } catch (SQLException ex) {
                Log.error("Error executing query", ex);
            }
        }, event.getId());
        points.values().forEach(ev -> ev.addIfMissing(keys));
    }

    public void saveResetNewDay(int id, long timestamp) {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE `players` SET `reset_new_day_at` = ? WHERE `id` = ? LIMIT 1;")) {
            stmt.setTimestamp(1, new Timestamp(timestamp));
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Timestamp getLastResetNewDay(int id) {
        Timestamp timestamp = null;
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT `reset_new_day_at` FROM `players` WHERE `id` = ? LIMIT 1;")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    timestamp = rs.getTimestamp("reset_new_day_at");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public void saveResetNewWeek(int id, long timestamp) {
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE `players` SET `reset_new_week_at` = ? WHERE `id` = ? LIMIT 1;")) {
            stmt.setTimestamp(1, new Timestamp(timestamp));
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Timestamp getLastResetNewWeek(int id) {
        Timestamp timestamp = null;
        try (Connection conn = DbManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT `reset_new_week_at` FROM `players` WHERE `id` = ? LIMIT 1;")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    timestamp = rs.getTimestamp("reset_new_week_at");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public MailManager getMailManager(int charId) {
        MailManager mailManager = new MailManager(null);
        AtomicBoolean isError = new AtomicBoolean(true);
        executeQuery("SELECT `mail` FROM `players` WHERE `id` = ?", rs -> {
            try {
                if (rs.next()) {
                    JSONArray JArray = (JSONArray) JSONValue.parse(rs.getString("mail"));
                    if (JArray != null) {
                        isError.set(false);
                        for (Object o : JArray) {
                            JSONObject obj = (JSONObject) o;
                            Mail mail = new Mail(obj);
                            if (mail.time > (System.currentTimeMillis() / 1000)) {
                                mailManager.add(mail);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, charId);
        if (isError.get()) {
            return null;
        }

        return mailManager;
    }

    public MailManager getMailManager(String name) {
        MailManager mailManager = new MailManager(null);
        AtomicBoolean isError = new AtomicBoolean(true);
        executeQuery("SELECT `mail` FROM `players` WHERE `name` = ?", rs -> {
            try {
                if (rs.next()) {
                    JSONArray JArray = (JSONArray) JSONValue.parse(rs.getString("mail"));
                    if (JArray != null) {
                        isError.set(false);
                        for (Object o : JArray) {
                            JSONObject obj = (JSONObject) o;
                            Mail mail = new Mail(obj);
                            if (mail.time > (System.currentTimeMillis() / 1000)) {
                                mailManager.add(mail);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, name);
        if (isError.get()) {
            return null;
        }

        return mailManager;
    }

    public void saveMailManager(int charId, MailManager mailManager) {
        JSONArray JArray = mailManager.toJson();
        executeUpdate("UPDATE `players` SET `mail` = ? WHERE `id` = ?", JArray.toJSONString(), charId);
    }

    public void saveMailManager(String name, MailManager mailManager) {
        JSONArray JArray = mailManager.toJson();
        executeUpdate("UPDATE `players` SET `mail` = ? WHERE `name` = ?", JArray.toJSONString(), name);
    }

    public void renameCharacter(int id, String oldName, String newName) {
        executeUpdate("UPDATE `players` SET `name` = ? WHERE `id` = ? LIMIT 1", newName, id);
        executeUpdate("UPDATE `stall` SET `seller` = ? WHERE `seller` = ? LIMIT 1", newName, oldName);
        executeUpdate("UPDATE `clan_member` SET `name` = ? WHERE `name` = ? LIMIT 1", newName, oldName);
    }

    public static final String LOAD_EVENT_POINT = "SELECT e.*, p.`name` FROM `event_points` AS e, `players` AS p WHERE e.`event_id` = ?  AND p.`id` = e.`player_id` AND p.`server_id` = '" + Config.getInstance().getServerID() + "' ;";
    public static final String INSERT_EVENT_POINT = "INSERT INTO `event_points`(`event_id`, `player_id`, `class_id`, `clan_name`, `key_point`, `point`, `updated_at`, `created_at`, `server_id`) VALUES (?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_EVENT_POINT = "UPDATE `event_points` SET `class_id` = ?, `clan_name` = ?, `point` = ?, `updated_at` = ?, `server_id` = ? WHERE `id` = ? LIMIT 1;";

}


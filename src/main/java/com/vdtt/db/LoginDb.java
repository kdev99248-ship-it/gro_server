package com.vdtt.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author outcast c-cute hột me 😳
 */
public class LoginDb extends DbManager {
    private static LoginDb instance;

    public static LoginDb getInstance() {
        if (instance == null) {
            instance = new LoginDb();
        }
        return instance;
    }

    public LoginDb() {
        dataSource = new HikariDataSource(new HikariConfig("mysql.properties"));
    }
}

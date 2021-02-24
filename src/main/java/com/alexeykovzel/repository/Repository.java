package com.alexeykovzel.repository;

import com.alexeykovzel.BotConfig;

import java.sql.Connection;
import java.sql.DriverManager;

abstract class Repository implements IRepository {
    public static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(BotConfig.getControllerDB());
            dbConnection = DriverManager.getConnection(BotConfig.getLinkDB(), BotConfig.getUserDB(), BotConfig.getPasswordDB());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbConnection;
    }
}

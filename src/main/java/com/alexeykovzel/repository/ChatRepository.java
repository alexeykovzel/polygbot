package com.alexeykovzel.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.alexeykovzel.repository.WordRepository.getDBConnection;

public class ChatRepository extends Repository {

    public static void saveChat(int chatId, String firstName, String lastName, String username) throws SQLException {
        Connection dbConnection = null;

        try {
            dbConnection = getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(String.format("insert into chat (id, first_name, last_name, username, memory_stability) values (%s, '%s', '%s', '%s', %s);",
                    chatId, firstName, lastName, username, 0.8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert dbConnection != null;
            dbConnection.close();
        }
    }

    public static boolean checkChatExistence(int chatId) throws SQLException {
        Connection dbConnection = null;
        boolean inDatabase = false;
        try {
            dbConnection = getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(String.format("select * from chat where id = %s", chatId));
            ResultSet rs = statement.getResultSet();
            inDatabase = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert dbConnection != null;
            dbConnection.close();
        }
        return inDatabase;
    }

    public static double getMemoryStabilityByChatId(String chatId) throws SQLException {
        Connection dbConnection = null;
        try {
            dbConnection = getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(String.format("select * from chat where id = %s", chatId));
            ResultSet rs = statement.getResultSet();
            if (rs.first()) {
                return rs.getDouble("memory_stability");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert dbConnection != null;
            dbConnection.close();
        }
        throw new NullPointerException();
    }
}

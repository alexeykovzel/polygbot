package com.alexeykovzel.repository;

import com.alexeykovzel.entity.Word;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WordRepository extends Repository {

    public static void saveWord(String chatId, String wordText, double retrievability) throws SQLException {
        Connection dbConnection = null;
        try {
            dbConnection = getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(String.format("insert into word (chat_id, text, retrievability) values (%s, '%s', %s);",
                    chatId, wordText, retrievability));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert dbConnection != null;
            dbConnection.close();
        }
    }

    public static List<Word> getWordListByChatId(String chatId) throws SQLException {
        List<Word> wordList = new ArrayList<>();

        Connection dbConnection = null;
        try {
            dbConnection = getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.execute(String.format("select * from word where chat_id = %s;", chatId));
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                Word word = new Word();
                word.setChatId(chatId);
                word.setWordText(rs.getString("text"));
                word.setRetrievability(rs.getDouble("retrievability"));
                word.setTimestamp(rs.getTimestamp("timestamp"));
                wordList.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert dbConnection != null;
            dbConnection.close();
        }
        return wordList;
    }

    public static boolean isDublicate(String chatId, String wordText) throws SQLException {
        Connection dbConnection = null;
        try {
            dbConnection = getDBConnection();
            Statement statement = dbConnection.createStatement();
            statement.execute(String.format("select * from word where chat_id = %s and text = '%s';",
                    chatId, wordText));
            ResultSet rs = statement.getResultSet();
            if (rs.first()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert dbConnection != null;
            dbConnection.close();
        }
        return false;
    }
}

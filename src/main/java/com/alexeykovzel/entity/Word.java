package com.alexeykovzel.entity;

import java.sql.Timestamp;

public class Word {
    private String chatId;
    private String wordText;
    private double Retrievability;
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }

    public double getRetrievability() {
        return Retrievability;
    }

    public void setRetrievability(double retrievability) {
        Retrievability = retrievability;
    }

    public double getRetrievability(double memoryStability, double passedTime, double exp, double pow) {
        return Math.pow(exp, -Math.pow(passedTime / memoryStability, pow));
    }

    @Override
    public String toString() {
        return "Word{" +
                "chatId='" + chatId + '\'' +
                ", wordText='" + wordText + '\'' +
                ", Retrievability=" + Retrievability +
                ", timestamp=" + timestamp +
                '}';
    }
}

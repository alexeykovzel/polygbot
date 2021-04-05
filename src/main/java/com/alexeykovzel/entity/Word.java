package com.alexeykovzel.entity;

import java.sql.Timestamp;

public class Word {
    private String wordId;
    private String chatId;
    private String stringValue;
    private double Retrievability;
    private Timestamp timestamp;

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

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

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
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
                "wordId='" + wordId + '\'' +
                ", chatId='" + chatId + '\'' +
                ", stringValue='" + stringValue + '\'' +
                ", Retrievability=" + Retrievability +
                ", timestamp=" + timestamp +
                '}';
    }
}

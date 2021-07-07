package com.alexeykovzel.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Term {
    private String id;
    private String chatId;
    private String value;
    private double retrievability;
    private Timestamp timestamp;
}

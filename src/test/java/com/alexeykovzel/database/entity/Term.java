package com.alexeykovzel.database.entity;

import javax.persistence.*;
import java.sql.Timestamp;

public class Term {
    private int id;
    private String chatId;
    private String value;
    private double retrievability;
    private Timestamp timestamp;
}

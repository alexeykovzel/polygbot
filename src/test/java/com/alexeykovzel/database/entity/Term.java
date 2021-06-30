package com.alexeykovzel.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Term {
    private static final String ID_FIELD = "id";
    private static final String CHAT_ID_FIELD = "chat_id";
    private static final String VALUE_FIELD = "value";
    private static final String RETRIEVABILITY_FIELD = "retrievability";
    private static final String TIMESTAMP_FIELD = "timestamp";

    @JsonProperty("TERM_ID_FIELD")
    private String id;
    @JsonProperty("CHAT_ID_FIELD")
    private String chatId;
    @JsonProperty("VALUE_FIELD")
    private String value;
    @JsonProperty("RETRIEVABILITY_FIELD")
    private double retrievability;
    @JsonProperty("TIMESTAMP_FIELD")
    private Timestamp timestamp;
}

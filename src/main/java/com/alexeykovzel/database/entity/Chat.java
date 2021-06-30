package com.alexeykovzel.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    private static final String ID_FIELD = "id";
    private static final String FIRSTNAME_FIELD = "first_name";
    private static final String LASTNAME_FIELD = "last_name";
    private static final String USERNAME_FIELD = "username";
    private static final String MEMORY_STABILITY_FIELD = "memory_stability";

    @JsonProperty("ID_FIELD")
    private int id;
    @JsonProperty("FIRSTNAME_FIELD")
    private String firstName;
    @JsonProperty("LASTNAME_FIELD")
    private String lastName;
    @JsonProperty("USERNAME_FIELD")
    private String username;
    @JsonProperty("MEMORY_STABILITY_FIELD")
    private double memoryStability;
}

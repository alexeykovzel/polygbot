package com.alexeykovzel.database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chat {
    private static final String ID_FIELD = "id";
    private static final String FIRSTNAME_FIELD = "first_name";
    private static final String LASTNAME_FIELD = "last_name";
    private static final String USERNAME_FIELD = "username";
    private static final String MEMORY_STABILITY_FIELD = "memory_stability";

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getMemoryStability() {
        return memoryStability;
    }

    public void setMemoryStability(double memoryStability) {
        this.memoryStability = memoryStability;
    }
}

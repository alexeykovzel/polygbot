package com.alexeykovzel.database.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class User {

    @Size(min = 1, max = 64)
    private String firstName;

    @Size(max = 64)
    private String lastName;

    @Size(min = 5, max = 32)
    private String username;

    private String memoryStability;

    protected User() {
    }

    public User(String firstName, String lastName, String username, String memoryStability) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.memoryStability = memoryStability;
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

    public String getMemoryStability() {
        return memoryStability;
    }

    public void setMemoryStability(String memoryStability) {
        this.memoryStability = memoryStability;
    }
}

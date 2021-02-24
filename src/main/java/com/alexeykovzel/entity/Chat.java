package com.alexeykovzel.entity;

public class Chat {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private double memoryStability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", memoryStability=" + memoryStability +
                '}';
    }
}

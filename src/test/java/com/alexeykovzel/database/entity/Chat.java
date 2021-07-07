package com.alexeykovzel.database.entity;


import com.alexeykovzel.database.AuditTrailListener;

import javax.persistence.*;
import javax.validation.constraints.Size;

@EntityListeners(AuditTrailListener.class)
@Entity
@Table(name = "chat", schema = "polygbot")
public class Chat {
    @Id
    @Column(name = "id", length = 32)
    private String id;
    @Column(name = "first_name", length = 64, nullable = false)
    @Size(min = 1, max = 64)
    private String firstName;
    @Column(name = "last_name", length = 64)
    @Size(max = 64)
    private String lastName;
    @Column(name = "username", length = 32, nullable = false)
    @Size(min = 5, max = 32)
    private String username;
    @Column(name = "memory_stability", precision = 4, scale = 2)
    private Double memoryStability;

    protected Chat() {
    }

    public Chat(String id, String firstName, String lastName, String username, Double memoryStability) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.memoryStability = memoryStability;
    }

    @Override
    public String toString() {
        return String.format("Chat{id=%s, firstName='%s', lastName='%s', username='%s', memoryStability=%s}'",
                id, firstName, lastName, username, memoryStability);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public Double getMemoryStability() {
        return memoryStability;
    }
}

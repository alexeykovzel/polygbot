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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", length = 64, nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", length = 64)),
            @AttributeOverride(name = "username", column = @Column(name = "username", length = 32, nullable = false)),
            @AttributeOverride(name = "memoryStability", column = @Column(name = "memory_stability", precision = 4, scale = 2))
    })
    private User user;

    protected Chat() {
    }

    public Chat(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("Chat{id=%s, firstName='%s', lastName='%s', username='%s', memoryStability=%s}'",
                id, user.getFirstName(), user.getLastName(), user.getUsername(), user.getMemoryStability());
    }
}

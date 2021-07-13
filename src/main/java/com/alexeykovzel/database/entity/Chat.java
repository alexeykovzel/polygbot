package com.alexeykovzel.database.entity;

import com.alexeykovzel.database.AuditTrailListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EntityListeners(AuditTrailListener.class)
@Entity
@Table(name = "chat")

@Getter
@Setter
@NoArgsConstructor
public class Chat {

    @Id
    @Column(name = "id", length = 32)
    @Setter(AccessLevel.PROTECTED)
    private String id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", length = 64, nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", length = 64)),
            @AttributeOverride(name = "username", column = @Column(name = "username", length = 32, nullable = false, unique = true)),
            @AttributeOverride(name = "memoryStability", column = @Column(name = "memory_stability", precision = 4, scale = 2))
    })
    private User user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CaseStudy> caseStudies = new HashSet<>();

    public Chat(String id, User user) {
        this.id = id;
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("Chat{id=%s, firstName='%s', lastName='%s', username='%s', memoryStability=%s}'",
                id, user.getFirstName(), user.getLastName(), user.getUsername(), user.getMemoryStability());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id.equals(chat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package com.alexeykovzel.database.entity;


import com.alexeykovzel.database.AuditTrailListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@EntityListeners(AuditTrailListener.class)
@Entity
@Table(name = "chat")
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

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<CaseStudy> caseStudies = new HashSet<>();

    protected Chat() {
    }

    public Chat(String id, User user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<CaseStudy> getCaseStudies() {
        return caseStudies;
    }

    public void setCaseStudies(Set<CaseStudy> caseStudies) {
        this.caseStudies = caseStudies;
    }

    @Override
    public String toString() {
        return String.format("Chat{id=%s, firstName='%s', lastName='%s', username='%s', memoryStability=%s}'",
                id, user.getFirstName(), user.getLastName(), user.getUsername(), user.getMemoryStability());
    }
}

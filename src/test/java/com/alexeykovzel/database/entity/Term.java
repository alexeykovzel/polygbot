package com.alexeykovzel.database.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "term")
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<CaseStudy> caseStudies = new HashSet<>();

    @NaturalId
    @Column(name = "value")
    private String value;

    protected Term() {
    }

    public Term(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CaseStudy> getCaseStudies() {
        return caseStudies;
    }

    public void setCaseStudies(Set<CaseStudy> caseStudies) {
        this.caseStudies = caseStudies;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

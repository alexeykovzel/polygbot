package com.alexeykovzel.database.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "case_study")
@IdClass(CaseStudyId.class)
public class CaseStudy {

    @Id
    @Column(name = "term_id")
    private Long termId;

    @Id
    @Column(name = "chat_id")
    private String chatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("termId")
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "retrievability")
    private Double retrievability;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    protected CaseStudy() {
    }

    public CaseStudy(Long termId, String chatId, Double retrievability, Timestamp timestamp) {
        this.termId = termId;
        this.chatId = chatId;
        this.retrievability = retrievability;
        this.timestamp = timestamp;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Double getRetrievability() {
        return retrievability;
    }

    public void setRetrievability(Double retrievability) {
        this.retrievability = retrievability;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

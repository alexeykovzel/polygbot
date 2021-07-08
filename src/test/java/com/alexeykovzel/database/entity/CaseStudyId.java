package com.alexeykovzel.database.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CaseStudyId implements Serializable {

    private Long termId;
    private String chatId;

    public CaseStudyId() {
    }

    public CaseStudyId(Long termId, String chatId) {
        this.termId = termId;
        this.chatId = chatId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CaseStudyId that = (CaseStudyId) o;
        return Objects.equals(termId, that.termId) &&
                Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(termId, chatId);
    }
}

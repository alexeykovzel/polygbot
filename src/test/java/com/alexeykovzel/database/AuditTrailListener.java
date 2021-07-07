package com.alexeykovzel.database;

import com.alexeykovzel.database.entity.Chat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;

public class AuditTrailListener {
    private static final Log log = LogFactory.getLog(AuditTrailListener.class);

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyUpdate(Chat chat) {
        log.info("[USER AUDIT] About to add/update/delete chat: id = " + chat.getId());
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    private void afterAnyUpdate(Chat chat) {
        log.info("[USER AUDIT] add/update/delete complete for chat: id = " + chat.getId());
    }

    @PostLoad
    private void afterLoad(Chat chat) {
        log.info("[USER AUDIT] chat loaded from database: id = " + chat.getId());
    }
}

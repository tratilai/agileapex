package com.agileapex.domain;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskHistory extends DomainObject {
    private static final long serialVersionUID = -4485715956334594035L;
    private static final Logger logger = LoggerFactory.getLogger(TaskHistory.class);
    private long parentTaskUniqueId;
    private TaskHistoryEvent event;
    private String value1;
    private String value2;
    private DateTime creationDate;
    private User createdBy;
    private DateTime modifiedDate;
    private User modifiedBy;

    public TaskHistory(User createdBy) {
        this(0, 0, null, null, null, createdBy, createdBy);
    }

    public TaskHistory(long uniqueId, long parentTaskUniqueId, TaskHistoryEvent event, String value1, DateTime creationDate, User createdBy) {
        this(uniqueId, parentTaskUniqueId, event, value1, creationDate, createdBy, createdBy);
    }

    public TaskHistory(long uniqueId, long parentTaskUniqueId, TaskHistoryEvent event, String value1, DateTime creationDate, User createdBy, User modifiedBy) {
        super(uniqueId);
        this.parentTaskUniqueId = parentTaskUniqueId;
        this.event = event;
        this.value1 = value1;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "[Task history: " + super.toString() + " parent task unique id: " + parentTaskUniqueId + " event: " + event + " value1: " + value1 + " value2: " + value2 + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof TaskHistory) {
            return this.getUniqueId() == ((TaskHistory) target).getUniqueId();
        }
        return false;
    }

    public long getParentTaskUniqueId() {
        return parentTaskUniqueId;
    }

    public void setParentTaskUniqueId(long parentTaskUniqueId) {
        this.parentTaskUniqueId = parentTaskUniqueId;
    }

    public TaskHistoryEvent getEvent() {
        return event;
    }

    public void setEvent(TaskHistoryEvent event) {
        this.event = event;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}

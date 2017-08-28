package com.agileapex.domain;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.ReleasePersistence;
import com.agileapex.persistence.ReleasePersistenceImpl;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;

public class Sprint extends DomainObject implements LazyLoadable {
    private static final long serialVersionUID = 3861620580535235617L;
    private static final Logger logger = LoggerFactory.getLogger(Sprint.class);
    private Release parentRelease;
    private String name;
    private String description;
    private SprintStatus status;
    private Task rootTask;
    private DateTime startDate;
    private DateTime endDate;
    private DateTime creationDate;
    private User createdBy;
    private DateTime modifiedDate;
    private User modifiedBy;

    public Sprint() {
        this(0, null, null, null, null, null, null, null, null);
    }

    public Sprint(long uniqueId, Task rootTask, String name, DateTime startDate, DateTime endDate, User createdBy) {
        this(uniqueId, rootTask, null, name, null, null, startDate, endDate, createdBy);
    }

    public Sprint(long uniqueId, Task rootTask, Release parentRelease, String name, String description, SprintStatus status, DateTime startDate, DateTime endDate, User createdBy) {
        super(uniqueId);
        this.parentRelease = parentRelease;
        this.name = name;
        this.description = description;
        this.status = status;
        this.rootTask = rootTask;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    @Override
    public String toString() {
        return "[Sprint: " + super.toString() + " name " + name + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof Sprint) {
            return this.getUniqueId() == ((Sprint) target).getUniqueId();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Task getRootTask() {
        return rootTask;
    }

    public void setRootTask(Task rootTask) {
        this.rootTask = rootTask;
    }

    public Release getParentRelease() {
        return parentRelease;
    }

    public void setParentRelease(Release parentRelease) {
        this.parentRelease = parentRelease;
    }

    @Override
    public void fetchSecondLevelObjects() {
        logger.debug("Fetching sprint object's second level objects. This sprint id: {}", super.getUniqueId());
        if (createdBy != null) {
            UserPersistence userDbService = new UserPersistenceImpl();
            createdBy = userDbService.get(createdBy.getUniqueId());
        }
        if (parentRelease != null) {
            ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
            parentRelease = releaseDbService.get(parentRelease.getUniqueId());
        }
        if (rootTask != null) {
            TaskPersistence taskDbService = new TaskPersistenceImpl();
            rootTask = taskDbService.get(rootTask.getUniqueId());
        }
    }
}

package com.agileapex.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.EffortPersistence;
import com.agileapex.persistence.EffortPersistenceImpl;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;

public class Task extends DomainObject implements LazyLoadable {
    private static final long serialVersionUID = 5021977291767742344L;
    private static final Logger logger = LoggerFactory.getLogger(Task.class);
    private String identifier;
    private String name;
    private String description;
    private Effort effort;
    private User assigned;
    private TaskStatus status;
    private Task parent;
    private List<Task> children;
    private long orderInChildren;
    private DateTime creationDate;
    private User createdBy;
    private DateTime modifiedDate;
    private User modifiedBy;

    public Task() {
        this(0, null, null, null, null, null, null, null);
    }

    public Task(long uniqueId) {
        this(uniqueId, null, null, null, null, null, null, null);
    }

    public Task(String identifier, String name, String description, Effort effort, TaskStatus status, User createdBy) {
        this(0, identifier, name, description, effort, null, status, createdBy);
    }

    public Task(String identifier, String name, String description, Effort effort, User assigned, TaskStatus status, User createdBy) {
        this(0, identifier, name, description, effort, assigned, status, createdBy);
    }

    public Task(long uniqueId, String identifier, String name, String description, Effort effort, User assigned, TaskStatus status, User createdBy) {
        super(uniqueId);
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.effort = effort;
        this.assigned = assigned;
        this.status = status;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
        clearChildren();
    }

    @Override
    public void fetchSecondLevelObjects() {
        logger.debug("Fetching task object's second level objects. This task id: {}", super.getUniqueId());
        if (createdBy != null) {
            UserPersistence userDbService = new UserPersistenceImpl();
            createdBy = userDbService.get(createdBy.getUniqueId());
        }
        if (effort != null) {
            EffortPersistence effortDbService = new EffortPersistenceImpl();
            effort = effortDbService.get(effort.getUniqueId());
        }
        if (assigned != null) {
            UserPersistence userDbService = new UserPersistenceImpl();
            assigned = userDbService.get(assigned.getUniqueId());
        }
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        children = taskDbService.getChildren(getUniqueId());
        if (parent != null) {
            parent = taskDbService.get(parent.getUniqueId());
        }
    }

    @Override
    public String toString() {
        String parentUniqueId = parent != null ? "" + parent.getUniqueId() : "null";
        return "[Task: " + super.toString() + " name: " + name + " order in dhildren: " + orderInChildren + " parent task: " + parentUniqueId + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof Task) {
            return this.getUniqueId() == ((Task) target).getUniqueId();
        }
        return false;
    }

    public boolean isHiddenRoot() {
        return parent == null;
    }

    public String getEffortAsText() {
        return effort != null ? effort.getEffortAsText() : "";
    }

    public void setStatus(TaskStatus newStatus, User createdBy) {
        if (newStatus == TaskStatus.DONE && getEffort() != null && !getEffort().isNull()) {
            if (getEffort().getEffortLeft() != null) {
                getEffort().setEffortLeft(0L);
            }
            if (getEffort().getSumOfEffortLeft() != null) {
                getEffort().setSumOfEffortLeft(0L);
            }
            EffortPersistence effortDbService = new EffortPersistenceImpl();
            effortDbService.update(getEffort(), this, createdBy);
        }
        this.status = newStatus;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEffort(Effort effort) {
        this.effort = effort;
    }

    public Effort getEffort() {
        return effort;
    }

    public User getAssigned() {
        return assigned;
    }

    public void setAssigned(User user) {
        this.assigned = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public void setOrderInChildren(long orderInChildren) {
        this.orderInChildren = orderInChildren;
    }

    public long getOrderInChildren() {
        return orderInChildren;
    }

    public List<Task> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children == null || children.size() < 1;
    }

    public void setChildren(List<Task> children) {
        this.children = children;
    }

    public void addChildren(Task task) {
        children.add(task);
    }

    public void addChildren(int index, Task task) {
        children.add(index, task);
    }

    public void removeChildren(Task task) {
        children.remove(task);
        if (this.children.size() < 1) {
            clearChildren();
        }
    }

    public void clearChildren() {
        this.children = new ArrayList<Task>();
    }
}

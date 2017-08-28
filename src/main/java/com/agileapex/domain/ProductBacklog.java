package com.agileapex.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;

public class ProductBacklog extends DomainObject implements LazyLoadable {
    private static final long serialVersionUID = -2332782112236047352L;
    private static final Logger logger = LoggerFactory.getLogger(ProductBacklog.class);
    private Task rootTask;

    public ProductBacklog() {
        this(0, null);
    }

    public ProductBacklog(long uniqueId) {
        this(uniqueId, null);
    }

    public ProductBacklog(long uniqueId, Task rootTask) {
        super(uniqueId);
        this.setRootTask(rootTask);
    }

    @Override
    public String toString() {
        return "[ProductBacklog: " + super.toString() + (getRootTask() != null ? getRootTask().toString() : " root task is null") + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof ProductBacklog) {
            return this.getUniqueId() == ((ProductBacklog) target).getUniqueId();
        }
        return false;
    }

    public Task getRootTask() {
        return rootTask;
    }

    public void setRootTask(Task rootTask) {
        this.rootTask = rootTask;
    }

    @Override
    public void fetchSecondLevelObjects() {
        logger.debug("Fetching product backlog object's second level objects. This product backlog id: {}", super.getUniqueId());
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        rootTask = taskDbService.get(rootTask.getUniqueId());
    }
}

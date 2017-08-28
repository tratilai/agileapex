package com.agileapex.persistence;

import java.util.List;

import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;

public interface SprintPersistence {

    public abstract Sprint get(final long uniqueId);

    public abstract List<Sprint> getByParentRelease(long parentReleaseUniqueId);

    public abstract List<Sprint> getAll();

    public abstract void create(Sprint sprint);

    public abstract void create(Release release, Sprint sprint, User createdBy);

    public abstract void update(Sprint sprint);

    public abstract void delete(Sprint sprint);

    public abstract List<Task> getAllTasks(Sprint sprint);
}
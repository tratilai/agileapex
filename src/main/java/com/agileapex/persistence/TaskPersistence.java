package com.agileapex.persistence;

import java.util.List;

import com.agileapex.domain.Project;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;

public interface TaskPersistence {

    public abstract Task get(Long uniqueId);

    public abstract List<Task> getChildren(long parentTaskUniqueId);

    public abstract List<Task> getAll();

    public abstract void create(Task task, User createdBy);

    public abstract void create(Task parent, Task task, User createdBy);

    public Task createRootTask(Project project, User createdBy);

    public abstract void update(Task task);

    public abstract void delete(Task task);
}
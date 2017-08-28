package com.agileapex.persistence;

import java.util.List;

import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.User;

public interface TaskHistoryPersistence {

    public abstract List<TaskHistory> get(Long taskUniqueId);

    public abstract void createFirst(Task task, User createdBy);

    public abstract void createChange(Effort newEffort, Task task, User createdBy);

    public abstract Effort getLatestEffortValueInTaskHistory(long taskUniqueId);

    public abstract void delete(long taskUniqueId);
}

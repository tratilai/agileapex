package com.agileapex.persistence;

import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;

public interface EffortPersistence {

    public abstract Effort get(long uniqueId);

    public abstract void create(Effort effort);

    public abstract void update(Effort effort, Task task, User modifiedBy);

    public void delete(Effort effort);
}
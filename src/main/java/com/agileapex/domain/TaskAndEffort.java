package com.agileapex.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TaskAndEffort implements Comparable<TaskAndEffort> {
    public Long taskUniqueId;
    public Long effort;

    public TaskAndEffort(Long taskUniqueIf, Long effort) {
        taskUniqueId = taskUniqueIf;
        this.effort = effort;
    }

    public int hashCode() {
        return new HashCodeBuilder(47, 67).append(taskUniqueId).append(effort).toHashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TaskAndEffort))
            return false;
        if (obj == this)
            return true;

        TaskAndEffort target = (TaskAndEffort) obj;
        return new EqualsBuilder().append(taskUniqueId, target.taskUniqueId).isEquals();
    }

    @Override
    public int compareTo(TaskAndEffort target) {
        return this.taskUniqueId.compareTo(target.taskUniqueId);
    }

    @Override
    public String toString() {
        return "[TaskAndEffort: " + taskUniqueId + " effort: " + effort + "]";
    }
}

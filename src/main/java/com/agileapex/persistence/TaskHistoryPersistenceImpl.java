package com.agileapex.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.domain.User;
import com.agileapex.persistence.dao.TaskHistoryDao;

public class TaskHistoryPersistenceImpl implements TaskHistoryPersistence {
    private static final Logger logger = LoggerFactory.getLogger(TaskHistoryPersistenceImpl.class);
    private TaskHistoryDao taskHistoryDao = new TaskHistoryDao();

    // TODO: tarviisko cachen myös tälle, aika paljon getAll-kutsuja

    @Override
    public List<TaskHistory> get(Long taskUniqueId) {
        return taskHistoryDao.getAll(taskUniqueId);
    }

    @Override
    public void createFirst(Task task, User createdBy) {
        if (!task.isHiddenRoot()) {
            createEffortTaskHistory(task.getEffort(), task.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, createdBy);
        }
    }

    @Override
    public void createChange(Effort newEffort, Task task, User createdBy) {
        if (!task.isHiddenRoot()) {
            Effort previousEffort = getLatestEffortValueInTaskHistory(task.getUniqueId());
            if (previousEffort == null) {
                createEffortTaskHistory(newEffort, task.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, createdBy);
            } else if (!previousEffort.hasSameValue(newEffort)) {
                createEffortTaskHistory(newEffort, task.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, createdBy);
            }
        }
    }

    @Override
    public Effort getLatestEffortValueInTaskHistory(long taskUniqueId) {
        List<TaskHistory> fullTaskHistory = taskHistoryDao.getAll(taskUniqueId);
        Effort oldEffort = null;
        for (TaskHistory taskHistory : fullTaskHistory) {
            if (taskHistory.getEvent() == TaskHistoryEvent.EFFORT_CHANGE || taskHistory.getEvent() == TaskHistoryEvent.EFFORT_AT_CREATION) {
                Long value1 = null;
                Long value2 = null;
                if (taskHistory.getValue1() != null) {
                    value1 = Long.parseLong(taskHistory.getValue1());
                }
                if (taskHistory.getValue2() != null) {
                    value2 = Long.parseLong(taskHistory.getValue2());
                }
                oldEffort = new Effort(value1, value2);
            }
        }
        return oldEffort;
    }

    @Override
    public void delete(long taskUniqueId) {
        taskHistoryDao.delete(taskUniqueId);
    }

    private void createEffortTaskHistory(Effort effort, long parentTaskUniqueId, TaskHistoryEvent event, User createdBy) {
        TaskHistory taskHistory = new TaskHistory(createdBy);
        taskHistory.setParentTaskUniqueId(parentTaskUniqueId);
        taskHistory.setEvent(event);
        if (effort != null) {
            taskHistory.setValue1((effort.getEffortLeft() != null) ? ("" + effort.getEffortLeft()) : null);
            taskHistory.setValue2((effort.getSumOfEffortLeft() != null) ? ("" + effort.getSumOfEffortLeft()) : null);
        }
        taskHistoryDao.create(taskHistory);
    }
}

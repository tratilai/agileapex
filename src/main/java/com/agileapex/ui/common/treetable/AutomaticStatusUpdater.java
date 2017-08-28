package com.agileapex.ui.common.treetable;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Task;
import com.agileapex.domain.TaskStatus;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;
import com.agileapex.session.ApplicationSession;

public class AutomaticStatusUpdater {

    private static final Logger logger = LoggerFactory.getLogger(AutomaticStatusUpdater.class);

    public void updateTaskTreeStatuses(Task currentTask, boolean afterDeletion) {
        if (currentTask != null) {
            currentTask.fetchSecondLevelObjects();
            updateStatus(currentTask, currentTask.getStatus());
            if (!afterDeletion) {
                updateToDown(currentTask, currentTask.getStatus());
            }
            updateToUp(currentTask, afterDeletion);
        }
    }

    private void updateToDown(Task currentTask, TaskStatus status) {
        List<Task> children = currentTask.getChildren();
        for (Task child : children) {
            child.fetchSecondLevelObjects();
            updateStatus(child, status);
            updateToDown(child, status);
        }
    }

    private void updateToUp(Task currentTask, boolean afterDeletion) {
        Task parent = currentTask.getParent();
        if (afterDeletion) {
            parent = currentTask;
        }
        if (parent != null) {
            parent.fetchSecondLevelObjects();
            List<Task> children = parent.getChildren();
            if (children.size() > 0) {
                TaskStatus parentsNewStatus = TaskStatus.NOT_STARTED;
                if (isEvenOneInGivenState(children, TaskStatus.IMPEDED)) {
                    parentsNewStatus = TaskStatus.IMPEDED;
                } else {
                    if (isAllInGivenState(children, TaskStatus.NOT_STARTED)) {
                        parentsNewStatus = TaskStatus.NOT_STARTED;
                    } else if (isAllInGivenState(children, TaskStatus.DONE)) {
                        parentsNewStatus = TaskStatus.DONE;
                    } else {
                        parentsNewStatus = TaskStatus.IN_PROGRESS;
                    }
                }
                if (parent.getStatus() != parentsNewStatus) {
                    updateStatus(parent, parentsNewStatus);
                    updateToUp(parent, false);
                }
            }
        }
    }

    private void updateStatus(Task task, TaskStatus newStatus) {
        logger.debug("New task status {} for task with unique id {}", newStatus, task.getUniqueId());
        task.setStatus(newStatus, ApplicationSession.getUser());
        final TaskPersistence taskDbService = new TaskPersistenceImpl();
        taskDbService.update(task);
    }

    private boolean isAllInGivenState(List<Task> tasks, TaskStatus statusToCheck) {
        for (Task task : tasks) {
            TaskStatus status = task.getStatus();
            if (status != statusToCheck) {
                return false;
            }
        }
        return true;
    }

    private boolean isEvenOneInGivenState(List<Task> tasks, TaskStatus statusToCheck) {
        for (Task task : tasks) {
            TaskStatus status = task.getStatus();
            if (status == statusToCheck) {
                return true;
            }
        }
        return false;
    }
}

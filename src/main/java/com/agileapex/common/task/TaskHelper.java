package com.agileapex.common.task;

import com.agileapex.domain.Task;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;

public class TaskHelper {

    public boolean isTaskPartOfTheGivenTree(Task rootTask, Task taskToCheck) {
        if (rootTask.equals(taskToCheck)) {
            return true;
        }
        if (rootTask.getChildren() == null || rootTask.getChildren().size() < 1) {
            TaskPersistence taskDbService = new TaskPersistenceImpl();
            rootTask.setChildren(taskDbService.getChildren(rootTask.getUniqueId()));
        }
        boolean isPart = false;
        if (!rootTask.isLeaf()) {
            for (Task children : rootTask.getChildren()) {
                if (children.equals(taskToCheck)) {
                    isPart = true;
                } else {
                    isPart = isTaskPartOfTheGivenTree(children, taskToCheck);
                }
                if (isPart) {
                    break;
                }
            }
        }
        return isPart;
    }
}

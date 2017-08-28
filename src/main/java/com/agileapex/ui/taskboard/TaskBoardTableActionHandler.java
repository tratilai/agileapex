package com.agileapex.ui.taskboard;

import com.agileapex.domain.Task;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.task.edit.EditTaskPopup;
import com.agileapex.ui.common.treetable.TaskTreeTable;
import com.vaadin.event.Action;
import com.vaadin.ui.AbstractComponent;

public class TaskBoardTableActionHandler implements Action.Handler, Constants {
    private static final long serialVersionUID = 9169049965393409693L;
    private final AbstractComponent parentComponent;
    private final TaskTreeTable treeTable;
    private final boolean showEditAction;

    public TaskBoardTableActionHandler(AbstractComponent parentComponent, TaskTreeTable treeTable, boolean showEditAction) {
        this.parentComponent = parentComponent;
        this.treeTable = treeTable;
        this.showEditAction = showEditAction;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == EDIT_TASK_ACTION) {
            editTaskAction(target);
        } else if (action == VIEW_TASK_ACTION) {
            viewTaskAction(target);
        }
    }

    private void editTaskAction(Object target) {
        if (target != null && target instanceof Task) {
            Task task = (Task) target;
            EditTaskPopup popup = new EditTaskPopup(treeTable, task, true);
            if (popup.getParent() != null) {
                parentComponent.getWindow().showNotification("'Edit task popup window' is already open");
            } else {
                parentComponent.getWindow().addWindow(popup);
            }
        }
    }

    private void viewTaskAction(Object target) {
        if (target != null && target instanceof Task) {
            Task task = (Task) target;
            EditTaskPopup popup = new EditTaskPopup(treeTable, task, false);
            if (popup.getParent() != null) {
                parentComponent.getWindow().showNotification("'View task popup window' is already open");
            } else {
                parentComponent.getWindow().addWindow(popup);
            }
        }
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        Action[] actions = {};
        if (target != null) {
            if (showEditAction) {
                actions = new Action[] { EDIT_TASK_ACTION };
            } else {
                actions = new Action[] { VIEW_TASK_ACTION };
            }
        }
        return actions;
    }
}

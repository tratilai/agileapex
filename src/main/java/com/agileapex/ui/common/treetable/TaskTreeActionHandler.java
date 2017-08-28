package com.agileapex.ui.common.treetable;

import com.agileapex.domain.Project;
import com.agileapex.domain.Task;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.task.create.CreateNewTaskPopup;
import com.agileapex.ui.common.task.delete.DeleteTaskPopup;
import com.agileapex.ui.common.task.edit.EditTaskPopup;
import com.vaadin.event.Action;
import com.vaadin.ui.AbstractComponent;

public class TaskTreeActionHandler implements Action.Handler, Constants {
    private static final long serialVersionUID = -3536856177828720765L;
    private final AbstractComponent parentComponent;
    private final TaskTreeTable treeTable;
    private final Project project;
    private final boolean canEdit;

    public TaskTreeActionHandler(AbstractComponent parentComponent, TaskTreeTable treeTable, Project project, boolean canEdit) {
        this.parentComponent = parentComponent;
        this.treeTable = treeTable;
        this.project = project;
        this.canEdit = canEdit;
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        Action[] actions = {};
        if (treeTable.getRootTask() != null) {
            if (target == null) {
                if (canEdit) {
                    actions = new Action[] { ADD_TASK_ACTION };
                }
            } else {
                if (canEdit) {
                    actions = new Action[] { EDIT_TASK_ACTION, ADD_TASK_ACTION, DELETE_TASK_ACTION };
                } else {
                    actions = new Action[] { VIEW_TASK_ACTION };
                }
            }
        }
        return actions;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD_TASK_ACTION) {
            addTaskAction(target);
        } else if (action == DELETE_TASK_ACTION) {
            deleteTaskAction(target);
        } else if (action == EDIT_TASK_ACTION) {
            editTaskAction(target);
        } else if (action == VIEW_TASK_ACTION) {
            viewTaskAction(target);
        }
    }

    private void addTaskAction(Object target) {
        Task task = treeTable.getRootTask();
        if (target != null && target instanceof Task) {
            task = (Task) target;
        }
        CreateNewTaskPopup popup = new CreateNewTaskPopup(treeTable, task, project);
        if (popup.getParent() != null) {
            parentComponent.getWindow().showNotification("'Create new task popup window' is already open");
        } else {
            parentComponent.getWindow().addWindow(popup);
        }
    }

    private void deleteTaskAction(Object target) {
        if (target != null && target instanceof Task) {
            Task task = (Task) target;
            DeleteTaskPopup popup = new DeleteTaskPopup(treeTable, task);
            if (popup.getParent() != null) {
                parentComponent.getWindow().showNotification("'Delete task popup window' is already open");
            } else {
                parentComponent.getWindow().addWindow(popup);
            }
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
}

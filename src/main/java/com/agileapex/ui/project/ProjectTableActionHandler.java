package com.agileapex.ui.project;

import com.agileapex.domain.Project;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.project.create.CreateNewProjectPopup;
import com.agileapex.ui.common.project.delete.DeleteProjectPopup;
import com.agileapex.ui.common.project.edit.EditProjectPopup;
import com.vaadin.event.Action;

public class ProjectTableActionHandler implements Action.Handler, Constants {
    private static final long serialVersionUID = -4046348201490451741L;
    private final RefreshableComponent parentComponent;

    public ProjectTableActionHandler(RefreshableComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD_PROJECT_ACTION) {
            addProjectAction();
        } else if (action == DELETE_PROJECT_ACTION) {
            deleteProjectAction(target);
        } else if (action == EDIT_PROJECT_ACTION) {
            editProjectAction(target);
        }
    }

    private void addProjectAction() {
        CreateNewProjectPopup popup = new CreateNewProjectPopup(parentComponent);
        parentComponent.getWindow().addWindow(popup);
    }

    private void deleteProjectAction(Object target) {
        if (target != null && target instanceof Project) {
            Project project = (Project) target;
            DeleteProjectPopup popup = new DeleteProjectPopup(parentComponent, project);
            parentComponent.getWindow().addWindow(popup);
        }
    }

    private void editProjectAction(Object target) {
        if (target != null && target instanceof Project) {
            Project project = (Project) target;
            EditProjectPopup popup = new EditProjectPopup(parentComponent, project);
            parentComponent.getWindow().addWindow(popup);
        }
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        Action[] actions = {};
        if (target == null) {
            actions = new Action[] { ADD_PROJECT_ACTION };
        } else {
            actions = new Action[] { EDIT_PROJECT_ACTION, ADD_PROJECT_ACTION, DELETE_PROJECT_ACTION };
        }
        return actions;
    }
}

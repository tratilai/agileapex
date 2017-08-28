package com.agileapex.ui.sprint;

import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.sprint.create.CreateNewSprintPopup;
import com.agileapex.ui.common.sprint.delete.DeleteSprintPopup;
import com.agileapex.ui.common.sprint.edit.EditSprintPopup;
import com.vaadin.event.Action;

public class SprintTableActionHandler implements Action.Handler, Constants {
    private static final long serialVersionUID = -7167360385831396151L;
    private final RefreshableComponent parentComponent;
    private Release release;

    public SprintTableActionHandler(RefreshableComponent parentComponent, Release release) {
        this.parentComponent = parentComponent;
        this.release = release;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD_SPRINT_ACTION) {
            addSprintAction();
        } else if (action == DELETE_SPRINT_ACTION) {
            deleteSprintAction(target);
        } else if (action == EDIT_SPRINT_ACTION) {
            editSprintAction(target);
        }
    }

    private void addSprintAction() {
        CreateNewSprintPopup popup = new CreateNewSprintPopup(parentComponent, release);
        parentComponent.getWindow().addWindow(popup);
    }

    private void deleteSprintAction(Object target) {
        if (target != null && target instanceof Sprint) {
            Sprint sprint = (Sprint) target;
            DeleteSprintPopup popup = new DeleteSprintPopup(parentComponent, sprint);
            parentComponent.getWindow().addWindow(popup);
        }
    }

    private void editSprintAction(Object target) {
        if (target != null && target instanceof Sprint) {
            Sprint sprint = (Sprint) target;
            EditSprintPopup popup = new EditSprintPopup(parentComponent, sprint);
            parentComponent.getWindow().addWindow(popup);
        }
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        Action[] actions = {};
        if (target == null) {
            actions = new Action[] { ADD_SPRINT_ACTION };
        } else {
            actions = new Action[] { EDIT_SPRINT_ACTION, ADD_SPRINT_ACTION, DELETE_SPRINT_ACTION };
        }
        return actions;
    }
}

package com.agileapex.ui.release;

import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.release.create.CreateNewReleasePopup;
import com.agileapex.ui.common.release.delete.DeleteReleasePopup;
import com.agileapex.ui.common.release.edit.EditReleasePopup;
import com.vaadin.event.Action;

public class ReleaseTableActionHandler implements Action.Handler, Constants {
    private static final long serialVersionUID = 6535589104632493821L;
    private final RefreshableComponent parentComponent;

    public ReleaseTableActionHandler(RefreshableComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD_RELEASE_ACTION) {
            addReleaseAction();
        } else if (action == DELETE_RELEASE_ACTION) {
            deleteReleaseAction(target);
        } else if (action == EDIT_RELEASE_ACTION) {
            editReleaseAction(target);
        }
    }

    private void addReleaseAction() {
        Project project = ApplicationSession.getSessionDataHelper().getCurrentProject();
        project.fetchSecondLevelObjects();
        CreateNewReleasePopup popup = new CreateNewReleasePopup(parentComponent, project);
        parentComponent.getWindow().addWindow(popup);
    }

    private void deleteReleaseAction(Object target) {
        if (target != null && target instanceof Release) {
            Release release = (Release) target;
            DeleteReleasePopup popup = new DeleteReleasePopup(parentComponent, release);
            parentComponent.getWindow().addWindow(popup);
        }
    }

    private void editReleaseAction(Object target) {
        if (target != null && target instanceof Release) {
            Release release = (Release) target;
            EditReleasePopup popup = new EditReleasePopup(parentComponent, release);
            parentComponent.getWindow().addWindow(popup);
        }
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        Action[] actions = {};
        if (target == null) {
            actions = new Action[] { ADD_RELEASE_ACTION };
        } else {
            actions = new Action[] { EDIT_RELEASE_ACTION, ADD_RELEASE_ACTION, DELETE_RELEASE_ACTION };
        }
        return actions;
    }
}

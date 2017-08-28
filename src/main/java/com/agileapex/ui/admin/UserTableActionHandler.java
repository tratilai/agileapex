package com.agileapex.ui.admin;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.User;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.user.create.CreateNewUserPopup;
import com.agileapex.ui.common.user.delete.DeleteUserPopup;
import com.agileapex.ui.common.user.edit.EditUserPopup;
import com.vaadin.event.Action;
import com.vaadin.ui.Window.Notification;

public class UserTableActionHandler implements Action.Handler, Constants {
    private static final long serialVersionUID = -1143689763996455664L;
    private final RefreshableComponent parentComponent;
    private User currentUser;

    public UserTableActionHandler(RefreshableComponent parentComponent, User currentUser) {
        this.parentComponent = parentComponent;
        this.currentUser = currentUser;
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD_USER_ACTION) {
            addUserAction();
        } else if (action == EDIT_USER_ACTION) {
            editUserAction(target);
        } else if (action == DELETE_USER_ACTION) {
            deleteUserAction(target);
        }
    }

    private void addUserAction() {
        UserHelper userUtil = new UserHelper();
        if (userUtil.isRegisteredUser()) {
            CreateNewUserPopup popup = new CreateNewUserPopup(parentComponent);
            parentComponent.getWindow().addWindow(popup);
        } else {
            parentComponent.getWindow().showNotification(null, "You must register your account first", Notification.TYPE_HUMANIZED_MESSAGE);
        }
    }

    private void editUserAction(Object target) {
        UserHelper userUtil = new UserHelper();
        if (userUtil.isRegisteredUser()) {
            if (target != null && target instanceof User) {
                User user = (User) target;
                EditUserPopup popup = new EditUserPopup(parentComponent, user);
                parentComponent.getWindow().addWindow(popup);
            }
        } else {
            parentComponent.getWindow().showNotification(null, "You must register your account first", Notification.TYPE_HUMANIZED_MESSAGE);
        }
    }

    private void deleteUserAction(Object target) {
        UserHelper userUtil = new UserHelper();
        if (userUtil.isRegisteredUser()) {
            if (target != null && target instanceof User) {
                User user = (User) target;
                DeleteUserPopup popup = new DeleteUserPopup(parentComponent, user);
                parentComponent.getWindow().addWindow(popup);
            }
        } else {
            parentComponent.getWindow().showNotification(null, "You must register your account first", Notification.TYPE_HUMANIZED_MESSAGE);
        }
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        Action[] actions = {};
        if (target == null) {
            actions = new Action[] { ADD_USER_ACTION };
        } else if (target != null && target instanceof User && currentUser.equals(target)) {
            actions = new Action[] { EDIT_USER_ACTION, ADD_USER_ACTION };
        } else {
            actions = new Action[] { EDIT_USER_ACTION, ADD_USER_ACTION, DELETE_USER_ACTION };
        }
        return actions;
    }
}

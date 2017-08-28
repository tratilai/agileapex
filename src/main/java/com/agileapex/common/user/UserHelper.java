package com.agileapex.common.user;

import java.util.List;

import com.agileapex.domain.Authorization;
import com.agileapex.domain.Customer;
import com.agileapex.domain.User;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.session.ApplicationSession;

public class UserHelper {

    public UserHelper() {
    }

    public boolean isRegisteredUser() {
        return isRegisteredUser(ApplicationSession.getUser());
    }

    public boolean isRegisteredUser(User user) {
        Customer customer = user.getCustomer();
        return customer != null && customer.getRegistrationDate() != null;
    }

    public boolean isUniqueUserName(User currentUser, String userNameToCheck) {
        UserPersistence userDbService = new UserPersistenceImpl();
        List<User> users = userDbService.getAll();
        for (User user : users) {
            if (currentUser != null) {
                if (!currentUser.equals(user) && user.getEmail().equalsIgnoreCase(userNameToCheck)) {
                    return false;
                }
            } else {
                if (user.getEmail().equalsIgnoreCase(userNameToCheck)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String setAuthorizationDescriptionText(Authorization authorization) {
        String response = "";
        if (authorization.equals(Authorization.ADMIN)) {
            response = "\n" + Authorization.ADMIN.getName() + ":\nCan do same as Manager but also execute administator tasks like creating new users.";
        } else if (authorization.equals(Authorization.MANAGER)) {
            response = "\n" + Authorization.MANAGER.getName() + ":\nCan do same as Sprint planner but also create, edit and delete projects and releases.";
        } else if (authorization.equals(Authorization.SPRINT_PLANNER)) {
            response = "\n" + Authorization.SPRINT_PLANNER.getName() + ":\nCan do same as Reporter but also create, edit and delete sprints.";
        } else if (authorization.equals(Authorization.REPORTER)) {
            response = "\n" + Authorization.REPORTER.getName() + ":\nCan do same as Viewer but also report progress in Task board.";
        } else if (authorization.equals(Authorization.VIEWER)) {
            response = "\n" + Authorization.VIEWER.getName() + ":\nCan view projects, releases, sprints and tasks, but can not modify anything.";
        }
        return response;
    }

    public boolean hasAdminPrivileges() {
        return ApplicationSession.getUser().getAuthorization() != null ? ApplicationSession.getUser().getAuthorization().hasAdminPrivileges() : false;
    }

    public boolean hasManagerPrivileges() {
        return ApplicationSession.getUser().getAuthorization() != null ? ApplicationSession.getUser().getAuthorization().hasManagerPrivileges() : false;
    }

    public boolean hasSprintPlannerPrivileges() {
        return ApplicationSession.getUser().getAuthorization() != null ? ApplicationSession.getUser().getAuthorization().hasSprintPlannerPrivileges() : false;
    }

    public boolean hasReporterPrivileges() {
        return ApplicationSession.getUser().getAuthorization() != null ? ApplicationSession.getUser().getAuthorization().hasReporterPrivileges() : false;
    }

    public boolean hasViewerPrivileges() {
        return ApplicationSession.getUser().getAuthorization() != null ? ApplicationSession.getUser().getAuthorization().hasViewerPrivileges() : false;
    }
}

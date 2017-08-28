package com.agileapex.ui.common.user.delete;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Authorization;
import com.agileapex.domain.User;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class DeleteButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteButtonClickListener.class);
    private static final long serialVersionUID = 3734439762594582308L;
    private final DeleteUserPopup layout;

    public DeleteButtonClickListener(DeleteUserPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Delete user button clicked: {}", layout.userToDelete);
        if (!layout.userToDelete.equals(ApplicationSession.getUser())) {
            if (!isLastAdminUser()) {
                logger.debug("About to delete user. User to delete: {}", layout.userToDelete);
                UserPersistence userDbService = new UserPersistenceImpl();
                userDbService.delete(layout.userToDelete);
                logger.debug("User deleted.");
            } else {
                logger.info("Can not delete the last admin user. User to delete: {}", layout.userToDelete);
                layout.parentLayout.getWindow().showNotification(null, "Can not delete the last admin user.", Notification.TYPE_ERROR_MESSAGE);
            }
        } else {
            logger.error("Internal error, this should not happen. Can not delete user in action.");
            layout.parentLayout.getWindow().showNotification(null, "Can not delete user in action.", Notification.TYPE_ERROR_MESSAGE);
        }
        layout.getParent().removeWindow(layout);
        layout.parentLayout.refresh();
    }

    private boolean isLastAdminUser() {
        if (layout.userToDelete.getAuthorization().equals(Authorization.ADMIN)) {
            UserPersistence userDbService = new UserPersistenceImpl();
            List<User> allUsers = userDbService.getAll();
            if (allUsers.size() > 1) {
                int adminUserCount = 0;
                for (User oneUser : allUsers) {
                    if (oneUser.getAuthorization().equals(Authorization.ADMIN)) {
                        adminUserCount++;
                    }
                }
                if (adminUserCount < 2) {
                    return true;
                }
            }
        }
        return false;
    }
}

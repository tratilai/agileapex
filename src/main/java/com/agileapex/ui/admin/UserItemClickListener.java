package com.agileapex.ui.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.User;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.user.edit.EditUserPopup;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.Notification;

public class UserItemClickListener implements ItemClickEvent.ItemClickListener {
    private static final Logger logger = LoggerFactory.getLogger(UserItemClickListener.class);
    private static final long serialVersionUID = -5132874319294833039L;
    private final RefreshableComponent parentComponent;
    private final Table usersTable;

    public UserItemClickListener(RefreshableComponent parentComponent, Table usersTable) {
        this.parentComponent = parentComponent;
        this.usersTable = usersTable;
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Object itemId = event.getItemId();
        if (itemId != null && itemId instanceof User) {
            User user = (User) itemId;
            if (event.getButton() == ItemClickEvent.BUTTON_RIGHT) {
                logger.debug("User table item selected. Right clicked.");
                usersTable.setValue(null);
                usersTable.select(event.getItemId());
            } else if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
                if (event.isDoubleClick()) {
                    UserHelper userUtil = new UserHelper();
                    if (userUtil.isRegisteredUser()) {
                        logger.debug("User table item opened. Double left clicked.");
                        EditUserPopup popup = new EditUserPopup(parentComponent, user);
                        parentComponent.getWindow().addWindow(popup);
                    } else {
                        parentComponent.getWindow().showNotification(null, "You must register your account first", Notification.TYPE_HUMANIZED_MESSAGE);
                    }
                }
            }
        }
    }
}

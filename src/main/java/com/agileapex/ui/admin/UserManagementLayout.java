package com.agileapex.ui.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.User;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;

public class UserManagementLayout extends GridLayout implements RefreshableComponent, Constants {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementLayout.class);
    private static final long serialVersionUID = -7514227281430935644L;
    private Table usersTable;
    private List<User> users;

    public UserManagementLayout() {
        super(1, 2);
        createLayout();
        createUserTable();
    }

    @Override
    public void attach() {
        setAdvancedFunctionalities();
        refresh();
    }

    private void createLayout() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);
        setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1000);
        setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1);
    }

    private void createUserTable() {
        usersTable = new Table("List of users");
        usersTable.setSizeFull();
        usersTable.setSelectable(true);
        usersTable.setMultiSelect(false);
        usersTable.setImmediate(true);
        usersTable.setColumnReorderingAllowed(true);
        BeanItemContainer<User> userContainer = new BeanItemContainer<User>(User.class);
        usersTable.setContainerDataSource(userContainer);

        UserHelper userUtil = new UserHelper();
        if (userUtil.isRegisteredUser()) {
            usersTable.setColumnHeader("email", "Email");
        }

        usersTable.setColumnHeader("lastName", "Last name");
        usersTable.setColumnHeader("firstName", "First name");
        usersTable.setColumnHeader("visibleAuthorizationName", "Authorization");
        if (userUtil.isRegisteredUser()) {
            usersTable.setVisibleColumns(new Object[] { "email", "lastName", "firstName", "visibleAuthorizationName" });
            usersTable.setSortContainerPropertyId("email");
        } else {
            usersTable.setVisibleColumns(new Object[] { "lastName", "firstName", "visibleAuthorizationName" });            
            usersTable.setSortContainerPropertyId("lastName");
        }
        addComponent(usersTable, COLUMN_ONE, ROW_TWO);
    }

    private void setAdvancedFunctionalities() {
        usersTable.addActionHandler(new UserTableActionHandler(this, ApplicationSession.getUser()));
        usersTable.addListener(new UserItemClickListener(this, usersTable));
    }

    @Override
    public void refresh() {
        logger.debug("Refreshing user management layout.");
        usersTable.removeAllItems();
        UserPersistence userDbService = new UserPersistenceImpl();
        users = userDbService.getAll();
        if (users != null) {
            for (User user : users) {
                usersTable.addItem(user);
            }
            usersTable.sort();
        }
    }
}

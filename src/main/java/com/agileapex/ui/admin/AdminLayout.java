package com.agileapex.ui.admin;

import com.agileapex.ui.common.Constants;
import com.vaadin.ui.GridLayout;

public class AdminLayout extends GridLayout implements Constants {
    private static final long serialVersionUID = 2865946029866167620L;

    public AdminLayout() {
        super(1, 1);
        createLayout();
    }

    private void createLayout() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        createTabs();
    }

    private void createTabs() {
        UserManagementLayout userManagement = new UserManagementLayout();
        addComponent(userManagement);
    }
}

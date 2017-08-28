package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.admin.AdminLayout;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class AdminPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -8445096097698603667L;

    public AdminPage() {
        super();
        init();
        addHeaderPanel();
        addAdminPanel();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.ADMIN);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void addAdminPanel() {
        AdminLayout adminPanel = new AdminLayout();
        addComponent(adminPanel);
        setExpandRatio(adminPanel, EXPAND_RATIO_1000);
    }
}

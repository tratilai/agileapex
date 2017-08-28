package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.report.ReportsPanel;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class ReportsPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -8009861310841756435L;

    public ReportsPage() {
        super();
        init();
        addHeaderPanel();
        addReportsPanel();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.REPORTS);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void addReportsPanel() {
        ReportsPanel reportsPanel = new ReportsPanel();
        addComponent(reportsPanel);
        setExpandRatio(reportsPanel, EXPAND_RATIO_1000);
    }
}

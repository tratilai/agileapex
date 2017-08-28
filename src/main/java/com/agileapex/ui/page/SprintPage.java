package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.sprint.SprintPanel;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class SprintPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -211691937893500305L;

    public SprintPage() {
        super();
        init();
        addHeaderPanel();
        addSprintPanel();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.SPRINT);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void addSprintPanel() {
        SprintPanel sprintPanel = new SprintPanel();
        addComponent(sprintPanel);
        setExpandRatio(sprintPanel, EXPAND_RATIO_1000);
    }
}

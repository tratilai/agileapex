package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.planning.SprintPlanningPanel;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class SprintPlanningPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = 4349591903063317625L;

    public SprintPlanningPage() {
        super();
        init();
        addHeaderPanel();
        SprintPlanningPanel sprintsPanel = new SprintPlanningPanel();
        addComponent(sprintsPanel);
        setExpandRatio(sprintsPanel, EXPAND_RATIO_1000);
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.SPRINT_PLANNING);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }
}

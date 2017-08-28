package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.backlog.ProjectBacklogPanel;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class ProjectBacklogPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -4589404609715881917L;

    public ProjectBacklogPage() {
        super();
        init();
        addHeaderPanel();
        addTaskBoardPanel();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.PROJECT_BACKLOG);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void addTaskBoardPanel() {
        ProjectBacklogPanel projectBacklogPanel = new ProjectBacklogPanel();
        addComponent(projectBacklogPanel);
        setExpandRatio(projectBacklogPanel, EXPAND_RATIO_1000);
    }
}

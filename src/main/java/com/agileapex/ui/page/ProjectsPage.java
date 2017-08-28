package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.project.ProjectPanel;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class ProjectsPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -628982241002180859L;

    public ProjectsPage() {
        super();
        init();
        addHeaderPanel();
        addProjectsLayout();
    }

    private void addProjectsLayout() {
        ProjectPanel projectsLayout = new ProjectPanel();
        addComponent(projectsLayout);
        setExpandRatio(projectsLayout, EXPAND_RATIO_1000);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.PROJECTS);
    }
}

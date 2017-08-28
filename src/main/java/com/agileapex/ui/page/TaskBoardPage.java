package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.taskboard.TaskBoardPanel;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class TaskBoardPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -3032540886168773177L;

    public TaskBoardPage() {
        super();
        init();
        addHeaderPanel();
        addTaskBoardPanel();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.TASK_BOARD);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void addTaskBoardPanel() {
        TaskBoardPanel taskBoardPanel = new TaskBoardPanel();
        addComponent(taskBoardPanel);
        setExpandRatio(taskBoardPanel, EXPAND_RATIO_1000);
    }
}

package com.agileapex.ui.report;

import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;

public class ReportsPanel extends Panel implements RefreshableComponent, Constants {
    private static final long serialVersionUID = -7028037110703263103L;
    protected HorizontalSplitPanel mainLayout;
    protected ReportTreePanel reportTreePanel;
    protected ReportsInfoPanel reportsInfoPanel;

    public ReportsPanel() {
        init();
        createRightSide();
        createLeftSide();
    }

    private void init() {
        setSizeFull();
        HorizontalLayout marginLayout = new HorizontalLayout();
        marginLayout.setSizeFull();
        marginLayout.setMargin(true);
        setContent(marginLayout);
        mainLayout = new HorizontalSplitPanel();
        mainLayout.setSplitPosition(THE_20_PERCENT, Sizeable.UNITS_PERCENTAGE);
        marginLayout.addComponent(mainLayout);
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
    }

    private void createLeftSide() {
        reportTreePanel = new ReportTreePanel(reportsInfoPanel);
        mainLayout.setFirstComponent(reportTreePanel);
    }

    private void createRightSide() {
        reportsInfoPanel = new ReportsInfoPanel();
        mainLayout.setSecondComponent(reportsInfoPanel);
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }
}

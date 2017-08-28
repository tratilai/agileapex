package com.agileapex.ui.planning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.ui.common.Constants;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class SprintPlanningPanel extends Panel implements Constants {
    private static final Logger logger = LoggerFactory.getLogger(SprintPlanningPanel.class);
    private static final long serialVersionUID = 4908875360676807535L;
    private HorizontalSplitPanel mainSplitPanel;
    private SprintListLayout sprintListLayout;
    private SprintBacklogLayout sprintBacklogLayout;
    private VerticalLayout layout;

    public SprintPlanningPanel() {
        init();
        createSplitterContent();
    }

    private void init() {
        layout = (VerticalLayout) getContent();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setSizeFull();
    }

    private void createSplitterContent() {
        VerticalLayout leftLayout = createLeftLayout();
        ProductBacklogLayout rightLayout = createRightLayout();
        addSplitter(leftLayout, rightLayout);
    }

    private VerticalLayout createLeftLayout() {
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setSizeFull();
        leftLayout.setMargin(false);
        leftLayout.setSpacing(true);
        sprintListLayout = new SprintListLayout(this);
        sprintBacklogLayout = new SprintBacklogLayout();
        leftLayout.addComponent(sprintListLayout);
        leftLayout.setExpandRatio(sprintListLayout, EXPAND_RATIO_1);
        leftLayout.addComponent(sprintBacklogLayout);
        leftLayout.setExpandRatio(sprintBacklogLayout, EXPAND_RATIO_1000);
        return leftLayout;
    }

    private ProductBacklogLayout createRightLayout() {
        return new ProductBacklogLayout();
    }

    private void addSplitter(VerticalLayout leftLayout, ProductBacklogLayout rightLayout) {
        mainSplitPanel = new HorizontalSplitPanel();
        mainSplitPanel.setSplitPosition(50.0f, UNITS_PERCENTAGE);
        mainSplitPanel.setFirstComponent(leftLayout);
        mainSplitPanel.setSecondComponent(rightLayout);
        layout.addComponent(mainSplitPanel);
        layout.setExpandRatio(mainSplitPanel, EXPAND_RATIO_1000);
    }

    public void refreshSprintBacklog() {
        logger.debug("Refreshing sprint planning panel.");
        sprintBacklogLayout.refresh();
    }
}

package com.agileapex.ui.sprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Sprint;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.window.MainWindow;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.event.ItemClickEvent;

public class SprintTableItemClickListener implements ItemClickEvent.ItemClickListener {
    private static final Logger logger = LoggerFactory.getLogger(SprintTableItemClickListener.class);
    private static final long serialVersionUID = 5831118924735015825L;
    private final SprintPanel sprintPanel;

    public SprintTableItemClickListener(SprintPanel sprintPanel) {
        this.sprintPanel = sprintPanel;
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Sprint sprint = (Sprint) event.getItemId();
        sprint.fetchSecondLevelObjects();
        if (event.getButton() == ItemClickEvent.BUTTON_RIGHT) {
            logger.debug("Sprint table item selected. Right button clicked.");
            sprintPanel.sprintsTable.setValue(null);
            sprintPanel.sprintsTable.select(event.getItemId());
        } else if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
            if (event.isDoubleClick()) {
                logger.debug("Sprint table item opened. Left buttond double clicked.");
                MainWindow mainWindow = (MainWindow) sprintPanel.getWindow();
                ApplicationSession.getSessionDataHelper().setCurrentSprint(sprint);
                ApplicationSession.getSessionDataHelper().setTargetPage(WindowIdentification.SPRINT_PLANNING);
                mainWindow.changePage();
            } else {
                logger.debug("Sprint table item selected. Left button clicked.");
            }
        }
    }
}

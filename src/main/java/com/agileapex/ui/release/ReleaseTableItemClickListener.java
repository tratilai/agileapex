package com.agileapex.ui.release;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Release;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.window.MainWindow;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.event.ItemClickEvent;

public class ReleaseTableItemClickListener implements ItemClickEvent.ItemClickListener {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseTableItemClickListener.class);
    private static final long serialVersionUID = 9158431015479867059L;
    private final ReleasePanel releasePanel;

    public ReleaseTableItemClickListener(ReleasePanel releasePanel) {
        this.releasePanel = releasePanel;
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Release release = (Release) event.getItemId();
        release.fetchSecondLevelObjects();
        if (event.getButton() == ItemClickEvent.BUTTON_RIGHT) {
            logger.debug("Release table item selected. Right button clicked.");
            releasePanel.releasesTable.setValue(null);
            releasePanel.releasesTable.select(event.getItemId());
        } else if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
            if (event.isDoubleClick()) {
                logger.debug("Release table item opened. Left button double clicked.");
                MainWindow mainWindow = (MainWindow) releasePanel.getWindow();
                ApplicationSession.getSessionDataHelper().setCurrentRelease(release);
                ApplicationSession.getSessionDataHelper().setTargetPage(WindowIdentification.SPRINT);
                mainWindow.changePage();
            } else {
                logger.debug("Release table item selected. Left button clicked.");
            }
        }
    }
}

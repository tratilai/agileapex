package com.agileapex.ui.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Project;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.window.MainWindow;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.event.ItemClickEvent;

public class ProjectTableItemClickListener implements ItemClickEvent.ItemClickListener {
    private static final Logger logger = LoggerFactory.getLogger(ProjectTableItemClickListener.class);
    private static final long serialVersionUID = 3683727094610582286L;
    private final ProjectPanel projectPanel;

    public ProjectTableItemClickListener(ProjectPanel projectPanel) {
        this.projectPanel = projectPanel;
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Project project = (Project) event.getItemId();
        project.fetchSecondLevelObjects();
        if (event.getButton() == ItemClickEvent.BUTTON_RIGHT) {
            logger.debug("Project table item selected. Right button clicked.");
            projectPanel.projectsTable.setValue(null);
            projectPanel.projectsTable.select(event.getItemId());
        } else if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
            if (event.isDoubleClick()) {
                logger.debug("Project table item opened. Left button double clicked.");
                MainWindow mainWindow = (MainWindow) projectPanel.getWindow();
                ApplicationSession.getSessionDataHelper().setCurrentProject(project);
                ApplicationSession.getSessionDataHelper().setTargetPage(WindowIdentification.RELEASES);
                mainWindow.changePage();
            } else {
                logger.debug("Project table item selected. Left button clicked.");
            }
        }
    }
}

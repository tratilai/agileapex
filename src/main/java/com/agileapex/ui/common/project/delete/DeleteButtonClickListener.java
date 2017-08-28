package com.agileapex.ui.common.project.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class DeleteButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteButtonClickListener.class);
    private static final long serialVersionUID = -851826773052854307L;
    private final DeleteProjectPopup layout;

    public DeleteButtonClickListener(DeleteProjectPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Delete project button clicked: {}", layout.project);
        ProjectPersistence projectDbService = new ProjectPersistenceImpl();
        projectDbService.delete(layout.project);
        layout.getParent().removeWindow(layout);
        layout.parentLayout.refresh();
        logger.debug("Project deleted.");
    }
}

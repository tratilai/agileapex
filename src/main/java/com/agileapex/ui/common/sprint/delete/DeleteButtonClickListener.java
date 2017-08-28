package com.agileapex.ui.common.sprint.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.SprintPersistence;
import com.agileapex.persistence.SprintPersistenceImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class DeleteButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteButtonClickListener.class);
    private static final long serialVersionUID = -2140403041274109310L;
    private final DeleteSprintPopup layout;

    public DeleteButtonClickListener(DeleteSprintPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Delete sprint button clicked: {}", layout.sprint);
        SprintPersistence sprintDbService = new SprintPersistenceImpl();
        sprintDbService.delete(layout.sprint);
        layout.getParent().removeWindow(layout);
        layout.parentLayout.refresh();
        logger.debug("Sprint deleted.");
    }
}

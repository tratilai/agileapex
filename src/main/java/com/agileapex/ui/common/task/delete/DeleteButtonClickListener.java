package com.agileapex.ui.common.task.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Task;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;
import com.agileapex.ui.common.treetable.AutomaticStatusUpdater;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class DeleteButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteButtonClickListener.class);
    private static final long serialVersionUID = -2886269168809274400L;
    private final DeleteTaskPopup layout;

    public DeleteButtonClickListener(DeleteTaskPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Delete task button clicked: {}", layout.task);
        Task parent = layout.task.getParent();
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        taskDbService.delete(layout.task);
        AutomaticStatusUpdater statusUpdater = new AutomaticStatusUpdater();
        statusUpdater.updateTaskTreeStatuses(parent, true);
        layout.getParent().removeWindow(layout);
        layout.parentLayout.refresh();
        logger.debug("Task(s) deleted.");
    }
}

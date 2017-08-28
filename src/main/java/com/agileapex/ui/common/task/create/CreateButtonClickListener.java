package com.agileapex.ui.common.task.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.task.TaskIdentifierGenerator;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskStatus;
import com.agileapex.persistence.EffortPersistence;
import com.agileapex.persistence.EffortPersistenceImpl;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.task.TaskDetailsValidator;
import com.agileapex.ui.common.treetable.AutomaticStatusUpdater;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CreateButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateButtonClickListener.class);
    private static final long serialVersionUID = 3893002396216528908L;
    private CreateNewTaskPopup layout;
    private final boolean closeAfterCreation;

    public CreateButtonClickListener(CreateNewTaskPopup layout, boolean closeAfterCreation) {
        this.layout = layout;
        this.closeAfterCreation = closeAfterCreation;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Create a new task button clicked. closeAfterCreation {}", closeAfterCreation);
        TaskDetailsValidator taskValidator = new TaskDetailsValidator();
        if (taskValidator.isValid(layout.getWindow(), layout.nameTextField, layout.descriptionTextField, layout.effortTextField, true)) {
            Task task = setDataFromPopupFields();
            TaskPersistence taskDbService = new TaskPersistenceImpl();
            taskDbService.create(layout.parentTask, task, ApplicationSession.getUser());
            AutomaticStatusUpdater statusUpdater = new AutomaticStatusUpdater();
            statusUpdater.updateTaskTreeStatuses(task, false);
            if (closeAfterCreation) {
                layout.getParent().removeWindow(layout);
            } else {
                layout.clearFields();
            }
            layout.nameTextField.focus();
            layout.treeTable.refresh();
            logger.debug("New task created: {}", task);
        }
    }

    private Task setDataFromPopupFields() {
        String name = (String) layout.nameTextField.getValue();
        String description = (String) layout.descriptionTextField.getValue();
        Object effortObject = layout.effortTextField.getValue();
        Effort effort = new Effort(null, null);
        if (effortObject != null && ((String) effortObject).length() > 0) {
            effort = new Effort(Long.parseLong((String) effortObject), null);
        }
        EffortPersistence effortDbService = new EffortPersistenceImpl();
        effortDbService.create(effort);
        TaskIdentifierGenerator idGenerator = new TaskIdentifierGenerator(layout.project);
        String id = idGenerator.getNextTaskIdentifier();
        Task newTask = new Task(id, name, description, effort, null, TaskStatus.NOT_STARTED, ApplicationSession.getUser());
        return newTask;
    }
}

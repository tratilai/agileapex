package com.agileapex.ui.common.task.edit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.effort.EffortHelper;
import com.agileapex.domain.Effort;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.EffortPersistence;
import com.agileapex.persistence.EffortPersistenceImpl;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.task.TaskDetailsValidator;
import com.agileapex.ui.common.treetable.AutomaticStatusUpdater;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class SaveButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(SaveButtonClickListener.class);
    private static final long serialVersionUID = -2312993471564027109L;
    private final EditTaskPopup layout;

    public SaveButtonClickListener(EditTaskPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        edit();
    }

    private void edit() {
        logger.debug("Save button clicked for edit task.");
        TaskDetailsValidator taskValidator = new TaskDetailsValidator();
        if (taskValidator.isValid(layout.getWindow(), layout.nameTextField, layout.descriptionTextField, layout.effortTextField, layout.task.isLeaf())) {
            setDataFromPopupFields();
            TaskPersistence taskDbService = new TaskPersistenceImpl();
            taskDbService.update(layout.task);
            AutomaticStatusUpdater statusUpdater = new AutomaticStatusUpdater();
            statusUpdater.updateTaskTreeStatuses(layout.task, false);
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
        }
    }

    private void setDataFromPopupFields() {
        String effort = (String) layout.effortTextField.getValue();
        layout.task.setName((String) layout.nameTextField.getValue());
        layout.task.setDescription((String) layout.descriptionTextField.getValue());
        if (!layout.task.isLeaf() && StringUtils.isNotEmpty(effort)) {
            EffortHelper effortHelper = new EffortHelper();
            effort = "" + effortHelper.getLongValueFromTextValue(effort);
        }
        Long newEffort = null;
        if (StringUtils.isNotEmpty(effort)) {
            newEffort = Long.parseLong(effort);
        }
        Effort oldEffort = layout.task.getEffort();
        oldEffort.setEffortLeft(newEffort);
        EffortPersistence effortDbService = new EffortPersistenceImpl();
        effortDbService.update(oldEffort, layout.task, ApplicationSession.getUser());
        layout.task.setStatus((TaskStatus) layout.status.getValue(), ApplicationSession.getUser());
        if (layout.pointPersonSelect.getValue() != null) {
            layout.task.setAssigned((User) layout.pointPersonSelect.getValue());
        } else {
            layout.task.setAssigned(null);
        }
    }
}

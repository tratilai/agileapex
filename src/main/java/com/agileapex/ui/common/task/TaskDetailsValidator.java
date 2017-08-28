package com.agileapex.ui.common.task;

import org.apache.commons.lang.StringUtils;

import com.agileapex.ui.common.Constants;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class TaskDetailsValidator implements Constants {

    public boolean isValid(Window parentWindow, TextField nameTF, TextField descriptionTF, TextField effortTF, boolean isLeaf) {
        String name = StringUtils.trimToEmpty((String) nameTF.getValue());
        String description = StringUtils.trimToEmpty((String) descriptionTF.getValue());
        String effortString = StringUtils.trimToEmpty((String) effortTF.getValue());
        if (StringUtils.isEmpty(name)) {
            parentWindow.showNotification(null, "Task Name can not be empty", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
        } else if (name.length() > TASK_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "Task Name is too long, maximum is " + TASK_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
        } else if (description.length() > TASK_DESCRIPTION_MAX_LENGTH) {
            parentWindow.showNotification(null, "Task Description is too long, maximum " + TASK_DESCRIPTION_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            descriptionTF.focus();
        } else if (isLeaf && StringUtils.isNotEmpty(effortString) && StringUtils.isNumeric(effortString) && (Long.parseLong(effortString) < 0 || Long.parseLong(effortString) > TASK_EFFORT_MAX_LENGTH)) {
            parentWindow.showNotification(null, "Task Effort must be a integer number between 0 and " + TASK_EFFORT_MAX_LENGTH, Notification.TYPE_WARNING_MESSAGE);
            effortTF.focus();
        } else {
            return true;
        }
        return false;
    }
}

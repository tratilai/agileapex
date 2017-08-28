package com.agileapex.ui.common.project;

import org.apache.commons.lang.StringUtils;

import com.agileapex.common.project.ProjectHelper;
import com.agileapex.domain.Project;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class ProjectDetailsValidator implements Constants {

    public boolean isValid(Project project, Window parentWindow, TextField nameTF, TextField descriptionTF) {
        return isValid(project, parentWindow, nameTF, descriptionTF, null);
    }

    public boolean isValid(Window parentWindow, TextField nameTF, TextField descriptionTF, TextField taskPrefixTF) {
        return isValid(null, parentWindow, nameTF, descriptionTF, taskPrefixTF);
    }

    public boolean isValid(Project project, Window parentWindow, TextField nameTF, TextField descriptionTF, TextField taskPrefixTF) {
        String name = StringUtils.trimToEmpty((String) nameTF.getValue());
        String description = StringUtils.trimToEmpty((String) descriptionTF.getValue());
        String taskPrefix = "";
        if (taskPrefixTF != null) {
            taskPrefix = StringUtils.trimToEmpty((String) taskPrefixTF.getValue());
        }
        if (StringUtils.isEmpty(name) || name.length() < PROJECT_NAME_MIN_LENGTH || name.length() > PROJECT_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "Project Name length must be from " + PROJECT_NAME_MIN_LENGTH + " to " + PROJECT_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
            return false;
        }
        if (description.length() > PROJECT_DESCRIPTION_MAX_CHARACTERS) {
            parentWindow.showNotification(null, "Project Description is too long, maximum is " + PROJECT_DESCRIPTION_MAX_CHARACTERS + " characters", Notification.TYPE_WARNING_MESSAGE);
            descriptionTF.focus();
            return false;
        }
        if (taskPrefixTF != null && taskPrefix.length() > PROJECT_TASK_PREFIX_MAX_LENGTH) {
            parentWindow.showNotification(null, "Project Task ID Prefix is too long, maximum is " + PROJECT_TASK_PREFIX_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            descriptionTF.focus();
            return false;
        }
        ProjectHelper projectUtil = new ProjectHelper();
        if (!projectUtil.isUniqueName(project, name)) {
            parentWindow.showNotification(null, "Project Name is already in use", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
            return false;
        }
        return true;
    }
}

package com.agileapex.ui.common.release;

import org.apache.commons.lang.StringUtils;

import com.agileapex.common.release.ReleaseHelper;
import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class ReleaseDetailsValidator implements Constants {

    public boolean isValid(Window parentWindow, Project project, TextField nameTF, TextField descriptionTF) {
        return isValid(parentWindow, null, project, nameTF, descriptionTF);
    }

    public boolean isValid(Window parentWindow, Release currentRelease, Project project, TextField nameTF, TextField descriptionTF) {
        String name = StringUtils.trimToEmpty((String) nameTF.getValue());
        String description = StringUtils.trimToEmpty((String) descriptionTF.getValue());
        if (StringUtils.isEmpty(name) || name.length() < RELEASE_NAME_MIN_LENGTH || name.length() > RELEASE_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "Release Name length must be from " + RELEASE_NAME_MIN_LENGTH + " to " + RELEASE_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
            return false;
        }
        if (description.length() > RELEASE_DESCRIPTION_MAX_CHARACTERS) {
            parentWindow.showNotification(null, "Release Description is too long, maximum is " + RELEASE_DESCRIPTION_MAX_CHARACTERS + " characters", Notification.TYPE_WARNING_MESSAGE);
            descriptionTF.focus();
            return false;
        }
        ReleaseHelper releaseUtil = new ReleaseHelper();
        if (!releaseUtil.isUniqueName(currentRelease, project, name)) {
            parentWindow.showNotification(null, "Release Name is already in use", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
            return false;
        }
        return true;
    }
}

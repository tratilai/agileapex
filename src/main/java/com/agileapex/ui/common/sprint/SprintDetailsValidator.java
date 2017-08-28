package com.agileapex.ui.common.sprint;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.agileapex.common.sprint.SprintHelper;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class SprintDetailsValidator implements Constants {

    public boolean isValid(Window parentWindow, Release release, TextField nameTF, TextField descriptionTF, PopupDateField startDateDF, PopupDateField endDateDF) {
        return isValid(parentWindow, null, release, nameTF, descriptionTF, startDateDF, endDateDF);
    }

    public boolean isValid(Window parentWindow, Sprint currentSprint, Release release, TextField nameTF, TextField descriptionTF, PopupDateField startDateDF, PopupDateField endDateDF) {
        String name = StringUtils.trimToEmpty((String) nameTF.getValue());
        String description = StringUtils.trimToEmpty((String) descriptionTF.getValue());
        Date startDate = (startDateDF != null) ? ((Date) startDateDF.getValue()) : (null);
        Date endDate = (startDateDF != null) ? ((Date) endDateDF.getValue()) : (null);
        if (StringUtils.isEmpty(name) || name.length() < SPRINT_NAME_MIN_LENGTH || name.length() > SPRINT_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "Sprint Name length must be from " + SPRINT_NAME_MIN_LENGTH + " to " + SPRINT_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
            return false;
        }
        if (description.length() > SPRINT_DESCRIPTION_MAX_CHARACTERS) {
            parentWindow.showNotification(null, "Sprint Description is too long, maximum is " + SPRINT_DESCRIPTION_MAX_CHARACTERS + " characters", Notification.TYPE_WARNING_MESSAGE);
            descriptionTF.focus();
            return false;
        }
        if (startDate == null || endDate == null) {
            parentWindow.showNotification(null, "Valid Start and End Dates are mandatory", Notification.TYPE_WARNING_MESSAGE);
            return false;
        }
        DateTime startDateTime = new DateTime(startDate);
        DateTime endDateTime = new DateTime(endDate);
        if (endDateTime.isBefore(startDateTime)) {
            parentWindow.showNotification(null, "End Date can not be before Start Date", Notification.TYPE_WARNING_MESSAGE);
            return false;
        }
        if (startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate())) {
            parentWindow.showNotification(null, "Start and End Dates can not be the same date", Notification.TYPE_WARNING_MESSAGE);
            return false;
        }
        int daysInBetween = Days.daysBetween(startDateTime.withTimeAtStartOfDay(), endDateTime.withTimeAtStartOfDay()).getDays();
        if (daysInBetween > SPRINT_MAX_DAYS_BETWEEN_START_AND_END_DATE) {
            parentWindow.showNotification(null, "Maximum Sprint length is " + SPRINT_MAX_DAYS_BETWEEN_START_AND_END_DATE + " days", Notification.TYPE_WARNING_MESSAGE);
            return false;
        }
        SprintHelper sprintUtil = new SprintHelper();
        if (!sprintUtil.isUniqueName(currentSprint, release, name)) {
            parentWindow.showNotification(null, "Sprint Name is already in use", Notification.TYPE_WARNING_MESSAGE);
            nameTF.focus();
            return false;
        }
        return true;
    }
}

package com.agileapex.ui.common.user;

import org.apache.commons.lang.StringUtils;

import com.agileapex.common.CustomEmailValidator;
import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.User;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class UserDetailsValidator implements Constants {

    public boolean isValid(User user, Window parentWindow, TextField emailTF, TextField firstNameTF, TextField lastNameTF, boolean allowEmptyPasswords, final AbstractTextField passwordTF, final AbstractTextField confirmPasswordTF) {
        String email = StringUtils.trimToEmpty((String) emailTF.getValue());
        String firstName = StringUtils.trimToEmpty((String) firstNameTF.getValue());
        String lastName = StringUtils.trimToEmpty((String) lastNameTF.getValue());
        final String password = StringUtils.trimToEmpty((String) passwordTF.getValue());
        final String confirmPassword = StringUtils.trimToEmpty((String) confirmPasswordTF.getValue());
        
        if (StringUtils.isEmpty(email) || email.length() < EMAIL_MIN_LENGTH || email.length() > EMAIL_MAX_LENGTH || !CustomEmailValidator.isEmailValid(email)) {
            parentWindow.showNotification(null, "Invalid email address", Notification.TYPE_WARNING_MESSAGE);
            emailTF.focus();
            return false;
        }
        if (StringUtils.isEmpty(firstName) || firstName.length() < USER_FIRST_NAME_MIN_LENGTH || firstName.length() > USER_FIRST_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "First Name length must be from " + USER_FIRST_NAME_MIN_LENGTH + " to " + USER_FIRST_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            firstNameTF.focus();
            return false;
        }
        if (StringUtils.isEmpty(lastName) || lastName.length() < USER_LAST_NAME_MIN_LENGTH || lastName.length() > USER_LAST_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "Last Name length must be from " + USER_LAST_NAME_MIN_LENGTH + " to " + USER_LAST_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            lastNameTF.focus();
            return false;
        }
        if (!allowEmptyPasswords || !StringUtils.isEmpty(password) || !StringUtils.isEmpty(confirmPassword)) {
            if (!password.equals(confirmPassword)) {
                parentWindow.showNotification(null, "Passwords do not match.", Notification.TYPE_WARNING_MESSAGE);
                confirmPasswordTF.focus();
                return false;
            }
            if (StringUtils.isEmpty(password) || password.length() < USER_PASSWORD_MIN_LENGTH || password.length() > USER_PASSWORD_MAX_LENGTH) {
                parentWindow.showNotification(null, "Password length must be from " + USER_PASSWORD_MIN_LENGTH + " to " + USER_PASSWORD_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
                passwordTF.focus();
                return false;
            }
            if (StringUtils.isEmpty(confirmPassword) || confirmPassword.length() < USER_PASSWORD_MIN_LENGTH || confirmPassword.length() > USER_PASSWORD_MAX_LENGTH) {
                parentWindow.showNotification(null, "Password length must be from " + USER_PASSWORD_MIN_LENGTH + " to " + USER_PASSWORD_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
                confirmPasswordTF.focus();
                return false;
            }
        }
        UserHelper userUtil = new UserHelper();
        if (!userUtil.isUniqueUserName(user, email)) {
            parentWindow.showNotification(null, "Email is already in use", Notification.TYPE_WARNING_MESSAGE);
            emailTF.focus();
            return false;
        }
        return true;
    }
}

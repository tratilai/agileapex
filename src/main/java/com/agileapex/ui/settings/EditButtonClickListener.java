package com.agileapex.ui.settings;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.security.DigestHelper;
import com.agileapex.common.user.UserHelper;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.ui.common.user.UserDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class EditButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(EditButtonClickListener.class);
    private static final long serialVersionUID = -5409258696778021942L;
    private final UserDetailsLayout layout;

    public EditButtonClickListener(UserDetailsLayout layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Settings edit button clicked.");
        UserHelper userUtil = new UserHelper();
        if (userUtil.isRegisteredUser()) {
            if (!layout.emailTextField.isEnabled()) {
                enable();
            } else {
                disable();
            }
        } else {
            layout.getWindow().showNotification(null, "You must register your account first", Notification.TYPE_HUMANIZED_MESSAGE);            
        }
    }

    private void enable() {
        layout.emailTextField.setEnabled(true);
        layout.firstNameTextField.setEnabled(true);
        layout.lastNameTextField.setEnabled(true);
        layout.passwordTextField.setEnabled(true);
        layout.confirmPasswordTextField.setEnabled(true);
        layout.editButton.setCaption("Save");
        layout.cancelButton.setEnabled(true);
        layout.emailTextField.focus();
    }

    private void disable() {
        UserDetailsValidator validator = new UserDetailsValidator();
        if (validator.isValid(layout.user, layout.getWindow(), layout.emailTextField, layout.firstNameTextField, layout.lastNameTextField, true, layout.passwordTextField, layout.confirmPasswordTextField)) {
            layout.cancelButton.setEnabled(false);
            layout.user.setEmail(StringUtils.trimToEmpty((String) layout.emailTextField.getValue()));
            layout.user.setFirstName(StringUtils.trimToEmpty((String) layout.firstNameTextField.getValue()));
            layout.user.setLastName(StringUtils.trimToEmpty((String) layout.lastNameTextField.getValue()));
            final String password = StringUtils.trimToEmpty((String) layout.passwordTextField.getValue());
            final String confirmPassword = StringUtils.trimToEmpty((String) layout.confirmPasswordTextField.getValue());
            if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confirmPassword) && password.equals(confirmPassword)) {
                DigestHelper digestHelper = new DigestHelper();
                final byte[] passwordDigest = digestHelper.getDigest(password, layout.user.getPasswordSalt());
                layout.user.setPasswordDigest(passwordDigest);
            }
            UserPersistence userDbService = new UserPersistenceImpl();
            userDbService.update(layout.user);
            layout.clearPasswordFields();
            layout.disableEditButton();
            layout.getWindow().showNotification(null, "User details saved", Notification.TYPE_HUMANIZED_MESSAGE);
        }
    }
}

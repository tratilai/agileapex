package com.agileapex.ui.common.user.edit;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.security.DigestHelper;
import com.agileapex.domain.Authorization;
import com.agileapex.domain.User;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.ui.common.user.UserDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class SaveButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(SaveButtonClickListener.class);
    private static final long serialVersionUID = 5839952552344123998L;
    private final EditUserPopup layout;

    public SaveButtonClickListener(EditUserPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Save button clicked for edit user.");
        UserDetailsValidator validator = new UserDetailsValidator();
        if (validator.isValid(layout.user, layout.getWindow(), layout.userNameTextField, layout.firstNameTextField, layout.lastNameTextField, true, layout.passwordTextField, layout.confirmPasswordTextField)) {
            UserPersistence userDbService = new UserPersistenceImpl();
            boolean isUpdateOk = true;
            Authorization oldAuthorization = layout.user.getAuthorization();
            Authorization newAuthorization = (Authorization) layout.authorizationSelect.getValue();
            isUpdateOk = checkThatAtLeastOneAdminUserExists(userDbService, oldAuthorization, newAuthorization);
            if (isUpdateOk) {
                setDataFromPopupFields();
                userDbService.update(layout.user);
                logger.debug("User edited: {}", layout.user);
            }
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
        }
    }

    private boolean checkThatAtLeastOneAdminUserExists(UserPersistence userDbService, Authorization oldAuthorization, Authorization newAuthorization) {
        boolean isUpdateOk = true;
        if (oldAuthorization.equals(Authorization.ADMIN) && !newAuthorization.equals(Authorization.ADMIN)) {
            List<User> allUsers = userDbService.getAll();
            int adminUserCount = 0;
            for (User oneUser : allUsers) {
                if (oneUser.getAuthorization().equals(Authorization.ADMIN)) {
                    adminUserCount++;
                }
            }
            if (adminUserCount < 2) {
                isUpdateOk = false;
                logger.info("Can not remove admin authorization from the only admin user. User to edit: {}", layout.user);
                layout.parentLayout.getWindow().showNotification(null, "Can not remove admin authorization from the only admin user.", Notification.TYPE_ERROR_MESSAGE);
            }
        }
        return isUpdateOk;
    }

    private void setDataFromPopupFields() {
        String userName = StringUtils.trimToEmpty((String) layout.userNameTextField.getValue());
        String firstName = StringUtils.trimToEmpty((String) layout.firstNameTextField.getValue());
        String lastName = StringUtils.trimToEmpty((String) layout.lastNameTextField.getValue());
        Authorization authorization = (Authorization) layout.authorizationSelect.getValue();
        layout.user.setEmail(userName);
        layout.user.setFirstName(firstName);
        layout.user.setLastName(lastName);
        layout.user.setAuthorization(authorization);
        final String password = StringUtils.trimToEmpty((String) layout.passwordTextField.getValue());
        final String confirmPassword = StringUtils.trimToEmpty((String) layout.confirmPasswordTextField.getValue());
        if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confirmPassword) && password.equals(confirmPassword)) {
            DigestHelper digestHelper = new DigestHelper();
            final byte[] passwordDigest = digestHelper.getDigest(StringUtils.trimToEmpty((String) layout.passwordTextField.getValue()), layout.user.getPasswordSalt());
            layout.user.setPasswordDigest(passwordDigest);
        }
    }
}

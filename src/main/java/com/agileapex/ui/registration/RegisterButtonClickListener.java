package com.agileapex.ui.registration;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.CustomEmailValidator;
import com.agileapex.common.security.DigestHelper;
import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Authorization;
import com.agileapex.domain.Customer;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;
import com.agileapex.persistence.CustomerPersistence;
import com.agileapex.persistence.CustomerPersistenceImpl;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class RegisterButtonClickListener implements Button.ClickListener, Constants {
    private static final Logger logger = LoggerFactory.getLogger(RegisterButtonClickListener.class);
    private static final long serialVersionUID = 7185754107167115132L;
    private final RegistrationPopup layout;

    public RegisterButtonClickListener(RegistrationPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.info("Register button clicked.");
        layout.setEmailWarning(false);
        try {
            if (isValid(layout.getWindow(), layout.firstNameTextField, layout.lastNameTextField, layout.emailTextField, layout.password1Field, layout.password2Field)) {

                // TODO: this needs transaction

                User user = setDataFromPopupFields();
                user.setStatus(UserStatus.REGISTERED);
                UserPersistence userDbService = new UserPersistenceImpl();

                boolean readyToCreateAsCustomer = true;
                try {
                    userDbService.update(user);
                } catch (org.springframework.dao.DuplicateKeyException e) {
                    logger.info("User already registered. user: {}", user);
                    layout.setEmailWarning(true);
                    readyToCreateAsCustomer = false;
                } catch (Throwable t) {
                    logger.error("Some unknown error when trying to register. user: {}", user, t);
                    readyToCreateAsCustomer = false;
                }

                if (readyToCreateAsCustomer) {
                    DateTime registrationDate = new DateTime();
                    Customer customer = new Customer();
                    customer.setSchemaInternalId(user.getSchemaInternalId());
                    CustomerPersistence customerPersistence = new CustomerPersistenceImpl();
                    customerPersistence.create(customer, registrationDate);

                    logger.info("New user registered and created: {}", user);

                    ApplicationSession.setUser(null);
                    ApplicationSession.setSessionDataHelper(null);
                    layout.getApplication().close();
                }
            }
        } catch (Throwable t) {
            logger.error("Error when registering. ", t);
        }
    }

    private boolean isValid(Window parentWindow, TextField firstNameTF, TextField lastNameTF, TextField emailTF, PasswordField password1PF, PasswordField password2PF) {
        String firstName = StringUtils.trimToEmpty((String) firstNameTF.getValue());
        if (StringUtils.isEmpty(firstName) || firstName.length() < USER_FIRST_NAME_MIN_LENGTH) {
            parentWindow.showNotification(null, "First name is too short. Minimum length is " + USER_FIRST_NAME_MIN_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            firstNameTF.focus();
            return false;
        }
        if (firstName.length() > USER_FIRST_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "First name is too long. Maximum length is " + USER_FIRST_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            firstNameTF.focus();
            return false;
        }

        String lastName = StringUtils.trimToEmpty((String) lastNameTF.getValue());
        if (StringUtils.isEmpty(lastName) || lastName.length() < USER_LAST_NAME_MIN_LENGTH) {
            parentWindow.showNotification(null, "Last name is too short. Minimum length is " + USER_LAST_NAME_MIN_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            lastNameTF.focus();
            return false;
        }
        if (lastName.length() > USER_LAST_NAME_MAX_LENGTH) {
            parentWindow.showNotification(null, "Last name is too long. Maximum length is " + USER_LAST_NAME_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            lastNameTF.focus();
            return false;
        }

        String email = StringUtils.trimToEmpty((String) emailTF.getValue());
        if (StringUtils.isEmpty(email) || email.length() < EMAIL_MIN_LENGTH || email.length() > EMAIL_MAX_LENGTH || !CustomEmailValidator.isEmailValid(email)) {
            parentWindow.showNotification(null, "Invalid email address", Notification.TYPE_WARNING_MESSAGE);
            emailTF.focus();
            return false;
        }
        UserHelper userUtil = new UserHelper();
        if (!userUtil.isUniqueUserName(null, email)) {
            parentWindow.showNotification(null, "Email is already in use", Notification.TYPE_WARNING_MESSAGE);
            emailTF.focus();
            return false;
        }

        String password1 = StringUtils.trimToEmpty((String) password1PF.getValue());
        String password2 = StringUtils.trimToEmpty((String) password2PF.getValue());
        if (StringUtils.isEmpty(password1) || password1.length() < USER_PASSWORD_MIN_LENGTH) {
            parentWindow.showNotification(null, "Password is too short. Minimum length is " + USER_PASSWORD_MIN_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            password1PF.focus();
            return false;
        }
        if (StringUtils.isEmpty(password2) || password2.length() < USER_PASSWORD_MIN_LENGTH) {
            parentWindow.showNotification(null, "Password is too short. Minimum length is " + USER_PASSWORD_MIN_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            password2PF.focus();
            return false;
        }

        if (password1.length() > USER_PASSWORD_MAX_LENGTH) {
            parentWindow.showNotification(null, "Password is too long. Maximum length is " + USER_PASSWORD_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            password1PF.focus();
            return false;
        }
        if (password2.length() > USER_PASSWORD_MAX_LENGTH) {
            parentWindow.showNotification(null, "Password is too long. Maximum length is " + USER_PASSWORD_MAX_LENGTH + " characters", Notification.TYPE_WARNING_MESSAGE);
            password2PF.focus();
            return false;
        }

        if (!password1.equals(password2)) {
            parentWindow.showNotification(null, "Passwords does not match", Notification.TYPE_WARNING_MESSAGE);
            password1PF.focus();
            return false;
        }

        return true;
    }

    private User setDataFromPopupFields() {
        String email = StringUtils.trimToEmpty((String) layout.emailTextField.getValue());
        String firstName = StringUtils.trimToEmpty((String) layout.firstNameTextField.getValue());
        String lastName = StringUtils.trimToEmpty((String) layout.lastNameTextField.getValue());
        DigestHelper digestHelper = new DigestHelper();
        final byte[] passwordSalt = digestHelper.getNewSalt();
        final byte[] passwordDigest = digestHelper.getDigest(StringUtils.trimToEmpty((String) layout.password1Field.getValue()), passwordSalt);
        Authorization authorization = Authorization.ADMIN;
        User oldUser = ApplicationSession.getUser();
        User user = new User(oldUser.getUniqueId(), email, passwordDigest, passwordSalt, firstName, lastName, UserStatus.REGISTERED, authorization, oldUser.getSchemaInternalId(), oldUser.getSchemaPublicId());
        logger.debug("About to create user: {}", user);
        return user;
    }
}

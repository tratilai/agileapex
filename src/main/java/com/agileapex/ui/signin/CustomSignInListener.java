package com.agileapex.ui.signin;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.security.DigestHelper;
import com.agileapex.domain.User;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.session.SessionDataHelper;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.window.MainWindow;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.Notification;

public class CustomSignInListener implements Button.ClickListener, Constants {
    private static final Logger logger = LoggerFactory.getLogger(CustomSignInListener.class);
    private static final long serialVersionUID = 1713832550257679397L;
    private final MainWindow mainWindow;
    transient private TextField userNameField;
    transient private PasswordField passwordField;

    public CustomSignInListener(MainWindow mainWindow, TextField userNameField, PasswordField passwordField) {
        this.mainWindow = mainWindow;
        this.userNameField = userNameField;
        this.passwordField = passwordField;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        try {
            logger.info("Sign in button clicked.");
            mainWindow.getApplication().setUser(null);
            String givenEmail = (String) userNameField.getValue();
            String givenPassword = (String) passwordField.getValue();
            int userNameLength = givenEmail.length();
            int passwordLenght = givenPassword.length();
            if (userNameLength >= Constants.EMAIL_MIN_LENGTH && userNameLength <= Constants.EMAIL_MAX_LENGTH && passwordLenght >= Constants.USER_PASSWORD_MIN_LENGTH && passwordLenght <= Constants.USER_PASSWORD_MAX_LENGTH) {
                UserPersistence userDbService = new UserPersistenceImpl();
                User user = userDbService.get(givenEmail);
                checkAuthentication(givenEmail, givenPassword, user);
            } else {
                mainWindow.showNotification("Wrong email or password.", Notification.TYPE_TRAY_NOTIFICATION);
            }
        } catch (Throwable t) {
            logger.error("Error in sign in.", t);
            mainWindow.showNotification("System error in sign in. Please try again later.", Notification.TYPE_ERROR_MESSAGE);
        }
    }

    private synchronized void checkAuthentication(String givenEmail, String givenPassword, User user) {
        boolean authorizationOk = false;
        if (user != null && user.getEmail() != null) {
            UserPersistence userDbService = new UserPersistenceImpl();
            user.setLastSignInDate(new DateTime());
            userDbService.update(user);
            logger.debug("User (id={}) last sign in date saved: {}", user.getUniqueId(), user.getLastSignInDate());

            DigestHelper digestHelper = new DigestHelper();
            if (user.getEmail().equals(givenEmail)) {
                if (digestHelper.authenticate(givenPassword, user.getPasswordDigest(), user.getPasswordSalt())) {
                    logger.info("Authentication ok");
                    authorizationOk = true;
                } else {
                    logger.info("Authentication failed: invalid password");
                }
            } else {
                logger.info("Authentication failed: invalid email");
            }
        }

        if (authorizationOk) {
            ApplicationSession.setUser(user);
            ApplicationSession.setSessionDataHelper(new SessionDataHelper());
            ApplicationSession.getSessionDataHelper().setTargetPage(WindowIdentification.PROJECTS);
            logger.info("User \"{}\" signed in.", user.getEmail());

            logger.info("Sleeping in order to make service overloading attack harder...");
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                logger.error("Sleep failed", e);
            }
            logger.info("Sleeping done");

            mainWindow.changePage();
        } else {
            mainWindow.showNotification("Wrong username or password.", Notification.TYPE_TRAY_NOTIFICATION);
        }
    }
}

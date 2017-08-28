package com.agileapex.ui.signin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.vaadin.RemoveButtonBlurListener;
import com.agileapex.ui.common.vaadin.SetButtonFocusListener;
import com.agileapex.ui.window.MainWindow;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SignInPanel extends Panel implements Constants {
    private static final Logger logger = LoggerFactory.getLogger(SignInPanel.class);
    private static final long serialVersionUID = -2487479093798992208L;
    private TextField userNameField;
    transient private PasswordField passwordField;
    transient private Button signInButton;

    public SignInPanel(final MainWindow mainWindow) {
        logger.info("SignInPanel");

        setSizeFull();

        userNameField = new TextField("Email");
        userNameField.setDebugId(SIGN_IN_PAGE_USERNAME);
        userNameField.setTabIndex(1);
        userNameField.setWidth("250px");
        userNameField.setMaxLength(Constants.EMAIL_MAX_LENGTH);

        passwordField = new PasswordField("Password");
        passwordField.setDebugId(SIGN_IN_PAGE_PASSWORD);
        passwordField.setTabIndex(2);
        passwordField.setWidth("250px");
        passwordField.setValue("");
        passwordField.setNullRepresentation("");
        passwordField.setMaxLength(Constants.USER_PASSWORD_MAX_LENGTH);

        signInButton = new Button("Sign in");
        signInButton.setDebugId(SIGN_IN_PAGE_BUTTON);
        signInButton.setTabIndex(3);
        signInButton.addListener(new CustomSignInListener(mainWindow, userNameField, passwordField));

        VerticalLayout panelsInnerLayout = new VerticalLayout();
        panelsInnerLayout.setSpacing(true);
        panelsInnerLayout.setMargin(true);
        Panel fieldsInPanel = new Panel();
        fieldsInPanel.setCaption("Agile Apex - Sign in");
        fieldsInPanel.setContent(panelsInnerLayout);
        fieldsInPanel.setSizeUndefined();
        fieldsInPanel.addComponent(userNameField);
        fieldsInPanel.addComponent(passwordField);
        fieldsInPanel.addComponent(signInButton);
        panelsInnerLayout.setComponentAlignment(signInButton, Alignment.MIDDLE_RIGHT);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(fieldsInPanel);
        layout.setSizeFull();
        layout.setComponentAlignment(fieldsInPanel, Alignment.MIDDLE_CENTER);

        addComponent(layout);
    }

    @Override
    public void attach() {
        userNameField.addListener(new SetButtonFocusListener(signInButton));
        userNameField.addListener(new RemoveButtonBlurListener(signInButton));
        passwordField.addListener(new SetButtonFocusListener(signInButton));
        passwordField.addListener(new RemoveButtonBlurListener(signInButton));
        userNameField.focus();
    }
}

package com.agileapex.ui.settings;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.User;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class UserDetailsLayout extends GridLayout implements Constants {
    private static final long serialVersionUID = -423121023136329379L;
    protected User user;
    protected TextField firstNameTextField;
    protected TextField lastNameTextField;
    protected TextField emailTextField;
    protected Button editButton;
    protected Button cancelButton;
    protected PasswordField passwordTextField;
    protected PasswordField confirmPasswordTextField;

    public UserDetailsLayout() {
        super(3, 6);
        this.user = ApplicationSession.getUser();
        createLayout();
        createUserDetails();
    }

    private void createLayout() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);
        setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_FIVE, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_SIX, EXPAND_RATIO_1000);
        setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1);
        setColumnExpandRatio(COLUMN_TWO, EXPAND_RATIO_1);
        setColumnExpandRatio(COLUMN_THREE, EXPAND_RATIO_2);
    }

    private void createUserDetails() {
        addUsername();
        addFirstName();
        addLastName();
        addPasswordFields();
        addButtons();
    }

    private void addUsername() {
        Label emailLabel = new Label("Email (used for sign in)");
        addComponent(emailLabel, COLUMN_ONE, ROW_ONE);
        emailTextField = new TextField();
        emailTextField.setMaxLength(Constants.EMAIL_MAX_LENGTH);
        emailTextField.setWidth(THE_40_PERCENT, UNITS_PERCENTAGE);
        UserHelper userUtil = new UserHelper();
        if (userUtil.isRegisteredUser()) {
            emailTextField.setValue(user.getEmail());
        }
        emailTextField.setEnabled(false);
        emailTextField.setTabIndex(1);
        addComponent(emailTextField, COLUMN_TWO, ROW_ONE, COLUMN_THREE, ROW_ONE);
    }

    private void addFirstName() {
        Label firstNameLabel = new Label("First Name");
        addComponent(firstNameLabel, COLUMN_ONE, ROW_TWO);
        firstNameTextField = new TextField();
        firstNameTextField.setMaxLength(Constants.USER_FIRST_NAME_MAX_LENGTH);
        firstNameTextField.setWidth(THE_50_PERCENT, UNITS_PERCENTAGE);
        firstNameTextField.setValue(user.getFirstName());
        firstNameTextField.setEnabled(false);
        firstNameTextField.setTabIndex(2);
        addComponent(firstNameTextField, COLUMN_TWO, ROW_TWO, COLUMN_THREE, ROW_TWO);
    }

    private void addLastName() {
        Label lastNameLabel = new Label("Last Name");
        addComponent(lastNameLabel, COLUMN_ONE, ROW_THREE);
        lastNameTextField = new TextField();
        lastNameTextField.setMaxLength(Constants.USER_LAST_NAME_MAX_LENGTH);
        lastNameTextField.setWidth(THE_50_PERCENT, UNITS_PERCENTAGE);
        lastNameTextField.setValue(user.getLastName());
        lastNameTextField.setEnabled(false);
        lastNameTextField.setTabIndex(3);
        addComponent(lastNameTextField, COLUMN_TWO, ROW_THREE, COLUMN_THREE, ROW_THREE);
    }

    private void addPasswordFields() {
        addPassword();
        addPasswordConfirmation();
    }

    private void addPassword() {
        Label passwordLabel = new Label("New Password");
        addComponent(passwordLabel, COLUMN_ONE, ROW_FOUR);
        passwordTextField = new PasswordField();
        passwordTextField.setEnabled(false);
        passwordTextField.setMaxLength(USER_PASSWORD_MAX_LENGTH);
        passwordTextField.setWidth(THE_40_PERCENT, UNITS_PERCENTAGE);
        passwordTextField.setValue("");
        passwordTextField.setTabIndex(4);
        addComponent(passwordTextField, COLUMN_TWO, ROW_FOUR, COLUMN_THREE, ROW_FOUR);
    }

    private void addPasswordConfirmation() {
        Label confirmPasswordLabel = new Label("Confirm Password");
        addComponent(confirmPasswordLabel, COLUMN_ONE, ROW_FIVE);
        confirmPasswordTextField = new PasswordField();
        confirmPasswordTextField.setEnabled(false);
        confirmPasswordTextField.setMaxLength(USER_PASSWORD_MAX_LENGTH);
        confirmPasswordTextField.setWidth(THE_40_PERCENT, UNITS_PERCENTAGE);
        confirmPasswordTextField.setValue("");
        confirmPasswordTextField.setTabIndex(5);
        addComponent(confirmPasswordTextField, COLUMN_TWO, ROW_FIVE, COLUMN_THREE, ROW_FIVE);
    }

    private void addButtons() {
        HorizontalLayout buttonsLayout = createButtonsInLayout();
        addComponent(buttonsLayout, COLUMN_ONE, ROW_SIX, COLUMN_THREE, ROW_SIX);
        addEditButton(buttonsLayout);
        addCancelButton(buttonsLayout);
        addExpander(buttonsLayout);
    }

    private HorizontalLayout createButtonsInLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        layout.setMargin(false);
        layout.setSpacing(true);
        return layout;
    }

    private void addEditButton(HorizontalLayout layout) {
        editButton = new Button("Edit", new EditButtonClickListener(this));
        editButton.setClickShortcut(KeyCode.ENTER);
        editButton.addStyleName("primary");
        editButton.setTabIndex(6);
        layout.addComponent(editButton);
        layout.setComponentAlignment(editButton, Alignment.TOP_LEFT);
        layout.setExpandRatio(editButton, EXPAND_RATIO_1);
    }

    private void addCancelButton(HorizontalLayout layout) {
        cancelButton = new Button("Cancel", new CancelButtonClickListener(this));
        cancelButton.setTabIndex(7);
        cancelButton.setEnabled(false);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.TOP_LEFT);
        layout.setExpandRatio(cancelButton, EXPAND_RATIO_1);
    }

    private void addExpander(HorizontalLayout layout) {
        Label emptyExpander = new Label();
        layout.addComponent(emptyExpander);
        layout.setComponentAlignment(emptyExpander, Alignment.TOP_LEFT);
        layout.setExpandRatio(emptyExpander, EXPAND_RATIO_1000);
    }

    protected void clearPasswordFields() {
        passwordTextField.setValue("");
        confirmPasswordTextField.setValue("");
    }

    protected void disableEditButton() {
        emailTextField.setEnabled(false);
        firstNameTextField.setEnabled(false);
        lastNameTextField.setEnabled(false);
        passwordTextField.setEnabled(false);
        confirmPasswordTextField.setEnabled(false);
        editButton.setCaption("Edit");
        cancelButton.setEnabled(false);
    }
}

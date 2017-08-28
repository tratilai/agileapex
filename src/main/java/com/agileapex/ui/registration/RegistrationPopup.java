package com.agileapex.ui.registration;

import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.vaadin.RemoveButtonBlurListener;
import com.agileapex.ui.common.vaadin.SetButtonFocusListener;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class RegistrationPopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = -8319522207808284752L;
    protected AbstractLayout parentLayout;
    protected GridLayout gridLayout;
    protected TextField firstNameTextField;
    protected TextField lastNameTextField;
    protected TextField emailTextField;
    protected PasswordField password1Field;
    protected PasswordField password2Field;
    protected Label infoText;
    protected Button registerButton;
    protected Button cancelButton;
    private Action cancelAction;
    private Label errorText;

    public RegistrationPopup(AbstractLayout parentLayout) {
        super("Registration");
        setDebugId(REGISTRATION_POPUP);
        this.parentLayout = parentLayout;
        setLayoutBasics();
        addFirstName();
        addLastName();
        addEmail();
        addPassword1();
        addPassword2();
        addInfoText();
        addButtons();
        addErrorTextField();
        setContent(gridLayout);
        addShortcutActions();
    }

    private void setLayoutBasics() {
        setModal(true);
        setWidth(POPUP_DEFAULT_WINDOW_WIDTH, UNITS_PIXELS);
        setHeight(POPUP_DEFAULT_WINDOW_HEIGHT, UNITS_PIXELS);
        gridLayout = new GridLayout(2, 8);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);
        gridLayout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_FIVE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_SIX, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_SEVEN, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_EIGHT, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_TWO, EXPAND_RATIO_1000);
    }

    private void addFirstName() {
        firstNameTextField = new TextField("First name");
        firstNameTextField.setDebugId(CREATE_NEW_PROJECT_POPUP_NAME);
        firstNameTextField.setMaxLength(USER_FIRST_NAME_MAX_LENGTH);
        firstNameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        firstNameTextField.setTabIndex(1);
        gridLayout.addComponent(firstNameTextField, COLUMN_ONE, ROW_ONE, COLUMN_TWO, ROW_ONE);
    }

    private void addLastName() {
        lastNameTextField = new TextField("Last name");
        lastNameTextField.setDebugId(CREATE_NEW_PROJECT_POPUP_NAME);
        lastNameTextField.setMaxLength(USER_LAST_NAME_MAX_LENGTH);
        lastNameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        lastNameTextField.setTabIndex(2);
        gridLayout.addComponent(lastNameTextField, COLUMN_ONE, ROW_TWO, COLUMN_TWO, ROW_TWO);
    }

    private void addEmail() {
        emailTextField = new TextField("Email (used for sign in)");
        emailTextField.setDebugId(CREATE_NEW_PROJECT_POPUP_NAME);
        emailTextField.setMaxLength(EMAIL_MAX_LENGTH);
        emailTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        emailTextField.setTabIndex(3);
        gridLayout.addComponent(emailTextField, COLUMN_ONE, ROW_THREE, COLUMN_TWO, ROW_THREE);
    }

    private void addPassword1() {
        password1Field = new PasswordField("Password");
        password1Field.setDebugId(CREATE_NEW_PROJECT_POPUP_NAME);
        password1Field.setMaxLength(USER_PASSWORD_MAX_LENGTH);
        password1Field.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        password1Field.setTabIndex(4);
        gridLayout.addComponent(password1Field, COLUMN_ONE, ROW_FOUR, COLUMN_TWO, ROW_FOUR);
    }

    private void addPassword2() {
        password2Field = new PasswordField("Repeat password");
        password2Field.setDebugId(CREATE_NEW_PROJECT_POPUP_NAME);
        password2Field.setMaxLength(USER_PASSWORD_MAX_LENGTH);
        password2Field.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        password2Field.setTabIndex(5);
        gridLayout.addComponent(password2Field, COLUMN_ONE, ROW_FIVE, COLUMN_TWO, ROW_FIVE);
    }

    private void addInfoText() {
        infoText = new Label("After successfull registration you must sign in again");
        infoText.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        gridLayout.addComponent(infoText, COLUMN_ONE, ROW_SIX, COLUMN_TWO, ROW_SIX);
    }
    
    private void addErrorTextField() {
        errorText = new Label("<font color='red'>Registration failed. Email address already in use.</font>", Label.CONTENT_XHTML);
        errorText.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        gridLayout.addComponent(errorText, COLUMN_ONE, ROW_SEVEN, COLUMN_TWO, ROW_SEVEN);        
        errorText.setVisible(false);            
    }

    public void setEmailWarning(boolean showIt) {
        if (showIt) {
            errorText.setVisible(true);
        } else {
            errorText.setVisible(false);            
        }
    }
    
    private void addButtons() {
        gridLayout.addComponent(createButtonsInLayout(), COLUMN_ONE, ROW_EIGHT, COLUMN_TWO, ROW_EIGHT);
    }

    private void addShortcutActions() {
        cancelAction = new ShortcutAction("", ShortcutAction.KeyCode.ESCAPE, null);
        addActionHandler(this);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[] { cancelAction };
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == cancelAction) {
            cancelButton.click();
        }
    }

    @Override
    public void attach() {
        firstNameTextField.addListener(new SetButtonFocusListener(registerButton));
        firstNameTextField.addListener(new RemoveButtonBlurListener(registerButton));
        lastNameTextField.addListener(new SetButtonFocusListener(registerButton));
        lastNameTextField.addListener(new RemoveButtonBlurListener(registerButton));
        firstNameTextField.focus();
    }

    private HorizontalLayout createButtonsInLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        layout.setMargin(false);
        layout.setSpacing(true);
        addCreateButton(layout);
        addCancelButton(layout);
        return layout;
    }

    private void addCreateButton(HorizontalLayout layout) {
        registerButton = new Button("Register", new RegisterButtonClickListener(this));
        registerButton.setDebugId(REGISTRATION_POPUP_REGISTER);
        registerButton.setTabIndex(6);
        registerButton.addStyleName("primary");
        layout.addComponent(registerButton);
        layout.setComponentAlignment(registerButton, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(registerButton, EXPAND_RATIO_1000);
    }

    private void addCancelButton(HorizontalLayout layout) {
        cancelButton = new Button("Cancel", new CancelButtonClickListener(this));
        cancelButton.setDebugId(REGISTRATION_POPUP_CANCEL);
        cancelButton.setTabIndex(7);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
}

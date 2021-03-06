package com.agileapex.ui.common.user.create;

import java.util.Arrays;
import java.util.List;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Authorization;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class CreateNewUserPopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = 254271704833626538L;
    protected RefreshableComponent parentLayout;
    protected GridLayout gridLayout;
    protected TextField userNameTextField;
    protected TextField firstNameTextField;
    protected TextField lastNameTextField;
    protected PasswordField passwordTextField;
    protected PasswordField confirmPasswordTextField;
    protected OptionGroup authorizationSelect;
    protected TextField infoTextField;
    private Button closeButton;
    private Button createButton;
    private Action cancelAction;
    private ShortcutAction enterAction;

    public CreateNewUserPopup(RefreshableComponent parentLayout) {
        super("Create New User");
        this.parentLayout = parentLayout;
        setLayoutBasics();
        addUsername();
        addFirstName();
        addLastName();
        addPassword();
        addAuthorization();
        addAuthorizationDescription();
        addButtons();
        setContent(gridLayout);
        addShortcutActions();
    }

    private void setLayoutBasics() {
        setModal(true);
        setWidth(POPUP_USER_WINDOW_WIDTH, UNITS_PIXELS);
        setHeight(POPUP_USER_WINDOW_HEIGHT, UNITS_PIXELS);
        gridLayout = new GridLayout(2, 7);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);
        gridLayout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_FIVE, EXPAND_RATIO_1000);
        gridLayout.setRowExpandRatio(ROW_SIX, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_SEVEN, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_TWO, EXPAND_RATIO_4);
    }

    private void addUsername() {
        userNameTextField = new TextField("Email");
        userNameTextField.setDebugId(CREATE_NEW_USER_POPUP_USERNAME);
        userNameTextField.setMaxLength(EMAIL_MAX_LENGTH);
        userNameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        userNameTextField.setTabIndex(1);
        gridLayout.addComponent(userNameTextField, COLUMN_ONE, ROW_ONE, COLUMN_TWO, ROW_ONE);
    }

    private void addFirstName() {
        firstNameTextField = new TextField("First Name");
        firstNameTextField.setDebugId(CREATE_NEW_USER_POPUP_FIRSTNAME);
        firstNameTextField.setMaxLength(USER_FIRST_NAME_MAX_LENGTH);
        firstNameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        firstNameTextField.setTabIndex(2);
        gridLayout.addComponent(firstNameTextField, COLUMN_ONE, ROW_TWO, COLUMN_TWO, ROW_TWO);
    }

    private void addLastName() {
        lastNameTextField = new TextField("Last Name");
        lastNameTextField.setDebugId(CREATE_NEW_USER_POPUP_LASTNAME);
        lastNameTextField.setMaxLength(USER_LAST_NAME_MAX_LENGTH);
        lastNameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        lastNameTextField.setTabIndex(3);
        gridLayout.addComponent(lastNameTextField, COLUMN_ONE, ROW_THREE, COLUMN_TWO, ROW_THREE);
    }

    private void addPassword() {
        passwordTextField = new PasswordField("Password");
        passwordTextField.setDebugId(CREATE_NEW_USER_POPUP_PASSWORD1);
        passwordTextField.setMaxLength(USER_PASSWORD_MAX_LENGTH);
        passwordTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        passwordTextField.setTabIndex(4);
        gridLayout.addComponent(passwordTextField, COLUMN_ONE, ROW_FOUR, COLUMN_TWO, ROW_FOUR);
        confirmPasswordTextField = new PasswordField("Confirm password");
        confirmPasswordTextField.setDebugId(CREATE_NEW_USER_POPUP_PASSWORD2);
        confirmPasswordTextField.setMaxLength(USER_PASSWORD_MAX_LENGTH);
        confirmPasswordTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        confirmPasswordTextField.setTabIndex(5);
        gridLayout.addComponent(confirmPasswordTextField, COLUMN_ONE, ROW_FIVE, COLUMN_TWO, ROW_FIVE);
    }

    private void addAuthorization() {
        authorizationSelect = new OptionGroup("Authorization");
        authorizationSelect.setDebugId(CREATE_NEW_USER_POPUP_AUTHORIZATION);
        BeanItemContainer<Authorization> container = new BeanItemContainer<Authorization>(Authorization.class);
        List<Authorization> authorizations = Arrays.asList(Authorization.values());
        container.addAll(authorizations);
        authorizationSelect.setContainerDataSource(container);
        authorizationSelect.setNullSelectionAllowed(false);
        authorizationSelect.setImmediate(true);
        authorizationSelect.setTabIndex(6);
        authorizationSelect.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        authorizationSelect.setItemCaptionPropertyId("name");
        gridLayout.addComponent(authorizationSelect, COLUMN_ONE, ROW_SIX);
        authorizationSelect.addListener(new ValueChangeListener() {
            private static final long serialVersionUID = -3846046569823924893L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Authorization authorization = (Authorization) authorizationSelect.getValue();
                infoTextField.setReadOnly(false);
                UserHelper userUtil = new UserHelper();
                infoTextField.setValue(userUtil.setAuthorizationDescriptionText(authorization));
                infoTextField.setReadOnly(true);
            }
        });
    }

    private void addAuthorizationDescription() {
        infoTextField = new TextField("Authorization Description");
        infoTextField.setDebugId(CREATE_NEW_USER_POPUP_AUTHORIZATION_DESCRIPTION);
        infoTextField.setSizeFull();
        infoTextField.setReadOnly(true);
        gridLayout.addComponent(infoTextField, COLUMN_TWO, ROW_SIX);
    }

    private void addButtons() {
        gridLayout.addComponent(createButtonsInLayout(), COLUMN_ONE, ROW_SEVEN, COLUMN_TWO, ROW_SEVEN);
    }

    private void addShortcutActions() {
        cancelAction = new ShortcutAction("", ShortcutAction.KeyCode.ESCAPE, null);
        enterAction = new ShortcutAction("", ShortcutAction.KeyCode.ENTER, null);
        addActionHandler(this);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[] { cancelAction, enterAction };
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == cancelAction) {
            closeButton.click();
        }
        if (action == enterAction) {
            createButton.click();
        }
    }

    @Override
    public void attach() {
        authorizationSelect.select(Authorization.VIEWER);
        userNameTextField.focus();
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
        createButton = new Button("Create", new CreateButtonClickListener(this));
        createButton.setDebugId(CREATE_NEW_USER_POPUP_CREATE);
        createButton.setClickShortcut(KeyCode.ENTER);
        createButton.addStyleName("primary");
        createButton.setTabIndex(7);
        layout.addComponent(createButton);
        layout.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(createButton, EXPAND_RATIO_1000);
    }

    private void addCancelButton(HorizontalLayout layout) {
        closeButton = new Button("Cancel", new CancelButtonClickListener(this));
        closeButton.setDebugId(CREATE_NEW_USER_POPUP_CANCEL);
        closeButton.setTabIndex(8);
        layout.addComponent(closeButton);
        layout.setComponentAlignment(closeButton, Alignment.MIDDLE_RIGHT);
    }
}

package com.agileapex.ui.common.project.create;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.ProjectStatus;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.vaadin.RemoveButtonBlurListener;
import com.agileapex.ui.common.vaadin.SetButtonFocusListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class CreateNewProjectPopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = -8569368957602293185L;
    protected RefreshableComponent parentLayout;
    protected GridLayout gridLayout;
    protected TextField nameTextField;
    protected TextField descriptionTextField;
    protected NativeSelect statusSelect;
    protected TextField taskPrefixTextField;
    protected Button createButton;
    protected Button cancelButton;
    private Action cancelAction;

    public CreateNewProjectPopup(RefreshableComponent parentLayout) {
        super("Create New Project");
        setDebugId(CREATE_NEW_PROJECT_POPUP);
        this.parentLayout = parentLayout;
        setLayoutBasics();
        addName();
        addDescription();
        addStatusSelect();
        addTaskPrefix();
        addButtons();
        setContent(gridLayout);
        addShortcutActions();
    }

    private void setLayoutBasics() {
        setModal(true);
        setWidth(POPUP_DEFAULT_WINDOW_WIDTH, UNITS_PIXELS);
        setHeight(POPUP_DEFAULT_WINDOW_HEIGHT, UNITS_PIXELS);
        gridLayout = new GridLayout(2, 4);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);
        gridLayout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1000);
        gridLayout.setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_TWO, EXPAND_RATIO_1000);
    }

    private void addName() {
        nameTextField = new TextField("Name");
        nameTextField.setDebugId(CREATE_NEW_PROJECT_POPUP_NAME);
        nameTextField.setMaxLength(PROJECT_NAME_MAX_LENGTH);
        nameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        nameTextField.setTabIndex(1);
        gridLayout.addComponent(nameTextField, COLUMN_ONE, ROW_ONE, COLUMN_TWO, ROW_ONE);
    }

    private void addDescription() {
        descriptionTextField = new TextField("Description");
        descriptionTextField.setDebugId(CREATE_NEW_PROJECT_POPUP_DESCRIPTION);
        descriptionTextField.setMaxLength(PROJECT_DESCRIPTION_MAX_CHARACTERS);
        descriptionTextField.setSizeFull();
        descriptionTextField.setTabIndex(2);
        gridLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_TWO, COLUMN_TWO, ROW_TWO);
    }

    private void addStatusSelect() {
        BeanItemContainer<ProjectStatus> container = new BeanItemContainer<ProjectStatus>(ProjectStatus.class);
        container.addAll(ProjectStatus.getStatuses());
        statusSelect = new NativeSelect("Status", container);
        statusSelect.setDebugId(CREATE_NEW_PROJECT_POPUP_STATUS);
        statusSelect.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        statusSelect.setItemCaptionPropertyId("status");
        statusSelect.setNullSelectionAllowed(false);
        statusSelect.select(ProjectStatus.OPEN);
        statusSelect.setImmediate(true);
        statusSelect.setTabIndex(3);
        gridLayout.addComponent(statusSelect, COLUMN_ONE, ROW_THREE);
    }

    private void addTaskPrefix() {
        taskPrefixTextField = new TextField("Task Id's Prefix");
        taskPrefixTextField.setDebugId(CREATE_NEW_PROJECT_POPUP_PREFIX);
        taskPrefixTextField.setMaxLength(PROJECT_TASK_PREFIX_MAX_LENGTH);
        taskPrefixTextField.setTabIndex(4);
        gridLayout.addComponent(taskPrefixTextField, COLUMN_TWO, ROW_THREE);
    }

    private void addButtons() {
        gridLayout.addComponent(createButtonsInLayout(), COLUMN_ONE, ROW_FOUR, COLUMN_TWO, ROW_FOUR);
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
        nameTextField.addListener(new SetButtonFocusListener(createButton));
        nameTextField.addListener(new RemoveButtonBlurListener(createButton));
        taskPrefixTextField.addListener(new SetButtonFocusListener(createButton));
        taskPrefixTextField.addListener(new RemoveButtonBlurListener(createButton));
        
        UserHelper userUtil = new UserHelper();
        if (!userUtil.hasManagerPrivileges()) {
            nameTextField.setReadOnly(true);
            descriptionTextField.setReadOnly(true);
            statusSelect.setReadOnly(true);
            taskPrefixTextField.setReadOnly(true);
            createButton.setEnabled(false);
            cancelButton.setReadOnly(false);
        }
        nameTextField.focus();
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
        createButton.setDebugId(CREATE_NEW_PROJECT_POPUP_CREATE);
        createButton.setTabIndex(5);
        createButton.addStyleName("primary");
        layout.addComponent(createButton);
        layout.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(createButton, EXPAND_RATIO_1000);
    }

    private void addCancelButton(HorizontalLayout layout) {
        cancelButton = new Button("Cancel", new CancelButtonClickListener(this));
        cancelButton.setDebugId(CREATE_NEW_PROJECT_POPUP_CANCEL);
        cancelButton.setTabIndex(6);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
}

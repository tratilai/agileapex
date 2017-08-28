package com.agileapex.ui.common.task.create;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Project;
import com.agileapex.domain.Task;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.treetable.TaskTreeTable;
import com.agileapex.ui.common.vaadin.RemoveButtonBlurListener;
import com.agileapex.ui.common.vaadin.SetButtonFocusListener;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class CreateNewTaskPopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = -6345282370006485674L;
    protected GridLayout gridLayout;
    protected Task parentTask;
    protected Project project;
    protected TextField nameTextField;
    protected TextField descriptionTextField;
    protected TextField effortTextField;
    protected TaskTreeTable treeTable;
    protected Button createMoreButton;
    protected Button createButton;
    protected Button cancelButton;
    private Action cancelAction;

    public CreateNewTaskPopup(TaskTreeTable treeTable, Task parentTask, Project project) {
        super("Create New Task");
        this.treeTable = treeTable;
        this.parentTask = parentTask;
        this.project = project;
        setLayoutBasics();
        addName();
        addDescription();
        addEffort();
        addButtons();
        setContent(gridLayout);
        addShortcutActions();
    }

    private void setLayoutBasics() {
        setModal(true);
        setWidth(POPUP_DEFAULT_WINDOW_WIDTH, UNITS_PIXELS);
        setHeight(POPUP_DEFAULT_WINDOW_HEIGHT, UNITS_PIXELS);
        gridLayout = new GridLayout(1, 4);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);
        gridLayout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1000);
        gridLayout.setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
    }

    private void addName() {
        nameTextField = new TextField("Name");
        nameTextField.setDebugId(CREATE_NEW_TASK_POPUP_NAME);
        nameTextField.setMaxLength(TASK_NAME_MAX_CHARACTERS);
        nameTextField.setTabIndex(1);
        gridLayout.addComponent(nameTextField, COLUMN_ONE, ROW_ONE);
        nameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
    }

    private void addDescription() {
        descriptionTextField = new TextField("Description");
        descriptionTextField.setDebugId(CREATE_NEW_TASK_POPUP_DESCRIPTION);
        descriptionTextField.setMaxLength(TASK_DESCRIPTION_MAX_CHARACTERS);
        descriptionTextField.setSizeFull();
        descriptionTextField.setTabIndex(2);
        gridLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_TWO);
    }

    private void addEffort() {
        effortTextField = new TextField("Effort");
        effortTextField.setDebugId(CREATE_NEW_TASK_POPUP_EFFORT);
        effortTextField.setMaxLength(TASK_EFFORT_MAX_CHARACTERS);
        effortTextField.setTabIndex(3);
        gridLayout.addComponent(effortTextField, COLUMN_ONE, ROW_THREE);
        effortTextField.setWidth(4, UNITS_EM);
    }

    private void addButtons() {
        gridLayout.addComponent(createButtonsInLayout(), COLUMN_ONE, ROW_FOUR);
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
        nameTextField.addListener(new SetButtonFocusListener(createMoreButton));
        nameTextField.addListener(new RemoveButtonBlurListener(createMoreButton));
        effortTextField.addListener(new SetButtonFocusListener(createMoreButton));
        effortTextField.addListener(new RemoveButtonBlurListener(createMoreButton));
        UserHelper userUtil = new UserHelper();
        if (!userUtil.hasSprintPlannerPrivileges()) {
            nameTextField.setReadOnly(true);
            descriptionTextField.setReadOnly(true);
            effortTextField.setReadOnly(true);
            createMoreButton.setEnabled(false);
            createButton.setEnabled(false);
            cancelButton.setReadOnly(false);
        }
        nameTextField.focus();
    }

    protected void clearFields() {
        nameTextField.setValue("");
        descriptionTextField.setValue("");
        effortTextField.setValue("");
    }

    private HorizontalLayout createButtonsInLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        layout.setMargin(false);
        layout.setSpacing(true);
        addCreateMoreButton(layout);
        addCreateButton(layout);
        addCancelButton(layout);
        return layout;
    }

    private void addCreateMoreButton(HorizontalLayout layout) {
        createMoreButton = new Button("Create >>", new CreateButtonClickListener(this, false));
        createMoreButton.setDebugId(CREATE_NEW_TASK_POPUP_CREATE_MORE);
        createMoreButton.setTabIndex(4);
        createMoreButton.addStyleName("primary");
        layout.addComponent(createMoreButton);
        layout.setComponentAlignment(createMoreButton, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(createMoreButton, EXPAND_RATIO_1000);
    }

    private void addCreateButton(HorizontalLayout layout) {
        createButton = new Button("Create", new CreateButtonClickListener(this, true));
        createButton.setDebugId(CREATE_NEW_TASK_POPUP_CREATE_ONE);
        createButton.setTabIndex(5);
        layout.addComponent(createButton);
        layout.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
    }

    private void addCancelButton(HorizontalLayout layout) {
        cancelButton = new Button("Cancel", new CancelButtonClickListener(this));
        cancelButton.setDebugId(CREATE_NEW_TASK_POPUP_CANCEL);
        cancelButton.setTabIndex(6);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
}

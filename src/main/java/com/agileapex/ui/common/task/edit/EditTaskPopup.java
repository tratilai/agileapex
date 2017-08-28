package com.agileapex.ui.common.task.edit;

import java.util.List;

import com.agileapex.domain.Task;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.vaadin.RemoveButtonBlurListener;
import com.agileapex.ui.common.vaadin.SetButtonFocusListener;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
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

public class EditTaskPopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = -8953115595265287920L;
    protected final RefreshableComponent parentLayout;
    protected final Task task;
    protected final boolean hasEditAuthorization;
    protected GridLayout gridLayout;
    protected TextField nameTextField;
    protected TextField descriptionTextField;
    protected TextField effortTextField;
    protected NativeSelect status;
    protected NativeSelect pointPersonSelect;
    protected Button saveButton;
    protected Button cancelButton;
    private Action cancelAction;

    public EditTaskPopup(RefreshableComponent parentLayout, Task task, boolean hasEditAuthorization) {
        super("Edit Task");
        if (!hasEditAuthorization) {
            setCaption("View task");
        }
        this.parentLayout = parentLayout;
        this.task = task;
        this.hasEditAuthorization = hasEditAuthorization;
        setLayoutBasics();
        addName();
        addDescription();
        addEffort();
        addStatus();
        addPointPerson();
        addButtons();
        setContent(gridLayout);
        addShortcutActions();
    }

    private void setLayoutBasics() {
        setModal(true);
        setWidth(POPUP_DEFAULT_WINDOW_WIDTH, UNITS_PIXELS);
        setHeight(POPUP_DEFAULT_WINDOW_HEIGHT, UNITS_PIXELS);
        gridLayout = new GridLayout(3, 4);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);
        gridLayout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1000);
        gridLayout.setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1);
        gridLayout.setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_TWO, EXPAND_RATIO_1);
        gridLayout.setColumnExpandRatio(COLUMN_THREE, EXPAND_RATIO_1000);
    }

    private void addName() {
        nameTextField = new TextField("Name");
        nameTextField.setDebugId(EDIT_TASK_POPUP_NAME);
        if (!hasEditAuthorization) {
            nameTextField.addStyleName("add-borders");
        }
        nameTextField.setMaxLength(TASK_NAME_MAX_CHARACTERS);
        nameTextField.setValue(task.getName());
        nameTextField.setTabIndex(1);
        gridLayout.addComponent(nameTextField, COLUMN_ONE, ROW_ONE, COLUMN_THREE, ROW_ONE);
        nameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
    }

    private void addDescription() {
        descriptionTextField = new TextField("Description");
        descriptionTextField.setDebugId(EDIT_TASK_POPUP_DESCRIPTION);
        if (!hasEditAuthorization) {
            descriptionTextField.addStyleName("add-borders");
        }
        descriptionTextField.setMaxLength(TASK_DESCRIPTION_MAX_CHARACTERS);
        descriptionTextField.setValue(task.getDescription());
        descriptionTextField.setSizeFull();
        descriptionTextField.setTabIndex(2);
        gridLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_TWO, COLUMN_THREE, ROW_TWO);
    }

    private void addEffort() {
        effortTextField = new TextField("Effort");
        effortTextField.setDebugId(EDIT_TASK_POPUP_EFFORT);
        effortTextField.addStyleName("add-borders");
        effortTextField.setMaxLength(TASK_EFFORT_MAX_CHARACTERS);
        effortTextField.setTabIndex(3);
        effortTextField.setImmediate(true);
        gridLayout.addComponent(effortTextField, COLUMN_ONE, ROW_THREE);
        effortTextField.setWidth(4, UNITS_EM);
        effortTextField.setValue(task.getEffortAsText());
    }

    private void addStatus() {
        BeanItemContainer<TaskStatus> container = new BeanItemContainer<TaskStatus>(TaskStatus.class);
        container.addAll(TaskStatus.getStatuses());
        status = new NativeSelect("Status", container);
        status.setDebugId(EDIT_TASK_POPUP_STATUS);
        status.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        status.setItemCaptionPropertyId("status");
        status.setNullSelectionAllowed(false);
        status.select(task.getStatus());
        status.setImmediate(true);
        status.setTabIndex(4);
        status.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 8178118345456625805L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                TaskStatus taskStatus = (TaskStatus) event.getProperty().getValue();
                if (taskStatus == TaskStatus.DONE) {
                    effortTextField.setReadOnly(true);
                } else {
                    if (!task.isLeaf()) {
                        effortTextField.setReadOnly(true);
                    } else {
                        effortTextField.setReadOnly(false);
                    }
                }
            }
        });
        gridLayout.addComponent(status, COLUMN_TWO, ROW_THREE);
    }

    private void addPointPerson() {
        UserPersistence userDbService = new UserPersistenceImpl();
        List<User> users = userDbService.getAll();
        pointPersonSelect = new NativeSelect("Point Person");
        pointPersonSelect.setDebugId(EDIT_TASK_POPUP_POINTPERSON);
        BeanItemContainer<User> container = new BeanItemContainer<User>(User.class);
        if (users != null) {
            container.addAll(users);
        }
        pointPersonSelect.setContainerDataSource(container);
        pointPersonSelect.setNullSelectionAllowed(true);
        pointPersonSelect.setImmediate(true);
        pointPersonSelect.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        pointPersonSelect.setItemCaptionPropertyId("fullName");
        pointPersonSelect.setTabIndex(5);
        if (task.getAssigned() != null) {
            pointPersonSelect.setValue(task.getAssigned());
        }
        gridLayout.addComponent(pointPersonSelect, COLUMN_THREE, ROW_THREE);
        pointPersonSelect.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
    }

    private void addButtons() {
        gridLayout.addComponent(createButtonsInLayout(), COLUMN_ONE, ROW_FOUR, COLUMN_THREE, ROW_FOUR);
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
        nameTextField.addListener(new SetButtonFocusListener(saveButton));
        nameTextField.addListener(new RemoveButtonBlurListener(saveButton));
        effortTextField.addListener(new SetButtonFocusListener(saveButton));
        effortTextField.addListener(new RemoveButtonBlurListener(saveButton));
        if (!hasEditAuthorization) {
            nameTextField.setReadOnly(true);
            descriptionTextField.setReadOnly(true);
            effortTextField.setReadOnly(true);
            status.setReadOnly(true);
            pointPersonSelect.setReadOnly(true);
        }
        if (!task.isLeaf() || task.getStatus().equals(TaskStatus.DONE)) {
            effortTextField.setReadOnly(true);
        }
        nameTextField.focus();
    }

    private HorizontalLayout createButtonsInLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        layout.setMargin(false);
        layout.setSpacing(true);
        addSaveButton(layout);
        addCancelButton(layout);
        return layout;
    }

    private void addSaveButton(HorizontalLayout layout) {
        if (hasEditAuthorization) {
            saveButton = new Button("Save", new SaveButtonClickListener(this));
            saveButton.setDebugId(EDIT_TASK_POPUP_SAVE);
            saveButton.setTabIndex(6);
            saveButton.addStyleName("primary");
            layout.addComponent(saveButton);
            layout.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
            layout.setExpandRatio(saveButton, EXPAND_RATIO_1000);
        }
    }

    private void addCancelButton(HorizontalLayout layout) {
        String caption = hasEditAuthorization ? "Cancel" : "Close";
        cancelButton = new Button(caption, new CancelButtonClickListener(this));
        cancelButton.setDebugId(EDIT_TASK_POPUP_CANCEL);
        cancelButton.setTabIndex(7);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
}

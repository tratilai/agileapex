package com.agileapex.ui.common.sprint.edit;

import java.util.Date;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Sprint;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.vaadin.RemoveButtonBlurListener;
import com.agileapex.ui.common.vaadin.SetButtonFocusListener;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class EditSprintPopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = 7768203380478189842L;
    protected RefreshableComponent parentLayout;
    protected final Sprint sprint;
    protected GridLayout gridLayout;
    protected TextField nameTextField;
    protected TextField descriptionTextField;
    protected PopupDateField startDateField;
    protected PopupDateField endDateField;
    protected Button saveButton;
    protected Button cancelButton;
    private Action cancelAction;

    public EditSprintPopup(RefreshableComponent parentLayout, Sprint sprint) {
        super("Edit Sprint");
        setDebugId(EDIT_SPRINT_POPUP);
        this.parentLayout = parentLayout;
        this.sprint = sprint;
        setLayoutBasics();
        addName();
        addDescription();
        addStartAndEndDates();
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
        gridLayout.setColumnExpandRatio(COLUMN_TWO, EXPAND_RATIO_1);
    }

    private void addName() {
        nameTextField = new TextField("Name");
        nameTextField.setDebugId(EDIT_SPRINT_POPUP_NAME);
        nameTextField.setMaxLength(PROJECT_NAME_MAX_LENGTH);
        nameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        nameTextField.setValue(sprint.getName());
        nameTextField.setTabIndex(1);
        gridLayout.addComponent(nameTextField, COLUMN_ONE, ROW_ONE, COLUMN_TWO, ROW_ONE);
    }

    private void addDescription() {
        descriptionTextField = new TextField("Description");
        descriptionTextField.setDebugId(EDIT_SPRINT_POPUP_DESCRIPTION);
        descriptionTextField.setMaxLength(PROJECT_DESCRIPTION_MAX_CHARACTERS);
        descriptionTextField.setSizeFull();
        descriptionTextField.setValue(sprint.getDescription());
        descriptionTextField.setTabIndex(2);
        gridLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_TWO, COLUMN_TWO, ROW_TWO);
    }

    private void addStartAndEndDates() {
        startDateField = new PopupDateField("Start Date") {
            private static final long serialVersionUID = -8877214133300480490L;

            @Override
            protected Date handleUnparsableDateString(String dateString) throws Property.ConversionException {
                return null;
            }
        };
        startDateField.setDebugId(EDIT_SPRINT_POPUP_START_DATE);
        startDateField.setShowISOWeekNumbers(true);
        startDateField.setValue(sprint.getStartDate().toDate());
        startDateField.setResolution(DateField.RESOLUTION_DAY);
        startDateField.setTabIndex(3);
        gridLayout.addComponent(startDateField, COLUMN_ONE, ROW_THREE);
        startDateField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        endDateField = new PopupDateField("End Date") {
            private static final long serialVersionUID = -2750199385259920822L;

            @Override
            protected Date handleUnparsableDateString(String dateString) throws Property.ConversionException {
                return null;
            }
        };
        endDateField.setDebugId(EDIT_SPRINT_POPUP_END_DATE);
        endDateField.setShowISOWeekNumbers(true);
        endDateField.setValue(sprint.getEndDate().toDate());
        endDateField.setResolution(DateField.RESOLUTION_DAY);
        endDateField.setTabIndex(4);
        gridLayout.addComponent(endDateField, COLUMN_TWO, ROW_THREE);
        endDateField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
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
        nameTextField.addListener(new SetButtonFocusListener(saveButton));
        nameTextField.addListener(new RemoveButtonBlurListener(saveButton));
        UserHelper userUtil = new UserHelper();
        if (!userUtil.hasSprintPlannerPrivileges()) {
            nameTextField.setReadOnly(true);
            descriptionTextField.setReadOnly(true);
            saveButton.setEnabled(false);
            cancelButton.setReadOnly(false);
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
        saveButton = new Button("Save", new SaveButtonClickListener(this));
        saveButton.setDebugId(EDIT_SPRINT_POPUP_SAVE);
        saveButton.setTabIndex(5);
        saveButton.addStyleName("primary");
        layout.addComponent(saveButton);
        layout.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(saveButton, EXPAND_RATIO_1000);
    }

    private void addCancelButton(HorizontalLayout layout) {
        cancelButton = new Button("Cancel", new CancelButtonClickListener(this));
        cancelButton.setDebugId(EDIT_SPRINT_POPUP_CANCEL);
        cancelButton.setTabIndex(6);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
}

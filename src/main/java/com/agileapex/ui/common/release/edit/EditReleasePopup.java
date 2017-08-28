package com.agileapex.ui.common.release.edit;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Release;
import com.agileapex.domain.ReleaseStatus;
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

public class EditReleasePopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = -6475371220901057114L;
    protected RefreshableComponent parentLayout;
    protected final Release release;
    protected GridLayout gridLayout;
    protected TextField nameTextField;
    protected TextField descriptionTextField;
    protected NativeSelect statusSelect;
    protected Button saveButton;
    protected Button cancelButton;
    private Action cancelAction;

    public EditReleasePopup(RefreshableComponent parentLayout, Release release) {
        super("Edit Release");
        setDebugId(EDIT_RELEASE_POPUP);
        this.parentLayout = parentLayout;
        this.release = release;
        setLayoutBasics();
        addName();
        addDescription();
        addStatus();
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
        gridLayout.setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1);
    }

    private void addName() {
        nameTextField = new TextField("Name");
        nameTextField.setDebugId(EDIT_RELEASE_POPUP_NAME);
        nameTextField.setMaxLength(PROJECT_NAME_MAX_LENGTH);
        nameTextField.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        nameTextField.setValue(release.getName());
        nameTextField.setTabIndex(1);
        gridLayout.addComponent(nameTextField, COLUMN_ONE, ROW_ONE);
    }

    private void addDescription() {
        descriptionTextField = new TextField("Description");
        descriptionTextField.setDebugId(EDIT_RELEASE_POPUP_DESCRIPTION);
        descriptionTextField.setMaxLength(PROJECT_DESCRIPTION_MAX_CHARACTERS);
        descriptionTextField.setSizeFull();
        descriptionTextField.setValue(release.getDescription());
        descriptionTextField.setTabIndex(2);
        gridLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_TWO);
    }

    private void addStatus() {
        BeanItemContainer<ReleaseStatus> container = new BeanItemContainer<ReleaseStatus>(ReleaseStatus.class);
        container.addAll(ReleaseStatus.getStatuses());
        statusSelect = new NativeSelect("Status", container);
        statusSelect.setDebugId(EDIT_RELEASE_POPUP_STATUS);
        statusSelect.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        statusSelect.setItemCaptionPropertyId("status");
        statusSelect.setNullSelectionAllowed(false);
        statusSelect.select(release.getStatus());
        statusSelect.setImmediate(true);
        statusSelect.setTabIndex(3);
        gridLayout.addComponent(statusSelect, COLUMN_ONE, ROW_THREE);
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
        nameTextField.addListener(new SetButtonFocusListener(saveButton));
        nameTextField.addListener(new RemoveButtonBlurListener(saveButton));
        UserHelper userUtil = new UserHelper();
        if (!userUtil.hasManagerPrivileges()) {
            nameTextField.setReadOnly(true);
            descriptionTextField.setReadOnly(true);
            statusSelect.setReadOnly(true);
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
        saveButton.setDebugId(EDIT_RELEASE_POPUP_SAVE);
        saveButton.setTabIndex(4);
        saveButton.addStyleName("primary");
        layout.addComponent(saveButton);
        layout.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(saveButton, EXPAND_RATIO_1000);
    }

    private void addCancelButton(HorizontalLayout layout) {
        cancelButton = new Button("Cancel", new CancelButtonClickListener(this));
        cancelButton.setDebugId(EDIT_RELEASE_POPUP_CANCEL);
        cancelButton.setTabIndex(5);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
}

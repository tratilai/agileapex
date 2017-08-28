package com.agileapex.ui.common.user.delete;

import com.agileapex.domain.User;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class DeleteUserPopup extends Window implements Handler, Constants {
    private static final long serialVersionUID = 876088665200733470L;
    protected RefreshableComponent parentLayout;
    protected final User userToDelete;
    protected GridLayout gridLayout;
    private Action cancelAction;
    private Button cancelButton;
    private Button deleteButton;

    public DeleteUserPopup(RefreshableComponent parentLayout, User userToDelete) {
        super("Delete User");
        this.parentLayout = parentLayout;
        this.userToDelete = userToDelete;
        setLayoutBasics();
        addText();
        addButtons();
        setContent(gridLayout);
        addShortcutActions();
    }

    private void setLayoutBasics() {
        setModal(true);
        setWidth(POPUP_DELETE_WINDOW_WIDTH, UNITS_PIXELS);
        setHeight(POPUP_DELETE_WINDOW_HEIGHT, UNITS_PIXELS);
        gridLayout = new GridLayout(1, 2);
        gridLayout.setSizeFull();
        gridLayout.setMargin(true);
        gridLayout.setSpacing(true);
        gridLayout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1000);
        gridLayout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1);
    }

    private void addText() {
        String text = "Are you sure you want to delete the user?";
        text += "<br><br><table>";
        text += "<tr><td>Last Name:&nbsp;&nbsp;</td><td><b>" + userToDelete.getLastName() + "</b></td></tr>";
        text += "<tr><td>First Name:&nbsp;&nbsp;</td><td><b>" + userToDelete.getFirstName() + "</b></td></tr>";
        text += "<tr><td>Email:&nbsp;&nbsp;</td><td><b>" + userToDelete.getEmail() + "</b></td></tr>";
        text += "</table>";
        text += "<br>Please note, deleting the user will also clear all references to the user.";
        Label textLabel = new Label(text, Label.CONTENT_XHTML);
        textLabel.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        gridLayout.addComponent(textLabel, COLUMN_ONE, ROW_ONE);
    }

    private void addButtons() {
        gridLayout.addComponent(createButtonsInLayout(), COLUMN_ONE, ROW_TWO);
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
        cancelButton.focus();
    }

    private HorizontalLayout createButtonsInLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        layout.setMargin(false);
        layout.setSpacing(true);
        addDeleteButton(layout);
        addCancelButton(layout);
        return layout;
    }

    private void addDeleteButton(HorizontalLayout layout) {
        deleteButton = new Button("Delete", new DeleteButtonClickListener(this));
        deleteButton.setDebugId(DELETE_USER_POPUP_DELETE);
        deleteButton.setTabIndex(2);
        layout.addComponent(deleteButton);
        layout.setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);
        layout.setExpandRatio(deleteButton, EXPAND_RATIO_1000);
    }

    private void addCancelButton(HorizontalLayout layout) {
        cancelButton = new Button("Cancel", new CancelButtonClickListener(this));
        cancelButton.setDebugId(DELETE_USER_POPUP_CANCEL);
        cancelButton.setClickShortcut(KeyCode.ENTER);
        cancelButton.addStyleName("primary");
        cancelButton.setTabIndex(1);
        layout.addComponent(cancelButton);
        layout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    }
}

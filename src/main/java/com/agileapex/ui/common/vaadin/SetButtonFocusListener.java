package com.agileapex.ui.common.vaadin;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

public class SetButtonFocusListener implements FocusListener {
    private static final long serialVersionUID = -1278299372887176165L;
    private final Button button;

    public SetButtonFocusListener(Button button) {
        this.button = button;
    }

    @Override
    public void focus(FocusEvent event) {
        if (this.button != null) {
            this.button.setClickShortcut(KeyCode.ENTER);
        }
    }
}

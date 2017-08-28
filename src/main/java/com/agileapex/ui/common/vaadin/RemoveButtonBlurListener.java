package com.agileapex.ui.common.vaadin;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Button;

public class RemoveButtonBlurListener implements BlurListener {
    private static final long serialVersionUID = 5356164540993793360L;
    private final Button button;

    public RemoveButtonBlurListener(Button button) {
        this.button = button;
    }

    @Override
    public void blur(BlurEvent event) {
        if (this.button != null) {
            this.button.removeClickShortcut();
        }
    }
}

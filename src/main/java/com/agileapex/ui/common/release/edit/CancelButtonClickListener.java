package com.agileapex.ui.common.release.edit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 1254248523627635578L;
    private final EditReleasePopup layout;

    public CancelButtonClickListener(EditReleasePopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When editing release, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

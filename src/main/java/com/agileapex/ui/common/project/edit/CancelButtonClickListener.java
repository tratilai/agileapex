package com.agileapex.ui.common.project.edit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 1677366269192960158L;
    private final EditProjectPopup layout;

    public CancelButtonClickListener(EditProjectPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When editing project, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

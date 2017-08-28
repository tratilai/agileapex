package com.agileapex.ui.common.sprint.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = -679236733121555468L;
    private final DeleteSprintPopup layout;

    public CancelButtonClickListener(DeleteSprintPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When deleting sprint, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

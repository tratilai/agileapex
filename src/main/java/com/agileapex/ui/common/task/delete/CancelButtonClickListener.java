package com.agileapex.ui.common.task.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = -1352948796629254294L;
    private final DeleteTaskPopup layout;

    public CancelButtonClickListener(DeleteTaskPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When deleting task, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

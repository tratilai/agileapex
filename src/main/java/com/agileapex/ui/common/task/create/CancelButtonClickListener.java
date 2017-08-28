package com.agileapex.ui.common.task.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 8497328195678105266L;
    private final CreateNewTaskPopup layout;

    public CancelButtonClickListener(CreateNewTaskPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When creating a new task, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

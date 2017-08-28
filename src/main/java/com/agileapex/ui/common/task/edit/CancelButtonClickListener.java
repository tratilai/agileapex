package com.agileapex.ui.common.task.edit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 8284960835547752711L;
    private final EditTaskPopup layout;

    public CancelButtonClickListener(EditTaskPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When editing task, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

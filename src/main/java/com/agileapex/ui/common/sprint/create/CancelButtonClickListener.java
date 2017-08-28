package com.agileapex.ui.common.sprint.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 7197627387894040850L;
    private final CreateNewSprintPopup layout;

    public CancelButtonClickListener(CreateNewSprintPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When creating a new sprint, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

package com.agileapex.ui.common.project.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = -1893893950814174322L;
    private final CreateNewProjectPopup layout;

    public CancelButtonClickListener(CreateNewProjectPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When creating a new project, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

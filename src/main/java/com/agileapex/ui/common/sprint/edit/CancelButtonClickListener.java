package com.agileapex.ui.common.sprint.edit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 5638591602535726599L;
    private final EditSprintPopup layout;

    public CancelButtonClickListener(EditSprintPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When editing sprint, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

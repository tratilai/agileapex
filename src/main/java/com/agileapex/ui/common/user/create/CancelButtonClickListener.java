package com.agileapex.ui.common.user.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 8148933105217652802L;
    private final CreateNewUserPopup layout;

    public CancelButtonClickListener(CreateNewUserPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When creating a new user, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

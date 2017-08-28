package com.agileapex.ui.common.user.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = -2955158336441262241L;
    private final DeleteUserPopup layout;

    public CancelButtonClickListener(DeleteUserPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When deleting user, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

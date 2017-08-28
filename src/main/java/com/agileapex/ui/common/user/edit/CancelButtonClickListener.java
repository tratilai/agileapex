package com.agileapex.ui.common.user.edit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 782485447156971735L;
    private final EditUserPopup layout;

    public CancelButtonClickListener(EditUserPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When editing user, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

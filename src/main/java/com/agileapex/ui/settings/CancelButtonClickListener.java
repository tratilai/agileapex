package com.agileapex.ui.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = -5409258696778021942L;
    private final UserDetailsLayout layout;

    public CancelButtonClickListener(UserDetailsLayout layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Settings cancel button clicked.");
        if (layout.emailTextField.isEnabled()) {
            layout.clearPasswordFields();
            layout.disableEditButton();
            layout.firstNameTextField.setValue(layout.user.getFirstName());
            layout.lastNameTextField.setValue(layout.user.getLastName());
            layout.emailTextField.setValue(layout.user.getEmail());
        }
    }
}

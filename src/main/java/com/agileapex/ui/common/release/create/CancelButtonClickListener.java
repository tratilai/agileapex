package com.agileapex.ui.common.release.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 3086190744869592841L;
    private final CreateNewReleasePopup layout;

    public CancelButtonClickListener(CreateNewReleasePopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When creating a new release, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

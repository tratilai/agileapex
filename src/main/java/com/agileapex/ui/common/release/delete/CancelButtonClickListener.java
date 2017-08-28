package com.agileapex.ui.common.release.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = 6244608878302461313L;
    private final DeleteReleasePopup layout;

    public CancelButtonClickListener(DeleteReleasePopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When deleting release, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

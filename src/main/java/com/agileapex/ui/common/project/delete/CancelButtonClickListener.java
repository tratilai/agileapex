package com.agileapex.ui.common.project.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CancelButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CancelButtonClickListener.class);
    private static final long serialVersionUID = -1446460824418133764L;
    private final DeleteProjectPopup layout;

    public CancelButtonClickListener(DeleteProjectPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("When deleting project, cancel button clicked.");
        layout.getParent().removeWindow(layout);
    }
}

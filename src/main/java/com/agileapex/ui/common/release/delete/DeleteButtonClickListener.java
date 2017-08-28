package com.agileapex.ui.common.release.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.ReleasePersistence;
import com.agileapex.persistence.ReleasePersistenceImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class DeleteButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteButtonClickListener.class);
    private static final long serialVersionUID = 5176968852979106142L;
    private final DeleteReleasePopup layout;

    public DeleteButtonClickListener(DeleteReleasePopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Delete release button clicked: {}", layout.release);
        ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
        releaseDbService.delete(layout.release);
        layout.getParent().removeWindow(layout);
        layout.parentLayout.refresh();
        logger.debug("Release deleted.");
    }
}

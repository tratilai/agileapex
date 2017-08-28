package com.agileapex.ui.release;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Release;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

public class ReleaseTableValueChangeListener implements Property.ValueChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseTableValueChangeListener.class);
    private static final long serialVersionUID = -7662074818280982634L;
    private final ReleasePanel releasePanel;

    public ReleaseTableValueChangeListener(ReleasePanel releasePanel) {
        this.releasePanel = releasePanel;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        if (value != null) {
            logger.debug("Release table item value changed. {}", value);
            releasePanel.doSelectionActions((Release) value);
        }
    }
}

package com.agileapex.ui.sprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Sprint;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

public class SprintTableValueChangeListener implements Property.ValueChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(SprintTableValueChangeListener.class);
    private static final long serialVersionUID = -2757636527941076631L;
    private final SprintPanel sprintPanel;

    public SprintTableValueChangeListener(SprintPanel sprintPanel) {
        this.sprintPanel = sprintPanel;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        if (value != null) {
            logger.debug("Sprint table item value changed. {}", value);
            sprintPanel.doSelectionActions((Sprint) value);
        }
    }
}

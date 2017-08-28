package com.agileapex.ui.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Project;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

public class ProjectTableValueChangeListener implements Property.ValueChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(ProjectTableValueChangeListener.class);
    private static final long serialVersionUID = -1763066691498310769L;
    private final ProjectPanel projectPanel;

    public ProjectTableValueChangeListener(ProjectPanel projectPanel) {
        this.projectPanel = projectPanel;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        if (value != null) {
            logger.debug("Project table item value changed. {}", value);
            projectPanel.doSelectionActions((Project) value);
        }
    }
}

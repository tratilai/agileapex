package com.agileapex.ui.common.project.edit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Project;
import com.agileapex.domain.ProjectStatus;
import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.agileapex.ui.common.project.ProjectDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class SaveButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(SaveButtonClickListener.class);
    private static final long serialVersionUID = -7030843312418744460L;
    private final EditProjectPopup layout;

    public SaveButtonClickListener(EditProjectPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Save button clicked for edit project.");
        ProjectDetailsValidator validator = new ProjectDetailsValidator();
        if (validator.isValid(layout.project, layout.getWindow(), layout.nameTextField, layout.descriptionTextField)) {
            Project project = setDataFromPopupFields();
            ProjectPersistence projectDbService = new ProjectPersistenceImpl();
            projectDbService.update(project);
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
            logger.debug("Project updated: {}", project);
        }
    }

    private Project setDataFromPopupFields() {
        String name = StringUtils.trimToEmpty((String) layout.nameTextField.getValue());
        String description = (String) layout.descriptionTextField.getValue();
        ProjectStatus status = (ProjectStatus) layout.statusSelect.getValue();
        layout.project.setName(name);
        layout.project.setDescription(description);
        layout.project.setStatus(status);
        return layout.project;
    }
}

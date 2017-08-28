package com.agileapex.ui.common.project.create;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Project;
import com.agileapex.domain.ProjectStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.project.ProjectDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CreateButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateButtonClickListener.class);
    private static final long serialVersionUID = 7185754107167115132L;
    private final CreateNewProjectPopup layout;

    public CreateButtonClickListener(CreateNewProjectPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Create a new project button clicked.");
        ProjectDetailsValidator validator = new ProjectDetailsValidator();
        if (validator.isValid(layout.getWindow(), layout.nameTextField, layout.descriptionTextField, layout.taskPrefixTextField)) {
            Project project = setDataFromPopupFields();
            ProjectPersistence projectDbService = new ProjectPersistenceImpl();
            projectDbService.create(project, ApplicationSession.getUser());
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
            logger.debug("New project created: {}", project);
        }
    }

    private Project setDataFromPopupFields() {
        String name = StringUtils.trimToEmpty((String) layout.nameTextField.getValue());
        ProjectStatus projectStatus = (ProjectStatus) layout.statusSelect.getValue();
        String description = (String) layout.descriptionTextField.getValue();
        String prefix = StringUtils.trimToEmpty((String) layout.taskPrefixTextField.getValue());
        User user = ApplicationSession.getUser();
        Project project = new Project(name, description, projectStatus, user, prefix);
        return project;
    }
}

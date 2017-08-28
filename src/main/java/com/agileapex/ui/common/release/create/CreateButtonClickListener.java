package com.agileapex.ui.common.release.create;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Release;
import com.agileapex.domain.ReleaseStatus;
import com.agileapex.persistence.ReleasePersistence;
import com.agileapex.persistence.ReleasePersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.release.ReleaseDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CreateButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateButtonClickListener.class);
    private static final long serialVersionUID = -3303524029890563011L;
    private final CreateNewReleasePopup layout;

    public CreateButtonClickListener(CreateNewReleasePopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Create a new release button clicked.");
        ReleaseDetailsValidator validator = new ReleaseDetailsValidator();
        if (validator.isValid(layout.getWindow(), layout.project, layout.nameTextField, layout.descriptionTextField)) {
            Release release = setDataFromPopupFields();
            ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
            releaseDbService.create(release);
            closePopup();
            logger.debug("New release created: {}", release);
        }
    }

    private Release setDataFromPopupFields() {
        String name = StringUtils.trimToEmpty((String) layout.nameTextField.getValue());
        ReleaseStatus releaseStatus = (ReleaseStatus) layout.statusSelect.getValue();
        String description = (String) layout.descriptionTextField.getValue();
        Release release = new Release(layout.project, name, description, releaseStatus, ApplicationSession.getUser());
        return release;
    }

    private void closePopup() {
        layout.getParent().removeWindow(layout);
        layout.parentLayout.refresh();
    }
}

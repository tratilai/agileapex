package com.agileapex.ui.common.release.edit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Release;
import com.agileapex.domain.ReleaseStatus;
import com.agileapex.persistence.ReleasePersistence;
import com.agileapex.persistence.ReleasePersistenceImpl;
import com.agileapex.ui.common.release.ReleaseDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class SaveButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(SaveButtonClickListener.class);
    private static final long serialVersionUID = 3801819473522270459L;
    private final EditReleasePopup layout;

    public SaveButtonClickListener(EditReleasePopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Save button clicked for edit release.");
        ReleaseDetailsValidator validator = new ReleaseDetailsValidator();
        if (validator.isValid(layout.getWindow(), layout.release, layout.release.getParentProject(), layout.nameTextField, layout.descriptionTextField)) {
            Release release = setDataFromPopupFields();
            ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
            releaseDbService.update(release);
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
            logger.debug("Release updated: {}", release);
        }
    }

    private Release setDataFromPopupFields() {
        String name = StringUtils.trimToEmpty((String) layout.nameTextField.getValue());
        String description = (String) layout.descriptionTextField.getValue();
        ReleaseStatus status = (ReleaseStatus) layout.statusSelect.getValue();
        layout.release.setName(name);
        layout.release.setDescription(description);
        layout.release.setStatus(status);
        return layout.release;
    }
}

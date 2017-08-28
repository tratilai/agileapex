package com.agileapex.ui.common.sprint.edit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.domain.Sprint;
import com.agileapex.persistence.SprintPersistence;
import com.agileapex.persistence.SprintPersistenceImpl;
import com.agileapex.ui.common.sprint.SprintDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class SaveButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(SaveButtonClickListener.class);
    private static final long serialVersionUID = 5995849433499644762L;
    private final EditSprintPopup layout;

    public SaveButtonClickListener(EditSprintPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        logger.debug("Save button clicked for edit sprint.");
        SprintDetailsValidator validator = new SprintDetailsValidator();
        if (validator.isValid(layout.getWindow(), layout.sprint, layout.sprint.getParentRelease(), layout.nameTextField, layout.descriptionTextField, layout.startDateField, layout.endDateField)) {
            Sprint sprint = setDataFromPopupFields();
            SprintPersistence sprintDbService = new SprintPersistenceImpl();
            sprintDbService.update(sprint);
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
            logger.debug("Sprint updated: {}", sprint);
        }
    }

    private Sprint setDataFromPopupFields() {
        String name = StringUtils.trimToEmpty((String) layout.nameTextField.getValue());
        String description = (String) layout.descriptionTextField.getValue();
        layout.sprint.setName(name);
        layout.sprint.setDescription(description);
        DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
        layout.sprint.setStartDate(dateAndTimeUtil.convert(layout.startDateField).withTime(0, 0, 0, 0));
        layout.sprint.setEndDate(dateAndTimeUtil.convert(layout.endDateField).withTime(23, 59, 59, 999));
        return layout.sprint;
    }
}

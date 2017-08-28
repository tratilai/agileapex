package com.agileapex.ui.common.sprint.create;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.SprintStatus;
import com.agileapex.persistence.SprintPersistence;
import com.agileapex.persistence.SprintPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.sprint.SprintDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CreateButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateButtonClickListener.class);
    private static final long serialVersionUID = -9080020422449618310L;
    private final CreateNewSprintPopup layout;

    public CreateButtonClickListener(CreateNewSprintPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        create();
    }

    private void create() {
        logger.debug("Create a new sprint button clicked.");
        SprintDetailsValidator validator = new SprintDetailsValidator();
        if (validator.isValid(layout.getWindow(), layout.release, layout.nameTextField, layout.descriptionTextField, layout.startDateField, layout.endDateField)) {
            Sprint sprint = setDataFromPopupFields();
            SprintPersistence sprintDbService = new SprintPersistenceImpl();
            sprintDbService.create(layout.release, sprint, ApplicationSession.getUser());
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
            logger.debug("New sprint created: {}", sprint);
        }
    }

    private Sprint setDataFromPopupFields() {
        String name = StringUtils.trimToEmpty((String) layout.nameTextField.getValue());
        String description = (String) layout.descriptionTextField.getValue();
        DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
        DateTime startDate = dateAndTimeUtil.convert(layout.startDateField).withTime(0, 0, 0, 0);
        DateTime endDate = dateAndTimeUtil.convert(layout.endDateField).withTime(23, 59, 59, 999);
        Sprint sprint = new Sprint(0, null, layout.release, name, description, SprintStatus.OPEN, startDate, endDate, ApplicationSession.getUser());
        return sprint;
    }
}

package com.agileapex.ui.common.sprint.edit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import mockit.Mock;
import mockit.MockUp;

import org.joda.time.DateTime;
import org.junit.Test;

import com.agileapex.AgileApexUnitTest;
import com.agileapex.common.sprint.SprintHelper;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.sprint.SprintDetailsValidator;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class SprintDetailsValidatorTest extends AgileApexUnitTest implements Constants {

    private TextField nameTextField;
    private TextField descriptionTextField;
    private PopupDateField startDateField;
    private PopupDateField endDateField;

    @Test
    public void sprintShouldHaveUniqueName() {
        Window window = new Window();
        new MockUp<SprintHelper>() {
            @Mock
            public boolean isUniqueName(Sprint currentSprint, Release release, String nameToCheck) {
                return false;
            }
        };
        DateTime date = new DateTime().plusDays(11);
        setSprintParameters("a", "b", date.toDate(), date.plusDays(142).toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Non-unique name passed.", result);
    }

    @Test
    public void endDateMissingShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime date = new DateTime().minusDays(1);
        setSprintParameters("a", "b", date.toDate(), null);
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Invalid dates passed.", result);
    }

    @Test
    public void startDateMissingShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime date = new DateTime().minusDays(1);
        setSprintParameters("a", "b", null, date.toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Invalid dates passed.", result);
    }

    @Test
    public void dateDifferenceTooLongShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime date = new DateTime().minusDays(1);
        setSprintParameters("a", "b", date.toDate(), date.plusDays(SPRINT_MAX_DAYS_BETWEEN_START_AND_END_DATE + 1).toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Invalid dates passed.", result);
    }

    @Test
    public void endDateSameThanStartDateShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime date = new DateTime().minusDays(1000);
        setSprintParameters("a", "b", date.toDate(), date.toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Invalid dates passed.", result);
    }

    @Test
    public void endDateBeforeStartDateShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime now = new DateTime();
        setSprintParameters("a", "b", now.toDate(), now.minusDays(1).toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Invalid dates passed.", result);
    }

    @Test
    public void tooLongDescriptionShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime date = new DateTime().plusDays(654);
        setSprintParameters("name", createText(SPRINT_DESCRIPTION_MAX_CHARACTERS + 1), date.toDate(), date.plusDays(1).toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Too long description passed.", result);
    }

    @Test
    public void tooLongNameShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime now = new DateTime();
        setSprintParameters(createText(SPRINT_NAME_MAX_LENGTH + 1), "description", now.toDate(), now.plusDays(14).toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Too long name passed.", result);
    }

    @Test
    public void emptyNameShouldNotPass() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime now = new DateTime();
        setSprintParameters(createText(SPRINT_NAME_MIN_LENGTH - 1), "description", now.toDate(), now.plusDays(14).toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertFalse("Empty name passed.", result);
    }

    @Test
    public void testShouldPassWithValidParameters() {
        Window window = new Window();
        new SprintHelperMock();
        DateTime now = new DateTime();
        setSprintParameters(createText(SPRINT_NAME_MAX_LENGTH), createText(SPRINT_DESCRIPTION_MAX_CHARACTERS), now.toDate(), now.plusDays(SPRINT_MAX_DAYS_BETWEEN_START_AND_END_DATE).toDate());
        SprintDetailsValidator validator = new SprintDetailsValidator();
        boolean result = validator.isValid(window, null, null, nameTextField, descriptionTextField, startDateField, endDateField);
        assertTrue("Basic validation test didn't pass.", result);

    }

    private void setSprintParameters(String name, String description, Date startDate, Date endDate) {
        nameTextField = new TextField("Name");
        nameTextField.setValue(name);
        descriptionTextField = new TextField("Description");
        descriptionTextField.setValue(description);
        startDateField = new PopupDateField("Start Date");
        startDateField.setValue(startDate);
        endDateField = new PopupDateField("End Date");
        endDateField.setValue(endDate);
    }
}

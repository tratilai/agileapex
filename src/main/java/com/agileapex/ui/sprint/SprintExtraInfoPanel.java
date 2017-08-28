package com.agileapex.ui.sprint;

import com.agileapex.common.DomainObjectHelper;
import com.agileapex.domain.Project;
import com.agileapex.domain.Sprint;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

public class SprintExtraInfoPanel extends Panel implements Constants {
    private static final long serialVersionUID = -8650169055093801614L;
    protected final SprintPanel sprintPanel;
    protected GridLayout infoLayout;
    protected Label nameTextField;
    protected TextField descriptionTextField;
    protected Label createdByLabel;
    protected Label creationDateLabel;
    protected Label nameTitle;
    protected Label createdByTitle;
    protected Label creationDateTitle;
    protected Label descriptionTitle;
    protected Label projectTitle;
    protected Label projectTextField;
    protected Label releaseTitle;
    protected Label releaseTextField;
    protected Label startDateTitle;
    protected Label startDateTextField;
    protected Label endDateTitle;
    protected Label endDateTextField;

    public SprintExtraInfoPanel(SprintPanel sprintPanel) {
        this.sprintPanel = sprintPanel;
        init();
        addName();
        addReleaseTitle();
        addProjectTitle();
        addStartDate();
        addEndDate();
        addCreatedBy();
        addCreationDate();
        addDescription();
    }

    protected void viewExtraInfoLayoutTitles(boolean view) {
        if (view) {
            nameTitle.setValue("<b>Sprint Name</b>");
            releaseTitle.setValue("<br><b>Release Name</b>");
            projectTitle.setValue("<br><b>Project Name</b>");
            startDateTitle.setValue("<br><b>Start Date</b>");
            endDateTitle.setValue("<br><b>End Date</b>");
            createdByTitle.setValue("<br><b>Created By</b>");
            creationDateTitle.setValue("<br><b>Creation Date</b>");
            descriptionTitle.setValue("<br><b>Description</b>");
        } else {
            nameTitle.setValue("");
            releaseTitle.setValue("");
            projectTitle.setValue("");
            startDateTitle.setValue("");
            endDateTitle.setValue("");
            createdByTitle.setValue("");
            creationDateTitle.setValue("");
            descriptionTitle.setValue("");
        }
    }

    protected void setExtraInfoLayoutData(Sprint sprint) {
        if (DomainObjectHelper.isNotNullOrZero(sprint)) {
            nameTextField.setValue(sprint.getName());
            releaseTextField.setValue(DomainObjectHelper.isNotNullOrZero(sprint.getParentRelease()) ? sprint.getParentRelease().getName() : "");
            if (DomainObjectHelper.isNotNullOrZero(sprint.getParentRelease())) {
                sprint.getParentRelease().fetchSecondLevelObjects();
                Project parentProject = sprint.getParentRelease().getParentProject();
                projectTextField.setValue(DomainObjectHelper.isNotNullOrZero(parentProject) ? parentProject.getName() : "");
            } else {
                projectTextField.setValue("");
            }
            startDateTextField.setValue(dateFormatter.formatToMediumDate(sprint.getStartDate()));
            endDateTextField.setValue(dateFormatter.formatToMediumDate(sprint.getEndDate()));
            descriptionTextField.setReadOnly(false);
            descriptionTextField.setValue(sprint.getDescription());
            descriptionTextField.setReadOnly(true);
            createdByLabel.setValue(DomainObjectHelper.isNotNullOrZero(sprint.getCreatedBy()) ? sprint.getCreatedBy().getFullName() : "");
            creationDateLabel.setValue(sprint.getCreationDate() != null ? dateFormatter.formatToMediumDateAndTime(sprint.getCreationDate()) : "");
        } else {
            nameTextField.setValue("");
            releaseTextField.setValue("");
            projectTextField.setValue("");
            startDateTextField.setValue("");
            endDateTextField.setValue("");
            createdByLabel.setValue("");
            creationDateLabel.setValue("");
            descriptionTextField.setReadOnly(false);
            descriptionTextField.setValue("");
            descriptionTextField.setReadOnly(true);
        }
    }

    private void init() {
        setSizeFull();
        infoLayout = new GridLayout(2, 12);
        infoLayout.setSizeFull();
        infoLayout.setMargin(false, false, false, true);
        infoLayout.setSpacing(false);
        infoLayout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_FIVE, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_SIX, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_SEVEN, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_EIGHT, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_NINE, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_TEN, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_ELEVEN, EXPAND_RATIO_1);
        infoLayout.setRowExpandRatio(ROW_TWELVE, EXPAND_RATIO_1000);
        setContent(infoLayout);
    }

    private void addName() {
        nameTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(nameTitle, COLUMN_ONE, ROW_ONE, COLUMN_TWO, ROW_ONE);
        nameTextField = new Label("");
        nameTextField.setDebugId(SPRINT_EXTRA_NAME);
        infoLayout.addComponent(nameTextField, COLUMN_ONE, ROW_TWO, COLUMN_TWO, ROW_TWO);
    }

    private void addReleaseTitle() {
        releaseTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(releaseTitle, COLUMN_ONE, ROW_THREE, COLUMN_TWO, ROW_THREE);
        releaseTextField = new Label("");
        releaseTextField.setDebugId(SPRINT_EXTRA_RELEASE);
        infoLayout.addComponent(releaseTextField, COLUMN_ONE, ROW_FOUR, COLUMN_TWO, ROW_FOUR);
    }

    private void addProjectTitle() {
        projectTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(projectTitle, COLUMN_ONE, ROW_FIVE, COLUMN_TWO, ROW_FIVE);
        projectTextField = new Label("");
        projectTextField.setDebugId(SPRINT_EXTRA_PROJECT);
        infoLayout.addComponent(projectTextField, COLUMN_ONE, ROW_SIX, COLUMN_TWO, ROW_SIX);
    }

    private void addStartDate() {
        startDateTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(startDateTitle, COLUMN_ONE, ROW_SEVEN);
        startDateTextField = new Label("");
        startDateTextField.setDebugId(SPRINT_EXTRA_START_DATE);
        infoLayout.addComponent(startDateTextField, COLUMN_ONE, ROW_EIGHT);
    }

    private void addEndDate() {
        endDateTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(endDateTitle, COLUMN_TWO, ROW_SEVEN);
        endDateTextField = new Label("");
        endDateTextField.setDebugId(SPRINT_EXTRA_END_DATE);
        infoLayout.addComponent(endDateTextField, COLUMN_TWO, ROW_EIGHT);
    }

    private void addCreatedBy() {
        createdByTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(createdByTitle, COLUMN_ONE, ROW_NINE);
        createdByLabel = new Label("");
        createdByLabel.setDebugId(SPRINT_EXTRA_CREATEDBY);
        infoLayout.addComponent(createdByLabel, COLUMN_ONE, ROW_TEN);
    }

    private void addCreationDate() {
        creationDateTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(creationDateTitle, COLUMN_TWO, ROW_NINE);
        creationDateLabel = new Label("");
        creationDateLabel.setDebugId(SPRINT_EXTRA_CREATIONDATE);
        infoLayout.addComponent(creationDateLabel, COLUMN_TWO, ROW_TEN);
    }

    private void addDescription() {
        descriptionTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(descriptionTitle, COLUMN_ONE, ROW_ELEVEN, COLUMN_TWO, ROW_ELEVEN);
        descriptionTextField = new TextField();
        descriptionTextField.setDebugId(SPRINT_EXTRA_DESCRIPTION);
        descriptionTextField.setSizeFull();
        descriptionTextField.setReadOnly(true);
        infoLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_TWELVE, COLUMN_TWO, ROW_TWELVE);
    }
}

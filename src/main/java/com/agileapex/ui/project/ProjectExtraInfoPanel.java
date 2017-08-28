package com.agileapex.ui.project;

import com.agileapex.common.DomainObjectHelper;
import com.agileapex.domain.Project;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

public class ProjectExtraInfoPanel extends Panel implements Constants {
    private static final long serialVersionUID = -2922228572451345693L;
    protected final ProjectPanel projectPanel;
    protected GridLayout infoLayout;
    protected Label nameTextField;
    protected TextField descriptionTextField;
    protected Label statusLabel;
    protected Label createdByLabel;
    protected Label creationDateLabel;
    protected Label nameTitle;
    protected Label statusTitle;
    protected Label createdByTitle;
    protected Label creationDateTitle;
    protected Label descriptionTitle;
    protected Label taskPrefixLabel;
    protected Label taskPrefixLabelTitle;

    public ProjectExtraInfoPanel(ProjectPanel projectPanel) {
        this.projectPanel = projectPanel;
        init();
        addName();
        addStatus();
        addTitle();
        addCreatedBy();
        addCreationDate();
        addDescription();
    }

    protected void viewExtraInfoLayoutTitles(boolean view) {
        if (view) {
            nameTitle.setValue("<b>Project Name</b>");
            statusTitle.setValue("<br><b>Status</b>");
            createdByTitle.setValue("<br><b>Created By</b>");
            creationDateTitle.setValue("<br><b>Creation Date</b>");
            taskPrefixLabelTitle.setValue("<br><b>Task Id's Prefix</b>");
            descriptionTitle.setValue("<br><b>Description</b>");
        } else {
            nameTitle.setValue("");
            statusTitle.setValue("");
            createdByTitle.setValue("");
            creationDateTitle.setValue("");
            taskPrefixLabelTitle.setValue("");
            descriptionTitle.setValue("");
        }
    }

    protected void setExtraInfoLayoutData(Project project) {
        if (DomainObjectHelper.isNotNullOrZero(project)) {
            nameTextField.setValue(project.getName());
            descriptionTextField.setReadOnly(false);
            descriptionTextField.setValue(project.getDescription());
            descriptionTextField.setReadOnly(true);
            statusLabel.setValue(project.getStatus().getStatus());
            createdByLabel.setValue(DomainObjectHelper.isNotNullOrZero(project.getCreatedBy()) ? project.getCreatedBy().getFullName() : "");
            creationDateLabel.setValue(project.getCreationDate() != null ? dateFormatter.formatToMediumDateAndTime(project.getCreationDate()) : "");
            taskPrefixLabel.setValue(project.getTaskPrefix());
        } else {
            nameTextField.setValue("");
            descriptionTextField.setReadOnly(false);
            descriptionTextField.setValue("");
            descriptionTextField.setReadOnly(true);
            statusLabel.setValue("");
            createdByLabel.setValue("");
            creationDateLabel.setValue("");
            taskPrefixLabel.setValue("");
        }
    }

    private void init() {
        setSizeFull();
        infoLayout = new GridLayout(2, 8);
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
        infoLayout.setRowExpandRatio(ROW_EIGHT, EXPAND_RATIO_1000);
        setContent(infoLayout);
    }

    private void addName() {
        nameTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(nameTitle, COLUMN_ONE, ROW_ONE, COLUMN_TWO, ROW_ONE);
        nameTextField = new Label("");
        nameTextField.setDebugId(PROJECT_EXTRA_NAME);        
        infoLayout.addComponent(nameTextField, COLUMN_ONE, ROW_TWO, COLUMN_TWO, ROW_TWO);
    }

    private void addStatus() {
        statusTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(statusTitle, COLUMN_ONE, ROW_THREE);
        statusLabel = new Label();
        statusLabel.setDebugId(PROJECT_EXTRA_STATUS);        
        infoLayout.addComponent(statusLabel, COLUMN_ONE, ROW_FOUR);
    }

    private void addTitle() {
        taskPrefixLabelTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(taskPrefixLabelTitle, COLUMN_TWO, ROW_THREE);
        taskPrefixLabel = new Label("", Label.CONTENT_XHTML);
        taskPrefixLabel.setDebugId(PROJECT_EXTRA_PREFIX);
        infoLayout.addComponent(taskPrefixLabel, COLUMN_TWO, ROW_FOUR);
    }

    private void addCreatedBy() {
        createdByTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(createdByTitle, COLUMN_ONE, ROW_FIVE);
        createdByLabel = new Label("");
        createdByLabel.setDebugId(PROJECT_EXTRA_CREATEDBY);        
        infoLayout.addComponent(createdByLabel, COLUMN_ONE, ROW_SIX);
    }

    private void addCreationDate() {
        creationDateTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(creationDateTitle, COLUMN_TWO, ROW_FIVE);
        creationDateLabel = new Label("");
        creationDateLabel.setDebugId(PROJECT_EXTRA_CREATIONDATE);        
        infoLayout.addComponent(creationDateLabel, COLUMN_TWO, ROW_SIX);
    }

    private void addDescription() {
        descriptionTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(descriptionTitle, COLUMN_ONE, ROW_SEVEN, COLUMN_TWO, ROW_SEVEN);
        descriptionTextField = new TextField();
        descriptionTextField.setDebugId(PROJECT_EXTRA_DESCRIPTION);        
        descriptionTextField.setSizeFull();
        descriptionTextField.setReadOnly(true);
        infoLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_EIGHT, COLUMN_TWO, ROW_EIGHT);
    }
}

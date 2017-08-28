package com.agileapex.ui.release;

import com.agileapex.common.DomainObjectHelper;
import com.agileapex.domain.Release;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

public class ReleaseExtraInfoPanel extends Panel implements Constants {
    private static final long serialVersionUID = -8650169055093801614L;
    protected final ReleasePanel releasePanel;
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
    protected Label projectTitle;
    protected Label projectTextField;

    public ReleaseExtraInfoPanel(ReleasePanel releasePanel) {
        this.releasePanel = releasePanel;
        init();
        addName();
        addProject();
        addStatus();
        addCreatedBy();
        addCreationDate();
        addDescription();
    }

    protected void viewExtraInfoLayoutTitles(boolean view) {
        if (view) {
            nameTitle.setValue("<b>Release Name</b>");
            projectTitle.setValue("<br><b>Project Name</b>");
            statusTitle.setValue("<br><b>Status</b>");
            createdByTitle.setValue("<br><b>Created By</b>");
            creationDateTitle.setValue("<br><b>Creation Date</b>");
            descriptionTitle.setValue("<br><b>Description</b>");
        } else {
            nameTitle.setValue("");
            projectTitle.setValue("");
            statusTitle.setValue("");
            createdByTitle.setValue("");
            creationDateTitle.setValue("");
            descriptionTitle.setValue("");
        }
    }

    protected void setExtraInfoLayoutData(Release release) {
        if (DomainObjectHelper.isNotNullOrZero(release)) {
            nameTextField.setValue(release.getName());
            descriptionTextField.setReadOnly(false);
            descriptionTextField.setValue(release.getDescription());
            descriptionTextField.setReadOnly(true);
            statusLabel.setValue(release.getStatus().getStatus());
            projectTextField.setValue(DomainObjectHelper.isNotNullOrZero(release.getParentProject()) ? release.getParentProject().getName() : "");
            createdByLabel.setValue(DomainObjectHelper.isNotNullOrZero(release.getCreatedBy()) ? release.getCreatedBy().getFullName() : "");
            creationDateLabel.setValue(release.getCreationDate() != null ? dateFormatter.formatToMediumDateAndTime(release.getCreationDate()) : "");
        } else {
            nameTextField.setValue("");
            descriptionTextField.setReadOnly(false);
            descriptionTextField.setValue("");
            descriptionTextField.setReadOnly(true);
            statusLabel.setValue("");
            projectTextField.setValue("");
            createdByLabel.setValue("");
            creationDateLabel.setValue("");
        }
    }

    private void init() {
        setSizeFull();
        infoLayout = new GridLayout(2, 10);
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
        infoLayout.setRowExpandRatio(ROW_TEN, EXPAND_RATIO_1000);
        setContent(infoLayout);
    }

    private void addName() {
        nameTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(nameTitle, COLUMN_ONE, ROW_ONE, COLUMN_TWO, ROW_ONE);
        nameTextField = new Label("");
        nameTextField.setDebugId(RELEASE_EXTRA_NAME);
        infoLayout.addComponent(nameTextField, COLUMN_ONE, ROW_TWO, COLUMN_TWO, ROW_TWO);
    }

    private void addProject() {
        projectTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(projectTitle, COLUMN_ONE, ROW_THREE, COLUMN_TWO, ROW_THREE);
        projectTextField = new Label("");
        projectTextField.setDebugId(RELEASE_EXTRA_PROJECT);
        infoLayout.addComponent(projectTextField, COLUMN_ONE, ROW_FOUR, COLUMN_TWO, ROW_FOUR);
    }

    private void addStatus() {
        statusTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(statusTitle, COLUMN_ONE, ROW_FIVE, COLUMN_TWO, ROW_FIVE);
        statusLabel = new Label();
        statusLabel.setDebugId(RELEASE_EXTRA_STATUS);
        infoLayout.addComponent(statusLabel, COLUMN_ONE, ROW_SIX, COLUMN_TWO, ROW_SIX);
    }

    private void addCreatedBy() {
        createdByTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(createdByTitle, COLUMN_ONE, ROW_SEVEN);
        createdByLabel = new Label("");
        createdByLabel.setDebugId(RELEASE_EXTRA_CREATEDBY);
        infoLayout.addComponent(createdByLabel, COLUMN_ONE, ROW_EIGHT);
    }

    private void addCreationDate() {
        creationDateTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(creationDateTitle, COLUMN_TWO, ROW_SEVEN);
        creationDateLabel = new Label("");
        creationDateLabel.setDebugId(RELEASE_EXTRA_CREATIONDATE);
        infoLayout.addComponent(creationDateLabel, COLUMN_TWO, ROW_EIGHT);
    }

    private void addDescription() {
        descriptionTitle = new Label("", Label.CONTENT_XHTML);
        infoLayout.addComponent(descriptionTitle, COLUMN_ONE, ROW_NINE, COLUMN_TWO, ROW_NINE);
        descriptionTextField = new TextField();
        descriptionTextField.setDebugId(RELEASE_EXTRA_DESCRIPTION);
        descriptionTextField.setSizeFull();
        descriptionTextField.setReadOnly(true);
        infoLayout.addComponent(descriptionTextField, COLUMN_ONE, ROW_TEN, COLUMN_TWO, ROW_TEN);
    }
}

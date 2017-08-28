package com.agileapex.ui.settings;

import com.agileapex.common.properties.ApplicationInternalPropertyVersionHelper;
import com.agileapex.domain.User;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

public class AboutLayout extends GridLayout implements Constants {
    private static final long serialVersionUID = 6526940941461821744L;
    protected User user;

    public AboutLayout() {
        super(1, 2);
        this.user = ApplicationSession.getUser();
        createLayout();
        ApplicationInternalPropertyVersionHelper versionHelper = new ApplicationInternalPropertyVersionHelper();
        addLabelRow("<strong>Agile Apex</strong> - Agile Project Management Tool<br>", COLUMN_ONE, ROW_ONE);
        //addLabelRow("Version " + versionHelper.getVersionText() + "<br>", COLUMN_ONE, ROW_TWO);
        addLabelRow("www.agileapex.com Copyright © 2009-2016<br>", COLUMN_ONE, ROW_TWO);
    }

    private void addLabelRow(String text, int column, int row) {
        Label textLabel = new Label(text, Label.CONTENT_XHTML);
        textLabel.setSizeFull();
        textLabel.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        addComponent(textLabel, column, row);
    }

    private void createLayout() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);
        setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        setColumnExpandRatio(COLUMN_ONE, EXPAND_RATIO_1000);
    }
}

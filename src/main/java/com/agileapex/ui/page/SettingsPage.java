package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.settings.SettingsPanel;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class SettingsPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -3032540886168773177L;

    public SettingsPage() {
        super();
        init();
        addHeaderPanel();
        addSettingsPanel();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.SETTINGS);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void addSettingsPanel() {
        SettingsPanel settingsPanel = new SettingsPanel();
        addComponent(settingsPanel);
        setExpandRatio(settingsPanel, EXPAND_RATIO_1000);
    }
}

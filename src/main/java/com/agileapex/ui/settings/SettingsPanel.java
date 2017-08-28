package com.agileapex.ui.settings;

import com.agileapex.ui.common.Constants;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

public class SettingsPanel extends Panel implements Constants {
    private static final long serialVersionUID = -6198413712641729670L;

    public SettingsPanel() {
        init();
    }

    private void init() {
        setSizeFull();
        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addTab(new UserDetailsLayout(), "User Details");
        tabs.addTab(new AboutLayout(), "About");
        setContent(tabs);
    }
}

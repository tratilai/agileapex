package com.agileapex.ui.page;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.header.HeaderLayout;
import com.agileapex.ui.release.ReleasePanel;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.VerticalLayout;

public class ReleasesPage extends VerticalLayout implements Constants {
    private static final long serialVersionUID = -628982241002180859L;

    public ReleasesPage() {
        super();
        init();
        addHeaderPanel();
        addReleasesPanel();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        ApplicationSession.getSessionDataHelper().setCurrentPage(WindowIdentification.RELEASES);
    }

    private void addHeaderPanel() {
        HeaderLayout headerPanel = new HeaderLayout();
        addComponent(headerPanel);
        setExpandRatio(headerPanel, EXPAND_RATIO_1);
    }

    private void addReleasesPanel() {
        ReleasePanel releasesPanel = new ReleasePanel();
        addComponent(releasesPanel);
        setExpandRatio(releasesPanel, EXPAND_RATIO_1000);
    }
}

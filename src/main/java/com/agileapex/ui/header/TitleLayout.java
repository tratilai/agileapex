package com.agileapex.ui.header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

public class TitleLayout extends HorizontalLayout implements Constants {
    private static final Logger logger = LoggerFactory.getLogger(TitleLayout.class);
    private static final long serialVersionUID = 4189033056011581492L;

    public TitleLayout() {
        init();
        addHeaderTitle();
        addLogoutButton();
    }

    private void init() {
        setWidth("100%");
        setHeight(32.0f, Sizeable.UNITS_PIXELS);
        setMargin(false, true, false, false);
        setSpacing(true);
    }

    private void addHeaderTitle() {
        Label titleLabel = new Label();
        titleLabel.setIcon(new ThemeResource("../apextheme/img/logo_300x30.png"));
        addComponent(titleLabel);
        setExpandRatio(titleLabel, EXPAND_RATIO_100);
    }

    private void addLogoutButton() {
        Button signOutButton = new Button("sign out");
        signOutButton.setDebugId(HEADER_SIGN_OUT);
        signOutButton.setStyleName(BaseTheme.BUTTON_LINK);
        addComponent(signOutButton);
        setComponentAlignment(signOutButton, Alignment.TOP_RIGHT);
        setExpandRatio(signOutButton, EXPAND_RATIO_1);
        signOutButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -7974434457188038753L;

            @Override
            public void buttonClick(ClickEvent event) {
                logger.info("User \"{}\" signing out.", ApplicationSession.getUser().getEmail());
                ApplicationSession.setUser(null);
                ApplicationSession.setSessionDataHelper(null);
                getApplication().close();
            }
        });
    }
}

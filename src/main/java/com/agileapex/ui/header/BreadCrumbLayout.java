package com.agileapex.ui.header;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.window.MainWindow;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

public class BreadCrumbLayout extends HorizontalLayout implements Constants {
    private static final long serialVersionUID = -2452479717391573255L;

    public BreadCrumbLayout() {
        init();
        createLocationMenu();
    }

    private void init() {
        setWidth("100%");
        setHeight(20.0f, Sizeable.UNITS_PIXELS);
        setMargin(false, true, false, true);
        setSpacing(false);
        addStyleName("v-location-header-style");
    }

    private void createLocationMenu() {
        WindowIdentification currentPage = ApplicationSession.getSessionDataHelper().getCurrentPage();
        if (currentPage.equals(WindowIdentification.RELEASES)) {
            addButtonLink("projects", BREADCRUMB_PROJECTS, WindowIdentification.PROJECTS);
            addLinkArrow();
            addLabel("releases");
        } else if (currentPage.equals(WindowIdentification.SPRINT)) {
            addButtonLink("projects", BREADCRUMB_PROJECTS, WindowIdentification.PROJECTS);
            addLinkArrow();
            addButtonLink("releases", BREADCRUMB_RELEASES, WindowIdentification.RELEASES);
            addLinkArrow();
            addLabel("sprints");
        } else if (currentPage.equals(WindowIdentification.SPRINT_PLANNING)) {
            addButtonLink("projects", BREADCRUMB_PROJECTS, WindowIdentification.PROJECTS);
            addLinkArrow();
            addButtonLink("releases", BREADCRUMB_RELEASES, WindowIdentification.RELEASES);
            addLinkArrow();
            addButtonLink("sprints", BREADCRUMB_SPRINTS, WindowIdentification.SPRINT);
            addLinkArrow();
            addLabel("sprint planning");
        }
        addExpander();
    }

    private void addLabel(String caption) {
        Label label = new Label(caption);
        label.setSizeUndefined();
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_LEFT);
    }

    private void addButtonLink(String caption, String debugId, final WindowIdentification targetPage) {
        Button button = new Button(caption);
        button.setDebugId(debugId);
        button.setStyleName(BaseTheme.BUTTON_LINK);
        addComponent(button);
        setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        button.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -1342022945087120159L;

            @Override
            public void buttonClick(ClickEvent event) {
                MainWindow mainWindow = (MainWindow) getWindow();
                ApplicationSession.getSessionDataHelper().setTargetPage(targetPage);
                mainWindow.changePage();
            }
        });
    }

    private void addLinkArrow() {
        Label label = new Label("&nbsp;->&nbsp;", Label.CONTENT_XHTML);
        label.setSizeUndefined();
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_LEFT);
    }

    private void addExpander() {
        Label emptyExpander = new Label();
        addComponent(emptyExpander);
        setComponentAlignment(emptyExpander, Alignment.MIDDLE_LEFT);
        setExpandRatio(emptyExpander, EXPAND_RATIO_1000);
    }
}

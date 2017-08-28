package com.agileapex.ui.header;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.VerticalLayout;

public class HeaderLayout extends VerticalLayout {
    private static final long serialVersionUID = 6063105703115060318L;

    public HeaderLayout() {
        init();
        addComponent(new TitleLayout());
        addComponent(new NavigatorLayout());
        addComponent(new BreadCrumbLayout());
    }

    private void init() {
        setWidth("100%");
        setHeight(90.0f, Sizeable.UNITS_PIXELS);
        setMargin(false);
        setSpacing(false);
        addStyleName("v-header-style");
    }
}

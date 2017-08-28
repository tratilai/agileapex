package com.agileapex.ui.report.project;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ProjectGeneralStatisticsReport extends VerticalLayout {
    private static final long serialVersionUID = 4477267872749065888L;

    public ProjectGeneralStatisticsReport() {
        Label label = new Label("Project General Statistics Report");
        addComponent(label);
    }
}

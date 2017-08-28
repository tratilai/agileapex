package com.agileapex.ui.report.sprint;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class SprintGeneralStatisticsReport extends VerticalLayout {
    private static final long serialVersionUID = -5466828053011672618L;

    public SprintGeneralStatisticsReport() {
        Label label = new Label("Sprint General Statistics Report");
        addComponent(label);
    }
}

package com.agileapex.ui.report;

import com.agileapex.domain.Report;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class ReportTreeValueChangeListener implements ValueChangeListener {
    private static final long serialVersionUID = -1020033731555658587L;
    private ReportsInfoPanel reportsInfoPanel;

    public ReportTreeValueChangeListener(ReportsInfoPanel reportsInfoPanel) {
        this.reportsInfoPanel = reportsInfoPanel;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        if (value != null && value instanceof Report) {
            reportsInfoPanel.changeReport((Report) value);
        }
    }
}

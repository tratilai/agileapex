package com.agileapex.ui.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Report;
import com.agileapex.domain.ReportId;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.report.project.ProjectGeneralStatisticsReport;
import com.agileapex.ui.report.sprint.SprintBurndownReport;
import com.agileapex.ui.report.sprint.SprintGeneralStatisticsReport;
import com.vaadin.ui.Panel;

public class ReportsInfoPanel extends Panel implements Constants {
    private static final long serialVersionUID = -5608046942249100955L;
    private static final Logger logger = LoggerFactory.getLogger(ReportsInfoPanel.class);

    public ReportsInfoPanel() {
        init();
    }

    private void init() {
        setSizeFull();
    }

    public void changeReport(Report reportId) {
        logger.debug("Changing report panel to: {}", reportId.reportId);
        removeAllComponents();
        if (reportId.reportId == ReportId.PROJECT_GENERAL_STATISTICS) {
            ProjectGeneralStatisticsReport report = new ProjectGeneralStatisticsReport();
            addComponent(report);
        } else if (reportId.reportId == ReportId.SPRINT_GENERAL_STATISTICS) {
            SprintGeneralStatisticsReport report = new SprintGeneralStatisticsReport();
            addComponent(report);
        } else if (reportId.reportId == ReportId.SPRINT_BURNDOWN_CHART) {
            SprintBurndownReport report = new SprintBurndownReport();
            addComponent(report);
        }
    }
}

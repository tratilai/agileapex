package com.agileapex.ui.report;

import java.util.ArrayList;
import java.util.List;

import com.agileapex.domain.Report;
import com.agileapex.domain.ReportId;
import com.agileapex.ui.common.Constants;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class ReportTreePanel extends Panel implements Constants {
    private static final long serialVersionUID = 7232308042790658296L;
    private VerticalLayout layout;
    protected Tree tree;
    private ReportsInfoPanel reportsInfoPanel;
    private List<Report> projectReports;
    private List<Report> sprintReports;

    public ReportTreePanel(ReportsInfoPanel reportsInfoPanel) {
        this.reportsInfoPanel = reportsInfoPanel;
        init();
        createTitle();
        createReportHolders();
        createReportTree();
        addComponent(this.layout);
    }

    private void createTitle() {
        Label title = new Label("Reports");
        title.setHeight("30px");
        this.layout.addComponent(title);
    }

    private void init() {
        setSizeFull();
        layout = new VerticalLayout();
    }

    private void createReportHolders() {
        projectReports = new ArrayList<Report>();
        projectReports.add(new Report(ReportId.PROJECT_GENERAL_STATISTICS));
        sprintReports = new ArrayList<Report>();
        sprintReports.add(new Report(ReportId.SPRINT_GENERAL_STATISTICS));
        sprintReports.add(new Report(ReportId.SPRINT_BURNDOWN_CHART));
    }

    private void createReportTree() {
        tree = new Tree();
        tree.setSelectable(true);
        tree.setMultiSelect(false);
        tree.setImmediate(true);
        addReportsToTree("Project", projectReports);
        addReportsToTree("Sprint", sprintReports);
        tree.addListener(new ReportTreeValueChangeListener(reportsInfoPanel));
        this.layout.addComponent(tree);
    }

    private void addReportsToTree(String caption, List<Report> reports) {
        tree.addItem(caption);
        if (reports.size() < 1) {
            tree.setChildrenAllowed(caption, false);
        }
        for (Report report : reports) {
            tree.addItem(report);
            tree.setItemCaption(report, report.reportId.getName());
            tree.setParent(report, caption);
            tree.setChildrenAllowed(report, false);
        }
        tree.expandItemsRecursively(caption);
    }
}

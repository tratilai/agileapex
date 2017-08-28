package com.agileapex.domain;

public class Report {
    public ReportId reportId;

    public Report(ReportId reportId) {
        this.reportId = reportId;
    }

    @Override
    public String toString() {
        return "[Report: " + super.toString() + " report id: " + reportId + "]";
    }
}

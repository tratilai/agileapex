package com.agileapex.domain;

public enum ReportId {

    PROJECT_GENERAL_STATISTICS("General Statistics"), SPRINT_GENERAL_STATISTICS("General Statistics"), SPRINT_BURNDOWN_CHART("Burndown Chart");

    private String name;

    private ReportId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

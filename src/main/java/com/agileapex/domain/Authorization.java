package com.agileapex.domain;

public enum Authorization {

    ADMIN("Admin"), MANAGER("Manager"), SPRINT_PLANNER("Sprint planner"), REPORTER("Reporter"), VIEWER("Viewer");
    private String name;

    private Authorization() {
    }

    private Authorization(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[Authorization: " + super.toString() + " name: " + getName() + "]";
    }

    public boolean hasAdminPrivileges() {
        return this == ADMIN;
    }

    public boolean hasManagerPrivileges() {
        return hasAdminPrivileges() || this == MANAGER;
    }

    public boolean hasSprintPlannerPrivileges() {
        return hasManagerPrivileges() || this == SPRINT_PLANNER;
    }

    public boolean hasReporterPrivileges() {
        return hasSprintPlannerPrivileges() || this == REPORTER;
    }

    public boolean hasViewerPrivileges() {
        return hasReporterPrivileges() || this == VIEWER;
    }

    public String getName() {
        return name;
    }
}

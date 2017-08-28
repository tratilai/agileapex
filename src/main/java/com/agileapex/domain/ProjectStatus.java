package com.agileapex.domain;

import java.util.Arrays;
import java.util.List;

public enum ProjectStatus {

    OPEN("Open"), CLOSED("Closed");
    private String status;

    private ProjectStatus() {
    }

    private ProjectStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[ProjectStatus: " + super.toString() + " status: " + status + "]";
    }

    public static List<ProjectStatus> getStatuses() {
        return Arrays.asList(ProjectStatus.values());
    }

    public String getStatus() {
        return status;
    }
}

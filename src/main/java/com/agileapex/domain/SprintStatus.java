package com.agileapex.domain;

import java.util.Arrays;
import java.util.List;

public enum SprintStatus {

    OPEN("Open"), CLOSED("Closed");
    private String status;

    private SprintStatus() {
    }

    private SprintStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[SprintStatus: " + super.toString() + " status: " + status + "]";
    }

    public static List<SprintStatus> getStatuses() {
        return Arrays.asList(SprintStatus.values());
    }

    public String getStatus() {
        return status;
    }
}

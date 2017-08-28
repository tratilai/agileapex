package com.agileapex.domain;

import java.util.Arrays;
import java.util.List;

public enum ReleaseStatus {

    OPEN("Open"), CLOSED("Closed");
    private String status;

    private ReleaseStatus() {
    }

    private ReleaseStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[ReleaseStatus: " + super.toString() + " status: " + status + "]";
    }

    public static List<ReleaseStatus> getStatuses() {
        return Arrays.asList(ReleaseStatus.values());
    }

    public String getStatus() {
        return status;
    }
}

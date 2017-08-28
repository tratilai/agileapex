package com.agileapex.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public enum TaskStatus implements Serializable {

    NOT_STARTED("Not started"), IMPEDED("Impeded"), IN_PROGRESS("In progress"), DONE("Done");
    private String status;

    private TaskStatus() {
    }

    private TaskStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[TaskStatus: " + super.toString() + " status: " + status + "]";
    }

    public static List<TaskStatus> getStatuses() {
        return Arrays.asList(TaskStatus.values());
    }

    public String getStatus() {
        return status;
    }
}

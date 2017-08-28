package com.agileapex.common.task;

import java.util.Comparator;

import com.agileapex.domain.Task;

public class OrderTasksByChildrenComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        return new Long(task1.getOrderInChildren()).compareTo(new Long(task2.getOrderInChildren()));
    }
}

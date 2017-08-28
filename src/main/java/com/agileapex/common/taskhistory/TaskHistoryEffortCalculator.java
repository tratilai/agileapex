package com.agileapex.common.taskhistory;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Sprint;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskAndEffort;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.persistence.SprintPersistence;
import com.agileapex.persistence.SprintPersistenceImpl;
import com.agileapex.persistence.TaskHistoryPersistence;
import com.agileapex.persistence.TaskHistoryPersistenceImpl;

public class TaskHistoryEffortCalculator {
    private static final Logger logger = LoggerFactory.getLogger(TaskHistoryEffortCalculator.class);

    /**
     * Calculates sprint's sum of efforts per date from the start of the first task history event.
     * Calculation is stopped at the end of sprint's end date. 
     * Note: this algorithm fills all dates at least with NULL effort so the returned data structure
     * is always from sprint's start date to end date.
     */
    public SortedMap<LocalDate, Long> calculateTaskAndEffortPerDateUpToSprintsEndDate(Sprint sprint) {
        logger.debug("About to calculate sum of efforts for sprint {}", sprint.getUniqueId());
        final SortedMap<LocalDate, HashMap<Long, TaskAndEffort>> taskAndEfffortsPerDate = calculateEffortsPerTaskPerDate(sprint);
        SortedMap<LocalDate, Long> efforts = calculateSumOfEffortsPerDay(taskAndEfffortsPerDate);
        logger.debug("Sum of efforts calculated for {} dates.", efforts.size());
        return efforts;
    }

    private final SortedMap<LocalDate, HashMap<Long, TaskAndEffort>> calculateEffortsPerTaskPerDate(Sprint sprint) {
        SortedMap<LocalDate, HashMap<Long, TaskAndEffort>> taskAndEfffortsPerDate = new TreeMap<LocalDate, HashMap<Long, TaskAndEffort>>();
        SprintPersistence sprintPersistence = new SprintPersistenceImpl();
        List<Task> tasks = sprintPersistence.getAllTasks(sprint);
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        LocalDate startDate = sprint.getStartDate().toLocalDate();
        LocalDate endDate = sprint.getEndDate().toLocalDate();
        initializeNullEffortsForAllDays(taskAndEfffortsPerDate, startDate, endDate);
        for (Task task : tasks) {
            List<TaskHistory> taskHistories = taskHistoryPersistence.get(task.getUniqueId());
            for (TaskHistory taskHistory : taskHistories) {
                if (taskHistory.getEvent() == TaskHistoryEvent.EFFORT_AT_CREATION || taskHistory.getEvent() == TaskHistoryEvent.EFFORT_CHANGE) {
                    LocalDate taskHistoryDate = taskHistory.getCreationDate().toLocalDate();
                    Long effort = (StringUtils.isNotEmpty(taskHistory.getValue1()) ? Long.valueOf(taskHistory.getValue1()) : null);
                    if (taskHistoryDate.isAfter(endDate)) {
                        break;
                    } else {
                        addTaskAndEffortToSpecificDate(taskAndEfffortsPerDate, task.getUniqueId(), taskHistoryDate, effort);
                        fillToTheEndOfDatesWithGivenEffort(taskAndEfffortsPerDate, task.getUniqueId(), taskHistory, effort, endDate);
                    }
                }
            }
        }
        return taskAndEfffortsPerDate;
    }

    private void initializeNullEffortsForAllDays(SortedMap<LocalDate, HashMap<Long, TaskAndEffort>> taskAndEfffortsPerDate, LocalDate startDate, LocalDate endDate) {
        LocalDate indexDate = startDate;
        while (!indexDate.isAfter(endDate)) {
            addTaskAndEffortToSpecificDate(taskAndEfffortsPerDate, null, new LocalDate(indexDate), null);
            indexDate = indexDate.plusDays(1);
        }
    }

    private void addTaskAndEffortToSpecificDate(SortedMap<LocalDate, HashMap<Long, TaskAndEffort>> dateAndEffforts, Long taskUniqueId, LocalDate taskHistoryDate, Long effort) {
        HashMap<Long, TaskAndEffort> taskAndEffortMap = dateAndEffforts.get(taskHistoryDate);
        if (taskAndEffortMap == null) {
            taskAndEffortMap = new HashMap<Long, TaskAndEffort>();
        }
        TaskAndEffort taskAndEffort = null;
        if (taskUniqueId != null && effort != null) {
            taskAndEffort = new TaskAndEffort(taskUniqueId, effort);
            taskAndEffortMap.put(taskUniqueId, taskAndEffort);
        }
        dateAndEffforts.put(taskHistoryDate, taskAndEffortMap);
    }

    private void fillToTheEndOfDatesWithGivenEffort(SortedMap<LocalDate, HashMap<Long, TaskAndEffort>> taskAndEfffortsPerDate, Long taskUniqueId, TaskHistory taskHistory, Long effort, LocalDate endDate) {
        DateTime indexDateTime = taskHistory.getCreationDate();
        LocalDate indexDate = indexDateTime.toLocalDate();
        while (indexDate.isBefore(endDate) || indexDate.isEqual(endDate)) {
            addTaskAndEffortToSpecificDate(taskAndEfffortsPerDate, taskUniqueId, indexDate, effort);
            indexDate = indexDate.plusDays(1);
        }
    }

    private SortedMap<LocalDate, Long> calculateSumOfEffortsPerDay(final SortedMap<LocalDate, HashMap<Long, TaskAndEffort>> taskAndEfffortsPerDate) {
        SortedMap<LocalDate, Long> dateAndEfforts = new TreeMap<LocalDate, Long>();
        Set<LocalDate> dateSet = taskAndEfffortsPerDate.keySet();
        for (LocalDate date : dateSet) {
            Long dateEffortSum = null;
            HashMap<Long, TaskAndEffort> taskAndEffortMap = taskAndEfffortsPerDate.get(date);
            for (TaskAndEffort taskAndEffort : taskAndEffortMap.values()) {
                if (taskAndEffort.effort != null) {
                    if (dateEffortSum == null) {
                        dateEffortSum = 0L;
                    }
                    dateEffortSum += taskAndEffort.effort;
                }
            }
            dateAndEfforts.put(date, dateEffortSum);
        }
        return dateAndEfforts;
    }
}

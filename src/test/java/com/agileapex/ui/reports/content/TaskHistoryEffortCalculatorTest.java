package com.agileapex.ui.reports.content;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.agileapex.AgileApexUnitTest;
import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.common.taskhistory.TaskHistoryEffortCalculator;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;
import com.agileapex.persistence.SprintPersistenceImpl;
import com.agileapex.persistence.TaskHistoryPersistenceImpl;

import mockit.Mock;
import mockit.MockUp;

public class TaskHistoryEffortCalculatorTest extends AgileApexUnitTest {

    private TaskHistoryEffortCalculator calculator = new TaskHistoryEffortCalculator();
    private DateAndTimeUtil dateUtil = new DateAndTimeUtil();

    @Test
    public void dynamicTestAroundCurrentRealDate() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Effort rootEffort = new Effort(1L, null, 1L);
        final Effort effort1 = new Effort(1L, 1L, null);

        final Task rootTask = createTask(1, rootEffort, TaskStatus.NOT_STARTED, user);
        final Task taskA1 = createTask(2, effort1, TaskStatus.NOT_STARTED, user);
        final List<Task> allTask = new ArrayList<Task>();
        allTask.add(taskA1);

        DateTime now = new DateTime();
        final TaskHistory taskHistory1 = new TaskHistory(1L, taskA1.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, "10", now.minusDays(2), user);
        final TaskHistory taskHistory2 = new TaskHistory(2L, taskA1.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "0", now.plusDays(2), user);
        final List<TaskHistory> taskHistoryForTaskA1 = new ArrayList<TaskHistory>();
        taskHistoryForTaskA1.add(taskHistory1);
        taskHistoryForTaskA1.add(taskHistory2);

        new MockUp<SprintPersistenceImpl>() {
            @Mock
            List<Task> getAllTasks(Sprint sprint) {
                return allTask;
            }
        };
        new MockUp<TaskHistoryPersistenceImpl>() {
            @Mock
            public List<TaskHistory> get(Long taskUniqueId) {
                return taskHistoryForTaskA1;
            }
        };

        Sprint sprint = new Sprint(1L, rootTask, "Sprint 1", now.minusDays(3), now.plusDays(3), user);
        SortedMap<LocalDate, Long> result = calculator.calculateTaskAndEffortPerDateUpToSprintsEndDate(sprint);
        assertTrue("Invalid result size. Is " + result.size() + " should be 7", result.size() == 7);
        Object[] values = result.values().toArray();

        assertTrue("Invalid effort sum", values[values.length - 7] == null);
        assertTrue("Invalid effort sum", (Long) values[values.length - 6] == 10);
        assertTrue("Invalid effort sum", (Long) values[values.length - 5] == 10);
        assertTrue("Invalid effort sum", (Long) values[values.length - 4] == 10);
        assertTrue("Invalid effort sum", (Long) values[values.length - 3] == 10);
        assertTrue("Invalid effort sum", (Long) values[values.length - 2] == 0);
        assertTrue("Invalid effort sum", (Long) values[values.length - 1] == 0);
    }

    @Test
    public void multipleTaskHistoryEvents() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Effort effort = new Effort(1L, null, 1L);

        final Task rootTask = createTask(1, effort, TaskStatus.NOT_STARTED, user);
        final Task task1 = createTask(2, effort, TaskStatus.IMPEDED, user);
        final Task task2 = createTask(3, effort, TaskStatus.DONE, user);
        final Task task3 = createTask(4, effort, TaskStatus.IN_PROGRESS, user);

        final List<Task> allTask = new ArrayList<Task>();
        allTask.add(task1);
        allTask.add(task2);
        allTask.add(task3);

        final TaskHistory taskHistory1 = new TaskHistory(1L, task1.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, "10", new DateTime(2014, 1, 1, 0, 1), user);
        final TaskHistory taskHistory2 = new TaskHistory(2L, task1.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "0", new DateTime(2014, 3, 2, 12, 30), user);
        final List<TaskHistory> taskHistoryForTask1 = new ArrayList<TaskHistory>();
        taskHistoryForTask1.add(taskHistory1);
        taskHistoryForTask1.add(taskHistory2);

        final TaskHistory taskHistory3 = new TaskHistory(3L, task2.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, "10", new DateTime(2014, 3, 2, 0, 1), user);
        final TaskHistory taskHistory4 = new TaskHistory(4L, task2.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "8", new DateTime(2014, 3, 4, 11, 11), user);
        final TaskHistory taskHistory5 = new TaskHistory(5L, task2.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "20", new DateTime(2014, 3, 5, 11, 11), user);
        final TaskHistory taskHistory6 = new TaskHistory(6L, task2.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "5", new DateTime(2014, 3, 7, 11, 11), user);
        final TaskHistory taskHistory7 = new TaskHistory(7L, task2.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "0", new DateTime(2014, 3, 9, 11, 11), user);
        final List<TaskHistory> taskHistoryForTask2 = new ArrayList<TaskHistory>();
        taskHistoryForTask2.add(taskHistory3);
        taskHistoryForTask2.add(taskHistory4);
        taskHistoryForTask2.add(taskHistory5);
        taskHistoryForTask2.add(taskHistory6);
        taskHistoryForTask2.add(taskHistory7);

        final TaskHistory taskHistory10 = new TaskHistory(10L, task3.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, "0", new DateTime(2014, 3, 4, 0, 1), user);
        final TaskHistory taskHistory11 = new TaskHistory(11L, task3.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "7", new DateTime(2014, 3, 5, 12, 30), user);
        final TaskHistory taskHistory12 = new TaskHistory(12L, task3.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, "0", new DateTime(2014, 3, 7, 12, 30), user);
        final List<TaskHistory> taskHistoryForTask3 = new ArrayList<TaskHistory>();
        taskHistoryForTask3.add(taskHistory10);
        taskHistoryForTask3.add(taskHistory11);
        taskHistoryForTask3.add(taskHistory12);

        new MockUp<SprintPersistenceImpl>() {
            @Mock
            List<Task> getAllTasks(Sprint sprint) {
                return allTask;
            }
        };
        new MockUp<TaskHistoryPersistenceImpl>() {
            @Mock
            public List<TaskHistory> get(Long taskUniqueId) {
                if (taskUniqueId == 2) {
                    return taskHistoryForTask1;
                } else if (taskUniqueId == 3) {
                    return taskHistoryForTask2;
                } else if (taskUniqueId == 4) {
                    return taskHistoryForTask3;
                }
                return null;
            }
        };

        Sprint sprint = new Sprint(1L, rootTask, "Sprint 1", new DateTime(2014, 3, 3, 0, 1), new DateTime(2014, 3, 8, 23, 59), user);
        SortedMap<LocalDate, Long> result = calculator.calculateTaskAndEffortPerDateUpToSprintsEndDate(sprint);
        assertTrue("Invalid result size. Is " + result.size() + " should be 67", result.size() == 67);

        Object[] values = result.values().toArray();
        assertTrue("Invalid effort sum", (Long) values[values.length - 6] == 10);
        assertTrue("Invalid effort sum", (Long) values[values.length - 5] == 8);
        assertTrue("Invalid effort sum", (Long) values[values.length - 4] == 27);
        assertTrue("Invalid effort sum", (Long) values[values.length - 3] == 27);
        assertTrue("Invalid effort sum", (Long) values[values.length - 2] == 5);
        assertTrue("Invalid effort sum", (Long) values[values.length - 1] == 5);
    }

    @Test
    public void testSprintWithOnlyOneTaskHistoryEvent() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Effort rootEffort = new Effort(1L, null, 1L);
        final Effort effort1 = new Effort(1L, 1L, null);

        final Task rootTask = createTask(1, rootEffort, TaskStatus.NOT_STARTED, user);
        final Task taskA1 = createTask(2, effort1, TaskStatus.NOT_STARTED, user);
        final List<Task> allTask = new ArrayList<Task>();
        allTask.add(taskA1);

        final TaskHistory taskHistory1 = new TaskHistory(1L, taskA1.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, "" + taskA1.getEffort().getEffortLeft(), new DateTime(2014, 1, 1, 0, 1), user);
        final List<TaskHistory> taskHistoryForTaskA1 = new ArrayList<TaskHistory>();
        taskHistoryForTaskA1.add(taskHistory1);

        new MockUp<SprintPersistenceImpl>() {
            @Mock
            List<Task> getAllTasks(Sprint sprint) {
                return allTask;
            }
        };
        new MockUp<TaskHistoryPersistenceImpl>() {
            @Mock
            public List<TaskHistory> get(Long taskUniqueId) {
                return taskHistoryForTaskA1;
            }
        };

        Sprint sprint = new Sprint(1L, rootTask, "Sprint 1", new DateTime(2014, 1, 1, 0, 1), new DateTime(2014, 1, 10, 23, 59), user);
        SortedMap<LocalDate, Long> result = calculator.calculateTaskAndEffortPerDateUpToSprintsEndDate(sprint);
        assertTrue("Invalid result size. Is " + result.size() + " should be 10", result.size() == 10);
        Set<LocalDate> dates = result.keySet();
        for (LocalDate date : dates) {
            assertTrue("Invalid effort sum for date " + dateUtil.formatToMediumDate(date), result.get(date) == 1);
        }
    }

    @Test
    public void testSprintWithNoTaskHistoryEvents() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Effort rootEffort = new Effort(1L, null, 1L);

        final Task rootTask = createTask(1, rootEffort, TaskStatus.NOT_STARTED, user);

        new MockUp<SprintPersistenceImpl>() {
            @Mock
            List<Task> getAllTasks(Sprint sprint) {
                return new ArrayList<Task>();
            }
        };
        new MockUp<TaskHistoryPersistenceImpl>() {
            @Mock
            public List<TaskHistory> get(Long taskUniqueId) {
                return new ArrayList<TaskHistory>();
            }
        };

        Sprint sprint = new Sprint(1L, rootTask, "Sprint 1", new DateTime(2014, 1, 1, 0, 1), new DateTime(2014, 1, 9, 23, 59), user);
        SortedMap<LocalDate, Long> result = calculator.calculateTaskAndEffortPerDateUpToSprintsEndDate(sprint);
        assertTrue("Invalid result size. Is " + result.size() + " should be 9", result.size() == 9);

        Object[] values = result.values().toArray();
        for (Object value : values) {
            assertTrue("Invalid effort sum, should be null, is " + value, value == null);
        }
    }
}

package com.agileapex.persistence;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.agileapex.AgileApexUnitTest;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;
import com.agileapex.persistence.dao.TaskHistoryDao;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

public class TaskHistoryPersistenceImplTest extends AgileApexUnitTest {

    @Test
    public void shouldCreateNewTaskHistoryFromFirstEffortChange() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        final Effort hiddenRootEffort = new Effort(1L, null, null);
        final Effort task1OldEffort = new Effort(2L, null, null);
        final Effort task1NewEffort = new Effort(3L, null, 52L);
        final Task hiddenRootTask = createTask(1, hiddenRootEffort, TaskStatus.DONE, user);
        final Task task1 = createTask(2, task1OldEffort, TaskStatus.NOT_STARTED, user);
        task1.setParent(hiddenRootTask);

        new MockUp<TaskHistoryDao>() {

            @Mock(invocations = 1)
            public List<TaskHistory> getAll(long parentTaskUniqueId) {
                List<TaskHistory> list = new ArrayList<TaskHistory>();
                return list;
            }

            @Mock(invocations = 1)
            public void create(Invocation invocation, TaskHistory taskHistory) {
                assertTrue("Task history event invalid", taskHistory.getEvent() == TaskHistoryEvent.EFFORT_AT_CREATION);
                assertTrue("Task history parent task unique id invalid", taskHistory.getParentTaskUniqueId() == 2L);
                assertTrue("Task history value 1 invalid", taskHistory.getValue1() == null);
                assertTrue("Task history value 2 invalid", taskHistory.getValue2().equals("52"));
            }
        };

        taskHistoryPersistence.createChange(task1NewEffort, task1, user);
    }

    @Test
    public void shouldInsertNewTaskHistoryFromEffortChange() {
        final User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        final Effort hiddenRootEffort = new Effort(1L, null, null);
        final Effort task1OldEffort = new Effort(2L, null, null);
        final Effort task1NewEffort = new Effort(3L, 666L, null);
        final Task hiddenRootTask = createTask(1, hiddenRootEffort, TaskStatus.DONE, user);
        final Task task1 = createTask(2, task1OldEffort, TaskStatus.NOT_STARTED, user);
        task1.setParent(hiddenRootTask);

        new MockUp<TaskHistoryDao>() {

            @Mock(invocations = 1)
            public List<TaskHistory> getAll(long parentTaskUniqueId) {
                List<TaskHistory> list = new ArrayList<TaskHistory>();
                TaskHistory taskHistory = new TaskHistory(user);
                taskHistory.setEvent(TaskHistoryEvent.EFFORT_AT_CREATION);
                taskHistory.setParentTaskUniqueId(task1.getUniqueId());
                taskHistory.setValue1(task1OldEffort.getEffortLeft() != null ? "" + task1OldEffort.getEffortLeft() : null);
                taskHistory.setValue2(task1OldEffort.getSumOfEffortLeft() != null ? "" + task1OldEffort.getSumOfEffortLeft() : null);
                list.add(taskHistory);
                return list;
            }

            @Mock(invocations = 2)
            public void create(Invocation invocation, TaskHistory taskHistory) {
                if (invocation.getInvocationCount() == 1) {
                    assertTrue("Task history event invalid", taskHistory.getEvent() == TaskHistoryEvent.EFFORT_AT_CREATION);
                    assertTrue("Task history parent task unique id invalid", taskHistory.getParentTaskUniqueId() == 2L);
                    assertTrue("Task history value 1 invalid", taskHistory.getValue1() == null);
                    assertTrue("Task history value 2 invalid", taskHistory.getValue2() == null);
                } else {
                    assertTrue("Task history event invalid", taskHistory.getEvent() == TaskHistoryEvent.EFFORT_CHANGE);
                    assertTrue("Task history parent task unique id invalid", taskHistory.getParentTaskUniqueId() == 2L);
                    assertTrue("Task history value 1 invalid", taskHistory.getValue1().equals("666"));
                    assertTrue("Task history value 2 invalid", taskHistory.getValue2() == null);
                }
            }
        };

        taskHistoryPersistence.createFirst(task1, user);
        task1.setEffort(task1NewEffort);
        taskHistoryPersistence.createChange(task1NewEffort, task1, user);
    }

    @Test
    public void shouldNotInsertNewTaskHistoryForHiddenRoot() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        final Effort hiddenRootEffort = new Effort(1L, null, null);
        final Effort newEffort = new Effort(3L, 666L, null);
        final Task hiddenRootTask = createTask(1, hiddenRootEffort, TaskStatus.DONE, user);
        new MockUp<TaskHistoryDao>() {
            @Mock(invocations = 0)
            public void create(TaskHistory taskHistory) {
            }
        };
        taskHistoryPersistence.createChange(newEffort, hiddenRootTask, user);
    }

    @Test
    public void shouldCreateTaskHistory() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        final Effort hiddenRootEffort = new Effort(1L, null, null);
        final Effort task1Effort = new Effort(2L, null, null);
        final Task hiddenRootTask = createTask(1, hiddenRootEffort, TaskStatus.DONE, user);
        final Task task1 = createTask(2, task1Effort, TaskStatus.NOT_STARTED, user);
        task1.setParent(hiddenRootTask);

        new MockUp<TaskHistoryDao>() {
            @Mock(invocations = 1)
            public void create(TaskHistory taskHistory) {
                assertTrue("Task history event invalid", taskHistory.getEvent() == TaskHistoryEvent.EFFORT_AT_CREATION);
                assertTrue("Task history parent task unique id invalid", taskHistory.getParentTaskUniqueId() == 2L);
                assertTrue("Task history value 1 invalid", taskHistory.getValue1() == null);
                assertTrue("Task history value 2 invalid", taskHistory.getValue2() == null);
            }
        };

        taskHistoryPersistence.createFirst(task1, user);
    }

    @Test
    public void shouldNotCreateTaskHistoryForHiddenRoot() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        final Effort hiddenRootEffort = new Effort(1L, null, null);
        final Task hiddenRootTask = createTask(1, hiddenRootEffort, TaskStatus.DONE, user);
        new MockUp<TaskHistoryDao>() {
            @Mock(invocations = 0)
            public void create(TaskHistory taskHistory) {
            }
        };
        taskHistoryPersistence.createFirst(hiddenRootTask, user);
    }
}

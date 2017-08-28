package com.agileapex.ui.common.treetable;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.agileapex.AgileApexUnitTest;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;
import com.agileapex.persistence.EffortPersistenceImpl;
import com.agileapex.persistence.TaskPersistenceImpl;

import mockit.Mock;
import mockit.MockUp;

public class TaskTreeTableTest extends AgileApexUnitTest {

    @Before
    public void init() {
        super.init();
    }

    @Test
    public void simpleTaskTreeShouldRefreshAndCalculateEfforts() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        TaskTreeTable taskTreeTable = new TaskTreeTable("id", "caption");
        final Effort effort1 = new Effort(1L, 100L, 44L);
        final Effort effort2 = new Effort(2L, 55L, 33L);
        final Effort effort3 = new Effort(3L, 20L, 30L);
        final Task task1 = createTask(1, effort1, TaskStatus.NOT_STARTED, user);
        final Task task2 = createTask(2, effort2, TaskStatus.NOT_STARTED, user);
        final Task task3 = createTask(3, effort3, TaskStatus.IMPEDED, user);
        final List<Task> task1Children = createChildrenList(task2, task3);
        task1.setChildren(task1Children);
        task2.setParent(task1);
        task3.setParent(task2);
        taskTreeTable.setRootTask(task1);
        new TaskHistoryPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return task1;
                case 2:
                    return task2;
                default:
                    return task3;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return task1Children;
                }
                return new ArrayList<Task>();
            }
        };
        new MockUp<EffortPersistenceImpl>() {
            @Mock
            public Effort get(long uniqueId) {
                switch (new Long(uniqueId).intValue()) {
                case 1:
                    return effort1;
                case 2:
                    return effort2;
                case 3:
                    return effort3;
                default:
                    return null;
                }
            }
        };
        taskTreeTable.refresh();
        assertTrue("Task1 effort invalid", isEffortValid(task1, null, 75L));
        assertTrue("Task2 effort invalid", isEffortValid(task2, 55L, null));
        assertTrue("Task3 effort invalid", isEffortValid(task3, 20L, null));
    }

    @Test
    public void oneTaskShouldNotChange() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        TaskTreeTable taskTreeTable = new TaskTreeTable("id", "caption");
        final Effort effort1 = new Effort(1L, 100L, null);
        final Task rootTask = createTask(1, effort1, TaskStatus.NOT_STARTED, user);
        taskTreeTable.setRootTask(rootTask);
        taskTreeTable.refresh();
        assertTrue("Root task effort invalid", isEffortValid(rootTask, 100L, null));
    }

    @Test
    public void nullTest() {
        TaskTreeTable taskTreeTable = new TaskTreeTable("id", "caption");
        taskTreeTable.setRootTask(null);
        taskTreeTable.refresh();
    }
}

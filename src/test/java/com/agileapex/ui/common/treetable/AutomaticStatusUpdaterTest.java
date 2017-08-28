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
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;

import mockit.Mock;
import mockit.MockUp;

public class AutomaticStatusUpdaterTest extends AgileApexUnitTest {

    private AutomaticStatusUpdater statusUpdater = new AutomaticStatusUpdater();
    private Effort nullEffort = new Effort(null, null);

    @Before
    public void init() {
        super.init();
        nullEffort.setUniqueId(3L);
    }

    @Test
    public void firstLevelStatusChangePropagatesTolowestLevel() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Effort effort1 = new Effort(1L, null, 12L);
        final Effort effort2 = new Effort(2L, null, 9L);
        final Effort effort3 = new Effort(3L, 3L, null);
        final Effort effort4 = new Effort(4L, 4L, null);
        final Effort effort5 = new Effort(5L, 5L, null);
        final Task rootTask = createTask(1, effort1, TaskStatus.DONE, user);
        final Task taskA1 = createTask(2, effort2, TaskStatus.NOT_STARTED, user);
        final Task taskA2 = createTask(3, effort3, TaskStatus.NOT_STARTED, user);
        final Task taskB1 = createTask(4, effort4, TaskStatus.NOT_STARTED, user);
        final Task taskB2 = createTask(5, effort5, TaskStatus.NOT_STARTED, user);
        taskA1.setParent(rootTask);
        taskA2.setParent(rootTask);
        taskB1.setParent(taskA1);
        taskB2.setParent(taskA1);
        final List<Task> rootChildren = createChildrenList(taskA1, taskA2);
        final List<Task> taskA1Children = createChildrenList(taskB1, taskB2);
        rootTask.setChildren(rootChildren);
        taskA1.setChildren(taskA1Children);
        new CustomerPersistenceImplMock();
        new TaskHistoryPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return rootTask;
                case 2:
                    return taskA1;
                case 3:
                    return taskA2;
                case 4:
                    return taskB1;
                default:
                    return taskB2;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return rootChildren;
                } else if (parentTaskUniqueId == 2) {
                    return taskA1Children;
                }
                return new ArrayList<Task>();
            }

            @Mock
            public void update(Task updateTask) {
                if (updateTask.getStatus() == TaskStatus.DONE) {
                    switch (new Long(updateTask.getUniqueId()).intValue()) {
                    case 1:
                        effort1.setToZero();
                        break;
                    case 2:
                        effort2.setToZero();
                        break;
                    case 3:
                        effort3.setToZero();
                        break;
                    case 4:
                        effort4.setToZero();
                        break;
                    case 5:
                        effort5.setToZero();
                        break;
                    }
                }
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
                case 4:
                    return effort4;
                case 5:
                    return effort5;
                default:
                    return null;
                }
            }
        };
        statusUpdater.updateTaskTreeStatuses(rootTask, false);
        assertTrue("Root task status invalid", rootTask.getStatus() == TaskStatus.DONE);
        assertTrue("Task A1 status invalid", taskA1.getStatus() == TaskStatus.DONE);
        assertTrue("Task A2 status invalid", taskA2.getStatus() == TaskStatus.DONE);
        assertTrue("Task B1 status invalid", taskB1.getStatus() == TaskStatus.DONE);
        assertTrue("Task B2 status invalid", taskB2.getStatus() == TaskStatus.DONE);
        assertTrue("Root task effort invalid", isEffortDetailsValid(rootTask, null, 0L));
        assertTrue("Task A1 effort invalid", isEffortDetailsValid(taskA1, null, 0L));
        assertTrue("Task A2 effort invalid", isEffortDetailsValid(taskA2, 0L, null));
        assertTrue("Task B1 effort invalid", isEffortDetailsValid(taskB1, 0L, null));
        assertTrue("Task B2 effort invalid", isEffortDetailsValid(taskB2, 0L, null));
    }

    @Test
    public void deletingSecondLevelTaskShouldChangeFirstLevelStatus() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Effort effort1 = new Effort(1L, null, 5L);
        final Effort effort2 = new Effort(2L, 2L, null);
        final Effort effort3 = new Effort(3L, 3L, null);
        final Task rootTask = createTask(1, effort1, TaskStatus.IN_PROGRESS, user);
        final Task taskA1 = createTask(2, effort2, TaskStatus.DONE, user);
        final Task taskA2 = createTask(3, effort3, TaskStatus.NOT_STARTED, user);
        taskA1.setParent(rootTask);
        taskA2.setParent(rootTask);
        List<Task> rootChildren = createChildrenList(taskA1, taskA2);
        rootTask.setChildren(rootChildren);
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        new CustomerPersistenceImplMock();
        new TaskHistoryPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return rootTask;
                case 2:
                    return taskA1;
                default:
                    return taskA2;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return createChildrenList(taskA1);
                }
                return new ArrayList<Task>();
            }

            @Mock
            public void delete(Task task) {
                taskA2.setParent(null);

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
                default:
                    return effort3;
                }
            }
        };
        taskDbService.delete(taskA2);
        statusUpdater.updateTaskTreeStatuses(rootTask, true);
        assertTrue("Root task status invalid", rootTask.getStatus() == TaskStatus.DONE);
        assertTrue("Task A1 status invalid", taskA1.getStatus() == TaskStatus.DONE);
        assertTrue("Root task effort invalid", isEffortDetailsValid(rootTask, null, 0L));
        assertTrue("Task A1 effort invalid", taskA1.getEffort().getEffortLeft() == effort2.getEffortLeft());
    }

    @Test
    public void addingNewTaskToThirdLevelShouldChangeFirstLevelStatus() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Task rootTask = createTask(1, nullEffort, TaskStatus.DONE, user);
        final Task taskA1 = createTask(2, nullEffort, TaskStatus.DONE, user);
        final Task taskA2 = createTask(3, nullEffort, TaskStatus.DONE, user);
        final Task taskB1 = createTask(4, nullEffort, TaskStatus.DONE, user);
        final Task taskB2 = createTask(5, nullEffort, TaskStatus.DONE, user);
        final Task taskB3 = createTask(6, nullEffort, TaskStatus.NOT_STARTED, user);
        taskA1.setParent(rootTask);
        taskA2.setParent(rootTask);
        taskB1.setParent(taskA1);
        taskB2.setParent(taskA2);
        taskB3.setParent(taskA2);
        final List<Task> rootChildren = createChildrenList(taskA1, taskA2);
        final List<Task> taskA1Children = createChildrenList(taskB1);
        final List<Task> taskA2Children = createChildrenList(taskB2, taskB3);
        rootTask.setChildren(rootChildren);
        taskA1.setChildren(taskA1Children);
        taskA2.setChildren(taskA2Children);
        new CustomerPersistenceImplMock();
        new TaskHistoryPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return rootTask;
                case 2:
                    return taskA1;
                case 3:
                    return taskA2;
                case 4:
                    return taskB1;
                case 5:
                    return taskB2;
                default:
                    return taskB3;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return rootChildren;
                } else if (parentTaskUniqueId == 2) {
                    return taskA1Children;
                } else if (parentTaskUniqueId == 3) {
                    return taskA2Children;
                }
                return new ArrayList<Task>();
            }
        };
        statusUpdater.updateTaskTreeStatuses(taskB2, false);
        assertTrue("Root task status invalid", rootTask.getStatus() == TaskStatus.IN_PROGRESS);
        assertTrue("Task A1 status invalid", taskA1.getStatus() == TaskStatus.DONE);
        assertTrue("Task A2 status invalid", taskA2.getStatus() == TaskStatus.IN_PROGRESS);
        assertTrue("Task B1 status invalid", taskA1.getStatus() == TaskStatus.DONE);
        assertTrue("Task B2 status invalid", taskB2.getStatus() == TaskStatus.DONE);
        assertTrue("Task B3 status invalid", taskB3.getStatus() == TaskStatus.NOT_STARTED);
    }

    @Test
    public void thirdLevelStatusChangeToDoneShouldChangeFirstLevelStatusFromNotStartedToInProgres() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Task rootTask = createTask(1, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskA1 = createTask(2, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskA2 = createTask(3, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskB1 = createTask(4, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskB2 = createTask(5, nullEffort, TaskStatus.DONE, user);
        final Task taskC1 = createTask(6, nullEffort, TaskStatus.NOT_STARTED, user);
        taskA1.setParent(rootTask);
        taskA2.setParent(rootTask);
        taskB1.setParent(taskA1);
        taskB2.setParent(taskA2);
        taskC1.setParent(taskB2);
        final List<Task> rootChildren = createChildrenList(taskA1, taskA2);
        final List<Task> taskA1Children = createChildrenList(taskB1);
        final List<Task> taskA2Children = createChildrenList(taskB2);
        final List<Task> taskB2Children = createChildrenList(taskC1);
        rootTask.setChildren(rootChildren);
        taskA1.setChildren(taskA1Children);
        taskA2.setChildren(taskA2Children);
        taskB2.setChildren(taskB2Children);
        new CustomerPersistenceImplMock();
        new TaskHistoryPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return rootTask;
                case 2:
                    return taskA1;
                case 3:
                    return taskA2;
                case 4:
                    return taskB1;
                case 5:
                    return taskB2;
                default:
                    return taskC1;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return rootChildren;
                } else if (parentTaskUniqueId == 2) {
                    return taskA1Children;
                } else if (parentTaskUniqueId == 3) {
                    return taskA2Children;
                } else if (parentTaskUniqueId == 5) {
                    return taskB2Children;
                }
                return new ArrayList<Task>();
            }
        };
        statusUpdater.updateTaskTreeStatuses(taskB2, false);
        assertTrue("Root task status invalid", rootTask.getStatus() == TaskStatus.IN_PROGRESS);
        assertTrue("Task A1 status invalid", taskA1.getStatus() == TaskStatus.NOT_STARTED);
        assertTrue("Task A2 status invalid", taskA2.getStatus() == TaskStatus.DONE);
        assertTrue("Task B1 status invalid", taskA1.getStatus() == TaskStatus.NOT_STARTED);
        assertTrue("Task B2 status invalid", taskB2.getStatus() == TaskStatus.DONE);
        assertTrue("Task C1 status invalid", taskC1.getStatus() == TaskStatus.DONE);
    }

    @Test
    public void thirdLevelStatusChangePropagatesToFirstLevel() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Task rootTask = createTask(1, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskA1 = createTask(2, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskA2 = createTask(3, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskB1 = createTask(4, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task taskB2 = createTask(5, nullEffort, TaskStatus.IMPEDED, user);
        taskA1.setParent(rootTask);
        taskA2.setParent(rootTask);
        taskB1.setParent(taskA1);
        taskB2.setParent(taskA1);
        final List<Task> rootChildren = createChildrenList(taskA1, taskA2);
        final List<Task> taskA1Children = createChildrenList(taskB1, taskB2);
        rootTask.setChildren(rootChildren);
        taskA1.setChildren(taskA1Children);
        new CustomerPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return rootTask;
                case 2:
                    return taskA1;
                case 3:
                    return taskA2;
                case 4:
                    return taskB1;
                default:
                    return taskB2;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return rootChildren;
                } else if (parentTaskUniqueId == 2) {
                    return taskA1Children;
                }
                return new ArrayList<Task>();
            }
        };
        statusUpdater.updateTaskTreeStatuses(taskB2, false);
        assertTrue("Root task status invalid", rootTask.getStatus() == TaskStatus.IMPEDED);
        assertTrue("Task A1 status invalid", taskA1.getStatus() == TaskStatus.IMPEDED);
        assertTrue("Task A2 status invalid", taskA2.getStatus() == TaskStatus.NOT_STARTED);
        assertTrue("Task B1 status invalid", taskB1.getStatus() == TaskStatus.NOT_STARTED);
        assertTrue("Task B2 status invalid", taskB2.getStatus() == TaskStatus.IMPEDED);
    }

    @Test
    public void differentChildStatusChangesTheParentStatus() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Task task1 = createTask(1, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task task2 = createTask(2, nullEffort, TaskStatus.IMPEDED, user);
        task2.setParent(task1);
        final List<Task> children = createChildrenList(task2);
        task1.setChildren(children);
        new CustomerPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return task1;
                default:
                    return task2;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return children;
                }
                return new ArrayList<Task>();
            }
        };
        statusUpdater.updateTaskTreeStatuses(task2, false);
        assertTrue("Task 1 status invalid", task1.getStatus() == TaskStatus.IMPEDED);
        assertTrue("Task 2 status invalid", task2.getStatus() == TaskStatus.IMPEDED);
    }

    @Test
    public void sameChildStatusDoesNotChangeParentStatus() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Task task1 = createTask(1, nullEffort, TaskStatus.NOT_STARTED, user);
        final Task task2 = createTask(2, nullEffort, TaskStatus.NOT_STARTED, user);
        task2.setParent(task1);
        final List<Task> children = createChildrenList(task2);
        task1.setChildren(children);
        new CustomerPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                switch (uniqueId.intValue()) {
                case 1:
                    return task1;
                default:
                    return task2;
                }
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                if (parentTaskUniqueId == 1) {
                    return children;
                }
                return new ArrayList<Task>();
            }
        };

        statusUpdater.updateTaskTreeStatuses(task2, false);
        assertTrue("Task 1 status invalid", task1.getStatus() == TaskStatus.NOT_STARTED);
        assertTrue("Task 2 status invalid", task2.getStatus() == TaskStatus.NOT_STARTED);
    }

    @Test
    public void noActionWhenJustOneStatus() {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        final Task task = createTask(1, nullEffort, TaskStatus.NOT_STARTED, user);
        AutomaticStatusUpdater statusUpdater = new AutomaticStatusUpdater();
        new CustomerPersistenceImplMock();
        new MockUp<TaskPersistenceImpl>() {
            @Mock
            Task get(Long uniqueId) {
                return task;
            }

            @Mock
            List<Task> getChildren(long parentTaskUniqueId) {
                return new ArrayList<Task>();
            }
        };
        statusUpdater.updateTaskTreeStatuses(task, false);
        assertTrue("Task status invalid", task.getStatus() == TaskStatus.NOT_STARTED);
    }

    @Test
    public void nullCaseTest() {
        AutomaticStatusUpdater statusUpdater = new AutomaticStatusUpdater();
        statusUpdater.updateTaskTreeStatuses(null, false);
    }

    private boolean isEffortDetailsValid(Task task, Long effortLeft, Long sumOfEffortLeft) {
        return task.getEffort().getSumOfEffortLeft() == sumOfEffortLeft && task.getEffort().getEffortLeft() == effortLeft;
    }
}

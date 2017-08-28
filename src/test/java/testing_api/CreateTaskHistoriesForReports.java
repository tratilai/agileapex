package testing_api;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import testing_api.persistence.EffortTestPersistence;
import testing_api.persistence.ProjectTestPersistence;
import testing_api.persistence.ReleaseTestPersistence;
import testing_api.persistence.SprintTestPersistence;
import testing_api.persistence.TaskHistoryTestPersistence;
import testing_api.persistence.TaskTestPersistence;
import testing_api.persistence.UserTestPersistence;

import com.agileapex.domain.Effort;
import com.agileapex.domain.Project;
import com.agileapex.domain.ProjectStatus;
import com.agileapex.domain.Release;
import com.agileapex.domain.ReleaseStatus;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.SprintStatus;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.SequencerPersistence;

public class CreateTaskHistoriesForReports {
    private static final Logger logger = LoggerFactory.getLogger(CreateTaskHistoriesForReports.class);

    public static void main(String[] args) {
        logger.info("Started.");
        TestingApiDatabaseUtils dbUtils = new TestingApiDatabaseUtils();
        dbUtils.initialize();
        CreateTaskHistoriesForReports creator = new CreateTaskHistoriesForReports();
        creator.createTaskHistories();
        logger.info("Ended.");
    }

    private void createTaskHistories() {
        UserTestPersistence userTestPersistence = new UserTestPersistence();
        User adminUser = userTestPersistence.get("admin");

        DateTime creationTime = new DateTime(2014, 1, 1, 13, 44);
        Project project = new Project(0, "Test API project 1", "", ProjectStatus.OPEN, adminUser, "");
        ProjectTestPersistence projectTestDao = new ProjectTestPersistence();
        projectTestDao.create(project, creationTime, adminUser, creationTime, adminUser);

        Release release = new Release(0, project, "Test API release 1", "", ReleaseStatus.OPEN, null, adminUser);
        ReleaseTestPersistence releaseTestDao = new ReleaseTestPersistence();
        releaseTestDao.create(release, creationTime, adminUser, creationTime, adminUser);

        TaskTestPersistence taskTestPersistence = new TaskTestPersistence();
        Task rootTask = taskTestPersistence.createRootTask(SequencerPersistence.KEY_PREFIX_FOR_ROOT, creationTime, adminUser, creationTime, adminUser);

        DateTime sprintStartDate = new DateTime(2014, 1, 10, 0, 0);
        DateTime sprintEndDate = new DateTime(2014, 1, 20, 23, 59);
        Sprint sprint = new Sprint(1L, rootTask, release, "Test API sprint 1", "", SprintStatus.OPEN, sprintStartDate, sprintEndDate, adminUser);
        SprintTestPersistence sprintTestPersistence = new SprintTestPersistence();
        sprintTestPersistence.create(sprint, creationTime, adminUser, creationTime, adminUser);

        Task task1 = createTask(1, new Effort(0, 10L, null), new DateTime(2014, 1, 1, 18, 35), adminUser, rootTask);
        Task task2 = createTask(2, new Effort(0, 10L, null), new DateTime(2014, 1, 9, 18, 50), adminUser, rootTask);
        Task task3 = createTask(3, new Effort(0, 5L, null), new DateTime(2014, 1, 9, 11, 22), adminUser, rootTask);
        Task task4 = createTask(4, new Effort(0, 2L, null), new DateTime(2014, 1, 11, 4, 33), adminUser, rootTask);

        TaskHistoryTestPersistence taskHistoryTestPersistence = new TaskHistoryTestPersistence();
        TaskHistory taskHistory1 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 7L, null), task1.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory1, new DateTime(2014, 1, 7, 10, 10), adminUser, new DateTime(2014, 1, 7, 10, 10), adminUser);

        TaskHistory taskHistory2 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 3L, null), task1.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory2, new DateTime(2014, 1, 8, 12, 12), adminUser, new DateTime(2014, 1, 8, 12, 12), adminUser);

        TaskHistory taskHistory3 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 4L, null), task2.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory3, new DateTime(2014, 1, 10, 4, 44), adminUser, new DateTime(2014, 1, 10, 4, 44), adminUser);

        TaskHistory taskHistory4 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 0L, null), task2.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory4, new DateTime(2014, 1, 14, 23, 59), adminUser, new DateTime(2014, 1, 12, 23, 59), adminUser);

        TaskHistory taskHistory5 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 8L, null), task3.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory5, new DateTime(2014, 1, 16, 10, 20), adminUser, new DateTime(2014, 1, 16, 10, 20), adminUser);

        TaskHistory taskHistory6 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 0L, null), task4.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory6, new DateTime(2014, 1, 17, 0, 0), adminUser, new DateTime(2014, 1, 17, 0, 0), adminUser);

        TaskHistory taskHistory7 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 4L, null), task3.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory7, new DateTime(2014, 1, 19, 12, 12), adminUser, new DateTime(2014, 1, 19, 12, 12), adminUser);

        TaskHistory taskHistory8 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 0L, null), task1.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory8, new DateTime(2014, 1, 19, 14, 14), adminUser, new DateTime(2014, 1, 19, 14, 14), adminUser);

        TaskHistory taskHistory9 = taskHistoryTestPersistence.createTaskHistory(new Effort(0, 0L, null), task3.getUniqueId(), TaskHistoryEvent.EFFORT_CHANGE, adminUser);
        taskHistoryTestPersistence.create(taskHistory9, new DateTime(2014, 1, 20, 15, 34), adminUser, new DateTime(2014, 1, 20, 15, 34), adminUser);
    }

    private Task createTask(int id, Effort effort, DateTime creationTime, User createdBy, Task rootTask) {
        EffortTestPersistence effortTestPersistence = new EffortTestPersistence();
        effortTestPersistence.create(effort);
        TaskTestPersistence taskTestPersistence = new TaskTestPersistence();
        Task task = new Task("ID_" + id, "Task " + id, "", effort, null, TaskStatus.NOT_STARTED, createdBy);
        taskTestPersistence.create(rootTask, task, creationTime, createdBy, creationTime, createdBy);
        return task;
    }
}

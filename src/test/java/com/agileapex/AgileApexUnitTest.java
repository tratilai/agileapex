package com.agileapex;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;

import com.agileapex.common.sprint.SprintHelper;
import com.agileapex.domain.Customer;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.CustomerPersistenceImpl;
import com.agileapex.persistence.TaskHistoryPersistenceImpl;
import com.agileapex.persistence.dao.EffortDao;
import com.agileapex.persistence.dao.TaskDao;
import com.agileapex.persistence.dao.UserDao;
import com.agileapex.session.ApplicationSession;
import com.agileapex.session.SessionDataHelper;
import com.vaadin.Application;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

public class AgileApexUnitTest {
    @Mocked
    private UserDao userDao = new UserDao();
    @Mocked
    private EffortDao effortDao = new EffortDao();
    @Mocked
    private TaskDao taskDao = new TaskDao();

    @Before
    public void init() {
        Application application = new TestApplication();
        ApplicationSession session = new ApplicationSession(application);
        ApplicationSession.setSessionDataHelper(new SessionDataHelper());
        application.getContext().addTransactionListener(session);
        User user = createUser(1L, "Ted", "Tester", "tester");
        ApplicationSession.setUser(user);
    }

    public static class CustomerPersistenceImplMock extends MockUp<CustomerPersistenceImpl> {
        @Mock
        public void $init() {
        }

        @Mock
        public Customer getBySchemaInternalId(String schemaInternalId) {
            return null;
        }

        @Mock
        public void create(Customer customer, DateTime registrationDate) {
        }
    }

    public static class TaskHistoryPersistenceImplMock extends MockUp<TaskHistoryPersistenceImpl> {
        @Mock
        public void $init() {
        }

        @Mock
        public void createFirst(Task task, User createdBy) {
        }

        @Mock
        public void createChange(Effort newEffort, Task task, User createdBy) {
        }
    }

    public static class SprintHelperMock extends MockUp<SprintHelper> {
        @Mock
        public void $init() {
        }

        @Mock
        public boolean isUniqueName(Sprint currentSprint, Release release, String nameToCheck) {
            return true;
        }
    }

    public String createText(int size) {
        String text = "";
        for (int i = 0; i < size; i++) {
            text += "a";
        }
        return text;
    }

    public boolean isEffortValid(final Task rootTask, Long effortLeft, Long sumOfEffortLeft) {
        return rootTask.getEffort().getEffortLeft() == effortLeft && rootTask.getEffort().getSumOfEffortLeft() == sumOfEffortLeft;
    }

    public Task createTask(long uniqueId, Effort effort, TaskStatus status, User createdBy) {
        Task task = new Task(uniqueId, "id" + uniqueId, "name " + uniqueId, "description", effort, null, status, createdBy);
        return task;
    }

    public List<Task> createListOfTasks(Effort effort, User createdBy, TaskStatus... statuses) {
        List<Task> tasks = new ArrayList<Task>();
        long idCounter = 1;
        for (TaskStatus status : statuses) {
            Task newTask = new Task("id" + idCounter, "name", "description", effort, null, status, createdBy);
            newTask.setUniqueId(idCounter);
            tasks.add(newTask);
            idCounter++;
        }
        return tasks;
    }

    public User createUser(Long uniqueId, String firstName, String lastName, String userName) {
        User user = new User();
        user.setUniqueId(uniqueId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(userName);
        return user;
    }

    public List<Task> createChildrenList(Task... tasks) {
        List<Task> children = new ArrayList<Task>();
        for (Task task : tasks) {
            children.add(task);
        }
        return children;
    }
}

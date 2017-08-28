package testing_api.persistence;

import java.util.Random;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.common.DomainObjectHelper;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;

public class TaskTestPersistence {
    private static final Logger logger = LoggerFactory.getLogger(TaskTestPersistence.class);

    private Random randomGenerator = new Random(System.currentTimeMillis());

    public Task createRootTask(String prefix, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        Effort emptyEffort = new Effort(null, null);
        EffortTestPersistence effortTestDao = new EffortTestPersistence();
        effortTestDao.create(emptyEffort);
        String id = prefix + (10000 + randomGenerator.nextInt(10000));
        Task rootTask = new Task(id, id, id, emptyEffort, TaskStatus.DONE, createdBy);
        create(rootTask, creationTime, createdBy, modifiedDate, modifiedBy);
        return rootTask;
    }

    public void create(Task parent, Task task, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        if (parent != null) {
            task.setParent(parent);
            parent.addChildren(task);
            task.setOrderInChildren(parent.getChildren().size());
        }
        create(task, creationTime, createdBy, modifiedDate, modifiedBy);
        TaskHistoryTestPersistence taskHistoryTestPersistence = new TaskHistoryTestPersistence();
        TaskHistory taskHistory = taskHistoryTestPersistence.createTaskHistory(task.getEffort(), task.getUniqueId(), TaskHistoryEvent.EFFORT_AT_CREATION, createdBy);
        taskHistoryTestPersistence.create(taskHistory, creationTime, createdBy, modifiedDate, modifiedBy);
    }

    private void create(Task task, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        logger.info("About to create task. task: {} ", task);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("parent_unique_id", DomainObjectHelper.isNotNullOrZero(task.getParent()) ? task.getParent().getUniqueId() : null);
        parameters.addValue("assigned_unique_id", DomainObjectHelper.isNotNullOrZero(task.getAssigned()) ? task.getAssigned().getUniqueId() : null);
        parameters.addValue("effort_unique_id", DomainObjectHelper.isNotNullOrZero(task.getEffort()) ? task.getEffort().getUniqueId() : null);
        parameters.addValue("identifier", task.getIdentifier());
        parameters.addValue("name", task.getName());
        parameters.addValue("description", task.getDescription());
        parameters.addValue("status", task.getStatus().name());
        parameters.addValue("order_in_children", task.getOrderInChildren());
        parameters.addValue("creation_date", creationTime.toDate());
        parameters.addValue("created_by_unique_id", createdBy.getUniqueId());
        parameters.addValue("modified_date", modifiedDate.toDate());
        parameters.addValue("modified_by_unique_id", modifiedBy.getUniqueId());
        SimpleJdbcInsert command = new SimpleJdbcInsert(TestingApiDatabaseUtils.getDatasource()).withTableName("task").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        task.setUniqueId(uniqueId.longValue());
        logger.info("Task created. task: {} ", task);
    }

    public void delete(Task task) {
        logger.info("About to delete task. task: {} ", task);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(TestingApiDatabaseUtils.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", task.getUniqueId());
        String sql = "DELETE FROM task WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.info("Affected rows: {}", rows);
    }
}

package testing_api.persistence;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.domain.Effort;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.domain.User;

public class TaskHistoryTestPersistence {
    private static final Logger logger = LoggerFactory.getLogger(TaskHistoryTestPersistence.class);

    public void create(TaskHistory taskHistory, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        logger.info("About to create task history. task history: {} ", taskHistory);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("parent_task_unique_id", taskHistory.getParentTaskUniqueId());
        parameters.addValue("event", taskHistory.getEvent());
        parameters.addValue("value_1", taskHistory.getValue1());
        parameters.addValue("value_2", taskHistory.getValue2());
        parameters.addValue("creation_date", creationTime.toDate());
        parameters.addValue("created_by_unique_id", createdBy.getUniqueId());
        parameters.addValue("modified_date", modifiedDate.toDate());
        parameters.addValue("modified_by_unique_id", modifiedBy.getUniqueId());
        SimpleJdbcInsert command = new SimpleJdbcInsert(TestingApiDatabaseUtils.getDatasource()).withTableName("task_history").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        taskHistory.setUniqueId(uniqueId.longValue());
        logger.debug("Task history created. task history: {} ", taskHistory);
    }
    
    public TaskHistory createTaskHistory(Effort effort, long parentTaskUniqueId, TaskHistoryEvent event, User createdBy) {
        TaskHistory taskHistory = new TaskHistory(createdBy);
        taskHistory.setParentTaskUniqueId(parentTaskUniqueId);
        taskHistory.setEvent(event);
        if (effort != null) {
            taskHistory.setValue1((effort.getEffortLeft() != null) ? ("" + effort.getEffortLeft()) : null);
            taskHistory.setValue2((effort.getSumOfEffortLeft() != null) ? ("" + effort.getSumOfEffortLeft()) : null);
        }
        return taskHistory;
    }
}

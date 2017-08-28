package com.agileapex.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.common.DomainObjectHelper;
import com.agileapex.domain.TaskHistory;
import com.agileapex.domain.TaskHistoryEvent;
import com.agileapex.domain.User;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class TaskHistoryDao {
    private static final Logger logger = LoggerFactory.getLogger(TaskHistoryDao.class);

    public List<TaskHistory> getAll(long parentTaskUniqueId) {
        logger.debug("About to retrieve all task histories.");
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("parent_task_unique_id", parentTaskUniqueId);
        String sql = "SELECT unique_id, parent_task_unique_id, event, value_1, value_2, ";
        sql += "creation_date, created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".task_history WHERE parent_task_unique_id=:parent_task_unique_id ORDER BY unique_id";
        return template.query(sql, parameters, new TaskHistoryRowMapper());
    }

    public void create(TaskHistory taskHistory) {
        logger.debug("About to create task history. task history: {} ", taskHistory);
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("parent_task_unique_id", taskHistory.getParentTaskUniqueId());
        parameters.addValue("event", taskHistory.getEvent());
        parameters.addValue("value_1", taskHistory.getValue1());
        parameters.addValue("value_2", taskHistory.getValue2());
        parameters.addValue("creation_date", now.toDate());
        parameters.addValue("created_by_unique_id", DomainObjectHelper.isNotNullOrZero(taskHistory.getCreatedBy()) ? taskHistory.getCreatedBy().getUniqueId() : null);
        parameters.addValue("modified_date", now.toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(taskHistory.getModifiedBy()) ? taskHistory.getModifiedBy().getUniqueId() : null);
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("task_history").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        taskHistory.setUniqueId(uniqueId.longValue());
        logger.debug("Task history created. task history: {} ", taskHistory);
    }

    public void delete(long taskUniqueId) {
        logger.debug("About to delete all task history for task unique id: {} ", taskUniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("parent_task_unique_id", taskUniqueId);
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".task_history WHERE parent_task_unique_id=:parent_task_unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    private class TaskHistoryRowMapper implements RowMapper<TaskHistory> {
        @Override
        public TaskHistory mapRow(ResultSet resultSet, int row) throws SQLException {
            TaskHistory taskHistory = new TaskHistory(new User(resultSet.getLong("created_by_unique_id")));
            taskHistory.setUniqueId(resultSet.getLong("unique_id"));
            taskHistory.setParentTaskUniqueId(resultSet.getLong("parent_task_unique_id"));
            taskHistory.setEvent(TaskHistoryEvent.valueOf(resultSet.getString("event")));
            taskHistory.setValue1(resultSet.getString("value_1"));
            taskHistory.setValue2(resultSet.getString("value_2"));
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            taskHistory.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            taskHistory.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            taskHistory.setModifiedBy(new User(resultSet.getLong("modified_by_unique_id")));
            return taskHistory;
        }
    }
}

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
import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class TaskDao {
    private static final Logger logger = LoggerFactory.getLogger(TaskDao.class);

    public Task get(Long uniqueId) {
        Task task = null;
        if (uniqueId == null) {
            logger.debug("About to retrieve task from db, but task uniqueId is null: {}", uniqueId);
        } else {
            logger.debug("About to retrieve task: {}", uniqueId);
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
            SqlParameterSource parameters = new MapSqlParameterSource("unique_id", uniqueId);
            String sql = "SELECT unique_id, parent_unique_id, assigned_unique_id, effort_unique_id, identifier, name, description, ";
            sql += "status, order_in_children, creation_date, created_by_unique_id, modified_date, modified_by_unique_id ";
            sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".task WHERE unique_id=:unique_id";
            task = template.queryForObject(sql, parameters, new TaskRowMapper());
        }
        return task;
    }

    public List<Task> getChildren(long parentTaskUniqueId) {
        logger.debug("About to retrieve tasks by parent task id: {}", parentTaskUniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("parent_unique_id", parentTaskUniqueId);
        String sql = "SELECT unique_id, parent_unique_id, assigned_unique_id, effort_unique_id, identifier, name, description, ";
        sql += "status, order_in_children, creation_date, created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".task WHERE parent_unique_id =:parent_unique_id ORDER BY order_in_children, name";
        return template.query(sql, parameters, new TaskRowMapper());
    }

    public List<Task> getAll() {
        logger.debug("About to retrieve all tasks.");
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        String sql = "SELECT unique_id, parent_unique_id, assigned_unique_id, effort_unique_id, identifier, name, description, ";
        sql += "status, order_in_children, creation_date, created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".task ORDER BY order_in_children, name";
        return template.getJdbcOperations().query(sql, new TaskRowMapper());
    }

    public void create(Task task) {
        logger.debug("About to create task. task: {} ", task);
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("parent_unique_id", DomainObjectHelper.isNotNullOrZero(task.getParent()) ? task.getParent().getUniqueId() : null);
        parameters.addValue("assigned_unique_id", DomainObjectHelper.isNotNullOrZero(task.getAssigned()) ? task.getAssigned().getUniqueId() : null);
        parameters.addValue("effort_unique_id", DomainObjectHelper.isNotNullOrZero(task.getEffort()) ? task.getEffort().getUniqueId() : null);
        parameters.addValue("identifier", task.getIdentifier());
        parameters.addValue("name", task.getName());
        parameters.addValue("description", task.getDescription());
        parameters.addValue("status", task.getStatus().name());
        parameters.addValue("order_in_children", task.getOrderInChildren());
        parameters.addValue("creation_date", now.toDate());
        parameters.addValue("created_by_unique_id", DomainObjectHelper.isNotNullOrZero(task.getCreatedBy()) ? task.getCreatedBy().getUniqueId() : null);
        parameters.addValue("modified_date", now.toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(task.getModifiedBy()) ? task.getModifiedBy().getUniqueId() : null);
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("task").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        task.setUniqueId(uniqueId.longValue());
        logger.debug("Task created. task: {} ", task);
    }

    public void update(Task task) {
        logger.debug("About to update task. task: {}", task);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", task.getUniqueId());
        parameters.addValue("parent_unique_id", DomainObjectHelper.isNotNullOrZero(task.getParent()) ? task.getParent().getUniqueId() : null);
        parameters.addValue("assigned_unique_id", DomainObjectHelper.isNotNullOrZero(task.getAssigned()) ? task.getAssigned().getUniqueId() : null);
        parameters.addValue("effort_unique_id", DomainObjectHelper.isNotNullOrZero(task.getEffort()) ? task.getEffort().getUniqueId() : null);
        parameters.addValue("identifier", task.getIdentifier());
        parameters.addValue("name", task.getName());
        parameters.addValue("description", task.getDescription());
        parameters.addValue("status", task.getStatus().name());
        parameters.addValue("order_in_children", task.getOrderInChildren());
        parameters.addValue("modified_date", new DateTime().toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(task.getModifiedBy()) ? task.getModifiedBy().getUniqueId() : null);
        String sql = "UPDATE " + ApplicationSession.getUser().getSchemaInternalId() + ".task SET parent_unique_id=:parent_unique_id, assigned_unique_id=:assigned_unique_id, ";
        sql += "effort_unique_id=:effort_unique_id, identifier=:identifier, name=:name, description=:description, status=:status, ";
        sql += "order_in_children=:order_in_children, modified_date=:modified_date, modified_by_unique_id=:modified_by_unique_id ";
        sql += "WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    public void delete(Task task) {
        logger.debug("About to delete task. task: {} ", task);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", task.getUniqueId());
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".task WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    private class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet resultSet, int row) throws SQLException {
            Task task = new Task();
            task.setUniqueId(resultSet.getLong("unique_id"));
            task.setName(resultSet.getString("name"));
            task.setDescription(resultSet.getString("description"));
            task.setIdentifier(resultSet.getString("identifier"));
            task.setStatus(TaskStatus.valueOf(resultSet.getString("status")), null);
            task.setAssigned(new User(resultSet.getLong("assigned_unique_id")));

            if (resultSet.getLong("parent_unique_id") != 0) {
                task.setParent(new Task(resultSet.getLong("parent_unique_id")));
            }
            task.setEffort(new Effort(resultSet.getLong("effort_unique_id")));
            task.setOrderInChildren(resultSet.getLong("order_in_children"));
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            task.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            task.setCreatedBy(new User(resultSet.getLong("created_by_unique_id")));
            task.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            task.setModifiedBy(new User(resultSet.getLong("modified_by_unique_id")));
            return task;
        }
    }
}

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
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.SprintStatus;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class SprintDao {

    private static final Logger logger = LoggerFactory.getLogger(SprintDao.class);

    public Sprint get(final long uniqueId) {
        logger.debug("About to retrieve sprint from db. Unique id: {}", uniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("unique_id", uniqueId);
        String sql = "SELECT unique_id, parent_releaze_unique_id, root_task_unique_id, name, description, status, start_date, end_date, ";
        sql += "creation_date, created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".sprint WHERE unique_id=:unique_id";
        Sprint sprint = template.queryForObject(sql, parameters, new SprintRowMapper());
        return sprint;
    }

    public List<Sprint> getByParentRelease(long parentReleaseUniqueId) {
        logger.debug("About to retrieve sprints by parent release id: {}", parentReleaseUniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("parent_releaze_unique_id", parentReleaseUniqueId);
        String sql = "SELECT unique_id, parent_releaze_unique_id, root_task_unique_id, name, description, status, creation_date, start_date, end_date, ";
        sql += "created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".sprint WHERE parent_releaze_unique_id=:parent_releaze_unique_id ORDER BY name";
        List<Sprint> sprints = template.query(sql, parameters, new SprintRowMapper());
        return sprints;
    }

    public List<Sprint> getAll() {
        logger.debug("About to retrieve all sprints.");
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        String sql = "SELECT unique_id, parent_releaze_unique_id, root_task_unique_id, name, description, status, start_date, end_date, ";
        sql += "creation_date, created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".sprint ORDER BY name";
        List<Sprint> sprints = template.getJdbcOperations().query(sql, new SprintRowMapper());
        return sprints;
    }

    public void create(Sprint sprint) {
        logger.debug("About to create sprint. sprint: {} ", sprint);
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", sprint.getName());
        parameters.addValue("description", sprint.getDescription());
        parameters.addValue("status", sprint.getStatus().name());
        parameters.addValue("parent_releaze_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getParentRelease()) ? sprint.getParentRelease().getUniqueId() : null);
        parameters.addValue("root_task_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getRootTask()) ? sprint.getRootTask().getUniqueId() : null);
        parameters.addValue("start_date", sprint.getStartDate().toDate());
        parameters.addValue("end_date", sprint.getEndDate().toDate());
        parameters.addValue("creation_date", now.toDate());
        parameters.addValue("created_by_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getCreatedBy()) ? sprint.getCreatedBy().getUniqueId() : null);
        parameters.addValue("modified_date", now.toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getModifiedBy()) ? sprint.getModifiedBy().getUniqueId() : null);
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("sprint").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        sprint.setUniqueId(uniqueId.longValue());
        logger.debug("Sprint created. sprint: {} ", sprint);
    }

    public void update(Sprint sprint) {
        logger.debug("About to update sprint. sprint: {} ", sprint);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", sprint.getUniqueId());
        parameters.addValue("parent_releaze_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getParentRelease()) ? sprint.getParentRelease().getUniqueId() : null);
        parameters.addValue("root_task_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getRootTask()) ? sprint.getRootTask().getUniqueId() : null);
        parameters.addValue("name", sprint.getName());
        parameters.addValue("description", sprint.getDescription());
        parameters.addValue("status", sprint.getStatus().name());
        parameters.addValue("start_date", sprint.getStartDate().toDate());
        parameters.addValue("end_date", sprint.getEndDate().toDate());
        parameters.addValue("modified_date", new DateTime().toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getModifiedBy()) ? sprint.getModifiedBy().getUniqueId() : null);
        String sql = "UPDATE " + ApplicationSession.getUser().getSchemaInternalId() + ".sprint SET parent_releaze_unique_id=:parent_releaze_unique_id, root_task_unique_id=:root_task_unique_id, ";
        sql += "name=:name, description=:description, status=:status, start_date=:start_date, end_date=:end_date, modified_date=:modified_date, ";
        sql += "modified_by_unique_id=:modified_by_unique_id ";
        sql += "WHERE unique_id=:unique_id";
        template.update(sql, parameters);
    }

    public void delete(Sprint sprint) {
        logger.debug("About to delete sprint. sprint: {} ", sprint);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", sprint.getUniqueId());
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".sprint WHERE unique_id=:unique_id";
        template.update(sql, parameters);
    }

    private class SprintRowMapper implements RowMapper<Sprint> {
        @Override
        public Sprint mapRow(ResultSet resultSet, int row) throws SQLException {
            Sprint sprint = new Sprint();
            sprint.setUniqueId(resultSet.getLong("unique_id"));
            sprint.setName(resultSet.getString("name"));
            sprint.setDescription(resultSet.getString("description"));
            sprint.setStatus(SprintStatus.valueOf(resultSet.getString("status")));
            if (resultSet.getLong("parent_releaze_unique_id") != 0) {
                sprint.setParentRelease(new Release(resultSet.getLong("parent_releaze_unique_id")));
            }
            if (resultSet.getLong("root_task_unique_id") != 0) {
                sprint.setRootTask(new Task(resultSet.getLong("root_task_unique_id")));
            }
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            sprint.setStartDate(dateAndTimeUtil.convert(resultSet.getTimestamp("start_date")));
            sprint.setEndDate(dateAndTimeUtil.convert(resultSet.getTimestamp("end_date")));
            sprint.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            sprint.setCreatedBy(new User(resultSet.getLong("created_by_unique_id")));
            sprint.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            sprint.setModifiedBy(new User(resultSet.getLong("modified_by_unique_id")));
            return sprint;
        }
    }
}

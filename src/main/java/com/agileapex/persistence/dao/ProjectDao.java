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
import com.agileapex.domain.ProductBacklog;
import com.agileapex.domain.Project;
import com.agileapex.domain.ProjectStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class ProjectDao {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDao.class);

    public Project get(long uniqueId) {
        logger.debug("About to retrieve project from db. Unique id: {}", uniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("unique_id", uniqueId);
        String sql = "SELECT unique_id, product_backlog_unique_id, name, description, status, task_prefix, creation_date, ";
        sql += "created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".project WHERE unique_id=:unique_id";
        Project project = template.queryForObject(sql, parameters, new ProjectRowMapper());
        return project;
    }

    public List<Project> getAll() {
        logger.debug("About to retrieve all projects.");
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        String sql = "SELECT unique_id, product_backlog_unique_id, name, description, status, task_prefix, creation_date, ";
        sql += "created_by_unique_id, modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".project ORDER BY name";
        List<Project> projects = template.getJdbcOperations().query(sql, new ProjectRowMapper());
        return projects;
    }

    public void create(Project project) {
        logger.debug("About to create project. project: {} ", project);
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("product_backlog_unique_id", project.getProductBacklog() != null ? project.getProductBacklog().getUniqueId() : null);
        parameters.addValue("name", project.getName());
        parameters.addValue("description", project.getDescription());
        parameters.addValue("status", project.getStatus().name());
        parameters.addValue("task_prefix", project.getTaskPrefix());
        parameters.addValue("creation_date", now.toDate());
        parameters.addValue("created_by_unique_id", project.getCreatedBy() != null ? project.getCreatedBy().getUniqueId() : null);
        parameters.addValue("modified_date", now.toDate());
        parameters.addValue("modified_by_unique_id", project.getModifiedBy() != null ? project.getModifiedBy().getUniqueId() : null);
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("project").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        project.setUniqueId(uniqueId.longValue());
        logger.debug("Project created. project: {} ", project);
    }

    public void update(Project project) {
        logger.debug("About to update project. project: {} ", project);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", project.getUniqueId());
        parameters.addValue("product_backlog_unique_id", DomainObjectHelper.isNotNullOrZero(project.getProductBacklog()) ? project.getProductBacklog().getUniqueId() : null);
        parameters.addValue("name", project.getName());
        parameters.addValue("description", project.getDescription());
        parameters.addValue("status", project.getStatus().name());
        parameters.addValue("task_prefix", project.getTaskPrefix());
        parameters.addValue("modified_date", new DateTime().toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(project.getModifiedBy()) ? project.getModifiedBy().getUniqueId() : null);
        String sql = "UPDATE " + ApplicationSession.getUser().getSchemaInternalId() + ".project SET product_backlog_unique_id=:product_backlog_unique_id, name=:name, description=:description, ";
        sql += "status=:status, task_prefix=:task_prefix, modified_date=:modified_date, modified_by_unique_id=:modified_by_unique_id ";
        sql += "WHERE unique_id=:unique_id";
        template.update(sql, parameters);
    }

    public void delete(Project project) {
        logger.debug("About to delete project. project: {} ", project);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", project.getUniqueId());
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".project WHERE unique_id=:unique_id";
        template.update(sql, parameters);
    }

    private class ProjectRowMapper implements RowMapper<Project> {
        @Override
        public Project mapRow(ResultSet resultSet, int row) throws SQLException {
            Project project = new Project();
            project.setUniqueId(resultSet.getLong("unique_id"));
            project.setName(resultSet.getString("name"));
            project.setDescription(resultSet.getString("description"));
            project.setStatus(ProjectStatus.valueOf(resultSet.getString("status")));
            project.setTaskPrefix(resultSet.getString("task_prefix"));
            project.setProductBacklog(new ProductBacklog(resultSet.getLong("product_backlog_unique_id")));
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            project.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            project.setCreatedBy(new User(resultSet.getLong("created_by_unique_id")));
            project.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            project.setModifiedBy(new User(resultSet.getLong("modified_by_unique_id")));
            return project;
        }
    }
}

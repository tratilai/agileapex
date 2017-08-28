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
import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.domain.ReleaseStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class ReleaseDao {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseDao.class);

    public Release get(long uniqueId) {
        logger.debug("About to retrieve releases from db. Unique id: {}", uniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("unique_id", uniqueId);
        String sql = "SELECT unique_id, parent_project_unique_id, name, description, status, creation_date, created_by_unique_id, ";
        sql += "modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".releaze WHERE unique_id = :unique_id";
        Release release = template.queryForObject(sql, parameters, new ReleaseRowMapper());
        return release;
    }

    public List<Release> getByParentProject(long parentProjectUniqueId) {
        logger.debug("About to retrieve release by parent project id: {}", parentProjectUniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("parent_project_unique_id", parentProjectUniqueId);
        String sql = "SELECT unique_id, parent_project_unique_id, name, description, status, creation_date, created_by_unique_id, ";
        sql += "modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".releaze WHERE parent_project_unique_id = :parent_project_unique_id ORDER BY name";
        List<Release> releases = template.query(sql, parameters, new ReleaseRowMapper());
        return releases;
    }

    public List<Release> getAll() {
        logger.debug("About to retrieve all releases.");
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        String sql = "SELECT unique_id, parent_project_unique_id, name, description, status, creation_date, created_by_unique_id, ";
        sql += "modified_date, modified_by_unique_id ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".releaze ORDER BY name";
        List<Release> releases = template.getJdbcOperations().query(sql, new ReleaseRowMapper());
        return releases;
    }

    public void create(Release release) {
        logger.debug("About to create release. release: {}", release);
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", release.getName());
        parameters.addValue("description", release.getDescription());
        parameters.addValue("status", release.getStatus().name());
        parameters.addValue("parent_project_unique_id", release.getParentProject() != null ? release.getParentProject().getUniqueId() : null);
        parameters.addValue("creation_date", now.toDate());
        parameters.addValue("created_by_unique_id", DomainObjectHelper.isNotNullOrZero(release.getCreatedBy()) ? release.getCreatedBy().getUniqueId() : null);
        parameters.addValue("modified_date", now.toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(release.getModifiedBy()) ? release.getModifiedBy().getUniqueId() : null);
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("releaze").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        release.setUniqueId(uniqueId.longValue());
        logger.debug("Release created. release: {}", release);
    }

    public void update(Release release) {
        logger.debug("About to update release. release: {} ", release);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", release.getUniqueId());
        parameters.addValue("name", release.getName());
        parameters.addValue("description", release.getDescription());
        parameters.addValue("status", release.getStatus().name());
        parameters.addValue("parent_project_unique_id", DomainObjectHelper.isNotNullOrZero(release.getParentProject()) ? release.getParentProject().getUniqueId() : null);
        parameters.addValue("modified_date", new DateTime().toDate());
        parameters.addValue("modified_by_unique_id", DomainObjectHelper.isNotNullOrZero(release.getModifiedBy()) ? release.getModifiedBy().getUniqueId() : null);
        String sql = "UPDATE " + ApplicationSession.getUser().getSchemaInternalId() + ".releaze SET parent_project_unique_id=:parent_project_unique_id, name=:name, description=:description, status=:status, ";
        sql += "modified_date=:modified_date, modified_by_unique_id=:modified_by_unique_id ";
        sql += "WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    public void delete(Release release) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", release.getUniqueId());
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".releaze WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    private class ReleaseRowMapper implements RowMapper<Release> {
        @Override
        public Release mapRow(ResultSet resultSet, int row) throws SQLException {
            Release release = new Release();
            release.setUniqueId(resultSet.getLong("unique_id"));
            release.setName(resultSet.getString("name"));
            release.setDescription(resultSet.getString("description"));
            release.setStatus(ReleaseStatus.valueOf(resultSet.getString("status")));
            release.setParentProject(new Project(resultSet.getLong("parent_project_unique_id")));
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            release.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            release.setCreatedBy(new User(resultSet.getLong("created_by_unique_id")));
            release.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            release.setModifiedBy(new User(resultSet.getLong("modified_by_unique_id")));
            return release;
        }
    }
}
package com.agileapex.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.agileapex.domain.Effort;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class EffortDao {

    private static final Logger logger = LoggerFactory.getLogger(EffortDao.class);

    public Effort get(long uniqueId) {
        logger.debug("About to retrieve effort from db. Unique id: {}", uniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("unique_id", uniqueId);
        String sql = "SELECT unique_id, effort_left, sum_of_effort_left ";
        sql += "FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".effort WHERE unique_id=:unique_id";
        return template.queryForObject(sql, parameters, new EffortRowMapper());
    }

    public void create(Effort effort) {
        logger.debug("About to create an effort: {}", effort);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("effort_left", effort.getEffortLeft());
        parameters.addValue("sum_of_effort_left", effort.getSumOfEffortLeft());
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("effort").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        effort.setUniqueId(uniqueId.longValue());
        logger.debug("Effort created: {}", effort);
    }

    public void update(Effort effort) {
        logger.debug("About to update effort. effort: {}", effort);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", effort.getUniqueId());
        parameters.addValue("effort_left", effort.getEffortLeft());
        parameters.addValue("sum_of_effort_left", effort.getSumOfEffortLeft());
        String sql = "UPDATE " + ApplicationSession.getUser().getSchemaInternalId() + ".effort SET effort_left=:effort_left, sum_of_effort_left=:sum_of_effort_left ";
        sql += "WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    public void delete(Effort effort) {
        logger.debug("About to delete effort. effort: {} ", effort);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", effort.getUniqueId());
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".effort WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    private class EffortRowMapper implements RowMapper<Effort> {
        @Override
        public Effort mapRow(ResultSet resultSet, int row) throws SQLException {
            Effort effort = new Effort();
            effort.setUniqueId(resultSet.getLong("unique_id"));
            effort.setEffortLeft(resultSet.getLong("effort_left"));
            effort.setSumOfEffortLeft(resultSet.getLong("sum_of_effort_left"));
            return effort;
        }
    }
}

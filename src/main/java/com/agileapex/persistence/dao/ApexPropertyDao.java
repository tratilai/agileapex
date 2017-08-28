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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.agileapex.AgileApexException;
import com.agileapex.domain.ApexProperty;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class ApexPropertyDao {
    private static final Logger logger = LoggerFactory.getLogger(ApexPropertyDao.class);

    public ApexProperty get(long userUniqueId, String key) {
        logger.debug("About to retrieve property for user: {}", userUniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_unique_id", userUniqueId);
        parameters.addValue("key_1", key);
        String sql = "SELECT unique_id, user_unique_id, key_1, value_1 FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".apex_property WHERE user_unique_id=:user_unique_id AND key_1=:key_1";
        List<ApexProperty> properties = template.query(sql, parameters, new ApexPropertyRowMapper());
        if (properties.size() == 0) {
            return null;
        } else if (properties.size() == 1) {
            return properties.get(0);
        } else {
            throw new AgileApexException("Too many properties for key '" + key + "' and user '" + userUniqueId + "'. Maybe database is corrupted.");
        }
    }

    public void createOrUpdate(long userUniqueId, String key, String newValue) {
        ApexProperty property = get(userUniqueId, key);
        if (property == null) {
            create(userUniqueId, key, newValue);
        } else if (!newValue.equals(property.getValue())) {
            udpate(userUniqueId, key, newValue);
        }
    }

    public void create(long userUniqueId, String key, String value) {
        logger.debug("About to create property for user: {}", userUniqueId);
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_unique_id", userUniqueId);
        parameters.addValue("key_1", key);
        parameters.addValue("value_1", value);
        parameters.addValue("modified_date", now.toDate());
        parameters.addValue("modified_by_unique_id", userUniqueId);
        parameters.addValue("creation_date", now.toDate());
        parameters.addValue("created_by_unique_id", userUniqueId);
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("apex_property").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        logger.debug("User property inserted to id: {}  for key: {}  and value: {}", uniqueId, key, value);
    }

    public void udpate(long userUniqueId, String key, String value) {
        logger.debug("About to update property for user: {}", userUniqueId);
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_unique_id", userUniqueId);
        parameters.addValue("key_1", key);
        parameters.addValue("value_1", value);
        parameters.addValue("modified_date", now.toDate());
        parameters.addValue("modified_by_unique_id", userUniqueId);
        String sql = "UPDATE " + ApplicationSession.getUser().getSchemaInternalId() + ".apex_property SET user_unique_id=:user_unique_id, key_1=:key_1, value_1=:value_1, ";
        sql += "modified_date=:modified_date, modified_by_unique_id=:modified_by_unique_id ";
        sql += "WHERE user_unique_id=:user_unique_id AND key_1=:key_1";
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        int rows = template.update(sql, parameters);
        logger.debug("User property update affected to {} rows. For key: {}  and value: {}", rows, key, value);
    }

    public void delete(long userUniqueId) {
        logger.debug("About to delete properties for user: {}", userUniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_unique_id", userUniqueId);
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".apex_property WHERE user_unique_id=:user_unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    private class ApexPropertyRowMapper implements RowMapper<ApexProperty> {
        @Override
        public ApexProperty mapRow(ResultSet resultSet, int row) throws SQLException {
            ApexProperty property = new ApexProperty();
            property.setUniqueId(resultSet.getLong("unique_id"));
            property.setUserUniqueId(resultSet.getLong("user_unique_id"));
            property.setKey(resultSet.getString("key_1"));
            property.setValue(resultSet.getString("value_1"));
            return property;
        }
    }
}

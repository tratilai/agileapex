package com.agileapex.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.agileapex.AgileApexException;
import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.domain.Authorization;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;
import com.agileapex.persistence.db.AdminDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public User getBySchemaPublicId(String schemaPublicId) {
        logger.debug("About to retrieve user by schema public id: {}", schemaPublicId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(AdminDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("schema_public_id", schemaPublicId);
        String sql = "SELECT unique_id, authorisation, first_name, last_name, schema_internal_id, schema_public_id, ";
        sql += "password_digest, password_salt, user_name, status, last_sign_in_date, creation_date, modified_date ";
        sql += "FROM admin.uzer WHERE schema_public_id=:schema_public_id ORDER BY creation_date LIMIT 1";
        return template.queryForObject(sql, parameters, new UserRowMapper());
    }

    public User get(Long uniqueId) {
        User user = null;
        if (uniqueId > 0) {
            logger.debug("About to retrieve user from db. Unique id: {}", uniqueId);
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(AdminDatabaseHelper.getDatasource());
            SqlParameterSource parameters = new MapSqlParameterSource("unique_id", uniqueId);
            String sql = "SELECT unique_id, authorisation, first_name, last_name, schema_internal_id, schema_public_id, ";
            sql += "password_digest, password_salt, user_name, status, last_sign_in_date, creation_date, modified_date ";
            sql += "FROM admin.uzer WHERE unique_id=:unique_id";
            user = template.queryForObject(sql, parameters, new UserRowMapper());
        }
        return user;
    }

    public User get(String userName) {
        User user = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(userName)) {
            logger.debug("About to retrieve user by user name: {}", userName);
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(AdminDatabaseHelper.getDatasource());
            SqlParameterSource parameters = new MapSqlParameterSource("user_name", userName);
            String sql = "SELECT unique_id, authorisation, first_name, last_name, schema_internal_id, schema_public_id, ";
            sql += "password_digest, password_salt, user_name, status, last_sign_in_date, creation_date, modified_date ";
            sql += "FROM admin.uzer WHERE user_name=:user_name";
            user = template.queryForObject(sql, parameters, new UserRowMapper());
        }
        return user;
    }

    public List<User> getAll() {
        logger.debug("About to retrieve all users.");
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(AdminDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("schema_internal_id", ApplicationSession.getUser().getSchemaInternalId());
        List<User> users = null;
        String sql = "SELECT unique_id, authorisation, first_name, last_name, schema_internal_id, schema_public_id, ";
        sql += "password_digest, password_salt, user_name, status, last_sign_in_date, creation_date, modified_date ";
        sql += "FROM admin.uzer WHERE schema_internal_id=:schema_internal_id ORDER BY last_name";
        users = template.query(sql, parameters, new UserRowMapper());
        return users;
    }

    public void create(User user) {
        logger.debug("About to create a user: {}", user);
        if (StringUtils.isEmpty(user.getSchemaInternalId()) || StringUtils.isEmpty(user.getSchemaPublicId())) {
            throw new AgileApexException("Schema id can not be null ever! Security risk to see other client's data. user: " + user);
        }
        DateTime now = new DateTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_name", user.getEmail());
        parameters.addValue("first_name", user.getFirstName());
        parameters.addValue("last_name", user.getLastName());
        parameters.addValue("schema_internal_id", user.getSchemaInternalId());
        parameters.addValue("schema_public_id", user.getSchemaPublicId());
        parameters.addValue("password_digest", user.getPasswordDigest());
        parameters.addValue("password_salt", user.getPasswordSalt());
        parameters.addValue("status", user.getStatus().name());
        parameters.addValue("authorisation", user.getAuthorization().name());
        if (user.getLastSignInDate() != null) {
            parameters.addValue("last_sign_in_date", user.getLastSignInDate().toDate());
        }
        parameters.addValue("creation_date", now.toDate());
        parameters.addValue("modified_date", now.toDate());
        SimpleJdbcInsert command = new SimpleJdbcInsert(AdminDatabaseHelper.getDatasource()).withSchemaName("admin").withTableName("uzer").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        user.setUniqueId(uniqueId.longValue());
        logger.debug("User created: {}", user);
    }

    public void update(User user) {
        logger.debug("About to update user. user: {} ", user);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(AdminDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", user.getUniqueId());
        parameters.addValue("user_name", user.getEmail());
        parameters.addValue("first_name", user.getFirstName());
        parameters.addValue("last_name", user.getLastName());
        if (user.getPasswordDigest() != null && user.getPasswordDigest().length > 0) {
            parameters.addValue("password_digest", user.getPasswordDigest());
            parameters.addValue("password_salt", user.getPasswordSalt());
        }
        parameters.addValue("status", user.getStatus().name());
        parameters.addValue("authorisation", user.getAuthorization().name());
        parameters.addValue("last_sign_in_date", (user.getLastSignInDate() != null ? user.getLastSignInDate().toDate() : null));
        parameters.addValue("modified_date", new DateTime().toDate());
        String sql = "UPDATE admin.uzer SET authorisation=:authorisation, first_name=:first_name, ";
        sql += "last_name=:last_name, password_digest=:password_digest, password_salt=:password_salt, ";
        sql += "user_name=:user_name, status=:status, last_sign_in_date=:last_sign_in_date, modified_date=:modified_date ";
        sql += "WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    public void delete(User user) {
        logger.debug("About to delete user: {}", user);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(AdminDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", user.getUniqueId());
        parameters.addValue("schema_internal_id", user.getSchemaInternalId());
        String sql = "DELETE FROM admin.uzer WHERE unique_id=:unique_id AND schema_internal_id=:schema_internal_id";
        int rows = template.update(sql, parameters);
        logger.debug("Affected rows: {}", rows);
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int row) throws SQLException {
            User user = new User();
            user.setUniqueId(resultSet.getLong("unique_id"));
            user.setEmail(resultSet.getString("user_name"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setSchemaInternalId(resultSet.getString("schema_internal_id"));
            user.setSchemaPublicId(resultSet.getString("schema_public_id"));
            user.setPasswordDigest(resultSet.getBytes("password_digest"));
            user.setPasswordSalt(resultSet.getBytes("password_salt"));
            user.setStatus(UserStatus.valueOf(resultSet.getString("status")));
            user.setAuthorization(Authorization.valueOf(resultSet.getString("authorisation")));
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            user.setLastSignInDate(dateAndTimeUtil.convert(resultSet.getTimestamp("last_sign_in_date")));
            user.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            user.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            return user;
        }
    }
}

package testing_api.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.domain.Authorization;
import com.agileapex.domain.User;

public class UserTestPersistence {

    private static final Logger logger = LoggerFactory.getLogger(UserTestPersistence.class);

    public User get(String userName) {
        User user = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(userName)) {
            logger.info("About to retrieve user by user name: {}", userName);
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(TestingApiDatabaseUtils.getDatasource());
            SqlParameterSource parameters = new MapSqlParameterSource("user_name", userName);
            String sql = "SELECT unique_id, authorisation, first_name, last_name, ";
            sql += "password_digest, password_salt, user_name, creation_date, modified_date ";
            sql += "FROM uzer WHERE user_name=:user_name";
            user = template.queryForObject(sql, parameters, new UserRowMapper());
        }
        return user;
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int row) throws SQLException {
            User user = new User();
            user.setUniqueId(resultSet.getLong("unique_id"));
            user.setAuthorization(Authorization.valueOf(resultSet.getString("authorisation")));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setPasswordDigest(resultSet.getBytes("password_digest"));
            user.setPasswordSalt(resultSet.getBytes("password_salt"));
            user.setEmail(resultSet.getString("user_name"));
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            user.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            user.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            return user;
        }
    }
}

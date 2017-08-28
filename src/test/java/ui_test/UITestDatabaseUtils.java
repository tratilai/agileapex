package ui_test;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import db.DatabaseUtils;

public class UITestDatabaseUtils extends DatabaseUtils {

    public int getTaskCount() {
        return getCount("select count(*) from task");
    }

    public int getEffortCount() {
        return getCount("select count(*) from effort");
    }

    public int getProjectCount() {
        return getCount("select count(*) from project");
    }

    public int getReleaseCount() {
        return getCount("select count(*) from releaze");
    }

    public int getSprintCount() {
        return getCount("select count(*) from sprint");
    }

    public int getCount(String sql) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource();
        return template.queryForInt(sql, parameters);
    }
}

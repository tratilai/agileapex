package testing_api.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.domain.Effort;

public class EffortTestPersistence {

    private static final Logger logger = LoggerFactory.getLogger(EffortTestPersistence.class);

    public void create(Effort effort) {
        logger.info("About to create an effort: {}", effort);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("effort_left", effort.getEffortLeft());
        parameters.addValue("sum_of_effort_left", effort.getSumOfEffortLeft());
        SimpleJdbcInsert command = new SimpleJdbcInsert(TestingApiDatabaseUtils.getDatasource()).withTableName("effort").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        effort.setUniqueId(uniqueId.longValue());
        logger.info("Effort created: {}", effort);
    }

    public void delete(Effort effort) {
        logger.info("About to delete effort. effort: {} ", effort);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(TestingApiDatabaseUtils.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", effort.getUniqueId());
        String sql = "DELETE FROM effort WHERE unique_id=:unique_id";
        int rows = template.update(sql, parameters);
        logger.info("Affected rows: {}", rows);
    }

}

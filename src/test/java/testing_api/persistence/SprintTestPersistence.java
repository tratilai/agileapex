package testing_api.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.common.DomainObjectHelper;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.SprintStatus;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;

public class SprintTestPersistence {

    private static final Logger logger = LoggerFactory.getLogger(SprintTestPersistence.class);

    public void create(Sprint sprint, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        logger.info("About to create sprint. sprint: {} ", sprint);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", sprint.getName());
        parameters.addValue("description", sprint.getDescription());
        parameters.addValue("status", sprint.getStatus().name());
        parameters.addValue("parent_releaze_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getParentRelease()) ? sprint.getParentRelease().getUniqueId() : null);
        parameters.addValue("root_task_unique_id", DomainObjectHelper.isNotNullOrZero(sprint.getRootTask()) ? sprint.getRootTask().getUniqueId() : null);
        parameters.addValue("start_date", sprint.getStartDate().toDate());
        parameters.addValue("end_date", sprint.getEndDate().toDate());
        parameters.addValue("creation_date", creationTime.toDate());
        parameters.addValue("created_by_unique_id", createdBy.getUniqueId());
        parameters.addValue("modified_date", modifiedDate.toDate());
        parameters.addValue("modified_by_unique_id", modifiedBy.getUniqueId());
        SimpleJdbcInsert command = new SimpleJdbcInsert(TestingApiDatabaseUtils.getDatasource()).withTableName("sprint").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        sprint.setUniqueId(uniqueId.longValue());
        logger.info("Sprint created. sprint: {} ", sprint);
    }

    public void delete(Sprint sprint) {
        logger.info("About to delete sprint. sprint: {} ", sprint);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(TestingApiDatabaseUtils.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", sprint.getUniqueId());
        String sql = "DELETE FROM sprint WHERE unique_id=:unique_id";
        template.update(sql, parameters);
    }
}

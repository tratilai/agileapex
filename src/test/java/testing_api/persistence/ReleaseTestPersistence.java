package testing_api.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.domain.ReleaseStatus;
import com.agileapex.domain.User;

public class ReleaseTestPersistence {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseTestPersistence.class);

    public void create(Release release, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        logger.info("About to create release. release: {}", release);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", release.getName());
        parameters.addValue("description", release.getDescription());
        parameters.addValue("status", release.getStatus().name());
        parameters.addValue("parent_project_unique_id", release.getParentProject() != null ? release.getParentProject().getUniqueId() : null);
        parameters.addValue("creation_date", creationTime.toDate());
        parameters.addValue("created_by_unique_id", createdBy.getUniqueId());
        parameters.addValue("modified_date", modifiedDate.toDate());
        parameters.addValue("modified_by_unique_id", modifiedBy.getUniqueId());
        SimpleJdbcInsert command = new SimpleJdbcInsert(TestingApiDatabaseUtils.getDatasource()).withTableName("releaze").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        release.setUniqueId(uniqueId.longValue());
        logger.info("Release created. release: {}", release);
    }
}
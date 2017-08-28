package testing_api.persistence;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.domain.ProductBacklog;
import com.agileapex.domain.Project;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;
import com.agileapex.persistence.SequencerPersistence;

public class ProjectTestPersistence {

    private static final Logger logger = LoggerFactory.getLogger(ProjectTestPersistence.class);

    public void create(Project project, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        logger.info("About to create project. project: {} ", project);
        createProductBacklog(project, creationTime, createdBy, modifiedDate, modifiedBy);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("product_backlog_unique_id", project.getProductBacklog() != null ? project.getProductBacklog().getUniqueId() : null);
        parameters.addValue("name", project.getName());
        parameters.addValue("description", project.getDescription());
        parameters.addValue("status", project.getStatus().name());
        parameters.addValue("task_prefix", project.getTaskPrefix());
        parameters.addValue("creation_date", creationTime.toDate());
        parameters.addValue("created_by_unique_id", createdBy.getUniqueId());
        parameters.addValue("modified_date", modifiedDate.toDate());
        parameters.addValue("modified_by_unique_id", modifiedBy.getUniqueId());
        SimpleJdbcInsert command = new SimpleJdbcInsert(TestingApiDatabaseUtils.getDatasource()).withTableName("project").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        project.setUniqueId(uniqueId.longValue());
        logger.info("Project created. project: {} ", project);
    }

    private void createProductBacklog(Project project, DateTime creationTime, User createdBy, DateTime modifiedDate, User modifiedBy) {
        TaskTestPersistence taskTestDao = new TaskTestPersistence();
        Task rootTask = taskTestDao.createRootTask(SequencerPersistence.KEY_PREFIX_FOR_PROJECT, creationTime, createdBy, modifiedDate, modifiedBy);
        ProductBacklogTestPersistence productBacklogTestDao = new ProductBacklogTestPersistence();
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setRootTask(rootTask);
        productBacklogTestDao.create(productBacklog);
        project.setProductBacklog(productBacklog);
    }

    public void delete(Project project) {
        logger.info("About to delete project. project: {} ", project);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(TestingApiDatabaseUtils.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", project.getUniqueId());
        String sql = "DELETE FROM project WHERE unique_id=:unique_id";
        template.update(sql, parameters);
    }
}

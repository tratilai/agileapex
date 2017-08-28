package testing_api.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import testing_api.TestingApiDatabaseUtils;

import com.agileapex.domain.ProductBacklog;

public class ProductBacklogTestPersistence {
    private static final Logger logger = LoggerFactory.getLogger(ProductBacklogTestPersistence.class);

    public void create(ProductBacklog productBacklog) {
        logger.info("About to create a product backlog: {}", productBacklog);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("root_task_unique_id", productBacklog.getRootTask().getUniqueId());
        SimpleJdbcInsert command = new SimpleJdbcInsert(TestingApiDatabaseUtils.getDatasource()).withTableName("product_backlog").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        productBacklog.setUniqueId(uniqueId.longValue());
        logger.info("Product backlog created: {}", productBacklog);
    }
}

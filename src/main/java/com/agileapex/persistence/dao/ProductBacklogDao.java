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

import com.agileapex.domain.ProductBacklog;
import com.agileapex.domain.Task;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;

public class ProductBacklogDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductBacklogDao.class);

    public ProductBacklog get(long uniqueId) {
        logger.debug("About to retrieve product backlog from db. Unique id: {}", uniqueId);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("unique_id", uniqueId);
        String sql = "SELECT unique_id, root_task_unique_id FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".product_backlog ";
        sql += "WHERE unique_id=:unique_id";
        ProductBacklog productBacklog = template.queryForObject(sql, parameters, new ProductBacklogRowMapper());
        return productBacklog;
    }

    public void create(ProductBacklog productBacklog) {
        logger.debug("About to create a product backlog: {}", productBacklog);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("root_task_unique_id", productBacklog.getRootTask().getUniqueId());
        SimpleJdbcInsert command = new SimpleJdbcInsert(ClientSchemaDatabaseHelper.getDatasource()).withSchemaName(ApplicationSession.getUser().getSchemaInternalId()).withTableName("product_backlog").usingGeneratedKeyColumns("unique_id");
        Number uniqueId = command.executeAndReturnKey(parameters);
        productBacklog.setUniqueId(uniqueId.longValue());
        logger.debug("Product backlog created: {}", productBacklog);
    }

    public void delete(ProductBacklog productBacklog) {
        logger.debug("About to delete product backlog. product backlog: {} ", productBacklog);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ClientSchemaDatabaseHelper.getDatasource());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("unique_id", productBacklog.getUniqueId());
        String sql = "DELETE FROM " + ApplicationSession.getUser().getSchemaInternalId() + ".product_backlog WHERE unique_id=:unique_id";
        template.update(sql, parameters);
    }

    private class ProductBacklogRowMapper implements RowMapper<ProductBacklog> {
        @Override
        public ProductBacklog mapRow(ResultSet resultSet, int row) throws SQLException {
            ProductBacklog productBacklog = new ProductBacklog();
            productBacklog.setUniqueId(resultSet.getLong("unique_id"));
            productBacklog.setRootTask(new Task(resultSet.getLong("root_task_unique_id")));
            return productBacklog;
        }
    }
}

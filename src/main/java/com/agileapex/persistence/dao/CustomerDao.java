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
import com.agileapex.domain.Customer;
import com.agileapex.persistence.db.AdminDatabaseHelper;

public class CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDao.class);

    public Customer getBySchemaInternalId(String schemaInternalId) {
        logger.debug("About to retrieve customer by schema internal id: {}", schemaInternalId);
        Customer customer = null;
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(AdminDatabaseHelper.getDatasource());
        SqlParameterSource parameters = new MapSqlParameterSource("schema_internal_id", schemaInternalId);
        String sql = "SELECT schema_internal_id, registration_date, creation_date, modified_date ";
        sql += "FROM admin.customer WHERE schema_internal_id=:schema_internal_id LIMIT 1";
        List<Customer> customers = template.query(sql, parameters, new CustomerRowMapper());
        if (customers.size() > 0) {
            customer = customers.get(0);
        }
        return customer;
    }

    public void create(Customer customer, DateTime registrationDate) {
        logger.debug("About to create a customer: {}", customer);
        if (StringUtils.isEmpty(customer.getSchemaInternalId())) {
            throw new AgileApexException("Schema internal id can not be null ever! Security risk to see other client's data. customer: " + customer);
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("schema_internal_id", customer.getSchemaInternalId());
        parameters.addValue("registration_date", registrationDate.toDate());
        parameters.addValue("creation_date", registrationDate.toDate());
        parameters.addValue("modified_date", registrationDate.toDate());
        SimpleJdbcInsert command = new SimpleJdbcInsert(AdminDatabaseHelper.getDatasource()).withSchemaName("admin").withTableName("customer");
        int rows = command.execute(parameters);
        logger.debug("Affected rows: {}", rows);
    }

    private class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet resultSet, int row) throws SQLException {
            Customer customer = new Customer();
            customer.setSchemaInternalId(resultSet.getString("schema_internal_id"));
            DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();
            customer.setRegistrationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("registration_date")));
            customer.setCreationDate(dateAndTimeUtil.convert(resultSet.getTimestamp("creation_date")));
            customer.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            customer.setModifiedDate(dateAndTimeUtil.convert(resultSet.getTimestamp("modified_date")));
            return customer;
        }
    }
}

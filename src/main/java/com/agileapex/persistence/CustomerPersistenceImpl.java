package com.agileapex.persistence;

import org.joda.time.DateTime;

import com.agileapex.domain.Customer;
import com.agileapex.persistence.dao.CustomerDao;

public class CustomerPersistenceImpl implements CustomerPersistence {
    private CustomerDao customerDao = new CustomerDao();

    @Override
    public Customer getBySchemaInternalId(String schemaInternalId) {
        return customerDao.getBySchemaInternalId(schemaInternalId);
    }

    @Override
    public void create(Customer customer, DateTime registrationDate) {
        customerDao.create(customer, registrationDate);
    }
}

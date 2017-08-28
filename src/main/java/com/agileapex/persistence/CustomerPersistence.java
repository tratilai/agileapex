package com.agileapex.persistence;

import org.joda.time.DateTime;

import com.agileapex.domain.Customer;

public interface CustomerPersistence {

    public abstract Customer getBySchemaInternalId(String schemaInternalId);

    public abstract void create(Customer customer, DateTime registrationDate);

}
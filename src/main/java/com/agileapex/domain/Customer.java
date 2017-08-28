package com.agileapex.domain;

import java.io.Serializable;

import org.joda.time.DateTime;

public class Customer implements Serializable, Comparable<Customer> {
    private static final long serialVersionUID = -4935962986552471859L;
    private String schemaInternalId;
    private DateTime registrationDate;
    private DateTime creationDate;
    private DateTime modifiedDate;

    @Override
    public String toString() {
        return "[Customer: " + super.toString() + " " + schemaInternalId + " " + registrationDate + "]";
    }

    @Override
    public int compareTo(Customer customer) {
        return schemaInternalId.compareTo(customer.getSchemaInternalId());
    }

    @Override
    public int hashCode() {
        return (184 + schemaInternalId.hashCode()) % Integer.MAX_VALUE;
    }

    public String getSchemaInternalId() {
        return schemaInternalId;
    }

    public void setSchemaInternalId(String schemaInternalId) {
        this.schemaInternalId = schemaInternalId;
    }

    public DateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(DateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

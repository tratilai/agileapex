package com.agileapex.domain;

import org.joda.time.DateTime;

public class User extends DomainObject implements Comparable<User> {
    private static final long serialVersionUID = 8632626561637749923L;
    private Customer customer;
    private String email;
    private String firstName;
    private String lastName;
    private String schemaInternalId;
    private String schemaPublicId;
    private byte[] passwordDigest;
    private byte[] passwordSalt;
    private UserStatus status;
    private Authorization authorization;
    private DateTime lastSignInDate;
    private DateTime creationDate;
    private DateTime modifiedDate;

    public User() {
        this(0, null, null, null, null, null, null, null, null, null);
    }

    public User(long uniqueId) {
        this(uniqueId, null, null, null, null, null, null, null, null, null);
    }

    public User(String email, byte[] passwordDigest, byte[] passwordSalt, String firstName, String lastName, UserStatus status, Authorization authorization, String schemaInternalId, String schemaPublicId) {
        this(0, email, passwordDigest, passwordSalt, firstName, lastName, status, authorization, schemaInternalId, schemaPublicId);
    }

    public User(long uniqueId, String email, byte[] passwordDigest, byte[] passwordSalt, String firstName, String lastName, UserStatus status, Authorization authorization, String schemaInternalId, String schemaPublicId) {
        super(uniqueId);
        this.email = email;
        this.passwordDigest = passwordDigest;
        this.passwordSalt = passwordSalt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.authorization = authorization;
        this.schemaInternalId = schemaInternalId;
        this.schemaPublicId = schemaPublicId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getVisibleAuthorizationName() {
        String result = "";
        if (authorization != null) {
            result = authorization.getName();
        }
        return result;
    }

    @Override
    public String toString() {
        return "[User: " + super.toString() + " " + firstName + " " + lastName + " " + email + " " + schemaInternalId + "]";
    }

    @Override
    public int compareTo(User user) {
        return ((Long) super.getUniqueId()).compareTo(user.getUniqueId());
    }

    @Override
    public int hashCode() {
        return (78 + email.hashCode() + firstName.hashCode() + lastName.hashCode() + schemaInternalId.hashCode()) % Integer.MAX_VALUE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(byte[] passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public String getSchemaInternalId() {
        return schemaInternalId;
    }

    public void setSchemaInternalId(String schemaInternalId) {
        this.schemaInternalId = schemaInternalId;
    }

    public String getSchemaPublicId() {
        return schemaPublicId;
    }

    public void setSchemaPublicId(String schemaPublicId) {
        this.schemaPublicId = schemaPublicId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public DateTime getLastSignInDate() {
        return lastSignInDate;
    }

    public void setLastSignInDate(DateTime lastSignInDate) {
        this.lastSignInDate = lastSignInDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}

package com.agileapex.domain;

public class ApexProperty extends DomainObject {
    private static final long serialVersionUID = -6411053854309193349L;
    private long userUniqueId;
    private String key;
    private String value;

    @Override
    public String toString() {
        return "[ApexProperty: " + super.toString() + " user id: " + userUniqueId + " key: " + key + " value: " + value + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof ApexProperty) {
            return this.getUniqueId() == ((ApexProperty) target).getUniqueId();
        }
        return false;
    }

    public long getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(long userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

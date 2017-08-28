package com.agileapex.domain;

import java.io.Serializable;

public class DomainObject implements Serializable {
    private static final long serialVersionUID = 4122499162591957110L;
    private Long uniqueId;

    public DomainObject() {
    }

    public DomainObject(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        return "[uniqueId: " + uniqueId + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof DomainObject) {
            return this.getUniqueId() == ((DomainObject) target).getUniqueId();
        }
        return false;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }
}

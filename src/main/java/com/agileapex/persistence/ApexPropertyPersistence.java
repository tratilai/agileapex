package com.agileapex.persistence;

import com.agileapex.domain.ApexProperty;

public interface ApexPropertyPersistence {

    public ApexProperty getProperty(long userUniqueId, String key);

    public Long getLong(long userUniqueId, String key);

    public String getString(long userUniqueId, String key);

    public void delete(long userUniqueId);

    public void createOrUpdate(long userUniqueId, String key, long value);

    public void createOrUpdate(long userUniqueId, String key, String value);
}
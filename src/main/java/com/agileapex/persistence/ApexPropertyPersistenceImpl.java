package com.agileapex.persistence;

import com.agileapex.domain.ApexProperty;
import com.agileapex.persistence.dao.ApexPropertyDao;

public class ApexPropertyPersistenceImpl implements ApexPropertyPersistence {
    private ApexPropertyDao apexPropertyDao = new ApexPropertyDao();

    @Override
    public ApexProperty getProperty(long userUniqueId, String key) {
        return apexPropertyDao.get(userUniqueId, key);
    }

    @Override
    public Long getLong(long userUniqueId, String key) {
        ApexProperty property = getProperty(userUniqueId, key);
        if (property != null) {
            return Long.parseLong(property.getValue());
        }
        return null;
    }

    @Override
    public String getString(long userUniqueId, String key) {
        ApexProperty property = getProperty(userUniqueId, key);
        if (property != null) {
            return property.getValue();
        }
        return null;
    }

    @Override
    public void createOrUpdate(long userUniqueId, String key, long value) {
        createOrUpdate(userUniqueId, key, "" + value);
    }

    @Override
    public void createOrUpdate(long userUniqueId, String key, String value) {
        apexPropertyDao.createOrUpdate(userUniqueId, key, value);
    }

    @Override
    public void delete(long userUniqueId) {
        apexPropertyDao.delete(userUniqueId);
    }
}

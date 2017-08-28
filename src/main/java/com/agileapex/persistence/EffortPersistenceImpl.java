package com.agileapex.persistence;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;
import com.agileapex.persistence.dao.EffortDao;

public class EffortPersistenceImpl implements EffortPersistence {
    private static final Logger logger = LoggerFactory.getLogger(EffortPersistenceImpl.class);
    private Cache cache = CacheManager.getInstance().getCache("effortCache");
    private EffortDao effortDao = new EffortDao();

    @Override
    public Effort get(long uniqueId) {
        Effort effort = null;
        Element element = cache.get(uniqueId);
        if (element == null) {
            effort = effortDao.get(uniqueId);
            cache.put(new Element(uniqueId, effort));
        } else {
            logger.debug("About to get effort from cache. Unique id: {}", uniqueId);
            effort = (Effort) element.getObjectValue();
        }
        return effort;
    }

    @Override
    public void create(Effort effort) {
        effortDao.create(effort);
        cache.put(new Element(effort.getUniqueId(), effort));
    }

    @Override
    public void update(Effort effort, Task task, User modifiedBy) {
        effortDao.update(effort);
        cache.put(new Element(effort.getUniqueId(), effort));
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        taskHistoryPersistence.createChange(effort, task, modifiedBy);
    }

    @Override
    public void delete(Effort effort) {
        cache.remove(effort.getUniqueId());
        effortDao.delete(effort);
    }
}

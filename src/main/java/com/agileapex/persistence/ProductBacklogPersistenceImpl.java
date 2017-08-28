package com.agileapex.persistence;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Effort;
import com.agileapex.domain.ProductBacklog;
import com.agileapex.domain.Task;
import com.agileapex.persistence.dao.ProductBacklogDao;

public class ProductBacklogPersistenceImpl implements ProductBacklogPersistence {
    private static final Logger logger = LoggerFactory.getLogger(ProductBacklogPersistenceImpl.class);
    private ProductBacklogDao productBacklogDao = new ProductBacklogDao();
    private Cache cache = CacheManager.getInstance().getCache("productBacklogCache");

    @Override
    public ProductBacklog get(long uniqueId) {
        ProductBacklog productBacklog = null;
        Element element = cache.get(uniqueId);
        if (element == null) {
            productBacklog = productBacklogDao.get(uniqueId);
            cache.put(new Element(uniqueId, productBacklog));
        } else {
            logger.debug("About to get product backlog from cache. Unique id: {}", uniqueId);
            productBacklog = (ProductBacklog) element.getObjectValue();
        }
        return productBacklog;
    }

    @Override
    public void create(ProductBacklog productBacklog) {
        productBacklogDao.create(productBacklog);
        cache.put(new Element(productBacklog.getUniqueId(), productBacklog));
    }

    @Override
    public void delete(ProductBacklog productBacklog) {
        Task rootTask = productBacklog.getRootTask();
        rootTask.fetchSecondLevelObjects();
        Effort effort = rootTask.getEffort();
        cache.remove(productBacklog.getUniqueId());
        productBacklogDao.delete(productBacklog);
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        taskDbService.delete(rootTask);
        if (effort != null) {
            EffortPersistenceImpl effortDbService = new EffortPersistenceImpl();
            effortDbService.delete(effort);
        }
    }
}

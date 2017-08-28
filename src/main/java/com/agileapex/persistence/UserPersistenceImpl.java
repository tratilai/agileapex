package com.agileapex.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.agileapex.domain.Customer;
import com.agileapex.domain.User;
import com.agileapex.persistence.dao.UserDao;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class UserPersistenceImpl implements UserPersistence {
    private static final Logger logger = LoggerFactory.getLogger(UserPersistenceImpl.class);
    private UserDao userDao = new UserDao();
    private Cache cache = CacheManager.getInstance().getCache("userCache");

    @Override
    public User get(Long uniqueId) {
        User user = null;
        Element element = cache.get(uniqueId);
        if (element == null) {
            try {
                user = userDao.get(uniqueId);
                insertCustomer(user);
                cache.put(new Element(uniqueId, user));
            } catch (DataAccessException e) {
                logger.debug("User not found with unique id: {}", uniqueId);
            }
        } else {
            logger.debug("About to get user from cache. Unique id: {}", uniqueId);
            user = (User) element.getObjectValue();
        }
        return user;
    }

    @Override
    public User getBySchemaPublicId(String schemaPublicId) {
        User user = null;
        try {
            user = userDao.getBySchemaPublicId(schemaPublicId);
            insertCustomer(user);
        } catch (DataAccessException e) {
            logger.error("User not found with public schema id: {}", schemaPublicId);
            logger.error("Exception: {}", e);
        }
        return user;
    }

    @Override
    public User get(String userName) {
        User user = null;
        try {
            user = userDao.get(userName);
            insertCustomer(user);
            cache.put(new Element(user.getUniqueId(), user));
        } catch (DataAccessException e) {
            logger.debug("User not found with user name: {}", userName);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userDao.getAll();
        logger.debug("Clear the cache and add new elements. Number of users: {}", users.size());
        cache.removeAll();
        for (User user : users) {
            insertCustomer(user);
            cache.put(new Element(user.getUniqueId(), user));
        }
        return users;
    }

    @Override
    public void create(User user) {
        userDao.create(user);
        insertCustomer(user);
        cache.put(new Element(user.getUniqueId(), user));
    }

    @Override
    public void update(User user) {
        userDao.update(user);
        insertCustomer(user);
        cache.put(new Element(user.getUniqueId(), user));
    }

    @Override
    public void delete(User user) {
        long userUniqueId = user.getUniqueId();
        cache.remove(user.getUniqueId());
        userDao.delete(user);
        ApexPropertyPersistence apexPropertyDbService = new ApexPropertyPersistenceImpl();
        apexPropertyDbService.delete(userUniqueId);
    }

    private void insertCustomer(User user) {
        if (user != null) {
            CustomerPersistence customerPersistence = new CustomerPersistenceImpl();
            Customer customer = customerPersistence.getBySchemaInternalId(user.getSchemaInternalId());
            user.setCustomer(customer);
        }
    }
}

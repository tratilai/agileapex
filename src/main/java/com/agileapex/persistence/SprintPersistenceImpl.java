package com.agileapex.persistence;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;
import com.agileapex.persistence.dao.SprintDao;

public class SprintPersistenceImpl implements SprintPersistence {
    private static final Logger logger = LoggerFactory.getLogger(SprintPersistenceImpl.class);
    private Cache cache = CacheManager.getInstance().getCache("sprintCache");
    private SprintDao sprintDao = new SprintDao();

    @Override
    public Sprint get(final long uniqueId) {
        Sprint sprint = null;
        Element element = cache.get(uniqueId);
        if (element == null) {
            sprint = sprintDao.get(uniqueId);
            cache.put(new Element(uniqueId, sprint));
        } else {
            logger.debug("About to get sprint from cache. Unique id: {}", uniqueId);
            sprint = (Sprint) element.getObjectValue();
        }
        return sprint;
    }

    @Override
    public List<Sprint> getByParentRelease(long parentReleaseUniqueId) {
        List<Sprint> sprints = sprintDao.getByParentRelease(parentReleaseUniqueId);
        putInCache(sprints);
        return sprints;
    }

    @Override
    public List<Sprint> getAll() {
        List<Sprint> sprints = sprintDao.getAll();
        cache.removeAll();
        putInCache(sprints);
        return sprints;
    }

    @Override
    public void create(Sprint sprint) {
        sprintDao.create(sprint);
        cache.put(new Element(sprint.getUniqueId(), sprint));
    }

    @Override
    public void create(Release release, Sprint sprint, User createdBy) {
        if (sprint.getRootTask() == null) {
            TaskPersistence taskDbService = new TaskPersistenceImpl();
            Task rootTask = taskDbService.createRootTask(release.getParentProject(), createdBy);
            sprint.setRootTask(rootTask);
        }
        create(sprint);
        release.addSprint(sprint);
        ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
        releaseDbService.update(release);
    }

    @Override
    public void update(Sprint sprint) {
        sprintDao.update(sprint);
        cache.put(new Element(sprint.getUniqueId(), sprint));
    }

    @Override
    public void delete(Sprint sprint) {
        sprint.fetchSecondLevelObjects();
        Release parentRelease = sprint.getParentRelease();
        parentRelease.fetchSecondLevelObjects();
        List<Sprint> sprints = parentRelease.getSprints();
        sprints.remove(sprint);
        ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
        releaseDbService.update(parentRelease);
        cache.remove(sprint.getUniqueId());
        sprintDao.delete(sprint);
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        Task sprintRootTask = sprint.getRootTask();
        taskDbService.delete(sprintRootTask);
    }

    @Override
    public List<Task> getAllTasks(Sprint sprint) {
        logger.debug("About to get all tasks for the sprint. Unique id: {}", sprint.getUniqueId());
        sprint.fetchSecondLevelObjects();
        Task rootTask = sprint.getRootTask();
        List<Task> allTasks = getAllTasks(rootTask);
        return allTasks;
    }

    private List<Task> getAllTasks(Task task) {
        List<Task> tasks = new ArrayList<Task>();
        task.fetchSecondLevelObjects();
        List<Task> children = task.getChildren();
        for (Task child : children) {
            if (!child.isLeaf()) {
                tasks.addAll(getAllTasks(child));
            }
            tasks.add(child);
        }
        return tasks;
    }

    private void putInCache(List<Sprint> sprints) {
        for (Sprint sprint : sprints) {
            cache.put(new Element(sprint.getUniqueId(), sprint));
        }
    }
}

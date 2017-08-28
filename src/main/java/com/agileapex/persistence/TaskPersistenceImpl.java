package com.agileapex.persistence;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.task.TaskIdentifierGenerator;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Project;
import com.agileapex.domain.Task;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.persistence.dao.TaskDao;

public class TaskPersistenceImpl implements TaskPersistence {
    private static final Logger logger = LoggerFactory.getLogger(TaskPersistenceImpl.class);
    private Cache cache = CacheManager.getInstance().getCache("taskCache");
    private TaskDao taskDao = new TaskDao();

    @Override
    public Task get(Long uniqueId) {
        Task task = null;
        Element element = cache.get(uniqueId);
        if (element == null) {
            task = taskDao.get(uniqueId);
            cache.put(new Element(task.getUniqueId(), task));
        } else {
            logger.debug("About to get task from cache. Unique id: {}", uniqueId);
            task = (Task) element.getObjectValue();
        }
        return task;
    }

    @Override
    public List<Task> getChildren(long parentTaskUniqueId) {
        List<Task> tasks = taskDao.getChildren(parentTaskUniqueId);
        putInCache(tasks);
        return tasks;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskDao.getAll();
        logger.debug("Clear the cache and add new elements. Number of tasks: {}", tasks.size());
        cache.removeAll();
        putInCache(tasks);
        return tasks;
    }

    @Override
    public void create(Task task, User createdBy) {
        create(null, task, createdBy);
    }

    @Override
    public Task createRootTask(Project project, User createdBy) {
        TaskIdentifierGenerator idGenerator = new TaskIdentifierGenerator(project);
        Effort emptyEffort = new Effort(null, null);
        EffortPersistence effortDbService = new EffortPersistenceImpl();
        effortDbService.create(emptyEffort);
        String id = idGenerator.getNextRootIdentifier();
        Task rootTask = new Task(id, id, id, emptyEffort, TaskStatus.DONE, createdBy);
        create(rootTask, createdBy);
        return rootTask;
    }

    @Override
    public void create(Task parent, Task task, User createdBy) {
        if (parent != null) {
            task.setParent(parent);
            parent.addChildren(task);
            task.setOrderInChildren(parent.getChildren().size());
        }
        taskDao.create(task);
        cache.put(new Element(task.getUniqueId(), task));
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        taskHistoryPersistence.createFirst(task, createdBy);
    }

    @Override
    public void update(Task task) {
        taskDao.update(task);
        cache.put(new Element(task.getUniqueId(), task));
    }

    @Override
    public void delete(Task task) {
        if (task.getParent() != null) {
            task.getParent().removeChildren(task);
        }
        cache.remove(task.getUniqueId());
        deleteStartingFromThis(task);
    }

    private void deleteStartingFromThis(Task task) {
        logger.debug("About to delete tasks starting from task: {}", task);
        if (task != null) {
            EffortPersistenceImpl effortDbService = new EffortPersistenceImpl();
            task.fetchSecondLevelObjects();
            Task parent = task.getParent();
            if (parent != null) {
                parent.fetchSecondLevelObjects();
                List<Task> children = parent.getChildren();
                children.remove(task);
                update(parent);
                for (Task child : children) {
                    child.setOrderInChildren(children.indexOf(child) + 1);
                    update(child);
                }
            }
            deleteRecursively(task, effortDbService);
        }
    }

    private void deleteRecursively(Task task, EffortPersistenceImpl effortDbService) {
        task.fetchSecondLevelObjects();
        List<Task> children = task.getChildren();
        if (children != null) {
            for (Task child : children) {
                deleteRecursively(child, effortDbService);
            }
        }
        if (task.getEffort() != null) {
            effortDbService.delete(task.getEffort());
        }
        TaskHistoryPersistence taskHistoryPersistence = new TaskHistoryPersistenceImpl();
        taskHistoryPersistence.delete(task.getUniqueId());
        taskDao.delete(task);
    }

    private void putInCache(List<Task> tasks) {
        for (Task task : tasks) {
            cache.put(new Element(task.getUniqueId(), task));
        }
    }
}

package com.agileapex.persistence;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.ProductBacklog;
import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;
import com.agileapex.persistence.dao.ProjectDao;
import com.agileapex.session.ApplicationSession;

public class ProjectPersistenceImpl implements ProjectPersistence {
    private static final Logger logger = LoggerFactory.getLogger(ProjectPersistenceImpl.class);
    private Cache cache = CacheManager.getInstance().getCache("projectCache");
    private ProjectDao projectDao = new ProjectDao();

    @Override
    public Project get(long uniqueId) {
        Project project = null;
        Element element = cache.get(uniqueId);
        if (element == null) {
            project = projectDao.get(uniqueId);
            cache.put(new Element(uniqueId, project));
        } else {
            logger.debug("About to get project from cache. Unique id: {}", uniqueId);
            project = (Project) element.getObjectValue();
        }
        return project;
    }

    @Override
    public List<Project> getAll() {
        List<Project> projects = projectDao.getAll();
        cache.removeAll();
        putInCache(projects);
        return projects;
    }

    @Override
    public void create(Project project, User createdBy) {
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        Task rootTask = taskDbService.createRootTask(project, createdBy);
        ProductBacklogPersistence productBacklogDbService = new ProductBacklogPersistenceImpl();
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setRootTask(rootTask);
        productBacklogDbService.create(productBacklog);
        project.setProductBacklog(productBacklog);
        projectDao.create(project);
        cache.put(new Element(project.getUniqueId(), project));
    }

    @Override
    public void update(Project project) {
        projectDao.update(project);
        cache.put(new Element(project.getUniqueId(), project));
    }

    @Override
    public void delete(Project project) {
        project.fetchSecondLevelObjects();
        List<Release> releases = project.getReleases();
        if (releases != null) {
            ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
            for (Release release : releases) {
                releaseDbService.delete(release);
            }
        }
        ProductBacklog productBacklog = project.getProductBacklog();
        cache.remove(project.getUniqueId());
        projectDao.delete(project);
        ProductBacklogPersistence productBacklogDbService = new ProductBacklogPersistenceImpl();
        productBacklogDbService.delete(productBacklog);
    }

    private void putInCache(List<Project> projects) {
        for (Project project : projects) {
            cache.put(new Element(project.getUniqueId(), project));
        }
    }
}

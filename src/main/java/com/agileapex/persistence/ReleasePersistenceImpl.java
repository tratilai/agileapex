package com.agileapex.persistence;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.persistence.dao.ReleaseDao;

public class ReleasePersistenceImpl implements ReleasePersistence {
    private static final Logger logger = LoggerFactory.getLogger(ReleasePersistenceImpl.class);
    private Cache cache = CacheManager.getInstance().getCache("releaseCache");
    private ReleaseDao releaseDao = new ReleaseDao();

    @Override
    public Release get(long uniqueId) {
        Release release = null;
        Element element = cache.get(uniqueId);
        if (element == null) {
            release = releaseDao.get(uniqueId);
            cache.put(new Element(uniqueId, release));
        } else {
            logger.debug("About to get release from cache. Unique id: {}", uniqueId);
            release = (Release) element.getObjectValue();
        }
        return release;
    }

    @Override
    public List<Release> getByParentProject(long parentProjectUniqueId) {
        List<Release> releases = releaseDao.getByParentProject(parentProjectUniqueId);
        putInCache(releases);
        return releases;
    }

    @Override
    public List<Release> getAll() {
        List<Release> releases = releaseDao.getAll();
        cache.removeAll();
        putInCache(releases);
        return releases;
    }

    @Override
    public void create(Release release) {
        releaseDao.create(release);
        cache.put(new Element(release.getUniqueId(), release));
        Project parentProject = release.getParentProject();
        List<Release> releases = parentProject.getReleases();
        releases.add(release);
        ProjectPersistence projectDbService = new ProjectPersistenceImpl();
        projectDbService.update(parentProject);
    }

    @Override
    public void update(Release release) {
        releaseDao.update(release);
        cache.put(new Element(release.getUniqueId(), release));
    }

    @Override
    public void delete(Release release) {
        release.fetchSecondLevelObjects();
        Project parentProject = release.getParentProject();
        List<Release> releases = parentProject.getReleases();
        releases.remove(release);
        ProjectPersistence projectDbService = new ProjectPersistenceImpl();
        projectDbService.update(parentProject);
        SprintPersistence sprintDbService = new SprintPersistenceImpl();
        List<Sprint> sprints = release.getSprints();
        if (sprints != null) {
            for (Sprint sprint : sprints) {
                sprintDbService.delete(sprint);
            }
        }
        cache.remove(release.getUniqueId());
        releaseDao.delete(release);
    }

    private void putInCache(List<Release> releases) {
        for (Release release : releases) {
            cache.put(new Element(release.getUniqueId(), release));
        }
    }
}

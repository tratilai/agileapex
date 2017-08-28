package com.agileapex.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.agileapex.persistence.SprintPersistence;
import com.agileapex.persistence.SprintPersistenceImpl;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;

public class Release extends DomainObject implements LazyLoadable {
    private static final long serialVersionUID = 7014173869972593840L;
    private static final Logger logger = LoggerFactory.getLogger(Release.class);
    private String name;
    private String description;
    private ReleaseStatus status;
    private List<Sprint> sprints;
    private Project parentProject;
    private DateTime creationDate;
    private User createdBy;
    private DateTime modifiedDate;
    private User modifiedBy;

    public Release() {
        this(0, null, null, null, null, null, null);
    }

    public Release(long uniqueId) {
        this(uniqueId, null, null, null, null, null, null);
    }

    public Release(long uniqueId, String name, String description, ReleaseStatus status, List<Sprint> sprints, User createdBy) {
        this(uniqueId, null, name, description, status, sprints, createdBy);
    }

    public Release(Project project, String name, String description, ReleaseStatus status, User createdBy) {
        this(0, project, name, description, status, null, createdBy);
    }

    public Release(long uniqueId, Project parentProject, String name, String description, ReleaseStatus status, List<Sprint> sprints, User createdBy) {
        super(uniqueId);
        this.parentProject = parentProject;
        this.name = name;
        this.description = description;
        this.status = status;
        this.sprints = sprints;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    public void addSprint(Sprint sprint) {
        if (getSprints() == null) {
            setSprints(new ArrayList<Sprint>());
        }
        getSprints().add(sprint);
    }

    @Override
    public String toString() {
        return "[Release: " + super.toString() + " name " + name + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof Release) {
            return this.getUniqueId() == ((Release) target).getUniqueId();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReleaseStatus getStatus() {
        return status;
    }

    public void setStatus(ReleaseStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public Project getParentProject() {
        return parentProject;
    }

    @Override
    public void fetchSecondLevelObjects() {
        logger.debug("Fetching release object's second level objects. This release id: {}", super.getUniqueId());
        if (createdBy != null) {
            UserPersistence userDbService = new UserPersistenceImpl();
            createdBy = userDbService.get(createdBy.getUniqueId());
        }
        if (parentProject != null) {
            ProjectPersistence projectDbService = new ProjectPersistenceImpl();
            parentProject = projectDbService.get(parentProject.getUniqueId());
        }
        SprintPersistence sprintDbService = new SprintPersistenceImpl();
        sprints = sprintDbService.getByParentRelease(getUniqueId());
    }
}

package com.agileapex.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.persistence.ProductBacklogPersistence;
import com.agileapex.persistence.ProductBacklogPersistenceImpl;
import com.agileapex.persistence.ReleasePersistence;
import com.agileapex.persistence.ReleasePersistenceImpl;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.session.ApplicationSession;

public class Project extends DomainObject implements LazyLoadable {
    private static final long serialVersionUID = 3847427011830699956L;
    private static final Logger logger = LoggerFactory.getLogger(Project.class);
    private String name;
    private String description;
    private ProjectStatus status;
    private ProductBacklog productBacklog;
    private String taskPrefix;
    private List<Release> releases = new ArrayList<Release>();
    private DateTime creationDate;
    private User createdBy;
    private DateTime modifiedDate;
    private User modifiedBy;

    public Project() {
        this(0, null, null, null, null, null);
    }

    public Project(long uniqueId) {
        this(uniqueId, null, null, null, null, null);
    }

    public Project(String name, String description, ProjectStatus status, User createdBy, String taskPrefix) {
        this(0, name, description, status, createdBy, taskPrefix);
    }

    public Project(long uniqueId, String name, String description, ProjectStatus status, User createdBy, String taskPrefix) {
        super(uniqueId);
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskPrefix = taskPrefix;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    @Override
    public String toString() {
        return "[Project: " + super.toString() + " name: " + name + " taskPrefix: " + taskPrefix + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof Project) {
            return this.getUniqueId() == ((Project) target).getUniqueId();
        }
        return false;
    }

    public void setProductBacklog(ProductBacklog productBacklog) {
        this.productBacklog = productBacklog;
    }

    public ProductBacklog getProductBacklog() {
        return productBacklog;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
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

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    public String getTaskPrefix() {
        return taskPrefix;
    }

    public void setTaskPrefix(String taskPrefix) {
        this.taskPrefix = taskPrefix;
    }

    @Override
    public void fetchSecondLevelObjects() {
        logger.debug("Fetching project object's second level objects. This project id: {}", super.getUniqueId());
        if (createdBy != null) {
            UserPersistence userDbService = new UserPersistenceImpl();
            createdBy = userDbService.get(createdBy.getUniqueId());
        }
        if (this.productBacklog != null) {
            ProductBacklogPersistence productBacklogDbservice = new ProductBacklogPersistenceImpl();
            this.productBacklog = productBacklogDbservice.get(this.productBacklog.getUniqueId());
        }
        if (releases != null) {
            ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
            releases = releaseDbService.getByParentProject(getUniqueId());
        }
    }
}

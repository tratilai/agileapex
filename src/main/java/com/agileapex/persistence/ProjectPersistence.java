package com.agileapex.persistence;

import java.util.List;

import com.agileapex.domain.Project;
import com.agileapex.domain.User;

public interface ProjectPersistence {

    public abstract Project get(long uniqueId);

    public abstract List<Project> getAll();

    public abstract void create(Project project, User createdBy);

    public abstract void update(Project project);

    public abstract void delete(Project project);
}
package com.agileapex.persistence;

import java.util.List;

import com.agileapex.domain.Release;

public interface ReleasePersistence {

    public abstract Release get(long uniqueId);

    public abstract List<Release> getByParentProject(long parentProjectUniqueId);

    public abstract List<Release> getAll();

    public abstract void create(Release release);

    public abstract void update(Release release);

    public abstract void delete(Release release);
}
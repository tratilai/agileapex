package com.agileapex.persistence;

import com.agileapex.domain.ProductBacklog;

public interface ProductBacklogPersistence {

    public abstract ProductBacklog get(long uniqueId);

    public abstract void create(ProductBacklog productBacklog);

    public abstract void delete(ProductBacklog productBacklog);
}
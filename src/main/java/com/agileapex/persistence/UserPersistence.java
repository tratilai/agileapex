package com.agileapex.persistence;

import java.util.List;

import com.agileapex.domain.User;

public interface UserPersistence {

    public abstract User get(Long uniqueId);

	public abstract User getBySchemaPublicId(String pid);

    public User get(String userName);

    public abstract List<User> getAll();

    public abstract void create(User user);

    public abstract void update(User user);

    public abstract void delete(User user);

}
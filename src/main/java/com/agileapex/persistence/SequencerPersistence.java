package com.agileapex.persistence;

import com.agileapex.domain.Project;

public interface SequencerPersistence {
    public static final String KEY_PREFIX_FOR_PROJECT = "project_";
    public static final String KEY_PREFIX_FOR_ROOT = "root_";

    public abstract long create(String sequenceKey, long value);

    public abstract void update(String sequenceKey, long newValue);

    public abstract long getNextTaskSequenceNumber(Project project);

    public abstract long getNextRootTaskSequenceNumber();
}
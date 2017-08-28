package com.agileapex.common.task;

import com.agileapex.domain.Project;
import com.agileapex.persistence.SequencerPersistence;
import com.agileapex.persistence.SequencerPersistenceImpl;

public class TaskIdentifierGenerator {
    private Project project;

    public TaskIdentifierGenerator(Project project) {
        this.project = project;
    }

    public synchronized String getNextTaskIdentifier() {
        SequencerPersistence sequencerDbService = new SequencerPersistenceImpl();
        return project.getTaskPrefix() + sequencerDbService.getNextTaskSequenceNumber(project);
    }

    public synchronized String getNextRootIdentifier() {
        SequencerPersistence sequencerDbService = new SequencerPersistenceImpl();
        return SequencerPersistence.KEY_PREFIX_FOR_ROOT + sequencerDbService.getNextRootTaskSequenceNumber();
    }
}

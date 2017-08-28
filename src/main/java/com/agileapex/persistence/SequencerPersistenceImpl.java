package com.agileapex.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.AgileApexException;
import com.agileapex.domain.Project;
import com.agileapex.persistence.dao.SequencerDao;

public class SequencerPersistenceImpl implements SequencerPersistence {
    private static final Logger logger = LoggerFactory.getLogger(SequencerPersistenceImpl.class);
    private SequencerDao sequencerDao = new SequencerDao();

    @Override
    public long create(String sequenceKey, long value) {
        return sequencerDao.create(sequenceKey, value);
    }

    @Override
    public void update(String sequenceKey, long newValue) {
        sequencerDao.update(sequenceKey, newValue);
    }

    /**
     * All task under one project will have use the same sequencer.
     * Sequence numbers start from 1.
     */
    @Override
    public long getNextTaskSequenceNumber(Project project) {
        logger.debug("About to get next task sequence number. project: {}", project);
        String sequenceKey = SequencerPersistence.KEY_PREFIX_FOR_PROJECT + project.getUniqueId();
        List<Long> sequences = sequencerDao.getNextSequence(sequenceKey);
        long sequence = getCorrectSequence(sequences, sequenceKey);
        return sequence;
    }

    /**
     * Root task sequence is unique for all task roots.
     * Task roots are mainly used in tree table root's in the user interface,
     * but usually never shown to the user.
     */
    @Override
    public long getNextRootTaskSequenceNumber() {
        logger.debug("About to get next root task sequence number.");
        String sequenceKey = SequencerPersistence.KEY_PREFIX_FOR_ROOT;
        List<Long> sequences = sequencerDao.getNextSequence(sequenceKey);
        long sequence = getCorrectSequence(sequences, sequenceKey);
        return sequence;
    }

    private long getCorrectSequence(List<Long> sequences, String sequenceKey) {
        long returnValue = Long.MIN_VALUE;
        if (sequences.size() == 1) {
            Long lastUsedSequence = sequences.get(0);
            lastUsedSequence = lastUsedSequence + 1;
            sequencerDao.update(sequenceKey, lastUsedSequence);
            returnValue = lastUsedSequence;
        } else if (sequences.size() > 1) {
            String message = "Too many sequences with the same key. Result set size: " + sequences.size();
            logger.error(message);
            throw new AgileApexException(message);
        } else {
            long startingValue = 1L;
            logger.debug("Sequence key not found. Creating the default one. sequenceKey: {} starting value: {}", sequenceKey, startingValue);
            sequencerDao.create(sequenceKey, startingValue);
            returnValue = startingValue;
        }
        return returnValue;
    }
}

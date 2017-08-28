package com.agileapex.domain;

import java.io.Serializable;

public class Sequencer implements Serializable {
    private static final long serialVersionUID = 7155916100273804787L;
    private Long uniqueId;
    private long sequenceKey;
    private long sequenceValue;

    @Override
    public String toString() {
        return "[Sequencer: " + super.toString() + " key: " + sequenceKey + " value: " + sequenceValue + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof Sequencer) {
            return this.getUniqueId() == ((Sequencer) target).getUniqueId();
        }
        return false;
    }

    public void setSequenceKey(long key) {
        this.sequenceKey = key;
    }

    public long getSequenceKey() {
        return sequenceKey;
    }

    public void setSequenceValue(long value) {
        this.sequenceValue = value;
    }

    public long getSequenceValue() {
        return sequenceValue;
    }

    public Long getUniqueId() {
        return uniqueId;
    }
}

package com.agileapex.domain;

public class Effort extends DomainObject {
    public static final String SUM_PREFIX = "Sum ";
    private static final long serialVersionUID = -6570502559856535430L;
    private Long effortLeft;
    private Long sumOfEffortLeft;

    public Effort() {
    }

    public Effort(long uniqueId) {
        super(uniqueId);
    }

    public Effort(Long effortLeft, Long sumOfEffortLeft) {
        this(0L, effortLeft, sumOfEffortLeft);
    }

    public Effort(long uniqueID, Long effortLeft, Long sumOfEffortLeft) {
        super(uniqueID);
        this.setEffortLeft(effortLeft);
        this.setSumOfEffortLeft(sumOfEffortLeft);
    }

    @Override
    public String toString() {
        return "[Effort: " + super.toString() + " effortLeft: " + effortLeft + " sumOfEffortLeft: " + sumOfEffortLeft + "]";
    }

    @Override
    public boolean equals(Object target) {
        if (target != null && target instanceof Effort) {
            return this.getUniqueId() == ((Effort) target).getUniqueId();
        }
        return false;
    }

    public boolean hasSameValue(Effort effort) {
        if (effort != null) {
            if (getEffortLeft() == effort.getEffortLeft() && getSumOfEffortLeft() == effort.sumOfEffortLeft) {
                return true;
            }
        }
        return false;
    }

    public String getEffortAsText() {
        String effortText = "";
        if (this.effortLeft != null) {
            effortText = "" + getEffortLeft();
        } else if (this.sumOfEffortLeft != null) {
            effortText = SUM_PREFIX + getSumOfEffortLeft();
        }
        return effortText;
    }

    public void setToZero() {
        if (effortLeft != null) {
            effortLeft = 0L;
        }
        if (sumOfEffortLeft != null) {
            sumOfEffortLeft = 0L;
        }
    }

    public boolean isNull() {
        return sumOfEffortLeft == null && effortLeft == null;
    }

    public boolean isSumOfEffortLeftGreaterThanZero() {
        return sumOfEffortLeft != null && sumOfEffortLeft > 0;
    }

    public boolean isEffortLeftGreaterThanZero() {
        return effortLeft != null && effortLeft > 0;
    }

    public void setEffortLeft(Long effortLeft) {
        this.effortLeft = effortLeft;
    }

    public Long getEffortLeft() {
        return effortLeft;
    }

    public void setSumOfEffortLeft(Long sumOfEffortLeft) {
        this.sumOfEffortLeft = sumOfEffortLeft;
    }

    public Long getSumOfEffortLeft() {
        return sumOfEffortLeft;
    }
}

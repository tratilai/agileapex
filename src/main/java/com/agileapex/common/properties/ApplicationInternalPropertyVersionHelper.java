package com.agileapex.common.properties;

import org.apache.commons.lang.math.NumberUtils;

public class ApplicationInternalPropertyVersionHelper {

    public String getVersionText() {
        return getMajorVarsionNumber() + "." + getMinorVarsionNumber() + "." + getHotfixVarsionNumber();
    }

    public int getMajorVarsionNumber() {
        String[] versionNumbers = getVersionNumbers();
        return NumberUtils.toInt(versionNumbers[0]);
    }

    public int getMinorVarsionNumber() {
        String[] versionNumbers = getVersionNumbers();
        return NumberUtils.toInt(versionNumbers[1]);
    }

    public int getHotfixVarsionNumber() {
        String[] versionNumbers = getVersionNumbers();
        return NumberUtils.toInt(versionNumbers[2]);
    }

    private String[] getVersionNumbers() {
        String versionProperty = PropertyHelper.getInternalProperty("application.version", String.class);
        String[] versionNumbers = versionProperty.split("\\.");
        return versionNumbers;
    }
}

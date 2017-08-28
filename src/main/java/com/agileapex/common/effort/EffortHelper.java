package com.agileapex.common.effort;

import org.apache.commons.lang.StringUtils;

import com.agileapex.domain.Effort;

public class EffortHelper {

    public Long getLongValueFromTextValue(String effortString) {
        if (effortString.length() > 0) {
            effortString = StringUtils.removeStart(effortString, Effort.SUM_PREFIX);
        }
        return Long.valueOf(effortString);
    }
}

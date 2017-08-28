package com.agileapex.common;

import com.agileapex.domain.DomainObject;

public class DomainObjectHelper {

    public static boolean isNullOrZero(DomainObject domainObject) {
        return domainObject == null || domainObject.getUniqueId() == 0;
    }

    public static boolean isNotNullOrZero(DomainObject domainObject) {
        return !isNullOrZero(domainObject);
    }
}

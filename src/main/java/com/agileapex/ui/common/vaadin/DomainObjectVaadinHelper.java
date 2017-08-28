package com.agileapex.ui.common.vaadin;

import com.agileapex.domain.DomainObject;
import com.vaadin.ui.AbstractSelect;

public class DomainObjectVaadinHelper {

    public DomainObject getObjectFromSelect(AbstractSelect list, long uniqueId) {
        for (Object listObject : list.getItemIds()) {
            if (((DomainObject) listObject).getUniqueId() == uniqueId) {
                return (DomainObject) listObject;
            }
        }
        return null;
    }
}

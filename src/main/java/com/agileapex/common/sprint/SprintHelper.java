package com.agileapex.common.sprint;

import java.util.List;

import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.persistence.SprintPersistence;
import com.agileapex.persistence.SprintPersistenceImpl;

public class SprintHelper {

    public boolean isUniqueName(Sprint currentSprint, Release release, String nameToCheck) {
        SprintPersistence sprintDbService = new SprintPersistenceImpl();
        List<Sprint> sprints = sprintDbService.getByParentRelease(release.getUniqueId());
        if (sprints != null) {
            for (Sprint sprint : sprints) {
                if (currentSprint != null) {
                    if (!currentSprint.equals(sprint) && sprint.getName().equalsIgnoreCase(nameToCheck)) {
                        return false;
                    }
                } else {
                    if (sprint.getName().equalsIgnoreCase(nameToCheck)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

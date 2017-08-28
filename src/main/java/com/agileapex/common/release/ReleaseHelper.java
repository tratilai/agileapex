package com.agileapex.common.release;

import java.util.List;

import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.persistence.ReleasePersistence;
import com.agileapex.persistence.ReleasePersistenceImpl;

public class ReleaseHelper {

    public boolean isUniqueName(Release currentRelease, Project project, String nameToCheck) {

        ReleasePersistence releaseDbService = new ReleasePersistenceImpl();
        List<Release> releases = releaseDbService.getByParentProject(project.getUniqueId());
        if (releases != null) {
            for (Release release : releases) {
                if (currentRelease != null) {
                    if (!release.equals(currentRelease) && release.getName().equalsIgnoreCase(nameToCheck)) {
                        return false;
                    }
                } else {
                    if (release.getName().equalsIgnoreCase(nameToCheck)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

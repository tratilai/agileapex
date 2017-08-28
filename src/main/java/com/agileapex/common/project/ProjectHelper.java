package com.agileapex.common.project;

import java.util.List;

import com.agileapex.domain.Project;
import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;

public class ProjectHelper {

    public boolean isUniqueName(Project currentProject, String nameToCheck) {
        ProjectPersistence projectDbService = new ProjectPersistenceImpl();
        List<Project> projects = projectDbService.getAll();
        for (Project project : projects) {
            if (currentProject != null) {
                if (!currentProject.equals(project) && project.getName().equalsIgnoreCase(nameToCheck)) {
                    return false;
                }
            } else {
                if (project.getName().equalsIgnoreCase(nameToCheck)) {
                    return false;
                }
            }
        }
        return true;
    }
}

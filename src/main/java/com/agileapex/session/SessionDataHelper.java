package com.agileapex.session;

import java.util.HashMap;
import java.util.Map;

import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.ui.window.WindowIdentification;

public class SessionDataHelper {
    private WindowIdentification currentPage;
    private WindowIdentification targetPage;
    private Project project;
    private Release release;
    private Sprint sprint;
    private Map<String, Map<Long, Boolean>> treeTableExpandProperties = new HashMap<String, Map<Long, Boolean>>();

    public void putTreeTableExpandProperty(String key, Map<Long, Boolean> property) {
        treeTableExpandProperties.put(key, property);
    }

    public Map<Long, Boolean> getTreeTableExpandProperty(String key) {
        return treeTableExpandProperties.get(key);
    }

    public WindowIdentification getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(WindowIdentification targetPage) {
        this.targetPage = targetPage;
    }

    public Project getCurrentProject() {
        return project;
    }

    public void setCurrentProject(Project project) {
        this.project = project;
    }

    public Release getCurrentRelease() {
        return release;
    }

    public void setCurrentRelease(Release release) {
        this.release = release;
    }

    public Sprint getCurrentSprint() {
        return sprint;
    }

    public void setCurrentSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public WindowIdentification getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(WindowIdentification currentPage) {
        this.currentPage = currentPage;
    }
}
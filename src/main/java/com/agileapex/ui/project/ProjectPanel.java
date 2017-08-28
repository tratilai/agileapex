package com.agileapex.ui.project;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Project;
import com.agileapex.persistence.ApexPropertyPersistence;
import com.agileapex.persistence.ApexPropertyPersistenceImpl;
import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.vaadin.DomainObjectVaadinHelper;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class ProjectPanel extends Panel implements RefreshableComponent, Constants {
    private static final Logger logger = LoggerFactory.getLogger(ProjectPanel.class);
    private static final long serialVersionUID = 2956727004047310401L;
    protected HorizontalSplitPanel mainLayout;
    protected List<Project> projects;
    protected Table projectsTable;
    protected ProjectExtraInfoPanel extraInfoPanel;

    public ProjectPanel() {
        init();
        createProjectsTable();
        createExtraInfoLayout();
    }

    @Override
    public void attach() {
        setAdvancedFunctionalities();
        refresh();
        refreshFromApexProperties();
    }

    private void init() {
        setSizeFull();
        HorizontalLayout marginLayout = new HorizontalLayout();
        marginLayout.setSizeFull();
        marginLayout.setMargin(true);
        setContent(marginLayout);
        mainLayout = new HorizontalSplitPanel();
        marginLayout.addComponent(mainLayout);
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
    }

    private void createProjectsTable() {
        projectsTable = new Table();
        projectsTable.setDebugId(PROJECTPAGE_PROJECTS_TABLE);
        projectsTable.setSizeFull();
        projectsTable.setSelectable(true);
        projectsTable.setMultiSelect(false);
        projectsTable.setImmediate(true);
        projectsTable.setColumnReorderingAllowed(true);
        BeanItemContainer<Project> container = new BeanItemContainer<Project>(Project.class);
        container.addNestedContainerProperty("name");
        container.addNestedContainerProperty("status.status");
        projectsTable.setContainerDataSource(container);
        projectsTable.setColumnHeader("name", "Project name");
        projectsTable.setColumnExpandRatio("name", EXPAND_RATIO_4);
        projectsTable.setColumnHeader("status.status", "Status");
        projectsTable.setColumnExpandRatio("status.status", EXPAND_RATIO_1);
        projectsTable.setVisibleColumns(new Object[] { "name", "status.status" });
        projectsTable.setSortContainerPropertyId("name");
        projectsTable.addListener(new ProjectTableItemClickListener(this));
        projectsTable.addListener(new ProjectTableValueChangeListener(this));
        mainLayout.setFirstComponent(projectsTable);
    }

    private void createExtraInfoLayout() {
        extraInfoPanel = new ProjectExtraInfoPanel(this);
        mainLayout.setSecondComponent(extraInfoPanel);
    }

    private void setAdvancedFunctionalities() {
        UserHelper userUtil = new UserHelper();
        if (userUtil.hasManagerPrivileges()) {
            projectsTable.addActionHandler(new ProjectTableActionHandler(this));
        }
    }

    protected void doSelectionActions(Project project) {
        ApplicationSession.getSessionDataHelper().setCurrentProject(project);
        setExtraInfoLayoutDataAndTitles(project);
        saveSelectionToApexProperties(project);
    }

    private void setExtraInfoLayoutDataAndTitles(Project project) {
        extraInfoPanel.viewExtraInfoLayoutTitles(true);
        extraInfoPanel.setExtraInfoLayoutData(project);
    }

    private void saveSelectionToApexProperties(Project project) {
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        long userUniqueId = ApplicationSession.getUser().getUniqueId();
        apexPropertiesDbService.createOrUpdate(userUniqueId, PROJECTPAGE_PROJECTLIST_ID, project.getUniqueId());
    }

    private void clearExtraInfoLayout() {
        extraInfoPanel.viewExtraInfoLayoutTitles(false);
        extraInfoPanel.setExtraInfoLayoutData(null);
    }

    private void refreshFromApexProperties() {
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        long userUniqueId = ApplicationSession.getUser().getUniqueId();
        Long projectUniqueId = apexPropertiesDbService.getLong(userUniqueId, PROJECTPAGE_PROJECTLIST_ID);
        if (projectUniqueId != null && projectUniqueId >= 0) {
            DomainObjectVaadinHelper domainUtil = new DomainObjectVaadinHelper();
            Project selectedProject = (Project) domainUtil.getObjectFromSelect(projectsTable, projectUniqueId);
            if (selectedProject != null) {
                setExtraInfoLayoutDataAndTitles(selectedProject);
                projectsTable.select(selectedProject);
                projectsTable.setCurrentPageFirstItemId(selectedProject);
            }
        }
    }

    @Override
    public void refresh() {
        logger.debug("Refreshing project panel.");
        projectsTable.removeAllItems();
        clearExtraInfoLayout();
        ProjectPersistence projectDbService = new ProjectPersistenceImpl();
        projects = projectDbService.getAll();
        if (projects != null) {
            for (Project project : projects) {
                project.fetchSecondLevelObjects();
                projectsTable.addItem(project);
            }
            projectsTable.sort();
        }
    }
}

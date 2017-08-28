package com.agileapex.ui.taskboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.Task;
import com.agileapex.domain.User;
import com.agileapex.persistence.ApexPropertyPersistence;
import com.agileapex.persistence.ApexPropertyPersistenceImpl;
import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.treetable.TaskTreeTable;
import com.agileapex.ui.common.treetable.TreeTableItemClickListener;
import com.agileapex.ui.common.vaadin.DomainObjectVaadinHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;

public class TaskBoardPanel extends Panel implements Constants {
    private static final Logger logger = LoggerFactory.getLogger(TaskBoardPanel.class);
    private static final long serialVersionUID = 7822761963585458225L;
    protected List<Project> projects;
    protected NativeSelect projectsList;
    protected NativeSelect releasesList;
    protected NativeSelect sprintsList;
    protected TaskTreeTable treeTable;
    protected GridLayout layout;

    public TaskBoardPanel() {
        super();
        init();
        createProjectList();
        createReleaseList();
        createSprintList();
        refreshProjects();
        createTreeTable();
    }

    @Override
    public void attach() {
        setAdvancedFunctionalities();
        refreshFromApexProperties();
    }

    private void init() {
        setSizeFull();
        layout = new GridLayout(3, 2);
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        layout.setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        layout.setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1000);
    }

    private void createProjectList() {
        projectsList = new NativeSelect("Project");
        projectsList.setDebugId(TASKBOARD_PROJECT);
        projectsList.setNullSelectionAllowed(false);
        projectsList.setImmediate(true);
        projectsList.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        projectsList.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        projectsList.setItemCaptionPropertyId("name");
        projectsList.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -1234367217232949808L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Project project = (Project) event.getProperty().getValue();
                refreshReleases(project);
                if (project != null) {
                    ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
                    long userUniqueId = ApplicationSession.getUser().getUniqueId();
                    apexPropertiesDbService.createOrUpdate(userUniqueId, TASKBOARDPAGE_PROJECT_SELECTION_ID, project.getUniqueId());
                }
            }
        });
        layout.addComponent(projectsList, COLUMN_ONE, ROW_ONE);
    }

    private void createReleaseList() {
        releasesList = new NativeSelect("Release");
        releasesList.setDebugId(TASKBOARD_RELEASE);
        releasesList.setNullSelectionAllowed(false);
        releasesList.setImmediate(true);
        releasesList.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        releasesList.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        releasesList.setItemCaptionPropertyId("name");
        releasesList.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 8435042627484582766L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Release release = (Release) event.getProperty().getValue();
                refreshSprints(release);
                if (release != null) {
                    ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
                    long userUniqueId = ApplicationSession.getUser().getUniqueId();
                    apexPropertiesDbService.createOrUpdate(userUniqueId, TASKBOARDPAGE_RELEASE_SELECTION_ID, release.getUniqueId());
                }
            }
        });
        layout.addComponent(releasesList, COLUMN_TWO, ROW_ONE);
    }

    private void createSprintList() {
        sprintsList = new NativeSelect("Sprint");
        sprintsList.setDebugId(TASKBOARD_SPRINT);
        sprintsList.setNullSelectionAllowed(false);
        sprintsList.setImmediate(true);
        sprintsList.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        sprintsList.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        sprintsList.setItemCaptionPropertyId("name");
        sprintsList.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -5193634088981916709L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Sprint sprint = (Sprint) event.getProperty().getValue();
                refreshTreeTable(sprint);
                if (sprint != null) {
                    ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
                    long userUniqueId = ApplicationSession.getUser().getUniqueId();
                    apexPropertiesDbService.createOrUpdate(userUniqueId, TASKBOARDPAGE_SPRINT_SELECTION_ID, sprint.getUniqueId());
                }
            }
        });
        layout.addComponent(sprintsList, COLUMN_THREE, ROW_ONE);
    }

    private void createTreeTable() {
        treeTable = new TaskTreeTable("" + serialVersionUID, "Sprint Backlog's Tasks");
        treeTable.setDebugId(TASKBOARD_TASKTREETABLE);
        treeTable.setSizeFull();
        treeTable.setSelectable(true);
        treeTable.setMultiSelect(false);
        treeTable.setImmediate(true);
        treeTable.setSortDisabled(true);
        treeTable.setColumnReorderingAllowed(false);
        treeTable.setFooterVisible(true);
        treeTable.setColumnHeader("identifier", "Id");
        treeTable.setColumnExpandRatio("identifier", 2);
        treeTable.addContainerProperty("identifier", String.class, "");
        treeTable.setColumnHeader("name", "Name");
        treeTable.setColumnExpandRatio("name", 7);
        treeTable.addContainerProperty("name", Label.class, new Label());
        treeTable.setColumnHeader("assigned", "Point Person");
        treeTable.setColumnExpandRatio("assigned", 3);
        treeTable.addContainerProperty("assigned", String.class, "");
        treeTable.setColumnExpandRatio("status", 3);
        treeTable.addContainerProperty("status", String.class, "");
        treeTable.setColumnHeader("status", "Status");
        treeTable.setColumnHeader("effort", "Effort");
        treeTable.setColumnExpandRatio("effort", 1);
        treeTable.addContainerProperty("effort", String.class, "");
        treeTable.setVisibleColumns(new Object[] { "identifier", "name", "assigned", "status", "effort" });
        layout.addComponent(treeTable, COLUMN_ONE, ROW_TWO, COLUMN_THREE, ROW_TWO);
    }

    private void setAdvancedFunctionalities() {
        UserHelper userUtil = new UserHelper();
        treeTable.addActionHandler(new TaskBoardTableActionHandler(this, treeTable, userUtil.hasReporterPrivileges()));
        treeTable.addListener(new TreeTableItemClickListener(this, treeTable, userUtil.hasReporterPrivileges()));
    }

    private void refreshFromApexProperties() {
        logger.debug("Refreshing from user memory in the task board panel.");
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        User user = ApplicationSession.getUser();
        Long projectUniqueId = apexPropertiesDbService.getLong(user.getUniqueId(), TASKBOARDPAGE_PROJECT_SELECTION_ID);
        if (projectUniqueId != null && projectUniqueId >= 0) {
            DomainObjectVaadinHelper domainUtil = new DomainObjectVaadinHelper();
            Project selectedProject = (Project) domainUtil.getObjectFromSelect(projectsList, projectUniqueId);
            if (selectedProject != null) {
                projectsList.select(selectedProject);
                Long releaseUniqueId = apexPropertiesDbService.getLong(user.getUniqueId(), TASKBOARDPAGE_RELEASE_SELECTION_ID);
                if (releaseUniqueId != null && releaseUniqueId >= 0) {
                    Release selectedRelease = (Release) domainUtil.getObjectFromSelect(releasesList, releaseUniqueId);
                    if (selectedRelease != null) {
                        releasesList.select(selectedRelease);
                        Long sprintUniqueId = apexPropertiesDbService.getLong(user.getUniqueId(), TASKBOARDPAGE_SPRINT_SELECTION_ID);
                        if (sprintUniqueId != null && sprintUniqueId >= 0) {
                            Sprint selectedSprint = (Sprint) domainUtil.getObjectFromSelect(sprintsList, sprintUniqueId);
                            if (selectedSprint != null) {
                                sprintsList.select(selectedSprint);
                            }
                        }
                    }
                }
            }
        }
    }

    private void refreshProjects() {
        logger.debug("Refreshing projects of the task board panel.");
        projectsList.removeAllItems();
        ProjectPersistence projectDbService = new ProjectPersistenceImpl();
        projects = projectDbService.getAll();
        BeanItemContainer<Project> container = new BeanItemContainer<Project>(Project.class);
        if (projects != null) {
            container.addAll(projects);
        }
        projectsList.setContainerDataSource(container);
    }

    private void refreshReleases(Project project) {
        logger.debug("Refreshing releases of the task board panel.");
        releasesList.removeAllItems();
        BeanItemContainer<Release> container = new BeanItemContainer<Release>(Release.class);
        if (project != null) {
            project.fetchSecondLevelObjects();
            List<Release> releases = project.getReleases();
            if (releases != null) {
                container.addAll(releases);
            }
        }
        releasesList.setContainerDataSource(container);
    }

    private void refreshSprints(Release release) {
        logger.debug("Refreshing sprints of the task board panel.");
        sprintsList.removeAllItems();
        BeanItemContainer<Sprint> container = new BeanItemContainer<Sprint>(Sprint.class);
        if (release != null) {
            release.fetchSecondLevelObjects();
            List<Sprint> sprints = release.getSprints();
            if (sprints != null) {
                container.addAll(sprints);
            }
        }
        sprintsList.setContainerDataSource(container);
    }

    private void refreshTreeTable(Sprint sprint) {
        logger.debug("Refreshing tree table of the task board panel.");
        treeTable.removeAllItems();
        if (sprint != null) {
            sprint.fetchSecondLevelObjects();
            Task rootTask = sprint.getRootTask();
            rootTask.fetchSecondLevelObjects();
            treeTable.setRootTask(rootTask);
            treeTable.refresh();
        }
    }
}

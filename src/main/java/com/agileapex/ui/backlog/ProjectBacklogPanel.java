package com.agileapex.ui.backlog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.ProductBacklog;
import com.agileapex.domain.Project;
import com.agileapex.domain.Task;
import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.agileapex.persistence.ApexPropertyPersistence;
import com.agileapex.persistence.ApexPropertyPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.treetable.TaskTreeActionHandler;
import com.agileapex.ui.common.treetable.TaskTreeTable;
import com.agileapex.ui.common.treetable.TreeTableDropHandler;
import com.agileapex.ui.common.treetable.TreeTableItemClickListener;
import com.agileapex.ui.common.vaadin.DomainObjectVaadinHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table.TableDragMode;

public class ProjectBacklogPanel extends Panel implements Constants {
    private static final Logger logger = LoggerFactory.getLogger(ProjectBacklogPanel.class);
    private static final long serialVersionUID = -6352087391055227016L;
    protected NativeSelect projectsList;
    protected TaskTreeTable treeTable;
    protected GridLayout layout;
    private Action.Handler projectBacklogTreeActionHandler;

    public ProjectBacklogPanel() {
        super();
        init();
        createProjectList();
        refreshProjects();
        createAndSetTreeTableAttributes();
        createColumns();
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
        projectsList.setNullSelectionAllowed(false);
        projectsList.setImmediate(true);
        projectsList.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        projectsList.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        projectsList.setItemCaptionPropertyId("name");
        projectsList.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 4135718138161358057L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Project project = (Project) event.getProperty().getValue();
                if (project != null) {
                    updateApexProperties(project);
                    changeActionHandler(project);
                }
                refreshTreeTable();
            }

            private void updateApexProperties(Project project) {
                ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
                long userUniqueId = ApplicationSession.getUser().getUniqueId();
                apexPropertiesDbService.createOrUpdate(userUniqueId, PROJECTBACKLOGPANEL_PROJECTLIST_ID, project.getUniqueId());
            }

            private void changeActionHandler(Project project) {
                treeTable.removeActionHandler(projectBacklogTreeActionHandler);
                UserHelper userUtil = new UserHelper();
                projectBacklogTreeActionHandler = new TaskTreeActionHandler(ProjectBacklogPanel.this, treeTable, project, userUtil.hasSprintPlannerPrivileges());
                treeTable.addActionHandler(projectBacklogTreeActionHandler);
            }
        });
        layout.addComponent(projectsList, COLUMN_ONE, ROW_ONE);
    }

    private void createAndSetTreeTableAttributes() {
        treeTable = new TaskTreeTable("" + serialVersionUID, "Product Backlog");
        treeTable.setSizeFull();
        treeTable.setSelectable(true);
        treeTable.setMultiSelect(false);
        treeTable.setImmediate(true);
        treeTable.setSortDisabled(true);
        treeTable.setColumnReorderingAllowed(false);
        treeTable.setDragMode(TableDragMode.ROW);
        treeTable.setFooterVisible(true);
        layout.addComponent(treeTable, COLUMN_ONE, ROW_TWO, COLUMN_THREE, ROW_TWO);
    }

    private void createColumns() {
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
    }

    private void setAdvancedFunctionalities() {
        boolean hasEditAuthorization = false;
        UserHelper userUtil = new UserHelper();
        if (userUtil.hasSprintPlannerPrivileges()) {
            hasEditAuthorization = true;
            treeTable.setDropHandler(new TreeTableDropHandler(treeTable));
        }
        treeTable.addListener(new TreeTableItemClickListener(this, treeTable, hasEditAuthorization));
    }

    private void refreshFromApexProperties() {
        logger.debug("Refreshing from user memory in the project backlog panel.");
        Project selectedProject = getSelectedProject();
        if (selectedProject != null) {
            projectsList.select(selectedProject);
        }
    }

    private void refreshProjects() {
        logger.debug("Refreshing projects of the project backlog panel.");
        projectsList.removeAllItems();
        ProjectPersistence projectDbService = new ProjectPersistenceImpl();
        List<Project> projects = projectDbService.getAll();
        BeanItemContainer<Project> container = new BeanItemContainer<Project>(Project.class);
        if (projects != null) {
            container.addAll(projects);
        }
        projectsList.setContainerDataSource(container);
    }

    private void refreshTreeTable() {
        logger.debug("Refreshing tree table of the project backlog panel.");
        treeTable.removeAllItems();
        Project project = getSelectedProject();
        if (project != null) {
            project.fetchSecondLevelObjects();
            ProductBacklog productBacklog = project.getProductBacklog();
            productBacklog.fetchSecondLevelObjects();
            Task rootTask = productBacklog.getRootTask();
            rootTask.fetchSecondLevelObjects();
            treeTable.setRootTask(rootTask);
            treeTable.refresh();
        }
    }

    private Project getSelectedProject() {
        Project project = null;
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        long userUniqueId = ApplicationSession.getUser().getUniqueId();
        Long projectUniqueId = apexPropertiesDbService.getLong(userUniqueId, PROJECTBACKLOGPANEL_PROJECTLIST_ID);
        if (projectUniqueId != null && projectUniqueId >= 0) {
            DomainObjectVaadinHelper domainUtil = new DomainObjectVaadinHelper();
            project = (Project) domainUtil.getObjectFromSelect(projectsList, projectUniqueId);
        }
        return project;
    }
}

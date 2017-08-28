package com.agileapex.ui.planning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Project;
import com.agileapex.domain.Sprint;
import com.agileapex.domain.Task;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.treetable.TaskTreeActionHandler;
import com.agileapex.ui.common.treetable.TaskTreeTable;
import com.agileapex.ui.common.treetable.TreeTableDropHandler;
import com.agileapex.ui.common.treetable.TreeTableItemClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table.TableDragMode;

public class SprintBacklogLayout extends GridLayout implements RefreshableComponent, Constants {
    private static final Logger logger = LoggerFactory.getLogger(SprintBacklogLayout.class);
    private static final long serialVersionUID = -2214156344514508935L;
    private TaskTreeTable treeTable;

    public SprintBacklogLayout() {
        super(1, 1);
        createLayout();
        createAndSetTreeTableAttributes();
        createTreeTableColumns();
        refresh();
    }

    @Override
    public void attach() {
        setAdvancedFunctionalities();
    }

    private void createLayout() {
        setSizeFull();
        setMargin(false, true, false, false);
        setSpacing(false);
    }

    private void createAndSetTreeTableAttributes() {
        treeTable = new TaskTreeTable("" + serialVersionUID, "Sprint backlog");
        treeTable.setDebugId(PLANNING_SPRINT_BACKLOG);                
        treeTable.setSizeFull();
        treeTable.setSelectable(true);
        treeTable.setMultiSelect(false);
        treeTable.setImmediate(true);
        treeTable.setSortDisabled(true);
        treeTable.setColumnReorderingAllowed(false);
        treeTable.setDragMode(TableDragMode.ROW);
        treeTable.setFooterVisible(true);
        addComponent(treeTable, COLUMN_ONE, ROW_ONE);
    }

    private void createTreeTableColumns() {
        treeTable.addContainerProperty("identifier", String.class, "");
        treeTable.addContainerProperty("name", String.class, "");
        treeTable.addContainerProperty("effort", String.class, "");
        treeTable.setColumnHeader("identifier", "Id");
        treeTable.setColumnHeader("name", "Name");
        treeTable.setColumnHeader("effort", "Effort");
        treeTable.setVisibleColumns(new Object[] { "identifier", "name", "effort" });
        treeTable.setColumnExpandRatio("identifier", 1);
        treeTable.setColumnExpandRatio("name", 5);
        treeTable.setColumnExpandRatio("effort", 1);
    }

    private void setAdvancedFunctionalities() {
        boolean canEdit = false;
        UserHelper userUtil = new UserHelper();
        if (userUtil.hasSprintPlannerPrivileges()) {
            canEdit = true;
            treeTable.setDropHandler(new TreeTableDropHandler(treeTable));
        }
        Project project = ApplicationSession.getSessionDataHelper().getCurrentProject();
        project.fetchSecondLevelObjects();
        treeTable.addActionHandler(new TaskTreeActionHandler(this, treeTable, project, canEdit));
        treeTable.addListener(new TreeTableItemClickListener(this, treeTable, canEdit));
    }

    private Task getRootTask() {
        Task rootTask = null;
        Sprint currentSprint = ApplicationSession.getSessionDataHelper().getCurrentSprint();
        if (currentSprint != null) {
            currentSprint.fetchSecondLevelObjects();
            rootTask = currentSprint.getRootTask();
            rootTask.fetchSecondLevelObjects();
        }
        return rootTask;
    }

    @Override
    public void refresh() {
        logger.debug("Refreshing sprint backlog layout.");
        treeTable.setRootTask(getRootTask());
        treeTable.refresh();
    }
}

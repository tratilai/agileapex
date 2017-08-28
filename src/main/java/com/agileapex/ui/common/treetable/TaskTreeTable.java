package com.agileapex.ui.common.treetable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.ClassHelper;
import com.agileapex.common.task.OrderTasksByChildrenComparator;
import com.agileapex.domain.Effort;
import com.agileapex.domain.Task;
import com.agileapex.persistence.EffortPersistence;
import com.agileapex.persistence.EffortPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.CollapseListener;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;
import com.vaadin.ui.TreeTable;

public class TaskTreeTable extends TreeTable implements RefreshableComponent, Constants {
    private static final Logger logger = LoggerFactory.getLogger(TaskTreeTable.class);
    private static final long serialVersionUID = -5572625325541811763L;
    private Task rootTask;
    private Map<Long, Boolean> expandedNodes = new HashMap<Long, Boolean>();
    private final String treeTableUniqueId;

    public TaskTreeTable(String treeTableUniqueId, String caption) {
        super(caption);
        this.treeTableUniqueId = treeTableUniqueId;
        if (ApplicationSession.getSessionDataHelper().getTreeTableExpandProperty(treeTableUniqueId) != null) {
            expandedNodes = ApplicationSession.getSessionDataHelper().getTreeTableExpandProperty(treeTableUniqueId);
        }
        addListener(getCollapseListener());
        addListener(getExpandListener());
    }

    @Override
    public void refresh() {
        ClassHelper classHelper = new ClassHelper();
        logger.debug("Tree table refresh. Called from: {}", classHelper.getCallerClassName());
        removeAllItems();
        if (rootTask != null && !rootTask.isLeaf()) {
            rootTask.setParent(null);
            EffortPersistence effortDbService = new EffortPersistenceImpl();
            calculateAndSetEffortsRecursively(rootTask, effortDbService);
            createTreeTableStructure();
            setColumnFooter("effort", getRootTask().getEffortAsText());
        }
    }

    public Long calculateAndSetEffortsRecursively(Task task, EffortPersistence effortDbService) {
        task.fetchSecondLevelObjects();
        if (task.isLeaf()) {
            return calculateLeaf(task, effortDbService);
        } else {
            return calculateNonLeaf(task, effortDbService);
        }
    }

    private Long calculateLeaf(Task task, EffortPersistence effortDbService) {
        Effort effortToUpdate = task.getEffort();
        boolean hasSumOfEffortValue = hasSumOfEffortValue(effortToUpdate);
        boolean isEffortLeftValueChanged = hasEffortLeftValueChanged(effortDbService, effortToUpdate);
        if (isEffortLeftValueChanged || hasSumOfEffortValue) {
            effortToUpdate.setSumOfEffortLeft(null);
            effortDbService.update(effortToUpdate, task, ApplicationSession.getUser());
        }
        return effortToUpdate.getEffortLeft();
    }

    private Long calculateNonLeaf(Task task, EffortPersistence effortDbService) {
        Long effortSum = null;
        for (Task childTask : task.getChildren()) {
            Long tempEffort = calculateAndSetEffortsRecursively(childTask, effortDbService);
            if (tempEffort != null) {
                if (effortSum == null) {
                    effortSum = 0L;
                }
                effortSum += tempEffort;
            }
        }
        Effort effortToUpdate = task.getEffort();
        boolean hasEffortLeftValue = hasEffortLeftValue(effortToUpdate);
        boolean isSumOfEffortValueChanged = hasSumOfEffortValueChanged(effortSum, effortToUpdate);
        if (hasEffortLeftValue || isSumOfEffortValueChanged) {
            effortToUpdate.setEffortLeft(null);
            effortToUpdate.setSumOfEffortLeft(effortSum);
            effortDbService.update(effortToUpdate, task, ApplicationSession.getUser());
        }
        return effortSum;
    }

    private boolean hasSumOfEffortValue(Effort effortToUpdate) {
        return effortToUpdate.getSumOfEffortLeft() != null;
    }

    private boolean hasEffortLeftValueChanged(EffortPersistence effortDbService, Effort effortToUpdate) {
        boolean hasEffortLeftValueChanged = true;
        Effort oldEffort = effortDbService.get(effortToUpdate.getUniqueId());
        if (oldEffort != null && oldEffort.getEffortLeft() != null) {
            hasEffortLeftValueChanged = !oldEffort.getEffortLeft().equals(effortToUpdate.getEffortLeft());
        }
        return hasEffortLeftValueChanged;
    }

    private boolean hasEffortLeftValue(Effort effortToUpdate) {
        boolean hasEffortLeftValue = false;
        if (effortToUpdate != null) {
            hasEffortLeftValue = effortToUpdate.getEffortLeft() != null;
        }
        return hasEffortLeftValue;
    }

    private boolean hasSumOfEffortValueChanged(Long effortSum, Effort effortToUpdate) {
        boolean hasSumOfEffortValueChanged = false;
        if (effortToUpdate == null && effortSum != null && effortSum >= 0) {
            hasSumOfEffortValueChanged = true;
        } else if (effortToUpdate != null) {
            if (effortToUpdate.getSumOfEffortLeft() == null && effortSum != null && effortSum >= 0) {
                hasSumOfEffortValueChanged = true;
            } else if (effortToUpdate.getSumOfEffortLeft() != null) {
                hasSumOfEffortValueChanged = !effortToUpdate.getSumOfEffortLeft().equals(effortSum);
            }
        }
        return hasSumOfEffortValueChanged;
    }

    private Object createTreeTableItem(Task task) {
        String id = task.getIdentifier();
        Label name = new Label(task.getName());
        String effort = task.getEffortAsText();
        String userFullName = "";
        if (task.getAssigned() != null) {
            userFullName = task.getAssigned().getFullName();
        }
        String status = task.getStatus().getStatus();

        List<Object> visibleColumns = Arrays.asList(getVisibleColumns());
        Object itemId = null;
        if (visibleColumns.contains("status")) {
            itemId = addItem(new Object[] { id, name, userFullName, status, effort }, task);
        } else {
            itemId = addItem(new Object[] { id, name, effort }, task);
        }
        if (itemId == null) {
            logger.debug("Error when adding a row to the treetable. id: {}", id);
        } else {
            if (expandedNodes.containsKey(task.getUniqueId()) && expandedNodes.get(task.getUniqueId())) {
                setCollapsed(itemId, false);
            } else {
                setCollapsed(itemId, true);
            }
        }
        return itemId;
    }

    private void createTreeTableStructure() {
        for (Task rootLevelTask : rootTask.getChildren()) {
            rootLevelTask.setParent(rootTask);
            Object treeTableFirstLevelItem = createTreeTableItem(rootLevelTask);
            setChildrenAllowed(treeTableFirstLevelItem, false);
            refreshByRecursion(rootLevelTask, treeTableFirstLevelItem);
        }
    }

    private void refreshByRecursion(Task task, Object treeTableItem) {
        Collections.sort(task.getChildren(), new OrderTasksByChildrenComparator());
        for (Task childTask : task.getChildren()) {
            Object treeTableChildItem = createTreeTableItem(childTask);
            setChildrenAllowed(treeTableItem, true);
            if (!setParent(treeTableChildItem, treeTableItem)) {
                logger.error("Setting parent item to tree table item failed. item: {} parent item: {}", treeTableChildItem, treeTableItem);
            }
            if (childTask.isLeaf()) {
                if (!setChildrenAllowed(treeTableChildItem, false)) {
                    logger.error("Setting tree table's setChildrenAllowed(..) method failed. child item: {}", treeTableChildItem);
                }
            } else {
                refreshByRecursion(childTask, treeTableChildItem);
            }
        }
    }

    private CollapseListener getCollapseListener() {
        return new CollapseListener() {
            private static final long serialVersionUID = -8551007800740883210L;

            @Override
            public void nodeCollapse(CollapseEvent event) {
                Task task = (Task) event.getItemId();
                if (expandedNodes.containsKey(task.getUniqueId())) {
                    expandedNodes.put(task.getUniqueId(), false);
                    ApplicationSession.getSessionDataHelper().putTreeTableExpandProperty(treeTableUniqueId, expandedNodes);
                }
            }
        };
    }

    private ExpandListener getExpandListener() {
        return new ExpandListener() {
            private static final long serialVersionUID = -7312499261557319969L;

            @Override
            public void nodeExpand(ExpandEvent event) {
                Task task = (Task) event.getItemId();
                expandedNodes.put(task.getUniqueId(), true);
                ApplicationSession.getSessionDataHelper().putTreeTableExpandProperty(treeTableUniqueId, expandedNodes);
            }
        };
    }

    public Task getRootTask() {
        return rootTask;
    }

    public void setRootTask(Task rootTask) {
        this.rootTask = rootTask;
    }
}

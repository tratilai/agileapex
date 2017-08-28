package com.agileapex.ui.common.treetable;

import java.util.List;

import com.agileapex.common.task.TaskHelper;
import com.agileapex.domain.Task;
import com.agileapex.persistence.TaskPersistence;
import com.agileapex.persistence.TaskPersistenceImpl;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;

public class TreeTableDropHandler implements DropHandler {
    private static final long serialVersionUID = -3427906513356453744L;
    private final TaskTreeTable treeTable;

    public TreeTableDropHandler(TaskTreeTable treeTable) {
        this.treeTable = treeTable;
    }

    @Override
    public void drop(DragAndDropEvent event) {
        Transferable transferable = event.getTransferable();
        TaskTreeTable sourceTreeTable = (TaskTreeTable) transferable.getSourceComponent();
        AbstractSelectTargetDetails targetDetails = (AbstractSelectTargetDetails) event.getTargetDetails();
        Task sourceTask = (Task) transferable.getData("itemId");
        if (sourceTask != null) {
            sourceTask.fetchSecondLevelObjects();
            if (sourceTask != null) {
                Task targetTask = (Task) targetDetails.getItemIdOver();
                if (targetTask == null) {
                    targetTask = treeTable.getRootTask();
                }
                targetTask.fetchSecondLevelObjects();
                dropSourceTaskToTargetPosition(sourceTreeTable, targetDetails, sourceTask, targetTask);
            }
        }
    }

    private void dropSourceTaskToTargetPosition(TaskTreeTable sourceTreeTable, AbstractSelectTargetDetails targetDetails, Task sourceTask, Task targetTask) {
        TaskHelper taskUtil = new TaskHelper();
        boolean sourceAndTargetAreInSameSubTree = taskUtil.isTaskPartOfTheGivenTree(sourceTask, targetTask);
        if (!sourceAndTargetAreInSameSubTree) {
            moveAndUpdateTasks(sourceTreeTable, targetDetails, sourceTask, targetTask);
        }
    }

    private void moveAndUpdateTasks(TaskTreeTable sourceTreeTable, AbstractSelectTargetDetails targetDetails, Task sourceTask, Task targetTask) {
        Task targetParent = getTargetParent(targetDetails, targetTask);
        Task sourceParent = getSourceParent(sourceTask);
        int sourceIndex = sourceParent.getChildren().indexOf(sourceTask);
        int targetIndex = targetParent.getChildren().indexOf(targetTask);
        List<Task> sourceChildren = sourceParent.getChildren();
        sourceChildren.remove(sourceTask);
        List<Task> targetChildren = sourceChildren;
        if (!sourceParent.equals(targetParent)) {
            udpateChildrenTasks(sourceChildren);
            sourceTask.setParent(targetParent);
            targetChildren = targetParent.getChildren();
        }
        targetIndex = adjustTargetIndexByVerticalDroppingLocation(targetDetails, sourceIndex, targetIndex);
        addTaskToNewLocation(sourceTask, targetIndex, targetChildren);
        udpateChildrenTasks(targetChildren);
        AutomaticStatusUpdater statusUpdater = new AutomaticStatusUpdater();
        sourceParent.fetchSecondLevelObjects();
        statusUpdater.updateTaskTreeStatuses(sourceParent, true);
        statusUpdater.updateTaskTreeStatuses(sourceTask, false);
        refreshTaskTrees(sourceTreeTable);
    }

    private Task getTargetParent(AbstractSelectTargetDetails targetDetails, Task targetTask) {
        Task targetParent = targetTask;
        if (targetDetails.getDropLocation() != VerticalDropLocation.MIDDLE && targetTask.getParent() != null) {
            targetParent = targetTask.getParent();
            targetParent.fetchSecondLevelObjects();
        }
        return targetParent;
    }

    private Task getSourceParent(Task sourceTask) {
        Task sourceParent = sourceTask.getParent();
        sourceParent.fetchSecondLevelObjects();
        return sourceParent;
    }

    private void udpateChildrenTasks(List<Task> children) {
        TaskPersistence taskDbService = new TaskPersistenceImpl();
        for (int index = 0; index < children.size(); ++index) {
            Task task = children.get(index);
            task.setOrderInChildren(index + 1);
            taskDbService.update(task);
        }
    }

    private int adjustTargetIndexByVerticalDroppingLocation(AbstractSelectTargetDetails targetDetails, int sourceIndex, int targetIndex) {
        if (targetDetails.getDropLocation() == VerticalDropLocation.TOP) {
            if (sourceIndex < targetIndex) {
                targetIndex = targetIndex - 1;
            }
        } else if (targetDetails.getDropLocation() == VerticalDropLocation.BOTTOM) {
            if (sourceIndex > targetIndex) {
                targetIndex = targetIndex + 1;
            }
        }
        return targetIndex;
    }

    private void addTaskToNewLocation(Task sourceTask, int targetIndex, List<Task> children) {
        if (targetIndex >= 0) {
            children.add(targetIndex, sourceTask);
        } else {
            children.add(sourceTask);
        }
    }

    private void refreshTaskTrees(TaskTreeTable sourceTreeTable) {
        treeTable.getRootTask().fetchSecondLevelObjects();
        treeTable.refresh();
        sourceTreeTable.getRootTask().fetchSecondLevelObjects();
        sourceTreeTable.refresh();
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }
}

package com.agileapex.ui.common.treetable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Task;
import com.agileapex.ui.common.task.edit.EditTaskPopup;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractComponent;

public class TreeTableItemClickListener implements ItemClickEvent.ItemClickListener {
    private static final Logger logger = LoggerFactory.getLogger(TreeTableItemClickListener.class);
    private static final long serialVersionUID = 8917743107041008180L;
    private final AbstractComponent parentComponent;
    private final TaskTreeTable treeTable;
    private final boolean hasAuthorizationToEdit;

    public TreeTableItemClickListener(AbstractComponent parentComponent, TaskTreeTable treeTable, boolean hasAuthorizationToEdit) {
        this.parentComponent = parentComponent;
        this.treeTable = treeTable;
        this.hasAuthorizationToEdit = hasAuthorizationToEdit;
    }

    @Override
    public void itemClick(ItemClickEvent event) {
        Object itemId = event.getItemId();
        if (itemId != null && itemId instanceof Task) {
            Task task = (Task) itemId;
            if (event.getButton() == ItemClickEvent.BUTTON_RIGHT) {
                logger.debug("Task tree table item selected. Right clicked.");
                treeTable.setValue(null);
                treeTable.select(event.getItemId());
            } else if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
                if (event.isDoubleClick()) {
                    logger.debug("Task tree table item opened. Double left clicked.");
                    EditTaskPopup popup = new EditTaskPopup(treeTable, task, hasAuthorizationToEdit);
                    parentComponent.getWindow().addWindow(popup);
                }
            }
        }
    }
}

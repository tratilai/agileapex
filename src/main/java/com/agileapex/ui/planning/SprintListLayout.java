package com.agileapex.ui.planning;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.persistence.ApexPropertyPersistence;
import com.agileapex.persistence.ApexPropertyPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Select;

public class SprintListLayout extends GridLayout implements RefreshableComponent, Constants {
    private static final Logger logger = LoggerFactory.getLogger(SprintListLayout.class);
    private static final long serialVersionUID = -5703859399765324635L;
    private SprintPlanningPanel sprintsPlanningPanel;
    private NativeSelect sprintsSelectList;

    public SprintListLayout(SprintPlanningPanel sprintsPlanningPanel) {
        super(1, 2);
        this.sprintsPlanningPanel = sprintsPlanningPanel;
        createLayout();
        createTitle();
        createSprintList();
    }

    @Override
    public void attach() {
        refresh();
        selectCurrentSprint();
    }

    private void createLayout() {
        setWidth(100.0f, UNITS_PERCENTAGE);
        setMargin(false, true, false, false);
        setSpacing(false);
        setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1000);
    }

    private void createTitle() {
        Label title = new Label("Sprint");
        addComponent(title, COLUMN_ONE, ROW_ONE);
    }

    private void createSprintList() {
        sprintsSelectList = new NativeSelect();
        sprintsSelectList.setDebugId(PLANNING_SPRINT_LIST);
        sprintsSelectList.setNullSelectionAllowed(false);
        sprintsSelectList.setImmediate(true);
        sprintsSelectList.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        sprintsSelectList.setItemCaptionPropertyId("name");
        sprintsSelectList.setWidth(100, UNITS_PERCENTAGE);
        sprintsSelectList.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -3576347502617523115L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Sprint selectedSprint = (Sprint) event.getProperty().getValue();
                if (!selectedSprint.equals(ApplicationSession.getSessionDataHelper().getCurrentSprint())) {
                    ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
                    long userUniqueId = ApplicationSession.getUser().getUniqueId();
                    apexPropertiesDbService.createOrUpdate(userUniqueId, SPRINTPAGE_SPRINTLIST_ID, selectedSprint.getUniqueId());
                    ApplicationSession.getSessionDataHelper().setCurrentSprint(selectedSprint);
                    sprintsPlanningPanel.refreshSprintBacklog();
                }
            }
        });
        addComponent(sprintsSelectList, COLUMN_ONE, ROW_TWO);
    }

    @Override
    public void refresh() {
        logger.debug("Refreshing sprint list layout.");
        sprintsSelectList.removeAllItems();
        Sprint currentSprint = ApplicationSession.getSessionDataHelper().getCurrentSprint();
        currentSprint.fetchSecondLevelObjects();
        Release parentRelease = currentSprint.getParentRelease();
        parentRelease.fetchSecondLevelObjects();
        List<Sprint> sprints = parentRelease.getSprints();
        for (Sprint sprint : sprints) {
            sprint.fetchSecondLevelObjects();
        }
        BeanItemContainer<Sprint> container = new BeanItemContainer<Sprint>(Sprint.class);
        if (sprints != null) {
            container.addAll(sprints);
        }
        sprintsSelectList.setContainerDataSource(container);
    }

    protected void selectCurrentSprint() {
        Sprint currentSprint = ApplicationSession.getSessionDataHelper().getCurrentSprint();
        @SuppressWarnings("unchecked")
        Collection<Sprint> sprints = (Collection<Sprint>) sprintsSelectList.getItemIds();
        for (Sprint sprint : sprints) {
            if (sprint.equals(currentSprint)) {
                sprintsSelectList.select(sprint);
            }
        }
    }
}

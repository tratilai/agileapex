package com.agileapex.ui.sprint;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.persistence.ApexPropertyPersistence;
import com.agileapex.persistence.ApexPropertyPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.agileapex.ui.common.vaadin.DomainObjectVaadinHelper;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class SprintPanel extends Panel implements RefreshableComponent, Constants {
    private static final Logger logger = LoggerFactory.getLogger(SprintPanel.class);
    private static final long serialVersionUID = -8530003119133686765L;
    protected HorizontalSplitPanel mainLayout;
    protected Table sprintsTable;
    protected SprintExtraInfoPanel extraInfoPanel;

    public SprintPanel() {
        init();
        createSprintsTable();
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

    private void createSprintsTable() {
        sprintsTable = new Table();
        sprintsTable.setDebugId(SPRINTPAGE_SPRINTS_TABLE);
        sprintsTable.setSizeFull();
        sprintsTable.setSelectable(true);
        sprintsTable.setMultiSelect(false);
        sprintsTable.setImmediate(true);
        sprintsTable.setColumnReorderingAllowed(true);
        BeanItemContainer<Sprint> container = new BeanItemContainer<Sprint>(Sprint.class);
        container.addNestedContainerProperty("name");
        sprintsTable.setContainerDataSource(container);
        sprintsTable.setColumnHeader("name", "Sprint name");
        sprintsTable.setColumnExpandRatio("name", EXPAND_RATIO_1);
        sprintsTable.setVisibleColumns(new Object[] { "name" });
        sprintsTable.setSortContainerPropertyId("name");
        sprintsTable.addListener(new SprintTableItemClickListener(this));
        sprintsTable.addListener(new SprintTableValueChangeListener(this));
        mainLayout.setFirstComponent(sprintsTable);
    }

    private void createExtraInfoLayout() {
        extraInfoPanel = new SprintExtraInfoPanel(this);
        mainLayout.setSecondComponent(extraInfoPanel);

    }

    private void setAdvancedFunctionalities() {
        UserHelper userUtil = new UserHelper();
        if (userUtil.hasSprintPlannerPrivileges()) {
            Release currentRelease = ApplicationSession.getSessionDataHelper().getCurrentRelease();
            currentRelease.fetchSecondLevelObjects();
            sprintsTable.addActionHandler(new SprintTableActionHandler(this, currentRelease));
        }
    }

    protected void doSelectionActions(Sprint sprint) {
        ApplicationSession.getSessionDataHelper().setCurrentSprint(sprint);
        setExtraInfoLayoutDataAndTitles(sprint);
        saveSelectionToApexProperties(sprint);
    }

    private void setExtraInfoLayoutDataAndTitles(Sprint sprint) {
        extraInfoPanel.viewExtraInfoLayoutTitles(true);
        extraInfoPanel.setExtraInfoLayoutData(sprint);
    }

    private void saveSelectionToApexProperties(Sprint sprint) {
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        long userUniqueId = ApplicationSession.getUser().getUniqueId();
        apexPropertiesDbService.createOrUpdate(userUniqueId, SPRINTPAGE_SPRINTLIST_ID, sprint.getUniqueId());
    }

    private void clearExtraInfoLayout() {
        extraInfoPanel.viewExtraInfoLayoutTitles(false);
        extraInfoPanel.setExtraInfoLayoutData(null);
    }

    private void refreshFromApexProperties() {
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        long userUniqueId = ApplicationSession.getUser().getUniqueId();
        Long sprintUniqueId = apexPropertiesDbService.getLong(userUniqueId, SPRINTPAGE_SPRINTLIST_ID);
        if (sprintUniqueId != null && sprintUniqueId >= 0) {
            DomainObjectVaadinHelper domainUtil = new DomainObjectVaadinHelper();
            Sprint selectedSprint = (Sprint) domainUtil.getObjectFromSelect(sprintsTable, sprintUniqueId);
            if (selectedSprint != null) {
                selectedSprint.fetchSecondLevelObjects();
                sprintsTable.select(selectedSprint);
                sprintsTable.setCurrentPageFirstItemId(selectedSprint);
                doSelectionActions(selectedSprint);
            }
        }
    }

    @Override
    public void refresh() {
        logger.debug("Refreshing sprint panel.");
        clearExtraInfoLayout();
        sprintsTable.removeAllItems();
        Release release = ApplicationSession.getSessionDataHelper().getCurrentRelease();
        release.fetchSecondLevelObjects();
        List<Sprint> sprints = release.getSprints();
        if (sprints != null) {
            for (Sprint sprint : sprints) {
                sprint.fetchSecondLevelObjects();
                sprintsTable.addItem(sprint);
            }
            sprintsTable.sort();
        }
    }
}

package com.agileapex.ui.release;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
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

public class ReleasePanel extends Panel implements RefreshableComponent, Constants {
    private static final Logger logger = LoggerFactory.getLogger(ReleasePanel.class);
    private static final long serialVersionUID = 99992980156599137L;
    protected HorizontalSplitPanel mainLayout;
    protected Table releasesTable;
    protected ReleaseExtraInfoPanel extraInfoPanel;

    public ReleasePanel() {
        init();
    }

    @Override
    public void attach() {
        createReleasesTable();
        createExtraInfoLayout();
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

    private void createReleasesTable() {
        releasesTable = new Table();
        releasesTable.setDebugId(RELEASEPAGE_RELEASES_TABLE);
        releasesTable.setSizeFull();
        releasesTable.setSelectable(true);
        releasesTable.setMultiSelect(false);
        releasesTable.setImmediate(true);
        releasesTable.setColumnReorderingAllowed(true);
        BeanItemContainer<Release> container = new BeanItemContainer<Release>(Release.class);
        container.addNestedContainerProperty("name");
        container.addNestedContainerProperty("status.status");
        releasesTable.setContainerDataSource(container);
        releasesTable.setColumnHeader("name", "Release name");
        releasesTable.setColumnExpandRatio("name", EXPAND_RATIO_4);
        releasesTable.setColumnHeader("status.status", "Status");
        releasesTable.setColumnExpandRatio("status.status", EXPAND_RATIO_1);
        releasesTable.setVisibleColumns(new Object[] { "name", "status.status" });
        releasesTable.setSortContainerPropertyId("name");
        releasesTable.addListener(new ReleaseTableItemClickListener(this));
        releasesTable.addListener(new ReleaseTableValueChangeListener(this));
        mainLayout.setFirstComponent(releasesTable);
    }

    private void createExtraInfoLayout() {
        extraInfoPanel = new ReleaseExtraInfoPanel(this);
        mainLayout.setSecondComponent(extraInfoPanel);
    }

    private void setAdvancedFunctionalities() {
        UserHelper userUtil = new UserHelper();
        if (userUtil.hasManagerPrivileges()) {
            releasesTable.addActionHandler(new ReleaseTableActionHandler(this));
        }
    }

    private void setExtraInfoLayoutDataAndTitles(Release release) {
        extraInfoPanel.viewExtraInfoLayoutTitles(true);
        extraInfoPanel.setExtraInfoLayoutData(release);
    }

    protected void doSelectionActions(Release release) {
        ApplicationSession.getSessionDataHelper().setCurrentRelease(release);
        setExtraInfoLayoutDataAndTitles(release);
        saveSelectionToApexProperties(release);
    }

    private void saveSelectionToApexProperties(Release release) {
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        long userUniqueId = ApplicationSession.getUser().getUniqueId();
        apexPropertiesDbService.createOrUpdate(userUniqueId, RELEASEPAGE_RELEASELIST_ID, release.getUniqueId());
    }

    private void clearExtraInfoLayout() {
        extraInfoPanel.viewExtraInfoLayoutTitles(false);
        extraInfoPanel.setExtraInfoLayoutData(null);
    }

    private void refreshFromApexProperties() {
        ApexPropertyPersistence apexPropertiesDbService = new ApexPropertyPersistenceImpl();
        long userUniqueId = ApplicationSession.getUser().getUniqueId();       
        Long releaseUniqueId = apexPropertiesDbService.getLong(userUniqueId, RELEASEPAGE_RELEASELIST_ID);
        if (releaseUniqueId != null && releaseUniqueId >= 0) {
            DomainObjectVaadinHelper domainUtil = new DomainObjectVaadinHelper();
            Release selectedRelease = (Release) domainUtil.getObjectFromSelect(releasesTable, releaseUniqueId);
            if (selectedRelease != null) {
                setExtraInfoLayoutDataAndTitles(selectedRelease);
                releasesTable.select(selectedRelease);
                releasesTable.setCurrentPageFirstItemId(selectedRelease);
            }
        }
    }

    @Override
    public void refresh() {
        logger.debug("Refreshing release panel.");
        clearExtraInfoLayout();
        releasesTable.removeAllItems();
        Project project = ApplicationSession.getSessionDataHelper().getCurrentProject();
        project.fetchSecondLevelObjects();
        List<Release> releases = project.getReleases();
        if (releases != null) {
            for (Release release : releases) {
                release.fetchSecondLevelObjects();
                releasesTable.addItem(release);
            }
            releasesTable.sort();
        }
    }
}

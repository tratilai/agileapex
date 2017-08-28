package com.agileapex.ui.report.sprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.common.taskhistory.TaskHistoryEffortCalculator;
import com.agileapex.domain.Project;
import com.agileapex.domain.Release;
import com.agileapex.domain.Sprint;
import com.agileapex.persistence.ProjectPersistence;
import com.agileapex.persistence.ProjectPersistenceImpl;
import com.agileapex.persistence.SprintPersistence;
import com.agileapex.persistence.SprintPersistenceImpl;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.common.RefreshableComponent;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Select;

public class SprintBurndownReport extends GridLayout implements Constants, RefreshableComponent {
    private static final long serialVersionUID = -970285132830579325L;
    private static final Logger logger = LoggerFactory.getLogger(SprintBurndownReport.class);
    private DateAndTimeUtil dateUtil = new DateAndTimeUtil();
    protected List<Project> projects;
    protected NativeSelect projectsList;
    protected NativeSelect releasesList;
    protected NativeSelect sprintsList;
    private MenuBar actionMenu;
    private Chart chart;
    private Configuration configuration;
    private boolean showWeekends;
    private Sprint sprintInChart;
    private List<Number> yDataList;
    private ArrayList<String> xDataList;
    private Button generateButton;

    public SprintBurndownReport() {
        super(1, 4);
        init();
        addReportFilterPanel();
        refreshProjects();
    }

    private void init() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        setRowExpandRatio(ROW_ONE, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_TWO, EXPAND_RATIO_1);
        setRowExpandRatio(ROW_THREE, EXPAND_RATIO_1000);
        setRowExpandRatio(ROW_FOUR, EXPAND_RATIO_1);
    }

    private void addReportFilterPanel() {
        addProjectList();
        addReleaseList();
        addSprintList();
        addGenerateButton();
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSizeFull();
        filterLayout.setSpacing(true);
        filterLayout.setMargin(false);
        filterLayout.addComponent(projectsList);
        filterLayout.setExpandRatio(projectsList, EXPAND_RATIO_1000);
        filterLayout.setComponentAlignment(projectsList, Alignment.BOTTOM_LEFT);
        filterLayout.addComponent(releasesList);
        filterLayout.setExpandRatio(releasesList, EXPAND_RATIO_1000);
        filterLayout.setComponentAlignment(releasesList, Alignment.BOTTOM_LEFT);
        filterLayout.addComponent(sprintsList);
        filterLayout.setExpandRatio(sprintsList, EXPAND_RATIO_1000);
        filterLayout.setComponentAlignment(sprintsList, Alignment.BOTTOM_LEFT);
        filterLayout.addComponent(generateButton);
        filterLayout.setExpandRatio(generateButton, EXPAND_RATIO_1);
        filterLayout.setComponentAlignment(generateButton, Alignment.BOTTOM_LEFT);
        addComponent(filterLayout, COLUMN_ONE, ROW_ONE);
    }

    private void addProjectList() {
        projectsList = new NativeSelect("Project");
        projectsList.setNullSelectionAllowed(false);
        projectsList.setImmediate(true);
        projectsList.setWidth(THE_100_PERCENT, UNITS_PERCENTAGE);
        projectsList.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        projectsList.setItemCaptionPropertyId("name");
        projectsList.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -1234367217232949808L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                generateButton.setEnabled(false);
                Project project = (Project) event.getProperty().getValue();
                refreshReleases(project);
            }
        });
    }

    private void addReleaseList() {
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
                generateButton.setEnabled(false);
                Release release = (Release) event.getProperty().getValue();
                refreshSprints(release);
            }
        });
    }

    private void addSprintList() {
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
                sprintInChart = (Sprint) event.getProperty().getValue();
                generateButton.setEnabled(true);
            }
        });
    }

    private void addGenerateButton() {
        generateButton = new Button("Generate");
        generateButton.setClickShortcut(KeyCode.ENTER);
        generateButton.addStyleName("primary");
        generateButton.setEnabled(false);
        generateButton.addListener(new ClickListener() {
            private static final long serialVersionUID = 4237686428030676191L;

            @Override
            public void buttonClick(ClickEvent event) {
                generateButton.setEnabled(false);
                logger.debug("Generate report button pressed for sprint {}", sprintInChart.getUniqueId());
                if (chart == null) {
                    createChart(sprintInChart);
                } else {
                    removeComponent(chart);
                    addChart(sprintInChart);
                }
                generateButton.setEnabled(true);
            }
        });
    }

    private void refreshProjects() {
        logger.debug("Refreshing project list in the report.");
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
        logger.debug("Refreshing release list in the report.");
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
        logger.debug("Refreshing sprint list in the reports.");
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

    private void createChart(Sprint sprint) {
        logger.debug("Creating chart for the sprint {}", sprint.getUniqueId());
        addSpacer();
        addChart(sprint);
        addActionMenu();
    }

    private void addSpacer() {
        Label spacer = new Label("&nbsp;", Label.CONTENT_XHTML);
        spacer.setHeight("40px");
        addComponent(spacer, COLUMN_ONE, ROW_TWO);
    }

    private void addChart(Sprint sprint) {
        chart = new Chart(ChartType.SPLINE);
        chart.setWidth("100%");
        chart.setHeight("450px");
        configuration = chart.getConfiguration();
        configuration.setTitle("Sprint Burndown Chart");
        String sprintDates = dateUtil.formatToShortDate(sprint.getStartDate()) + " - " + dateUtil.formatToShortDate(sprint.getEndDate());
        configuration.setSubTitle(sprint.getName() + "<br>" + sprintDates);
        configuration.getLegend().setEnabled(false);
        configuration.disableCredits();
        XAxis xAxis = new XAxis();
        xAxis.setTitle("Date<br>(end of day)");
        xAxis.setType(AxisType.CATEGORY);
        configuration.addxAxis(xAxis);
        YAxis yAxis = new YAxis();
        yAxis.setTitle("Effort Sum");
        yAxis.setMin(0);
        configuration.addyAxis(yAxis);
        generateDataSeries(sprint, showWeekends);
        ListSeries series = new ListSeries();
        series.setName("Effort Sum");
        series.setData(yDataList);
        configuration.addSeries(series);
        configuration.getxAxis().setCategories(xDataList.toArray(new String[0]));
        addComponent(chart, COLUMN_ONE, ROW_THREE);
    }

    private void generateDataSeries(Sprint sprint, boolean includeWeekends) {
        TaskHistoryEffortCalculator calculator = new TaskHistoryEffortCalculator();
        SortedMap<LocalDate, Long> dateAndEfforts = calculator.calculateTaskAndEffortPerDateUpToSprintsEndDate(sprint);
        Set<LocalDate> dates = dateAndEfforts.keySet();
        LocalDate startDate = sprint.getStartDate().toLocalDate();
        yDataList = new ArrayList<Number>();
        xDataList = new ArrayList<String>();
        LocalDate currentDay = new LocalDate();
        for (LocalDate date : dates) {
            if (date.isAfter(startDate) || date.isEqual(startDate)) {
                if (!includeWeekends) {
                    if (date.getDayOfWeek() != DateTimeConstants.SATURDAY && date.getDayOfWeek() != DateTimeConstants.SUNDAY) {
                        addEffortToDate(dateAndEfforts, currentDay, date);
                    }
                } else {
                    addEffortToDate(dateAndEfforts, currentDay, date);
                }
            }
        }
    }

    private void addEffortToDate(SortedMap<LocalDate, Long> dateAndEfforts, LocalDate currentDay, LocalDate date) {
        if (date.isAfter(currentDay)) {
            xDataList.add(dateUtil.formatToOnlyDateAndMonthShort(date));
            yDataList.add(null);
        } else {
            xDataList.add(dateUtil.formatToOnlyDateAndMonthShort(date));
            Long effort = dateAndEfforts.get(date);
            yDataList.add(effort);
        }
    }

    private void addActionMenu() {
        actionMenu = new MenuBar();
        actionMenu.addStyleName("actionmenu");
        addComponent(actionMenu, COLUMN_ONE, ROW_FOUR);
        setComponentAlignment(actionMenu, Alignment.MIDDLE_LEFT);
        MenuItem topLevelItem = actionMenu.addItem("►", null, null);
        MenuItem showWeekendsItem = topLevelItem.addItem("Show Weekends", null, new Command() {
            private static final long serialVersionUID = -3380427780004047081L;

            @Override
            public void menuSelected(MenuItem selectedItem) {
                showWeekends = selectedItem.isChecked();
                refresh();
            }
        });
        showWeekendsItem.setCheckable(true);
        showWeekendsItem.setChecked(false);
        topLevelItem.addItem("Refresh", null, new Command() {
            private static final long serialVersionUID = 2040937142257741225L;

            @Override
            public void menuSelected(MenuItem selectedItem) {
                refresh();

            }
        });
    }

    @Override
    public void refresh() {
        logger.debug("About to refresh the report");
        actionMenu.setEnabled(false);
        removeComponent(chart);
        if (sprintInChart != null) {
            SprintPersistence sprintPersistence = new SprintPersistenceImpl();
            sprintInChart = sprintPersistence.get(sprintInChart.getUniqueId());
            if (sprintInChart != null) {
                addChart(sprintInChart);
            }
        }
        actionMenu.setEnabled(true);
    }
}

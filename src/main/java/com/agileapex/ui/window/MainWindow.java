package com.agileapex.ui.window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.page.AdminPage;
import com.agileapex.ui.page.ProjectBacklogPage;
import com.agileapex.ui.page.ProjectsPage;
import com.agileapex.ui.page.ReleasesPage;
import com.agileapex.ui.page.ReportsPage;
import com.agileapex.ui.page.SettingsPage;
import com.agileapex.ui.page.SprintPage;
import com.agileapex.ui.page.SprintPlanningPage;
import com.agileapex.ui.page.TaskBoardPage;
import com.agileapex.ui.signin.SignInPanel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MainWindow extends Window {
	private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
	private static final long serialVersionUID = -4312016406953382361L;

	public MainWindow() {
		super();
    	logger.info("MainWindow");
		setScrollable(true);
		VerticalLayout layout = (VerticalLayout) getContent();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setSpacing(false);
		SignInPanel signinPanel = new SignInPanel(this);
		layout.addComponent(signinPanel);
		layout.setComponentAlignment(signinPanel, Alignment.MIDDLE_CENTER);
	}
	
	public void changePage() {
		removeAllComponents();
		logger.debug("Changing page to: {}", ApplicationSession.getSessionDataHelper().getTargetPage());
		if (ApplicationSession.getSessionDataHelper().getTargetPage().equals(WindowIdentification.PROJECTS)) {
			ProjectsPage projectsPage = new ProjectsPage();
			addComponent(projectsPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage()
				.equals(WindowIdentification.PROJECT_BACKLOG)) {
			ProjectBacklogPage projectBacklogPage = new ProjectBacklogPage();
			addComponent(projectBacklogPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage().equals(WindowIdentification.RELEASES)) {
			ReleasesPage releasesPage = new ReleasesPage();
			addComponent(releasesPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage().equals(WindowIdentification.SPRINT)) {
			SprintPage sprintPage = new SprintPage();
			addComponent(sprintPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage()
				.equals(WindowIdentification.SPRINT_PLANNING)) {
			SprintPlanningPage sprintPlanningPage = new SprintPlanningPage();
			addComponent(sprintPlanningPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage().equals(WindowIdentification.TASK_BOARD)) {
			TaskBoardPage taskBoardPage = new TaskBoardPage();
			addComponent(taskBoardPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage().equals(WindowIdentification.REPORTS)) {
			ReportsPage reportsPage = new ReportsPage();
			addComponent(reportsPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage().equals(WindowIdentification.SETTINGS)) {
			SettingsPage settingsPage = new SettingsPage();
			addComponent(settingsPage);
		} else if (ApplicationSession.getSessionDataHelper().getTargetPage().equals(WindowIdentification.ADMIN)) {
			AdminPage adminPage = new AdminPage();
			addComponent(adminPage);
		} else {
			ApplicationSession.setUser(null);
			logger.error("Trying to change to unknown page.");
			addComponent(
					new Label("Internal error. Trying to change to unknown page. Please contant your administrator."));
			logger.error("Target page: " + (ApplicationSession.getSessionDataHelper().getTargetPage() != null
					? ApplicationSession.getSessionDataHelper().getTargetPage() : "target page is null"));
		}
	}
}

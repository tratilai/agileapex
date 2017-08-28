package com.agileapex.ui.header;

import com.agileapex.common.user.UserHelper;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.Constants;
import com.agileapex.ui.registration.RegistrationPopup;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Reindeer;

public class NavigatorLayout extends HorizontalLayout implements Constants {
	private static final long serialVersionUID = 278542434382050010L;
	protected static final String TAB_PROJECTS = "Projects";
	protected static final String TAB_BACKLOGS = "Product Backlogs";
	protected static final String TAB_TASK_BOARD = "Task Board";
	protected static final String TAB_REPORTS = "Reports";
	protected static final String TAB_SETTINGS = "Settings";
	protected static final String TAB_ADMIN = "Admin";
	private TabSheet tabsheet;
	private Button registerButton;

	public NavigatorLayout() {
		init();
		addTabsheet();
		addExpander();
		addRegisterButton();
	}

	private void init() {
		setDebugId(HEADER_NAVIGATION);
		setWidth("100%");
		setHeight(38.0f, Sizeable.UNITS_PIXELS);
		setMargin(false, true, false, true);
		setSpacing(false);
	}

	private void addTabsheet() {
		tabsheet = new TabSheet();
		tabsheet.addStyleName(Reindeer.TABSHEET_MINIMAL);
		tabsheet.setSizeUndefined();
		addComponent(tabsheet);
		setComponentAlignment(tabsheet, Alignment.MIDDLE_LEFT);
		setExpandRatio(tabsheet, EXPAND_RATIO_1);
		tabsheet.addTab(new CssLayout(), TAB_PROJECTS);
		tabsheet.addTab(new CssLayout(), TAB_BACKLOGS);
		tabsheet.addTab(new CssLayout(), TAB_TASK_BOARD);
		// tabsheet.addTab(new CssLayout(), TAB_REPORTS); // TODO
		tabsheet.addTab(new CssLayout(), TAB_SETTINGS);
		if (ApplicationSession.getUser().getAuthorization().hasAdminPrivileges()) {
			tabsheet.addTab(new CssLayout(), TAB_ADMIN);
		}
		tabsheet.addListener(new HeaderSelectedTabChangeListener(this));
	}

	private void addExpander() {
		Label emptyExpander = new Label();
		emptyExpander.setSizeFull();
		addComponent(emptyExpander);
		setComponentAlignment(emptyExpander, Alignment.MIDDLE_LEFT);
	}

	private void addRegisterButton() {
		UserHelper userUtil = new UserHelper();
		if (userUtil.hasAdminPrivileges() && !userUtil.isRegisteredUser()) {
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setSizeUndefined();
			registerButton = new Button("Register to keep this account");
			buttonLayout.addComponent(registerButton);
			buttonLayout.setComponentAlignment(registerButton, Alignment.MIDDLE_RIGHT);
			addComponent(buttonLayout);
			setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);

			registerButton.addListener(new ClickListener() {
				private static final long serialVersionUID = 7137517628730935242L;

				@Override
				public void buttonClick(ClickEvent event) {
					RegistrationPopup popup = new RegistrationPopup(NavigatorLayout.this);
					NavigatorLayout.this.getWindow().addWindow(popup);
				}
			});
		}
	}

	@Override
	public void attach() {
		WindowIdentification currentPage = ApplicationSession.getSessionDataHelper().getCurrentPage();
		if (currentPage.equals(WindowIdentification.PROJECTS)) {
			selectTab(TAB_PROJECTS);
		} else if (currentPage.equals(WindowIdentification.PROJECT_BACKLOG)) {
			selectTab(TAB_BACKLOGS);
		} else if (currentPage.equals(WindowIdentification.TASK_BOARD)) {
			selectTab(TAB_TASK_BOARD);
		} else if (currentPage.equals(WindowIdentification.REPORTS)) {
			selectTab(TAB_REPORTS);
		} else if (currentPage.equals(WindowIdentification.SETTINGS)) {
			selectTab(TAB_SETTINGS);
		} else if (currentPage.equals(WindowIdentification.ADMIN)) {
			selectTab(TAB_ADMIN);
		}
	}

	private void selectTab(String tabCaption) {
		for (int index = 0;; ++index) {
			if (tabsheet.getTab(index) != null) {
				if (tabsheet.getTab(index).getCaption().equals(tabCaption)) {
					tabsheet.setSelectedTab(tabsheet.getTab(index));
					break;
				}
			}
		}
	}
}

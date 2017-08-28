package com.agileapex.ui.common;

import com.agileapex.common.DateAndTimeUtil;
import com.vaadin.event.Action;

public interface Constants extends DebugIds {

    public static final DateAndTimeUtil dateFormatter = new DateAndTimeUtil();

    public static final Action REPORT_REFRESH = new Action("Refresh");

    public static final Action EDIT_SPRINT_ACTION = new Action("Edit sprint");
    public static final Action ADD_SPRINT_ACTION = new Action("Add sprint");
    public static final Action DELETE_SPRINT_ACTION = new Action("Delete sprint");

    public static final Action EDIT_RELEASE_ACTION = new Action("Edit release");
    public static final Action ADD_RELEASE_ACTION = new Action("Add release");
    public static final Action DELETE_RELEASE_ACTION = new Action("Delete release");

    public static final Action EDIT_PROJECT_ACTION = new Action("Edit project");
    public static final Action ADD_PROJECT_ACTION = new Action("Add project");
    public static final Action DELETE_PROJECT_ACTION = new Action("Delete project");

    public static final Action EDIT_USER_ACTION = new Action("Edit user");
    public static final Action ADD_USER_ACTION = new Action("Add user");
    public static final Action DELETE_USER_ACTION = new Action("Delete user");

    public static final Action VIEW_TASK_ACTION = new Action("View task");
    public static final Action EDIT_TASK_ACTION = new Action("Edit task");
    public static final Action ADD_TASK_ACTION = new Action("Add task");
    public static final Action DELETE_TASK_ACTION = new Action("Delete task");

    public static final int SPRINT_NAME_MIN_LENGTH = 1;
    public static final int SPRINT_NAME_MAX_LENGTH = 80;
    public static final int SPRINT_DESCRIPTION_MAX_CHARACTERS = 5000;
    public static final int SPRINT_MAX_DAYS_BETWEEN_START_AND_END_DATE = 365;

    public static final int RELEASE_NAME_MIN_LENGTH = 1;
    public static final int RELEASE_NAME_MAX_LENGTH = 80;
    public static final int RELEASE_DESCRIPTION_MAX_CHARACTERS = 5000;

    public static final int PROJECT_NAME_MIN_LENGTH = 1;
    public static final int PROJECT_NAME_MAX_LENGTH = 80;
    public static final int PROJECT_DESCRIPTION_MAX_CHARACTERS = 5000;
    public static final int PROJECT_TASK_PREFIX_MAX_LENGTH = 5;

    public static final int EMAIL_MIN_LENGTH = 1;
    public static final int EMAIL_MAX_LENGTH = 50;
    public static final int USER_FIRST_NAME_MIN_LENGTH = 1;
    public static final int USER_FIRST_NAME_MAX_LENGTH = 30;
    public static final int USER_LAST_NAME_MIN_LENGTH = 1;
    public static final int USER_LAST_NAME_MAX_LENGTH = 30;
    public static final int USER_PASSWORD_MIN_LENGTH = 10;
    public static final int USER_PASSWORD_MAX_LENGTH = 250;

    public static final int DISABLE_TABLE_CACHING = 0;

    public static final int TASK_NAME_MAX_LENGTH = 100;
    public static final int TASK_NAME_MAX_CHARACTERS = 100;
    public static final int TASK_DESCRIPTION_MAX_LENGTH = 5000;
    public static final int TASK_DESCRIPTION_MAX_CHARACTERS = 5000;
    public static final int TASK_EFFORT_MAX_LENGTH = 9999;
    public static final int TASK_EFFORT_MAX_CHARACTERS = 4;

    public static final int POPUP_DEFAULT_WINDOW_WIDTH = 600;
    public static final int POPUP_DEFAULT_WINDOW_HEIGHT = 450;
    public static final int POPUP_DELETE_WINDOW_WIDTH = 600;
    public static final int POPUP_DELETE_WINDOW_HEIGHT = 400;
    public static final int POPUP_USER_WINDOW_WIDTH = 600;
    public static final int POPUP_USER_WINDOW_HEIGHT = 500;

    public static final int ROW_ONE = 0;
    public static final int ROW_TWO = 1;
    public static final int ROW_THREE = 2;
    public static final int ROW_FOUR = 3;
    public static final int ROW_FIVE = 4;
    public static final int ROW_SIX = 5;
    public static final int ROW_SEVEN = 6;
    public static final int ROW_EIGHT = 7;
    public static final int ROW_NINE = 8;
    public static final int ROW_TEN = 9;
    public static final int ROW_ELEVEN = 10;
    public static final int ROW_TWELVE = 11;

    public static final int COLUMN_ONE = 0;
    public static final int COLUMN_TWO = 1;
    public static final int COLUMN_THREE = 2;
    public static final int COLUMN_FOUR = 3;
    public static final int COLUMN_FIVE = 4;
    public static final int COLUMN_SIX = 5;
    public static final int COLUMN_SEVEN = 6;
    public static final int COLUMN_EIGTH = 7;

    public static final float EXPAND_RATIO_1 = 1.0f;
    public static final float EXPAND_RATIO_2 = 2.0f;
    public static final float EXPAND_RATIO_4 = 4.0f;
    public static final float EXPAND_RATIO_6 = 6.0f;
    public static final float EXPAND_RATIO_8 = 8.0f;
    public static final float EXPAND_RATIO_10 = 10.0f;
    public static final float EXPAND_RATIO_100 = 100.0f;
    public static final float EXPAND_RATIO_1000 = 1000.0f;

    public static final float THE_1_PERCENT = 1;
    public static final float THE_10_PERCENT = 10;
    public static final float THE_20_PERCENT = 20;
    public static final float THE_40_PERCENT = 40;
    public static final float THE_50_PERCENT = 50;
    public static final float THE_100_PERCENT = 100;

    public static final float TITLE_LAYOUT_HEIGHT = 30.0f;

    public static final long SYSTEM_PROPERTY = -12345678;
    public static final String PROJECTPAGE_PROJECTLIST_ID = "projectpage.projectlist.id";
    public static final String RELEASEPAGE_RELEASELIST_ID = "releasepage.releaselist.id";
    public static final String SPRINTPAGE_SPRINTLIST_ID = "sprintpage.sprintlist.id";
    public static final String TASKBOARDPAGE_SPRINT_SELECTION_ID = "taskboardpage.sprintselection.id";
    public static final String TASKBOARDPAGE_RELEASE_SELECTION_ID = "taskboardpage.releaseselection.id";
    public static final String TASKBOARDPAGE_PROJECT_SELECTION_ID = "taskboardpage.projectselection.id";
    public static final String PROJECTBACKLOGPANEL_PROJECTLIST_ID = "projectbacklogpanel.projectlist.id";
}

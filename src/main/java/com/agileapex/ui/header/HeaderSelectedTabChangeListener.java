package com.agileapex.ui.header;

import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.window.MainWindow;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Window.Notification;

public class HeaderSelectedTabChangeListener implements TabSheet.SelectedTabChangeListener {
    private static final long serialVersionUID = 6869951261855491799L;
    private final Layout layout;

    public HeaderSelectedTabChangeListener(Layout layout) {
        this.layout = layout;
    }

    @Override
    public void selectedTabChange(SelectedTabChangeEvent event) {
        TabSheet tabsheet = event.getTabSheet();
        Layout tab = (Layout) tabsheet.getSelectedTab();
        String caption = tabsheet.getTab(tab).getCaption();
        changePageByTabCaption(caption);
    }

    private void changePageByTabCaption(String caption) {
        if (caption.equals(NavigatorLayout.TAB_PROJECTS) && !ApplicationSession.getSessionDataHelper().getCurrentPage().equals(WindowIdentification.PROJECTS)) {
            changePage(WindowIdentification.PROJECTS);
        } else if (caption.equals(NavigatorLayout.TAB_BACKLOGS) && !ApplicationSession.getSessionDataHelper().getCurrentPage().equals(WindowIdentification.PROJECT_BACKLOG)) {
            changePage(WindowIdentification.PROJECT_BACKLOG);
        } else if (caption.equals(NavigatorLayout.TAB_TASK_BOARD) && !ApplicationSession.getSessionDataHelper().getCurrentPage().equals(WindowIdentification.TASK_BOARD)) {
            changePage(WindowIdentification.TASK_BOARD);
        } else if (caption.equals(NavigatorLayout.TAB_REPORTS) && !ApplicationSession.getSessionDataHelper().getCurrentPage().equals(WindowIdentification.REPORTS)) {
            changePage(WindowIdentification.REPORTS);
        } else if (caption.equals(NavigatorLayout.TAB_SETTINGS) && !ApplicationSession.getSessionDataHelper().getCurrentPage().equals(WindowIdentification.SETTINGS)) {
            changePage(WindowIdentification.SETTINGS);
        } else if (caption.equals(NavigatorLayout.TAB_ADMIN) && !ApplicationSession.getSessionDataHelper().getCurrentPage().equals(WindowIdentification.ADMIN)) {
            if (ApplicationSession.getUser().getAuthorization().hasAdminPrivileges()) {
                changePage(WindowIdentification.ADMIN);
            } else {
                ApplicationSession.getApplication().getMainWindow().showNotification("User is not authorized to do the operation.", Notification.TYPE_ERROR_MESSAGE);
            }
        }
    }

    private void changePage(WindowIdentification target) {
        MainWindow mainWindow = (MainWindow) layout.getWindow();
        ApplicationSession.getSessionDataHelper().setTargetPage(target);
        mainWindow.changePage();
    }
}

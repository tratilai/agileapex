package ui_test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.agileapex.domain.ProjectStatus;
import com.agileapex.domain.ReleaseStatus;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;
import com.agileapex.ui.common.Constants;
import com.vaadin.testbench.By;

public class ProjectReleaseSprintTest extends UITest implements TestingConstants, Constants {

    private User user;

    /**
     * This test maybe have too many lines, but.. testing these simple test cases by always opening
     * new browser window is too slow. Lets do it all in one window and one test case. 
     */
    @Test
    public void testEditingAndFailureNotificationsInCreatePopup() throws Exception {
        user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");

        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(PROJECTPAGE_PROJECTS_TABLE), "Add project");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_NAME)).sendKeys("");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_CREATE)).click();
        assertNotificationText("Project Name length must be from 1 to 80 characters");

        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_NAME)).sendKeys("Project 1");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_CREATE)).click();
        assertEquals("Project not created: Project 1", "Project 1", getTableCellContent(PROJECTPAGE_PROJECTS_TABLE, "Project 1", 1, 1));
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(PROJECTPAGE_PROJECTS_TABLE), "Add project");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_NAME)).sendKeys("Project 1");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_CREATE)).click();
        assertNotificationText("Project Name is already in use");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_CANCEL)).click();

        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(RELEASEPAGE_RELEASES_TABLE), "Add release");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_NAME)).sendKeys("");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_CREATE)).click();
        assertNotificationText("Release Name length must be from 1 to 80 characters");

        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_NAME)).sendKeys("Release 1");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_CREATE)).click();
        assertEquals("Release not created: Release 1", "Release 1", getTableCellContent(RELEASEPAGE_RELEASES_TABLE, "Release 1", 1, 1));
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(RELEASEPAGE_RELEASES_TABLE), "Add release");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_NAME)).sendKeys("Release 1");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_CREATE)).click();
        assertNotificationText("Release Name is already in use");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_CANCEL)).click();

        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(SPRINTPAGE_SPRINTS_TABLE), "Add sprint");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_NAME)).sendKeys("");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Sprint Name length must be from 1 to 80 characters");

        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_NAME)).sendKeys("Sprint 1");
        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "4/20/14");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Valid Start and End Dates are mandatory");

        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "4/20/14");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Valid Start and End Dates are mandatory");

        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "not good");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "4/20/14");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Valid Start and End Dates are mandatory");

        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "4/20/14");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "not good");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Valid Start and End Dates are mandatory");

        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "4/20/14");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "4/20/14");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Start and End Dates can not be the same date");

        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "4/2/14");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "4/1/14");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("End Date can not be before Start Date");

        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "5/20/11");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "5/21/12");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Maximum Sprint length is " + SPRINT_MAX_DAYS_BETWEEN_START_AND_END_DATE + " days");

        insertInDateField(CREATE_SPRINT_POPUP_START_DATE, "5/20/13");
        insertInDateField(CREATE_SPRINT_POPUP_END_DATE, "5/30/13");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertEquals("Sprint not created: Sprint 1", "Sprint 1", getTableCellContent(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", 1, 1));
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(SPRINTPAGE_SPRINTS_TABLE), "Add sprint");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_NAME)).sendKeys("Sprint 1");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertNotificationText("Sprint Name is already in use");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CANCEL)).click();

        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }

    @Test
    public void testEditingAndFailureNotificationsInEditPopup() throws Exception {
        user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", "This is project description", ProjectStatus.OPEN, "-----", user);
        editProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", "a", "Changed", ProjectStatus.CLOSED, user);
        editProject(PROJECTPAGE_PROJECTS_TABLE, "a", "Project 1", "", ProjectStatus.OPEN, user);
        selectFromContextMenu(getTableCellBy(PROJECTPAGE_PROJECTS_TABLE, "Project 1", 1, 1), "Edit project");
        driver.findElement(By.id(EDIT_PROJECT_POPUP_NAME)).clear();
        driver.findElement(By.id(EDIT_PROJECT_POPUP_SAVE)).click();
        assertNotificationText("Project Name length must be from 1 to 80 characters");
        driver.findElement(By.id(EDIT_PROJECT_POPUP_CANCEL)).click();
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Project 1", "Release 1", "This is release description", ReleaseStatus.CLOSED, user);
        editRelease(RELEASEPAGE_RELEASES_TABLE, "Project 1", "Release 1", "a", "Changed", ReleaseStatus.OPEN, user);
        editRelease(RELEASEPAGE_RELEASES_TABLE, "Project 1", "a", "Release 1", "", ReleaseStatus.CLOSED, user);
        selectFromContextMenu(getTableCellBy(RELEASEPAGE_RELEASES_TABLE, "Release 1", 1, 1), "Edit release");
        driver.findElement(By.id(EDIT_RELEASE_POPUP_NAME)).clear();
        driver.findElement(By.id(EDIT_RELEASE_POPUP_SAVE)).click();
        assertNotificationText("Release Name length must be from 1 to 80 characters");
        driver.findElement(By.id(EDIT_RELEASE_POPUP_CANCEL)).click();
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Project 1", "Release 1", "Sprint 1", "This is sprint description", user);
        editSprint(SPRINTPAGE_SPRINTS_TABLE, "Project 1", "Release 1", "Sprint 1", "a", "Changed", user);
        editSprint(SPRINTPAGE_SPRINTS_TABLE, "Project 1", "Release 1", "a", "Sprint 1", "", user);
        selectFromContextMenu(getTableCellBy(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", 1, 1), "Edit sprint");
        driver.findElement(By.id(EDIT_SPRINT_POPUP_NAME)).clear();
        driver.findElement(By.id(EDIT_SPRINT_POPUP_SAVE)).click();
        assertNotificationText("Sprint Name length must be from 1 to 80 characters");
        driver.findElement(By.id(EDIT_SPRINT_POPUP_CANCEL)).click();
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        logout();
    }

    @Test
    public void testProjectReleaseAndSprintCreationCancelationMethods() throws Exception {
        user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(PROJECTPAGE_PROJECTS_TABLE), "Add project");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_CANCEL)).click();
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(PROJECTPAGE_PROJECTS_TABLE), "Add project");
        clickPopupXCloseButton(CREATE_NEW_PROJECT_POPUP);
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", "-", ProjectStatus.CLOSED, "", user);
        selectFromContextMenu(getTableCellBy(PROJECTPAGE_PROJECTS_TABLE, "Project 1", 1, 1), "Edit project");
        driver.findElement(By.id(EDIT_PROJECT_POPUP_CANCEL)).click();
        selectFromContextMenu(getTableCellBy(PROJECTPAGE_PROJECTS_TABLE, "Project 1", 1, 1), "Edit project");
        clickPopupXCloseButton(EDIT_PROJECT_POPUP);
        selectFromContextMenu(getTableCellBy(PROJECTPAGE_PROJECTS_TABLE, "Project 1", 1, 1), "Delete project");
        driver.findElement(By.id(DELETE_PROJECT_POPUP_CANCEL)).click();
        selectFromContextMenu(getTableCellBy(PROJECTPAGE_PROJECTS_TABLE, "Project 1", 1, 1), "Delete project");
        clickPopupXCloseButton(DELETE_PROJECT_POPUP);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(RELEASEPAGE_RELEASES_TABLE), "Add release");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_CANCEL)).click();
        assertThatTableIsEmpty(RELEASEPAGE_RELEASES_TABLE);
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(RELEASEPAGE_RELEASES_TABLE), "Add release");
        clickPopupXCloseButton(CREATE_NEW_RELEASE_POPUP);
        assertThatTableIsEmpty(RELEASEPAGE_RELEASES_TABLE);
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Project 1", "Release 1", "-", ReleaseStatus.CLOSED, user);
        selectFromContextMenu(getTableCellBy(RELEASEPAGE_RELEASES_TABLE, "Release 1", 1, 1), "Edit release");
        driver.findElement(By.id(EDIT_RELEASE_POPUP_CANCEL)).click();
        selectFromContextMenu(getTableCellBy(RELEASEPAGE_RELEASES_TABLE, "Release 1", 1, 1), "Edit release");
        clickPopupXCloseButton(EDIT_RELEASE_POPUP);
        selectFromContextMenu(getTableCellBy(RELEASEPAGE_RELEASES_TABLE, "Release 1", 1, 1), "Delete release");
        driver.findElement(By.id(DELETE_RELEASE_POPUP_CANCEL)).click();
        selectFromContextMenu(getTableCellBy(RELEASEPAGE_RELEASES_TABLE, "Release 1", 1, 1), "Delete release");
        clickPopupXCloseButton(DELETE_RELEASE_POPUP);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(SPRINTPAGE_SPRINTS_TABLE), "Add sprint");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CANCEL)).click();
        assertThatTableIsEmpty(SPRINTPAGE_SPRINTS_TABLE);
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(SPRINTPAGE_SPRINTS_TABLE), "Add sprint");
        clickPopupXCloseButton(CREATE_NEW_SPRINT_POPUP);
        assertThatTableIsEmpty(SPRINTPAGE_SPRINTS_TABLE);
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Project 1", "Release 1", "Sprint 1", "-", user);
        selectFromContextMenu(getTableCellBy(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", 1, 1), "Edit sprint");
        driver.findElement(By.id(EDIT_SPRINT_POPUP_CANCEL)).click();
        selectFromContextMenu(getTableCellBy(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", 1, 1), "Edit sprint");
        clickPopupXCloseButton(EDIT_SPRINT_POPUP);
        selectFromContextMenu(getTableCellBy(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", 1, 1), "Delete sprint");
        driver.findElement(By.id(DELETE_SPRINT_POPUP_CANCEL)).click();
        selectFromContextMenu(getTableCellBy(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", 1, 1), "Delete sprint");
        clickPopupXCloseButton(DELETE_SPRINT_POPUP);
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }

    @Test
    public void createAndDeleteOneProjectAndReleaseWithDetails() throws Exception {
        user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", "This is project description", ProjectStatus.OPEN, "p1-", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Project 1", "Release 1", "This is release description", ReleaseStatus.OPEN, user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Project 1", "Release 1", "Sprint 1", "This is sprint description", user);
        deleteSprint(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1");
        clickBreadCrumbLink(BREADCRUMB_RELEASES);
        deleteRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1");
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }

    @Test
    public void testLargeInputs() throws Exception {
        user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, text80NoSpaces, text5000NoSpaces, ProjectStatus.CLOSED, "abcde", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, text80NoSpaces);
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, text80NoSpaces, text80NoSpaces, text5000NoSpaces, ReleaseStatus.CLOSED, user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, text80NoSpaces);
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, text80NoSpaces, text80NoSpaces, text80NoSpaces, text5000NoSpaces, user);
        deleteSprint(SPRINTPAGE_SPRINTS_TABLE, text80NoSpaces);
        clickBreadCrumbLink(BREADCRUMB_RELEASES);
        deleteRelease(RELEASEPAGE_RELEASES_TABLE, text80NoSpaces);
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, text80NoSpaces);
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }

    @Test
    public void testSpecialCharacters() throws Exception {
        user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, text80SpecialCharacters, textSpecialCharacters, ProjectStatus.CLOSED, "%&/()", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, text80SpecialCharacters);
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, text80SpecialCharacters, text80SpecialCharacters, textSpecialCharacters, ReleaseStatus.CLOSED, user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, text80SpecialCharacters);
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, text80SpecialCharacters, text80SpecialCharacters, text80SpecialCharacters, textSpecialCharacters, user);
        deleteSprint(SPRINTPAGE_SPRINTS_TABLE, text80SpecialCharacters);
        clickBreadCrumbLink(BREADCRUMB_RELEASES);
        deleteRelease(RELEASEPAGE_RELEASES_TABLE, text80SpecialCharacters);
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, text80SpecialCharacters);
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }

    /**
     * This test maybe have too many lines, but.. testing these simple test cases by always opening
     * new browser window is too slow. Lets do it all in one window and one test case. 
     */
    @Test
    public void bigTestForProjectReleaseAndSprintStructuralCreationAndDeletion() throws Exception {
        user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin"); // Create and delete one project
        //
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);

        // Create and delete two projects
        //
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 2", user);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 2");
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);

        // Create one project and release, then delete the project
        //
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);

        // Create one project and release, delete the release and then the project
        //
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        deleteRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1");
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);

        // Create one project, release and sprint, then delete the project
        //
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Project 1", "Release 1", "Sprint 1", null, "5/20/14", "5/21/14", user);
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);

        // Create one project, release and sprint, then delete the release and the project
        //
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", user);
        clickBreadCrumbLink(BREADCRUMB_RELEASES);
        deleteRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1");
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);

        // Create one project, release and sprint, 
        // then delete the sprint, the release and the project
        //
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", user);
        deleteSprint(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1");
        clickBreadCrumbLink(BREADCRUMB_RELEASES);
        deleteRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1");
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);

        logout();
    }
}

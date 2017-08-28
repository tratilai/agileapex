package ui_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.agileapex.common.DateAndTimeUtil;
import com.agileapex.domain.ProjectStatus;
import com.agileapex.domain.ReleaseStatus;
import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.ui.common.DebugIds;
import com.vaadin.testbench.By;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;

public class UITest extends TestBenchTestCase implements TestingConstants, DebugIds {

    private UITestDatabaseUtils dbUtils = new UITestDatabaseUtils();
    protected StringBuffer verificationErrors = new StringBuffer();
    protected String baseUrl;
    private DateAndTimeUtil dateAndTimeUtil = new DateAndTimeUtil();

    @Rule
    public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);

    @Before
    public void setUp() throws Exception {
        dbUtils.initialize();
        setDriver(TestBench.createDriver(new FirefoxDriver()));
        baseUrl = "http://localhost:8080/";
        assertDatabaseEmptiness();
    }

    @After
    public void tearDown() throws Exception {
        assertDatabaseEmptiness();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
        driver.close();
        dbUtils.closeDatabasource();
    }

    protected void assertDatabaseEmptiness() {
        assertTrue("Task table not empty in the database.", dbUtils.getTaskCount() == 0);
        assertTrue("Effort table not empty in the database.", dbUtils.getEffortCount() == 0);
        assertTrue("Project table not empty in the database.", dbUtils.getProjectCount() == 0);
        assertTrue("Release table not empty in the database.", dbUtils.getReleaseCount() == 0);
        assertTrue("Sprint table not empty in the database.", dbUtils.getSprintCount() == 0);
    }

    protected void createNewProject(String rootDomId, String projectName, User user) {
        createNewProject(rootDomId, projectName, null, null, null, user);
    }

    protected void createNewProject(String rootDomId, String projectName, String description, ProjectStatus status, String prefix, User creator) {
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(rootDomId), "Add project");
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_NAME)).clear();
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_NAME)).sendKeys(projectName);
        if (description != null) {
            driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_DESCRIPTION)).clear();
            driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_DESCRIPTION)).sendKeys(description);
            selectFromDropDownMenu(CREATE_NEW_PROJECT_POPUP_STATUS, status.getStatus());
            driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_PREFIX)).clear();
            driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_PREFIX)).sendKeys(prefix);
        }
        driver.findElement(By.id(CREATE_NEW_PROJECT_POPUP_CREATE)).click();
        assertEquals("Project not created: " + projectName, projectName, getTableCellContent(rootDomId, projectName, 1, 1));
        if (description != null) {
            assertProjectDetails(rootDomId, projectName, description, status, prefix, creator);
        }
    }

    protected void editProject(String rootDomId, String oldProjectName, String newProjectName, String description, ProjectStatus status, User creator) {
        selectFromContextMenu(getTableCellBy(PROJECTPAGE_PROJECTS_TABLE, oldProjectName, 1, 1), "Edit project");
        driver.findElement(By.id(EDIT_PROJECT_POPUP_NAME)).clear();
        driver.findElement(By.id(EDIT_PROJECT_POPUP_NAME)).sendKeys(newProjectName);
        if (description != null) {
            driver.findElement(By.id(EDIT_PROJECT_POPUP_DESCRIPTION)).clear();
            driver.findElement(By.id(EDIT_PROJECT_POPUP_DESCRIPTION)).sendKeys(description);
            selectFromDropDownMenu(EDIT_PROJECT_POPUP_STATUS, status.getStatus());
        }
        driver.findElement(By.id(EDIT_PROJECT_POPUP_SAVE)).click();
        assertEquals("Project editing failed: " + newProjectName, newProjectName, getTableCellContent(rootDomId, newProjectName, 1, 1));
        if (description != null) {
            assertProjectDetails(rootDomId, newProjectName, description, status, null, creator);
        }
    }

    protected void assertProjectDetails(String rootDomId, String projectName, String description, ProjectStatus status, String prefix, User creator) {
        driver.findElement(getTableCellBy(rootDomId, projectName, 1, 1)).click();
        assertTrue("Project extra info panel: project name not found", getText(PROJECT_EXTRA_NAME).equals(projectName));
        assertTrue("Project extra info panel: description not found", getText(PROJECT_EXTRA_DESCRIPTION).equals(description));
        assertTrue("Project extra info panel: status not found", getText(PROJECT_EXTRA_STATUS).equals(status.getStatus()));
        assertTrue("Project extra info panel: createdby not found", getText(PROJECT_EXTRA_CREATEDBY).equals(creator.getFullName()));
        assertTrue("Project extra info panel: creationdate not found", StringUtils.isNotEmpty(getText(PROJECT_EXTRA_CREATIONDATE)));
        if (prefix != null) {
            assertTrue("Extra info panel: prefix not found", getText(PROJECT_EXTRA_PREFIX).equals(prefix));
        }
    }

    protected void deleteProject(String rootDomId, String projectName) {
        org.openqa.selenium.By by = getTableCellBy(rootDomId, projectName, 1, 1);
        assertTrue("No project with given name: " + projectName, by != null);
        selectFromContextMenu(by, "Delete project");
        driver.findElement(By.id(DELETE_PROJECT_POPUP_DELETE)).click();
        assertTrue("Project not deleted, still in the table: " + projectName, !isElementPresent(by));
    }

    protected void createNewRelease(String rootDomId, String releaseName, User user) {
        createNewRelease(rootDomId, null, releaseName, null, null, user);
    }

    protected void createNewRelease(String rootDomId, String projectName, String releaseName, String description, ReleaseStatus status, User creator) {
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(rootDomId), "Add release");
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_NAME)).clear();
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_NAME)).sendKeys(releaseName);
        if (description != null) {
            driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_DESCRIPTION)).clear();
            driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_DESCRIPTION)).sendKeys(description);
            selectFromDropDownMenu(CREATE_NEW_RELEASE_POPUP_STATUS, status.getStatus());
        }
        driver.findElement(By.id(CREATE_NEW_RELEASE_POPUP_CREATE)).click();
        assertEquals("Release not created: " + releaseName, releaseName, getTableCellContent(rootDomId, releaseName, 1, 1));
        if (description != null) {
            assertReleaseDetails(rootDomId, projectName, releaseName, description, status, creator);
        }
    }

    protected void editRelease(String rootDomId, String projectName, String oldReleaseName, String newReleaseName, String description, ReleaseStatus status, User creator) {
        selectFromContextMenu(getTableCellBy(rootDomId, oldReleaseName, 1, 1), "Edit release");
        driver.findElement(By.id(EDIT_RELEASE_POPUP_NAME)).clear();
        driver.findElement(By.id(EDIT_RELEASE_POPUP_NAME)).sendKeys(newReleaseName);
        if (description != null) {
            driver.findElement(By.id(EDIT_RELEASE_POPUP_DESCRIPTION)).clear();
            driver.findElement(By.id(EDIT_RELEASE_POPUP_DESCRIPTION)).sendKeys(description);
            selectFromDropDownMenu(EDIT_RELEASE_POPUP_STATUS, status.getStatus());
        }
        driver.findElement(By.id(EDIT_RELEASE_POPUP_SAVE)).click();
        assertEquals("Release editing failed: " + newReleaseName, newReleaseName, getTableCellContent(rootDomId, newReleaseName, 1, 1));
        if (description != null) {
            assertReleaseDetails(rootDomId, projectName, newReleaseName, description, status, creator);
        }
    }

    private void assertReleaseDetails(String rootDomId, String projectName, String releaseName, String description, ReleaseStatus status, User creator) {
        driver.findElement(getTableCellBy(rootDomId, releaseName, 1, 1)).click();
        assertTrue("Release extra info panel: release name not found", getText(RELEASE_EXTRA_NAME).equals(releaseName));
        assertTrue("Release extra info panel: description not found", getText(RELEASE_EXTRA_DESCRIPTION).equals(description));
        assertTrue("Release extra info panel: status not found", getText(RELEASE_EXTRA_STATUS).equals(status.getStatus()));
        assertTrue("Release extra info panel: project name in release not found", getText(RELEASE_EXTRA_PROJECT).equals(projectName));
        assertTrue("Release extra info panel: createdby not found", getText(RELEASE_EXTRA_CREATEDBY).equals(creator.getFullName()));
        assertTrue("Release extra info panel: creationdate not found", StringUtils.isNotEmpty(getText(RELEASE_EXTRA_CREATIONDATE)));
    }

    protected void deleteRelease(String rootDomId, String releaseName) {
        org.openqa.selenium.By by = getTableCellBy(rootDomId, releaseName, 1, 1);
        assertTrue("No release with given name: " + releaseName, by != null);
        selectFromContextMenu(by, "Delete release");
        driver.findElement(By.id(DELETE_RELEASE_POPUP_DELETE)).click();
        assertTrue("Release not deleted, still in the table: " + releaseName, !isElementPresent(by));
    }

    protected void createNewSprint(String rootDomId, String sprintName, User creator) {
        createNewSprint(rootDomId, null, null, sprintName, null, null, null, creator);
    }

    protected void createNewSprint(String rootDomId, String projectName, String releaseName, String sprintName, String description, User creator) {
        createNewSprint(rootDomId, projectName, releaseName, sprintName, description, null, null, creator);
    }

    protected void createNewSprint(String rootDomId, String projectName, String releaseName, String sprintName, String description, String startDate, String endDate, User creator) {
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(rootDomId), "Add sprint");
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_NAME)).clear();
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_NAME)).sendKeys(sprintName);
        if (description != null) {
            driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_DESCRIPTION)).clear();
            driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_DESCRIPTION)).sendKeys(description);
        }
        if (startDate != null) {
            insertInDateField(CREATE_SPRINT_POPUP_START_DATE, startDate);
        }
        if (endDate != null) {
            insertInDateField(CREATE_SPRINT_POPUP_END_DATE, endDate);
        }
        driver.findElement(By.id(CREATE_NEW_SPRINT_POPUP_CREATE)).click();
        assertSprint(rootDomId, projectName, releaseName, sprintName, description, creator);
    }

    protected void editSprint(String rootDomId, String projectName, String releaseName, String oldSprintName, String newSprintName, String description, User creator) {
        selectFromContextMenu(getTableCellBy(rootDomId, oldSprintName, 1, 1), "Edit sprint");
        driver.findElement(By.id(EDIT_SPRINT_POPUP_NAME)).clear();
        driver.findElement(By.id(EDIT_SPRINT_POPUP_NAME)).sendKeys(newSprintName);
        if (description != null) {
            driver.findElement(By.id(EDIT_SPRINT_POPUP_DESCRIPTION)).clear();
            driver.findElement(By.id(EDIT_SPRINT_POPUP_DESCRIPTION)).sendKeys(description);
        }
        driver.findElement(By.id(EDIT_SPRINT_POPUP_SAVE)).click();
        assertSprint(rootDomId, projectName, releaseName, newSprintName, description, creator);
    }

    private void assertSprint(String rootDomId, String projectName, String releaseName, String sprintName, String description, User creator) {
        assertEquals("Sprint assertion failed: " + sprintName, sprintName, getTableCellContent(rootDomId, sprintName, 1, 1));
        driver.findElement(getTableCellBy(rootDomId, sprintName, 1, 1)).click();
        assertTrue("Sprint extra info panel: start_date not found", StringUtils.isNotEmpty(getText(SPRINT_EXTRA_START_DATE)));
        assertTrue("Sprint extra info panel: end_date not found", StringUtils.isNotEmpty(getText(SPRINT_EXTRA_END_DATE)));
        if (description != null) {
            assertTrue("Sprint extra info panel: sprint name not found", getText(SPRINT_EXTRA_NAME).equals(sprintName));
            assertTrue("Sprint extra info panel: description not found", getText(SPRINT_EXTRA_DESCRIPTION).equals(description));
            assertTrue("Sprint extra info panel: project name in sprint not found", getText(SPRINT_EXTRA_PROJECT).equals(projectName));
            assertTrue("Sprint extra info panel: release name in sprint not found", getText(SPRINT_EXTRA_RELEASE).equals(releaseName));
            assertTrue("Sprint extra info panel: createdby not found", getText(SPRINT_EXTRA_CREATEDBY).equals(creator.getFullName()));
            assertTrue("Sprint extra info panel: creationdate not found", StringUtils.isNotEmpty(getText(SPRINT_EXTRA_CREATIONDATE)));
        }
    }

    protected void deleteSprint(String rootDomId, String sprintName) {
        org.openqa.selenium.By by = getTableCellBy(rootDomId, sprintName, 1, 1);
        assertTrue("No sprint with given name: " + sprintName, by != null);
        selectFromContextMenu(by, "Delete sprint");
        driver.findElement(By.id(DELETE_SPRINT_POPUP_DELETE)).click();
        assertTrue("Sprint not deleted, still in the table: " + sprintName, !isElementPresent(by));
    }

    protected void openSprint(String project, String release, String sprint) {
        clickHeaderNavigation(HEADER_NAVIGATION, "Projects");
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, project);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, release);
        doubleClickOnTableCell(SPRINTPAGE_SPRINTS_TABLE, 1, sprint);
    }

    protected void createTask(String rootDomId, String taskName, int keyColumn) {
        createTask(rootDomId, taskName, null, keyColumn);
    }

    protected void createTask(String rootDomId, String taskName, Integer effort, int keyColumn) {
        selectFromContextMenu(getXPathForTableFirstVisibleDiv(rootDomId), "Add task");
        driver.findElement(By.id(CREATE_NEW_TASK_POPUP_NAME)).clear();
        driver.findElement(By.id(CREATE_NEW_TASK_POPUP_NAME)).sendKeys(taskName);
        if (effort != null) {
            driver.findElement(By.id(CREATE_NEW_TASK_POPUP_EFFORT)).clear();
            driver.findElement(By.id(CREATE_NEW_TASK_POPUP_EFFORT)).sendKeys(effort.toString());
        }
        driver.findElement(By.id(CREATE_NEW_TASK_POPUP_CREATE_ONE)).click();
        assertEquals("Task not created: " + taskName, taskName, getTableCellContent(rootDomId, taskName, keyColumn, keyColumn));
    }

    protected void editTask(String rootDomId, String taskName, TaskStatus newStatus) {
        editTask(rootDomId, taskName, taskName, null, null, newStatus, null);
    }

    protected void editTask(String rootDomId, String oldTaskName, String newTaskName, String description, Integer effort, TaskStatus status, User pointPerson) {
        selectFromContextMenu(getTableCellBy(rootDomId, oldTaskName, 2, 2), "Edit task");
        driver.findElement(By.id(EDIT_TASK_POPUP_NAME)).clear();
        driver.findElement(By.id(EDIT_TASK_POPUP_NAME)).sendKeys(newTaskName);
        if (description != null) {
            driver.findElement(By.id(EDIT_TASK_POPUP_DESCRIPTION)).clear();
            driver.findElement(By.id(EDIT_TASK_POPUP_DESCRIPTION)).sendKeys(description);
        }
        selectFromDropDownMenu(EDIT_TASK_POPUP_STATUS, status.getStatus());
        if (effort != null) {
            driver.findElement(By.id(EDIT_TASK_POPUP_EFFORT)).clear();
            driver.findElement(By.id(EDIT_TASK_POPUP_EFFORT)).sendKeys(effort.toString());
        }
        if (pointPerson != null) {
            selectFromDropDownMenu(EDIT_TASK_POPUP_POINTPERSON, pointPerson.getFullName());
        }
        assertTaskEditPopupBeforeSaving(status);
        driver.findElement(By.id(EDIT_TASK_POPUP_SAVE)).click();
        assertEditTaskDetails(rootDomId, newTaskName);
    }

    private void assertTaskEditPopupBeforeSaving(TaskStatus status) {
        if (status == TaskStatus.DONE) {
            assertTrue("Error: status in DONE and effort field editable.", isElementPresent(By.xpath(getXPathForId(EDIT_TASK_POPUP_EFFORT) + getByClass("v-textfield-readonly"))));
        } else {
            assertTrue("Error: status other than DONE and effort field not present.", isElementPresent(By.xpath(getXPathForId(EDIT_TASK_POPUP_EFFORT))));
        }
    }

    private void assertEditTaskDetails(String rootDomId, String newTaskName) {
        assertEquals("Task editing failed: " + newTaskName, newTaskName, getTableCellContent(rootDomId, newTaskName, 2, 2));
    }

    protected void deleteTask(String rootDomId, String taskName) {
        org.openqa.selenium.By by = getTableCellBy(rootDomId, taskName, 2, 2);
        assertTrue("No task with given name: " + taskName, by != null);
        selectFromContextMenu(by, "Delete task");
        driver.findElement(By.id(DELETE_TASK_POPUP_DELETE)).click();
        assertTrue("Task not deleted, still in the table: " + taskName, !isElementPresent(by));
    }

    protected void assertTaskDetails(String taskboardTasktreetable, String taskName, TaskStatus status, String effort) {
        String statusInTable = getTableCellContent(TASKBOARD_TASKTREETABLE, taskName, 2, 4);
        assertEquals("Invalid task status: " + taskName + " should be: " + status + " and is: " + statusInTable, status.getStatus(), statusInTable);
        String effortInTable = getTableCellContent(TASKBOARD_TASKTREETABLE, taskName, 2, 5);
        assertEquals("Invalid effort. Waited: " + effort + " but was: " + effortInTable, effort, effortInTable);
    }

    protected void doubleClickOnTableCell(String rootDomId, int columnNumber, String cellText) {
        new Actions(driver).doubleClick(driver.findElement(getTableCellBy(rootDomId, cellText, 1, 1))).perform();
    }

    protected void dragAndDropTask(String rootDomId, String taskFrom, String taskTo) {
        dragAndDropTask(rootDomId, rootDomId, taskFrom, taskTo);
    }

    protected void dragAndDropTask(String fromRootDomId, String toRootDomId, String taskFrom, String taskTo) {
        Actions dnd = new Actions(driver).moveToElement(driver.findElement(getTableCellBy(fromRootDomId, taskFrom, 2, 2)), 10, 10).clickAndHold();
        dnd.moveToElement(driver.findElement(getTableCellBy(toRootDomId, taskTo, 2, 2)), 10, 10).release().perform();
    }

    /**
     * 
     * @param rootDomId The top level DOM id for the search (usually the table's Vaadin debug id)
     * @param numberOfClosedNode Number of closed tree node to open. Starts from 1.
     */
    protected void openClosedTreeTableNode(String rootDomId, int numberOfClosedNode) {
        String xPath = "(" + getXPathForId(rootDomId) + "//span" + getByClass("v-treetable-node-closed") + ")[" + numberOfClosedNode + "]";
        driver.findElement(By.xpath(xPath)).click();
    }

    protected String getTableCellContent(String rootDomId, String key, int keyColumn, int columnToReturn) {
        return driver.findElement(getTableCellBy(rootDomId, key, keyColumn, columnToReturn)).getText();
    }

    protected org.openqa.selenium.By getTableCellBy(String rootDomId, String key, int keyColumn, int columnToReturn) {
        int row = 1;
        while (true) {
            org.openqa.selenium.By rowBy = By.xpath(getXPathForTableCell(rootDomId, row, keyColumn));
            if (!isElementPresent(rowBy)) {
                return null;
            }
            String keyCellText = driver.findElement(rowBy).getText();
            if (keyCellText.equals(key)) {
                return By.xpath(getXPathForTableCell(rootDomId, row, columnToReturn));
            }
            row++;
        }
    }

    protected void assertThatTableIsEmpty(String rootDomId) {
        assertTrue("Project table is not empty", !isElementPresent(By.xpath(getXPathForFirstNonEmptyTableCell(rootDomId))));
    }

    protected void clickBreadCrumbLink(String rootDomId) {
        driver.findElement(By.id(rootDomId)).click();
    }

    protected void selectFromContextMenu(String xPath, String menuTextToBeSelected) {
        selectFromContextMenu(By.xpath(xPath), menuTextToBeSelected);
    }

    protected void selectFromContextMenu(org.openqa.selenium.By by, String menuTextToBeSelected) {
        new Actions(driver).contextClick(driver.findElement(by)).perform();
        WebElement element = driver.findElement(By.xpath("//*[text() = '" + menuTextToBeSelected + "']"));
        assertEquals("Context menu does not have the given option: " + menuTextToBeSelected, menuTextToBeSelected, element.getText());
        testBenchElement(element).click(10, 10);
    }

    protected void selectFromDropDownMenu(String rootDomId, String menuTextToBeSelected) {
        if (menuTextToBeSelected != null) {
            driver.findElement(By.id(rootDomId)).click();
            WebElement element = driver.findElement(By.xpath(getXPathForId(rootDomId) + "//*[text() = '" + menuTextToBeSelected + "']"));
            assertEquals("Option menu does not have the given option: " + menuTextToBeSelected, menuTextToBeSelected, element.getText());
            testBenchElement(element).click(10, 10);
        }
    }

    protected void assertNotificationText(String text) {
        String noticationText = driver.findElement(By.xpath("//*" + getByClass("popupContent") + "/div/p")).getText();
        driver.findElement(By.xpath("//*" + getByClass("popupContent") + "/div/p")).click();
        assertTrue("Notification text invalid. Is: '" + noticationText + "'   when it should be: " + text + "'", noticationText.equals(text));
    }

    protected void openAndLogin(User user, String password) {
        driver.get(concatUrl(baseUrl, "/apex/"));
        driver.findElement(By.id(SIGN_IN_PAGE_USERNAME)).clear();
        driver.findElement(By.id(SIGN_IN_PAGE_USERNAME)).sendKeys(user.getEmail());
        driver.findElement(By.id(SIGN_IN_PAGE_PASSWORD)).clear();
        driver.findElement(By.id(SIGN_IN_PAGE_PASSWORD)).sendKeys(password);
        driver.findElement(By.id(SIGN_IN_PAGE_BUTTON)).click();
        assertTrue("Login failed, no logout link in the page", isElementPresent(By.id("header.logout")));
    }

    protected void logout() {
        driver.findElement(By.id("header.logout")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Login wait failed.", e);
        }
        assertTrue("Logout failed, not back in login page", isElementPresent(By.id(SIGN_IN_PAGE_BUTTON)));
    }

    protected void clickPopupXCloseButton(String domId) {
        driver.findElement(By.xpath(getXPathForId(domId) + "//div" + getByClass("v-window-closebox"))).click();
        assertTrue("Popup not closed: " + domId, !isElementPresent(By.xpath(getXPathForId(domId) + "//div" + getByClass("v-window-closebox"))));
    }

    protected void insertInDateField(String rootDomId, String text) {
        driver.findElement(By.xpath(getXPathForId(rootDomId) + "/input")).clear();
        driver.findElement(By.xpath(getXPathForId(rootDomId) + "/input")).sendKeys(text);
    }

    /**
     * Returns xPath string to cell content in given table.
     *  
     * @param rootDomId The top level DOM id for the search (usually the table's Vaadin debug id)
     * @param row Table row starting from 1
     * @param column Table column starting from 1
     * @return The xPath string to the cell content
     */
    protected String getXPathForTableCell(String rootDomId, int row, int column) {
        return getXPathForId(rootDomId) + "//table" + getByClass("v-table-table") + "//tbody/tr[" + row + "]/td[" + column + "]/div";
    }

    protected void clickHeaderNavigation(String rootDomId, String navigationButtonText) {
        driver.findElement(By.xpath(getXPathForId(rootDomId) + "//*[text()='" + navigationButtonText + "']")).click();
    }

    protected boolean isTextAnywhereInTheDocument(String text) {
        return isElementPresent(By.id("//*[text()='" + text + "']"));
    }

    protected String getText(String rootDomId) {
        String tagName = driver.findElement(By.id(rootDomId)).getTagName();
        if (tagName.equalsIgnoreCase("textarea")) {
            return driver.findElement(By.id(rootDomId)).getAttribute("value");
        }
        return driver.findElement(By.id(rootDomId)).getText();
    }

    /**
     * This is complicated xPath expression because the same class attribute can contain multiple
     * values separated by spaces.
     */
    protected String getXPathForFirstNonEmptyTableCell(String rootDomId) {
        return getXPathForId(rootDomId) + "//td" + getByClass("v-table-cell-content") + "[1]";
    }

    protected String getXPathForTableFirstVisibleDiv(String rootDomId) {
        return getXPathForId(rootDomId) + "/div[2]/div[1]";
    }

    protected String getByClass(String className) {
        return "[contains(concat(' ', @class, ' '), ' " + className + " ')]";
    }

    protected String getXPathForId(String domId) {
        return "//*[@id='" + domId + "']";
    }
}

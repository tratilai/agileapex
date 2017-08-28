package ui_test;

import org.junit.Test;

import com.agileapex.domain.TaskStatus;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;

public class TaskBoardTest extends UITest {

    /**
     * Use big test cases to avoid long project task tree creation at the start.
     */
    @Test
    public void testAutomaticStatusChanges() throws Exception {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");

        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", user);
        doubleClickOnTableCell(SPRINTPAGE_SPRINTS_TABLE, 1, "Sprint 1");

        createTask(PLANNING_SPRINT_BACKLOG, "Task 1", 1, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 2", 2, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 3", 3, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 4", 4, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 5", 5, 2);

        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 2", "Task 1");
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 3", "Task 1");
        openClosedTreeTableNode(PLANNING_SPRINT_BACKLOG, 1);
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 4", "Task 2");
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 5", "Task 2");
        openClosedTreeTableNode(PLANNING_SPRINT_BACKLOG, 1);

        clickHeaderNavigation(HEADER_NAVIGATION, "Task Board");
        selectFromDropDownMenu(TASKBOARD_PROJECT, "Project 1");
        selectFromDropDownMenu(TASKBOARD_RELEASE, "Release 1");
        selectFromDropDownMenu(TASKBOARD_SPRINT, "Sprint 1");
        openClosedTreeTableNode(TASKBOARD_TASKTREETABLE, 1);
        openClosedTreeTableNode(TASKBOARD_TASKTREETABLE, 1);

        editTask(TASKBOARD_TASKTREETABLE, "Task 3", TaskStatus.IMPEDED);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IMPEDED, "Sum 12");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.NOT_STARTED, "Sum 9");
        editTask(TASKBOARD_TASKTREETABLE, "Task 5", TaskStatus.IN_PROGRESS);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IMPEDED, "Sum 12");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 3", TaskStatus.IMPEDED, "3");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.IN_PROGRESS, "Sum 9");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5", TaskStatus.IN_PROGRESS, "5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.NOT_STARTED, "4");
        editTask(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.NOT_STARTED);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IMPEDED, "Sum 12");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5", TaskStatus.NOT_STARTED, "5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.NOT_STARTED, "4");
        editTask(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.IMPEDED);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IMPEDED, "Sum 12");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5", TaskStatus.IMPEDED, "5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.IMPEDED, "4");
        editTask(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.IN_PROGRESS);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IMPEDED, "Sum 12");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5", TaskStatus.IN_PROGRESS, "5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.IN_PROGRESS, "4");
        editTask(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.DONE);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IMPEDED, "Sum 3");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5", TaskStatus.DONE, "0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.DONE, "0");
        editTask(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IN_PROGRESS);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IN_PROGRESS, "Sum 3");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.IN_PROGRESS, "Sum 0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 3", TaskStatus.IN_PROGRESS, "3");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.IN_PROGRESS, "0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5", TaskStatus.IN_PROGRESS, "0");

        clickHeaderNavigation(HEADER_NAVIGATION, "Projects");
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }

    /**
     * Use big test cases to avoid long project task tree creation at the start.
     */
    @Test
    public void testAutomaticStateChangesWithTaskAddingAndRemovingAndDragAndDrop() throws Exception {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");

        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", user);
        doubleClickOnTableCell(SPRINTPAGE_SPRINTS_TABLE, 1, "Sprint 1");

        createTask(PLANNING_SPRINT_BACKLOG, "Task 1", 1, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 2", 2, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 3", 3, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 4", 4, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 5", 5, 2);

        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 2", "Task 1");
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 3", "Task 1");
        openClosedTreeTableNode(PLANNING_SPRINT_BACKLOG, 1);
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 4", "Task 2");
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 5", "Task 3");
        openClosedTreeTableNode(PLANNING_SPRINT_BACKLOG, 1);

        clickHeaderNavigation(HEADER_NAVIGATION, "Task Board");
        selectFromDropDownMenu(TASKBOARD_PROJECT, "Project 1");
        selectFromDropDownMenu(TASKBOARD_RELEASE, "Release 1");
        selectFromDropDownMenu(TASKBOARD_SPRINT, "Sprint 1");
        openClosedTreeTableNode(TASKBOARD_TASKTREETABLE, 1);
        openClosedTreeTableNode(TASKBOARD_TASKTREETABLE, 1);

        editTask(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.DONE);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IN_PROGRESS, "Sum 5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.DONE, "Sum 0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.DONE, "0");

        openSprint("Project 1", "Release 1", "Sprint 1");
        deleteTask(PLANNING_SPRINT_BACKLOG, "Task 3");
        clickHeaderNavigation(HEADER_NAVIGATION, "Task Board");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.DONE, "Sum 0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.DONE, "Sum 0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.DONE, "0");

        openSprint("Project 1", "Release 1", "Sprint 1");
        createTask(PLANNING_SPRINT_BACKLOG, "Task 3b", 3, 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 5b", 5, 2);
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 5b", "Task 3b");
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 3b", "Task 1");

        clickHeaderNavigation(HEADER_NAVIGATION, "Task Board");
        openClosedTreeTableNode(TASKBOARD_TASKTREETABLE, 1);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IN_PROGRESS, "Sum 5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.DONE, "Sum 0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.DONE, "0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 3b", TaskStatus.NOT_STARTED, "Sum 5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5b", TaskStatus.NOT_STARTED, "5");

        openSprint("Project 1", "Release 1", "Sprint 1");
        createTask(PLANNING_PRODUCT_BACKLOG, "häcki_rootti", 100, 2); // TODO: This hack is needed, because testbench can not drag&drop onto empty table. Fix when time.
        createTask(PLANNING_PRODUCT_BACKLOG, "Task 6", 6, 2);
        createTask(PLANNING_PRODUCT_BACKLOG, "Task 7", 7, 2);
        dragAndDropTask(PLANNING_PRODUCT_BACKLOG, "Task 7", "Task 6");
        openClosedTreeTableNode(PLANNING_PRODUCT_BACKLOG, 1);
        editTask(PLANNING_PRODUCT_BACKLOG, "Task 7", TaskStatus.IMPEDED);
        dragAndDropTask(PLANNING_PRODUCT_BACKLOG, PLANNING_SPRINT_BACKLOG, "Task 6", "Task 1");

        clickHeaderNavigation(HEADER_NAVIGATION, "Task Board");
        openClosedTreeTableNode(TASKBOARD_TASKTREETABLE, 1);
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IMPEDED, "Sum 12");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.DONE, "Sum 0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.DONE, "0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 3b", TaskStatus.NOT_STARTED, "Sum 5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5b", TaskStatus.NOT_STARTED, "5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 6", TaskStatus.IMPEDED, "Sum 7");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 7", TaskStatus.IMPEDED, "7");

        openSprint("Project 1", "Release 1", "Sprint 1");
        openClosedTreeTableNode(PLANNING_SPRINT_BACKLOG, 1);
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, PLANNING_PRODUCT_BACKLOG, "Task 6", "häcki_rootti");

        clickHeaderNavigation(HEADER_NAVIGATION, "Task Board");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 1", TaskStatus.IN_PROGRESS, "Sum 5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 2", TaskStatus.DONE, "Sum 0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 4", TaskStatus.DONE, "0");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 3b", TaskStatus.NOT_STARTED, "Sum 5");
        assertTaskDetails(TASKBOARD_TASKTREETABLE, "Task 5b", TaskStatus.NOT_STARTED, "5");

        clickHeaderNavigation(HEADER_NAVIGATION, "Projects");
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }
}

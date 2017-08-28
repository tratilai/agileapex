package ui_test;

import org.junit.Test;

import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;

public class TaskTreeTest extends UITest {

    /* TEhtävät testit:
     * 
     * - tarkistaa sprinttilistan oikeellisuuden useammalla sprintillä, tuhoaa sprintin ja tarkistaa
     * - luo, muuttaa ja tuhoaa taskin
     * - luo muutaman taskin ja siirtelee niitä puurakenteessa
     * - tuhoaa puurakenteessa taskeja
     * - siirtää puurakenteita puiden välillä
     * - luo taskipuun sprintille, siirtää sen productbacklogiin ja sieltä toiselle sprintille
     * - luo puurakenteen ja tarkistaa effort-summien oikeellisuuden
     */

    @Test
    public void testSimpleProjectReleaseAndSprintStructureWithTaskTree() throws Exception {
        User user = new User(1, "admin", null, null, "Admin", "Admin", UserStatus.REGISTERED, null, "s1", "123");
        openAndLogin(user, "admin");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        createNewProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1", user);
        doubleClickOnTableCell(PROJECTPAGE_PROJECTS_TABLE, 1, "Project 1");
        createNewRelease(RELEASEPAGE_RELEASES_TABLE, "Release 1", user);
        doubleClickOnTableCell(RELEASEPAGE_RELEASES_TABLE, 1, "Release 1");
        createNewSprint(SPRINTPAGE_SPRINTS_TABLE, "Sprint 1", user);
        doubleClickOnTableCell(SPRINTPAGE_SPRINTS_TABLE, 1, "Sprint 1");
        createTask(PLANNING_SPRINT_BACKLOG, "Task 1", 2);
        createTask(PLANNING_SPRINT_BACKLOG, "Task 2", 2);
        dragAndDropTask(PLANNING_SPRINT_BACKLOG, "Task 2", "Task 1");
        clickBreadCrumbLink(BREADCRUMB_PROJECTS);
        deleteProject(PROJECTPAGE_PROJECTS_TABLE, "Project 1");
        assertThatTableIsEmpty(PROJECTPAGE_PROJECTS_TABLE);
        logout();
    }
}

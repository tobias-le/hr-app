package cz.cvut.fel.pm2.timely_be.e2e;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepartmentManagementTest extends AbstractE2ETest{

    @BeforeEach
    public void navigateToWorkTimePage() {
        navigateTo("/team-management");
    }

    @Test
    public void assertSelectedWorkerView_JohnSmith(){
        page.getByTestId("employee-search-input").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("John Smith")).click();
        PlaywrightAssertions.assertThat(page.getByTestId("employee-search-input")).hasText("John Smith");
    }

    @Test
    // TODO: test after implementation
    public void testAddNewTeam(){
        page.getByTestId("add-team-button").click();
        page.getByTestId("team-name-input").fill("Team 1");
        page.getByTestId("submit-button").click();
        PlaywrightAssertions.assertThat(page.getByTestId("team-name-input")).hasText("Team 1");
    }

    private int countTeams(){
        return page.locator("div[data-testid='teams-table'] table tbody tr").count();
    }
    @Test
    public void assertAmountOfTeams(){
//        int amount = page.locator("div[data-testid='teams-table'] table tbody tr").count();
        int initialCount = countTeams();
//        testAddNewTeam();
        Assertions.assertEquals(initialCount, 14);
    }

    @Test
    public void assertTeamMembers(){

    }

    @Test
    public void assertNewTeamMember(){

    }

    @Test
    public void assertRemoveTeamMember(){

    }

    @Test
    public void assertRemoveTeam(){

    }

}

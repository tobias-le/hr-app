package cz.cvut.fel.pm2.timely_be.e2e;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkingHoursManagementTest extends AbstractE2ETest{

    private final LocalDate now = LocalDate.now();
    // Locate the table to count the rows in the <tbody>
    private final String tableSelector = "div[data-testid='entries-table'] table tbody tr";

    @BeforeEach
    public void navigateToWorkTimePage() {
        navigateTo("/work-time");
    }

    @Test
    public void assertSelectedWorkerView_JohnSmith(){
        page.getByTestId("employee-search-input").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("John Smith")).click();
        PlaywrightAssertions.assertThat(page.getByTestId("employee-search-input")).hasText("John Smith");
    }

    @Test
    public void testNewWorkTimeEntry_JohnSmith() {
        String projectName = "AI Integration Initiative";
        assertSelectedWorkerView_JohnSmith();
        page.getByTestId("project-select").getByLabel("").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("AI Integration Initiative")).click();
        page.getByLabel("Description").click();
        page.getByLabel("Description").fill("Worked on " + projectName);
        page.getByTestId("submit-button").click();

        //TODO: update assertions after the sorting bug is fixed
        Locator latestEntry = page.locator(tableSelector).nth(-1);
        Locator dateCell = latestEntry.locator("td:nth-of-type(1)");
        PlaywrightAssertions.assertThat(dateCell).hasText("Nov 23, 2024");

        Locator projectNameCell = latestEntry.locator("td:nth-of-type(2) span.MuiChip-label");
        PlaywrightAssertions.assertThat(projectNameCell).hasText("Network Infrastructure Upgrade");

        Locator timeCell = latestEntry.locator("td:nth-of-type(3)");
        PlaywrightAssertions.assertThat(timeCell).hasText("09:00 - 17:00");

        Locator statusCell = latestEntry.locator("td:nth-of-type(4) span.MuiChip-label");
        PlaywrightAssertions.assertThat(statusCell).hasText("PENDING");

        Locator editButton = latestEntry.locator("td:nth-of-type(6) button[data-testid='edit-button-116']");
        Locator deleteButton = latestEntry.locator("td:nth-of-type(6) button[data-testid='delete-button-116']");

        //TODO: use these locators after bug fix
//        Locator editButton = page.getByTestId("edit-button-116");
//        Locator deleteButton = page.getByTestId("delete-button-116");

        PlaywrightAssertions.assertThat(editButton).isVisible();
        PlaywrightAssertions.assertThat(deleteButton).isVisible();
    }

    private int countWorkTimeEntriesAmount_JohnSmith() {
        assertSelectedWorkerView_JohnSmith();
        return page.locator(tableSelector).count();
    }

    @Test
    public void testWorkTimeEntriesAmount_JohnSmith() {
        int startingWorkTimeEntries = countWorkTimeEntriesAmount_JohnSmith();
        testNewWorkTimeEntry_JohnSmith();
        int updatedWorkTimeEntries = countWorkTimeEntriesAmount_JohnSmith();

        // Assert that the updated count is exactly 1 more than the starting count
        assertEquals(startingWorkTimeEntries + 1, updatedWorkTimeEntries,
                "The number of work time entries did not increase by 1");
    }

    @Test
    public void testCancelledNewWorkTimeEntry_JohnSmith() {
        assertSelectedWorkerView_JohnSmith();
        int startingWorkTimeEntries = countWorkTimeEntriesAmount_JohnSmith();
        page.getByTestId("project-select").getByLabel("").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("AI Integration Initiative")).click();
        page.getByTestId("cancel-button").click();
        int updatedWorkTimeEntries = countWorkTimeEntriesAmount_JohnSmith();
        assertEquals(startingWorkTimeEntries, updatedWorkTimeEntries,
                "The number of work time entries has not changed after cancelling a new entry");
    }

//    @Test
//    // TODO: test after fixing the bug
//    public void testEditWorkTimeEntry() {
//        String projectName = "AI Integration Initiative";
//        assertSelectedWorkerView_JohnSmith();
//    }
}

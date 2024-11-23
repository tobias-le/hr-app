package cz.cvut.fel.pm2.timely_be.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HolidayManagementTest extends AbstractE2ETest {
    private final LocalDate now = LocalDate.now();

    @BeforeEach
    public void navigateToWorkTimePage() {
        navigateTo("/time-off");
    }

    private int countTimeOffRequests() {
        return page.locator("div[data-testid='time-off-table'] table tbody tr").count();
    }

    @Test
    public void newSickLeaveRequest() {
        page.getByTestId("start-date-input").fill("2024-11-23"); //todo: change to LocalDate
        page.getByTestId("end-date-input").fill("2024-11-23");
        page.getByTestId("reason-input").fill("Sick leave");
        page.getByTestId("submit-button").click();
    }
    @Test
    public void newVacationRequest() {
        page.getByTestId("start-date-input").fill("2024-11-23"); //todo: change to LocalDate
        page.getByTestId("end-date-input").fill("2024-11-23");
        page.getByTestId("reason-input").fill("Vacation");
        page.getByTestId("submit-button").click();
    }

    @Test
    public void newPersonalLeaveRequest(){
        page.getByTestId("start-date-input").fill("2024-11-23"); //todo: change to LocalDate
        page.getByTestId("end-date-input").fill("2024-11-23");
        page.getByTestId("reason-input").fill("Personal leave");
        page.getByTestId("submit-button").click();
    }

    @Test
    public void assertTimeOffRequestAmount() {
        int initialCount = countTimeOffRequests();
        newSickLeaveRequest();
        int finalCount = countTimeOffRequests();
        assertEquals(finalCount, initialCount + 1);
    }

    @Test
    public void assertNotSubmittedLeaveRequest() {
        int initialCount = countTimeOffRequests();
        page.getByTestId("start-date-input").fill("2024-11-23"); //todo: change to LocalDate
        page.getByTestId("end-date-input").fill("2024-11-23");
        page.getByTestId("reason-input").fill("Personal leave");
        page.getByTestId("cancel-button").click();
        int finalCount = countTimeOffRequests();
        assertEquals(finalCount, initialCount);
    }

    // TODO: rest after merge of https://github.com/tobias-le/hr-app-frontend/pull/11/files#diff-2254fb82c65a566970ee0664149edd8336eea6a01873a622a18378de16b770b2:~:text=leave/requests/%24%7B-,employeeId,-%7D%60)
}

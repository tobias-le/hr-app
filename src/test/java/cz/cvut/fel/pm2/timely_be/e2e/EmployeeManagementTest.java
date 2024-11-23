package cz.cvut.fel.pm2.timely_be.e2e;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeManagementTest extends AbstractE2ETest{

    @BeforeEach
    public void navigateToWorkTimePage() {
        navigateTo("/employee-details");
    }

    @Test
    public void assertSelectedWorkerView_JohnSmith(){
        page.getByTestId("employee-search-input").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("John Smith")).click();
        PlaywrightAssertions.assertThat(page.getByTestId("employee-search-input")).hasText("John Smith");
    }

    @Test
    public void assertChangeName_JohnSmith(){
        assertSelectedWorkerView_JohnSmith();
        page.getByTestId("employee-name-input").fill("John Doe");
        page.getByTestId("update-employee-button").click();
        PlaywrightAssertions.assertThat(page.getByTestId("employee-name-input")).hasText("John Doe");
    }

    @Test
    public void assertChangeJobTitle_JohnSmith() {
        assertSelectedWorkerView_JohnSmith();
        page.getByTestId("job-title-input").fill("Associate | Software Engineer");
        page.getByTestId("update-employee-button").click();
        PlaywrightAssertions.assertThat(page.getByTestId("employee-email-input")).hasText("Associate | Software Engineer");
    }

    @Test
    public void assertChangeEmail_JohnSmith() {
        assertSelectedWorkerView_JohnSmith();
        page.getByTestId("employee-email-input").fill("123@email.com");
        page.getByTestId("update-employee-button").click();
        PlaywrightAssertions.assertThat(page.getByTestId("employee-email-input")).hasText("123@email.com");
    }

    @Test
    public void assertChangePhoneNumber_JohnSmith() {
        assertSelectedWorkerView_JohnSmith();
        page.getByTestId("employee-phone-input").fill("+420721632349");
        page.getByTestId("update-employee-button").click();
        PlaywrightAssertions.assertThat(page.getByTestId("employee-email-input")).hasText("+420721632349");
    }

}

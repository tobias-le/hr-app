package cz.cvut.fel.pm2.timely_be.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import cz.cvut.fel.pm2.timely_be.e2e.AbstractE2ETest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * PageAssertionsTest class.
 * Each button is asserted so the singular assertion failure does not stop the test and can be reused in other tests (E2E)
 * @version 1.0
 */
public class PageAssertionsTest extends AbstractE2ETest {

    private Page page;

    private Locator toolbarSection = page.getByTestId("header");

//    public PageAssertionsTest(Page page) {
//        this.page = page;
//    }

    //    Button Assertions Tests for HEADER SECTION

    @Test
    public void assertToolbarSection(){
        PlaywrightAssertions.assertThat(toolbarSection).isVisible();
    }

    @Test
    public void assertAttendanceButtonAction(){

        Locator attendanceButton = toolbarSection.getByTestId("attendance-link");
        attendanceButton.click();
        PlaywrightAssertions.assertThat(attendanceButton).hasAttribute("href", "/");
        PlaywrightAssertions.assertThat(attendanceButton).hasText("Attendance");
    }

    @Test
    public void assertTimeOffButtonAction(){
        Locator timeOffButton = toolbarSection.getByTestId("time-off-link");
        timeOffButton.click();
        Assertions.assertEquals(page.url(), TestConfig.FRONTEND_URL + "/time-off");
        PlaywrightAssertions.assertThat(timeOffButton).hasAttribute("/href", "/time-off");
        PlaywrightAssertions.assertThat(timeOffButton).hasText("Time Off");
    }

    @Test
    public void assertWorkTimeButtonAction(){
        Locator workTimeButton = toolbarSection.getByTestId("work-time-link");
        workTimeButton.click();
        Assertions.assertEquals(page.url(), TestConfig.FRONTEND_URL + "/work-time");
        PlaywrightAssertions.assertThat(workTimeButton).hasAttribute("/href", "/work-time");
        PlaywrightAssertions.assertThat(workTimeButton).hasText("Work Time");
    }

//    public void assertFindButtonAction(){
//        Locator findButton = toolbarSection.locator("button").nth(4);
//        findButton.click();
////        PlaywrightAssertions.assertThat(findButton).hasAttribute("/href", "/find");
////        PlaywrightAssertions.assertThat(findButton).hasText("Find");
//    }

    @Test
    public void assertAlertsButtonAction(){
        Locator alertButton = toolbarSection.getByTestId("notifications-button");
        alertButton.click();
//        PlaywrightAssertions.assertThat(findButton).hasAttribute("/href", "/find");
//        PlaywrightAssertions.assertThat(findButton).hasText("Find");
    }

    @Test
    public void assertProfileButtonAction(){
        Locator profileButton = toolbarSection.getByTestId("profile-button");
        profileButton.click();
        Locator profileButtonLink = page.getByTestId("profile-link");
        if (profileButtonLink.isVisible()){
            profileButtonLink.click();
        }
        Assertions.assertEquals(page.url(), TestConfig.FRONTEND_URL + "/employee-details");
    }

//    Button Assertions Tests for LANDING/ATTENDANCE PAGE

//    public void assertAttendanceReportButtonAction(){
//        Locator attendanceButton = toolbarSection.locator("css-r3djoj-MuiTouchRipple-root");
//        attendanceButton.click();
////        PlaywrightAssertions.assertThat(attendanceButton).hasAttribute("/href", "/");
//        //todo: check the popup window
//
//        PlaywrightAssertions.assertThat(attendanceButton).hasText("Attendance Report");
//    }
//
//    public void assertAddAttendanceButtonAction() {
//        Locator attendanceButton = toolbarSection.locator("css-r3djoj-MuiTouchRipple-root");
//        attendanceButton.click();
//    }

//    Button Assertions Tests for TIME OFF PAGE

//    Button Assertions Tests for WORK TIME PAGE

}

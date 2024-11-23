package cz.cvut.fel.pm2.timely_be.e2e;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectWorkManagementTest extends AbstractE2ETest {

    @BeforeEach
    public void navigateToWorkTimePage() {
        navigateTo("/project-management");
    }

    // TODO: after data-testId's will be added from FE

}

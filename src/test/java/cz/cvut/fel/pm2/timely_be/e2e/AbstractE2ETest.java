package cz.cvut.fel.pm2.timely_be.e2e;

import com.microsoft.playwright.*;
import cz.cvut.fel.pm2.timely_be.pages.BasePage;
import cz.cvut.fel.pm2.timely_be.pages.DashboardPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

public class AbstractE2ETest {

    // Define the URL for the frontend
    public static final String FRONTEND_URL = "https://hr-app-frontend-test.up.railway.app/";

    // Playwright components
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    private Page page;
    protected BasePage basePage;

    // Set up the Playwright environment before each test
    @BeforeEach
    public void setUp() {
        // Initialize Playwright
        playwright = Playwright.create();

        // Launch the browser
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));  // Set headless to false for visible browser

        // Create a new browser context and page
        context = browser.newContext();
        page = context.newPage();

        // Navigate to the frontend URL
        page.navigate(FRONTEND_URL);
        basePage = new DashboardPage(page);
    }

    // Clean up the Playwright environment after each test
    @AfterEach
    public void tearDown() {
        // Close the browser and context
        if (browser != null) {
            browser.close();
        }

        // Close the Playwright instance
        if (playwright != null) {
            playwright.close();
        }
    }
}

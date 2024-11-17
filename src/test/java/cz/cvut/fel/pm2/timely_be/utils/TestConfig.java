package cz.cvut.fel.pm2.timely_be.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class TestConfig {
    public static final String FRONTEND_URL = "https://hr-app-frontend-test.up.railway.app";
    public static final Browser BROWSER;

    static {
        // Initialize Playwright and browser instance
        Playwright playwright = Playwright.create();
        BROWSER = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        // Shutdown hook to close the browser when tests complete (on JVM shutdown)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            BROWSER.close();
            playwright.close();
        }));
    }

}

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
        // !! setHeadless must be set to true to run on CI !! (set to false for visible browser - for debugging purposes)
        BROWSER = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        // Shutdown hook to close the browser when tests complete (on JVM shutdown)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            BROWSER.close();
            playwright.close();
        }));
    }

}

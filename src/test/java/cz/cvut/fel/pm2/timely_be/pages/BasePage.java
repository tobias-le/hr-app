package cz.cvut.fel.pm2.timely_be.pages;

import com.microsoft.playwright.Page;

public class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }
}

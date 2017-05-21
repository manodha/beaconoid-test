package com.company.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by manodha on 21/5/17.
 */
public class DashboardPage extends PageObject {

    @FindBy(tagName = "h1")
    private WebElement dashBoardTitle;

    public DashboardPage(WebDriver webDriver) {
        super(webDriver);
    }

}

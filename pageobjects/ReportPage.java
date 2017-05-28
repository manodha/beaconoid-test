package com.company.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by manodha on 22/5/17.
 */
public class ReportPage extends PageObject {

    @FindBy(id = "total_clients")
    private WebElement totalClients;

    @FindBy(id = "total_fetches")
    private WebElement totalFetches;

    @FindBy(id = "total_clicks")
    private WebElement totalClicks;

    @FindBy(id = "total_stores")
    private WebElement totalStores;

    @FindBy(id = "total_categories")
    private WebElement totalCategories;

    @FindBy(id = "total_advertisements")
    private WebElement totalAdvertisements;


    public ReportPage(WebDriver webDriver) {
        super(webDriver);
    }

    public int getTotalClients() {
        return Integer.parseInt(totalClients.getText());
    }

    public int getTotalFetches() {
        return Integer.parseInt(totalFetches.getText());
    }

    public int getTotalClicks() {
        return Integer.parseInt(totalClicks.getText());
    }

    public int getTotalStores() {
        return Integer.parseInt(totalStores.getText());
    }

    public int getTotalCategories() {
        return Integer.parseInt(totalCategories.getText());
    }

    public int getTotalAdvertisements() {
        return Integer.parseInt(totalAdvertisements.getText());
    }
}

package com.company.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by manodha on 3/4/17.
 */
public class NavigationMenu extends PageObject {
    private WebDriver webDriver;

    @FindBy(css = "div.alert.alert-success")
    private WebElement succesAlert;

    //Selenium Reference to the clickLogoutLink link
    @FindBy(xpath = "//a[@href='/users/sign_out'][@data-method='delete']")
    private WebElement logoutLink;

    //Selenium Reference to the dashboard link
    @FindBy(partialLinkText = "Beaconoid")
    private WebElement beconsWebConsoleLink;

    @FindBy(partialLinkText = "Dashboard")
    private WebElement dashboardLink;

    @FindBy(xpath = "//a[@href='/stores']")
    private WebElement storesLink;

    @FindBy(xpath = "//a[@href='/categories']")
    private WebElement categoriesLink;

    @FindBy(xpath = "//a[@href='/beacons']")
    private WebElement beaconsLink;

    @FindBy(xpath = "//a[@href='/advertisements']")
    private WebElement advertisementsLink;

    @FindBy(xpath = "//a[@href='/staffs']")
    private WebElement staffLink;

    @FindBy(partialLinkText = " Settings")
    private WebElement settingsLink;


    public NavigationMenu(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;

    }

    public LoginPage clickLogoutLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", logoutLink);
        return new LoginPage(webDriver);
    }

    public void clickBeconsWebConsole() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", beconsWebConsoleLink);
    }

    public void clickDashboardLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", dashboardLink);
    }

    public StoresPage clickStoresLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", storesLink);
        return new StoresPage(webDriver);
    }

    public CategoryPage clickCatogoriesLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", categoriesLink);
        return new CategoryPage(webDriver);
    }

    public BeaconsPage clickBeconsLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", beaconsLink);
        return new BeaconsPage(webDriver);
    }

    public AdvertisementsPage clickAdvertisementsLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", advertisementsLink);
        return new AdvertisementsPage(webDriver);
    }

    public StaffPage clickStaffLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", staffLink);
        return new StaffPage(webDriver);
    }

    public void clickSettingsLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", settingsLink);
    }

    public WebElement getStaffLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/staffs']")).size() != 0)
            return staffLink;
        else
            return null;
    }

    public WebElement getLogoutLink() {
        return logoutLink;
    }


}

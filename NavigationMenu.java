package com.company;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.company.FunctionalTest.webDriver;

/**
 * Created by manodha on 3/4/17.
 */
public class NavigationMenu extends PageObject {
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

    @FindBy(partialLinkText = " Stores")
    private WebElement storesLink;

    @FindBy(partialLinkText = " Categories")
    private WebElement categoriesLink;

    @FindBy(partialLinkText = " Beacons")
    private WebElement beaconsLink;

    @FindBy(partialLinkText = " Advertisements")
    private WebElement advertisementsLink;

    @FindBy(partialLinkText = " Settings")
    private WebElement settingsLink;


    public NavigationMenu(WebDriver webDriver) {
        super(webDriver);
    }

    public String getSuccessAlertText() {
        return succesAlert.getText();
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

    public void clickCatogoriesLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", categoriesLink);
    }

    public BeaconsPage clickBeconsLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", beaconsLink);
        return new BeaconsPage(webDriver);
    }

    public void clickAdvertisementsLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", advertisementsLink);
    }

    public void clickSettingsLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", settingsLink);
    }
}

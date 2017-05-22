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

    @FindBy(xpath = "//a[@href='/report']")
    private WebElement reportLink;

    @FindBy(xpath = "//a[@href='/report/store']")
    private WebElement storeReportLink;

    @FindBy(xpath = "//a[@href='/report/beacon']")
    private WebElement beaconReportLink;

    @FindBy(xpath = "//a[@href='/report/category']")
    private WebElement categoryReportLink;


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

    public DashboardPage clickDashboardLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", dashboardLink);
        return new DashboardPage(webDriver);
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

    public ReportPage clickReportLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", reportLink);
        return new ReportPage(webDriver);

    }

    public StoreReportPage clickStoreReportLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", storeReportLink);
        return new StoreReportPage(webDriver);
    }

    public BeaconReportPage clickBeaconReportLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", storeReportLink);
        return new BeaconReportPage(webDriver);
    }

    public CategoryReportPage clickCategoryReportLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", categoryReportLink);
        return new CategoryReportPage(webDriver);
    }


    public WebElement getStaffLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/staffs']")).size() != 0)
            return staffLink;
        else
            return null;
    }

    public WebElement getLogoutLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/users/sign_out']")).size() != 0)
            return staffLink;
        else
            return null;
    }

    public WebElement getReportLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/report']")).size() != 0)
            return reportLink;
        else
            return null;
    }

    public WebElement getStoreReportLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/report/store']")).size() != 0)
            return storeReportLink;
        else
            return null;
    }

    public WebElement getBeaconReportLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/report/beacon']")).size() != 0)
            return beaconReportLink;
        else
            return null;
    }

    public WebElement getCategoryReportLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/report/category']")).size() != 0)
            return categoryReportLink;
        else
            return null;
    }

    public WebElement getDashboardLink() {
        if (webDriver.findElements(By.partialLinkText("Dashboard")).size() != 0)
            return dashboardLink;
        else
            return null;
    }

    public WebElement getStoresLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/stores']")).size() != 0)
            return storesLink;
        else
            return null;
    }

    public WebElement getCategoriesLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/categories']")).size() != 0)
            return categoriesLink;
        else
            return null;
    }

    public WebElement getBeaconsLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/beacons']")).size() != 0)
            return beaconsLink;
        else
            return null;
    }

    public WebElement getAdvertisementsLink() {
        if (webDriver.findElements(By.xpath("//a[@href='/advertisements']")).size() != 0)
            return advertisementsLink;
        else
            return null;
    }

}

package com.company.view;

import com.company.model.Advertisement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manodha on 1/5/17.
 */
public class AdvertisementsPage extends PageObject {
    private WebDriver webDriver;

    @FindBy(css = "table.table-bordered.table-striped > tbody")
    private WebElement advertisementsTable;

    @FindBy(xpath = "//a[@href='/advertisements/new']")
    private WebElement newAdvertisementBtn;

    @FindBy(id = "advertisement_name")
    private WebElement adverNameTxt;

    @FindBy(id = "advertisement_beacon_id")
    private WebElement adverBeaconDD;

    @FindBy(id = "advertisement_category_id")
    private WebElement adverCategoryDD;

    @FindBy(id = "advertisement_description")
    private WebElement adverDescriptionTxt;

    @FindBy(id = "advertisement_price")
    private WebElement adverPriceTxt;

    @FindBy(name = "commit")
    private WebElement createUpdatateAdvBtn;

    public AdvertisementsPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public List<Advertisement> getAllAdvertisements() {
        List<Advertisement> advertisements = new ArrayList<>();
        List<WebElement> advertisementRows = advertisementsTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = advertisementRows.size();
        for (int i = 0; i < numRows; i++) {
            Advertisement advertisement = new Advertisement();
            advertisement.setName(advertisementRows.get(i).findElement(By.xpath("td[1]")).getText());
            advertisement.setBeacon(advertisementRows.get(i).findElement(By.xpath("td[2]")).getText());
            advertisement.setCategory(advertisementRows.get(i).findElement(By.xpath("td[3]")).getText());
            advertisement.setViewLink(advertisementRows.get(i).findElement(By.xpath("td[4]/a")));
            advertisement.setEditLink(advertisementRows.get(i).findElement(By.xpath("td[5]/a")));
            advertisement.setDeleteBtn(advertisementRows.get(i).findElement(By.xpath("td[6]/form/input[@value='Delete']")));
            advertisements.add(advertisement);
        }
        return advertisements;
    }

    public void clickNewAdvertisementBtn() {
        newAdvertisementBtn.click();
    }

    public void enterAdverName(String adverName) {
        adverNameTxt.clear();
        adverNameTxt.sendKeys(adverName);
    }

    public void selectAdverBeaconByName(String adverBeacon) {
        Select advBeaconDD = new Select(adverBeaconDD);
        advBeaconDD.selectByVisibleText(adverBeacon);
    }

    public void selectAdverBeaconByIndex(int index) {
        Select advBeaconDD = new Select(adverBeaconDD);
        advBeaconDD.selectByIndex(index);
    }

    public void selectAdverCategoryByName(String adverCategory) {
        Select advCategoryDD = new Select(adverCategoryDD);
        advCategoryDD.selectByVisibleText(adverCategory);
    }

    public void selectAdverCategoryByIndex(int index) {
        Select advCategoryDD = new Select(adverCategoryDD);
        advCategoryDD.selectByIndex(index);
    }

    public void enterAdvDescription(String adverDescription) {
        adverDescriptionTxt.clear();
        adverDescriptionTxt.sendKeys(adverDescription);
    }

    public void enterAdvPrice(String adverPrice) {
        adverPriceTxt.clear();
        adverPriceTxt.sendKeys(adverPrice);
    }

    public void clickCreateUpdateAdverBtn() {
        createUpdatateAdvBtn.click();
    }

    public void clickEditAdverBtn(WebElement editAdverBtn) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editAdverBtn);
    }

    public void clickDeleteAdvBtn(WebElement deleteAdvBtn) {
        deleteAdvBtn.click();
        webDriver.switchTo().alert().accept();
    }

}

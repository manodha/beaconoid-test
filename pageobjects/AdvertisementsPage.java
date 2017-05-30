package com.company.pageobjects;

import com.company.model.Advertisement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    @FindBy(css = "table.table-bordered.table-striped > tbody")
    WebElement advertisementsTable;
    private WebDriver webDriver;
    @FindBy(tagName = "h1")
    private WebElement title;
    @FindBy(partialLinkText = "New Advertisement")
    private WebElement newAdvertisementBtn;

    @FindBy(id = "name")
    private WebElement adverNameTxt;

    @FindBy(id = "advertisement_beacon_id")
    private WebElement adverBeaconDD;

    @FindBy(id = "category")
    private WebElement adverCategoryDD;

    @FindBy(id = "description")
    private WebElement adverDescriptionTxt;

    @FindBy(id = "image")
    private WebElement imageSelect;

    @FindBy(id = "price")
    private WebElement adverPriceTxt;

    @FindBy(name = "commit")
    private WebElement createUpdatateAdvBtn;

    public AdvertisementsPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public void clickNewAdvertisementBtn() {
        newAdvertisementBtn.click();
    }

    private void enterAdverName(String adverName) {
        adverNameTxt.clear();
        adverNameTxt.sendKeys(adverName);
    }

    private void selectAdverBeaconByName(String adverBeacon) {
        Select advBeaconDD = new Select(adverBeaconDD);
        advBeaconDD.selectByVisibleText(adverBeacon);
    }

    private void selectAdverBeaconByIndex(int index) {
        Select advBeaconDD = new Select(adverBeaconDD);
        advBeaconDD.selectByIndex(index);
    }

    private void selectAdverCategoryByName(String adverCategory) {
        Select advCategoryDD = new Select(adverCategoryDD);
        advCategoryDD.selectByVisibleText(adverCategory);
    }

    private void selectAdverCategoryByIndex(int index) {
        Select advCategoryDD = new Select(adverCategoryDD);
        advCategoryDD.selectByIndex(index);
    }

    private void enterAdvDescription(String adverDescription) {
        adverDescriptionTxt.clear();
        adverDescriptionTxt.sendKeys(adverDescription);
    }

    private void selectImage(String image) {
        imageSelect.clear();
        if (!image.equals(""))
            imageSelect.sendKeys(projectpath + image);
    }

    private void enterAdvPrice(String adverPrice) {
        adverPriceTxt.clear();
        adverPriceTxt.sendKeys(adverPrice);
    }

    private void clickCreateUpdateAdverBtn() {
        createUpdatateAdvBtn.click();
    }

    public void clickEditAdverBtn(WebElement editAdverBtn) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editAdverBtn);
    }

    public void clickDeleteAdvBtn(WebElement deleteAdvBtn) {
        deleteAdvBtn.click();
        webDriver.switchTo().alert().accept();
    }

    public void clickViewAdverBtn(WebElement editAdverBtn) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editAdverBtn);
    }

    public String getTitle() {
        return title.getText();
    }


    public List<Advertisement> getAllAdvertisements() {
        List<Advertisement> advertisements = new ArrayList<>();
        List<WebElement> advertisementRows = advertisementsTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = advertisementRows.size();
        numColumns = advertisementRows.get(0).findElements(By.tagName("td")).size();

        if (numColumns <= 1)
            return advertisements;


        for (int i = 0; i < numRows; i++) {
            Advertisement advertisement = new Advertisement();
            advertisement.setName(advertisementRows.get(i).findElement(By.xpath("td[1]")).getText());
            advertisement.setBeacon(advertisementRows.get(i).findElement(By.xpath("td[2]")).getText());
            advertisement.setCategory(advertisementRows.get(i).findElement(By.xpath("td[3]")).getText());
            advertisement.setEditLink(advertisementRows.get(i).findElement(By.xpath("td[4]/a")));
            advertisement.setDeleteBtn(advertisementRows.get(i).findElement(By.xpath("td[5]/form/input[@value='Delete']")));


            advertisements.add(advertisement);
        }
        return advertisements;
    }


    public Advertisement getAdvertisment(List<Advertisement> advertisements, String name, String beaconName) {
        for (Advertisement advertisement : advertisements) {
            if (advertisement.getName().equals(name) && advertisement.getBeacon().equals(beaconName)) {
                return advertisement;
            }
        }
        return null;
    }

    public void createUpdateAdvertisement(Advertisement advertisement) {
        enterAdverName(advertisement.getName());
        if (advertisement.getBeacon().equals("")) {
            selectAdverBeaconByIndex(1);
        } else {
            selectAdverBeaconByName(advertisement.getBeacon());
        }
        if (advertisement.getCategory().equals("")) {
            selectAdverCategoryByIndex(1);
        } else {
            selectAdverCategoryByName(advertisement.getCategory());
        }
        enterAdvDescription(advertisement.getDescription());
        selectImage(advertisement.getImage());
        enterAdvPrice(advertisement.getPrice());
        clickCreateUpdateAdverBtn();
    }

    public String getNoAdvertisementsTXT() {
        return advertisementsTable.findElement(By.xpath("tr[1]/td[1]")).getText();
    }

    public void printAllAdvertisements(List<Advertisement> advertisements) {
        System.out.println("*********** All Advertisements ***********");
        for (Advertisement advertisement : advertisements) {
            printAdvertisement(advertisement);
        }
    }

    public void printAdvertisement(Advertisement advertisement) {
        System.out.print("Name - " + advertisement.getName());
        System.out.print(" Beacon - " + advertisement.getBeacon());
        System.out.print(" Category- " + advertisement.getCategory());
        System.out.print(" Description - " + advertisement.getDescription());
        System.out.println(" Price - " + advertisement.getPrice());
    }

    public String getAdvertisementID(String editLink) {
        String advertisementID = editLink.replace("/edit", "");
        advertisementID = advertisementID.replace(advertisementID.substring(0, advertisementID.indexOf("advertisements/")), "");
        advertisementID = advertisementID.replace("advertisements/", "");
        return advertisementID;
    }

    public List<Advertisement> getBeaconAdvers(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null)
            return null;

        if (jsonArray.length() == 0)
            return null;

        List<Advertisement> beaconAdvers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Advertisement advertisement = new Advertisement();
            advertisement.setName(jsonObject.getString("name"));
            advertisement.setBeacon(jsonObject.getString("beacon_id"));
            advertisement.setCategory(jsonObject.getString("category_id"));
            advertisement.setDescription(jsonObject.getString("description"));
            advertisement.setImage(jsonObject.getString("image"));
            //advertisement.setPrice(jsonObject.getString("price"));
            beaconAdvers.add(advertisement);
        }

        return beaconAdvers;
    }



}

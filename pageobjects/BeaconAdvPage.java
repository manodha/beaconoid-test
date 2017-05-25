package com.company.pageobjects;

import com.company.model.Advertisement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manodha on 30/4/17.
 */
public class BeaconAdvPage extends AdvertisementsPage {
    private WebDriver webDriver;


    @FindBy(tagName = "h1")
    private WebElement title;

    @FindBy(id = "page-wrapper")
    private WebElement mainContainer;

    @FindBy(partialLinkText = "Back")
    private WebElement backLink;

    public BeaconAdvPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    @Override
    public List<Advertisement> getAllAdvertisements() {
        List<Advertisement> advertisements = new ArrayList<>();
        List<WebElement> advertisementRows = advertisementsTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = advertisementRows.size();
        numColumns = advertisementRows.get(0).findElements(By.tagName("td")).size();

        if (numColumns <= 1)
            return null;

        for (int i = 0; i < numRows; i++) {
            Advertisement advertisement = new Advertisement();
            advertisement.setName(advertisementRows.get(i).findElement(By.xpath("td[1]")).getText());
            advertisement.setCategory(advertisementRows.get(i).findElement(By.xpath("td[2]")).getText());
            advertisement.setDescription(advertisementRows.get(i).findElement(By.xpath("td[3]")).getText());
            advertisement.setPrice(advertisementRows.get(i).findElement(By.xpath("td[4]")).getText());
            advertisement.setViewLink(advertisementRows.get(i).findElement(By.xpath("td[5]/a")));
            advertisement.setEditLink(advertisementRows.get(i).findElement(By.xpath("td[6]/a")));
            advertisement.setDeleteBtn(advertisementRows.get(i).findElement(By.xpath("td[7]/form/input[@value='Delete']")));
            advertisement.setBeacon(getBeaconName());
            advertisements.add(advertisement);
        }
        return advertisements;
    }

    public BeaconsPage clickBackLink() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", backLink);
        return new BeaconsPage(webDriver);
    }

    public String getBeaconName() {
        String h1 = title.getText();
        h1 = h1.trim();
        h1 = h1.replace(" Add | Edit | remove advertisements", "");
        String beaconName = h1.substring(0, h1.indexOf(":")).trim();
        return beaconName;
    }

    public String getBeaconUniqueRef() {
        String h1 = title.getText();
        h1 = h1.trim();
        h1 = h1.replace(" Add | Edit | remove advertisements", "");
        String beaconUniqueRef = h1.substring(h1.indexOf("#") + 1).trim();
        return beaconUniqueRef;
    }


}

package com.company.view;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by manodha on 30/4/17.
 */
public class BeaconAdvPage extends PageObject {
    private WebDriver webDriver;


    @FindBy(tagName = "h1")
    private WebElement title;

    @FindBy(id = "page-wrapper")
    private WebElement mainContainer;

    public BeaconAdvPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public void clickBackLink() {
        WebElement backLink = mainContainer.findElement(By.xpath("div[1]/div[1]/a"));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", backLink);
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

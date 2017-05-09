package com.company.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by manodha on 27/3/17.
 */
public class PageObject {

    protected WebDriver driver;
    @FindBy(css = "div.alert.alert-success")
    private WebElement succesAlert;
    @FindBy(css = "div.alert.alert-danger")
    private WebElement dangerAlert;

    public PageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getSucessAlert() {
        return succesAlert.getText();
    }

    public String getDangerAlert() {
        return dangerAlert.getText();
    }

}

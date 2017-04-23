package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.company.FunctionalTest.webDriver;

/**
 * Created by manodha on 22/4/17.
 */
public class StoresPage extends PageObject {

    @FindBy(xpath = "//a[@href='/stores/new']")
    private WebElement newStore;

    @FindBy(css = "table.table-bordered.table-striped > tbody")
    private WebElement storesTable;

    public StoresPage(WebDriver driver) {
        super(driver);
    }

    public void clickNewStore() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", newStore);
    }

    public List<Stores> getAllStores() {
        List<Stores> stores = null;
        List<WebElement> storeRows = storesTable.findElements(By.tagName("tr"));
        for (WebElement storeRow : storeRows) {
            Stores store;
        }

        System.out.println(storeRows.size());
        return stores;
    }
}

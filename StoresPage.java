package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
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

    @FindBy(id = "store_name")
    private WebElement storeNameTxt;

    @FindBy(id = "store_unique_id")
    private WebElement storeUniqueIdTxt;

    @FindBy(id = "store_image_url")
    private WebElement storeImgUrlTxt;

    @FindBy(name = "commit")
    private WebElement createStoreBtn;

    public StoresPage(WebDriver driver) {
        super(driver);
    }

    public void clickNewStore() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", newStore);
    }

    public List<Stores> getAllStores() {
        List<Stores> stores = new ArrayList<>();
        List<WebElement> storeRows = storesTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = storeRows.size();
        for (int i = 0; i < numRows; i++) {
            Stores store = new Stores();
            store.setName(storeRows.get(i).findElement(By.xpath("td[2]")).getText());
            store.setStoreCode(storeRows.get(i).findElement(By.xpath("td[3]")).getText());
            stores.add(store);
        }
        return stores;
    }

    public void printAllStores(List<Stores> stores) {
        for (Stores store : stores) {
            System.out.println("Store Name - " + store.getName() + " Store Unique Id - " + store.getStoreCode());
        }
    }

    public int getNumberOfRows() {
        return storesTable.findElements(By.tagName("tr")).size();
    }

    public int getNumOfColumns() {
        List<WebElement> rows = storesTable.findElements(By.tagName("tr"));
        return rows.get(0).findElements(By.tagName("td")).size();
    }

    public void enterStoreName(String name) {
        storeNameTxt.sendKeys(name);
    }

    public void enterStoreUniqueId(String storeUniqueId) {
        storeUniqueIdTxt.sendKeys(storeUniqueId);
    }

    public void clickCreateStore() {
        createStoreBtn.click();
    }


}

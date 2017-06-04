package pageobjects;

import model.Stores;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manodha on 22/4/17.
 */
public class StoresPage extends PageObject {

    private WebDriver webDriver;

    @FindBy(xpath = "//a[@href='/stores/new']")
    private WebElement newStore;

    @FindBy(css = "table.table-bordered.table-striped > tbody")
    private WebElement storesTable;

    @FindBy(id = "store_name")
    private WebElement storeNameTxt;

    @FindBy(id = "store_unique_id")
    private WebElement storeUniqueIdTxt;

    @FindBy(id = "store_sales")
    private WebElement storeSalesTxt;

    @FindBy(name = "commit")
    private WebElement createUpdateStoreBtn;

    @FindBy(tagName = "h1")
    private WebElement indexTitle;

    public StoresPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public void clickNewStore() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", newStore);
    }

    public List<Stores> getAllStores() {
        List<Stores> stores = new ArrayList<>();
        List<WebElement> storeRows = storesTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = storeRows.size();
        numColumns = storeRows.get(0).findElements(By.tagName("td")).size();

        if (numColumns <= 1)
            return null;

        for (int i = 0; i < numRows; i++) {
            Stores store = new Stores();
            store.setName(storeRows.get(i).findElement(By.xpath("td[1]")).getText());
            store.setStoreCode(storeRows.get(i).findElement(By.xpath("td[2]")).getText());
            store.setSales(storeRows.get(i).findElement(By.xpath("td[3]")).getText());
            store.setEditLink(storeRows.get(i).findElement(By.xpath("td[4]/a")));
            store.setDeleteBtn(storeRows.get(i).findElement(By.xpath("td[5]/form/input[@type='submit']")));
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
        storeNameTxt.clear();
        storeNameTxt.sendKeys(name);
    }

    public void enterStoreUniqueId(String storeUniqueId) {
        storeUniqueIdTxt.clear();
        storeUniqueIdTxt.sendKeys(storeUniqueId);
    }

    public void enterStoreSales(String sales) {
        storeSalesTxt.clear();
        storeSalesTxt.sendKeys(sales);

    }

    public void clickCreateUpdateStore() {
        createUpdateStoreBtn.click();
    }

    public void clickEditButton(Stores stores) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", stores.getEditLink());
    }

    public void clickDeleteButton(WebElement deleteButton) {
        deleteButton.click();
        webDriver.switchTo().alert().accept();
    }

    public void createUpdateStore(Stores store) {
        enterStoreName(store.getName());
        enterStoreUniqueId(store.getStoreCode());
        enterStoreSales(store.getSales());
        clickCreateUpdateStore();
    }

    public Stores getStore(List<Stores> stores, String storeName, String storeUniqueCode) {
        if (stores == null)
            return null;
        for (Stores store : stores) {
            if (store.getName().equals(storeName) && store.getStoreCode().equals(storeUniqueCode)) {
                return store;
            }
        }
        return null;
    }

    public String getNoStoreTxt() {
        return storesTable.findElement(By.xpath("tr[1]/td[1]")).getText();
    }

    public String getIndexTitle(){
        return indexTitle.getText();
    }

}
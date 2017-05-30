package pageobjects;

import model.Beacons;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manodha on 24/4/17.
 */
public class BeaconsPage extends PageObject {

    private WebDriver webDriver;

    @FindBy(css = "div.alert.alert-success")
    private WebElement succesAlert;

    @FindBy(css = "div.alert.alert-danger")
    private WebElement errorAlert;

    @FindBy(id = "other-list")
    private WebElement otherBeaconList;

    @FindBy(css = "table.table-bordered.table-striped > tbody")
    private WebElement beaconsTable;

    @FindBy(xpath = "//a[@href='/beacons/new']")
    private WebElement newBeaconBtn;

    @FindBy(id = "beacon_name")
    private WebElement beaconNameTxt;

    @FindBy(id = "beacon_store_id")
    private WebElement storeDropDown;

    @FindBy(id = "beacon_current_status")
    private WebElement currentStatusTxt;

    @FindBy(id = "beacon_unique_reference")
    private WebElement uniqueReferenceTxt;

    @FindBy(id = "beacon_latitude")
    private WebElement latitudeTxt;

    @FindBy(id = "beacon_longitude")
    private WebElement longitudeTxt;

    @FindBy(name = "commit")
    private WebElement createUpdateBeaconBtn;


    public BeaconsPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public List<Beacons> getOtherBeaconsList() {
        List<Beacons> beacons = new ArrayList<>();
        WebElement otherBeaconsTable = otherBeaconList.findElement(By.tagName("tbody"));
        List<WebElement> beaconRows = otherBeaconsTable.findElements(By.xpath("tr"));
        int numRows, numColumns;
        numRows = beaconRows.size();
        numColumns = beaconRows.get(0).findElements(By.tagName("td")).size();

        if (numColumns <= 1)
            return beacons;

        for (int i = 0; i < numRows; i++) {
            Beacons beacon = new Beacons();
            beacon.setUniqueRef(beaconRows.get(i).findElement(By.xpath("td[1]")).getText());
            beacon.setName(beaconRows.get(i).findElement(By.xpath("td[2]")).getText());
            beacon.setStoreName(beaconRows.get(i).findElement(By.xpath("td[3]")).getText());
            beacon.setStatus(beaconRows.get(i).findElement(By.xpath("td[4]")).getText());
            beacon.setAdvertisementsLink(beaconRows.get(i).findElement(By.xpath("td[5]/a")));
            beacon.setEditLink(beaconRows.get(i).findElement(By.xpath("td[6]/a")));
            beacon.setDeleteLink(beaconRows.get(i).findElement(By.xpath("td[7]/form/input[@value='Delete']")));
            beacons.add(beacon);
        }
        return beacons;

    }

    public void clickNewBeaconBtn() {
        newBeaconBtn.click();
    }

    public void enterBeaconsName(String beaconName) {
        beaconNameTxt.clear();
        beaconNameTxt.sendKeys(beaconName);
    }

    public void selectStoreByName(String storeName) {
        Select storeDropdown = new Select(storeDropDown);
        storeDropdown.selectByVisibleText(storeName);
    }

    public void selectStoreByIndex(int index) {
        Select storeDropdown = new Select(storeDropDown);
        storeDropdown.selectByIndex(index);
    }

    public void enterUniqueRef(String uniqueRef) {
        uniqueReferenceTxt.clear();
        uniqueReferenceTxt.sendKeys(uniqueRef);
    }

    public void enterCurrentStatus(String currentStatus) {
        currentStatusTxt.clear();
        currentStatusTxt.sendKeys(currentStatus);
    }

    public void enterLatitude(String latitude) {
        latitudeTxt.clear();
        latitudeTxt.sendKeys(latitude);
    }

    public void enterLongitude(String longitude) {
        longitudeTxt.clear();
        longitudeTxt.sendKeys(longitude);
    }

    public void clickCreateUpdateBeaconBtn() {
        createUpdateBeaconBtn.click();
    }

    public void clickEditBeaconBtn(WebElement editBeaconBtn) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editBeaconBtn);
    }

    public void clickDeleteBeaconBtn(WebElement deleteBeaconBtn) {
        deleteBeaconBtn.click();
        webDriver.switchTo().alert().accept();
    }


    public BeaconAdvPage clickViewAdvertisementsLink(WebElement viewAdvertisementsLink) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", viewAdvertisementsLink);
        return new BeaconAdvPage(webDriver);
    }

    public Beacons getBeacon(List<Beacons> beacons, String uniqueRef, String beaconName) {
        for (Beacons beacon : beacons) {
            if (beacon.getUniqueRef().equals(uniqueRef) && beacon.getName().equals(beaconName)) {
                return beacon;
            }
        }
        return null;
    }

    public void createUpdateBeacon(Beacons newBeacon) {
        enterBeaconsName(newBeacon.getName());
        if (newBeacon.getStoreName().equals("")) {
            selectStoreByIndex(1);
        } else {
            selectStoreByName(newBeacon.getStoreName());
        }
        enterCurrentStatus(newBeacon.getStatus());
        enterUniqueRef(newBeacon.getUniqueRef());
        enterLatitude(newBeacon.getLatitude());
        enterLongitude(newBeacon.getLongitude());
        clickCreateUpdateBeaconBtn();
    }

    public void printAllBeacons(List<Beacons> beacons) {
        System.out.println("*********** All Beacons ***********");
        for (Beacons beacon : beacons) {
            printBeacon(beacon);
        }
    }

    public void printBeacon(Beacons beacon) {
        System.out.print("Unique Reference - " + beacon.getUniqueRef());
        System.out.print(" Beacons Name - " + beacon.getName());
        System.out.print(" Store Name - " + beacon.getStoreName());
        System.out.print(" Status - " + beacon.getStatus());
        System.out.print(" Latitude - " + beacon.getLatitude());
        System.out.println(" Longitude - " + beacon.getLongitude());
    }

}

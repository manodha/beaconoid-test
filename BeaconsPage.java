package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manodha on 24/4/17.
 */
public class BeaconsPage extends PageObject {

    @FindBy(css = "table.table-bordered.table-striped > tbody")
    WebElement beaconsTable;

    public BeaconsPage(WebDriver driver) {
        super(driver);
    }

    public List<Beacons> getAllBeacons() {
        List<Beacons> beacons = new ArrayList<>();
        List<WebElement> beaconRows = beaconsTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = beaconRows.size();
        for (int i = 0; i < numRows; i++) {
            Beacons beacon = new Beacons();
            beacon.setUniqueRef(beaconRows.get(i).findElement(By.xpath("td[1]")).getText());
            beacon.setName(beaconRows.get(i).findElement(By.xpath("td[2]")).getText());
            beacon.setStoreName(beaconRows.get(i).findElement(By.xpath("td[3]")).getText());
            beacon.setStatus(beaconRows.get(i).findElement(By.xpath("td[4]")).getText());
            beacon.setAdvertisementsLink(beaconRows.get(i).findElement(By.xpath("td[5]/a")).getAttribute("href"));
            beacon.setEditLink(beaconRows.get(i).findElement(By.xpath("td[6]/a")).getAttribute("href"));
            beacon.setDeleteLink(beaconRows.get(i).findElement(By.xpath("td[7]/a")).getAttribute("href"));
            beacons.add(beacon);
        }
        return beacons;

    }

    public void printAllBeacons() {

    }
}

package com.company;

import org.openqa.selenium.WebElement;

/**
 * Created by manodha on 24/4/17.
 */
public class Beacons {
    private String uniqueRef;
    private String name;
    private String storeName;
    private String status;
    private WebElement advertisementsLink;
    private WebElement editLink;
    private WebElement deleteLink;

    public Beacons() {
        this.uniqueRef = "";
        this.name = "";
        this.storeName = "";
        this.status = "";
    }

    public Beacons(String uniqueRef, String name, String storeName, String status) {
        this.uniqueRef = uniqueRef;
        this.name = name;
        this.storeName = storeName;
        this.status = status;
    }

    public String getUniqueRef() {
        return uniqueRef;
    }

    public void setUniqueRef(String uniqueRef) {
        this.uniqueRef = uniqueRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WebElement getAdvertisementsLink() {
        return advertisementsLink;
    }

    public void setAdvertisementsLink(WebElement advertisementsLink) {
        this.advertisementsLink = advertisementsLink;
    }

    public WebElement getEditLink() {
        return editLink;
    }

    public void setEditLink(WebElement editLink) {
        this.editLink = editLink;
    }

    public WebElement getDeleteLink() {
        return deleteLink;
    }

    public void setDeleteLink(WebElement deleteLink) {
        this.deleteLink = deleteLink;
    }
}

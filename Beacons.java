package com.company;

/**
 * Created by manodha on 24/4/17.
 */
public class Beacons {
    private String uniqueRef;
    private String name;
    private String storeName;
    private String status;
    private String advertisementsLink;
    private String editLink;
    private String deleteLink;

    public Beacons() {
        this.uniqueRef = "";
        this.name = "";
        this.storeName = "";
        this.status = "";
        this.advertisementsLink = "";
        this.editLink = "";
        this.deleteLink = "";
    }

    public Beacons(String uniqueRef, String name, String storeName, String status) {
        this.uniqueRef = uniqueRef;
        this.name = name;
        this.storeName = storeName;
        this.status = status;
        this.advertisementsLink = "";
        this.editLink = "";
        this.deleteLink = "";
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

    public String getAdvertisementsLink() {
        return advertisementsLink;
    }

    public void setAdvertisementsLink(String advertisementsLink) {
        this.advertisementsLink = advertisementsLink;
    }

    public String getEditLink() {
        return editLink;
    }

    public void setEditLink(String editLink) {
        this.editLink = editLink;
    }

    public String getDeleteLink() {
        return deleteLink;
    }

    public void setDeleteLink(String deleteLink) {
        this.deleteLink = deleteLink;
    }
}

package com.company.model;

import org.openqa.selenium.WebElement;

/**
 * Created by manodha on 1/5/17.
 */
public class Advertisement {
    private String name;
    private String beacon;
    private String category;
    private String description;
    private String image;
    private String price;
    private WebElement editLink;
    private WebElement deleteBtn;

    public Advertisement(String name, String beacon, String category, String description, String image, String price) {
        this.name = name;
        this.beacon = beacon;
        this.category = category;
        this.description = description;
        this.image = image;
        this.price = price;
        this.editLink = null;
        this.deleteBtn = null;
    }

    public Advertisement() {
        this.name = "";
        this.beacon = "";
        this.category = "";
        this.description = "";
        this.price = "";
        this.editLink = null;
        this.deleteBtn = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public WebElement getEditLink() {
        return editLink;
    }

    public void setEditLink(WebElement editLink) {
        this.editLink = editLink;
    }

    public WebElement getDeleteBtn() {
        return deleteBtn;
    }

    public void setDeleteBtn(WebElement deleteBtn) {
        this.deleteBtn = deleteBtn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

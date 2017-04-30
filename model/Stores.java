package com.company.model;

import org.openqa.selenium.WebElement;

/**
 * Created by manodha on 23/4/17.
 */
public class Stores {
    private String name;
    private String storeCode;
    private String imgUrl;
    private WebElement editLink;
    private WebElement deleteBtn;

    public Stores(String name, String storeCode, String imgUrl) {
        this.name = name;
        this.storeCode = storeCode;
        this.imgUrl = imgUrl;
    }

    public Stores() {
        this.name = "";
        this.storeCode = "";
        this.imgUrl = "";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
}

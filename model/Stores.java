package model;

import org.openqa.selenium.WebElement;

/**
 * Created by manodha on 23/4/17.
 */
public class Stores {
    private String name;
    private String storeCode;
    private String sales;
    private WebElement editLink;
    private WebElement deleteBtn;

    public Stores(String name, String storeCode, String sales) {
        this.name = name;
        this.storeCode = storeCode;
        this.sales = sales;
    }

    public Stores() {
        this.name = "";
        this.storeCode = "";
        this.sales = "";
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

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
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

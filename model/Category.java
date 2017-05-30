package model;

import org.openqa.selenium.WebElement;

/**
 * Created by manodha on 29/4/17.
 */
public class Category {
    private String categoryId;
    private String categoryName;
    private String categoryDescription;
    private WebElement editButton;
    private WebElement deleteButton;

    public Category(String categoryName, String categoryDescription) {
        this.categoryId = "";
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.editButton = null;
        this.deleteButton = null;
    }

    public Category() {
        this.categoryId = "";
        this.categoryName = "";
        this.categoryDescription = "";
        this.editButton = null;
        this.deleteButton = null;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public WebElement getEditButton() {
        return editButton;
    }

    public void setEditButton(WebElement editButton) {
        this.editButton = editButton;
    }

    public WebElement getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(WebElement deleteButton) {
        this.deleteButton = deleteButton;
    }
}

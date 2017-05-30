package pageobjects;

import model.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manodha on 29/4/17.
 */
public class CategoryPage extends PageObject {

    private WebDriver webDriver;

    @FindBy(tagName = "h1")
    private WebElement title;

    @FindBy(xpath = "//a[@href='/categories/new']")
    private WebElement newCategoryBtn;

    @FindBy(css = "table.table-bordered.table-striped > tbody") //table table-bordered table-striped
    private WebElement categoriesTable;

    @FindBy(id = "category_name")
    private WebElement categoryNameTxt;

    @FindBy(id = "category_description")
    private WebElement categoryDescriptionTxt;

    @FindBy(name = "commit")
    private WebElement createCategoryBtn;

    public CategoryPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        List<WebElement> categoryRows = categoriesTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = categoryRows.size();
        numColumns = categoryRows.get(0).findElements(By.tagName("td")).size();

        if (numColumns <= 1)
            return categories;

        for (int i = 0; i < numRows; i++) {
            Category category = new Category();
            category.setCategoryId(categoryRows.get(i).findElement(By.xpath("td[1]")).getText());
            category.setCategoryName(categoryRows.get(i).findElement(By.xpath("td[2]")).getText());
            category.setCategoryDescription(categoryRows.get(i).findElement(By.xpath("td[3]")).getText());
            category.setEditButton(categoryRows.get(i).findElement(By.xpath("td[4]/a")));
            category.setDeleteButton(categoryRows.get(i).findElement(By.xpath("td[5]/form/input[@type='submit']")));
            categories.add(category);
        }
        return categories;
    }

    public void clickNewCategoryBtn() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", newCategoryBtn);
    }

    public void clickCreateEditCategoryBtn() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", createCategoryBtn);
    }

    public void enterCategoryName(String categoryName) {
        categoryNameTxt.clear();
        categoryNameTxt.sendKeys(categoryName);
    }

    public void enterCategoryDescription(String categoryDescription) {
        categoryDescriptionTxt.clear();
        categoryDescriptionTxt.sendKeys(categoryDescription);
    }

    public void clickEditCategoryBtn(WebElement editBtn) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editBtn);
    }

    public void clickDeleteCategoryBtn(WebElement deleteBtn) {
        deleteBtn.click();
        webDriver.switchTo().alert().accept();
    }

    public void createUpdateCategory(Category category) {
        enterCategoryName(category.getCategoryName());
        enterCategoryDescription(category.getCategoryDescription());
        clickCreateEditCategoryBtn();
    }


    public Category getCategory(List<Category> categories, String categoryName, String categoryDescription) {
        for (Category category : categories) {
            if (category.getCategoryName().equals(categoryName) && category.getCategoryDescription().equals(categoryDescription)) {
                return category;
            }

        }
        return null;
    }

    public void printAllCategories(List<Category> categoryList) {
        for (Category category : categoryList) {
            printCategory(category);
        }

    }

    public void printCategory(Category category) {
        System.out.print("Category Id - " + category.getCategoryId());
        System.out.print(" Category Name - " + category.getCategoryName());
        System.out.println(" Category Name - " + category.getCategoryDescription());
    }
}

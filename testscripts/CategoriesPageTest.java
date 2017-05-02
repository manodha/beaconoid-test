package com.company.testscripts;

import com.company.model.Category;
import com.company.pageobjects.CategoryPage;
import com.company.pageobjects.NavigationMenu;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 29/4/17.
 */
public class CategoriesPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private List<Category> allCategories;
    private CategoryPage categoryPage;


    @BeforeTest
    @Parameters({"email", "password"})
    private void accessCategoriesPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        categoryPage = navigationMenu.clickCatogoriesLink();
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    @Parameters({"categoryName", "categoryDescription"})
    public void createCategoryTC(String categoryName, String categoryDescription) {

        createCategory(new Category(categoryName, categoryDescription));
    }

    @Test(priority = 2)
    @Parameters({"categoryName", "categoryDescription", "categoryNameNew", "categoryDescriptionNew"})
    public void updateCategoryTC(String categoryName, String categoryDescription, String categoryNameNew,
                                 String categoryDescriptionNew) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        allCategories = categoryPage.getAllCategories();

        Category category = categoryPage.getCategory(allCategories, categoryName, categoryDescription);

        updateCategory(category, new Category(categoryNameNew, categoryDescriptionNew));
    }

    @Test(priority = 3)
    @Parameters({"categoryNameNew", "categoryDescriptionNew"})
    public void deleteCategoryTC(String categoryName, String categoryDescription) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        allCategories = categoryPage.getAllCategories();

        Category category = categoryPage.getCategory(allCategories, categoryName, categoryDescription);
        categoryPage.clickDeleteCategoryBtn(category.getDeleteButton());
    }


    private void createCategory(Category category) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();
        assertEquals(addCategoryUrl, webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(category);
        //testCategories.add(new Category(categoryName, categoryDescription));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
    }

    private void updateCategory(Category oldCategory, Category newCategory) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickEditCategoryBtn(oldCategory.getEditButton());
        assertEquals(oldCategory.getEditButton().getAttribute("href"), webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(newCategory);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
    }



}

package com.company;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created by manodha on 29/4/17.
 */
public class CategoriesPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private List<Category> allCategories;
    private List<Category> testCategories = new ArrayList<>();
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
        allCategories = categoryPage.getAllCategories();
        printAllCategories(allCategories);

    }

    @Test(priority = 1)
    @Parameters({"categoryName", "categoryDescription"})
    public void createCategoryTC(String categoryName, String categoryDescription) {

        createCategory(categoryName, categoryDescription);
    }

    @Test(priority = 2)
    @Parameters({"categoryName", "categoryDescription", "categoryNameNew", "categoryDescriptionNew"})
    public void updateCategoryTC(String categoryName, String categoryDescription, String categoryNameNew,
                                 String categoryDescriptionNew) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        allCategories = categoryPage.getAllCategories();

        Category category = getCategory(allCategories, categoryName, categoryDescription);

        updateCategory(category, categoryNameNew, categoryDescriptionNew);
    }

    @Test(priority = 3)
    @Parameters({"categoryNameNew", "categoryDescriptionNew"})
    public void deleteCategoryTC(String categoryName, String categoryDescription) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        allCategories = categoryPage.getAllCategories();

        Category category = getCategory(allCategories, categoryName, categoryDescription);
        deleteCategory(category);
    }


    private void createCategory(String categoryName, String categoryDescription) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();
        assertEquals(addCategoryUrl, webDriver.getCurrentUrl());
        categoryPage.enterCategoryName(categoryName);
        categoryPage.enterCategoryDescription(categoryDescription);
        categoryPage.clickCreateEditCategoryBtn();
        //testCategories.add(new Category(categoryName, categoryDescription));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
    }

    private void updateCategory(Category category, String categoryName, String categoryDescription) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickEditCategoryBtn(category.getEditButton());
        categoryPage.enterCategoryName(categoryName);
        categoryPage.enterCategoryDescription(categoryDescription);
        categoryPage.clickCreateEditCategoryBtn();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
    }

    private void deleteCategory(Category category) {
        categoryPage.clickDeleteCategoryBtn(category.getDeleteButton());
    }

    private Category getCategory(List<Category> categories, String categoryName, String categoryDescription) {
        for (Category category : categories) {
            if (category.getCategoryName().equals(categoryName) && category.getCategoryDescription().equals(categoryDescription)) {
                return category;
            }

        }
        return null;
    }

    private void printAllCategories(List<Category> categoryList) {
        for (Category category : categoryList) {
            printCategory(category);
        }

    }

    private void printCategory(Category category) {
        System.out.print("Category Id - " + category.getCategoryId());
        System.out.print(" Category Name - " + category.getCategoryName());
        System.out.println(" Category Name - " + category.getCategoryDescription());
    }

}

package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.model.Category;
import com.company.pageobjects.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created by manodha on 29/4/17.
 */
public class CategoriesPageTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private List<Category> allCategories;
    private List<Advertisement> allAdvertisements;
    private CategoryPage categoryPage;
    private StoresPage storesPage;
    private AdvertisementsPage advertisementsPage;
    private BeaconsPage beaconsPage;


    @BeforeTest
    @Parameters({"email", "password"})
    private void accessCategoriesPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);

        storesPage = navigationMenu.clickStoresLink();
        assertEquals(storesUrl, webDriver.getCurrentUrl());
        createStore(storesPage, defaultTestStore);

        categoryPage = navigationMenu.clickCatogoriesLink();
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1, testName = "TC023")
    @Parameters({"categoryName", "categoryDescription"})
    public void createCategoryTC023(String categoryName, String categoryDescription) {

        // Creating a new category.
        createCategory(categoryPage, new Category(categoryName, categoryDescription));
        // Getting all the categories list
        allCategories = categoryPage.getAllCategories();
        // Checking if the category has been created successfully.
        assertThat(allCategories, hasItem(allOf(hasProperty("categoryName", equalTo(categoryName)),
                hasProperty("categoryDescription", equalTo(categoryDescription)))));
    }

    @Test(priority = 2, testName = "TC024")
    @Parameters({"categoryName", "categoryDescription"})
    public void createCategoryTC024(String categoryName, String categoryDescription) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();

        /*To create a category it is required to enter both category name and the category description.*/

        categoryPage.createUpdateCategory(new Category("", categoryDescription));
        assertEquals(addCategoryUrl, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category(categoryName, ""));
        assertEquals(addCategoryUrl, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category("", ""));
        assertEquals(addCategoryUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 3, testName = "TC025")
    @Parameters({"categoryName", "categoryDescription", "categoryNameNew", "categoryDescriptionNew"})
    public void updateCategoryTC025(String categoryName, String categoryDescription, String categoryNameNew,
                                    String categoryDescriptionNew) {
        // Accessing the Categories Page
        if (!webDriver.getCurrentUrl().equals(categoriesUrl))
            webDriver.get(categoriesUrl);
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());

        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), categoryName, categoryDescription);
        updateCategory(category, new Category(categoryNameNew, categoryDescriptionNew));

        allCategories = categoryPage.getAllCategories();

        // Checking the category list doesn't have an category with old category name and description
        assertThat(allCategories, not(hasItem(allOf(hasProperty("categoryName", equalTo(categoryName)),
                hasProperty("categoryDescription", equalTo(categoryDescription))))));

        // Checking the category list has an category with old category name and description
        assertThat(allCategories, hasItem(allOf(hasProperty("categoryName", equalTo(categoryNameNew)),
                hasProperty("categoryDescription", equalTo(categoryDescriptionNew)))));
    }

    @Test(priority = 4, testName = "TC026")
    @Parameters({"categoryNameNew", "categoryDescriptionNew"})
    public void deleteCategoryTC026(String categoryName, String categoryDescription) {
        AdvertisementsPage advertisementsPage = navigationMenu.clickAdvertisementsLink();
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allAdvertisements = advertisementsPage.getAllAdvertisements();
        /* Checking that there are no advertisement with this category */
        assertThat(allAdvertisements, not(hasItem(hasProperty("category", equalTo(categoryName)))));

        categoryPage = navigationMenu.clickCatogoriesLink();
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Deleting the category
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), categoryName, categoryDescription);
        categoryPage.clickDeleteCategoryBtn(category.getDeleteButton());
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        allCategories = categoryPage.getAllCategories();
        // Checking that the category has been deleted successfully.
        assertThat(allCategories, not(hasItem(allOf(hasProperty("categoryName", equalTo(categoryName)),
                hasProperty("categoryDescription", equalTo(categoryDescription))))));
    }

    @Test(priority = 5, testName = "TC028")
    public void getListOfCategories() {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        createCategory(categoryPage, defaultTestCategory);
        allCategories = categoryPage.getAllCategories();
        assertNotNull(allCategories);
    }

    @Test(priority = 6, testName = "TC030")
    public void checkIfAdvertisementBeCreated() {
        beaconsPage = navigationMenu.clickBeconsLink();
        assertEquals(beaconsUrl, webDriver.getCurrentUrl());
        createBeacon(beaconsPage, defaultTestBeacon);
        advertisementsPage = navigationMenu.clickAdvertisementsLink();
        assertEquals(advertisementsUrl, webDriver.getCurrentUrl());
        createAdvertisement(advertisementsPage, defaultTestAdvertisement);
        assertThat(advertisementsPage.getAllAdvertisements(), hasItem(hasProperty("category",
                equalTo(defaultTestAdvertisement.getCategory()))));
    }

    // @Test(priority = 7, testName = "TC027")
    public void checkIfCategoryAssiToAdvBeDeleted() {
        if (webDriver.getCurrentUrl().equals(categoriesUrl)) {
            categoryPage = navigationMenu.clickCatogoriesLink();
            try {
                Thread.sleep(waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        deleteDefaultTestCategory(categoryPage);
        allCategories = categoryPage.getAllCategories();
        assertThat(allCategories, hasItem(allOf(hasProperty("categoryName", equalTo(defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(defaultTestCategory.getCategoryDescription())))));
    }

    //@Test(priority = 8, testName = "TC029")
    public void checkIfCateCreaWithSameName() {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        createCategory(categoryPage, defaultTestCategory);


    }

    @AfterTest
    public void clearAllTestData() {
        // Deleting the Test Advertisement that was created for the Test Case TC030
        if (!webDriver.getCurrentUrl().equals(advertisementsUrl)) {
            advertisementsPage = navigationMenu.clickAdvertisementsLink();
            try {
                Thread.sleep(waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        deleteDefaultTestAdvertisement(advertisementsPage);


        // Deleting the Test Category that was created for the test case TC028
        categoryPage = navigationMenu.clickCatogoriesLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteDefaultTestCategory(categoryPage);


        // Deleting the Test Beacon that was created for the test case TC030
        beaconsPage = navigationMenu.clickBeconsLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteDefaultTestBeacon(beaconsPage);

        // Deleting the Test Store that was created in the @BeforeTest Method
        storesPage = navigationMenu.clickStoresLink();
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteDefaultTestStore(storesPage);
    }

    private void updateCategory(Category oldCategory, Category newCategory) {
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickEditCategoryBtn(oldCategory.getEditButton());
        assertEquals(oldCategory.getEditButton().getAttribute("href"), webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(newCategory);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(categoriesUrl, webDriver.getCurrentUrl());
    }
}

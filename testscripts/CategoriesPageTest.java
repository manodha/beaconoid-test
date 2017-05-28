package com.company.testscripts;

import com.company.model.Advertisement;
import com.company.model.Category;
import com.company.pageobjects.*;
import com.company.util.WebConstants;
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
    private CategoryPage categoryPage;
    private StoresPage storesPage;
    private AdvertisementsPage advertisementsPage;
    private BeaconsPage beaconsPage;


    @BeforeTest
    @Parameters({"email", "password"})
    private void accessCategoriesPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);

        storesPage = accessStoresPage(navigationMenu);
        createStore(storesPage, WebConstants.defaultTestStore);

        categoryPage = accessCategoriesPage(navigationMenu);
    }

    @Test(priority = 1, testName = "TC023")
    @Parameters({"categoryName", "categoryDescription"})
    public void checkIfCateCanBeCreaWAF(String categoryName, String categoryDescription) {

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
    public void checkIfCateCanBeCreaWORF(String categoryName, String categoryDescription) {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();

        /*To create a category it is required to enter both category name and the category description.*/

        categoryPage.createUpdateCategory(new Category("", categoryDescription));
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category(categoryName, ""));
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category("", ""));
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 3, testName = "TC025")
    @Parameters({"categoryName", "categoryDescription", "categoryNameNew", "categoryDescriptionNew"})
    public void checkIfCateCanBeUpda(String categoryName, String categoryDescription, String categoryNameNew,
                                     String categoryDescriptionNew) {
        // Accessing the Categories Page
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(navigationMenu);

        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), categoryName, categoryDescription);
        updateCategory(categoryPage, category, new Category(categoryNameNew, categoryDescriptionNew));

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
    public void checkIfCateCanBeDel(String categoryName, String categoryDescription) {
        AdvertisementsPage advertisementsPage = accessAdvertisementsPage(navigationMenu);

        List<Advertisement> allAdvertisements = advertisementsPage.getAllAdvertisements();
        /* Checking that there are no advertisement with this category */
        assertThat(allAdvertisements, not(hasItem(hasProperty("category", equalTo(categoryName)))));

        categoryPage = accessCategoriesPage(navigationMenu);
        // Deleting the category
        deleteCategory(categoryPage, new Category(categoryName, categoryDescription));

        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        allCategories = categoryPage.getAllCategories();
        // Checking that the category has been deleted successfully.
        assertThat(allCategories, not(hasItem(allOf(hasProperty("categoryName", equalTo(categoryName)),
                hasProperty("categoryDescription", equalTo(categoryDescription))))));
    }

    @Test(priority = 5, testName = "TC028")
    public void getListOfCategories() {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        createCategory(categoryPage, WebConstants.defaultTestCategory);
        allCategories = categoryPage.getAllCategories();
        assertNotNull(allCategories);
    }

    @Test(priority = 6, testName = "TC030")
    public void checkIfCateCanBeAssiToAdver() {
        beaconsPage = accessBeaconsPage(navigationMenu);
        createBeacon(beaconsPage, WebConstants.defaultTestBeacon);

        advertisementsPage = accessAdvertisementsPage(navigationMenu);
        createAdvertisement(advertisementsPage, WebConstants.defaultTestAdvertisement);

        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(navigationMenu);

        assertThat(advertisementsPage.getAllAdvertisements(), allOf(hasItem(
                hasProperty("category", equalTo(WebConstants.defaultTestAdvertisement.getCategory()))
        )));
    }

    @Test(priority = 7, testName = "TC027")
    public void checkIfCateAssiToAdverBeDel() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(navigationMenu);
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        deleteCategory(categoryPage, WebConstants.defaultTestCategory);
        assertEquals(WebConstants.defaultTestCategory.getCategoryName() + WebConstants.cantDelCateMsg, categoryPage.getDangerAlert());
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(hasProperty("categoryName", equalTo(WebConstants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(WebConstants.defaultTestCategory.getCategoryDescription())))));
    }

    @Test(priority = 8, testName = "TC029")
    public void checkIfCateCreaWithSameName() {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        createCategory(categoryPage, WebConstants.defaultTestCategory);


    }

    @AfterTest
    public void clearAllTestData() {
        // Deleting the Test Advertisement that was created for the Test Case TC030
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl)) {
            advertisementsPage = accessAdvertisementsPage(navigationMenu);
        }
        deleteAdvertisement(advertisementsPage, WebConstants.defaultTestAdvertisement);

        // Deleting the Test Category that was created for the test case TC028
        categoryPage = accessCategoriesPage(navigationMenu);
        deleteCategory(categoryPage, WebConstants.defaultTestCategory);


        // Deleting the Test Beacon that was created for the test case TC030
        beaconsPage = accessBeaconsPage(navigationMenu);
        deleteBeacon(beaconsPage, WebConstants.defaultTestBeacon);

        // Deleting the Test Store that was created in the @BeforeTest Method
        storesPage = accessStoresPage(navigationMenu);
        deleteStore(storesPage, WebConstants.defaultTestStore);
    }
}

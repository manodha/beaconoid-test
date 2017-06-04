package testscripts;

import model.Beacons;
import model.Category;
import model.Stores;
import org.junit.Assert;
import org.testng.annotations.*;
import pageobjects.*;
import util.WebConstants;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;
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
    private void setupTestData(String email, String password) throws InterruptedException {
        navigationMenu = loginToBeaconoid(webDriver, email, password);

        storesPage = accessStoresPage(webDriver, navigationMenu);
        createStore(webDriver, storesPage, WebConstants.defaultTestStore);

        beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        createBeacon(webDriver, beaconsPage, WebConstants.defaultTestBeacon);
    }

    @Test(priority = 1, testName = "TC_CP_01")
    public void chkIfUserCanAccessCatePage(){
        // Accessing the categories page.
        categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        assertEquals(WebConstants.cateIndexTitle, categoryPage.getCateIndexTitle());
    }

    @Test(priority = 2, testName = "TC_CP_02")
    public void  chkIfNoCateMsgIsShown(){
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        // Verifying that there are no categories
        assertNull(categoryPage.getAllCategories());
        // Verifying if the message 'No categories found.' is visible
        Assert.assertEquals(categoryPage.getNoCateTxt(), WebConstants.noCateTxt);
    }

    @Test(priority = 3, testName = "TC_CP_03")
    @Parameters({"categoryName", "categoryDescription"})
    public void chkIfCateCanBeCreaWORF(String name, String desc){
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category("", ""));
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category(name, ""));
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category("", desc));
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 4, testName = "TC_CP_04")
    @Parameters({"categoryName", "categoryDescription"})
    public void chkIfCateCanBeCreaWRF(String name, String desc) throws InterruptedException {
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(new Category(name, desc));
        Thread.sleep(WebConstants.waitMilliSeconds);

        // Checking if the successful message is shown when a Category get added successfully.
        assertEquals(WebConstants.creaCateSuccess, categoryPage.getSucessAlert());

        // Checking if the category has been created successfully.
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(name)),
                hasProperty("categoryDescription", equalTo(desc))
        )));
    }

    @Test(priority = 5, testName = "TC_CP_05")
    @Parameters({"categoryName", "categoryDescription"})
    public void chkIfCateCanBeCreWSN(String name, String desc) throws InterruptedException {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        categoryPage.clickNewCategoryBtn();
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(new Category(name, desc));
        Thread.sleep(WebConstants.waitMilliSeconds);

        // Verifying that URL didn't changed and a Error message is shown to the user
        assertEquals(WebConstants.addCategoryUrl, webDriver.getCurrentUrl());
        assertEquals(WebConstants.sameCreaCateName, categoryPage.getDangerAlert());

        // Acessing the Categories Page
        if(!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);

        // Getting the num of categories with the same name
        int numOfCateWSN = 0;
        allCategories = categoryPage.getAllCategories();
        for (Category category : allCategories){
            if(category.getCategoryName().equals(name))
                numOfCateWSN++;
        }
        // Verifying that the num of categories with the same name is not more than one
        assertEquals(1, numOfCateWSN);
    }

    @Test(priority = 6, testName = "TC_CP_06")
    @Parameters({"categoryName", "categoryDescription"})
    public void chkIfCateIsInCateTable(String name, String desc){
        // TODO Uncomment the below code.
        //if(!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        // Check if Category Table has category which was created
        assertNotNull(categoryPage.getAllCategories());
        // Check if the created Category is in the Category Table
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(
                hasProperty("categoryName", equalTo(name)),
                hasProperty("categoryDescription", equalTo(desc)
                ))));
    }

    @Test(priority = 7, testName = "TC_CP_07")
    @Parameters({"categoryName", "categoryDescription", "categoryNameNew", "categoryDescriptionNew"})
    public void chkIfCateCanUpdaWORF(String oldName, String oldDesc, String newName, String newDesc){
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), oldName, oldDesc);
        String editLink = category.getEditButton().getAttribute("href");
        categoryPage.clickEditCategoryBtn(category.getEditButton());
        assertEquals(editLink, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category("",""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category(newName,""));
        assertEquals(editLink, webDriver.getCurrentUrl());

        categoryPage.createUpdateCategory(new Category("",newDesc));
        assertEquals(editLink, webDriver.getCurrentUrl());

        if(!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);

        assertThat(categoryPage.getAllCategories(), not(hasItem(allOf(
                hasProperty("categoryName", equalTo(newName)),
                hasProperty("categoryDescription", equalTo(newDesc)
                )))));
    }


    @Test(priority = 8, testName = "TC_CP_08")
    @Parameters({"categoryName", "categoryDescription", "categoryNameNew", "categoryDescriptionNew"})
    public void chkIfCateCanBeUpdaWRF(String categoryName, String categoryDescription, String categoryNameNew,
                                     String categoryDescriptionNew) {
        // Accessing the Categories Page
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);

        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), categoryName, categoryDescription);
        updateCategory(webDriver, categoryPage, category, new Category(categoryNameNew, categoryDescriptionNew));

        // Checking the update successful message
        assertEquals(categoryNameNew+WebConstants.updaCateSuccees, categoryPage.getSucessAlert());

        allCategories = categoryPage.getAllCategories();

        // Checking the category list doesn't have an category with old category name and description
        assertThat(allCategories, not(hasItem(allOf(hasProperty("categoryName", equalTo(categoryName)),
                hasProperty("categoryDescription", equalTo(categoryDescription))))));

        // Checking the category list has an category with old category name and description
        assertThat(allCategories, hasItem(allOf(hasProperty("categoryName", equalTo(categoryNameNew)),
                hasProperty("categoryDescription", equalTo(categoryDescriptionNew)))));
    }

    @BeforeGroups("UpdaCateWSN")
    public void creaDefCate(){
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        createCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);
    }

    @Test(priority = 9, testName = "TC_CP_09", groups = "UpdaCateWSN")
    @Parameters({"categoryNameNew", "categoryDescriptionNew"})
    public void chkIfCateCanBeUpdaWSN(String name, String desc) throws InterruptedException {
        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        Category category = categoryPage.getCategory(categoryPage.getAllCategories(), name, desc);
        String editLink = category.getEditButton().getAttribute("href");
        categoryPage.clickEditCategoryBtn(category.getEditButton());

        assertEquals(editLink, webDriver.getCurrentUrl());
        categoryPage.createUpdateCategory(WebConstants.defaultTestCategory);
        Thread.sleep(WebConstants.waitMilliSeconds);

        assertEquals(WebConstants.sameUpdaCateName, categoryPage.getDangerAlert());
        assertEquals(editLink, webDriver.getCurrentUrl());

        if(!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);

        allCategories = categoryPage.getAllCategories();
        int numOfCateWSN = 0;
        for(Category cate : allCategories){
            if(cate.getCategoryName().equals(name))
                numOfCateWSN++;
        }
        // Verifying that there is only one category with the same name.
        assertEquals(1, numOfCateWSN);


    }

    @BeforeGroups("DelCate")
    @Parameters({"categoryNameNew"})
    public void creaDefAdver(String categoryName){
        advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        createAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        assertThat(advertisementsPage.getAllAdvertisements(), not(hasItem(
                hasProperty("category", equalTo(categoryName))
        )));
    }


    @Test(priority = 10, testName = "TC_CP_10", groups = "DelCate")
    @Parameters({"categoryNameNew", "categoryDescriptionNew"})
    public void chkIfCateWithNoAdverCanBeDel(String categoryName, String categoryDescription) {
        if(!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);

        // Deleting the category
        deleteCategory(webDriver, categoryPage, new Category(categoryName, categoryDescription));

        assertEquals(WebConstants.categoriesUrl, webDriver.getCurrentUrl());
        // Checking the successful message
        assertEquals(categoryName+WebConstants.delStoreSucess, categoryPage.getSucessAlert());
        // Checking that the category has been deleted successfully.
        assertThat(categoryPage.getAllCategories(), not(hasItem(allOf(hasProperty("categoryName", equalTo(categoryName)),
                hasProperty("categoryDescription", equalTo(categoryDescription))))));
    }

    @Test(priority = 11, testName = "TC_CP_11", groups = "DelCate")
    public void chkIfCateWithAdverCanBeDel() {
        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        deleteCategory(webDriver, categoryPage, WebConstants.defaultTestCategory);
        assertEquals(WebConstants.defaultTestCategory.getCategoryName() + WebConstants.delCateError, categoryPage.getDangerAlert());
        assertThat(categoryPage.getAllCategories(), hasItem(allOf(hasProperty("categoryName", equalTo(WebConstants.defaultTestCategory.getCategoryName())),
                hasProperty("categoryDescription", equalTo(WebConstants.defaultTestCategory.getCategoryDescription())))));
    }

    @AfterGroups("DelCate")
    public void delDefAdver(){
        if (!webDriver.getCurrentUrl().equals(WebConstants.advertisementsUrl))
            advertisementsPage = accessAdvertisementsPage(webDriver, navigationMenu);
        deleteAdvertisement(webDriver, advertisementsPage, WebConstants.defaultTestAdvertisement);
    }

    // TODO Implement the Test Cases TC_CP_12, TC_CP_13 and TC_CP_14.

    @AfterTest
    public void clearAllTestData() throws InterruptedException {
        if (!webDriver.getCurrentUrl().equals(WebConstants.beaconsUrl))
            beaconsPage = accessBeaconsPage(webDriver, navigationMenu);
        List<Beacons> beacons = beaconsPage.getRegOtherBeacons(WebConstants.otherBeaconTitle);
        for(Beacons beacon : beacons){
            deleteBeacon(webDriver, beaconsPage, beacon);
        }

        if (!webDriver.getCurrentUrl().equals(WebConstants.categoriesUrl))
            categoryPage = accessCategoriesPage(webDriver, navigationMenu);
        allCategories = categoryPage.getAllCategories();
        for(Category category: allCategories){
            deleteCategory(webDriver, categoryPage, category);
        }

        if (!webDriver.getCurrentUrl().equals(WebConstants.storesUrl))
            storesPage = accessStoresPage(webDriver, navigationMenu);
        List<Stores> stores = storesPage.getAllStores();
        for(Stores store: stores){
            deleteStore(webDriver, storesPage, store);
        }
    }
}

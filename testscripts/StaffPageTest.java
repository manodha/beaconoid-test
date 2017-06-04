package testscripts;

import model.Staff;
import org.testng.annotations.*;
import pageobjects.LoginPage;
import pageobjects.NavigationMenu;
import pageobjects.StaffPage;
import util.WebConstants;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by manodha on 11/5/17.
 */
public class StaffPageTest extends FunctionalTest {


    private NavigationMenu navigationMenu;
    private StaffPage staffPage;
    private List<Staff> allStaff;
    private LoginPage loginPage;


    @BeforeGroups({"SuperAdmin", "password"})
    @Parameters({"email", "password"})
    public void loginBySuperAdmin(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);

    }

    @Test(priority = 1, testName = "TC_STP_01", groups = "SuperAdmin")
    public void checkIfStaffLinkIsVisibleForSA() {
        // Checking if the Staff Link is present in the Navigation Menu.
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNotNull(navigationMenu.getStaffLink());
    }

    @Test(priority = 2, testName = "TC_STP_02", groups = "SuperAdmin")
    public void checkIfSACanAccessStaffPage() {
        // Accessing the Staff Page
        staffPage = accessStaffPage(webDriver, navigationMenu);
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 3, testName = "TC_STP_06", groups = "SuperAdmin")
    public void checkAtLeastOneUserIsAdded() {
        // Getting all the staff members from the staff page
        allStaff = staffPage.getAllStaff();
        // Checking if the List of Staff Member is Null
        assertNotNull(allStaff);
    }

    @Test(priority = 4, testName = "TC_STP_07", groups = "SuperAdmin")
    @Parameters({"super_admin_role"})
    public void checkIfSACanDelSARecord(String role) {
        Staff superAdmin = staffPage.getStaffByRole(role);
        deleteStaff(webDriver, superAdmin, staffPage);
        assertEquals(WebConstants.notAuthorisedMsg, staffPage.getDangerAlert());
        allStaff = staffPage.getAllStaff();
        assertThat(allStaff, hasItem(hasProperty("role", equalTo(role))));
    }

    @Test(priority = 5, testName = "TC_STP_08", groups = "SuperAdmin")
    @Parameters({"userName", "userEmail", "nickname", "userPassword", "confirmPassword", "store_manager_role"})
    public void checkIfSACanAddStaff(String name, String email, String nickname, String password, String confirmPassword, String role) {
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));
        allStaff = staffPage.getAllStaff();
        assertThat(allStaff, hasItem(hasProperty("email", equalTo(email))));
    }

    @Test(priority = 6, testName = "TC_STP_09", groups = "SuperAdmin")
    @Parameters({"userEmail", "userNameNew", "userEmailNew", "nicknameNew", "userPasswordNew", "confirmPasswordNew", "beacon_manager_role"})
    public void checkIfSACanUpdateStaff(String userEmail, String name, String userEmailNew, String nickname, String password,
                                        String confirmPassword, String role) {
        Staff staff = staffPage.getStaffByEmail(userEmail);
        updateStaff(staff, new Staff(name, userEmailNew, nickname, password, confirmPassword, role));

        allStaff = staffPage.getAllStaff();
        // Verifying that a user with the Old email address doesn't exists anymore
        assertThat(allStaff, not(hasItem(hasProperty("email", equalTo(userEmail)))));
        // Verifying that a user with the updated details exists
        assertThat(allStaff, hasItem(hasProperty("email", equalTo(userEmailNew))));

    }

    @Test(priority = 7, testName = "TC_STP_10", groups = "SuperAdmin")
    @Parameters({"userEmailNew"})
    public void checkIfSACanDelStaff(String userEmailNew) {
        Staff staff = staffPage.getStaffByEmail(userEmailNew);
        // Deleting the Staff Member
        deleteStaff(webDriver, staff, staffPage);
        allStaff = staffPage.getAllStaff();
        // Verifying that the staff has been deleted successfully
        assertThat(allStaff, not(hasItem(hasProperty("email", equalTo(userEmailNew)))));

    }

    @AfterGroups("SuperAdmin")
    @Parameters({"userName", "userEmail", "nickname", "userPassword", "confirmPassword", "admin_role"})
    public void addAdminUserLogout(String name, String email, String nickname, String password, String confirmPassword,
                                   String role) {
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 8, testName = "TC_STP_20")
    @Parameters({"userEmail", "userPassword"})
    public void checkIfAddedUserCanLogin(String email, String password) {
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        navigationMenu = loginToBeaconoid(webDriver, email, password);
    }

    @Test(priority = 9, testName = "TC_STP_01", groups = "Admin")
    public void checkIfStaffLinkIsVisibleForA() {
        // Checking if the Staff Link is present in the Navigation Menu.
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        assertNotNull(navigationMenu.getStaffLink());
    }

    @Test(priority = 10, testName = "TC_STP_02", groups = "Admin")
    public void checkIfACanAccessStaffPage() {
        staffPage = accessStaffPage(webDriver, navigationMenu);
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
    }

    @Test(priority = 11, testName = "TC_STP_11", groups = "Admin")
    @Parameters({"super_admin_role"})
    public void checkIfACanDelSA(String role) {
        allStaff = staffPage.getAllStaff();
        Staff superAdmin = staffPage.getStaffByRole(role);
        deleteStaff(webDriver, superAdmin, staffPage);

        // Checking the error message.
        assertEquals(WebConstants.notAuthorisedMsg, staffPage.getDangerAlert());

        // Checking that the Super Admin hasn't been deleted.
        allStaff = staffPage.getAllStaff();
        assertThat(allStaff, hasItem(hasProperty("role", equalTo(role))));
    }

    @Test(priority = 12, testName = "TC_STP_12", groups = "Admin")
    @Parameters({"userNameNew", "userEmailNew", "nicknameNew", "userPasswordNew", "confirmPasswordNew", "beacon_manager_role"})
    public void checkIfACanAddStaff(String name, String email, String nickname, String password, String confirmPassword,
                                    String role) {
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));
        allStaff = staffPage.getAllStaff();
        assertThat(allStaff, hasItem(hasProperty("email", equalTo(email))));
    }

    @Test(priority = 13, testName = "TC_STP_13", groups = "Admin")
    @Parameters({"userEmailNew", "name3", "email3", "nickname3", "password3", "confirmPassword3", "store_manager_role"})
    public void checkIfACanUpadateStaff(String emailOld, String name, String email, String nickname, String password,
                                        String confirmPassword, String role) {

        Staff staff = staffPage.getStaffByEmail(emailOld);
        updateStaff(staff, new Staff(name, email, nickname, password, confirmPassword, role));

        allStaff = staffPage.getAllStaff();
        // Verifying that a user with the Old email address doesn't exists anymore
        assertThat(allStaff, not(hasItem(hasProperty("email", equalTo(emailOld)))));
        // Verifying that a user with the updated details exists
        assertThat(allStaff, hasItem(hasProperty("email", equalTo(email))));
    }

    @Test(priority = 14, testName = "TC_STP_14", groups = "Admin")
    @Parameters({"email3"})
    public void checkIfACanDelStaff(String email) {
        Staff staff = staffPage.getStaffByEmail(email);
        // Deleting the Staff Member
        deleteStaff(webDriver, staff, staffPage);

        assertEquals(staff.getName() + WebConstants.deleteStaffMsg, staffPage.getSucessAlert());
        allStaff = staffPage.getAllStaff();
        // Verifying that the staff has been deleted successfully
        assertThat(allStaff, not(hasItem(hasProperty("email", equalTo(email)))));
    }

    @Test(priority = 15, testName = "TC_STP_15", groups = "Admin")
    @Parameters({"userEmail"})
    public void checkIfACanDelHisRecord(String email) {
        Staff staff = staffPage.getStaffByEmail(email);
        // Deleting the Staff Member
        staffPage.clickDeleteStaffBtn(staff.getDeleteBtn());
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loginPage = new LoginPage(webDriver);
        assertEquals(WebConstants.loginTitle, loginPage.getTitleText());
        assertEquals(WebConstants.loginUrl, webDriver.getCurrentUrl());
    }

    @AfterGroups("Admin")
    public void logOutIfNot() {
        if (navigationMenu.getLogoutLink() != null) {
            navigationMenu.clickLogoutLink();
            try {
                Thread.sleep(WebConstants.waitMilliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeGroups("General")
    @Parameters({"email", "password"})
    public void accessStaffPage(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        staffPage = accessStaffPage(webDriver, navigationMenu);
    }

    @Test(priority = 16, testName = "TC_STP_16", groups = "General")
    @Parameters({"name3", "email3", "nickname3", "password3", "confirmPassword3", "store_manager_role"})
    public void checkIfStaffCanBeCreaWithOutRF(String name, String email, String nickname, String password, String confirmPassword, String role) {
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
        staffPage.clickNewStaffBtn();
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());

        staffPage.creaUpdateStaff(new Staff("", "", "", "", "", ""));
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());

        staffPage.creaUpdateStaff(new Staff(name, "", "", "", "", ""));
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());

        staffPage.creaUpdateStaff(new Staff(name, email, "", "", "", ""));
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());

        staffPage.creaUpdateStaff(new Staff(name, email, nickname, password, "", ""));
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());

        staffPage.creaUpdateStaff(new Staff(name, email, nickname, password, confirmPassword, ""));
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());

    }

    @Test(priority = 17, testName = "TC_STP_22", groups = "General")
    @Parameters({"name3", "email3", "nickname3", "password3", "confirmPassword4", "store_manager_role"})
    public void checkIfStaffCanBeCreaWithDifPassAndCP(String name, String email, String nickname, String password,
                                                      String confirmPassword, String role) {
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());
        staffPage.creaUpdateStaff(new Staff(name, email, nickname, password, confirmPassword, role));
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.addStaffUrl, webDriver.getCurrentUrl());

        if(!webDriver.getCurrentUrl().equals(WebConstants.staffUrl))
            staffPage = accessStaffPage(webDriver, navigationMenu);
        assertThat(staffPage.getAllStaff(), not(hasItem(allOf(
                hasProperty("email", equalTo(email)),
                hasProperty("name", equalTo(name))
        ))));


    }


    @Test(priority = 18, testName = "TC_STP_19", groups = "General")
    @Parameters({"name3", "email3", "nickname3", "password3", "confirmPassword3", "store_manager_role"})
    public void checkIfStaffBeCreaWithRF(String name, String email, String nickname, String password,
                                         String confirmPassword, String role) {
        // TODO uncomment the below line when the TC_STP_22 issue is fixed
        // if(!webDriver.getCurrentUrl().equals(WebConstants.staffUrl))
            staffPage = accessStaffPage(webDriver, navigationMenu);
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));

        assertThat(staffPage.getAllStaff(), hasItem(allOf(
                hasProperty("email", equalTo(email)),
                hasProperty("name", equalTo(name))
        )));
    }


    //@Test(priority = 19, testName = "TC_STP_17", groups = "General")
    @Parameters({"name4", "email3", "nickname4", "password4", "confirmPassword4", "beacon_manager_role"})
    public void checkIfStaffBeCreaWithSameEmail(String name, String email, String nickname, String password,
                                                String confirmPassword, String role) {

        if(!webDriver.getCurrentUrl().equals(WebConstants.staffUrl))
            staffPage = accessStaffPage(webDriver, navigationMenu);

        Staff existingStaff = staffPage.getStaffByEmail(email);

        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));

        allStaff = staffPage.getAllStaff();

        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());

        assertThat(allStaff, hasItem(allOf(
                hasProperty("email", equalTo(email)),
                hasProperty("name", equalTo(existingStaff.getName()))
        )));

        assertThat(allStaff, not(hasItem(allOf(
                hasProperty("email", equalTo(email)),
                hasProperty("name", equalTo(name))
        ))));
    }

    //@Test(priority = 20, testName = "TC_STP_18", groups = "General")
    @Parameters({"email3", "email"})
    public void checkIfStaffBeUpdaWithSameEmail(String userToBeUpdaEmail, String existingEmail) {
        Staff userToBeUpda = staffPage.getStaffByEmail(userToBeUpdaEmail);
        staffPage.clickEditStaffBtn(userToBeUpda.getEditLink());
        assertEquals(userToBeUpda.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        staffPage.enterEmail(existingEmail);
        staffPage.clickCreaUpdateStaffBtn();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());

        assertThat(allStaff, hasItem(allOf(
                hasProperty("email", equalTo(userToBeUpdaEmail)),
                hasProperty("name", equalTo(userToBeUpda.getName()))
        )));

        assertThat(allStaff, not(hasItem(allOf(
                hasProperty("email", equalTo(existingEmail)),
                hasProperty("name", equalTo(userToBeUpda.getName()))
        ))));
    }

    @Test(priority = 21, testName = "TC_STP_21", groups = "General")
    @Parameters({"email3", "password3"})
    public void checkIfDelStaffCanLogin(String email, String password) throws InterruptedException {
        Staff staff = staffPage.getStaffByEmail(email);
        deleteStaff(webDriver, staff, staffPage);
        loginPage = navigationMenu.clickLogoutLink();
        Thread.sleep(WebConstants.waitMilliSeconds);
        assertEquals(WebConstants.baseUrl, webDriver.getCurrentUrl());
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.login();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.loginUrl, webDriver.getCurrentUrl());
    }

    @BeforeGroups("BeaconManager")
    @Parameters({"email", "password", "userNameNew", "userEmailNew", "nicknameNew", "userPasswordNew", "confirmPasswordNew", "beacon_manager_role"})
    public void createBeaconManager(String loginEmail, String loginPassword, String name, String email, String nickname,
                                    String password, String confirmPassword, String role) {
        if (navigationMenu.getLogoutLink() == null)
            navigationMenu = loginToBeaconoid(webDriver, loginEmail, loginPassword);
        staffPage = accessStaffPage(webDriver, navigationMenu);
        createStaff(staffPage, new Staff(name, email, nickname, password, confirmPassword, role));
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 22, testName = "TC_STP_03", groups = "BeaconManager")
    @Parameters({"userEmailNew", "userPasswordNew"})
    public void checkIfStaffLinkIsVisibleForBM(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        assertNull(navigationMenu.getStaffLink());
    }

    @AfterGroups("BeaconManager")
    public void logoutBeaconManager() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeGroups("StoreManager")
    @Parameters({"email", "password", "name3", "email3", "nickname3", "password3", "confirmPassword3", "store_manager_role", "beacon_manager_role"})
    public void createStoreManager(String loginEmail, String loginPassword, String name, String email, String nickname,
                                   String password, String confirmPassword, String storeMRole, String beaconMRole) {
        navigationMenu = loginToBeaconoid(webDriver, loginEmail, loginPassword);
        staffPage = accessStaffPage(webDriver, navigationMenu);
        Staff staff = staffPage.getStaffByRole(beaconMRole);
        updateStaff(staff, new Staff(name, email, nickname, password, confirmPassword, storeMRole));
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 23, testName = "TC_STP_05", groups = "StoreManager")
    @Parameters({"email3", "password3"})
    public void checkIfStaffLinkIsVisibleForSM(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        assertNull(navigationMenu.getStaffLink());
    }

    @AfterGroups("StoreManager")
    public void logoutStoreManager() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeGroups("UserReportManager")
    @Parameters({"email", "password", "name4", "email4", "nickname4", "password4", "confirmPassword4", "user_report_manager_role", "store_manager_role"})
    public void createUserReportManager(String loginEmail, String loginPassword, String name, String email, String nickname,
                                        String password, String confirmPassword, String userRMRole, String storeMRole) {
        navigationMenu = loginToBeaconoid(webDriver, loginEmail, loginPassword);
        staffPage = accessStaffPage(webDriver, navigationMenu);
        Staff staff = staffPage.getStaffByRole(storeMRole);
        updateStaff(staff, new Staff(name, email, nickname, password, confirmPassword, userRMRole));
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 24, testName = "TC_STP_04", groups = "UserReportManager")
    @Parameters({"email4", "password4"})
    public void checkIfStaffLinkIsVisibleForURM(String email, String password) {
        navigationMenu = loginToBeaconoid(webDriver, email, password);
        assertNull(navigationMenu.getStaffLink());
    }

    @AfterGroups("UserReportManager")
    public void logoutUserReportManager() {
        navigationMenu.clickLogoutLink();
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 25, testName = "TC_STP_23", groups="password")
    @Parameters({"email4"})
    public void chkIfPassAndCPassIsBlank(String email){
        if(!webDriver.getCurrentUrl().equals(WebConstants.staffUrl))
            staffPage = accessStaffPage(webDriver, navigationMenu);

        Staff staff = staffPage.getStaffByEmail(email);
        staffPage.clickEditStaffBtn(staff.getEditLink());

        assertEquals("", staffPage.getPasswordText());
        assertEquals("", staffPage.getConfirmPasswordTxt());
    }

    @AfterTest
    @Parameters({"super_admin_role"})
    public void clearTestData(String role){
        if(!webDriver.getCurrentUrl().equals(WebConstants.staffUrl))
            staffPage = accessStaffPage(webDriver, navigationMenu);
        allStaff = staffPage.getAllStaff();
        for(Staff staff: allStaff){
            if(!staff.getRole().equals(role))
                deleteStaff(webDriver, staff, staffPage);
        }

    }

    private void updateStaff(Staff oldStaff, Staff newStaff) {
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
        staffPage.clickEditStaffBtn(oldStaff.getEditLink());
        assertEquals(oldStaff.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        staffPage.creaUpdateStaff(newStaff);
        try {
            Thread.sleep(WebConstants.waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(WebConstants.staffUrl, webDriver.getCurrentUrl());
    }

}

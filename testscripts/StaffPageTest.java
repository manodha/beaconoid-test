package com.company.testscripts;

import com.company.model.Staff;
import com.company.pageobjects.NavigationMenu;
import com.company.pageobjects.StaffPage;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by manodha on 11/5/17.
 */
public class StaffPageTest extends FunctionalTest {

    private String addStaffUrl = baseUrl + "staffs/new";
    private NavigationMenu navigationMenu;
    private StaffPage staffPage;
    private List<Staff> allStaff;


    @BeforeTest
    @Parameters({"email", "password"})
    public void accessStaffPage(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        staffPage = accessStaffPage(navigationMenu);

        staffPage.printAllStaff(staffPage.getAllStaff());
    }

    @Test(priority = 1)
    @Parameters({"userName", "userEmail", "nickname", "userPassword", "confirmPassword", "store_manager_role"})
    public void createStaffTC(String name, String email, String nickname, String password, String confirmPassword, String role) {
        createStaff(new Staff(name, email, nickname, password, confirmPassword, role));
    }

    @Test(priority = 2)
    @Parameters({"userEmail", "userNameNew", "userEmailNew", "nicknameNew", "userPasswordNew", "confirmPasswordNew", "beacon_manager_role"})
    public void updateStaffTC(String userEmail, String name, String userEmailNew, String nickname, String password,
                              String confirmPassword, String role) {
        Staff staff = staffPage.getStaff(userEmail);
        updateStaff(staff, new Staff(name, userEmailNew, nickname, password, confirmPassword, role));

    }

    @Test(priority = 3)
    @Parameters({"userEmailNew"})
    public void deleteStaffTC(String userEmailNew) {
        Staff staff = staffPage.getStaff(userEmailNew);
        deleteStaff(staff);
    }


    private void createStaff(Staff staff) {
        assertEquals(staffUrl, webDriver.getCurrentUrl());
        staffPage.clickNewStaffBtn();
        assertEquals(addStaffUrl, webDriver.getCurrentUrl());
        staffPage.creaUpdateStaff(staff);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(staffUrl, webDriver.getCurrentUrl());
    }

    private void updateStaff(Staff oldStaff, Staff newStaff) {
        assertEquals(staffUrl, webDriver.getCurrentUrl());
        staffPage.clickEditStaffBtn(oldStaff.getEditLink());
        assertEquals(oldStaff.getEditLink().getAttribute("href"), webDriver.getCurrentUrl());
        staffPage.creaUpdateStaff(newStaff);
        try {
            Thread.sleep(waitMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(staffUrl, webDriver.getCurrentUrl());
    }

    private void deleteStaff(Staff staff) {
        assertEquals(staffUrl, webDriver.getCurrentUrl());
        staffPage.clickDeleteStaffBtn(staff.getDeleteBtn());
        try {
            Thread.sleep(waitMilliSeconds);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(staffUrl, webDriver.getCurrentUrl());
    }
}

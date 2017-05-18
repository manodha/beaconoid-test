package com.company.testscripts;

import com.company.pageobjects.NavigationMenu;
import com.company.pageobjects.StaffPage;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

/**
 * Created by manodha on 17/5/17.
 */
public class StaffRolesTest extends FunctionalTest {

    private NavigationMenu navigationMenu;
    private StaffPage staffPage;

    @BeforeTest
    @Parameters({"email", "password"})
    public void createUsers(String email, String password) {
        navigationMenu = loginToBeaconoid(email, password);
        staffPage = accessStaffPage(navigationMenu);
    }

}

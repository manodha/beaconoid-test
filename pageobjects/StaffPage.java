package com.company.pageobjects;

import com.company.model.Staff;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manodha on 11/5/17.
 */
public class StaffPage extends PageObject {

    private WebDriver webDriver;

    @FindBy(css = "table.table-bordered.table-striped > tbody")
    private WebElement staffsTable;

    @FindBy(xpath = "//a[@href='/staffs/new']")
    private WebElement newStaffBtn;

    @FindBy(id = "user_name")
    private WebElement nameTxt;

    @FindBy(id = "user_email")
    private WebElement emailTxt;

    @FindBy(id = "user_nickname")
    private WebElement nicknameTxt;

    @FindBy(id = "user_password")
    private WebElement passwordTxt;

    @FindBy(id = "user_password_confirmation")
    private WebElement confirmPasswordTxt;

    @FindBy(id = "user_role")
    private WebElement roleDropDown;

    @FindBy(name = "commit")
    private WebElement creaUpdateStaffBtn;

    public StaffPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public void enterName(String name) {
        nameTxt.clear();
        nameTxt.sendKeys(name);
    }

    public void enterEmail(String email) {
        emailTxt.clear();
        emailTxt.sendKeys(email);
    }

    public void enterNickname(String nickname) {
        nicknameTxt.clear();
        nicknameTxt.sendKeys(nickname);
    }

    public void enterPassword(String password) {
        passwordTxt.clear();
        passwordTxt.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        confirmPasswordTxt.clear();
        confirmPasswordTxt.sendKeys(confirmPassword);
    }

    public void selectRoleByName(String role) {
        Select roleDropdown = new Select(roleDropDown);
        roleDropdown.selectByVisibleText(role);
    }

    public void selectRoleByIndex(int index) {
        Select roleDropdown = new Select(roleDropDown);
        roleDropdown.selectByIndex(index);
    }

    public void clickNewStaffBtn() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", newStaffBtn);
    }

    public void clickCreaUpdateStaffBtn() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", creaUpdateStaffBtn);
    }

    public void clickEditStaffBtn(WebElement editBtn) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editBtn);

    }

    public void clickDeleteStaffBtn(WebElement deleteBtn) {
        deleteBtn.click();
        webDriver.switchTo().alert().accept();
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffs = new ArrayList<>();
        List<WebElement> staffRows = staffsTable.findElements(By.tagName("tr"));
        int numRows, numColumns;
        numRows = staffRows.size();
        numColumns = staffRows.get(0).findElements(By.tagName("td")).size();

        if (numColumns <= 1)
            return staffs;

        for (int i = 0; i < numRows; i++) {
            Staff staff = new Staff();
            staff.setId(staffRows.get(i).findElement(By.xpath("td[1]")).getText());
            staff.setName(staffRows.get(i).findElement(By.xpath("td[2]")).getText());
            staff.setEmail(staffRows.get(i).findElement(By.xpath("td[3]")).getText());
            staff.setRole(staffRows.get(i).findElement(By.xpath("td[4]")).getText());
            staff.setEditLink(staffRows.get(i).findElement(By.xpath("td[5]/a")));
            staff.setDeleteBtn(staffRows.get(i).findElement(By.xpath("td[6]/form/input[@value='Delete']")));
            staffs.add(staff);
        }

        return staffs;
    }

    public Staff getStaffByEmail(String email) {
        List<Staff> staffs = getAllStaff();
        for (Staff staff : staffs) {
            if (staff.getEmail().equals(email)) {
                return staff;
            }
        }
        return null;
    }

    public Staff getStaffByRole(String role) {
        List<Staff> staffs = getAllStaff();
        for (Staff staff : staffs) {
            if (staff.getRole().equals(role)) {
                return staff;
            }
        }
        return null;
    }

    public void printAllStaff(List<Staff> staffs) {
        System.out.println("************** Staffs ****************");
        for (Staff staff : staffs) {
            printStaff(staff);
        }
    }

    public void printStaff(Staff staff) {
        System.out.print("id - " + staff.getId());
        System.out.print(" name - " + staff.getName());
        System.out.print(" email - " + staff.getEmail());
        System.out.print(" nickname - " + staff.getNickname());
        System.out.print(" password - " + staff.getPassword());
        System.out.print(" confirmPassword - " + staff.getConfirmPassword());
        System.out.println(" role - " + staff.getRole());
    }

    public void creaUpdateStaff(Staff staff) {
        enterName(staff.getName());
        enterEmail(staff.getEmail());
        enterNickname(staff.getNickname());
        enterPassword(staff.getPassword());
        enterConfirmPassword(staff.getConfirmPassword());
        selectRoleByName(staff.getRole());
        clickCreaUpdateStaffBtn();
    }

}

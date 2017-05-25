package com.company.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by manodha on 27/3/17.
 */
public class LoginPage extends PageObject {

    private WebDriver webDriver;

    /* TestNG references for the Web Elements in the Login Page */

    @FindBy(tagName = "h3")
    private WebElement titleH3;

    @FindBy(id = "user_email")
    private WebElement emailTextField;

    @FindBy(id = "user_password")
    private WebElement passwordTextField;

    @FindBy(name = "commit")
    private WebElement loginButton;


    public LoginPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }


    /* Methods to handle Web Elements in the Login Page */
    public void enterEmail(String email) {
        this.emailTextField.clear();
        this.emailTextField.sendKeys(email);
    }

    public void enterPassword(String password) {
        this.passwordTextField.clear();
        this.passwordTextField.sendKeys(password);
    }

    public NavigationMenu login() {
        loginButton.click();
        return new NavigationMenu(webDriver);
    }


    // Method to check if the Password is masked
    public Boolean isPassword() {
        return passwordTextField.getAttribute("type").equals("password");
    }

    // Method to get the Title of the Login Page
    public String getTitleText() {
        return titleH3.getText();
    }

}

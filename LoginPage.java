package com.company;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by manodha on 27/3/17.
 */
public class LoginPage extends PageObject {

    @FindBy(id = "user_email")
    private WebElement emailTextField;

    @FindBy(id = "user_password")
    private WebElement passwordTextField;

    @FindBy(name = "commit")
    private WebElement loginButton;


    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void enterEmail(String email) {
        this.emailTextField.clear();
        this.emailTextField.sendKeys(email);

    }

    public void enterPassword(String password) {
        this.passwordTextField.clear();
        this.passwordTextField.sendKeys(password);
    }

    public Boolean isPassword() {
        return passwordTextField.getAttribute("type").equals("password");
    }

    public NavigationMenu login() {
        loginButton.click();
        return new NavigationMenu(FunctionalTest.webDriver);
    }

}

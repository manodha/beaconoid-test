package com.company.model;

import org.openqa.selenium.WebElement;

/**
 * Created by manodha on 11/5/17.
 */
public class Staff {
    private String id;
    private String name;
    private String email;
    private String nickname;
    private String password;
    private String confirmPassword;
    private String role;
    private WebElement editLink;
    private WebElement deleteBtn;

    public Staff() {
        this.id = "";
        this.name = "";
        this.email = "";
        this.nickname = "";
        this.password = "";
        this.confirmPassword = "";
        this.role = "";
        this.editLink = null;
        this.deleteBtn = null;
    }

    public Staff(String name, String email, String nickname, String password, String confirmPassword, String role) {
        this.id = "";
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.editLink = null;
        this.deleteBtn = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public WebElement getEditLink() {
        return editLink;
    }

    public void setEditLink(WebElement editLink) {
        this.editLink = editLink;
    }

    public WebElement getDeleteBtn() {
        return deleteBtn;
    }

    public void setDeleteBtn(WebElement deleteBtn) {
        this.deleteBtn = deleteBtn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

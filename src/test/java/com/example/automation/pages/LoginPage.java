package com.example.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl);
        visible(usernameInput);
        return this;
    }

    public void login(String username, String password) {
        visible(usernameInput).clear();
        visible(usernameInput).sendKeys(username);
        visible(passwordInput).clear();
        visible(passwordInput).sendKeys(password);
        click(loginButton);
    }

    public String getErrorMessage() {
        return text(errorMessage);
    }
}

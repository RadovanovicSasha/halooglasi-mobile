package com.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    // ===== LOKATORI =====
    private static final By emailField = By.xpath("//android.widget.EditText[@resource-id='login_email_text_field']//android.widget.EditText");
    private static final By passwordField = By.xpath("//android.widget.EditText[@resource-id='login_password_text_field']//android.widget.EditText");
    private static final By loginButton = By.xpath("//android.widget.Button[@content-desc='Uloguj me']");

    // ===== EMAIL =====
    public void enterEmail(String email) {
        type(emailField, email);
    }

    // ===== PASSWORD =====
    public void enterPassword(String password) {
        type(passwordField, password);
    }

    // ===== LOGIN =====
    public void clickLogin() {
        click(loginButton);
    }

    // ===== FLOW =====
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    // ===== WAIT POSLE LOGIN =====
    public void waitForLoginSuccess() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(emailField));
    }
}

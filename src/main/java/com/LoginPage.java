package com;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    // ===== LOKATORI =====
    private By emailField = By.xpath("//android.widget.EditText[@resource-id='login_email_text_field']//android.widget.EditText");
    private By passwordField = By.xpath("//android.widget.EditText[@resource-id='login_password_text_field']//android.widget.EditText");
    private By loginButton = By.xpath("//android.widget.Button[@content-desc='Uloguj me']");

    // ===== EMAIL =====
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));

        WebElement emailInput = driver.findElement(emailField);
        emailInput.click();
        emailInput.clear();
        emailInput.sendKeys(email);

        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {}
    }

    // ===== PASSWORD =====
    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

        WebElement passInput = driver.findElement(passwordField);
        passInput.click();
        passInput.clear();
        passInput.sendKeys(password);

        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {}
    }

    // ===== LOGIN =====
    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        driver.findElement(loginButton).click();
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
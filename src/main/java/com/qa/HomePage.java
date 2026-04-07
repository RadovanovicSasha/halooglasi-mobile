package com.qa;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    // ===== LOKATORI =====

    // search field (već imaš)
    private By searchButton = By.xpath("//android.widget.Button[@resource-id='home_search_text_field']");

    // profil tab (za assert login-a)
    private By profileTab = By.xpath("//android.widget.Button[@resource-id='profile_tab']");


    // ===== METODE =====

    // provera da li je homepage učitan
    public boolean isSearchFieldVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // KLJUČNO — provera da li je user ulogovan
    public boolean isUserLoggedIn() {
        return driver.findElements(profileTab).size() > 0;
    }

    public void openSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        driver.findElement(searchButton).click();
    }
}
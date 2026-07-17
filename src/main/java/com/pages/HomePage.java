package com.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    // ===== LOKATORI =====

    // search field (već imaš)
    private static final By searchButton = By.xpath("//android.widget.Button[@resource-id='home_search_text_field']");

    // profil tab (za assert login-a)
    private static final By profileTab = By.xpath("//android.widget.Button[@resource-id='profile_tab']");


    // ===== METODE =====

    // provera da li je homepage učitan
    public boolean isSearchFieldVisible() {
        return isVisible(searchButton);
    }

    // KLJUČNO — provera da li je user ulogovan
    public boolean isUserLoggedIn() {
        return driver.findElements(profileTab).size() > 0;
    }

    public void openSearch() {
        click(searchButton);
    }
}

package com.qa;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    private By searchField = By.xpath("//android.widget.EditText");

    public boolean isSearchFieldVisible() {
        return driver.findElement(searchField).isDisplayed();
    }
}
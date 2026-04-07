package com.qa;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class SearchPage extends BasePage {

    public SearchPage(AndroidDriver driver) {
        super(driver);
    }

    // ===== LOKATOR =====

    // pravi input za unos teksta (nakon što se klikne search na HomePage)
    private By searchInput = By.className("android.widget.EditText");

    private By results = By.xpath("//android.widget.ImageView");

    // ===== METODE =====

    // unos teksta u search polje
    public void enterSearch(String text) {
        driver.findElement(searchInput).sendKeys(text);
    }

    // simulacija ENTER dugmeta (pokreće pretragu)
    public void submitSearch() {
        driver.pressKey(new io.appium.java_client.android.nativekey.KeyEvent(
                io.appium.java_client.android.nativekey.AndroidKey.ENTER));
    }
    public boolean areResultsDisplayed() {
        try {
            wait.until(driver -> driver.findElements(results).size() > 5);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
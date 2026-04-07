package com;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class OnboardingPage extends BasePage {

    public OnboardingPage(AndroidDriver driver) {
        super(driver);
    }

    // locatori
    private By skipButton = By.xpath("//android.widget.Button[@content-desc='Preskoči']");
    private By notNowButton = By.xpath("//android.widget.Button[@content-desc='Ne sada']");

    // glavna metoda
    public void completeOnboarding() {

        // klik na "Preskoči" ako postoji
        List<WebElement> skip = driver.findElements(skipButton);
        if (!skip.isEmpty()) {
            skip.get(0).click();
        }

        // klik na "Ne sada" ako postoji (notifikacije)
        List<WebElement> notNow = driver.findElements(notNowButton);
        if (!notNow.isEmpty()) {
            notNow.get(0).click();
        }
    }
}
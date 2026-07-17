package com.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class OnboardingPage extends BasePage {

    public OnboardingPage(AndroidDriver driver) {
        super(driver);
    }

    // locatori
    private static final By skipButton = By.xpath("//android.widget.Button[@content-desc='Preskoči']");
    private static final By notNowButton = By.xpath("//android.widget.Button[@content-desc='Ne sada']");

    // glavna metoda
    public void completeOnboarding() {
        // klik na "Preskoči" ako postoji
        clickIfPresent(skipButton);

        // klik na "Ne sada" ako postoji (notifikacije)
        clickIfPresent(notNowButton);
    }
}

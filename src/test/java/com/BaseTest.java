package com;

import com.config.Config;
import com.pages.HomePage;
import com.pages.LoginPage;
import com.pages.OnboardingPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.URL;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeEach
    public void setUp() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(Config.PLATFORM_NAME)
                .setDeviceName(Config.DEVICE_NAME)
                .setAppPackage(Config.APP_PACKAGE)
                .setAppActivity(Config.APP_ACTIVITY)
                .setAutomationName(Config.AUTOMATION_NAME);

        driver = new AndroidDriver(new URL(Config.APPIUM_SERVER_URL), options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected HomePage loginAsValidUser() {
        OnboardingPage onboardingPage = new OnboardingPage(driver);
        onboardingPage.completeOnboarding();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(Config.EMAIL, Config.PASSWORD);
        loginPage.waitForLoginSuccess();

        return new HomePage(driver);
    }
}

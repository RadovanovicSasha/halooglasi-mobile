package com;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeEach
    public void setUp() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Device");

        // 👇 BITNO — koristi tvoj app package/activity iz Appium Inspector-a
        caps.setCapability("appPackage", "com.halooglasi.android");
        caps.setCapability("appActivity", "com.halooglasi.android.MainActivity");

        caps.setCapability("automationName", "UiAutomator2");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
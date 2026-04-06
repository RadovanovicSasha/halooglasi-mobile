package com.qa;

import io.appium.java_client.android.AndroidDriver;

public class BasePage {

    protected AndroidDriver driver;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
    }
}
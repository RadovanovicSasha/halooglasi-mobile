package com.config;

public class Config {
    public static final String EMAIL = System.getenv("APP_EMAIL");
    public static final String PASSWORD = System.getenv("APP_PASSWORD");

    public static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";

    public static final String PLATFORM_NAME = "Android";
    public static final String DEVICE_NAME = "Android Device";

    // 👇 BITNO — koristi tvoj app package/activity iz Appium Inspector-a
    public static final String APP_PACKAGE = "com.halooglasi.android";
    public static final String APP_ACTIVITY = "com.halooglasi.android.MainActivity";

    public static final String AUTOMATION_NAME = "UiAutomator2";
}
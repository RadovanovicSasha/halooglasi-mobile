package com.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;

public class BasePage {

    private static final String SCREENSHOTS_DIR = "screenshots";

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ===== SHARED INTERACTIONS =====

    protected void waitVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected boolean isVisible(By locator) {
        try {
            waitVisible(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        waitVisible(locator);

        WebElement element = driver.findElement(locator);
        element.click();
        element.clear();
        element.sendKeys(text);

        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {}
    }

    protected void clickIfPresent(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        if (!elements.isEmpty()) {
            elements.get(0).click();
        }
    }

    // ===== SCREENSHOT SUPPORT =====

    protected String takeScreenshot(String screenshotName) {
        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalStateException("Current driver does not support capturing screenshots.");
        }

        try {
            File capturedFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Path screenshotsDir = Paths.get(SCREENSHOTS_DIR);
            Files.createDirectories(screenshotsDir);

            Path targetPath = screenshotsDir.resolve(screenshotName + ".png");
            Files.copy(capturedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return targetPath.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to capture screenshot '" + screenshotName + "': " + e.getMessage(), e);
        }
    }
}

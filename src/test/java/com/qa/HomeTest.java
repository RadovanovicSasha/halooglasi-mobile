package com.qa;

import com.qa.utils.Config;
import org.junit.jupiter.api.Test;

public class HomeTest extends BaseTest {

    @Test
    public void testAppFlow() {

        // ===== ONBOARDING =====
        OnboardingPage onboardingPage = new OnboardingPage(driver);
        onboardingPage.completeOnboarding();

        // ===== LOGIN =====
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(Config.EMAIL, Config.PASSWORD);

        // ===== VALIDACIJA LOGIN-A (KLJUČNO) =====
        loginPage.waitForLoginSuccess();

        // ===== DEBUG =====
        System.out.println("Login SUCCESS");
    }
}
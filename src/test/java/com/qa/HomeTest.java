package com.qa;

import com.qa.utils.Config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        // inicijalizacija homepage-a
        HomePage homePage = new HomePage(driver);

        // ASSERT da je homepage učitan
        assertTrue(homePage.isSearchFieldVisible());

        // ASSERT da je user ulogovan
        assertTrue(homePage.isUserLoggedIn());
    }
}
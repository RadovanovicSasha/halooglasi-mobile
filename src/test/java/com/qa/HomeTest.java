package com.qa;

import org.junit.jupiter.api.Test;

public class HomeTest extends BaseTest {

    @Test
    public void testAppFlow() {

        // onboarding
        OnboardingPage onboardingPage = new OnboardingPage(driver);
        onboardingPage.completeOnboarding();

        // ovde ćemo kasnije login
        System.out.println("Reached login screen");
    }
}
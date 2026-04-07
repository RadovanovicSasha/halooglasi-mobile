package com;

import com.Config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends BaseTest {

    @Test
    public void testSearchFlow() {

        // ===== ONBOARDING =====
        // prolaz kroz početne ekrane aplikacije
        OnboardingPage onboardingPage = new OnboardingPage(driver);
        onboardingPage.completeOnboarding();

        // ===== LOGIN =====
        // unos kredencijala iz Config klase
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(Config.EMAIL, Config.PASSWORD);

        // čekamo da login ekran nestane (znak da je login uspeo)
        loginPage.waitForLoginSuccess();

        // ===== HOME =====
        HomePage homePage = new HomePage(driver);

        // provera da je homepage učitan
        assertTrue(homePage.isSearchFieldVisible());

        // ===== SEARCH =====
        // klik na search (prelazak na novi ekran)
        homePage.openSearch();

        // radimo na search stranici
        SearchPage searchPage = new SearchPage(driver);

        // unos teksta
        searchPage.enterSearch("moto oprema");

        // pokretanje pretrage
        searchPage.submitSearch();

        // trenutno samo potvrđujemo da je flow izvršen
        System.out.println("Search executed");
        assertTrue(searchPage.areResultsDisplayed());
    }
}
package com;

import com.pages.HomePage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeTest extends BaseTest {

    @Test
    public void loginWithValidCredentialsShowsHomepage() {
        HomePage homePage = loginAsValidUser();

        assertTrue(homePage.isSearchFieldVisible());
        assertTrue(homePage.isUserLoggedIn());
    }
}

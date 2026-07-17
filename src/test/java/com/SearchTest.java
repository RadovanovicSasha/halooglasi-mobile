package com;

import com.pages.HomePage;
import com.pages.SearchPage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends BaseTest {

    @Test
    public void searchingAfterLoginDisplaysResults() {
        HomePage homePage = loginAsValidUser();
        assertTrue(homePage.isSearchFieldVisible());

        homePage.openSearch();

        SearchPage searchPage = new SearchPage(driver);
        searchPage.enterSearch("moto oprema");
        searchPage.submitSearch();

        assertTrue(searchPage.areResultsDisplayed());
    }
}

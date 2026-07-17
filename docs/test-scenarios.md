# Mobile Automation Test Scenarios (Appium)

## Project Overview

This project demonstrates a mobile test automation framework built using Appium, Java, and the Page Object Model (POM) design pattern.

The framework covers core user flows and is designed to be scalable, maintainable, and aligned with real-world QA practices.

---

## Scenario 1: User Login

**Automated by:** `HomeTest.loginWithValidCredentialsShowsHomepage`

### Objective

Verify that a user can successfully log into the application using valid credentials.

### Steps

1. Launch the application
2. Complete onboarding screens
3. Navigate to login screen
4. Enter valid email and password
5. Submit login form

### Expected Result

- User is successfully logged in
- Homepage is displayed
- Profile tab is visible

---

## Scenario 2: Search Functionality

**Automated by:** `SearchTest.searchingAfterLoginDisplaysResults`

### Objective

Verify that the search functionality returns relevant results.

### Steps

1. Ensure user is logged in
2. Click on search field
3. Enter search query (e.g. "moto oprema")
4. Submit search

### Expected Result

- Search results are displayed
- List of items is visible on the screen

---

## Technical Notes

- Framework uses explicit waits for synchronization
- Credentials are managed via environment variables
- Page Object Model is used for maintainability
- Tests are structured using JUnit and Maven

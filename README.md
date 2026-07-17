# 📱 Mobile Automation Framework (Appium)

![CI](https://github.com/RadovanovicSasha/halooglasi-mobile/actions/workflows/ci.yml/badge.svg)

## 🔧 Tech Stack
- Java 17
- Appium (java-client 9.2.2, UiAutomator2)
- Selenium
- Maven
- JUnit 5

## 📋 Project Description
This project is a mobile test automation framework built using Appium and Java.

It follows the Page Object Model (POM) design pattern and includes automated test scenarios for core application flows.

## 🗂️ Project Structure
```
src/
├── main/java/com/
│   ├── config/          # Config.java — server URL, device/app capabilities, credentials
│   └── pages/            # Page Objects (BasePage, HomePage, LoginPage, OnboardingPage, SearchPage)
└── test/java/com/
    ├── BaseTest.java      # driver lifecycle + shared login/onboarding helper
    ├── HomeTest.java
    └── SearchTest.java
```

## ✅ Test Scenarios
- `HomeTest.loginWithValidCredentialsShowsHomepage` — login flow (with validation)
- `SearchTest.searchingAfterLoginDisplaysResults` — search flow (enter text and verify results)

Full scenario documentation: [`docs/test-scenarios.md`](docs/test-scenarios.md)

## 🔐 Credentials & Configuration
Login credentials are not hardcoded. They are read from environment variables:

- `APP_EMAIL`
- `APP_PASSWORD`

The Appium server URL, device name, and app package/activity are defined as constants in `src/main/java/com/config/Config.java`, currently fixed for a local `UiAutomator2` / Android setup at `http://127.0.0.1:4723`.

## ▶️ How to Run
1. Set the `APP_EMAIL` and `APP_PASSWORD` environment variables.
2. Start an Appium server on `http://127.0.0.1:4723`.
3. Connect an Android device or start an emulator matching the package/activity configured in `Config.java`.
4. Run the tests via your IDE or with `mvn test`.

## 🤖 Continuous Integration
The GitHub Actions pipeline (`.github/workflows/ci.yml`) currently validates that the project **builds and compiles** on every push/PR. It does not execute the Appium tests, since no Android emulator or Appium server is provisioned in the CI runner.

## 📌 Notes
- Framework uses explicit waits for stability
- Designed for scalability and maintainability

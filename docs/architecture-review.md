# Architecture Review — halooglasi-mobile

**Applicable Procedure:** `03_procedures/03_Architecture_Assessment.md` (AI Operating System)
**Reviewed by:** AI Operating System (analysis only — no repository files modified)
**Date:** 2026-07-17

---

## 1. Executive Assessment

`halooglasi-mobile` is a small, functional, single-developer Appium/Java mobile automation portfolio project covering two flows (login, search). It is reasonably clean for its size, has no hardcoded credentials, and has a working CI build. However, it does **not** conform to the target architecture defined in `framework_architecture_standards.md` (flat package instead of layered `config/pages/tests/testdata`), contains leftover debug artifacts that violate the Playbook's "Investigation Code" rule, and has real duplication (login+onboarding steps repeated verbatim across test classes; no shared BasePage interaction helpers).

More importantly: the AI Operating System's own `CLAUDE.md` explicitly states mobile automation is **not currently in scope** for this mission and that the mobile agent (`amoby_kennoby.md`) is **registered but inactive**. Conducting this review is itself outside the stated current mission until the user explicitly brings mobile into scope. This is flagged in Section 9 as the first decision needed — not a blocker to reporting, but a governance gap that should be resolved before any implementation phase begins.

## 2. Current Project Architecture

```
halooglasi-mobile/
├── .github/workflows/ci.yml        # Maven build only, tests skipped
├── .idea/                          # committed shared IDE config
├── pom.xml                         # Java 17, Selenium 4.21, Appium java-client 9.2.2, JUnit 5.10.2
├── src/main/java/com/
│   ├── BasePage.java                # driver + WebDriverWait only
│   ├── Config.java                  # EMAIL/PASSWORD from env vars
│   ├── HomePage.java
│   ├── LoginPage.java
│   ├── OnboardingPage.java
│   └── SearchPage.java
├── src/test/java/com/
│   ├── BaseTest.java                 # session setup/teardown, hardcoded caps + server URL
│   ├── HomeTest.java
│   └── SearchTest.java
├── Test_Scenarios.docx              # 2 documented scenarios (login, search)
└── README.md
```

**Layers present:** a single flat `com` package holding both "config," "page," and "base" responsibilities together — no `config/`, `pages/`, or `testdata/` sub-packages exist. Tests are similarly flat, not split into `smoke/regression/e2e`.

**Dependency flow:** `Test → Page → BasePage → AndroidDriver`. Assertions correctly live only in tests; page objects return booleans rather than asserting — this part *is* compliant.

**Tooling:** JUnit 5, Selenium 4.21 (transitively), Appium java-client 9.2.2, Maven Surefire. No Allure/reporting plugin, no screenshot-on-failure mechanism, no driver-factory abstraction — Appium server URL and capabilities are hardcoded directly in `BaseTest`.

**CI:** GitHub Actions builds on JDK 21 (pom targets Java 17 — a version mismatch, not currently breaking but worth aligning) and explicitly skips test execution (`-DskipTests`), so CI validates compilation only, never a real Appium run.

## 3. Relevant System Rules and Mobile-Agent Responsibilities

Reviewed in full: `CLAUDE.md`, `Collaboration_Guidelines.md`, `Problem_Solving_Framework.md`, `core_rules.md` (Playbook), `agent_manual.md`, `framework_architecture_standards.md`, all seven `03_procedures/*`, all six `04_agents/*`, `01_architecture.md` report template, `CHANGELOG.md`.

**Governing rules that apply here:**
- **AP-002/003** — architecture before implementation, framework before tests.
- **AP-005/006** — no duplication, single responsibility per layer.
- **AP-007/008** — business logic in tests, UI logic in Page Objects (currently respected).
- **Target architecture** (`framework_architecture_standards.md`) mandates `config/`, `pages/`, `tests/`, `testdata/` as required early layers, `BasePage` owning common interactions + screenshot-on-failure, locators as class-level constants, and a driver-factory-style abstraction instead of raw hardcoded driver instantiation (explicitly required for Selenium; the same DRY/config-layer principle applies to Appium even though no Appium-specific tooling subsection exists yet — see Section 9).
- **Investigation Code rule** (Playbook, Production Safety Rules) — temporary investigation code/debug artifacts must not remain after root cause is found. Directly violated by leftover debug comments and `System.out.println` calls (Section 5).
- **State Restoration rule** — tests that log in should restore state (log out) when technically possible. Currently not done.
- **Workflow discipline** (`03_procedures/*`) — Discovery → Architecture Assessment → Plan → Approval → Implementation → Self-Review → Independent Review → Final Verdict. This review corresponds to steps 1–2 only (Discovery/Assessment), matching the "analysis only" instruction it was produced under.

**Mobile agent — `amoby_kennoby.md` (Senior Mobile QA Automation Engineer):** responsible for Appium development, Android/iOS support, reusable mobile components, and mobile test stability, following project conventions. Explicitly out of scope for Amoby: web automation, architecture redesign, approving changes, modifying unrelated files. Escalates to `archie_core.md` for architecture decisions. This maps directly onto `halooglasi-mobile`: Amoby is the natural agent to implement any approved fixes to `BasePage`, page objects, and driver setup, but **cannot** decide the target package layout — that decision belongs to `archie_core.md` (architecture) with final user approval, per the Playbook's approval rule and Amoby's own "do not redesign architecture" constraint.

**Critical scoping issue:** `CLAUDE.md` lists mobile automation execution as "not currently prioritized" and Amoby as "inactive... only activated when mobile automation becomes part of the project scope." See Section 9.

## 4. What Is Already Good (Preserve As-Is)

- **No hardcoded credentials** — `Config.EMAIL`/`Config.PASSWORD` pulled from environment variables, exactly matching the Playbook's Security Guidelines. Don't touch this pattern.
- **Assertions correctly isolated to the test layer** — page objects (`HomePage.isUserLoggedIn()`, `isSearchFieldVisible()`, `SearchPage.areResultsDisplayed()`) return booleans; `assertTrue()` only appears in test classes. This is textbook POM compliance, already matching AP-007/AP-008.
- **Explicit waits used throughout**, no `Thread.sleep()` anywhere in the codebase — matches the explicit-wait requirement in the standards.
- **Test isolation at the session level** — `@BeforeEach`/`@AfterEach` create and quit a fresh `AndroidDriver` per test, so tests don't share app/session state with each other.
- **CI exists and validates the build compiles** on every push/PR — a real, working quality gate, appropriate for a portfolio-scale project.
- **README and Test Scenarios doc accurately describe the actual implemented flows** — no aspirational/misleading documentation drift.
- **Page Object Model boundary is respected** — no Selenium calls or locators leak into test classes.

## 5. Problems and Risks

| # | Finding | Evidence | Impact |
|---|---|---|---|
| 5.1 | Flat package structure, no `config/pages/tests/testdata` layering | All classes in single `com` package | Violates the mandatory target architecture; will not scale past a handful of pages |
| 5.2 | Debug artifacts left in source (Investigation Code rule violation) | `BasePage.java:10-14` comments `// 🔥 OVO JE FALILO`, `// 🔥 INIT`; `System.out.println("Login SUCCESS")` / `"Search executed"` in tests | Unprofessional, clutters diffs, explicitly forbidden by Playbook once root cause is resolved |
| 5.3 | Duplicated onboarding+login flow across `HomeTest` and `SearchTest` | Both tests repeat identical `OnboardingPage`→`LoginPage`→`waitForLoginSuccess` sequence | DRY violation (AP-005); a locator change requires editing every test |
| 5.4 | `BasePage` doesn't provide shared interactions or screenshot-on-failure | `BasePage.java` — only holds `driver`/`wait` | Standard explicitly requires this; each page reimplements its own wait/click boilerplate |
| 5.5 | Locators are instance fields, not class-level constants | e.g. `private By searchButton = ...` (not `static final`) | Minor but explicit standards violation |
| 5.6 | Driver/session config hardcoded in `BaseTest`, not in `Config` | Appium URL `"http://127.0.0.1:4723"`, `appPackage`, `appActivity`, `deviceName` all literal in `BaseTest.setUp()` | Config layer is supposed to own base URL/environment/timeouts; currently split across two files inconsistently |
| 5.7 | `DesiredCapabilities` + generic `AndroidDriver` constructor instead of `UiAutomator2Options` | `BaseTest.java` | Appium java-client 9.x favors platform-specific Options classes; current approach is the older, less type-safe pattern |
| 5.8 | CI skips test execution entirely | `ci.yml`: `mvn clean install -DskipTests`; commit `84db460 "Skip tests in CI."` | CI only proves the code compiles, never that the framework actually runs — reasonable given no emulator/Appium server in the runner (see Section 6), but should be explicitly documented as a known limitation rather than silent |
| 5.9 | JDK version mismatch | `pom.xml` targets Java 17; `ci.yml` sets up JDK 21 | Not currently breaking, but inconsistent and worth aligning or documenting |
| 5.10 | No state restoration after login | Tests log in but never log out; only `driver.quit()` | Minor violation of the Playbook's State Restoration rule; low real-world risk on a disposable emulator/test account, but worth a decision |
| 5.11 | Magic number in `SearchPage.areResultsDisplayed()` | `size() > 5` | Unexplained threshold; fragile and unclear intent |
| 5.12 | Test method names are non-descriptive | `testAppFlow()`, `testSearchFlow()` | Standards require behavior-describing names (e.g. `loginWithValidCredentialsShowsHomepage`) |
| 5.13 | Test scenario documentation stored as binary `.docx` | `Test_Scenarios.docx` | Not diffable/version-friendly; inconsistent with the rest of the system's markdown-first documentation culture |

## 6. Recommended Improvements

Ordered roughly by value, all requiring approval before implementation:

1. **Reorganize into `config/`, `pages/`, `tests/`, `testdata/` packages** per the target architecture — physical move only, no behavior change.
2. **Move all driver/session configuration into `Config`** (server URL, app package/activity, device name, automationName, timeouts), leaving `BaseTest` to just consume `Config`.
3. **Extract shared common interactions + screenshot-on-failure into `BasePage`** (e.g. `click(By)`, `type(By, String)`, `waitVisible(By)`), then refactor page objects to use them — removes waiting-pattern duplication.
4. **Extract the repeated onboarding+login sequence** into a single reusable method (e.g. `BaseTest.loginAsValidUser()` or a `LoginPage.loginFlow()` helper) used by both `HomeTest` and `SearchTest`.
5. **Remove debug artifacts**: the `🔥` comments and `System.out.println` debug statements.
6. **Convert locators to `private static final By`** class-level constants.
7. **Migrate from `DesiredCapabilities` to `UiAutomator2Options`**, matching current Appium java-client 9.x conventions.
8. **Rename test methods to describe business behavior** (e.g. `loginWithValidCredentialsRedirectsToHomepage`).
9. **Move `Test_Scenarios.docx` content into a markdown file** (e.g. `docs/test-scenarios.md`) for diffability — content itself doesn't need to change.
10. **Align JDK version between `pom.xml` and `ci.yml`**, or document the intentional difference.
11. **Document/acknowledge the CI test-skip as an intentional, explained limitation** in the README (e.g. "CI validates build only; full Appium runs require a local emulator + Appium server").

## 7. Improvements That Would Be Over-Engineering

Explicitly **not** recommended, given the project's size (2 flows, 6 classes) and the Playbook's own "Avoid unnecessary abstractions" / "Refactor Only When justified" guidance:

- Introducing `components/`, `models/`, or `utils/` packages now — no shared UI components exist yet across pages, and no domain models are needed for two simple flows.
- Building a full Appium emulator matrix in CI (Android + iOS, multiple API levels) — disproportionate to a portfolio project; a single documented local-run path is sufficient at this stage.
- Adding Allure reporting infrastructure — not required by the standards for this scale, and no one is currently consuming test reports downstream.
- A generic multi-platform driver factory abstracting Android *and* iOS — there is no iOS implementation today; building the abstraction ahead of need violates KISS/AP-009 and the "Expand Only When... genuinely absent" rule.
- Retry/flake-handling frameworks, parallel execution, or a page-object registry/DI container — no evidence of flakiness or scale that would justify this yet.
- Full automatic state-restoration/logout-after-every-test framework — a single documented decision (Section 9) is proportionate; building elaborate teardown machinery is not.

## 8. Proposed Refactoring Order

Following `framework_architecture_standards.md`'s "Refactor Only When... Fix existing structure before adding new capability" rule, and the Playbook's mandatory workflow (Discovery → Assessment → Plan → **Approval** → Implementation → Self-Review → Independent Review → Final Verdict):

1. **Phase 0 (this document):** Discovery + Architecture Assessment — complete, awaiting approval to proceed.
2. **Phase 1 — Structural move (no logic change):** create `config/`, `pages/`, `testdata/` packages; move existing classes into them; update package declarations/imports only.
3. **Phase 2 — Config consolidation:** move hardcoded capabilities/server URL from `BaseTest` into `Config`.
4. **Phase 3 — BasePage enrichment:** add shared interaction helpers + screenshot-on-failure; refactor page objects to use them; convert locators to `static final`.
5. **Phase 4 — Test layer cleanup:** extract shared login/onboarding flow; rename test methods; remove debug artifacts (`System.out.println`, `🔥` comments).
6. **Phase 5 — Appium API modernization:** migrate `DesiredCapabilities` → `UiAutomator2Options`.
7. **Phase 6 — Documentation/CI polish:** markdown test scenarios, JDK alignment, CI limitation note.

Each phase should get its own Self-Review + Independent Review (Tarant Reviewino) cycle per procedure, rather than one large uncontrolled change.

## 9. Questions or Decisions Requiring Human Approval

1. **Scope activation conflict:** `CLAUDE.md` states mobile automation is *not currently prioritized* and `amoby_kennoby.md` is *inactive* until mobile is explicitly brought into scope. Do you want to formally activate Amoby Kennoby and update `CLAUDE.md`'s "Active Agent Roster" / "Not currently prioritized" list before any implementation phase proceeds? (This review itself was performed as analysis-only, consistent with "wait for approval before modifying files," but implementation would need this scoping question resolved first.)
2. **Missing Appium tooling standard:** `framework_architecture_standards.md`'s "Tooling Standards" section only defines rules for Selenium and Playwright projects — there is no Appium-specific subsection (required packages, driver-factory pattern, Options-class convention). Should one be authored (by Archie Core, per architecture ownership) before or alongside refactoring this repo, so the standard — not just this one project — reflects mobile conventions?
3. **Agent manual template gap:** `agent_manual.md` defines a fuller template (including a "Technical Expertise" section) than any actual file in `04_agents/` implements — none of the six agent profiles, including Amoby's, fill in specific tools/frameworks. Is this intentional (manual is aspirational/unused) or should agent profiles be completed?
4. **State restoration:** Should tests log out at teardown to comply with the Playbook's State Restoration rule, or is "quit the driver session" considered sufficient for this project given it likely runs against a disposable test account/emulator?
5. **CI scope:** Do you want CI to eventually run real Appium tests (requiring an emulator runner, e.g. `reactivecircus/android-emulator-runner`), or should the CI's role remain "compile-only" permanently, with that limitation simply documented?
6. **Priority/ordering:** Confirm whether the Phase 1–6 order in Section 8 is acceptable, or whether you'd prefer a different sequencing (e.g., debug-artifact cleanup first since it's zero-risk, before structural moves).

## 10. Exact Files Inspected / To Be Touched Per Phase

**Already inspected (this phase, read-only):**
`README.md`, `pom.xml`, `.gitignore`, `.github/workflows/ci.yml`, `.idea/*`, all of `src/main/java/com/*.java`, all of `src/test/java/com/*.java`, `Test_Scenarios.docx`, git log/status — plus every file under the `ai-operating-system` system folder (`CLAUDE.md`, `README.md`, `00_ai_collaboration/*`, `01_playbook/core_rules.md`, `02_manuals/*`, `03_procedures/*` all seven files, `04_agents/*` all six files, `05_reports/templates/01_architecture.md`, `CHANGELOG.md`).

**If approved, files touched per phase (no other files should be modified in that phase):**

- **Phase 1:** new `src/main/java/com/config/`, `src/main/java/com/pages/`, `src/test/java/com/testdata/` directories; move `Config.java`→`config/`, `BasePage.java/HomePage.java/LoginPage.java/OnboardingPage.java/SearchPage.java`→`pages/`; update package declarations in all moved files and their importers (`BaseTest.java`, `HomeTest.java`, `SearchTest.java`).
- **Phase 2:** `src/main/java/com/config/Config.java`, `src/test/java/com/BaseTest.java`.
- **Phase 3:** `src/main/java/com/pages/BasePage.java`, `HomePage.java`, `LoginPage.java`, `OnboardingPage.java`, `SearchPage.java`.
- **Phase 4:** `src/test/java/com/BaseTest.java`, `HomeTest.java`, `SearchTest.java`.
- **Phase 5:** `src/test/java/com/BaseTest.java` (and `pom.xml` only if a version bump is needed).
- **Phase 6:** `Test_Scenarios.docx` → new `docs/test-scenarios.md` (docx removed only with explicit confirmation), `README.md`, `pom.xml` and/or `.github/workflows/ci.yml`.

**System-folder files that would need updates only if you approve Decisions #1–#3 above:** `ai-operating-system/CLAUDE.md` (roster/mission), `ai-operating-system/02_manuals/framework_architecture_standards.md` (new Appium tooling subsection), `ai-operating-system/04_agents/amoby_kennoby.md` (technical expertise detail).

---

**Note:** this document reflects the state of the repository *before* the approved Phase 1–4/final-pass refactoring was implemented. See `docs/implementation-report-phase1-2.md` and the final-pass report for what was actually changed.

**Confirmation: No files were created, edited, deleted, moved, staged, committed, or pushed in either the `ai-operating-system` or `halooglasi-mobile` repositories during the original analysis itself.**

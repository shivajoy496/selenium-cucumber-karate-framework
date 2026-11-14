# selenium-cucumber-karate-framework

A Java 17 UI and API automation sample using Selenium WebDriver, Cucumber BDD, Karate, the JUnit Platform, scenario-level parallel execution, Page Objects, Maven Surefire, and Allure.

## Requirements

- JDK 17 (required by the current Karate 1.x/GraalJS combination; the integrated suite was verified on JDK 17)
- Maven 3.8+
- Google Chrome (or configure `browser=firefox` / `browser=edge` and install the matching browser)
- Network access to download Maven dependencies and Selenium Manager browser drivers

Selenium Manager resolves the browser driver automatically in current Selenium releases. The sample targets the public SauceDemo training site; it is not intended for production accounts or load testing.

## Run the tests

From the project root:

- Run defaults (`@ui` UI and `@api` API): `mvn clean test`
- Run UI tests only: `mvn test -Pui`
- Run API tests only: `mvn test -Papi`
- Run all UI and API tests: `mvn test -Pall`
- Run a member UI module: `mvn test -Pui '-Dcucumber.filter.tags=@member'`
- Run a cart API module: `mvn test -Papi '-Dkarate.tags=@cart'`
- Run only the PUT API scenario: `mvn test -Papi '-Dkarate.tags=@api_put'`
- Run selected UI and API modules together: `mvn test -Pall '-Dcucumber.filter.tags=@member' '-Dkarate.tags=@cart'`
- Override browser and app settings: `mvn test -Pui '-Dcucumber.filter.tags=@member' '-Dapp.baseUrl=https://test.example' '-Dapp.username=test-user' '-Dapp.password=test-password' -Dbrowser=chrome -Dheadless=true`
- Override API module settings: `mvn test -Papi '-Dkarate.tags=@posts' '-Dapi.posts.baseUrl=https://posts-api.test.example'`
- Run a cart API module: `mvn test -Papi '-Dkarate.tags=@cart' '-Dapi.cart.baseUrl=https://cart-api.test.example'`

`@member` and `@cart` are examples of module tags. Tag each feature/scenario with its module name; feature-level tags are inherited by its scenarios. The demo currently labels login UI as `@member`, shopping-cart UI as `@shopping-cart`, and the JSONPlaceholder API sample as `@posts`. It does not contain a cart API, so add `@cart` to your real cart API feature before selecting that tag. Optional tags such as `@ui`, `@api`, `@smoke`, and `@regression` can describe test type or purpose. Cucumber's and Karate's tag expressions are configured independently.

### Local configuration

For a direct IDE **Run Feature** or **Run Scenario** action, `ConfigReader` reads `src/test/resources/config.properties`; `headless=false` opens the browser. System properties and environment variables take precedence over this file. `cucumber.properties` supplies the glue packages and reporting plugins needed by a Cucumber IDE/CLI adapter, with no tag filter. Direct feature execution requires a Cucumber for Java adapter in the IDE.

Maven/Surefire runs receive defaults from `pom.xml` and active profiles. CLI `-D` properties override the POM values. For example, `mvn test -Pui -Dbrowser=chrome -Dheadless=true` uses Maven configuration, not the IDE-only feature-run configuration.

The supported environment variable names are `APP_BASE_URL`, `APP_USERNAME`, `APP_PASSWORD`, `API_BASE_URL`, `BROWSER`, `HEADLESS`, and `WAIT_TIMEOUT_SECONDS`. `APP_BASEURL` is also accepted as a legacy alias. The checked-in credentials are public SauceDemo sample values only; never put real credentials in `pom.xml` or `config.properties`. Use GitHub Actions/Jenkins secrets or local environment variables for real accounts.

## Parallel execution

Cucumber's native JUnit Platform engine parallel support is configured in `src/test/resources/junit-platform.properties`. It is enabled with a fixed parallelism of two scenarios to limit browser/resource contention; adjust parallelism and the matching max pool size after checking your application and CI capacity. PicoContainer creates a separate `ScenarioContext` for each scenario and injects it into steps and hooks; each context owns that scenario's WebDriver from `DriverFactory`. The Cucumber `@After` hook captures failure screenshots and always quits the browser. Do not store scenario-specific mutable state in static fields.

## Reports

- Cucumber HTML: `target/cucumber-report.html`
- Cucumber JSON: `target/cucumber.json`
- Karate HTML and logs: `target/karate-reports`
- Allure raw results: `target/allure-results`
- Generate/serve an Allure report: `mvn allure:serve`
- Generate a static Allure report: `mvn allure:report` (typically under `target/site/allure-maven-plugin`)

The `RunCucumberTest` suite registers Cucumber's pretty/summary, HTML, and JSON plugins, plus the Allure Cucumber adapter. Surefire passes Maven's tag and Karate enablement properties into the test JVM. Allure results are ignored by Git. Karate writes its own HTML report; the current official Allure Karate adapter targets Karate 2 and Java 21+, while this sample intentionally stays on Karate 1.4.1 and Java 17, so the reports remain separate.

## Project layout

- `pom.xml` — dependency versions, Java compiler, Maven Surefire test selection, and Allure Maven goal.
- `src/test/java/.../runner` — JUnit Platform suite entry point that discovers `features` and registers glue/report plugins.
- `src/test/java/.../api/KarateApiTest.java` — JUnit 5 entry point for Karate API features.
- `src/test/java/.../context/ScenarioContext.java` — PicoContainer-injected scenario-scoped state, including the scenario's WebDriver.
- `src/test/java/.../hooks` — per-scenario browser lifecycle and failure screenshot attachment.
- `src/test/java/.../steps` — readable Gherkin-to-page-object step implementations.
- `src/test/java/.../pages` — Page Object Model classes for login, inventory, and cart screens.
- `src/test/java/.../utils` — configuration, isolated WebDriver creation, and explicit-wait helpers.
- `src/test/resources/features` — login and cart Gherkin feature files.
- `src/test/resources/cucumber.properties` — local Cucumber IDE/CLI glue and reporting setup; intentionally contains no tag filter.
- `src/test/resources/karate` — Karate API feature files (independent of Cucumber's feature discovery).
- `src/test/resources/config.properties` — non-sensitive sample defaults, overridable at runtime.
- `src/test/resources/junit-platform.properties` — native Cucumber parallel execution settings.
- `.github/workflows/ui-tests.yml` — Java 17 UI job with configurable module tags, app URL, browser, and downloadable reports.
- `.github/workflows/api-tests.yml` — Java 17 API job with configurable module tags, API URL, and downloadable reports.

## Dependency guide

- `cucumber-java` provides Gherkin step annotations and Java glue.
- `cucumber-junit-platform-engine` discovers and executes Cucumber scenarios as JUnit Platform tests, including built-in parallel execution.
- `cucumber-picocontainer` provides constructor-based dependency injection and scenario-scoped object sharing for Cucumber glue.
- `junit-platform-suite` provides the `@Suite`, engine inclusion, and classpath feature selection annotations used by the runner.
- `selenium-java` provides WebDriver, browser options, and Selenium Manager integration.
- `allure-cucumber7-jvm` maps Cucumber scenarios, steps, and attachments into Allure results.
- `assertj-core` supplies fluent assertions for readable verification.
- `karate-junit5` runs Karate `.feature` API tests through JUnit 5, with HTTP request/response matching and Karate's HTML report output.
- `maven-compiler-plugin` compiles against Java 17; `maven-surefire-plugin` launches the suite during Maven's test phase; `allure-maven` provides report goals.

## Maven and runner behavior

`RunCucumberTest` is the single JUnit Platform suite entry point for both test types. It includes the Cucumber engine for UI features and the JUnit Jupiter engine for the selected `KarateApiTest` class, which runs the Karate API features. The suite selects Cucumber feature resources under `features` and configures Cucumber glue and report plugins. Surefire runs only this suite during Maven test execution. Scenario tags are separate Maven properties: `cucumber.filter.tags` selects UI scenarios and `karate.tags` selects API scenarios. The `ui`, `api`, and `all` profiles control which engine's tests are enabled and provide default tag expressions; CLI `-D` values can select module tags without editing Java or `junit-platform.properties`.

The runner disables ANSI-colored output with `cucumber.ansi-colors.disabled=true`. Cucumber 7 does not provide a `strict` option/property: undefined or pending steps already make the scenario/test run fail, so there is no separate strict setting to enable.

## Karate API tests

Karate API tests use their own JUnit 5 class and resource folder, selected by `RunCucumberTest`, so they run alongside Cucumber UI tests without being discovered as Cucumber features. The JSONPlaceholder examples use `@api @posts`; the PUT feature is additionally tagged `@api_put` and replaces post 1 with a full payload. Run just that scenario with `mvn test -Papi '-Dkarate.tags=@api_put'`. Each API module owns its URL in its feature: for example, posts reads `api.posts.baseUrl` then `API_POSTS_BASE_URL`, while cart can read `api.cart.baseUrl` then `API_CART_BASE_URL`. Set the module property in Maven or the API workflow; the posts feature has a public demo endpoint fallback for local runs. Do not define one shared API URL in `pom.xml` or `config.properties`. Karate reports are written under `target/karate-reports`.

## Continuous integration

GitHub Actions uses separate workflows: `.github/workflows/ui-tests.yml` runs the UI profile and `.github/workflows/api-tests.yml` runs the API profile. Each triggers on pushes, pull requests, or manual dispatch. The UI workflow accepts UI tags, app URL, and browser; the API workflow accepts API tags, an API module key, and an optional module URL, then passes it as `api.<module>.baseUrl`. The workflow contains no API endpoint default; provide a URL through the manual input or the `API_MODULE_BASE_URL` repository variable, or let the module feature use its own local fallback. UI credentials are read from `APP_USERNAME` and `APP_PASSWORD` repository secrets, with public SauceDemo defaults for the sample. The UI job runs headlessly; each workflow uploads its reports even when tests fail. Jenkins can use the same Maven profiles and module-scoped properties, injecting credentials from Jenkins credentials bindings.

## Cucumber dependency injection

The framework uses Cucumber PicoContainer. `ScenarioHooks` and `StoreSteps` receive `ScenarioContext` through their constructors. PicoContainer creates and shares that context only within the current scenario, so the hook can start/close the browser and step definitions can access the same scenario's driver. A new context is used for every parallel scenario, preventing state leakage. Add further scenario-scoped helper objects by declaring them as constructor parameters of glue classes; avoid static mutable state.

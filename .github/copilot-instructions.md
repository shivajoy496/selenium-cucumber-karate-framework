# Project instructions

- Use Java 17 and Maven; preserve the existing package structure under `com.example.automation`.
- Keep browser interactions in Page Object classes and reusable browser behavior in `utils`.
- Use Cucumber's JUnit Platform engine for native scenario-level parallel execution.
- Every parallel scenario must get an isolated WebDriver from `DriverFactory`; always quit it in an `@After` hook.
- Read runtime settings from `config.properties`, with system properties and environment variables taking precedence. Never commit real credentials.
- Run `mvn test` to validate framework changes.

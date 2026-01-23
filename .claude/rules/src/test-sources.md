---
paths:
  - "src/test/kotlin/**/*.kt"
---

# Test Source Rules

## Test Package Structure

```
io.github.seijikohara.logback.access
├── *Test.kt           # Feature-specific tests
├── joran/             # Joran configuration tests
├── tomcat/            # Tomcat-specific tests
├── tee/               # Tee filter tests
└── test/
    ├── assertion/     # Custom assertions
    ├── configuration/ # Test configurations
    ├── extension/     # JUnit extensions
    └── mock/          # Mock controllers
```

## Test Extensions

- Use `EventsCaptureExtension` for capturing Logback-access events
- Inject `EventsCapture` as test parameter
- Clear captured events between tests automatically

## Web Server Tests

- Test with both Tomcat and Jetty when applicable
- Use `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`
- Use `RestTestClient` for HTTP requests

## Test Configuration

- Place test configs in `src/test/resources`
- Use `logback-access-test.xml` for test-specific logging
- Use `@TestConfiguration` for test-specific beans
- Avoid modifying production configuration in tests

---
paths:
  - "src/test/**/*.kt"
  - "**/*Test.kt"
  - "**/*Tests.kt"
  - "**/*Spec.kt"
---

# Testing Conventions

## Framework

- JUnit 5 for test framework
- Kotest assertions for fluent assertions
- Spring Boot Test for integration tests

## Test Structure

- Test class naming: `{ClassName}Test.kt`
- Use `@Nested` for grouping related tests
- Use descriptive test method names with backticks

```kotlin
@Test
fun `should return 200 when request is valid`() { ... }
```

## Assertions (Kotest)

- Use Kotest matchers: `shouldBe`, `shouldNotBe`, `shouldContain`
- Prefer specific matchers over generic `shouldBe`
- Use `shouldThrow<Exception>` for exception testing

## Spring Boot Tests

- Use `@SpringBootTest` for full integration tests
- Use `@WebMvcTest` for controller-only tests
- Use `@DataJpaTest` for repository tests
- Prefer `@TestConfiguration` for test-specific beans

## Test Resources

- Place test resources under `src/test/resources`
- Use test-specific config files: `logback-access-test.xml`
- Mirror package structure for test resources

## Mocking

- Prefer real implementations over mocks when possible
- Use Mockito for necessary mocking
- Use `@MockBean` for Spring context mocks

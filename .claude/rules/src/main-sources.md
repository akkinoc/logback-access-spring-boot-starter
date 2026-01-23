---
paths:
  - "src/main/kotlin/**/*.kt"
---

# Main Source Rules

## Package Structure

```
io.github.seijikohara.logback.access
├── jetty/      # Jetty server integration
├── joran/      # Logback Joran configuration
├── security/   # Spring Security integration
├── tomcat/     # Tomcat server integration
├── tee/        # Tee filter implementation
└── value/      # Value objects and enums
```

## Public API Guidelines

- All public classes must have KDoc documentation
- Use `@since` tag for new public APIs
- Maintain backward compatibility when possible
- Mark experimental APIs with `@Experimental` annotation

## Event Source Implementation

- Extend `LogbackAccessEventSource` for new server support
- Implement lazy evaluation for expensive properties
- Use `LazyThreadSafetyMode.NONE` for non-thread-safe lazy properties
- Handle null values gracefully (return `NA` or empty collections)

## Configuration Classes

- Use `@AutoConfiguration` annotation
- Apply appropriate `@Conditional*` annotations
- Order configurations with `@AutoConfigureAfter`/`@AutoConfigureBefore`
- Document configuration properties in README

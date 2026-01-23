---
paths:
  - "examples/**/*.kt"
---

# Kotlin Example Projects

## Code Style

- Follow Kotlin idioms
- Use concise syntax where appropriate
- Keep examples simple and readable

## Application Class

```kotlin
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
```

## Keep It Simple

- Use Kotlin's concise syntax but avoid advanced features
- No custom configurations unless demonstrating a feature
- Rely on Spring Boot defaults where possible

---
paths:
  - "examples/**/*.java"
---

# Java Example Projects

## Code Style

- Follow standard Java conventions
- Use simple, readable code (examples are for learning)
- Avoid complex patterns or abstractions
- Include minimal dependencies

## Application Class

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Keep It Simple

- No custom configurations unless demonstrating a feature
- No additional controllers beyond what's needed
- Rely on Spring Boot defaults where possible

---
paths:
  - "examples/**/*"
---

# Example Projects Rules

## Project Structure

```
examples/
├── webflux-jetty-java/     # WebFlux + Jetty (Java)
├── webflux-tomcat-java/    # WebFlux + Tomcat (Java)
├── webmvc-jetty-java/      # Web MVC + Jetty (Java)
├── webmvc-tomcat-java/     # Web MVC + Tomcat (Java)
└── webmvc-tomcat-kotlin/   # Web MVC + Tomcat (Kotlin)
```

## Purpose

- Demonstrate library usage for different web server combinations
- Provide working examples for users to reference
- Validate library functionality across configurations

## Common Commands

- Build all examples: `./mvnw -pl examples -am clean verify`
- Run specific example: `./mvnw -pl examples/webmvc-tomcat-java spring-boot:run`

## Guidelines

- Keep examples minimal and focused
- Each example should demonstrate a specific configuration
- Include `logback-access.xml` with common pattern
- Use `static/index.html` for simple landing page
- Maintain consistent structure across all examples

## When Modifying Examples

- Update all relevant examples when changing library APIs
- Ensure examples compile after library changes
- Test examples manually before releasing

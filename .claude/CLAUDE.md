# logback-access-spring-boot-starter

Spring Boot Starter for Logback-access. See @README.md for full project documentation.

## Project Overview

- **Language**: Kotlin 2.2+
- **Framework**: Spring Boot 4.0
- **Build Tool**: Maven
- **Java Version**: 17, 21, 25

## Common Commands

- Build: `./mvnw clean verify`
- Test: `./mvnw test`
- Format check: `./mvnw spotless:check`
- Format apply: `./mvnw spotless:apply`
- Static analysis: `./mvnw detekt:check`
- Package: `./mvnw package -DskipTests`

## Code Quality Tools

| Tool | Purpose | Configuration |
|------|---------|---------------|
| Spotless (ktlint) | Code formatting | `pom.xml` (intellij_idea style) |
| detekt | Static analysis | `.detekt/config.yml` |
| JaCoCo | Code coverage | `pom.xml` |

## Architecture

```
.
├── src/
│   ├── main/kotlin/io/github/seijikohara/logback/access/
│   │   ├── jetty/          # Jetty web server support
│   │   ├── joran/          # Logback Joran configuration extensions
│   │   ├── security/       # Spring Security integration
│   │   ├── tomcat/         # Tomcat web server support
│   │   ├── tee/            # Tee filter for request/response logging
│   │   └── value/          # Value objects and enums
│   └── test/kotlin/io/github/seijikohara/logback/access/
│       └── test/           # Test utilities and configurations
└── examples/
    ├── webflux-jetty-java/     # WebFlux + Jetty (Java)
    ├── webflux-tomcat-java/    # WebFlux + Tomcat (Java)
    ├── webmvc-jetty-java/      # Web MVC + Jetty (Java)
    ├── webmvc-tomcat-java/     # Web MVC + Tomcat (Java)
    └── webmvc-tomcat-kotlin/   # Web MVC + Tomcat (Kotlin)
```

## Example Projects

Run example applications to test library functionality:

```bash
# Run specific example
./mvnw -pl examples/webmvc-tomcat-java spring-boot:run

# Build all examples
./mvnw -pl examples -am clean verify
```

## Git Workflow

- Branch naming: `feature/*`, `fix/*`, `chore/*`
- Commit messages: Conventional Commits format (English)
- PR descriptions: English

## Dependencies Reference

- Logback-access: https://logback.qos.ch/access.html
- Spring Boot: https://docs.spring.io/spring-boot/
- Kotlin: https://kotlinlang.org/docs/

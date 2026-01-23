---
paths:
  - "src/main/**/*.kt"
---

# Spring Boot Conventions

## Auto-Configuration

- Use `@AutoConfiguration` for auto-configuration classes
- Use `@ConditionalOnClass`, `@ConditionalOnProperty` for conditional beans
- Register auto-configurations in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

## Configuration Properties

- Use `@ConfigurationProperties` with `@ConfigurationPropertiesScan`
- Prefix properties with `logback.access`
- Use immutable properties with constructor binding

## Dependency Injection

- Prefer constructor injection over field injection
- Use `@Autowired` only when constructor injection is not possible
- Keep constructors clean; extract complex initialization to `@PostConstruct`

## Bean Naming

- Use meaningful bean names
- Avoid generic names like `config`, `service`, `repository`
- Use consistent suffixes: `*Configuration`, `*Properties`, `*Customizer`

## Logging

- Use SLF4J with Kotlin extensions
- Log levels: ERROR (failures), WARN (potential issues), INFO (lifecycle), DEBUG (details)
- Include context in log messages

## Web Server Support

- Tomcat: Use `TomcatServletWebServerFactory` customizers
- Jetty: Use `JettyServletWebServerFactory` customizers
- Support both Servlet and Reactive stacks

---
paths:
  - "**/*.kt"
---

# Kotlin Code Style

## Formatting

- Formatting is handled by Spotless with ktlint (`intellij_idea` style)
- No line length limit enforced
- Run `./mvnw spotless:apply` before committing

## Language Conventions

- Use `val` by default, `let` only when reassignment is needed
- Prefer expression bodies for simple functions
- Use named arguments for functions with multiple parameters
- Prefer data classes for DTOs and value objects
- Use sealed classes/interfaces for restricted hierarchies

## Null Safety

- Avoid `!!` operator; use safe calls (`?.`) or `requireNotNull()`
- Use `?:` (Elvis operator) for default values
- Prefer `let`, `also`, `apply`, `run` for null handling chains

## Collections

- Use immutable collections by default (`listOf`, `mapOf`, `setOf`)
- Use `sequence` for large collections with multiple operations
- Prefer collection operations over manual loops

## Coroutines (if used)

- Use `suspend` functions for asynchronous operations
- Prefer structured concurrency with `coroutineScope`
- Handle exceptions with `try-catch` or `supervisorScope`

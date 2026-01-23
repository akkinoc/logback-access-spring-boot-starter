# Documentation Standards

## Code Documentation

- Document all public APIs with KDoc
- Use `@param`, `@return`, `@throws` tags appropriately
- Include usage examples for complex APIs
- Keep documentation in sync with code changes

## KDoc Format

```kotlin
/**
 * Brief description of the class/function.
 *
 * More detailed description if needed.
 *
 * @param paramName Description of the parameter.
 * @return Description of the return value.
 * @throws ExceptionType When this exception is thrown.
 * @see RelatedClass
 */
```

## README and Markdown

- Use GitHub-flavored Markdown
- Include code examples with syntax highlighting
- Keep documentation up-to-date with releases
- Use badges for build status, version, license

## Commit Messages

- Format: Conventional Commits
- Language: English
- Examples:
  - `feat: add support for Netty web server`
  - `fix: resolve NPE in event source`
  - `docs: update README for Spring Boot 4.0`
  - `chore: update dependencies`

## PR Descriptions

- Language: English
- Include summary of changes
- Reference related issues if applicable
- Describe testing approach

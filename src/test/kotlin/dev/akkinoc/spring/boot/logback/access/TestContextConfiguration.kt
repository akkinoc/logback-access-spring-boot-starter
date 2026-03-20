package dev.akkinoc.spring.boot.logback.access

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.security.autoconfigure.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration
import org.springframework.boot.security.autoconfigure.web.reactive.ReactiveWebSecurityAutoConfiguration
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration

/**
 * The test context configuration.
 */
@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration(
    exclude = [
        SecurityAutoConfiguration::class,
        UserDetailsServiceAutoConfiguration::class,
        ReactiveUserDetailsServiceAutoConfiguration::class,
        ServletWebSecurityAutoConfiguration::class,
        SecurityFilterAutoConfiguration::class,
        ReactiveWebSecurityAutoConfiguration::class,
    ],
)
class TestContextConfiguration

package dev.akkinoc.spring.boot.logback.access

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

/**
 * The test context configuration.
 */
@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration(exclude = [SecurityAutoConfiguration::class, ReactiveSecurityAutoConfiguration::class])
class TestContextConfiguration

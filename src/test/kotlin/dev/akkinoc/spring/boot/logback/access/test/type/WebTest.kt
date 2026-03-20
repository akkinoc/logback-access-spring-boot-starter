package dev.akkinoc.spring.boot.logback.access.test.type

import dev.akkinoc.spring.boot.logback.access.test.configuration.WebTestConfiguration
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

/**
 * Indicates a Spring Boot based test using the Tomcat servlet web server.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.main.web-application-type=servlet"])
@Import(WebTestConfiguration::class)
@AutoConfigureTestRestTemplate
annotation class TomcatServletWebTest

/**
 * Indicates a Spring Boot based test using the Tomcat reactive web server.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.main.web-application-type=reactive"])
@Import(WebTestConfiguration::class)
@AutoConfigureTestRestTemplate
annotation class TomcatReactiveWebTest

/**
 * Indicates a Spring Boot based test using the Jetty servlet web server.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.main.web-application-type=servlet"])
@Import(WebTestConfiguration::class)
@AutoConfigureTestRestTemplate
annotation class JettyServletWebTest

/**
 * Indicates a Spring Boot based test using the Jetty reactive web server.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.main.web-application-type=reactive"])
@Import(WebTestConfiguration::class)
@AutoConfigureTestRestTemplate
annotation class JettyReactiveWebTest

/**
 * Indicates a Spring Boot based test using the Netty reactive web server.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.main.web-application-type=reactive"])
@Import(WebTestConfiguration::class)
@AutoConfigureTestRestTemplate
annotation class NettyReactiveWebTest

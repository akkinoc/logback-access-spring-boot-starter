package io.github.seijikohara.logback.access

import io.github.seijikohara.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import io.github.seijikohara.logback.access.test.extension.EventsCapture
import io.github.seijikohara.logback.access.test.extension.EventsCaptureExtension
import io.github.seijikohara.logback.access.test.type.JettyServletWebTest
import io.github.seijikohara.logback.access.test.type.TomcatServletWebTest
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the exclude patterns functionality.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test.capture.xml",
        "logback.access.exclude-patterns=/actuator/**,/health",
    ],
)
sealed class ExcludePatternsTest {

    @Test
    fun `Excludes requests matching exclude patterns from access logs`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        // Request to excluded path - should not be logged
        // Actuator health endpoint returns 200 when actuator is enabled
        val excludedRequest = RequestEntity.get("/actuator/health").build()
        val excludedResponse = rest.exchange<String>(excludedRequest)
        // Status code may be 200 (actuator enabled) or 404 (not enabled), either way it matches the pattern
        excludedResponse.statusCode.value() // Just execute the request

        // Request to non-excluded path - should be logged
        val includedRequest = RequestEntity.get("/mock-controller/text").build()
        val includedResponse = rest.exchange<String>(includedRequest)
        includedResponse.statusCode.value().shouldBe(200)

        // Only the non-excluded request should be in the logs
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.requestURI.shouldBe("/mock-controller/text")
    }

    @Test
    fun `Logs requests that do not match exclude patterns`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)

        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.requestURI.shouldBe("/mock-controller/text")
    }
}

/**
 * Tests the [ExcludePatternsTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebExcludePatternsTest : ExcludePatternsTest()

/**
 * Tests the [ExcludePatternsTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebExcludePatternsTest : ExcludePatternsTest()

/**
 * Tests the exclude-successful-only functionality.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test.capture.xml",
        "logback.access.exclude-patterns=/mock-controller/**",
        "logback.access.exclude-successful-only=true",
    ],
)
sealed class ExcludeSuccessfulOnlyTest {

    @Test
    fun `Excludes only successful requests when exclude-successful-only is true`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        // Successful request - should be excluded
        val successRequest = RequestEntity.get("/mock-controller/text").build()
        val successResponse = rest.exchange<String>(successRequest)
        successResponse.statusCode.value().shouldBe(200)

        // Error request - should be logged even though it matches pattern
        val errorRequest = RequestEntity.get("/mock-controller/exception").build()
        val errorResponse = rest.exchange<String>(errorRequest)
        errorResponse.statusCode.value().shouldBe(500)

        // Only the error request should be in the logs
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.requestURI.shouldBe("/mock-controller/exception")
        event.statusCode.shouldBe(500)
    }
}

/**
 * Tests the [ExcludeSuccessfulOnlyTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebExcludeSuccessfulOnlyTest : ExcludeSuccessfulOnlyTest()

/**
 * Tests the [ExcludeSuccessfulOnlyTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebExcludeSuccessfulOnlyTest : ExcludeSuccessfulOnlyTest()

/**
 * Tests that no exclusions occur when exclude-patterns is empty.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test.capture.xml",
    ],
)
sealed class NoExcludePatternsTest {

    @Test
    fun `Logs all requests when no exclude patterns are configured`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)

        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.requestURI.shouldBe("/mock-controller/text")
    }
}

/**
 * Tests the [NoExcludePatternsTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebNoExcludePatternsTest : NoExcludePatternsTest()

/**
 * Tests the [NoExcludePatternsTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebNoExcludePatternsTest : NoExcludePatternsTest()

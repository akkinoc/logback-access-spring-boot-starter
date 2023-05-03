package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the appended Logback-access event in the case where the access is invalid.
 *
 * @property supportsInvalidUrls Whether to support invalid URLs.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access-test.capture.xml"])
sealed class InvalidAccessEventTest(
    private val supportsInvalidUrls: Boolean,
) {

    @Test
    fun `Appends a Logback-access event with an invalid URL`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text?[]").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(if (supportsInvalidUrls) 200 else 400)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsInvalidUrls) {
            event.requestURI.shouldBe("/mock-controller/text")
            event.queryString.shouldBe("?[]")
            event.requestURL.shouldBe("GET /mock-controller/text?[] HTTP/1.1")
        } else {
            event.requestURI.shouldBe("-")
            event.queryString.shouldBeEmpty()
            event.requestURL.shouldBe("GET null HTTP/1.1")
        }
    }

}

/**
 * Tests the [InvalidAccessEventTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebInvalidAccessEventTest : InvalidAccessEventTest(
    supportsInvalidUrls = false,
)

/**
 * Tests the [InvalidAccessEventTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebInvalidAccessEventTest : InvalidAccessEventTest(
    supportsInvalidUrls = false,
)

/**
 * Tests the [InvalidAccessEventTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebInvalidAccessEventTest : InvalidAccessEventTest(
    supportsInvalidUrls = true,
)

/**
 * Tests the [InvalidAccessEventTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebInvalidAccessEventTest : InvalidAccessEventTest(
    supportsInvalidUrls = true,
)

/**
 * Tests the [InvalidAccessEventTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebInvalidAccessEventTest : InvalidAccessEventTest(
    supportsInvalidUrls = true,
)

/**
 * Tests the [InvalidAccessEventTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebInvalidAccessEventTest : InvalidAccessEventTest(
    supportsInvalidUrls = true,
)

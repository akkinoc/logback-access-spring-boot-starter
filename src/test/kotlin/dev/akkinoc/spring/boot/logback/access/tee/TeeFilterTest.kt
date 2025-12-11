package dev.akkinoc.spring.boot.logback.access.tee

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where the tee filter is enabled.
 *
 * @property supportsRequestContents Whether to support request contents.
 * @property supportsResponseContents Whether to support response contents.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test.capture.xml",
        "logback.access.tee-filter.enabled=true",
    ],
)
sealed class TeeFilterTest(
    private val supportsRequestContents: Boolean,
    private val supportsResponseContents: Boolean,
) {

    @Test
    fun `Appends a Logback-access event through the tee filter`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsRequestContents) event.requestContent.shouldBeEmpty()
        if (supportsResponseContents) event.responseContent.shouldBe("mock-text")
    }

    @Test
    fun `Appends a Logback-access event with a request content through the tee filter`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.post("/mock-controller/text")
            .header("content-type", "text/plain")
            .body("posted-text")
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsRequestContents) event.requestContent.shouldBe("posted-text")
        if (supportsResponseContents) event.responseContent.shouldBe("mock-text")
    }

    @Test
    fun `Appends a Logback-access event with a form data request content through the tee filter`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.post("/mock-controller/form-data")
            .header("content-type", "application/x-www-form-urlencoded")
            .body("a=value+%40a&b=value1+%40b&b=value2+%40b&c=")
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsRequestContents) event.requestContent.shouldBe("a=value+%40a&b=value1+%40b&b=value2+%40b&c=")
        if (supportsResponseContents) event.responseContent.shouldBe("mock-text")
    }

    @Test
    fun `Appends a Logback-access event with an image response content through the tee filter`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-static/image.svg").build()
        val response = rest.exchange<ByteArray>(request)
        response.statusCode.value().shouldBe(200)
        response.hasBody().shouldBeTrue()
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsRequestContents) event.requestContent.shouldBeEmpty()
        if (supportsResponseContents) event.responseContent.shouldBe("[IMAGE CONTENTS SUPPRESSED]")
    }

}

/**
 * Tests the [TeeFilterTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTeeFilterTest : TeeFilterTest(
    supportsRequestContents = true,
    supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTeeFilterTest : TeeFilterTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTeeFilterTest : TeeFilterTest(
    supportsRequestContents = true,
    supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTeeFilterTest : TeeFilterTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)


package io.github.seijikohara.logback.access

import io.github.seijikohara.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import io.github.seijikohara.logback.access.test.configuration.TracingTestConfiguration
import io.github.seijikohara.logback.access.test.extension.EventsCapture
import io.github.seijikohara.logback.access.test.extension.EventsCaptureExtension
import io.github.seijikohara.logback.access.test.type.JettyServletWebTest
import io.github.seijikohara.logback.access.test.type.TomcatServletWebTest
import io.github.seijikohara.logback.access.tracing.LogbackAccessTracingServletFilter.Companion.SPAN_ID_ATTRIBUTE
import io.github.seijikohara.logback.access.tracing.LogbackAccessTracingServletFilter.Companion.TRACE_ID_ATTRIBUTE
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeBlank
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.context.annotation.Import
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where Micrometer Tracing is enabled.
 *
 * @property supportsTracing Whether to support tracing attributes.
 */
@ExtendWith(EventsCaptureExtension::class)
@Import(TracingTestConfiguration::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test.capture.xml",
    ],
)
sealed class TracingAttributesTest(private val supportsTracing: Boolean) {

    @Test
    fun `Captures tracing attributes in the appended Logback-access event`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsTracing) {
            val traceId = event.getAttribute(TRACE_ID_ATTRIBUTE)
            val spanId = event.getAttribute(SPAN_ID_ATTRIBUTE)
            traceId.shouldNotBe("-")
            traceId.shouldNotBeBlank()
            spanId.shouldNotBe("-")
            spanId.shouldNotBeBlank()
        } else {
            event.getAttribute(TRACE_ID_ATTRIBUTE).shouldBe("-")
            event.getAttribute(SPAN_ID_ATTRIBUTE).shouldBe("-")
        }
    }
}

/**
 * Tests the [TracingAttributesTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTracingAttributesTest :
    TracingAttributesTest(
        supportsTracing = true,
    )

/**
 * Tests the [TracingAttributesTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTracingAttributesTest :
    TracingAttributesTest(
        supportsTracing = true,
    )

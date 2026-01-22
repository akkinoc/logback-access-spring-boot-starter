package io.github.seijikohara.logback.access

import io.github.seijikohara.logback.access.test.assertion.Assertions.assertLogbackAccessEventsContinually
import io.github.seijikohara.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import io.github.seijikohara.logback.access.test.extension.EventsCapture
import io.github.seijikohara.logback.access.test.extension.EventsCaptureExtension
import io.github.seijikohara.logback.access.test.type.JettyReactiveWebTest
import io.github.seijikohara.logback.access.test.type.JettyServletWebTest
import io.github.seijikohara.logback.access.test.type.TomcatReactiveWebTest
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
 * Tests the case where event filters are used in the configuration file.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access-test.filter.capture.xml"])
sealed class EventFilterTest {

    @Test
    fun `If the filter accepts a Logback-access event, appends it`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
            .header("mock-event-filter-reply", "accept")
            .build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        assertLogbackAccessEventsEventually { capture.shouldBeSingleton() }
    }

    @Test
    fun `If the filter passes a Logback-access event, appends it`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
            .header("mock-event-filter-reply", "neutral")
            .build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        assertLogbackAccessEventsEventually { capture.shouldBeSingleton() }
    }

    @Test
    fun `If the filter denies a Logback-access event, does not append it`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
            .header("mock-event-filter-reply", "deny")
            .build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        assertLogbackAccessEventsContinually { capture.shouldBeEmpty() }
    }

}

/**
 * Tests the [EventFilterTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebEventFilterTest : EventFilterTest()

/**
 * Tests the [EventFilterTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebEventFilterTest : EventFilterTest()

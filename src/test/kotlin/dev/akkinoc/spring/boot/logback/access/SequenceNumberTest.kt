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
import io.kotest.matchers.collections.shouldBeSorted
import io.kotest.matchers.collections.shouldBeUnique
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where sequence numbers are used in the configuration file.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access-test.sequence-number.capture.xml"])
sealed class SequenceNumberTest {

    @Test
    fun `Generates unique sequence numbers`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        for (n in 1..3) {
            val request = RequestEntity.get("/mock-controller/text").build()
            val response = rest.exchange<String>(request)
            response.statusCode.value().shouldBe(200)
            val event = assertLogbackAccessEventsEventually { capture.shouldHaveSize(n).last() }
            event.sequenceNumber.shouldBeGreaterThan(0L)
        }
        capture.map { it.sequenceNumber }.shouldBeUnique().shouldBeSorted()
    }

}

/**
 * Tests the [SequenceNumberTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebSequenceNumberTest : SequenceNumberTest()

/**
 * Tests the [SequenceNumberTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebSequenceNumberTest : SequenceNumberTest()

/**
 * Tests the [SequenceNumberTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebSequenceNumberTest : SequenceNumberTest()

/**
 * Tests the [SequenceNumberTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebSequenceNumberTest : SequenceNumberTest()

/**
 * Tests the [SequenceNumberTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebSequenceNumberTest : SequenceNumberTest()

/**
 * Tests the [SequenceNumberTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebSequenceNumberTest : SequenceNumberTest()

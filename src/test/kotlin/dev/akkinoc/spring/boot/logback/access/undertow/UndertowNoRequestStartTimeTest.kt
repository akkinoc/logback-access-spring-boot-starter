package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource
import java.lang.System.currentTimeMillis

/**
 * Tests the case where the Undertow request start time is not recorded.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test.capture.xml",
        "logback.access.undertow.record-request-start-time=false",
    ],
)
sealed class UndertowNoRequestStartTimeTest {

    @Test
    fun `Does not return the elapsed time of the appended Logback-access event`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val started = currentTimeMillis()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        val finished = currentTimeMillis()
        event.timeStamp.shouldBeBetween(started, finished)
        event.elapsedTime.shouldBe(-1L)
        event.elapsedSeconds.shouldBe(-1L)
    }

}

/**
 * Tests the [UndertowNoRequestStartTimeTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebNoRequestStartTimeTest : UndertowNoRequestStartTimeTest()

/**
 * Tests the [UndertowNoRequestStartTimeTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebNoRequestStartTimeTest : UndertowNoRequestStartTimeTest()

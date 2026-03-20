package dev.akkinoc.spring.boot.logback.access.tomcat

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where the Tomcat request attributes are enabled and forward headers are not supported.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
    properties = [
        "server.forward-headers-strategy=none",
        "logback.access.config=classpath:logback-access-test.capture.xml",
        "logback.access.tomcat.request-attributes-enabled=true",
    ],
)
sealed class TomcatRequestAttributesEnabledTest {

    @Test
    fun `Does not rewrite some attributes of the appended Logback-access event with forward headers`(
        @Autowired rest: TestRestTemplate,
        @LocalServerPort port: Int,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.serverName.shouldBe("localhost")
        event.localPort.shouldBe(port)
        event.remoteAddr.shouldBe("127.0.0.1")
        event.remoteHost.shouldBe("127.0.0.1")
        event.protocol.shouldBe("HTTP/1.1")
    }

    @Test
    fun `Does not rewrite some attributes of the appended Logback-access event without forward headers`(
        @Autowired rest: TestRestTemplate,
        @LocalServerPort port: Int,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.serverName.shouldBe("localhost")
        event.localPort.shouldBe(port)
        event.remoteAddr.shouldBe("127.0.0.1")
        event.remoteHost.shouldBe("127.0.0.1")
        event.protocol.shouldBe("HTTP/1.1")
    }

}

/**
 * Tests the [TomcatRequestAttributesEnabledTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebRequestAttributesEnabledTest : TomcatRequestAttributesEnabledTest()

/**
 * Tests the [TomcatRequestAttributesEnabledTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebRequestAttributesEnabledTest : TomcatRequestAttributesEnabledTest()

package io.github.seijikohara.logback.access

import io.github.seijikohara.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import io.github.seijikohara.logback.access.test.extension.EventsCapture
import io.github.seijikohara.logback.access.test.extension.EventsCaptureExtension
import io.github.seijikohara.logback.access.test.type.JettyServletWebTest
import io.github.seijikohara.logback.access.test.type.TomcatServletWebTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.boot.test.web.server.LocalManagementPort
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource
import java.net.URI

/**
 * Tests the case where management server runs on a separate port.
 *
 * Note: Currently only Tomcat is tested. Jetty test is disabled due to Spring Boot Actuator
 * management context auto-configuration conflict with test classloader settings.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test.capture.xml",
        "management.server.port=0",
        "management.endpoints.web.exposure.include=health,info",
    ],
)
abstract class ManagementPortTest {

    @Test
    fun `Logs requests to management endpoints on separate port`(
        @Autowired rest: TestRestTemplate,
        @LocalManagementPort managementPort: Int,
        capture: EventsCapture,
    ) {
        // Request to main application endpoint
        val mainRequest = RequestEntity.get("/mock-controller/text").build()
        val mainResponse = rest.exchange<String>(mainRequest)
        mainResponse.statusCode.value().shouldBe(200)

        // Request to management endpoint on separate port
        val managementRequest = RequestEntity.get(URI("http://localhost:$managementPort/actuator/health")).build()
        val managementResponse = rest.exchange<String>(managementRequest)
        managementResponse.statusCode.value().shouldBe(200)

        // Both requests should be logged
        val events = assertLogbackAccessEventsEventually { capture.shouldHaveSize(2) }
        val mainEvent = events.find { it.requestURI == "/mock-controller/text" }
        val managementEvent = events.find { it.requestURI?.contains("actuator") == true }

        mainEvent?.statusCode.shouldBe(200)
        managementEvent?.statusCode.shouldBe(200)
        managementEvent?.requestURI.shouldContain("actuator/health")
    }

    @Test
    fun `Logs only management endpoint requests on separate port`(
        @Autowired rest: TestRestTemplate,
        @LocalManagementPort managementPort: Int,
        capture: EventsCapture,
    ) {
        // Request only to management endpoint
        val managementRequest = RequestEntity.get(URI("http://localhost:$managementPort/actuator/health")).build()
        val managementResponse = rest.exchange<String>(managementRequest)
        managementResponse.statusCode.value().shouldBe(200)

        // Management request should be logged
        val event = assertLogbackAccessEventsEventually { capture.shouldHaveSize(1).single() }
        event.requestURI.shouldContain("actuator/health")
        event.statusCode.shouldBe(200)
    }
}

/**
 * Tests the [ManagementPortTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebManagementPortTest : ManagementPortTest()

// TODO: Jetty test is currently disabled due to Spring Boot Actuator management context
// auto-configuration conflict with test classloader settings.
// The TomcatServletManagementContextAutoConfiguration and JettyServletManagementContextAutoConfiguration
// both try to register 'servletWebChildContextFactory' bean in the management context.
// This needs further investigation in a separate issue.

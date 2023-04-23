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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where forward headers are supported.
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access-test.capture.xml"])
sealed class ForwardHeadersSupportTest {

    @Test
    fun `Rewrites some attributes of the appended Logback-access event with forward headers`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text")
            .header("x-forwarded-host", "forwarded-host")
            .header("x-forwarded-port", "12345")
            .header("x-forwarded-for", "1.2.3.4")
            .header("x-forwarded-proto", "https")
            .build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.serverName.shouldBe("forwarded-host")
        event.localPort.shouldBe(12345)
        event.remoteAddr.shouldBe("1.2.3.4")
        event.remoteHost.shouldBe("1.2.3.4")
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
 * Tests the case where forward headers are supported natively.
 */
@TestPropertySource(properties = ["server.forward-headers-strategy=native"])
sealed class NativeForwardHeadersSupportTest : ForwardHeadersSupportTest()

/**
 * Tests the [NativeForwardHeadersSupportTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebNativeForwardHeadersSupportTest : NativeForwardHeadersSupportTest()

/**
 * Tests the [NativeForwardHeadersSupportTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebNativeForwardHeadersSupportTest : NativeForwardHeadersSupportTest()

/**
 * Tests the [NativeForwardHeadersSupportTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebNativeForwardHeadersSupportTest : NativeForwardHeadersSupportTest()

/**
 * Tests the [NativeForwardHeadersSupportTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebNativeForwardHeadersSupportTest : NativeForwardHeadersSupportTest()

/**
 * Tests the [NativeForwardHeadersSupportTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebNativeForwardHeadersSupportTest : NativeForwardHeadersSupportTest()

/**
 * Tests the [NativeForwardHeadersSupportTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebNativeForwardHeadersSupportTest : NativeForwardHeadersSupportTest()

// TODO: Add support for forward headers supported by framework.
// /**
//  * Tests the case where forward headers are supported by framework.
//  */
// @TestPropertySource(properties = ["server.forward-headers-strategy=framework"])
// sealed class FrameworkForwardHeadersSupportTest : ForwardHeadersSupportTest()

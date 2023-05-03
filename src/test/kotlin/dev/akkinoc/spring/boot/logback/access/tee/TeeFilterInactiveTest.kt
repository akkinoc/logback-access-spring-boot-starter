package dev.akkinoc.spring.boot.logback.access.tee

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
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import java.net.InetAddress

/**
 * Tests the case where the tee filter is inactive.
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
sealed class TeeFilterInactiveTest(
    private val supportsRequestContents: Boolean,
    private val supportsResponseContents: Boolean,
) {

    @Test
    fun `Appends a Logback-access event without passing through the tee filter`(
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
        if (supportsRequestContents) event.requestContent.shouldBeEmpty()
        if (supportsResponseContents) event.responseContent.shouldBeEmpty()
    }

}

/**
 * Tests the case where the current host is not included in the tee filter's inclusion hosts.
 *
 * @property supportsRequestContents Whether to support request contents.
 * @property supportsResponseContents Whether to support response contents.
 */
sealed class TeeFilterHostNotIncludedTest(
    supportsRequestContents: Boolean,
    supportsResponseContents: Boolean,
) : TeeFilterInactiveTest(
    supportsRequestContents = supportsRequestContents,
    supportsResponseContents = supportsResponseContents,
) {

    companion object {

        /**
         * Registers dynamic properties.
         *
         * @param registry The dynamic property registry.
         */
        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            val host = InetAddress.getLocalHost().hostName
            registry.add("logback.access.tee-filter.includes") { "non-$host" }
        }

    }

}

/**
 * Tests the [TeeFilterHostNotIncludedTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTeeFilterHostNotIncludedTest : TeeFilterHostNotIncludedTest(
    supportsRequestContents = true,
    supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostNotIncludedTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTeeFilterHostNotIncludedTest : TeeFilterHostNotIncludedTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostNotIncludedTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTeeFilterHostNotIncludedTest : TeeFilterHostNotIncludedTest(
    supportsRequestContents = true,
    supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostNotIncludedTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTeeFilterHostNotIncludedTest : TeeFilterHostNotIncludedTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostNotIncludedTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTeeFilterHostNotIncludedTest : TeeFilterHostNotIncludedTest(
    supportsRequestContents = true,
    supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostNotIncludedTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTeeFilterHostNotIncludedTest : TeeFilterHostNotIncludedTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)

/**
 * Tests the case where the current host is included in the tee filter's exclusion hosts.
 *
 * @property supportsRequestContents Whether to support request contents.
 * @property supportsResponseContents Whether to support response contents.
 */
sealed class TeeFilterHostExcludedTest(
    supportsRequestContents: Boolean,
    supportsResponseContents: Boolean,
) : TeeFilterInactiveTest(
    supportsRequestContents = supportsRequestContents,
    supportsResponseContents = supportsResponseContents,
) {

    companion object {

        /**
         * Registers dynamic properties.
         *
         * @param registry The dynamic property registry.
         */
        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            val host = InetAddress.getLocalHost().hostName
            registry.add("logback.access.tee-filter.excludes") { host }
        }

    }

}

/**
 * Tests the [TeeFilterHostExcludedTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTeeFilterHostExcludedTest : TeeFilterHostExcludedTest(
    supportsRequestContents = true,
    supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostExcludedTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTeeFilterHostExcludedTest : TeeFilterHostExcludedTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostExcludedTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTeeFilterHostExcludedTest : TeeFilterHostExcludedTest(
    supportsRequestContents = true,
    supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostExcludedTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTeeFilterHostExcludedTest : TeeFilterHostExcludedTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostExcludedTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTeeFilterHostExcludedTest : TeeFilterHostExcludedTest(
    supportsRequestContents = true,
    supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostExcludedTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTeeFilterHostExcludedTest : TeeFilterHostExcludedTest(
    supportsRequestContents = false,
    supportsResponseContents = false,
)

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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import java.net.InetAddress.getLocalHost

/**
 * Tests the case where the tee filter is active.
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
sealed class TeeFilterActiveTest(
        private val supportsRequestContents: Boolean,
        private val supportsResponseContents: Boolean,
) {

    @Test
    fun `Appends a Logback-access event through the tee filter`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.post("/mock-controller/text")
                .header("content-type", "text/plain")
                .body("posted-text")
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        response.body.shouldBe("mock-text")
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsRequestContents) event.requestContent.shouldBe("posted-text")
        if (supportsResponseContents) event.responseContent.shouldBe("mock-text")
    }

}

/**
 * Tests the case where the current host is included in the tee filter's inclusion hosts.
 *
 * @property supportsRequestContents Whether to support request contents.
 * @property supportsResponseContents Whether to support response contents.
 */
sealed class TeeFilterHostIncludedTest(
        supportsRequestContents: Boolean,
        supportsResponseContents: Boolean,
) : TeeFilterActiveTest(
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
            val host = getLocalHost().hostName
            registry.add("logback.access.tee-filter.includes") { host }
        }

    }

}

/**
 * Tests the [TeeFilterHostIncludedTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTeeFilterHostIncludedTest : TeeFilterHostIncludedTest(
        supportsRequestContents = true,
        supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostIncludedTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTeeFilterHostIncludedTest : TeeFilterHostIncludedTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostIncludedTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTeeFilterHostIncludedTest : TeeFilterHostIncludedTest(
        supportsRequestContents = true,
        supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostIncludedTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTeeFilterHostIncludedTest : TeeFilterHostIncludedTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostIncludedTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTeeFilterHostIncludedTest : TeeFilterHostIncludedTest(
        supportsRequestContents = true,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostIncludedTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTeeFilterHostIncludedTest : TeeFilterHostIncludedTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

/**
 * Tests the case where the current host is not included in the tee filter's exclusion hosts.
 *
 * @property supportsRequestContents Whether to support request contents.
 * @property supportsResponseContents Whether to support response contents.
 */
sealed class TeeFilterHostNotExcludedTest(
        supportsRequestContents: Boolean,
        supportsResponseContents: Boolean,
) : TeeFilterActiveTest(
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
            val host = getLocalHost().hostName
            registry.add("logback.access.tee-filter.excludes") { "non-$host" }
        }

    }

}

/**
 * Tests the [TeeFilterHostNotExcludedTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTeeFilterHostNotExcludedTest : TeeFilterHostNotExcludedTest(
        supportsRequestContents = true,
        supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostNotExcludedTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTeeFilterHostNotExcludedTest : TeeFilterHostNotExcludedTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostNotExcludedTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTeeFilterHostNotExcludedTest : TeeFilterHostNotExcludedTest(
        supportsRequestContents = true,
        supportsResponseContents = true,
)

/**
 * Tests the [TeeFilterHostNotExcludedTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTeeFilterHostNotExcludedTest : TeeFilterHostNotExcludedTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostNotExcludedTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTeeFilterHostNotExcludedTest : TeeFilterHostNotExcludedTest(
        supportsRequestContents = true,
        supportsResponseContents = false,
)

/**
 * Tests the [TeeFilterHostNotExcludedTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTeeFilterHostNotExcludedTest : TeeFilterHostNotExcludedTest(
        supportsRequestContents = false,
        supportsResponseContents = false,
)

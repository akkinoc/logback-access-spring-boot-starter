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
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.context.annotation.Import
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where Spring Security is enabled.
 *
 * @property supportsRemoteUsers Whether to support remote users.
 */
@ExtendWith(EventsCaptureExtension::class)
@Import(SecurityAutoConfiguration::class, ReactiveSecurityAutoConfiguration::class)
@TestPropertySource(
    properties = [
        "spring.security.user.name=test-user",
        "spring.security.user.password=test-password",
        "logback.access.config=classpath:logback-access-test.capture.xml",
    ],
)
sealed class SecurityAttributesTest(
    private val supportsRemoteUsers: Boolean,
) {

    @Test
    fun `Rewrites the remote user of the appended Logback-access event with Spring Security`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.withBasicAuth("test-user", "test-password").exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        if (supportsRemoteUsers) {
            event.remoteUser.shouldBe("test-user")
        } else {
            event.remoteUser.shouldBe("-")
        }
    }

}

/**
 * Tests the [SecurityAttributesTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebSecurityAttributesTest : SecurityAttributesTest(
    supportsRemoteUsers = true,
)

/**
 * Tests the [SecurityAttributesTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebSecurityAttributesTest : SecurityAttributesTest(
    supportsRemoteUsers = false,
)

/**
 * Tests the [SecurityAttributesTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebSecurityAttributesTest : SecurityAttributesTest(
    supportsRemoteUsers = true,
)

/**
 * Tests the [SecurityAttributesTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebSecurityAttributesTest : SecurityAttributesTest(
    supportsRemoteUsers = false,
)

/**
 * Tests the [SecurityAttributesTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebSecurityAttributesTest : SecurityAttributesTest(
    supportsRemoteUsers = true,
)

/**
 * Tests the [SecurityAttributesTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebSecurityAttributesTest : SecurityAttributesTest(
    supportsRemoteUsers = false,
)

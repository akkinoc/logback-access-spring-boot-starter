package io.github.seijikohara.logback.access

import io.github.seijikohara.logback.access.test.type.JettyReactiveWebTest
import io.github.seijikohara.logback.access.test.type.JettyServletWebTest
import io.github.seijikohara.logback.access.test.type.NonWebTest
import io.github.seijikohara.logback.access.test.type.TomcatReactiveWebTest
import io.github.seijikohara.logback.access.test.type.TomcatServletWebTest
import io.kotest.matchers.nulls.shouldBeNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where auto-configuration is inactive.
 */
sealed class InactiveTest {

    @Test
    fun `Does not provide the configuration properties for Logback-access`(
        @Autowired logbackAccessProperties: LogbackAccessProperties?,
    ) {
        logbackAccessProperties.shouldBeNull()
    }

    @Test
    fun `Does not provide the Logback-access context`(@Autowired logbackAccessContext: LogbackAccessContext?) {
        logbackAccessContext.shouldBeNull()
    }
}

/**
 * Tests the case where the web server is not used.
 */
@NonWebTest
class NonWebInactiveTest : InactiveTest()

/**
 * Tests the case where auto-configuration is disabled.
 */
@TestPropertySource(properties = ["logback.access.enabled=false"])
sealed class DisabledTest : InactiveTest()

/**
 * Tests the [DisabledTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebDisabledTest : DisabledTest()

/**
 * Tests the [DisabledTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebDisabledTest : DisabledTest()

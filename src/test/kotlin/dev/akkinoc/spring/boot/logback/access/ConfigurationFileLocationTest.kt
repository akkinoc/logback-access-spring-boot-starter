package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCapture
import dev.akkinoc.spring.boot.logback.access.test.extension.EventsCaptureExtension
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
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
 * Tests the case where the location of the configuration file is specified.
 */
@ExtendWith(EventsCaptureExtension::class)
sealed class ConfigurationFileLocationTest {

    @Test
    fun `Appends a Logback-access event according to the configuration file found`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        assertLogbackAccessEventsEventually { capture.shouldBeSingleton() }
    }

}

/**
 * Tests the case where the location of the configuration file is specified as a file system path.
 */
@TestPropertySource(properties = ["logback.access.config=target/test-classes/logback-access-test.capture.xml"])
sealed class ConfigurationFilePathTest : ConfigurationFileLocationTest()

/**
 * Tests the [ConfigurationFilePathTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebConfigurationFilePathTest : ConfigurationFilePathTest()

/**
 * Tests the [ConfigurationFilePathTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebConfigurationFilePathTest : ConfigurationFilePathTest()

/**
 * Tests the [ConfigurationFilePathTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebConfigurationFilePathTest : ConfigurationFilePathTest()

/**
 * Tests the [ConfigurationFilePathTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebConfigurationFilePathTest : ConfigurationFilePathTest()

/**
 * Tests the case where the location of the configuration file is specified as a file scheme URI.
 */
@TestPropertySource(properties = ["logback.access.config=file:target/test-classes/logback-access-test.capture.xml"])
sealed class ConfigurationFileUriTest : ConfigurationFileLocationTest()

/**
 * Tests the [ConfigurationFileUriTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebConfigurationFileUriTest : ConfigurationFileUriTest()

/**
 * Tests the [ConfigurationFileUriTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebConfigurationFileUriTest : ConfigurationFileUriTest()

/**
 * Tests the [ConfigurationFileUriTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebConfigurationFileUriTest : ConfigurationFileUriTest()

/**
 * Tests the [ConfigurationFileUriTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebConfigurationFileUriTest : ConfigurationFileUriTest()

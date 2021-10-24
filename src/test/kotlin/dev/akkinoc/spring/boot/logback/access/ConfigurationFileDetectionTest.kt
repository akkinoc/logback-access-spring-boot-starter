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
import org.springframework.http.RequestEntity

/**
 * Tests the case where the configuration file is detected.
 */
@ExtendWith(EventsCaptureExtension::class)
sealed class ConfigurationFileDetectionTest {

    @Test
    fun `Appends a Logback-access event according to the detected configuration file`(
            @Autowired rest: TestRestTemplate,
            capture: EventsCapture,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCodeValue.shouldBe(200)
        assertLogbackAccessEventsEventually { capture.shouldBeSingleton() }
    }

}

/**
 * Tests the case where the configuration file ("classpath:logback-access-test.xml") is detected.
 */
sealed class TestConfigurationFileDetectionTest : ConfigurationFileDetectionTest()

/**
 * Tests the [TestConfigurationFileDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTestConfigurationFileDetectionTest : TestConfigurationFileDetectionTest()

/**
 * Tests the [TestConfigurationFileDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTestConfigurationFileDetectionTest : TestConfigurationFileDetectionTest()

/**
 * Tests the [TestConfigurationFileDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTestConfigurationFileDetectionTest : TestConfigurationFileDetectionTest()

/**
 * Tests the [TestConfigurationFileDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTestConfigurationFileDetectionTest : TestConfigurationFileDetectionTest()

/**
 * Tests the [TestConfigurationFileDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTestConfigurationFileDetectionTest : TestConfigurationFileDetectionTest()

/**
 * Tests the [TestConfigurationFileDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTestConfigurationFileDetectionTest : TestConfigurationFileDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access.xml") is detected.
 */
sealed class MainConfigurationFileDetectionTest : ConfigurationFileDetectionTest()

/**
 * Tests the [MainConfigurationFileDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebMainConfigurationFileDetectionTest : MainConfigurationFileDetectionTest()

/**
 * Tests the [MainConfigurationFileDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebMainConfigurationFileDetectionTest : MainConfigurationFileDetectionTest()

/**
 * Tests the [MainConfigurationFileDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebMainConfigurationFileDetectionTest : MainConfigurationFileDetectionTest()

/**
 * Tests the [MainConfigurationFileDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebMainConfigurationFileDetectionTest : MainConfigurationFileDetectionTest()

/**
 * Tests the [MainConfigurationFileDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebMainConfigurationFileDetectionTest : MainConfigurationFileDetectionTest()

/**
 * Tests the [MainConfigurationFileDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebMainConfigurationFileDetectionTest : MainConfigurationFileDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access-test-spring.xml") is detected.
 */
sealed class TestSpringConfigurationFileDetectionTest : ConfigurationFileDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebTestSpringConfigurationFileDetectionTest : TestSpringConfigurationFileDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebTestSpringConfigurationFileDetectionTest : TestSpringConfigurationFileDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebTestSpringConfigurationFileDetectionTest : TestSpringConfigurationFileDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebTestSpringConfigurationFileDetectionTest : TestSpringConfigurationFileDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebTestSpringConfigurationFileDetectionTest : TestSpringConfigurationFileDetectionTest()

/**
 * Tests the [TestSpringConfigurationFileDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebTestSpringConfigurationFileDetectionTest : TestSpringConfigurationFileDetectionTest()

/**
 * Tests the case where the configuration file ("classpath:logback-access-spring.xml") is detected.
 */
sealed class MainSpringConfigurationFileDetectionTest : ConfigurationFileDetectionTest()

/**
 * Tests the [MainSpringConfigurationFileDetectionTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebMainSpringConfigurationFileDetectionTest : MainSpringConfigurationFileDetectionTest()

/**
 * Tests the [MainSpringConfigurationFileDetectionTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebMainSpringConfigurationFileDetectionTest : MainSpringConfigurationFileDetectionTest()

/**
 * Tests the [MainSpringConfigurationFileDetectionTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebMainSpringConfigurationFileDetectionTest : MainSpringConfigurationFileDetectionTest()

/**
 * Tests the [MainSpringConfigurationFileDetectionTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebMainSpringConfigurationFileDetectionTest : MainSpringConfigurationFileDetectionTest()

/**
 * Tests the [MainSpringConfigurationFileDetectionTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebMainSpringConfigurationFileDetectionTest : MainSpringConfigurationFileDetectionTest()

/**
 * Tests the [MainSpringConfigurationFileDetectionTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebMainSpringConfigurationFileDetectionTest : MainSpringConfigurationFileDetectionTest()

package dev.akkinoc.spring.boot.logback.access

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.value.LogbackAccessLocalPortStrategy
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.http.RequestEntity

/**
 * Tests the case where the configuration is the default.
 */
@ExtendWith(OutputCaptureExtension::class)
sealed class BasicTest {

    @Test
    fun `Provides the configuration properties for Logback-access`(
        @Autowired logbackAccessProperties: LogbackAccessProperties?,
    ) {
        logbackAccessProperties.shouldNotBeNull()
        logbackAccessProperties.enabled.shouldBe(true)
        logbackAccessProperties.config.shouldBeNull()
        logbackAccessProperties.localPortStrategy.shouldBe(LogbackAccessLocalPortStrategy.SERVER)
        logbackAccessProperties.tomcat.requestAttributesEnabled.shouldBeNull()
        logbackAccessProperties.teeFilter.enabled.shouldBe(false)
        logbackAccessProperties.teeFilter.includes.shouldBeNull()
        logbackAccessProperties.teeFilter.excludes.shouldBeNull()
    }

    @Test
    fun `Provides the Logback-access context`(
        @Autowired logbackAccessContext: LogbackAccessContext?,
        @Autowired logbackAccessProperties: LogbackAccessProperties?,
    ) {
        logbackAccessContext.shouldNotBeNull()
        logbackAccessContext.properties.shouldBeSameInstanceAs(logbackAccessProperties)
    }

    @Test
    fun `Appends a Logback-access event`(
        @Autowired rest: TestRestTemplate,
        capture: CapturedOutput,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("mock-text")
        val regex = Regex("""^127\.0\.0\.1 - - \[.+] "GET /mock-controller/text HTTP/1\.1" 200 9$""")
        assertLogbackAccessEventsEventually { capture.out.lines().shouldHaveSingleElement { it matches regex } }
    }

}

/**
 * Tests the [BasicTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebBasicTest : BasicTest()

/**
 * Tests the [BasicTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebBasicTest : BasicTest()

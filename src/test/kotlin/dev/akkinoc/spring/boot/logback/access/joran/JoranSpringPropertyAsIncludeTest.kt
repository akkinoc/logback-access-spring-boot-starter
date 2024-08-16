package dev.akkinoc.spring.boot.logback.access.joran

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.UndertowServletWebTest
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where `<springProperty>` tags are used in the configuration file.
 */
@ExtendWith(OutputCaptureExtension::class)
@TestPropertySource(
    properties = [
        "logback.access.config=classpath:logback-access-test-spring.property-as-include.xml",
        "logback.access.test.console.pattern.prefix=>>>",
        "logback.access.test.console.pattern.suffix=<<<",
    ],
)
sealed class JoranSpringPropertyAsIncludeTest {

    @Test
    fun `Appends a Logback-access event according to the configuration file that contains springProperty tags`(
        @Autowired rest: TestRestTemplate,
        capture: CapturedOutput,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("mock-text")
        assertLogbackAccessEventsEventually {
            capture.out.lines().shouldHaveSingleElement { it.startsWith(">>>") && it.endsWith("<<<") }
        }
    }

}

/**
 * Tests the [JoranSpringPropertyAsIncludeTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebJoranSpringPropertyAsIncludeTest : JoranSpringPropertyAsIncludeTest()

/**
 * Tests the [JoranSpringPropertyAsIncludeTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebJoranSpringPropertyAsIncludeTest : JoranSpringPropertyAsIncludeTest()

/**
 * Tests the [JoranSpringPropertyAsIncludeTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebJoranSpringPropertyAsIncludeTest : JoranSpringPropertyAsIncludeTest()

/**
 * Tests the [JoranSpringPropertyAsIncludeTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebJoranSpringPropertyAsIncludeTest : JoranSpringPropertyAsIncludeTest()

/**
 * Tests the [JoranSpringPropertyAsIncludeTest] using the Undertow servlet web server.
 */
@UndertowServletWebTest
class UndertowServletWebJoranSpringPropertyAsIncludeTest : JoranSpringPropertyAsIncludeTest()

/**
 * Tests the [JoranSpringPropertyAsIncludeTest] using the Undertow reactive web server.
 */
@UndertowReactiveWebTest
class UndertowReactiveWebJoranSpringPropertyAsIncludeTest : JoranSpringPropertyAsIncludeTest()

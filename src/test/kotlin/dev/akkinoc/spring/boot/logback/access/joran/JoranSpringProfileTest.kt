package dev.akkinoc.spring.boot.logback.access.joran

import dev.akkinoc.spring.boot.logback.access.test.assertion.Assertions.assertLogbackAccessEventsEventually
import dev.akkinoc.spring.boot.logback.access.test.type.JettyReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.JettyServletWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatReactiveWebTest
import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.exchange
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.http.RequestEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

/**
 * Tests the case where `<springProfile>` tags are used in the configuration file.
 */
@ExtendWith(OutputCaptureExtension::class)
@ActiveProfiles(
    "logback-access-test-disable-default-console",
    "logback-access-test-enable-additional-console",
    "logback-access-test-enable-additional-nested-console",
)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access-test-spring.profile.xml"])
sealed class JoranSpringProfileTest {

    @Test
    fun `Appends a Logback-access event according to the configuration file that contains springProfile tags`(
        @Autowired rest: TestRestTemplate,
        capture: CapturedOutput,
    ) {
        val request = RequestEntity.get("/mock-controller/text").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("mock-text")
        assertLogbackAccessEventsEventually {
            capture.out.shouldNotContain("default_console:")
            capture.out.shouldNotContain("default_nested_console:")
            capture.out.lines().shouldHaveSingleElement { it.startsWith("additional_console:") }
            capture.out.lines().shouldHaveSingleElement { it.startsWith("additional_nested_console:") }
            capture.out.shouldNotContain("ignored_console:")
        }
    }

}

/**
 * Tests the [JoranSpringProfileTest] using the Tomcat servlet web server.
 */
@TomcatServletWebTest
class TomcatServletWebJoranSpringProfileTest : JoranSpringProfileTest()

/**
 * Tests the [JoranSpringProfileTest] using the Tomcat reactive web server.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebJoranSpringProfileTest : JoranSpringProfileTest()

/**
 * Tests the [JoranSpringProfileTest] using the Jetty servlet web server.
 */
@JettyServletWebTest
class JettyServletWebJoranSpringProfileTest : JoranSpringProfileTest()

/**
 * Tests the [JoranSpringProfileTest] using the Jetty reactive web server.
 */
@JettyReactiveWebTest
class JettyReactiveWebJoranSpringProfileTest : JoranSpringProfileTest()

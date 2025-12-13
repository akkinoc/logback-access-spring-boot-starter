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
 * Tests the appended Logback-access event in the case where the access is with special characters in URL.
 *
 * In Spring Boot 4, the TestRestTemplate (moved to spring-boot-resttestclient module) uses
 * Spring Framework's DefaultUriBuilderFactory with TEMPLATE_AND_VALUES encoding mode by default.
 * This mode pre-encodes the URI template, converting square brackets `[]` to `%5B%5D` before
 * sending the HTTP request. As a result, all servers receive RFC 3986 compliant URLs.
 *
 * This is a behavioral change from Spring Boot 3, where:
 * - Tomcat 10 rejected unencoded square brackets with HTTP 400 (RFC 3986/7230 strict compliance)
 * - Jetty 11 accepted unencoded square brackets and returned HTTP 200
 *
 * References:
 * - RFC 3986 Section 2.2: Square brackets are reserved characters (gen-delims) and must be
 *   percent-encoded in query strings
 * - Spring Framework DefaultUriBuilderFactory: TEMPLATE_AND_VALUES mode uses
 *   UriComponentsBuilder#encode() to pre-encode URI templates
 * - Spring Boot 4 Migration Guide: TestRestTemplate moved to spring-boot-resttestclient module
 *
 * @property queryString The expected query string format (URL-encoded as per RFC 3986).
 * @see <a href="https://www.rfc-editor.org/rfc/rfc3986#section-2.2">RFC 3986 Section 2.2</a>
 * @see <a href="https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-uri-building.html">
 *     Spring Framework URI Links</a>
 */
@ExtendWith(EventsCaptureExtension::class)
@TestPropertySource(properties = ["logback.access.config=classpath:logback-access-test.capture.xml"])
sealed class InvalidAccessEventTest(
    private val queryString: String,
) {

    @Test
    fun `Appends a Logback-access event with an invalid URL`(
        @Autowired rest: TestRestTemplate,
        capture: EventsCapture,
    ) {
        // Square brackets in the query string are automatically percent-encoded by
        // Spring Boot 4's TestRestTemplate (DefaultUriBuilderFactory with TEMPLATE_AND_VALUES mode)
        // before the HTTP request is sent: "?[]" becomes "?%5B%5D"
        val request = RequestEntity.get("/mock-controller/text?[]").build()
        val response = rest.exchange<String>(request)
        response.statusCode.value().shouldBe(200)
        val event = assertLogbackAccessEventsEventually { capture.shouldBeSingleton().single() }
        event.requestURI.shouldBe("/mock-controller/text")
        // Query string is URL-encoded as per RFC 3986
        event.queryString.shouldBe(queryString)
        event.requestURL.shouldBe("GET /mock-controller/text$queryString HTTP/1.1")
    }

}

/**
 * Tests the [InvalidAccessEventTest] using the Tomcat servlet web server.
 *
 * With Spring Boot 4's TestRestTemplate URL encoding, the query string is pre-encoded
 * before reaching the server, so Tomcat 11 receives a valid RFC 3986 compliant URL.
 */
@TomcatServletWebTest
class TomcatServletWebInvalidAccessEventTest : InvalidAccessEventTest(
    queryString = "?%5B%5D",
)

/**
 * Tests the [InvalidAccessEventTest] using the Tomcat reactive web server.
 *
 * With Spring Boot 4's TestRestTemplate URL encoding, the query string is pre-encoded
 * before reaching the server, so Tomcat 11 receives a valid RFC 3986 compliant URL.
 */
@TomcatReactiveWebTest
class TomcatReactiveWebInvalidAccessEventTest : InvalidAccessEventTest(
    queryString = "?%5B%5D",
)

/**
 * Tests the [InvalidAccessEventTest] using the Jetty servlet web server.
 *
 * With Spring Boot 4's TestRestTemplate URL encoding, the query string is pre-encoded
 * before reaching the server, so Jetty 12 receives a valid RFC 3986 compliant URL.
 */
@JettyServletWebTest
class JettyServletWebInvalidAccessEventTest : InvalidAccessEventTest(
    queryString = "?%5B%5D",
)

/**
 * Tests the [InvalidAccessEventTest] using the Jetty reactive web server.
 *
 * With Spring Boot 4's TestRestTemplate URL encoding, the query string is pre-encoded
 * before reaching the server, so Jetty 12 receives a valid RFC 3986 compliant URL.
 */
@JettyReactiveWebTest
class JettyReactiveWebInvalidAccessEventTest : InvalidAccessEventTest(
    queryString = "?%5B%5D",
)

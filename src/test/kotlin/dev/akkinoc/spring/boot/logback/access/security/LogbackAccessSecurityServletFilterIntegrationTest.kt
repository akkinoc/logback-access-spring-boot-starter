package dev.akkinoc.spring.boot.logback.access.security

import dev.akkinoc.spring.boot.logback.access.test.type.TomcatServletWebTest
import io.kotest.matchers.shouldBe
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.context.annotation.Import
import org.springframework.http.RequestEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

/**
 * Integration tests for [LogbackAccessSecurityServletFilter].
 */
@TomcatServletWebTest
@Import(
    ReactiveUserDetailsServiceAutoConfiguration::class,
    SecurityAutoConfiguration::class,
    ReactiveSecurityAutoConfiguration::class,
    LogbackAccessSecurityServletFilterIntegrationTest.DispatcherEndpoints::class,
)
@TestPropertySource(
    properties = [
        "spring.security.user.name=test-user",
        "spring.security.user.password=test-password",
        "logback.access.config=classpath:logback-access-test.capture.xml",
    ],
)
class LogbackAccessSecurityServletFilterIntegrationTest {

    @Test
    fun `Applies to the REQUEST dispatcher type`(@Autowired rest: TestRestTemplate) {
        val request = RequestEntity.get("/logback-access-security-filter/inspect").build()
        val response = rest.withBasicAuth("test-user", "test-password").exchange<String>(request)

        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("test-user")
    }

    @Test
    fun `Applies to the FORWARD dispatcher type`(@Autowired rest: TestRestTemplate) {
        val request = RequestEntity.get("/logback-access-security-filter/forward").build()
        val response = rest.withBasicAuth("test-user", "test-password").exchange<String>(request)

        response.statusCode.value().shouldBe(200)
        response.body.shouldBe("test-user")
    }

    /**
     * Endpoints used to verify dispatcher handling.
     */
    @Controller
    @RequestMapping("/logback-access-security-filter")
    class DispatcherEndpoints {

        @GetMapping("/inspect")
        @ResponseBody
        fun inspect(request: HttpServletRequest): String? {
            return request.getAttribute(LogbackAccessSecurityServletFilter.REMOTE_USER_ATTRIBUTE) as String?
        }

        @GetMapping("/forward")
        fun forward(request: HttpServletRequest): ModelAndView {
            // Overwrite the attribute so the FORWARD dispatch must restore the remote user.
            request.setAttribute(LogbackAccessSecurityServletFilter.REMOTE_USER_ATTRIBUTE, "sentinel")
            return ModelAndView("forward:/logback-access-security-filter/inspect")
        }
    }
}

package dev.akkinoc.spring.boot.logback.access.test.mock

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession

/**
 * The mock controller for testing using the reactive web server.
 */
@RestController
@RequestMapping("/mock-controller")
class MockReactiveController {

    /**
     * Gets a text with request attributes.
     *
     * @param exchange The request/response exchange to set attributes.
     * @return A text.
     */
    @GetMapping("/text-with-request-attributes")
    fun getTextWithRequestAttributes(exchange: ServerWebExchange): String {
        exchange.attributes["a"] = "value @a"
        exchange.attributes["b"] = listOf("value1 @b", "value2 @b")
        exchange.attributes["c"] = ""
        exchange.attributes["d"] = Any()
        val response = "mock-text"
        log.debug("Getting a text with request attributes: {}; {}", response, exchange)
        return response
    }

    /**
     * Gets a text with a session.
     *
     * @param session The session.
     * @return A text.
     */
    @GetMapping("/text-with-session")
    fun getTextWithSession(session: WebSession): String {
        session.start()
        val response = "mock-text"
        log.debug("Getting a text with a session: {}; {}", response, session)
        return response
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(MockReactiveController::class.java)

    }

}

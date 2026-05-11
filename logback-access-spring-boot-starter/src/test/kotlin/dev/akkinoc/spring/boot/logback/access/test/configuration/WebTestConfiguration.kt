package dev.akkinoc.spring.boot.logback.access.test.configuration

import dev.akkinoc.spring.boot.logback.access.test.mock.MockController
import dev.akkinoc.spring.boot.logback.access.test.mock.MockReactiveController
import dev.akkinoc.spring.boot.logback.access.test.mock.MockServletController
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

/**
 * The test configuration for testing using the web server.
 */
@TestConfiguration(proxyBeanMethods = false)
class WebTestConfiguration {

    /**
     * Provides the mock controller for testing using the web server.
     *
     * @return The mock controller for testing using the web server.
     */
    @Bean
    fun mockController(): MockController {
        val mockController = MockController()
        log.debug("Providing the {}: {}", MockController::class.simpleName, mockController)
        return mockController
    }

    /**
     * Provides the mock controller for testing using the servlet web server.
     *
     * @return The mock controller for testing using the servlet web server.
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    fun mockServletController(): MockServletController {
        val mockServletController = MockServletController()
        log.debug("Providing the {}: {}", MockServletController::class.simpleName, mockServletController)
        return mockServletController
    }

    /**
     * Provides the mock controller for testing using the reactive web server.
     *
     * @return The mock controller for testing using the reactive web server.
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    fun mockReactiveController(): MockReactiveController {
        val mockReactiveController = MockReactiveController()
        log.debug("Providing the {}: {}", MockReactiveController::class.simpleName, mockReactiveController)
        return mockReactiveController
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(WebTestConfiguration::class.java)

    }

}

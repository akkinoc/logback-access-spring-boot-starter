package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import io.undertow.Undertow
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The configuration for the Undertow servlet web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Undertow::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class LogbackAccessUndertowServletConfiguration {

    /**
     * Provides the [WebServerFactoryCustomizer] for the Undertow servlet web server.
     *
     * @param logbackAccessContext The Logback-access context.
     * @return The [WebServerFactoryCustomizer] for the Undertow servlet web server.
     */
    @Bean
    @ConditionalOnMissingBean
    fun logbackAccessUndertowServletWebServerFactoryCustomizer(
        logbackAccessContext: LogbackAccessContext,
    ): LogbackAccessUndertowServletWebServerFactoryCustomizer {
        val logbackAccessUndertowServletWebServerFactoryCustomizer =
            LogbackAccessUndertowServletWebServerFactoryCustomizer(logbackAccessContext)
        log.debug(
            "Providing the {}: {}",
            LogbackAccessUndertowServletWebServerFactoryCustomizer::class.simpleName,
            logbackAccessUndertowServletWebServerFactoryCustomizer,
        )
        return logbackAccessUndertowServletWebServerFactoryCustomizer
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessUndertowServletConfiguration::class.java)

    }

}

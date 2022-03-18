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
 * The configuration for the Undertow web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Undertow::class)
@ConditionalOnWebApplication
class LogbackAccessUndertowConfiguration {

    /**
     * Provides the [WebServerFactoryCustomizer] for the Undertow web server.
     *
     * @param logbackAccessContext The Logback-access context.
     * @return The [WebServerFactoryCustomizer] for the Undertow web server.
     */
    @Bean
    @ConditionalOnMissingBean
    fun logbackAccessUndertowWebServerFactoryCustomizer(
        logbackAccessContext: LogbackAccessContext,
    ): LogbackAccessUndertowWebServerFactoryCustomizer {
        val logbackAccessUndertowWebServerFactoryCustomizer =
            LogbackAccessUndertowWebServerFactoryCustomizer(logbackAccessContext)
        log.debug(
            "Providing the {}: {}",
            LogbackAccessUndertowWebServerFactoryCustomizer::class.simpleName,
            logbackAccessUndertowWebServerFactoryCustomizer,
        )
        return logbackAccessUndertowWebServerFactoryCustomizer
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessUndertowConfiguration::class.java)

    }

}

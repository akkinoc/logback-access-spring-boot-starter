package dev.akkinoc.spring.boot.logback.access.jetty

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import org.eclipse.jetty.server.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The configuration for the Jetty web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Server::class)
@ConditionalOnWebApplication
class LogbackAccessJettyConfiguration {

    /**
     * Provides the [WebServerFactoryCustomizer] for the Jetty web server.
     *
     * @param logbackAccessContext The Logback-access context.
     * @return The [WebServerFactoryCustomizer] for the Jetty web server.
     */
    @Bean
    @ConditionalOnMissingBean
    fun logbackAccessJettyWebServerFactoryCustomizer(
            logbackAccessContext: LogbackAccessContext,
    ): LogbackAccessJettyWebServerFactoryCustomizer {
        val logbackAccessJettyWebServerFactoryCustomizer =
                LogbackAccessJettyWebServerFactoryCustomizer(logbackAccessContext)
        log.debug(
                "Providing the {}: {}",
                LogbackAccessJettyWebServerFactoryCustomizer::class.simpleName,
                logbackAccessJettyWebServerFactoryCustomizer,
        )
        return logbackAccessJettyWebServerFactoryCustomizer
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessJettyConfiguration::class.java)

    }

}

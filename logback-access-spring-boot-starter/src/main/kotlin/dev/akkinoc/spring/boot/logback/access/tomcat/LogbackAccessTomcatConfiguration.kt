package dev.akkinoc.spring.boot.logback.access.tomcat

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import org.apache.catalina.startup.Tomcat
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The configuration for the Tomcat web server.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Tomcat::class)
@ConditionalOnWebApplication
class LogbackAccessTomcatConfiguration {

    /**
     * Provides the [WebServerFactoryCustomizer] for the Tomcat web server.
     *
     * @param logbackAccessContext The Logback-access context.
     * @return The [WebServerFactoryCustomizer] for the Tomcat web server.
     */
    @Bean
    @ConditionalOnMissingBean
    fun logbackAccessTomcatWebServerFactoryCustomizer(
        logbackAccessContext: LogbackAccessContext,
    ): LogbackAccessTomcatWebServerFactoryCustomizer {
        val logbackAccessTomcatWebServerFactoryCustomizer =
            LogbackAccessTomcatWebServerFactoryCustomizer(logbackAccessContext)
        log.debug(
            "Providing the {}: {}",
            LogbackAccessTomcatWebServerFactoryCustomizer::class.simpleName,
            logbackAccessTomcatWebServerFactoryCustomizer,
        )
        return logbackAccessTomcatWebServerFactoryCustomizer
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessTomcatConfiguration::class.java)

    }

}

package io.github.seijikohara.logback.access.management

import io.github.seijikohara.logback.access.LogbackAccessContext
import io.github.seijikohara.logback.access.jetty.LogbackAccessJettyWebServerFactoryCustomizer
import io.github.seijikohara.logback.access.tomcat.LogbackAccessTomcatWebServerFactoryCustomizer
import org.apache.catalina.startup.Tomcat
import org.eclipse.jetty.server.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextType
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The management context configuration for Logback-access.
 *
 * This configuration enables access logging for the management server when it runs
 * on a separate port (configured via `management.server.port`).
 */
@ManagementContextConfiguration(ManagementContextType.CHILD)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class LogbackAccessManagementContextConfiguration {

    /**
     * The configuration for the Tomcat management web server.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(Tomcat::class)
    class TomcatManagementConfiguration {

        /**
         * Provides the [LogbackAccessTomcatWebServerFactoryCustomizer] for the management server.
         *
         * @param logbackAccessContext The Logback-access context.
         * @return The [LogbackAccessTomcatWebServerFactoryCustomizer] for the management server.
         */
        @Bean
        @ConditionalOnMissingBean(name = ["logbackAccessManagementTomcatWebServerFactoryCustomizer"])
        fun logbackAccessManagementTomcatWebServerFactoryCustomizer(
            logbackAccessContext: LogbackAccessContext,
        ): LogbackAccessTomcatWebServerFactoryCustomizer {
            val customizer = LogbackAccessTomcatWebServerFactoryCustomizer(logbackAccessContext)
            log.debug(
                "Providing the {} for management context: {}",
                LogbackAccessTomcatWebServerFactoryCustomizer::class.simpleName,
                customizer,
            )
            return customizer
        }

        companion object {
            private val log: Logger = getLogger(TomcatManagementConfiguration::class.java)
        }
    }

    /**
     * The configuration for the Jetty management web server.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(Server::class)
    class JettyManagementConfiguration {

        /**
         * Provides the [LogbackAccessJettyWebServerFactoryCustomizer] for the management server.
         *
         * @param logbackAccessContext The Logback-access context.
         * @return The [LogbackAccessJettyWebServerFactoryCustomizer] for the management server.
         */
        @Bean
        @ConditionalOnMissingBean(name = ["logbackAccessManagementJettyWebServerFactoryCustomizer"])
        fun logbackAccessManagementJettyWebServerFactoryCustomizer(
            logbackAccessContext: LogbackAccessContext,
        ): LogbackAccessJettyWebServerFactoryCustomizer {
            val customizer = LogbackAccessJettyWebServerFactoryCustomizer(logbackAccessContext)
            log.debug(
                "Providing the {} for management context: {}",
                LogbackAccessJettyWebServerFactoryCustomizer::class.simpleName,
                customizer,
            )
            return customizer
        }

        companion object {
            private val log: Logger = getLogger(JettyManagementConfiguration::class.java)
        }
    }

    companion object {
        private val log: Logger = getLogger(LogbackAccessManagementContextConfiguration::class.java)
    }
}

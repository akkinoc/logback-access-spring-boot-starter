package io.github.seijikohara.logback.access.tomcat

import io.github.seijikohara.logback.access.LogbackAccessContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.tomcat.ConfigurableTomcatWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer

/**
 * The [WebServerFactoryCustomizer] for the Tomcat web server.
 *
 * @property logbackAccessContext The Logback-access context.
 * @see org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer
 */
class LogbackAccessTomcatWebServerFactoryCustomizer(
    private val logbackAccessContext: LogbackAccessContext,
) : WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> {

    override fun customize(factory: ConfigurableTomcatWebServerFactory) {
        val valve = LogbackAccessTomcatValve(logbackAccessContext)
        factory.addEngineValves(valve)
        log.debug(
            "Customized the {}: {} @{}",
            ConfigurableTomcatWebServerFactory::class.simpleName,
            factory,
            logbackAccessContext,
        )
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessTomcatWebServerFactoryCustomizer::class.java)

    }

}

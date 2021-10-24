package dev.akkinoc.spring.boot.logback.access.jetty

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.RequestLogHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer

/**
 * The [WebServerFactoryCustomizer] for the Jetty web server.
 *
 * @property logbackAccessContext The Logback-access context.
 * @see org.springframework.boot.autoconfigure.web.embedded.JettyWebServerFactoryCustomizer
 */
class LogbackAccessJettyWebServerFactoryCustomizer(
        private val logbackAccessContext: LogbackAccessContext,
) : WebServerFactoryCustomizer<ConfigurableJettyWebServerFactory> {

    override fun customize(factory: ConfigurableJettyWebServerFactory) {
        factory.addServerCustomizers(::customize)
        log.debug(
                "Customized the {}: {} @{}",
                ConfigurableJettyWebServerFactory::class.simpleName,
                factory,
                logbackAccessContext,
        )
    }

    /**
     * Customizes the [Server].
     *
     * @param server The [Server].
     */
    private fun customize(server: Server) {
        val handler = RequestLogHandler().apply {
            requestLog = LogbackAccessJettyRequestLog(logbackAccessContext)
        }
        server.insertHandler(handler)
        log.debug(
                "Customized the {}: {} @{}",
                Server::class.simpleName,
                server,
                logbackAccessContext,
        )
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessJettyWebServerFactoryCustomizer::class.java)

    }

}

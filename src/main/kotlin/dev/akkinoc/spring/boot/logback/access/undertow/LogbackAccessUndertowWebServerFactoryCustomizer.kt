package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import io.undertow.Undertow.Builder
import io.undertow.UndertowOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer

/**
 * The [WebServerFactoryCustomizer] for the Undertow web server.
 *
 * @property logbackAccessContext The Logback-access context.
 * @see org.springframework.boot.autoconfigure.web.embedded.UndertowWebServerFactoryCustomizer
 */
class LogbackAccessUndertowWebServerFactoryCustomizer(
    private val logbackAccessContext: LogbackAccessContext,
) : WebServerFactoryCustomizer<ConfigurableUndertowWebServerFactory> {

    override fun customize(factory: ConfigurableUndertowWebServerFactory) {
        factory.addBuilderCustomizers(::customize)
        log.debug(
            "Customized the {}: {} @{}",
            ConfigurableUndertowWebServerFactory::class.simpleName,
            factory,
            logbackAccessContext,
        )
    }

    /**
     * Customizes the [Builder].
     *
     * @param builder The [Builder].
     */
    private fun customize(builder: Builder) {
        val props = logbackAccessContext.properties.undertow
        builder.setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, props.recordRequestStartTime)
        log.debug(
            "Customized the {}: {} @{}",
            Builder::class.simpleName,
            builder,
            logbackAccessContext,
        )
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessUndertowWebServerFactoryCustomizer::class.java)

    }

}

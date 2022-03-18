package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import io.undertow.Undertow.Builder
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.web.embedded.undertow.HttpHandlerFactory
import org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory
import org.springframework.boot.web.embedded.undertow.UndertowWebServer
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory
import org.springframework.boot.web.server.WebServer
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.util.ReflectionUtils.findField
import org.springframework.util.ReflectionUtils.getField
import org.springframework.util.ReflectionUtils.makeAccessible

/**
 * The [ReactiveWebServerFactory] for the Undertow reactive web server.
 *
 * @property logbackAccessContext The Logback-access context.
 * @see org.springframework.boot.autoconfigure.web.embedded.UndertowWebServerFactoryCustomizer
 * @see org.springframework.boot.web.embedded.undertow.UndertowReactiveWebServerFactory
 * @see org.springframework.boot.web.embedded.undertow.UndertowWebServerFactoryDelegate
 * @see org.springframework.boot.web.embedded.undertow.AccessLogHttpHandlerFactory
 */
class LogbackAccessUndertowReactiveWebServerFactory(
    private val logbackAccessContext: LogbackAccessContext,
) : UndertowReactiveWebServerFactory() {

    // FIXME: I'd like to fix this class if there is a better way.
    // I couldn't find an extension point for Undertow HttpHandler, so I'm using reflection to customize it.

    override fun getWebServer(httpHandler: HttpHandler): WebServer {
        val originalServer = super.getWebServer(httpHandler) as UndertowWebServer
        val builder = originalServer.extractField<Builder>("builder")
        val httpHandlerFactories = mutableListOf<HttpHandlerFactory>().apply {
            addAll(originalServer.extractField<Iterable<HttpHandlerFactory>>("httpHandlerFactories"))
            add { LogbackAccessUndertowHttpHandler(logbackAccessContext, it) }
        }
        val autoStart = originalServer.extractField<Boolean>("autoStart")
        val server = UndertowWebServer(builder, httpHandlerFactories, autoStart)
        log.debug(
            "Customized the {}: {} @{}",
            WebServer::class.simpleName,
            server,
            logbackAccessContext,
        )
        return server
    }

    /**
     * Extracts the field value from the [UndertowWebServer].
     *
     * @receiver The [UndertowWebServer].
     * @param T The field type.
     * @param name The field name.
     * @return The field value.
     */
    private inline fun <reified T> UndertowWebServer.extractField(name: String): T {
        val field = findField(UndertowWebServer::class.java, name)
        checkNotNull(field) { "Failed to extract the field: UndertowWebServer.$name" }
        makeAccessible(field)
        return getField(field, this) as T
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessUndertowReactiveWebServerFactory::class.java)

    }

}

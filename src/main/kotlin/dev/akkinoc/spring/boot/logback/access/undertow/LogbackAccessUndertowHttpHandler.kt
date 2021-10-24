package dev.akkinoc.spring.boot.logback.access.undertow

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEvent
import io.undertow.server.ExchangeCompletionListener.NextListener
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

/**
 * The Undertow [HttpHandler] to emit Logback-access events.
 *
 * @property logbackAccessContext The Logback-access context.
 * @property next The next [HttpHandler].
 * @see io.undertow.server.handlers.accesslog.AccessLogHandler
 */
class LogbackAccessUndertowHttpHandler(
        private val logbackAccessContext: LogbackAccessContext,
        private val next: HttpHandler,
) : HttpHandler {

    override fun handleRequest(exchange: HttpServerExchange) {
        log.debug(
                "Handling the {}: {} @{}",
                HttpServerExchange::class.simpleName,
                exchange,
                logbackAccessContext,
        )
        exchange.addExchangeCompleteListener(::log)
        next.handleRequest(exchange)
    }

    /**
     * Logs the request/response exchange.
     *
     * @param exchange The request/response exchange.
     * @param next The next listener.
     */
    private fun log(exchange: HttpServerExchange, next: NextListener) {
        log.debug(
                "Logging the {}: {} @{}",
                HttpServerExchange::class.simpleName,
                exchange,
                logbackAccessContext,
        )
        val source = LogbackAccessUndertowEventSource(
                exchange = exchange,
                localPortStrategy = logbackAccessContext.properties.localPortStrategy,
        )
        val event = LogbackAccessEvent(source)
        logbackAccessContext.emit(event)
        next.proceed()
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessUndertowHttpHandler::class.java)

    }

}

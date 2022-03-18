package dev.akkinoc.spring.boot.logback.access.jetty

import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEvent
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.RequestLog
import org.eclipse.jetty.server.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

/**
 * The Jetty [RequestLog] to emit Logback-access events.
 *
 * @property logbackAccessContext The Logback-access context.
 * @see org.eclipse.jetty.server.CustomRequestLog
 * @see ch.qos.logback.access.jetty.RequestLogImpl
 */
class LogbackAccessJettyRequestLog(
    private val logbackAccessContext: LogbackAccessContext,
) : RequestLog {

    override fun log(request: Request, response: Response) {
        log.debug(
            "Logging the {}/{}: {} => {} @{}",
            Request::class.simpleName,
            Response::class.simpleName,
            request,
            response,
            logbackAccessContext,
        )
        val source = LogbackAccessJettyEventSource(
            request = request,
            response = response,
            localPortStrategy = logbackAccessContext.properties.localPortStrategy,
        )
        val event = LogbackAccessEvent(source)
        logbackAccessContext.emit(event)
    }

    companion object {

        /**
         * The logger.
         */
        private val log: Logger = getLogger(LogbackAccessJettyRequestLog::class.java)

    }

}

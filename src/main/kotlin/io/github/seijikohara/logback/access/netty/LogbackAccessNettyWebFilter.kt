package io.github.seijikohara.logback.access.netty

import io.github.seijikohara.logback.access.LogbackAccessContext
import io.github.seijikohara.logback.access.LogbackAccessEvent
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.core.Ordered
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.System.currentTimeMillis
import java.util.concurrent.atomic.AtomicLong

/**
 * A WebFilter that logs access events for Netty web server.
 *
 * @property logbackAccessContext The Logback-access context.
 */
class LogbackAccessNettyWebFilter(
    private val logbackAccessContext: LogbackAccessContext,
) : WebFilter,
    Ordered {

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val startTime = currentTimeMillis()
        val bytesWritten = AtomicLong(0)

        val decoratedResponse = ContentLengthTrackingResponse(exchange.response, bytesWritten)
        val decoratedExchange = exchange.mutate().response(decoratedResponse).build()

        return chain.filter(decoratedExchange)
            .doFinally {
                @Suppress("TooGenericExceptionCaught")
                try {
                    logAccess(decoratedExchange, startTime, bytesWritten.get())
                } catch (e: Exception) {
                    log.warn("Failed to log access event", e)
                }
            }
    }

    private fun logAccess(exchange: ServerWebExchange, startTime: Long, bytesWritten: Long) {
        val request = exchange.request
        val response = exchange.response

        log.debug(
            "Logging the Request/Response: {} {} => {} @{}",
            request.method,
            request.uri,
            response.statusCode,
            logbackAccessContext,
        )

        val source = LogbackAccessNettyEventSource(
            logbackAccessContext = logbackAccessContext,
            serverHttpRequest = request,
            serverHttpResponse = response,
            startTime = startTime,
            responseContentLength = bytesWritten,
        )
        val event = LogbackAccessEvent(source)
        logbackAccessContext.emit(event)
    }

    /**
     * A response decorator that tracks the number of bytes written.
     */
    private class ContentLengthTrackingResponse(
        delegate: ServerHttpResponse,
        private val bytesWritten: AtomicLong,
    ) : ServerHttpResponseDecorator(delegate) {

        override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
            val trackedBody = Flux.from(body).doOnNext { buffer ->
                bytesWritten.addAndGet(buffer.readableByteCount().toLong())
            }
            return super.writeWith(trackedBody)
        }

        override fun writeAndFlushWith(body: Publisher<out Publisher<out DataBuffer>>): Mono<Void> {
            val trackedBody = Flux.from(body).map { inner ->
                Flux.from(inner).doOnNext { buffer ->
                    bytesWritten.addAndGet(buffer.readableByteCount().toLong())
                }
            }
            return super.writeAndFlushWith(trackedBody)
        }
    }

    companion object {
        private val log: Logger = getLogger(LogbackAccessNettyWebFilter::class.java)
    }
}

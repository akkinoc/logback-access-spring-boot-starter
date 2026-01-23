package io.github.seijikohara.logback.access.tracing

import io.micrometer.tracing.Tracer
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest

/**
 * The tracing filter for the servlet web server.
 *
 * Extracts tracing context from Micrometer Tracing and stores it as request attributes
 * for use in logback-access patterns via `%reqAttribute{traceId}`, `%reqAttribute{spanId}`,
 * and `%reqAttribute{parentId}`.
 *
 * @property tracer The Micrometer Tracer.
 */
class LogbackAccessTracingServletFilter(private val tracer: Tracer) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        tracer.currentSpan()?.context()?.let { context ->
            request.setAttribute(TRACE_ID_ATTRIBUTE, context.traceId())
            request.setAttribute(SPAN_ID_ATTRIBUTE, context.spanId())
            context.parentId()?.let { parentId ->
                request.setAttribute(PARENT_ID_ATTRIBUTE, parentId)
            }
        }
        chain.doFilter(request, response)
    }

    companion object {

        /**
         * The attribute name for the trace ID.
         */
        const val TRACE_ID_ATTRIBUTE: String = "traceId"

        /**
         * The attribute name for the span ID.
         */
        const val SPAN_ID_ATTRIBUTE: String = "spanId"

        /**
         * The attribute name for the parent ID.
         */
        const val PARENT_ID_ATTRIBUTE: String = "parentId"
    }
}

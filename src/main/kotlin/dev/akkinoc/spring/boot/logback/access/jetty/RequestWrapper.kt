package dev.akkinoc.spring.boot.logback.access.jetty

import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.server.Request

/**
 * Request wrapper that implements Servlet API.
 */
class RequestWrapper(private val request: Request) : ch.qos.logback.access.jetty.RequestWrapper(request) {

    override fun getContentType(): String? {
        return request.headers[HttpHeader.CONTENT_TYPE]
    }
}

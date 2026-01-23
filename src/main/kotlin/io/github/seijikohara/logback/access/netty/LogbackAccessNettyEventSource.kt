package io.github.seijikohara.logback.access.netty

import ch.qos.logback.access.common.spi.ServerAdapter
import io.github.seijikohara.logback.access.LogbackAccessContext
import io.github.seijikohara.logback.access.LogbackAccessEventSource
import io.github.seijikohara.logback.access.value.LogbackAccessLocalPortStrategy
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.net.InetSocketAddress
import java.net.URLEncoder.encode
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableMap
import kotlin.text.Charsets.UTF_8

/**
 * The Logback-access event source for the Netty web server.
 *
 * @property logbackAccessContext The Logback-access context.
 * @property serverHttpRequest The server HTTP request.
 * @property serverHttpResponse The server HTTP response.
 * @property startTime The start time of the request.
 * @property responseContentLength The content length of the response body.
 * @property requestBody The captured request body.
 * @property responseBody The captured response body.
 */
@Suppress("LongParameterList")
class LogbackAccessNettyEventSource(
    private val logbackAccessContext: LogbackAccessContext,
    private val serverHttpRequest: ServerHttpRequest,
    private val serverHttpResponse: ServerHttpResponse,
    private val startTime: Long,
    private val responseContentLength: Long = 0L,
    private val requestBody: String? = null,
    private val responseBody: String? = null,
) : LogbackAccessEventSource() {

    /**
     * Netty does not use Servlet API, so this returns null.
     */
    override val request: HttpServletRequest? = null

    /**
     * Netty does not use Servlet API, so this returns null.
     */
    override val response: HttpServletResponse? = null

    /**
     * Netty does not use ServerAdapter, so this returns null.
     */
    override val serverAdapter: ServerAdapter? = null

    override val timeStamp: Long = currentTimeMillis()

    override val elapsedTime: Long = timeStamp - startTime

    override val sequenceNumber: Long? = logbackAccessContext.raw.sequenceNumberGenerator?.nextSequenceNumber()

    override val threadName: String = currentThread().name

    override val serverName: String? by lazy(LazyThreadSafetyMode.NONE) {
        serverHttpRequest.uri.host
    }

    override val localPort: Int by lazy(LazyThreadSafetyMode.NONE) {
        when (logbackAccessContext.properties.localPortStrategy) {
            LogbackAccessLocalPortStrategy.LOCAL -> serverHttpRequest.localAddress?.port ?: -1
            LogbackAccessLocalPortStrategy.SERVER -> serverHttpRequest.uri.port.takeIf { it > 0 } ?: DEFAULT_HTTP_PORT
        }
    }

    override val remoteAddr: String by lazy(LazyThreadSafetyMode.NONE) {
        serverHttpRequest.remoteAddress?.toResolvedIpAddress() ?: NA
    }

    override val remoteHost: String by lazy(LazyThreadSafetyMode.NONE) {
        serverHttpRequest.remoteAddress?.toResolvedIpAddress() ?: NA
    }

    override val remoteUser: String? by lazy(LazyThreadSafetyMode.NONE) {
        // Remote user is typically set via security filters
        // For Netty, we'll return null as there's no direct way to get it
        null
    }

    override val protocol: String by lazy(LazyThreadSafetyMode.NONE) {
        "HTTP/1.1" // Netty doesn't expose protocol version easily in ServerHttpRequest
    }

    override val method: String by lazy(LazyThreadSafetyMode.NONE) {
        serverHttpRequest.method.name()
    }

    override val requestURI: String? by lazy(LazyThreadSafetyMode.NONE) {
        serverHttpRequest.uri.rawPath
    }

    override val queryString: String by lazy(LazyThreadSafetyMode.NONE) {
        serverHttpRequest.uri.rawQuery?.let { "?$it" }.orEmpty()
    }

    override val requestURL: String by lazy(LazyThreadSafetyMode.NONE) {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        headers.putAll(serverHttpRequest.headers.toSingleValueMap())
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val cookies = linkedMapOf<String, String>()
        serverHttpRequest.cookies.forEach { (name, httpCookies) ->
            httpCookies.firstOrNull()?.let { cookies[name] = it.value }
        }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy(LazyThreadSafetyMode.NONE) {
        val params = linkedMapOf<String, List<String>>()
        serverHttpRequest.queryParams.forEach { (name, values) ->
            params[name] = unmodifiableList(values)
        }
        unmodifiableMap(params)
    }

    override val attributeMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        // Netty/WebFlux doesn't have request attributes in the same way as Servlet
        unmodifiableMap(emptyMap())
    }

    override val sessionID: String? by lazy(LazyThreadSafetyMode.NONE) {
        // Session handling in WebFlux is different from Servlet
        // Return null as there's no direct session ID access
        null
    }

    override val requestContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        requestBody
    }

    override val statusCode: Int by lazy(LazyThreadSafetyMode.NONE) {
        serverHttpResponse.statusCode?.value() ?: DEFAULT_STATUS_CODE
    }

    override val responseHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        headers.putAll(serverHttpResponse.headers.toSingleValueMap())
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy(LazyThreadSafetyMode.NONE) {
        // Use tracked bytes if available, otherwise fall back to Content-Length header
        responseContentLength.takeIf { it > 0 }
            ?: serverHttpResponse.headers.contentLength.takeIf { it >= 0 }
            ?: 0L
    }

    override val responseContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        responseBody
    }

    /**
     * Resolves the IP address from an InetSocketAddress.
     * If the address is already resolved, returns the host address.
     * If not resolved, resolves the hostname to get the IP address.
     */
    private fun InetSocketAddress.toResolvedIpAddress(): String {
        // If address is already resolved, return the IP address
        address?.let { return it.hostAddress }
        // If not resolved, try to resolve the hostname
        return try {
            java.net.InetAddress.getByName(hostString).hostAddress
        } catch (_: Exception) {
            hostString
        }
    }

    companion object {
        private const val NA = "-"
        private const val DEFAULT_HTTP_PORT = 80
        private const val DEFAULT_STATUS_CODE = 200
    }
}

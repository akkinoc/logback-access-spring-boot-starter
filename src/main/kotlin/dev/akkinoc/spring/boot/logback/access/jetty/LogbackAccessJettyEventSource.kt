package dev.akkinoc.spring.boot.logback.access.jetty

import ch.qos.logback.access.common.AccessConstants.LB_INPUT_BUFFER
import ch.qos.logback.access.common.AccessConstants.LB_OUTPUT_BUFFER
import ch.qos.logback.access.common.servlet.Util.isFormUrlEncoded
import ch.qos.logback.access.common.servlet.Util.isImageResponse
import ch.qos.logback.access.jetty.JettyModernServerAdapter
import ch.qos.logback.access.jetty.RequestWrapper
import ch.qos.logback.access.jetty.ResponseWrapper
import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import dev.akkinoc.spring.boot.logback.access.LogbackAccessHackyLoggingOverrides.overriddenRequestBody
import dev.akkinoc.spring.boot.logback.access.LogbackAccessHackyLoggingOverrides.overriddenResponseBody
import dev.akkinoc.spring.boot.logback.access.security.LogbackAccessSecurityServletFilter.Companion.REMOTE_USER_ATTRIBUTE
import dev.akkinoc.spring.boot.logback.access.value.LogbackAccessLocalPortStrategy
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import org.eclipse.jetty.session.DefaultSessionIdManager
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.net.URLEncoder.encode
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableMap
import kotlin.text.Charsets.UTF_8

/**
 * The Logback-access event source for the Jetty web server.
 *
 * @property logbackAccessContext The Logback-access context.
 * @property rawRequest The raw request.
 * @property rawResponse The raw response.
 * @see ch.qos.logback.access.common.spi.AccessEvent
 * @see ch.qos.logback.access.jetty.JettyServerAdapter
 * @see ch.qos.logback.access.common.PatternLayout
 * @see org.eclipse.jetty.server.CustomRequestLog
 */
class LogbackAccessJettyEventSource(
    private val logbackAccessContext: LogbackAccessContext,
    private val rawRequest: Request,
    private val rawResponse: Response,
) : LogbackAccessEventSource() {

    override val request: RequestWrapper = object : RequestWrapper(rawRequest) {
        override fun getContentType(): String? = rawRequest.headers[HttpHeader.CONTENT_TYPE]
    }

    override val response: ResponseWrapper = object : ResponseWrapper(rawResponse) {
        override fun getContentType(): String? = rawResponse.headers[HttpHeader.CONTENT_TYPE]
    }

    override val serverAdapter: JettyModernServerAdapter = JettyModernServerAdapter(rawRequest, rawResponse)

    override val timeStamp: Long = currentTimeMillis()

    override val elapsedTime: Long = timeStamp - Request.getTimeStamp(rawRequest)

    override val sequenceNumber: Long? = logbackAccessContext.raw.sequenceNumberGenerator?.nextSequenceNumber()

    override val threadName: String = currentThread().name

    override val serverName: String by lazy(LazyThreadSafetyMode.NONE) {
        Request.getServerName(rawRequest)
    }

    override val localPort: Int by lazy(LazyThreadSafetyMode.NONE) {
        when (logbackAccessContext.properties.localPortStrategy) {
            LogbackAccessLocalPortStrategy.LOCAL -> Request.getLocalPort(rawRequest)
            LogbackAccessLocalPortStrategy.SERVER -> Request.getServerPort(rawRequest)
        }
    }

    override val remoteAddr: String by lazy(LazyThreadSafetyMode.NONE) {
        Request.getRemoteAddr(rawRequest)
    }

    override val remoteHost: String by lazy(LazyThreadSafetyMode.NONE) {
        Request.getRemoteAddr(rawRequest)
    }

    override val remoteUser: String? by lazy(LazyThreadSafetyMode.NONE) {
        rawRequest.getAttribute(REMOTE_USER_ATTRIBUTE) as String?
    }

    override val protocol: String by lazy(LazyThreadSafetyMode.NONE) {
        rawRequest.connectionMetaData.protocol
    }

    override val method: String by lazy(LazyThreadSafetyMode.NONE) {
        rawRequest.method
    }

    override val requestURI: String? by lazy(LazyThreadSafetyMode.NONE) {
        rawRequest.httpURI.path
    }

    override val queryString: String by lazy(LazyThreadSafetyMode.NONE) {
        rawRequest.httpURI.query?.let { "?$it" }.orEmpty()
    }

    override val requestURL: String by lazy(LazyThreadSafetyMode.NONE) {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        rawRequest.headers.fieldNamesCollection.associateWithTo(headers) { rawRequest.headers[it] }
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val cookies = linkedMapOf<String, String>()
        Request.getCookies(rawRequest).associateTo(cookies) { it.name to it.value }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy(LazyThreadSafetyMode.NONE) {
        val params = linkedMapOf<String, List<String>>()
        Request.getParameters(rawRequest).associateTo(params) { it.name to unmodifiableList(it.values) }
        unmodifiableMap(params)
    }

    override val attributeMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val attrs = linkedMapOf<String, String>()
        rawRequest.attributeNameSet.asSequence()
            .filter { it !in setOf(LB_INPUT_BUFFER, LB_OUTPUT_BUFFER) }
            .associateWithTo(attrs) { "${rawRequest.getAttribute(it)}" }
        unmodifiableMap(attrs)
    }

    override val sessionID: String? by lazy(LazyThreadSafetyMode.NONE) {
        rawRequest.getAttribute(DefaultSessionIdManager.__NEW_SESSION_ID)?.toString()
    }

    override val requestContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        overriddenRequestBody(request)?.also { return@lazy it }
        val bytes = request.getAttribute(LB_INPUT_BUFFER) as ByteArray?
        if (bytes == null && isFormUrlEncoded(request)) {
            return@lazy requestParameterMap.asSequence()
                .flatMap { (key, values) -> values.asSequence().map { key to it } }
                .map { (key, value) -> encode(key, UTF_8.name()) to encode(value, UTF_8.name()) }
                .joinToString("&") { (key, value) -> "$key=$value" }
        }
        bytes?.let { String(it, UTF_8) }
    }

    override val statusCode: Int by lazy(LazyThreadSafetyMode.NONE) {
        rawResponse.status
    }

    override val responseHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        rawResponse.headers.fieldNamesCollection.associateWithTo(headers) { rawResponse.headers[it] }
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy(LazyThreadSafetyMode.NONE) {
        Response.getContentBytesWritten(rawResponse)
    }

    override val responseContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        overriddenResponseBody(request, response)?.also { return@lazy it }
        if (isImageResponse(response)) return@lazy "[IMAGE CONTENTS SUPPRESSED]"
        val bytes = request.getAttribute(LB_OUTPUT_BUFFER) as ByteArray?
        bytes?.let { String(it, UTF_8) }
    }

}

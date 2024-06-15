package dev.akkinoc.spring.boot.logback.access.tomcat

import ch.qos.logback.access.common.AccessConstants.LB_INPUT_BUFFER
import ch.qos.logback.access.common.AccessConstants.LB_OUTPUT_BUFFER
import ch.qos.logback.access.common.servlet.Util.isFormUrlEncoded
import ch.qos.logback.access.common.servlet.Util.isImageResponse
import ch.qos.logback.access.tomcat.TomcatServerAdapter
import dev.akkinoc.spring.boot.logback.access.LogbackAccessContext
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import dev.akkinoc.spring.boot.logback.access.LogbackAccessHackyLoggingOverrides.overriddenRequestBody
import dev.akkinoc.spring.boot.logback.access.LogbackAccessHackyLoggingOverrides.overriddenResponseBody
import dev.akkinoc.spring.boot.logback.access.security.LogbackAccessSecurityServletFilter.Companion.REMOTE_USER_ATTRIBUTE
import dev.akkinoc.spring.boot.logback.access.value.LogbackAccessLocalPortStrategy
import org.apache.catalina.AccessLog.PROTOCOL_ATTRIBUTE
import org.apache.catalina.AccessLog.REMOTE_ADDR_ATTRIBUTE
import org.apache.catalina.AccessLog.REMOTE_HOST_ATTRIBUTE
import org.apache.catalina.AccessLog.SERVER_NAME_ATTRIBUTE
import org.apache.catalina.AccessLog.SERVER_PORT_ATTRIBUTE
import org.apache.catalina.connector.Request
import org.apache.catalina.connector.Response
import org.apache.catalina.valves.RemoteIpValve
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.net.URLEncoder.encode
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableMap
import kotlin.text.Charsets.UTF_8

/**
 * The Logback-access event source for the Tomcat web server.
 *
 * @property logbackAccessContext The Logback-access context.
 * @property requestAttributesEnabled Whether to enable the request attributes to work with [RemoteIpValve].
 * @see ch.qos.logback.access.common.spi.AccessEvent
 * @see ch.qos.logback.access.tomcat.TomcatServerAdapter
 * @see ch.qos.logback.access.common.PatternLayout
 * @see org.apache.catalina.valves.AccessLogValve
 * @see org.apache.catalina.valves.AbstractAccessLogValve
 * @see org.apache.catalina.valves.AbstractAccessLogValve.AccessLogElement
 */
class LogbackAccessTomcatEventSource(
    private val logbackAccessContext: LogbackAccessContext,
    private val requestAttributesEnabled: Boolean,
    override val request: Request,
    override val response: Response,
) : LogbackAccessEventSource() {

    override val serverAdapter: TomcatServerAdapter = TomcatServerAdapter(request, response)

    override val timeStamp: Long = currentTimeMillis()

    override val elapsedTime: Long = timeStamp - request.coyoteRequest.startTime

    override val sequenceNumber: Long? = logbackAccessContext.raw.sequenceNumberGenerator?.nextSequenceNumber()

    override val threadName: String = currentThread().name

    override val serverName: String by lazy(LazyThreadSafetyMode.NONE) {
        request.getAccessLogAttribute(SERVER_NAME_ATTRIBUTE) ?: request.serverName
    }

    override val localPort: Int by lazy(LazyThreadSafetyMode.NONE) {
        when (logbackAccessContext.properties.localPortStrategy) {
            LogbackAccessLocalPortStrategy.LOCAL -> request.localPort
            LogbackAccessLocalPortStrategy.SERVER ->
                request.getAccessLogAttribute(SERVER_PORT_ATTRIBUTE) ?: request.serverPort
        }
    }

    override val remoteAddr: String by lazy(LazyThreadSafetyMode.NONE) {
        request.getAccessLogAttribute(REMOTE_ADDR_ATTRIBUTE) ?: request.remoteAddr
    }

    override val remoteHost: String by lazy(LazyThreadSafetyMode.NONE) {
        request.getAccessLogAttribute(REMOTE_HOST_ATTRIBUTE) ?: request.remoteHost
    }

    override val remoteUser: String? by lazy(LazyThreadSafetyMode.NONE) {
        request.getAttribute(REMOTE_USER_ATTRIBUTE) as String? ?: request.remoteUser
    }

    override val protocol: String by lazy(LazyThreadSafetyMode.NONE) {
        request.getAccessLogAttribute(PROTOCOL_ATTRIBUTE) ?: request.protocol
    }

    override val method: String by lazy(LazyThreadSafetyMode.NONE) {
        request.method
    }

    override val requestURI: String? by lazy(LazyThreadSafetyMode.NONE) {
        request.requestURI
    }

    override val queryString: String by lazy(LazyThreadSafetyMode.NONE) {
        request.queryString?.let { "?$it" }.orEmpty()
    }

    override val requestURL: String by lazy(LazyThreadSafetyMode.NONE) {
        "$method $requestURI$queryString $protocol"
    }

    override val requestHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        request.headerNames.asSequence().associateWithTo(headers) { request.getHeader(it) }
        unmodifiableMap(headers)
    }

    override val cookieMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val cookies = linkedMapOf<String, String>()
        request.cookies.orEmpty().associateTo(cookies) { it.name to it.value }
        unmodifiableMap(cookies)
    }

    override val requestParameterMap: Map<String, List<String>> by lazy(LazyThreadSafetyMode.NONE) {
        val params = linkedMapOf<String, List<String>>()
        request.parameterMap.mapValuesTo(params) { unmodifiableList(it.value.asList()) }
        unmodifiableMap(params)
    }

    override val attributeMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val attrs = linkedMapOf<String, String>()
        request.attributeNames.asSequence()
            .filter { it !in setOf(LB_INPUT_BUFFER, LB_OUTPUT_BUFFER) }
            .associateWithTo(attrs) { "${request.getAttribute(it)}" }
        unmodifiableMap(attrs)
    }

    override val sessionID: String? by lazy(LazyThreadSafetyMode.NONE) {
        request.getSession(false)?.id
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
        response.status
    }

    override val responseHeaderMap: Map<String, String> by lazy(LazyThreadSafetyMode.NONE) {
        val headers = sortedMapOf<String, String>(String.CASE_INSENSITIVE_ORDER)
        response.headerNames.associateWithTo(headers) { response.getHeader(it) }
        unmodifiableMap(headers)
    }

    override val contentLength: Long by lazy(LazyThreadSafetyMode.NONE) {
        response.getBytesWritten(false)
    }

    override val responseContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        overriddenResponseBody(request, response)?.also { return@lazy it }
        if (isImageResponse(response)) return@lazy "[IMAGE CONTENTS SUPPRESSED]"
        val bytes = request.getAttribute(LB_OUTPUT_BUFFER) as ByteArray?
        bytes?.let { String(it, UTF_8) }
    }

    /**
     * Gets the request attribute to work with [RemoteIpValve].
     *
     * @receiver The request.
     * @param <T> The request attribute type.
     * @param name The request attribute name.
     * @return The request attribute value.
     */
    private inline fun <reified T> Request.getAccessLogAttribute(name: String): T? {
        return if (requestAttributesEnabled) getAttribute(name) as T? else null
    }

}

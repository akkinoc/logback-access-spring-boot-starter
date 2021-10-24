package dev.akkinoc.spring.boot.logback.access.jetty

import ch.qos.logback.access.AccessConstants.LB_INPUT_BUFFER
import ch.qos.logback.access.AccessConstants.LB_OUTPUT_BUFFER
import ch.qos.logback.access.jetty.JettyServerAdapter
import ch.qos.logback.access.servlet.Util.isFormUrlEncoded
import ch.qos.logback.access.servlet.Util.isImageResponse
import dev.akkinoc.spring.boot.logback.access.LogbackAccessEventSource
import dev.akkinoc.spring.boot.logback.access.security.LogbackAccessSecurityServletFilter.Companion.REMOTE_USER_ATTRIBUTE
import dev.akkinoc.spring.boot.logback.access.value.LogbackAccessLocalPortStrategy
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.net.URLEncoder.encode
import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableMap
import kotlin.text.Charsets.UTF_8

/**
 * The Logback-access event source for the Jetty web server.
 *
 * @property localPortStrategy The strategy to change the behavior of [localPort].
 * @see ch.qos.logback.access.spi.AccessEvent
 * @see ch.qos.logback.access.jetty.JettyServerAdapter
 * @see ch.qos.logback.access.PatternLayout
 * @see org.eclipse.jetty.server.CustomRequestLog
 */
class LogbackAccessJettyEventSource(
        override val request: Request,
        override val response: Response,
        private val localPortStrategy: LogbackAccessLocalPortStrategy,
) : LogbackAccessEventSource() {

    override val serverAdapter: JettyServerAdapter = JettyServerAdapter(request, response)

    override val timeStamp: Long = currentTimeMillis()

    override val elapsedTime: Long = timeStamp - request.timeStamp

    override val threadName: String = currentThread().name

    override val serverName: String by lazy(LazyThreadSafetyMode.NONE) {
        request.serverName
    }

    override val localPort: Int by lazy(LazyThreadSafetyMode.NONE) {
        when (localPortStrategy) {
            LogbackAccessLocalPortStrategy.LOCAL -> request.localPort
            LogbackAccessLocalPortStrategy.SERVER -> request.serverPort
        }
    }

    override val remoteAddr: String by lazy(LazyThreadSafetyMode.NONE) {
        request.remoteAddr
    }

    override val remoteHost: String by lazy(LazyThreadSafetyMode.NONE) {
        request.remoteHost
    }

    override val remoteUser: String? by lazy(LazyThreadSafetyMode.NONE) {
        request.getAttribute(REMOTE_USER_ATTRIBUTE) as String? ?: request.remoteUser
    }

    override val protocol: String by lazy(LazyThreadSafetyMode.NONE) {
        request.protocol
    }

    override val method: String by lazy(LazyThreadSafetyMode.NONE) {
        request.method
    }

    override val requestURI: String by lazy(LazyThreadSafetyMode.NONE) {
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
        response.contentCount
    }

    override val responseContent: String? by lazy(LazyThreadSafetyMode.NONE) {
        if (isImageResponse(response)) return@lazy "[IMAGE CONTENTS SUPPRESSED]"
        val bytes = request.getAttribute(LB_OUTPUT_BUFFER) as ByteArray?
        bytes?.let { String(it, UTF_8) }
    }

}

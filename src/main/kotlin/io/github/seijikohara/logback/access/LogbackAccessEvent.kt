package io.github.seijikohara.logback.access

import ch.qos.logback.access.common.spi.IAccessEvent
import ch.qos.logback.access.common.spi.IAccessEvent.NA
import ch.qos.logback.access.common.spi.IAccessEvent.SENTINEL
import ch.qos.logback.access.common.spi.ServerAdapter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.Collections.enumeration
import java.util.Enumeration
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * The Logback-access event.
 *
 * @property source The Logback-access event source.
 * @see ch.qos.logback.access.common.spi.AccessEvent
 */
class LogbackAccessEvent(private var source: LogbackAccessEventSource) :
    IAccessEvent,
    Serializable {

    override fun getRequest(): HttpServletRequest? = source.request

    override fun getResponse(): HttpServletResponse? = source.response

    override fun getServerAdapter(): ServerAdapter? = source.serverAdapter

    override fun getTimeStamp(): Long = source.timeStamp

    override fun getElapsedTime(): Long = source.elapsedTime ?: SENTINEL.toLong()

    override fun getElapsedSeconds(): Long {
        val millis = source.elapsedTime ?: return SENTINEL.toLong()
        return MILLISECONDS.toSeconds(millis)
    }

    override fun getSequenceNumber(): Long = source.sequenceNumber ?: 0L

    override fun getThreadName(): String = source.threadName

    override fun setThreadName(value: String): Unit = throw UnsupportedOperationException("Cannot change: $this")

    override fun getServerName(): String = source.serverName ?: NA

    override fun getLocalPort(): Int = source.localPort

    override fun getRemoteAddr(): String = source.remoteAddr

    override fun getRemoteHost(): String = source.remoteHost

    override fun getRemoteUser(): String = source.remoteUser ?: NA

    override fun getProtocol(): String = source.protocol

    override fun getMethod(): String = source.method

    override fun getRequestURI(): String = source.requestURI ?: NA

    override fun getQueryString(): String = source.queryString

    override fun getRequestURL(): String = source.requestURL

    override fun getRequestHeaderMap(): Map<String, String> = source.requestHeaderMap

    override fun getRequestHeaderNames(): Enumeration<String> = enumeration(source.requestHeaderMap.keys)

    override fun getRequestHeader(key: String): String = source.requestHeaderMap[key] ?: NA

    override fun getCookie(key: String): String = source.cookieMap[key] ?: NA

    override fun getRequestParameterMap(): Map<String, Array<String>> = source.requestParameterMap.mapValues {
        it.value.toTypedArray()
    }

    override fun getRequestParameter(key: String): Array<String> {
        val values = source.requestParameterMap[key] ?: return arrayOf(NA)
        return values.toTypedArray()
    }

    override fun getAttribute(key: String): String = source.attributeMap[key] ?: NA

    override fun getSessionID(): String = source.sessionID ?: NA

    override fun getRequestContent(): String = source.requestContent.orEmpty()

    override fun getStatusCode(): Int = source.statusCode

    override fun getResponseHeaderMap(): Map<String, String> = source.responseHeaderMap

    override fun getResponseHeaderNameList(): List<String> = source.responseHeaderMap.keys.toList()

    override fun getResponseHeader(key: String): String = source.responseHeaderMap[key] ?: NA

    override fun getContentLength(): Long = source.contentLength

    override fun getResponseContent(): String = source.responseContent.orEmpty()

    override fun prepareForDeferredProcessing() {
        source = source.fix()
    }

    override fun toString(): String = "${this::class.simpleName}($requestURL $statusCode)"

    /**
     * @see java.io.Serializable
     */
    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        prepareForDeferredProcessing()
        out.defaultWriteObject()
    }
}

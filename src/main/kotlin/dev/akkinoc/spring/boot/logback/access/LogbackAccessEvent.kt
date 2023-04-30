package dev.akkinoc.spring.boot.logback.access

import ch.qos.logback.access.spi.IAccessEvent
import ch.qos.logback.access.spi.IAccessEvent.NA
import ch.qos.logback.access.spi.IAccessEvent.SENTINEL
import ch.qos.logback.access.spi.ServerAdapter
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
 * @see ch.qos.logback.access.spi.AccessEvent
 */
class LogbackAccessEvent(private var source: LogbackAccessEventSource) : IAccessEvent, Serializable {

    override fun getRequest(): HttpServletRequest? {
        return source.request
    }

    override fun getResponse(): HttpServletResponse? {
        return source.response
    }

    override fun getServerAdapter(): ServerAdapter? {
        return source.serverAdapter
    }

    override fun getTimeStamp(): Long {
        return source.timeStamp
    }

    override fun getElapsedTime(): Long {
        return source.elapsedTime ?: SENTINEL.toLong()
    }

    override fun getElapsedSeconds(): Long {
        val millis = source.elapsedTime ?: return SENTINEL.toLong()
        return MILLISECONDS.toSeconds(millis)
    }

    override fun getSequenceNumber(): Long {
        return source.sequenceNumber ?: 0L
    }

    override fun getThreadName(): String {
        return source.threadName
    }

    override fun setThreadName(value: String) {
        throw UnsupportedOperationException("Cannot change: $this")
    }

    override fun getServerName(): String {
        return source.serverName
    }

    override fun getLocalPort(): Int {
        return source.localPort
    }

    override fun getRemoteAddr(): String {
        return source.remoteAddr
    }

    override fun getRemoteHost(): String {
        return source.remoteHost
    }

    override fun getRemoteUser(): String {
        return source.remoteUser ?: NA
    }

    override fun getProtocol(): String {
        return source.protocol
    }

    override fun getMethod(): String {
        return source.method
    }

    override fun getRequestURI(): String {
        return source.requestURI ?: NA
    }

    override fun getQueryString(): String {
        return source.queryString
    }

    override fun getRequestURL(): String {
        return source.requestURL
    }

    override fun getRequestHeaderMap(): Map<String, String> {
        return source.requestHeaderMap
    }

    override fun getRequestHeaderNames(): Enumeration<String> {
        return enumeration(source.requestHeaderMap.keys)
    }

    override fun getRequestHeader(key: String): String {
        return source.requestHeaderMap[key] ?: NA
    }

    override fun getCookie(key: String): String {
        return source.cookieMap[key] ?: NA
    }

    override fun getRequestParameterMap(): Map<String, Array<String>> {
        return source.requestParameterMap.mapValues { it.value.toTypedArray() }
    }

    override fun getRequestParameter(key: String): Array<String> {
        val values = source.requestParameterMap[key] ?: return arrayOf(NA)
        return values.toTypedArray()
    }

    override fun getAttribute(key: String): String {
        return source.attributeMap[key] ?: NA
    }

    override fun getSessionID(): String {
        return source.sessionID ?: NA
    }

    override fun getRequestContent(): String {
        return source.requestContent.orEmpty()
    }

    override fun getStatusCode(): Int {
        return source.statusCode
    }

    override fun getResponseHeaderMap(): Map<String, String> {
        return source.responseHeaderMap
    }

    override fun getResponseHeaderNameList(): List<String> {
        return source.responseHeaderMap.keys.toList()
    }

    override fun getResponseHeader(key: String): String {
        return source.responseHeaderMap[key] ?: NA
    }

    override fun getContentLength(): Long {
        return source.contentLength
    }

    override fun getResponseContent(): String {
        return source.responseContent.orEmpty()
    }

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

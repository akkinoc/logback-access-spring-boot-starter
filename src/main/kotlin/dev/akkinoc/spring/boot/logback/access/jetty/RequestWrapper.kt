package dev.akkinoc.spring.boot.logback.access.jetty

import ch.qos.logback.access.common.spi.IAccessEvent.NA
import ch.qos.logback.access.common.spi.WrappedHttpRequest
import jakarta.servlet.AsyncContext
import jakarta.servlet.DispatcherType
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.ServletConnection
import jakarta.servlet.ServletContext
import jakarta.servlet.ServletException
import jakarta.servlet.ServletInputStream
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import jakarta.servlet.http.HttpUpgradeHandler
import jakarta.servlet.http.Part
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpScheme
import org.eclipse.jetty.http.HttpURI
import org.eclipse.jetty.http.HttpVersion
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Session
import java.io.BufferedReader
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.Principal
import java.util.Collections
import java.util.Enumeration
import java.util.Locale
import java.util.TreeMap

/**
 * Request wrapper that implements Servlet API.
 */
class RequestWrapper(private val request: Request) : HttpServletRequest, WrappedHttpRequest {
    private var requestURL: StringBuffer? = null

    override fun getAuthType(): String? {
        return null
    }

    override fun getCookies(): Array<Cookie?> {
        return arrayOfNulls(0)
    }

    override fun getDateHeader(name: String?): Long {
        return 0
    }

    override fun getHeader(name: String?): String? {
        return null
    }

    override fun getHeaders(name: String?): Enumeration<String>? {
        return null
    }

    override fun getHeaderNames(): Enumeration<String>? {
        return Collections.enumeration(request.headers.fieldNamesCollection)
    }

    override fun buildRequestHeaderMap(): Map<String, String> {
        val requestHeaderMap: MutableMap<String, String> = TreeMap(java.lang.String.CASE_INSENSITIVE_ORDER)
        request.headers.associateByTo(requestHeaderMap, { it.name }, { it.value })
        return requestHeaderMap
    }

    override fun getIntHeader(name: String?): Int {
        return 0
    }

    override fun getMethod(): String {
        return request.method
    }

    override fun getPathInfo(): String? {
        return null
    }

    override fun getPathTranslated(): String? {
        return null
    }

    override fun getContextPath(): String? {
        return null
    }

    override fun getQueryString(): String {
        return request.httpURI.query
    }

    override fun getRemoteUser(): String? {
        return null
    }

    override fun isUserInRole(role: String?): Boolean {
        return false
    }

    override fun getUserPrincipal(): Principal? {
        return null
    }

    override fun getRequestedSessionId(): String? {
        return null
    }

    override fun getRequestURI(): String {
        val mutable: HttpURI.Mutable = HttpURI.build(request.httpURI)
        mutable.query(null)
        return mutable.asString()
    }

    override fun getRequestURL(): StringBuffer {
        if (requestURL == null) {
            val result: String = request.httpURI.asString()
            requestURL = StringBuffer(result)
        }
        return requestURL!!
    }

    override fun getServletPath(): String? {
        return null
    }

    override fun getSessionID(): String {
        val session: Session = request.getSession(false)
        return if (session == null) {
            NA
        } else {
            session.id
        }
    }

    override fun getSession(create: Boolean): HttpSession {
        throw UnsupportedOperationException()
    }

    override fun getSession(): HttpSession {
        throw UnsupportedOperationException()
    }

    override fun changeSessionId(): String {
        throw UnsupportedOperationException()
    }

    override fun isRequestedSessionIdValid(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isRequestedSessionIdFromCookie(): Boolean {
        return false
    }

    override fun isRequestedSessionIdFromURL(): Boolean {
        throw UnsupportedOperationException()
    }

    @Throws(IOException::class, ServletException::class)
    override fun authenticate(response: HttpServletResponse?): Boolean {
        return false
    }

    @Throws(ServletException::class)
    override fun login(username: String?, password: String?) = Unit

    @Throws(ServletException::class)
    override fun logout() = Unit

    @Throws(IOException::class, ServletException::class)
    override fun getParts(): Collection<Part>? {
        return null
    }

    @Throws(IOException::class, ServletException::class)
    override fun getPart(name: String?): Part? {
        return null
    }

    @Throws(IOException::class, ServletException::class)
    override fun <T : HttpUpgradeHandler?> upgrade(handlerClass: Class<T>?): T? {
        return null
    }

    override fun getAttribute(name: String?): Any {
        return request.getAttribute(name)
    }

    override fun getAttributeNames(): Enumeration<String> {
        val attributeNamesSet: Set<String> = request.attributeNameSet
        return Collections.enumeration(attributeNamesSet)
    }

    override fun getCharacterEncoding(): String? {
        return null
    }

    @Throws(UnsupportedEncodingException::class)
    override fun setCharacterEncoding(env: String?) = Unit

    override fun getContentLength(): Int {
        return 0
    }

    override fun getContentLengthLong(): Long {
        return 0L
    }

    override fun getContentType(): String? {
        return request.headers[HttpHeader.CONTENT_TYPE]
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream? {
        return null
    }

    override fun buildRequestParameterMap(): Map<String, Array<String>> {
        val results: MutableMap<String, Array<String>> = HashMap()
        val allParameters = Request.extractQueryParameters(request, StandardCharsets.UTF_8)
        allParameters.associateByTo(results, { it.name }, { it.values.toTypedArray() })
        return results
    }

    override fun getParameter(name: String?): String {
        throw UnsupportedOperationException()
    }

    override fun getParameterNames(): Enumeration<String> {
        throw UnsupportedOperationException()
    }

    override fun getParameterValues(name: String?): Array<String> {
        throw UnsupportedOperationException()
    }

    override fun getParameterMap(): Map<String, Array<String>> {
        throw UnsupportedOperationException()
    }

    override fun getProtocol(): String {
        return request.connectionMetaData.protocol
    }

    override fun getScheme(): String {
        return request.httpURI.scheme
    }

    override fun getServerName(): String {
        return Request.getServerName(request)
    }

    override fun getServerPort(): Int {
        return Request.getServerPort(request)
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader? {
        return null
    }

    override fun getRemoteAddr(): String {
        return Request.getRemoteAddr(request)
    }

    override fun getRemoteHost(): String {
        return Request.getRemoteAddr(request)
    }

    override fun setAttribute(name: String?, o: Any?) = Unit

    override fun removeAttribute(name: String?) = Unit

    override fun getLocale(): Locale {
        return Request.getLocales(request)[0]
    }

    override fun getLocales(): Enumeration<Locale> {
        return Collections.enumeration(Request.getLocales(request))
    }

    override fun isSecure(): Boolean {
        return HttpScheme.HTTPS.`is`(request.httpURI.scheme)
    }

    override fun getRequestDispatcher(path: String?): RequestDispatcher? {
        return null
    }

    override fun getRemotePort(): Int {
        return 0
    }

    override fun getLocalName(): String? {
        return null
    }

    override fun getLocalAddr(): String? {
        return null
    }

    override fun getLocalPort(): Int {
        return 0
    }

    override fun getServletContext(): ServletContext? {
        return null
    }

    @Throws(IllegalStateException::class)
    override fun startAsync(): AsyncContext? {
        return null
    }

    @Throws(IllegalStateException::class)
    override fun startAsync(servletRequest: ServletRequest?, servletResponse: ServletResponse?): AsyncContext? {
        return null
    }

    override fun isAsyncStarted(): Boolean {
        return false
    }

    override fun isAsyncSupported(): Boolean {
        return false
    }

    override fun getAsyncContext(): AsyncContext? {
        return null
    }

    override fun getDispatcherType(): DispatcherType? {
        return null
    }

    override fun getRequestId(): String {
        return request.connectionMetaData.id + "#" + request.id
    }

    override fun getProtocolRequestId(): String {
        val httpVersion: HttpVersion = request.connectionMetaData.httpVersion
        return if (httpVersion === HttpVersion.HTTP_2 || httpVersion === (HttpVersion.HTTP_3)) {
            request.id
        } else {
            NA
        }
    }

    override fun getServletConnection(): ServletConnection? {
        return null
    }
}

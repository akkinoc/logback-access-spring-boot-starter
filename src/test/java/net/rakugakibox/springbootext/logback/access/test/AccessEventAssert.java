package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import static ch.qos.logback.access.spi.IAccessEvent.NA;
import static ch.qos.logback.access.spi.IAccessEvent.SENTINEL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 * The assertion of {@link IAccessEvent}.
 */
public class AccessEventAssert<S extends AccessEventAssert<S, A>, A extends IAccessEvent> extends AbstractAssert<S, A> {

    /**
     * Constructs an instance.
     *
     * @param actual the actual event.
     */
    protected AccessEventAssert(A actual) {
        super(actual, AccessEventAssert.class);
    }

    /**
     * Constructs an instance.
     *
     * @param actual the actual event.
     * @param selfClass the self class.
     */
    protected AccessEventAssert(A actual, Class<?> selfClass) {
        super(actual, selfClass);
    }

    /**
     * Verifies that the timestamp is in given range.
     *
     * @param start the start value of range (inclusive).
     * @param end the end value of range (inclusive).
     * @return this instance.
     */
    public S hasTimestamp(LocalDateTime start, LocalDateTime end) {
        long actualTimestampAsLong = actual.getTimeStamp();
        Assertions.assertThat(actualTimestampAsLong)
                .isNotEqualTo(SENTINEL);
        LocalDateTime actualTimestamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(actualTimestampAsLong), ZoneId.systemDefault());
        Assertions.assertThat(actualTimestamp)
                .isAfterOrEqualTo(start)
                .isBeforeOrEqualTo(end);
        return myself;
    }

    /**
     * Verifies that the server name is equal to the given one.
     *
     * @param serverName the server name.
     * @return this instance.
     */
    public S hasServerName(String serverName) {
        String actualServerName = actual.getServerName();
        Assertions.assertThat(actualServerName)
                .isNotEqualTo(NA)
                .isEqualTo(serverName);
        return myself;
    }

    /**
     * Verifies that the local port is equal to the given one.
     *
     * @param localPort the local port.
     * @return this instance.
     */
    public S hasLocalPort(int localPort) {
        int actualLocalPort = actual.getLocalPort();
        Assertions.assertThat(actualLocalPort)
                .isNotEqualTo(SENTINEL)
                .isEqualTo(localPort);
        return myself;
    }

    /**
     * Verifies that the remote address is equal to the given one.
     *
     * @param remoteAddr the remote address.
     * @return this instance.
     */
    public S hasRemoteAddr(String remoteAddr) {
        String actualRemoteAddr = actual.getRemoteAddr();
        Assertions.assertThat(actualRemoteAddr)
                .isNotEqualTo(NA)
                .isEqualTo(remoteAddr);
        return myself;
    }

    /**
     * Verifies that the remote host is equal to the given one.
     *
     * @param remoteHost the remote host.
     * @return this instance.
     */
    public S hasRemoteHost(String remoteHost) {
        String actualRemoteHost = actual.getRemoteHost();
        Assertions.assertThat(actualRemoteHost)
                .isNotEqualTo(NA)
                .isEqualTo(remoteHost);
        return myself;
    }

    /**
     * Verifies that the remote user is equal to the given one.
     *
     * @param remoteUser the remote user.
     * @return this instance.
     */
    public S hasRemoteUser(String remoteUser) {
        String actualRemoteUser = actual.getRemoteUser();
        Assertions.assertThat(actualRemoteUser)
                .isNotEqualTo(NA)
                .isEqualTo(remoteUser);
        return myself;
    }

    /**
     * Verifies that the protocol is equal to the given one.
     *
     * @param protocol the protocol.
     * @return this instance.
     */
    public S hasProtocol(String protocol) {
        String actualProtocol = actual.getProtocol();
        Assertions.assertThat(actualProtocol)
                .isNotEqualTo(NA)
                .isEqualTo(protocol);
        return myself;
    }

    /**
     * Verifies that the method is equal to the given one.
     *
     * @param method the method.
     * @return this instance.
     */
    public S hasMethod(HttpMethod method) {
        String actualMethod = actual.getMethod();
        Assertions.assertThat(actualMethod)
                .isNotEqualTo(NA)
                .isEqualTo(method.name());
        return myself;
    }

    /**
     * Verifies that the request URI is equal to the given one.
     *
     * @param requestUri the request URI.
     * @return this instance.
     */
    public S hasRequestUri(String requestUri) {
        String actualRequestUri = actual.getRequestURI();
        Assertions.assertThat(actualRequestUri)
                .isNotEqualTo(NA)
                .isEqualTo(requestUri);
        return myself;
    }

    /**
     * Verifies that the query string is equal to the given one.
     *
     * @param query the query string.
     * @return this instance.
     */
    public S hasQueryString(String query) {
        String actualQueryString = actual.getQueryString();
        Assertions.assertThat(actualQueryString)
                .isNotEqualTo(NA)
                .isEqualTo(query);
        return myself;
    }

    /**
     * Verifies that the request URL (first line of the request) is equal to the given one.
     *
     * @param method the method.
     * @param requestUri the request URI.
     * @param protocol the protocol.
     * @return this instance.
     */
    public S hasRequestUrl(HttpMethod method, String requestUri, String protocol) {
        String actualRequestUrl = actual.getRequestURL();
        Assertions.assertThat(actualRequestUrl)
                .isNotEqualTo(NA)
                .isEqualTo(method.name() + " " + requestUri + " " + protocol);
        return myself;
    }

    /**
     * Verifies that the request header is equal to the given one.
     *
     * @param name the request header name.
     * @param value the request header value.
     * @return this instance.
     */
    public S hasRequestHeader(String name, String value) {
        Map<String, String> actualRequestHeaderMap = actual.getRequestHeaderMap();
        Assertions.assertThat(actualRequestHeaderMap)
                .containsEntry(name, value);
        return myself;
    }

    /**
     * Verifies that the request parameter is equal to the given one.
     *
     * @param name the request parameter name.
     * @param values the request parameter values.
     * @return this instance.
     */
    public S hasRequestParameter(String name, String... values) {
        Map<String, String[]> actualRequestParameterMap = actual.getRequestParameterMap();
        Assertions.assertThat(actualRequestParameterMap)
                .containsEntry(name, values);
        return myself;
    }

    /**
     * Verifies that the status code is equal to the given one.
     *
     * @param status the status.
     * @return this instance.
     */
    public S hasStatusCode(HttpStatus status) {
        int actualStatusCode = actual.getStatusCode();
        Assertions.assertThat(actualStatusCode)
                .isNotEqualTo(SENTINEL)
                .isEqualTo(status.value());
        return myself;
    }

    /**
     * Verifies that the content length is greater than or equal to the given one.
     *
     * @param contentLength the minimal content length.
     * @return this instance.
     */
    public S hasContentLength(long contentLength) {
        long actualContentLength = actual.getContentLength();
        Assertions.assertThat(actualContentLength)
                .isNotEqualTo(SENTINEL)
                .isGreaterThanOrEqualTo(contentLength);
        return myself;
    }

    /**
     * Verifies that the response header is equal to the given one.
     *
     * @param name the response header name.
     * @param value the response header value.
     * @return this instance.
     */
    public S hasResponseHeader(String name, String value) {
        Map<String, String> actualResponseHeaderMap = actual.getResponseHeaderMap();
        Assertions.assertThat(actualResponseHeaderMap)
                .containsEntry(name, value);
        return myself;
    }

    /**
     * Verifies that the elapsed time is in given range.
     *
     * @param start the start value of range (inclusive).
     * @param end the end value of range (exclusive).
     * @return this instance.
     */
    public S hasElapsedTime(LocalDateTime start, LocalDateTime end) {
        long actualElapsedTimeAsLong = actual.getElapsedTime();
        Assertions.assertThat(actualElapsedTimeAsLong)
                .isNotEqualTo(SENTINEL);
        Duration actualElapsedSeconds = Duration.ofMillis(actualElapsedTimeAsLong);
        Assertions.assertThat(actualElapsedSeconds)
                .isGreaterThanOrEqualTo(Duration.ofMillis(0L))
                .isLessThanOrEqualTo(Duration.between(start, end));
        return myself;
    }

    /**
     * Verifies that the elapsed seconds is in given range.
     *
     * @param start the start value of range (inclusive).
     * @param end the end value of range (exclusive).
     * @return this instance.
     */
    public S hasElapsedSeconds(LocalDateTime start, LocalDateTime end) {
        long actualElapsedSecondsAsLong = actual.getElapsedSeconds();
        Assertions.assertThat(actualElapsedSecondsAsLong)
                .isNotEqualTo(SENTINEL);
        Duration actualElapsedSeconds = Duration.ofSeconds(actualElapsedSecondsAsLong);
        Assertions.assertThat(actualElapsedSeconds)
                .isGreaterThanOrEqualTo(Duration.ofSeconds(0L))
                .isLessThanOrEqualTo(Duration.between(start, end));
        return myself;
    }

    /**
     * Verifies that the thread name is not empty.
     *
     * @return this instance.
     */
    public S hasThreadName() {
        String actualThreadName = actual.getThreadName();
        Assertions.assertThat(actualThreadName)
                .isNotEqualTo(IAccessEvent.NA)
                .isNotEmpty();
        return myself;
    }

    /**
     * Starts the assertion.
     *
     * @param <A> the actual event type.
     * @param actual the actual event.
     * @return an assertion instance.
     */
    public static <A extends IAccessEvent> AccessEventAssert<?, A> assertThat(A actual) {
        return new AccessEventAssert<>(actual);
    }

}

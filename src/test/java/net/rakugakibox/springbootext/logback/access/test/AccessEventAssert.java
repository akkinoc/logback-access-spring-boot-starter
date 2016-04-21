package net.rakugakibox.springbootext.logback.access.test;

import ch.qos.logback.access.spi.IAccessEvent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        LocalDateTime timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(actual.getTimeStamp()), ZoneId.systemDefault());
        Assertions.assertThat(timestamp).isAfterOrEqualTo(start).isBeforeOrEqualTo(end);
        return myself;
    }

    /**
     * Verifies that the protocol is equal to the given one.
     *
     * @param expected the expected protocol.
     * @return this instance.
     */
    public S hasProtocol(String expected) {
        Assertions.assertThat(actual.getProtocol()).isEqualTo(expected);
        return myself;
    }

    /**
     * Verifies that the method is equal to the given one.
     *
     * @param expected the expected method.
     * @return this instance.
     */
    public S hasMethod(HttpMethod expected) {
        Assertions.assertThat(actual.getMethod()).isEqualTo(expected.name());
        return myself;
    }

    /**
     * Verifies that the request URI is equal to the given one.
     *
     * @param expected the expected request URI.
     * @return this instance.
     */
    public S hasRequestUri(String expected) {
        Assertions.assertThat(actual.getRequestURI()).isEqualTo(expected);
        return myself;
    }

    /**
     * Verifies that the request URL (first line of the request) is equal to the given one.
     *
     * @param expected the expected request URL (first line of the request).
     * @return this instance.
     */
    public S hasRequestUrl(String expected) {
        Assertions.assertThat(actual.getRequestURL()).isEqualTo(expected);
        return myself;
    }

    /**
     * Verifies that the request URL (first line of the request) is equal to the given one.
     *
     * @param method the method.
     * @param uri the request URI.
     * @param protocol the protocol.
     * @return this instance.
     */
    public S hasRequestUrl(HttpMethod method, String uri, String protocol) {
        Assertions.assertThat(actual.getRequestURL()).isEqualTo(method.name() + " " + uri + " " + protocol);
        return myself;
    }

    /**
     * Verifies that the remote address is equal to the given one.
     *
     * @param expected the expected remote address.
     * @return this instance.
     */
    public S hasRemoteAddr(String expected) {
        Assertions.assertThat(actual.getRemoteAddr()).isEqualTo(expected);
        return myself;
    }

    /**
     * Verifies that the remote host is equal to the given one.
     *
     * @param expected the expected remote host.
     * @return this instance.
     */
    public S hasRemoteHost(String expected) {
        Assertions.assertThat(actual.getRemoteHost()).isEqualTo(expected);
        return myself;
    }

    /**
     * Verifies that the remote user is equal to the given one.
     *
     * @param expected the expected remote user.
     * @return this instance.
     */
    public S hasRemoteUser(String expected) {
        Assertions.assertThat(actual.getRemoteUser()).isEqualTo(expected);
        return myself;
    }

    /**
     * Verifies that the status code is equal to the given one.
     *
     * @param expected the expected status.
     * @return this instance.
     */
    public S hasStatusCode(HttpStatus expected) {
        HttpStatus status = HttpStatus.valueOf(actual.getStatusCode());
        Assertions.assertThat(status).isEqualTo(expected);
        return myself;
    }

    /**
     * Verifies that the content length is greater than or equal to the given one.
     *
     * @param expected the expected minimal content length.
     * @return this instance.
     */
    public S hasContentLength(long expected) {
        Assertions.assertThat(actual.getContentLength()).isGreaterThanOrEqualTo(expected);
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

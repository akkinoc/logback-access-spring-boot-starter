package net.rakugakibox.spring.boot.logback.access.undertow;

import ch.qos.logback.access.pattern.AccessConverter;
import ch.qos.logback.access.spi.AccessEvent;
import io.undertow.server.HttpServerExchange;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessEvent;
import net.rakugakibox.spring.boot.logback.access.AbstractOverridenAttribute;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The Logback-access event for Undertow.
 */
public class UndertowLogbackAccessEvent extends AbstractLogbackAccessEvent {

    private final RequestURI requestURI = new RequestURI();

    private final QueryString queryString = new QueryString();

    /**
     * Constructs an instance.
     *
     * @param exchange the HTTP server exchange.
     */
    public UndertowLogbackAccessEvent(HttpServerExchange exchange) {
        super(extractHttpServletRequest(exchange), extractHttpServletResponse(exchange), new ServerAdapter(exchange));
    }

    /**
     * Extracts the HTTP servlet request from the HTTP server exchange.
     *
     * @param exchange the HTTP server exchange.
     * @return the HTTP servlet request.
     */
    private static HttpServletRequest extractHttpServletRequest(HttpServerExchange exchange) {
        ServletRequestContext context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
        return (HttpServletRequest) context.getServletRequest();
    }

    /**
     * Extracts the HTTP servlet response from the HTTP server exchange.
     *
     * @param exchange the HTTP server exchange.
     * @return the HTTP servlet response.
     */
    private static HttpServletResponse extractHttpServletResponse(HttpServerExchange exchange) {
        ServletRequestContext context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
        return (HttpServletResponse) context.getServletResponse();
    }

    @Override
    public String getRequestURI() {
        return requestURI.get();
    }

    @Override
    public String getQueryString() {
        return queryString.get();
    }

    @Override
    public String getRequestURL() {
        return getMethod() +
                AccessConverter.SPACE_CHAR +
                getRequestURI() +
                getQueryString() +
                AccessConverter.SPACE_CHAR +
                getProtocol();
    }

    /**
     * The server adapter.
     */
    private static class ServerAdapter implements ch.qos.logback.access.spi.ServerAdapter {

        /**
         * The HTTP server exchange.
         */
        private final HttpServerExchange exchange;

        /**
         * Constructs an instance.
         *
         * @param exchange the HTTP server exchange.
         */
        private ServerAdapter(HttpServerExchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public long getRequestTimestamp() {
            long currentTimeMillis = System.currentTimeMillis();
            long nanoTime = System.nanoTime();
            long requestStartTime = exchange.getRequestStartTime();
            return currentTimeMillis - TimeUnit.NANOSECONDS.toMillis(nanoTime - requestStartTime);
        }

        @Override
        public int getStatusCode() {
            return exchange.getStatusCode();
        }

        @Override
        public long getContentLength() {
            return exchange.getResponseBytesSent();
        }

        @Override
        public Map<String, String> buildResponseHeaderMap() {
            Map<String, String> result = new HashMap<>();
            HeaderMap headers = exchange.getResponseHeaders();
            for (HeaderValues header : headers) {
                result.put(header.getHeaderName().toString(), header.getFirst());
            }
            return result;
        }

    }

    private class RequestURI extends AbstractOverridenAttribute<String> {

        @Override
        protected Optional<String> evaluateValueToOverride() {
            return Optional.of(UndertowLogbackAccessEvent.this)
                    .map(AccessEvent::getRequest)
                    .map(request -> (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));
        }

        @Override
        protected String getOriginalValue() {
            return UndertowLogbackAccessEvent.super.getRequestURI();
        }

    }

    private class QueryString extends AbstractOverridenAttribute<String> {

        @Override
        protected Optional<String> evaluateValueToOverride() {
            return Optional.of(UndertowLogbackAccessEvent.this)
                    .map(AccessEvent::getRequest)
                    .map(request -> (String) request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING))
                    .filter(query -> !query.isEmpty())
                    .map(query -> AccessConverter.QUESTION_CHAR + query);
        }

        @Override
        protected String getOriginalValue() {
            return UndertowLogbackAccessEvent.super.getQueryString();
        }

    }

}

package net.rakugakibox.spring.boot.logback.access.undertow;

import io.undertow.server.ExchangeCompletionListener.NextListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessContext;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.springframework.context.ApplicationEventPublisher;

/**
 * The Undertow HTTP handler that emits Logback-access events.
 * This class is own implementation from scratch, based on {@link AccessLogHandler}.
 * Because emits an own customized access event ({@link UndertowLogbackAccessEvent}).
 *
 * @see AccessLogHandler
 */
public class LogbackAccessUndertowHttpHandler implements HttpHandler {

    /**
     * The Logback-access context.
     */
    private final LogbackAccessContext logbackAccessContext;

    /**
     * The next HTTP handler.
     */
    private final HttpHandler nextHandler;

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @param applicationEventPublisher the application event publisher.
     * @param nextHandler the next HTTP handler.
     */
    public LogbackAccessUndertowHttpHandler(
            LogbackAccessProperties logbackAccessProperties,
            ApplicationEventPublisher applicationEventPublisher,
            HttpHandler nextHandler
    ) {
        this.logbackAccessContext = new LogbackAccessContext(logbackAccessProperties, applicationEventPublisher);
        this.nextHandler = nextHandler;
        this.logbackAccessContext.configure();
        this.logbackAccessContext.start();
    }

    /** {@inheritDoc} */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.addExchangeCompleteListener(this::handleExchangeEvent);
        nextHandler.handleRequest(exchange);
    }

    /** {@inheritDoc} */
    public void handleExchangeEvent(HttpServerExchange exchange, NextListener nextListener) {
        UndertowLogbackAccessEvent event = new UndertowLogbackAccessEvent(exchange);
        logbackAccessContext.emit(event);
        nextListener.proceed();
    }

}

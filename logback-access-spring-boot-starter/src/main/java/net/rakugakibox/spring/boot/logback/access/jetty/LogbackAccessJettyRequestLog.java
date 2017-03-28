package net.rakugakibox.spring.boot.logback.access.jetty;

import ch.qos.logback.access.jetty.RequestLogImpl;
import ch.qos.logback.core.spi.FilterReply;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessConfigurer;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessContext;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

/**
 * The Jetty request-log that emits Logback-access events.
 * This class is own implementation from scratch, based on {@link RequestLogImpl} and {@link NCSARequestLog}.
 * Because emit an own customized access event ({@link JettyLogbackAccessEvent}) from this class.
 *
 * @see RequestLogImpl
 * @see NCSARequestLog
 */
public class LogbackAccessJettyRequestLog extends AbstractLifeCycle implements RequestLog {

    /**
     * The Logback-access context.
     */
    private final LogbackAccessContext logbackAccessContext = new LogbackAccessContext();

    /**
     * The configuration properties for Logback-access.
     */
    private final LogbackAccessProperties logbackAccessProperties;

    /**
     * The configurer of Logback-access.
     */
    private final LogbackAccessConfigurer logbackAccessConfigurer;

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @param logbackAccessConfigurer the configurer of Logback-access.
     */
    public LogbackAccessJettyRequestLog(
            LogbackAccessProperties logbackAccessProperties, LogbackAccessConfigurer logbackAccessConfigurer) {
        this.logbackAccessProperties = logbackAccessProperties;
        this.logbackAccessConfigurer = logbackAccessConfigurer;
    }

    /** {@inheritDoc} */
    @Override
    protected void doStart() throws Exception {
        startLogbackAccessContext();
        super.doStart();
    }

    /** {@inheritDoc} */
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        stopLogbackAccessContext();
    }

    /** {@inheritDoc} */
    @Override
    public void log(Request request, Response response) {
        emitLogbackAccessEvent(request, response);
    }

    /**
     * Configures and starts the Logback-access context.
     */
    private void startLogbackAccessContext() {
        logbackAccessConfigurer.configure(logbackAccessContext);
        logbackAccessContext.start();
    }

    /**
     * Stops and resets the Logback-access context.
     */
    private void stopLogbackAccessContext() {
        logbackAccessContext.stop();
        logbackAccessContext.reset();
        logbackAccessContext.detachAndStopAllAppenders();
        logbackAccessContext.clearAllFilters();
    }

    /**
     * Emits a Logback-access event.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     */
    private void emitLogbackAccessEvent(Request request, Response response) {
        JettyLogbackAccessEvent event = new JettyLogbackAccessEvent(request, response);
        event.setThreadName(Thread.currentThread().getName());
        event.setUseServerPortInsteadOfLocalPort(logbackAccessProperties.getUseServerPortInsteadOfLocalPort());
        if (logbackAccessContext.getFilterChainDecision(event) != FilterReply.DENY) {
            logbackAccessContext.callAppenders(event);
        }
    }

}

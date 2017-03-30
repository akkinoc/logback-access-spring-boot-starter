package net.rakugakibox.spring.boot.logback.access.jetty;

import ch.qos.logback.access.jetty.RequestLogImpl;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessContext;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

/**
 * The Jetty request-log that appends Logback-access events.
 * This class is own implementation from scratch, based on {@link RequestLogImpl} and {@link NCSARequestLog}.
 * Because appends an own customized access event ({@link JettyLogbackAccessEvent}) to the Logback-access appenders.
 *
 * @see RequestLogImpl
 * @see NCSARequestLog
 */
public class LogbackAccessJettyRequestLog extends AbstractLifeCycle implements RequestLog {

    /**
     * The Logback-access context.
     */
    private final LogbackAccessContext logbackAccessContext;

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     */
    public LogbackAccessJettyRequestLog(LogbackAccessProperties logbackAccessProperties) {
        this.logbackAccessContext = new LogbackAccessContext(logbackAccessProperties);
    }

    /** {@inheritDoc} */
    @Override
    protected void doStart() throws Exception {
        logbackAccessContext.start();
        super.doStart();
    }

    /** {@inheritDoc} */
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        logbackAccessContext.stop();
    }

    /** {@inheritDoc} */
    @Override
    public void log(Request request, Response response) {
        JettyLogbackAccessEvent event = new JettyLogbackAccessEvent(request, response);
        logbackAccessContext.emit(event);
    }

}

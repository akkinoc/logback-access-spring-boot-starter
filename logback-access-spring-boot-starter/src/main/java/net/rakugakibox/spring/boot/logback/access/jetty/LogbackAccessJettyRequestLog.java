package net.rakugakibox.spring.boot.logback.access.jetty;

import java.util.List;

import ch.qos.logback.access.jetty.RequestLogImpl;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessContext;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessListener;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

/**
 * The Jetty request-log that emits Logback-access events.
 * This class is own implementation from scratch, based on {@link RequestLogImpl} and {@link NCSARequestLog}.
 * Because emits an own customized access event ({@link JettyLogbackAccessEvent}).
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
     * @param logbackAccessListeners the listeners for Logback-access.
     */
    public LogbackAccessJettyRequestLog(
            LogbackAccessProperties logbackAccessProperties, List<LogbackAccessListener> logbackAccessListeners) {
        this.logbackAccessContext = new LogbackAccessContext(logbackAccessProperties, logbackAccessListeners);
    }

    /** {@inheritDoc} */
    @Override
    protected void doStart() throws Exception {
        logbackAccessContext.configure();
        logbackAccessContext.start();
        super.doStart();
    }

    /** {@inheritDoc} */
    @Override
    public void log(Request request, Response response) {
        JettyLogbackAccessEvent event = new JettyLogbackAccessEvent(request, response);
        logbackAccessContext.emit(event);
    }

}

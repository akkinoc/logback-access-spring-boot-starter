package net.rakugakibox.spring.boot.logback.access.tomcat;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.ServletException;

import ch.qos.logback.access.tomcat.LogbackValve;
import ch.qos.logback.core.spi.FilterReply;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessConfigurer;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessContext;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.apache.catalina.AccessLog;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.valves.RemoteIpValve;
import org.apache.catalina.valves.ValveBase;

/**
 * The Tomcat valve that emits Logback-access events.
 * This class is own implementation from scratch, based on {@link LogbackValve} and {@link AccessLogValve}.
 * Because emit an own customized access event ({@link TomcatLogbackAccessEvent}) from this class.
 *
 * @see LogbackValve
 * @see AccessLogValve
 */
public class LogbackAccessTomcatValve extends ValveBase implements AccessLog {

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
     * Whether to enable request attributes to work with the {@link RemoteIpValve}.
     */
    private Optional<Boolean> requestAttributesEnabled = Optional.empty();

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @param logbackAccessConfigurer the configurer of Logback-access.
     */
    public LogbackAccessTomcatValve(
            LogbackAccessProperties logbackAccessProperties, LogbackAccessConfigurer logbackAccessConfigurer) {
        this.logbackAccessProperties = logbackAccessProperties;
        this.logbackAccessConfigurer = logbackAccessConfigurer;
        this.requestAttributesEnabled = logbackAccessProperties.getTomcat().getEnableRequestAttributes();
        setAsyncSupported(true);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getRequestAttributesEnabled() {
        return requestAttributesEnabled.orElse(false);
    }

    /** {@inheritDoc} */
    @Override
    public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
        this.requestAttributesEnabled = Optional.of(requestAttributesEnabled);
    }

    /**
     * Initializes whether to enable request attributes to work with the {@link RemoteIpValve}.
     * Sets the presence of the {@link RemoteIpValve} by default.
     */
    private void initializeRequestAttributesEnabled() {
        if (requestAttributesEnabled.isPresent()) {
            return;
        }
        boolean presenceOfRemoteIpValve = Stream
                .of(getContainer().getPipeline().getValves())
                .anyMatch(RemoteIpValve.class::isInstance);
        setRequestAttributesEnabled(presenceOfRemoteIpValve);
    }

    /** {@inheritDoc} */
    @Override
    protected void startInternal() throws LifecycleException {
        initializeRequestAttributesEnabled();
        startLogbackAccessContext();
        super.startInternal();
    }

    /** {@inheritDoc} */
    @Override
    protected void stopInternal() throws LifecycleException {
        super.stopInternal();
        stopLogbackAccessContext();
    }

    /** {@inheritDoc} */
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);
    }

    /** {@inheritDoc} */
    @Override
    public void log(Request request, Response response, long time) {
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
        TomcatLogbackAccessEvent event = new TomcatLogbackAccessEvent(request, response);
        event.setThreadName(Thread.currentThread().getName());
        event.setUseServerPortInsteadOfLocalPort(logbackAccessProperties.getUseServerPortInsteadOfLocalPort());
        event.setRequestAttributesEnabled(getRequestAttributesEnabled());
        if (logbackAccessContext.getFilterChainDecision(event) != FilterReply.DENY) {
            logbackAccessContext.callAppenders(event);
        }
    }

}

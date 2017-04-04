package net.rakugakibox.spring.boot.logback.access.tomcat;

import java.io.IOException;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;
import javax.servlet.ServletException;

import ch.qos.logback.access.tomcat.LogbackValve;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessContext;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessListener;
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
 * Because emits an own customized access event ({@link TomcatLogbackAccessEvent}).
 *
 * @see LogbackValve
 * @see AccessLogValve
 */
public class LogbackAccessTomcatValve extends ValveBase implements AccessLog {

    /**
     * The Logback-access context.
     */
    private final LogbackAccessContext logbackAccessContext;

    /**
     * The supplier that takes whether to enable request attributes.
     * Returns and cache the presence of the {@link RemoteIpValve} by default.
     */
    private BooleanSupplier requestAttributesEnabled = () -> {
        boolean requestAttributesEnabled = Stream
                .of(getContainer().getPipeline().getValves())
                .anyMatch(RemoteIpValve.class::isInstance);
        this.requestAttributesEnabled = () -> requestAttributesEnabled;
        return requestAttributesEnabled;
    };

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @param logbackAccessListeners the listeners for Logback-access.
     */
    public LogbackAccessTomcatValve(
            LogbackAccessProperties logbackAccessProperties, List<LogbackAccessListener> logbackAccessListeners) {
        this.logbackAccessContext = new LogbackAccessContext(logbackAccessProperties, logbackAccessListeners);
        setAsyncSupported(true);
        logbackAccessProperties.getTomcat().getEnableRequestAttributes().ifPresent(this::setRequestAttributesEnabled);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getRequestAttributesEnabled() {
        return requestAttributesEnabled.getAsBoolean();
    }

    /** {@inheritDoc} */
    @Override
    public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
        this.requestAttributesEnabled = () -> requestAttributesEnabled;
    }

    /** {@inheritDoc} */
    @Override
    protected void startInternal() throws LifecycleException {
        logbackAccessContext.start();
        super.startInternal();
    }

    /** {@inheritDoc} */
    @Override
    protected void stopInternal() throws LifecycleException {
        super.stopInternal();
        logbackAccessContext.stop();
    }

    /** {@inheritDoc} */
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);
    }

    /** {@inheritDoc} */
    @Override
    public void log(Request request, Response response, long time) {
        TomcatLogbackAccessEvent event = new TomcatLogbackAccessEvent(request, response);
        event.setRequestAttributesEnabled(getRequestAttributesEnabled());
        logbackAccessContext.emit(event);
    }

}

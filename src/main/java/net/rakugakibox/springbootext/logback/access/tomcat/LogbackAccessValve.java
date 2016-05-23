package net.rakugakibox.springbootext.logback.access.tomcat;

import ch.qos.logback.access.spi.AccessContext;
import ch.qos.logback.access.tomcat.LogbackValve;
import ch.qos.logback.core.spi.FilterReply;
import java.io.IOException;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import lombok.Getter;
import lombok.Setter;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import net.rakugakibox.springbootext.logback.access.LogbackAccessProperties;
import org.apache.catalina.AccessLog;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.valves.RemoteIpValve;
import org.apache.catalina.valves.ValveBase;

/**
 * The Tomcat valve.
 * This class is own implementation from scratch, based on {@link LogbackValve} and {@link AccessLogValve}.
 */
public class LogbackAccessValve extends ValveBase implements AccessLog {

    /**
     * The Logback-access context.
     */
    @Getter
    private final AccessContext context;

    /**
     * The configuration properties.
     */
    @Getter
    @Setter
    private LogbackAccessProperties properties;

    /**
     * The configurator.
     */
    @Getter
    @Setter
    private LogbackAccessConfigurator configurator;

    /**
     * Whether request attributes is enabled.
     */
    private Boolean requestAttributesEnabled;

    /**
     * Constructs an instance.
     */
    public LogbackAccessValve() {

        // Allows asynchronous responses.
        setAsyncSupported(true);

        // Creates the Logback-access context.
        context = new AccessContext();
        context.setName(toString());

    }

    /** {@inheritDoc} */
    @Override
    public boolean getRequestAttributesEnabled() {
        return requestAttributesEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
        this.requestAttributesEnabled = requestAttributesEnabled;
    }

    /** {@inheritDoc} */
    @Override
    protected void startInternal() throws LifecycleException {

        // Initializes whether request attributes is enabled.
        if (requestAttributesEnabled == null) {
            requestAttributesEnabled = properties.getTomcat().getEnableRequestAttributes();
        }
        if (requestAttributesEnabled == null) {
            // Deduce the value from the presence of the RemoteIpValve.
            requestAttributesEnabled = Stream.of(getContainer().getPipeline().getValves())
                    .map(Object::getClass)
                    .anyMatch(RemoteIpValve.class::isAssignableFrom);
        }

        // Configures and starts the Logback-access context.
        configurator.configure(context);
        context.start();

        super.startInternal();
    }

    /** {@inheritDoc} */
    @Override
    protected void stopInternal() throws LifecycleException {
        super.stopInternal();

        // Stops and resets the Logback-access context.
        context.stop();
        context.reset();
        context.detachAndStopAllAppenders();
        context.clearAllFilters();

    }

    /** {@inheritDoc} */
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);
    }

    /** {@inheritDoc} */
    @Override
    public void log(Request request, Response response, long time) {

        // Creates a access event.
        TomcatAccessEvent accessEvent = new TomcatAccessEvent(request, response);
        accessEvent.setThreadName(Thread.currentThread().getName());
        accessEvent.setUseServerPortInsteadOfLocalPort(properties.getUseServerPortInsteadOfLocalPort());
        accessEvent.setRequestAttributesEnabled(requestAttributesEnabled);

        // Calls appenders.
        if (context.getFilterChainDecision(accessEvent) != FilterReply.DENY) {
            context.callAppenders(accessEvent);
        }

    }

}

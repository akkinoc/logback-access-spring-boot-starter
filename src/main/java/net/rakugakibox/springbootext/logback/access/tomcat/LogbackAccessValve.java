package net.rakugakibox.springbootext.logback.access.tomcat;

import ch.qos.logback.access.spi.AccessContext;
import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.access.tomcat.LogbackValve;
import ch.qos.logback.access.tomcat.TomcatServerAdapter;
import ch.qos.logback.core.spi.FilterReply;
import java.io.IOException;
import javax.servlet.ServletException;
import lombok.Getter;
import lombok.Setter;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import org.apache.catalina.AccessLog;
import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.AccessLogValve;
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
     * The configurator.
     */
    @Getter
    @Setter
    private LogbackAccessConfigurator configurator;

    /**
     * Whether request attributes is enabled.
     */
    private boolean requestAttributesEnabled;

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
        CustomizedTomcatServerAdapter adapter = new CustomizedTomcatServerAdapter(request, response);

        AccessEvent accessEvent;
        if (requestAttributesEnabled) {
            accessEvent = new RequestAttributeRegardingAccessEvent(request, response, adapter);
        } else {
            accessEvent = new AccessEvent(request, response, adapter);
        }

        accessEvent.setThreadName(Thread.currentThread().getName());

        // Calls appenders.
        if (context.getFilterChainDecision(accessEvent) != FilterReply.DENY) {
            context.callAppenders(accessEvent);
        }

    }

    /**
     * The customized tomcat server adapter.
     */
    private class CustomizedTomcatServerAdapter extends TomcatServerAdapter {

        /**
         * The tomcat request.
         */
        private final Request request;

        /**
         * The tomcat response.
         */
        private final Response response;

        /**
         * Constructs an instance.
         *
         * @param request the tomcat request.
         * @param response the tomcat response.
         */
        public CustomizedTomcatServerAdapter(Request request, Response response) {
            super(request, response);
            this.request = request;
            this.response = response;
        }

        /** {@inheritDoc} */
        @Override
        public long getContentLength() {
            long length = response.getBytesWritten(false);
            if (length <= 0) {
                Object start = request.getAttribute(Globals.SENDFILE_FILE_START_ATTR);
                Object end = request.getAttribute(Globals.SENDFILE_FILE_END_ATTR);
                if (start instanceof Long && end instanceof Long) {
                    Long startAsLong = (Long) start;
                    Long endAsLong = (Long) end;
                    length = endAsLong - startAsLong;
                }
            }
            return length;
        }

    }

}

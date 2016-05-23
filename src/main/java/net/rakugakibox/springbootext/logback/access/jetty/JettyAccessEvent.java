package net.rakugakibox.springbootext.logback.access.jetty;

import ch.qos.logback.access.jetty.JettyServerAdapter;
import net.rakugakibox.springbootext.logback.access.AbstractAccessEvent;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

/**
 * The Jetty access event.
 */
public class JettyAccessEvent extends AbstractAccessEvent {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     */
    public JettyAccessEvent(Request request, Response response) {
        super(request, response, new CustomizedServerAdapter(request, response));
    }

    /**
     * The customized server adapter.
     */
    public static class CustomizedServerAdapter extends JettyServerAdapter {

        /**
         * Constructs an instance.
         *
         * @param request the HTTP request.
         * @param response the HTTP response.
         */
        public CustomizedServerAdapter(Request request, Response response) {
            super(request, response);
        }

    }

}

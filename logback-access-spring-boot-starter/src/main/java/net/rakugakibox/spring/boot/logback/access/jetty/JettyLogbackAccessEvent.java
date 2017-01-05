package net.rakugakibox.spring.boot.logback.access.jetty;

import ch.qos.logback.access.jetty.JettyServerAdapter;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessEvent;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

/**
 * The Logback-access event for Jetty.
 */
public class JettyLogbackAccessEvent extends AbstractLogbackAccessEvent {

    /**
     * Constructs an instance.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     */
    public JettyLogbackAccessEvent(Request request, Response response) {
        super(request, response, new ServerAdapter(request, response));
    }

    /**
     * The server adapter.
     */
    public static class ServerAdapter extends JettyServerAdapter {

        /**
         * Constructs an instance.
         *
         * @param request the HTTP request.
         * @param response the HTTP response.
         */
        public ServerAdapter(Request request, Response response) {
            super(request, response);
        }

    }

}

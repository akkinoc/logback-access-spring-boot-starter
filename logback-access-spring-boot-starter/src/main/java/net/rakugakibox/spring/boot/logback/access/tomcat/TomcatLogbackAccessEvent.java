package net.rakugakibox.spring.boot.logback.access.tomcat;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.access.tomcat.TomcatServerAdapter;
import lombok.Getter;
import lombok.Setter;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessEvent;
import org.apache.catalina.AccessLog;
import org.apache.catalina.Globals;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.AbstractAccessLogValve;
import org.apache.catalina.valves.RemoteIpValve;

/**
 * The Logback-access event for Tomcat.
 */
public class TomcatLogbackAccessEvent extends AbstractLogbackAccessEvent {

    /**
     * Whether to enable request attributes to work with the {@link RemoteIpValve}.
     */
    @Getter
    @Setter
    private boolean requestAttributesEnabled;

    /**
     * The supplier of the local port.
     * The result will be cached.
     */
    private IntSupplier localPort = (IntSupplier & Serializable) () -> {
        int value = takeLocalPortOrServerPort();
        localPort = (IntSupplier & Serializable) () -> value;
        return value;
    };

    /**
     * The supplier of the remote address.
     * The result will be cached.
     */
    private Supplier<String> remoteAddr = (Supplier<String> & Serializable) () -> {
        String value = takeRemoteAddr();
        remoteAddr = (Supplier<String> & Serializable) () -> value;
        return value;
    };

    /**
     * The supplier of the remote host.
     * The result will be cached.
     */
    private Supplier<String> remoteHost = (Supplier<String> & Serializable) () -> {
        String value = takeRemoteHost();
        remoteHost = (Supplier<String> & Serializable) () -> value;
        return value;
    };

    /**
     * The supplier of the protocol.
     * The result will be cached.
     */
    private Supplier<String> protocol = (Supplier<String> & Serializable) () -> {
        String value = takeProtocol();
        protocol = (Supplier<String> & Serializable) () -> value;
        return value;
    };

    /**
     * Constructs an instance.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     */
    public TomcatLogbackAccessEvent(Request request, Response response) {
        super(request, response, new ServerAdapter(request, response));
    }

    /** {@inheritDoc} */
    @Override
    public int getLocalPort() {
        return localPort.getAsInt();
    }

    /**
     * Takes the local port.
     * Returns the server port in the same way as {@link AbstractAccessLogValve.PortElement} if necessary.
     *
     * @return the local port.
     */
    private int takeLocalPortOrServerPort() {
        return Optional.of(this)
                .filter(AbstractLogbackAccessEvent::isUseServerPortInsteadOfLocalPort)
                .filter(TomcatLogbackAccessEvent::isRequestAttributesEnabled)
                .map(AccessEvent::getRequest)
                .map(request -> (Integer) request.getAttribute(AccessLog.SERVER_PORT_ATTRIBUTE))
                .orElseGet(super::getLocalPort);
    }

    /** {@inheritDoc} */
    @Override
    public String getRemoteAddr() {
        return remoteAddr.get();
    }

    /**
     * Takes the remote address in the same way as {@link AbstractAccessLogValve.RemoteAddrElement}.
     *
     * @return the remote address.
     */
    private String takeRemoteAddr() {
        return Optional.of(this)
                .filter(TomcatLogbackAccessEvent::isRequestAttributesEnabled)
                .map(AccessEvent::getRequest)
                .map(request -> (String) request.getAttribute(AccessLog.REMOTE_ADDR_ATTRIBUTE))
                .orElseGet(super::getRemoteAddr);
    }

    /** {@inheritDoc} */
    @Override
    public String getRemoteHost() {
        return remoteHost.get();
    }

    /**
     * Takes the remote host in the same way as {@link AbstractAccessLogValve.HostElement}.
     *
     * @return the remote host.
     */
    private String takeRemoteHost() {
        return Optional.of(this)
                .filter(TomcatLogbackAccessEvent::isRequestAttributesEnabled)
                .map(AccessEvent::getRequest)
                .map(request -> (String) request.getAttribute(AccessLog.REMOTE_HOST_ATTRIBUTE))
                .orElseGet(super::getRemoteHost);
    }

    /** {@inheritDoc} */
    @Override
    public String getProtocol() {
        return protocol.get();
    }

    /**
     * Takes the protocol in the same way as {@link AbstractAccessLogValve.ProtocolElement}.
     *
     * @return the protocol.
     */
    private String takeProtocol() {
        return Optional.of(this)
                .filter(TomcatLogbackAccessEvent::isRequestAttributesEnabled)
                .map(AccessEvent::getRequest)
                .map(request -> (String) request.getAttribute(AccessLog.PROTOCOL_ATTRIBUTE))
                .orElseGet(super::getProtocol);
    }

    /**
     * The server adapter.
     */
    public static class ServerAdapter extends TomcatServerAdapter {

        /**
         * The HTTP request.
         */
        private final Request request;

        /**
         * The HTTP response.
         */
        private final Response response;

        /**
         * Constructs an instance.
         *
         * @param request the HTTP request.
         * @param response the HTTP response.
         */
        public ServerAdapter(Request request, Response response) {
            super(request, response);
            this.request = request;
            this.response = response;
        }

        /** {@inheritDoc} */
        @Override
        public long getContentLength() {
            return takeContentLength();
        }

        /**
         * Takes the content length in the same way as {@link AbstractAccessLogValve.ByteSentElement}.
         *
         * @return the content length.
         */
        private long takeContentLength() {
            long length = response.getBytesWritten(false);
            if (length > 0) {
                return length;
            }
            Long start = (Long) request.getAttribute(Globals.SENDFILE_FILE_START_ATTR);
            Long end = (Long) request.getAttribute(Globals.SENDFILE_FILE_END_ATTR);
            if (start != null && end != null) {
                return end - start;
            }
            return super.getContentLength();
        }

    }

}

package net.rakugakibox.springbootext.logback.access.tomcat;

import ch.qos.logback.access.tomcat.TomcatServerAdapter;
import lombok.Getter;
import lombok.Setter;
import net.rakugakibox.springbootext.logback.access.AbstractAccessEvent;
import org.apache.catalina.AccessLog;
import org.apache.catalina.Globals;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;

/**
 * The Tomcat access event.
 */
public class TomcatAccessEvent extends AbstractAccessEvent {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Whether request attributes is enabled.
     */
    @Getter
    @Setter
    private boolean requestAttributesEnabled;

    /**
     * The cached local port.
     */
    private Integer localPort;

    /**
     * The cached protocol.
     */
    private String protocol;

    /**
     * The cached remote address.
     */
    private String remoteAddr;

    /**
     * The cached remote host.
     */
    private String remoteHost;

    /**
     * Constructs an instance.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     */
    public TomcatAccessEvent(Request request, Response response) {
        super(request, response, new CustomizedServerAdapter(request, response));
    }

    /** {@inheritDoc} */
    @Override
    public int getLocalPort() {
        if (localPort == null) {
            if (requestAttributesEnabled && isUseServerPortInsteadOfLocalPort()) {
                localPort = getOptionalRequest()
                        .map(request -> (Integer) request.getAttribute(AccessLog.SERVER_PORT_ATTRIBUTE))
                        .orElseGet(super::getLocalPort);
            } else {
                localPort = super.getLocalPort();
            }
        }
        return localPort;
    }

    /** {@inheritDoc} */
    @Override
    public String getProtocol() {
        if (protocol == null) {
            if (requestAttributesEnabled) {
                protocol = getOptionalRequest()
                        .map(request -> (String) request.getAttribute(AccessLog.PROTOCOL_ATTRIBUTE))
                        .orElseGet(super::getProtocol);
            } else {
                protocol = super.getProtocol();
            }
        }
        return protocol;
    }

    /** {@inheritDoc} */
    @Override
    public String getRemoteAddr() {
        if (remoteAddr == null) {
            if (requestAttributesEnabled) {
                remoteAddr = getOptionalRequest()
                        .map(request -> (String) request.getAttribute(AccessLog.REMOTE_ADDR_ATTRIBUTE))
                        .orElseGet(super::getRemoteAddr);
            } else {
                remoteAddr = super.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    /** {@inheritDoc} */
    @Override
    public String getRemoteHost() {
        if (remoteHost == null) {
            if (requestAttributesEnabled) {
                remoteHost = getOptionalRequest()
                        .map(request -> (String) request.getAttribute(AccessLog.REMOTE_HOST_ATTRIBUTE))
                        .orElseGet(super::getRemoteHost);
            } else {
                remoteHost = super.getRemoteHost();
            }
        }
        return remoteHost;
    }

    /**
     * The customized server adapter.
     */
    public static class CustomizedServerAdapter extends TomcatServerAdapter {

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
        public CustomizedServerAdapter(Request request, Response response) {
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

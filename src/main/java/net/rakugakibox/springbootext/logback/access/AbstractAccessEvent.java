package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.access.spi.ServerAdapter;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * The base class of access event.
 */
public abstract class AbstractAccessEvent extends AccessEvent {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Whether use the server port instead of the local port.
     */
    @Getter
    @Setter
    private boolean useServerPortInsteadOfLocalPort;

    /**
     * The cached local port.
     */
    private Integer localPort;

    /**
     * Constructs an instance.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     * @param adapter the server adapter.
     */
    public AbstractAccessEvent(HttpServletRequest request, HttpServletResponse response, ServerAdapter adapter) {
        super(request, response, adapter);
    }

    /**
     * Returns the request.
     *
     * @return the request.
     */
    protected Optional<HttpServletRequest> getOptionalRequest() {
        return Optional.ofNullable(getRequest());
    }

    /**
     * Returns the response.
     *
     * @return the response.
     */
    protected Optional<HttpServletResponse> getOptionalResponse() {
        return Optional.ofNullable(getResponse());
    }

    /** {@inheritDoc} */
    @Override
    public int getLocalPort() {
        if (localPort == null) {
            if (useServerPortInsteadOfLocalPort) {
                localPort = getOptionalRequest()
                        .map(HttpServletRequest::getServerPort)
                        .filter(port -> port >= 0)
                        .orElseGet(super::getLocalPort);
            } else {
                localPort = super.getLocalPort();
            }
        }
        return localPort;
    }

}

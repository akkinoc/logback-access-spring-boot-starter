package net.rakugakibox.spring.boot.logback.access;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.IntSupplier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.access.spi.ServerAdapter;
import lombok.Getter;
import lombok.Setter;

/**
 * The base class of Logback-access events.
 */
public abstract class AbstractLogbackAccessEvent extends AccessEvent {

    /**
     * Whether to use the server port instead of the local port.
     */
    @Getter
    @Setter
    private boolean useServerPortInsteadOfLocalPort;

    /**
     * The supplier that takes the local port.
     * The result will be cached.
     */
    private IntSupplier localPort = (IntSupplier & Serializable) () -> {
        int localPort = takeLocalPort();
        this.localPort = (IntSupplier & Serializable) () -> localPort;
        return localPort;
    };

    /**
     * Constructs an instance.
     *
     * @param request the HTTP request.
     * @param response the HTTP response.
     * @param adapter the server adapter.
     */
    public AbstractLogbackAccessEvent(HttpServletRequest request, HttpServletResponse response, ServerAdapter adapter) {
        super(request, response, adapter);
        setThreadName(Thread.currentThread().getName());
    }

    /** {@inheritDoc} */
    @Override
    public int getLocalPort() {
        return localPort.getAsInt();
    }

    /**
     * Takes the local port.
     * Returns the server port if necessary.
     *
     * @return the local port.
     */
    private int takeLocalPort() {
        return Optional.of(this)
                .filter(AbstractLogbackAccessEvent::isUseServerPortInsteadOfLocalPort)
                .map(AccessEvent::getRequest)
                .map(HttpServletRequest::getServerPort)
                .orElseGet(super::getLocalPort);
    }

}

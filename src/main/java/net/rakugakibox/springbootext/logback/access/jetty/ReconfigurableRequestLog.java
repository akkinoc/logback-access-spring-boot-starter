package net.rakugakibox.springbootext.logback.access.jetty;

import ch.qos.logback.access.jetty.RequestLogImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;

/**
 * Customized {@link RequestLogImpl}.
 * Reconfigure Logback-access if reconfigurator is set.
 */
@Slf4j
class ReconfigurableRequestLog extends RequestLogImpl {

    /**
     * Reconfigurator.
     */
    @Getter
    @Setter
    private LogbackAccessConfigurator reconfigurator;

    /**
     * Constructor.
     */
    public ReconfigurableRequestLog() {
        // Suppress status printing.
        setQuiet(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void configure() {
        super.configure();
        if (reconfigurator != null) {
            getStatusManager().clear();
            reconfigurator.configure(this);
            log.debug("Reconfigured RequestLog: requestLog=[{}] with reconfigurator=[{}]", this, reconfigurator);
        } else {
            log.debug("Skipped reconfiguration: requestLog=[{}]", this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}

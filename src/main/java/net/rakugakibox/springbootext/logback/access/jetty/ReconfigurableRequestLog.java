package net.rakugakibox.springbootext.logback.access.jetty;

import ch.qos.logback.access.jetty.RequestLogImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;

/**
 * The customized {@link RequestLogImpl}.
 * Reconfigures the {@link RequestLogImpl} if reconfigurator is set.
 */
@Slf4j
class ReconfigurableRequestLog extends RequestLogImpl {

    /**
     * The reconfigurator.
     */
    @Getter
    @Setter
    private LogbackAccessConfigurator reconfigurator;

    /**
     * Constructs an instance.
     */
    public ReconfigurableRequestLog() {
        // Suppresses status printing.
        setQuiet(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void configure() {
        super.configure();
        if (reconfigurator != null) {
            getStatusManager().clear();
            reconfigurator.configure(this);
            log.debug("Reconfigured the RequestLog: requestLog=[{}] with reconfigurator=[{}]", this, reconfigurator);
        } else {
            log.debug("Skipped the RequestLog reconfiguration: requestLog=[{}]", this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}

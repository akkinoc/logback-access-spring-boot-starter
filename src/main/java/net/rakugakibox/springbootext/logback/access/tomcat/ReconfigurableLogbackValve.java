package net.rakugakibox.springbootext.logback.access.tomcat;

import ch.qos.logback.access.tomcat.LogbackValve;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import org.apache.catalina.LifecycleException;

/**
 * The customized {@link LogbackValve}.
 * Reconfigures the {@link LogbackValve} if reconfigurator is set.
 */
@Slf4j
class ReconfigurableLogbackValve extends LogbackValve {

    /**
     * The reconfigurator.
     */
    @Getter
    @Setter
    private LogbackAccessConfigurator reconfigurator;

    /**
     * Constructs an instance.
     */
    public ReconfigurableLogbackValve() {
        // Suppresses status printing.
        setQuiet(true);
        // Allows asynchronous responses.
        setAsyncSupported(true);
    }

    /** {@inheritDoc} */
    @Override
    public void startInternal() throws LifecycleException {
        super.startInternal();
        if (reconfigurator != null) {
            getStatusManager().clear();
            reconfigurator.configure(this);
            log.debug("Reconfigured the Valve: valve=[{}] with reconfigurator=[{}]", this, reconfigurator);
        } else {
            log.debug("Skipped the Valve reconfiguration: valve=[{}]", this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}

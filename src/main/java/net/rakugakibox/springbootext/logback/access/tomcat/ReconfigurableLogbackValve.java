package net.rakugakibox.springbootext.logback.access.tomcat;

import ch.qos.logback.access.tomcat.LogbackValve;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.springbootext.logback.access.LogbackAccessConfigurator;
import org.apache.catalina.LifecycleException;

/**
 * Customized {@link LogbackValve}.
 * Reconfigure Logback-access if reconfigurator is set.
 */
@Slf4j
class ReconfigurableLogbackValve extends LogbackValve {

    /**
     * Reconfigurator.
     */
    @Getter
    @Setter
    private LogbackAccessConfigurator reconfigurator;

    /**
     * Constructor.
     */
    public ReconfigurableLogbackValve() {
        // Suppress status printing.
        setQuiet(true);
    }

    /** {@inheritDoc} */
    @Override
    public void startInternal() throws LifecycleException {
        super.startInternal();
        if (reconfigurator != null) {
            getStatusManager().clear();
            reconfigurator.configure(this);
            log.debug("Reconfigured LogbackValve: valve=[{}] with reconfigurator=[{}]", this, reconfigurator);
        } else {
            log.debug("Skipped reconfiguration: valve=[{}]", this);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}

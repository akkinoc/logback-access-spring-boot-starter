package net.rakugakibox.spring.boot.logback.access.tomcat;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessListener;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

/**
 * The Logback-access installer for Tomcat.
 */
@Slf4j
public class TomcatLogbackAccessInstaller
        extends AbstractLogbackAccessInstaller<TomcatEmbeddedServletContainerFactory> {

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @param logbackAccessListeners the listeners for Logback-access.
     */
    public TomcatLogbackAccessInstaller(
            LogbackAccessProperties logbackAccessProperties, List<LogbackAccessListener> logbackAccessListeners) {
        super(TomcatEmbeddedServletContainerFactory.class, logbackAccessProperties, logbackAccessListeners);
    }

    /** {@inheritDoc} */
    @Override
    protected void installLogbackAccess(TomcatEmbeddedServletContainerFactory container) {
        LogbackAccessTomcatValve valve = new LogbackAccessTomcatValve(logbackAccessProperties, logbackAccessListeners);
        container.addEngineValves(valve);
        log.debug("Installed Logback-access: container=[{}], valve=[{}]", container, valve);
    }

}

package net.rakugakibox.spring.boot.logback.access.tomcat;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessInstaller;
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
     */
    public TomcatLogbackAccessInstaller(LogbackAccessProperties logbackAccessProperties) {
        super(TomcatEmbeddedServletContainerFactory.class, logbackAccessProperties);
    }

    /** {@inheritDoc} */
    @Override
    public void installLogbackAccess(TomcatEmbeddedServletContainerFactory container) {
        LogbackAccessTomcatValve valve = new LogbackAccessTomcatValve(logbackAccessProperties);
        container.addEngineValves(valve);
        log.debug("Installed Logback-access: container=[{}], valve=[{}]", container, valve);
    }

}

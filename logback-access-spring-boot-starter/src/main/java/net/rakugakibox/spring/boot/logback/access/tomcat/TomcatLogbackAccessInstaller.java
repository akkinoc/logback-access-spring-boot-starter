package net.rakugakibox.spring.boot.logback.access.tomcat;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationEventPublisher;

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
     * @param applicationEventPublisher the application event publisher.
     */
    public TomcatLogbackAccessInstaller(
            LogbackAccessProperties logbackAccessProperties, ApplicationEventPublisher applicationEventPublisher) {
        super(TomcatEmbeddedServletContainerFactory.class, logbackAccessProperties, applicationEventPublisher);
    }

    /** {@inheritDoc} */
    @Override
    protected void installLogbackAccess(TomcatEmbeddedServletContainerFactory container) {
        LogbackAccessTomcatValve valve =
                new LogbackAccessTomcatValve(logbackAccessProperties, applicationEventPublisher);
        container.addEngineValves(valve);
        log.debug("Installed Logback-access: container=[{}]", container);
    }

}

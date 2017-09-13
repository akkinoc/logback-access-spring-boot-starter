package net.rakugakibox.spring.boot.logback.access.tomcat;

import lombok.extern.slf4j.Slf4j;
import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

/**
 * The Logback-access installer for Tomcat.
 */
@Slf4j
public class TomcatLogbackAccessInstaller
        extends AbstractLogbackAccessInstaller<TomcatServletWebServerFactory> {

    /**
     * Constructs an instance.
     *
     * @param logbackAccessProperties the configuration properties for Logback-access.
     * @param environment the environment.
     * @param applicationEventPublisher the application event publisher.
     */
    public TomcatLogbackAccessInstaller(
            LogbackAccessProperties logbackAccessProperties,
            Environment environment,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        super(logbackAccessProperties, environment, applicationEventPublisher);
    }

    /** {@inheritDoc} */
    @Override
    protected void installLogbackAccess(TomcatServletWebServerFactory container) {
        LogbackAccessTomcatValve valve = new LogbackAccessTomcatValve(
                logbackAccessProperties, environment, applicationEventPublisher);
        container.addEngineValves(valve);
        log.debug("Installed Logback-access: container=[{}]", container);
    }

}

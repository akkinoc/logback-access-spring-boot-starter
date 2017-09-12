package net.rakugakibox.spring.boot.logback.access;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

/**
 * The base class of Logback-access installers.
 *
 * @param <T> the type of the servlet container.
 */
@RequiredArgsConstructor
public abstract class AbstractLogbackAccessInstaller<T extends ConfigurableServletWebServerFactory>
        implements WebServerFactoryCustomizer<T> {

    /**
     * The configuration properties for Logback-access.
     */
    protected final LogbackAccessProperties logbackAccessProperties;

    /**
     * The environment.
     */
    protected final Environment environment;

    /**
     * The application event publisher.
     */
    protected final ApplicationEventPublisher applicationEventPublisher;

    /** {@inheritDoc} */
    @Override
    public void customize(T container) {
        installLogbackAccess(container);
    }

    /**
     * Installs Logback-access.
     *
     * @param container the servlet container.
     */
    protected abstract void installLogbackAccess(T container);

}

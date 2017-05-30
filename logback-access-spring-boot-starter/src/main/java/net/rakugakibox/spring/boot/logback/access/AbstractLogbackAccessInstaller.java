package net.rakugakibox.spring.boot.logback.access;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.ApplicationEventPublisher;

/**
 * The base class of Logback-access installers.
 *
 * @param <T> the type of the servlet container.
 */
@RequiredArgsConstructor
public abstract class AbstractLogbackAccessInstaller<T extends ConfigurableEmbeddedServletContainer>
        implements EmbeddedServletContainerCustomizer {

    /**
     * The class of the servlet container.
     */
    protected final Class<T> containerClass;

    /**
     * The configuration properties for Logback-access.
     */
    protected final LogbackAccessProperties logbackAccessProperties;

    /**
     * The application event publisher.
     */
    protected final ApplicationEventPublisher applicationEventPublisher;

    /** {@inheritDoc} */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        installLogbackAccess(containerClass.cast(container));
    }

    /**
     * Installs Logback-access.
     *
     * @param container the servlet container.
     */
    protected abstract void installLogbackAccess(T container);

}

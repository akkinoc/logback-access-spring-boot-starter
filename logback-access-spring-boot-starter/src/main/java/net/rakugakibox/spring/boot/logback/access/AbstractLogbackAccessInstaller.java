package net.rakugakibox.spring.boot.logback.access;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

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
     * The listeners for Logback-access.
     */
    protected final List<LogbackAccessListener> logbackAccessListeners;

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
    public abstract void installLogbackAccess(T container);

}

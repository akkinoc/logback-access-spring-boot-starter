package net.rakugakibox.spring.boot.logback.access;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

/**
 * The base class for installing Logback-access.
 *
 * @param <T> the type of the servlet container.
 */
@RequiredArgsConstructor
public abstract class AbstractLogbackAccessInstaller<T extends ConfigurableEmbeddedServletContainer>
        implements EmbeddedServletContainerCustomizer {

    /**
     * The class of the servlet container.
     */
    private final Class<T> containerClass;

    /**
     * The configuration properties for Logback-access.
     */
    @Getter
    private final LogbackAccessProperties logbackAccessProperties;

    /**
     * The configurer of Logback-access.
     */
    @Getter
    private final LogbackAccessConfigurer logbackAccessConfigurer;

    /** {@inheritDoc} */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        install(containerClass.cast(container));
    }

    /**
     * Installs Logback-access.
     *
     * @param container the servlet container.
     */
    public abstract void install(T container);

}

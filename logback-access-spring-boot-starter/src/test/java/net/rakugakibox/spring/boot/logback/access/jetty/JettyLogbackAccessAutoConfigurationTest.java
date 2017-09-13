package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessAutoConfigurationTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test of {@link LogbackAccessAutoConfiguration} for Jetty.
 */
public class JettyLogbackAccessAutoConfigurationTest extends AbstractLogbackAccessAutoConfigurationTest {

    /** {@inheritDoc} */
    @Override
    public void tomcatLogbackAccessInstaller() {
        assertThat(tomcatLogbackAccessInstaller).isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void jettyLogbackAccessInstaller() {
        assertThat(jettyLogbackAccessInstaller).isPresent();
    }

    /** {@inheritDoc} */
    @Override
    public void undertowLogbackAccessInstaller() {
        assertThat(undertowLogbackAccessInstaller).isEmpty();
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(ServletWebServerFactoryAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

package net.rakugakibox.spring.boot.logback.access.tomcat;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessAutoConfigurationTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessAutoConfiguration;
import net.rakugakibox.spring.boot.logback.access.test.TomcatServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test of {@link LogbackAccessAutoConfiguration} for Tomcat.
 */
public class TomcatLogbackAccessAutoConfigurationTest extends AbstractLogbackAccessAutoConfigurationTest {

    /** {@inheritDoc} */
    @Override
    public void tomcatLogbackAccessInstaller() {
        assertThat(tomcatLogbackAccessInstaller).isPresent();
    }

    /** {@inheritDoc} */
    @Override
    public void jettyLogbackAccessInstaller() {
        assertThat(jettyLogbackAccessInstaller).isEmpty();
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
    @Import(TomcatServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

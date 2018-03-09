package net.rakugakibox.spring.boot.logback.access.undertow;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessAutoConfigurationTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessAutoConfiguration;
import net.rakugakibox.spring.boot.logback.access.test.UndertowServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test of {@link LogbackAccessAutoConfiguration} for Undertow.
 */
public class UndertowLogbackAccessAutoConfigurationTest extends AbstractLogbackAccessAutoConfigurationTest {

    /** {@inheritDoc} */
    @Override
    public void tomcatLogbackAccessInstaller() {
        assertThat(tomcatLogbackAccessInstaller).isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void jettyLogbackAccessInstaller() {
        assertThat(jettyLogbackAccessInstaller).isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void undertowLogbackAccessInstaller() {
        assertThat(undertowLogbackAccessInstaller).isPresent();
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(UndertowServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

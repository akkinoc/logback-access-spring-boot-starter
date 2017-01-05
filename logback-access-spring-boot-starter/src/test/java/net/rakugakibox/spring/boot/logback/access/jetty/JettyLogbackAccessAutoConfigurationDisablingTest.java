package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessAutoConfigurationDisablingTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to disable {@link LogbackAccessAutoConfiguration} for Jetty.
 */
public class JettyLogbackAccessAutoConfigurationDisablingTest
        extends AbstractLogbackAccessAutoConfigurationDisablingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

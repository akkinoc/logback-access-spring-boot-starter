package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractConfigurationFileInvalidTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test in the case where configuration files is invalid for Jetty.
 */
public class JettyConfigurationFileInvalidTest extends AbstractConfigurationFileInvalidTest {

    /**
     * Constructs an instance.
     */
    public JettyConfigurationFileInvalidTest() {
        super(ContextConfiguration.class);
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

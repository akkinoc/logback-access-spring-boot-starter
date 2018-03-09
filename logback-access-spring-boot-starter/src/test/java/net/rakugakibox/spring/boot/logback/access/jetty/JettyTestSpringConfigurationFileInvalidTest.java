package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractTestSpringConfigurationFileInvalidTest;
import net.rakugakibox.spring.boot.logback.access.test.JettyServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test in the case where test spring configuration files is invalid for Jetty.
 */
public class JettyTestSpringConfigurationFileInvalidTest extends AbstractTestSpringConfigurationFileInvalidTest {

    /**
     * Constructs an instance.
     */
    public JettyTestSpringConfigurationFileInvalidTest() {
        super(ContextConfiguration.class);
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(JettyServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

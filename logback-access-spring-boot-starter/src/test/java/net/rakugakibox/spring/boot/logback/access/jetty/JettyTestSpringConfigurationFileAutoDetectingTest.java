package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractTestSpringConfigurationFileAutoDetectingTest;
import net.rakugakibox.spring.boot.logback.access.test.JettyServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to auto detect test spring configuration file for Jetty.
 */
public class JettyTestSpringConfigurationFileAutoDetectingTest
        extends AbstractTestSpringConfigurationFileAutoDetectingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(JettyServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

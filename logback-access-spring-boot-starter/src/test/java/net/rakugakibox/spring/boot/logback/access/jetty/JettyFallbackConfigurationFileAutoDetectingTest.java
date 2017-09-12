package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractFallbackConfigurationFileAutoDetectingTest;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to auto detect fallback configuration file for Jetty.
 */
public class JettyFallbackConfigurationFileAutoDetectingTest
        extends AbstractFallbackConfigurationFileAutoDetectingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(ServletWebServerFactoryAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

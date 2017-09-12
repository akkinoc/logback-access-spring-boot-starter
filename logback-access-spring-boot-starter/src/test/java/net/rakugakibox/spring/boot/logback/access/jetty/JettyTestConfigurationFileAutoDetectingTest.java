package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractTestConfigurationFileAutoDetectingTest;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to auto detect test configuration file for Jetty.
 */
public class JettyTestConfigurationFileAutoDetectingTest extends AbstractTestConfigurationFileAutoDetectingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(ServletWebServerFactoryAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

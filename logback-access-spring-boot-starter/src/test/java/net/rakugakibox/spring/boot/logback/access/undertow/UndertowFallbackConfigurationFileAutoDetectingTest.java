package net.rakugakibox.spring.boot.logback.access.undertow;

import net.rakugakibox.spring.boot.logback.access.AbstractFallbackConfigurationFileAutoDetectingTest;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to auto detect fallback configuration file for Undertow.
 */
public class UndertowFallbackConfigurationFileAutoDetectingTest
        extends AbstractFallbackConfigurationFileAutoDetectingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(ServletWebServerFactoryAutoConfiguration.EmbeddedUndertow.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

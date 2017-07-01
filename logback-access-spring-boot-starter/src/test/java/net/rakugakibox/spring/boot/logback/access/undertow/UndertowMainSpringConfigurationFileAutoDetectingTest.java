package net.rakugakibox.spring.boot.logback.access.undertow;

import net.rakugakibox.spring.boot.logback.access.AbstractMainSpringConfigurationFileAutoDetectingTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to auto detect main spring configuration file for Undertow.
 */
public class UndertowMainSpringConfigurationFileAutoDetectingTest
        extends AbstractMainSpringConfigurationFileAutoDetectingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedUndertow.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractMainConfigurationFileAutoDetectingTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to auto detect main configuration file for Jetty.
 */
public class JettyMainConfigurationFileAutoDetectingTest extends AbstractMainConfigurationFileAutoDetectingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

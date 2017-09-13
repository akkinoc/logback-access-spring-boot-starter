package net.rakugakibox.spring.boot.logback.access.tomcat;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessAutoConfigurationDisablingTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to disable {@link LogbackAccessAutoConfiguration} for Tomcat.
 */
public class TomcatLogbackAccessAutoConfigurationDisablingTest
        extends AbstractLogbackAccessAutoConfigurationDisablingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(ServletWebServerFactoryAutoConfiguration.EmbeddedTomcat.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

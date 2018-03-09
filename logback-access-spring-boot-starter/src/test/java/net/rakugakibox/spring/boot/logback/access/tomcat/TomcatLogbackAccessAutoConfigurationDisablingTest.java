package net.rakugakibox.spring.boot.logback.access.tomcat;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessAutoConfigurationDisablingTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessAutoConfiguration;
import net.rakugakibox.spring.boot.logback.access.test.TomcatServletWebServerConfiguration;
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
    @Import(TomcatServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

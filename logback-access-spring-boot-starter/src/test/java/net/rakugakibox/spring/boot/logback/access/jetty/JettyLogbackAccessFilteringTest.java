package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessFilteringTest;
import net.rakugakibox.spring.boot.logback.access.test.JettyServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to filter Logback-access events for Jetty.
 */
public class JettyLogbackAccessFilteringTest extends AbstractLogbackAccessFilteringTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(JettyServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

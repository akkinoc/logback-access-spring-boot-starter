package net.rakugakibox.spring.boot.logback.access.undertow;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessAutoConfigurationDisablingTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessAutoConfiguration;
import net.rakugakibox.spring.boot.logback.access.test.UndertowServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to disable {@link LogbackAccessAutoConfiguration} for Undertow.
 */
public class UndertowLogbackAccessAutoConfigurationDisablingTest
        extends AbstractLogbackAccessAutoConfigurationDisablingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(UndertowServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

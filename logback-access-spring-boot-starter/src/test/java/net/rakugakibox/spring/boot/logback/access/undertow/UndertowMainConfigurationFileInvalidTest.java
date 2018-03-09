package net.rakugakibox.spring.boot.logback.access.undertow;

import net.rakugakibox.spring.boot.logback.access.AbstractMainConfigurationFileInvalidTest;
import net.rakugakibox.spring.boot.logback.access.test.UndertowServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test in the case where main configuration files is invalid for Undertow.
 */
public class UndertowMainConfigurationFileInvalidTest extends AbstractMainConfigurationFileInvalidTest {

    /**
     * Constructs an instance.
     */
    public UndertowMainConfigurationFileInvalidTest() {
        super(ContextConfiguration.class);
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(UndertowServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

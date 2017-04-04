package net.rakugakibox.spring.boot.logback.access.undertow;

import net.rakugakibox.spring.boot.logback.access.AbstractConfigurationFileInvalidTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test in the case where configuration files is invalid for Undertow.
 */
public class UndertowConfigurationFileInvalidTest extends AbstractConfigurationFileInvalidTest {

    /**
     * Constructs an instance.
     */
    public UndertowConfigurationFileInvalidTest() {
        super(ContextConfiguration.class);
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedUndertow.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

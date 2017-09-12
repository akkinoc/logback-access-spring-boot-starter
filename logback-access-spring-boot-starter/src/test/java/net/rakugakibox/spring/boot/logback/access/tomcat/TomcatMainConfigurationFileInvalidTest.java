package net.rakugakibox.spring.boot.logback.access.tomcat;

import net.rakugakibox.spring.boot.logback.access.AbstractMainConfigurationFileInvalidTest;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test in the case where main configuration files is invalid for Tomcat.
 */
public class TomcatMainConfigurationFileInvalidTest extends AbstractMainConfigurationFileInvalidTest {

    /**
     * Constructs an instance.
     */
    public TomcatMainConfigurationFileInvalidTest() {
        super(ContextConfiguration.class);
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(ServletWebServerFactoryAutoConfiguration.EmbeddedTomcat.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

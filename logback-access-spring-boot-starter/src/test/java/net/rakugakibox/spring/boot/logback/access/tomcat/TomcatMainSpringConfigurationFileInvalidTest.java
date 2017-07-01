package net.rakugakibox.spring.boot.logback.access.tomcat;

import net.rakugakibox.spring.boot.logback.access.AbstractMainSpringConfigurationFileInvalidTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test in the case where main spring configuration files is invalid for Tomcat.
 */
public class TomcatMainSpringConfigurationFileInvalidTest extends AbstractMainSpringConfigurationFileInvalidTest {

    /**
     * Constructs an instance.
     */
    public TomcatMainSpringConfigurationFileInvalidTest() {
        super(ContextConfiguration.class);
    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

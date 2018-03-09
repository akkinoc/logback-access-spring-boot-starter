package net.rakugakibox.spring.boot.logback.access.tomcat;

import net.rakugakibox.spring.boot.logback.access.AbstractTestSpringConfigurationFileAutoDetectingTest;
import net.rakugakibox.spring.boot.logback.access.test.TomcatServletWebServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to auto detect test spring configuration file for Tomcat.
 */
public class TomcatTestSpringConfigurationFileAutoDetectingTest
        extends AbstractTestSpringConfigurationFileAutoDetectingTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(TomcatServletWebServerConfiguration.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

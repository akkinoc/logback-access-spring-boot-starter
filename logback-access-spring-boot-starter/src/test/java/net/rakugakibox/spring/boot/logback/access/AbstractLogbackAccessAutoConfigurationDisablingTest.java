package net.rakugakibox.spring.boot.logback.access;

import java.util.Optional;

import net.rakugakibox.spring.boot.logback.access.jetty.JettyLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.tomcat.TomcatLogbackAccessInstaller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The base class for testing to disable {@link LogbackAccessAutoConfiguration}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(value = "logback.access.enabled=false", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractLogbackAccessAutoConfigurationDisablingTest {

    /**
     * The configuration properties for Logback-access.
     */
    @Autowired
    protected Optional<LogbackAccessProperties> logbackAccessProperties;

    /**
     * The Logback-access installer for Tomcat.
     */
    @Autowired
    protected Optional<TomcatLogbackAccessInstaller> tomcatLogbackAccessInstaller;

    /**
     * The Logback-access installer for Jetty.
     */
    @Autowired
    protected Optional<JettyLogbackAccessInstaller> jettyLogbackAccessInstaller;

    /**
     * Tests the configuration properties for Logback-access.
     */
    @Test
    public void logbackAccessProperties() {
        assertThat(logbackAccessProperties).isNotPresent();
    }

    /**
     * Tests the Logback-access installer for Tomcat.
     */
    @Test
    public void tomcatLogbackAccessInstaller() {
        assertThat(tomcatLogbackAccessInstaller).isNotPresent();
    }

    /**
     * Tests the Logback-access installer for Jetty.
     */
    @Test
    public void jettyLogbackAccessInstaller() {
        assertThat(jettyLogbackAccessInstaller).isNotPresent();
    }

    /**
     * The base class of context configuration.
     */
    @EnableAutoConfiguration
    public static abstract class AbstractContextConfiguration {
    }

}

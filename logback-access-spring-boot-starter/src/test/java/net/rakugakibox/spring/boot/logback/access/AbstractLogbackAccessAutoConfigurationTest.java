package net.rakugakibox.spring.boot.logback.access;

import java.util.Optional;

import net.rakugakibox.spring.boot.logback.access.jetty.JettyLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.tomcat.TomcatLogbackAccessInstaller;
import net.rakugakibox.spring.boot.logback.access.undertow.UndertowLogbackAccessInstaller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The base class for testing {@link LogbackAccessAutoConfiguration}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractLogbackAccessAutoConfigurationTest {

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
     * The Logback-access installer for Undertow.
     */
    @Autowired
    protected Optional<UndertowLogbackAccessInstaller> undertowLogbackAccessInstaller;

    /**
     * The filter that saves Spring Security attributes for Logback-access.
     */
    @Autowired
    @Qualifier("logbackAccessSecurityAttributesSaveFilter")
    protected Optional<FilterRegistrationBean> logbackAccessSecurityAttributesSaveFilter;

    /**
     * Tests the configuration properties for Logback-access.
     */
    @Test
    public void logbackAccessProperties() {
        assertThat(logbackAccessProperties).isPresent();
    }

    /**
     * Tests the Logback-access installer for Tomcat.
     */
    @Test
    public abstract void tomcatLogbackAccessInstaller();

    /**
     * Tests the Logback-access installer for Jetty.
     */
    @Test
    public abstract void jettyLogbackAccessInstaller();

    /**
     * Tests the Logback-access installer for Undertow.
     */
    @Test
    public abstract void undertowLogbackAccessInstaller();

    /**
     * Tests the filter that saves Spring Security attributes for Logback-access.
     */
    @Test
    public void logbackAccessSecurityAttributesSaveFilter() {
        assertThat(logbackAccessSecurityAttributesSaveFilter).isPresent();
    }

    /**
     * The base class of context configuration.
     */
    @EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
    public static abstract class AbstractContextConfiguration {
    }

}

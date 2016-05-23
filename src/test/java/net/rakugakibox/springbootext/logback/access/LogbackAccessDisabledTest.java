package net.rakugakibox.springbootext.logback.access;

import net.rakugakibox.springbootext.logback.access.jetty.LogbackAccessJettyCustomizer;
import net.rakugakibox.springbootext.logback.access.tomcat.LogbackAccessTomcatCustomizer;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The test in the case that set to disabled.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(value = "logback.access.enabled=false", randomPort = true)
public class LogbackAccessDisabledTest {

    /**
     * The auto-configuration.
     */
    @Autowired(required = false)
    private LogbackAccessAutoConfiguration autoConfiguration;

    /**
     * The configuration properties.
     */
    @Autowired(required = false)
    private LogbackAccessProperties properties;

    /**
     * The configurator.
     */
    @Autowired(required = false)
    private LogbackAccessConfigurator configurator;

    /**
     * The Tomcat customizer.
     */
    @Autowired(required = false)
    private LogbackAccessTomcatCustomizer tomcatCustomizer;

    /**
     * The Jetty customizer.
     */
    @Autowired(required = false)
    private LogbackAccessJettyCustomizer jettyCustomizer;

    /**
     * Tests that the components is not created.
     */
    @Test
    public void testComponents() {
        assertThat(autoConfiguration).isNull();
        assertThat(properties).isNull();
        assertThat(configurator).isNull();
        assertThat(tomcatCustomizer).isNull();
        assertThat(jettyCustomizer).isNull();
    }

    /**
     * The context configuration.
     */
    @Configuration
    @EnableAutoConfiguration
    public static class TestContextConfiguration {
    }

}

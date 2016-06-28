package net.rakugakibox.springbootext.logback.access.jetty;

import net.rakugakibox.springbootext.logback.access.AbstractTestConfigurationAutoDetectionTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test that auto-detect test configuration on Jetty.
 */
@SpringApplicationConfiguration
public class JettyTestConfigurationAutoDetectionTest extends AbstractTestConfigurationAutoDetectionTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class TestContextConfiguration {
    }

}

package net.rakugakibox.springbootext.logback.access.jetty;

import net.rakugakibox.springbootext.logback.access.AbstractGeneralTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test of general cases on Jetty.
 */
@SpringApplicationConfiguration
public class JettyGeneralTest extends AbstractGeneralTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class TestContextConfiguration {
    }

}

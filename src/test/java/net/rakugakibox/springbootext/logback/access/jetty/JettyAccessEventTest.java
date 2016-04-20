package net.rakugakibox.springbootext.logback.access.jetty;

import net.rakugakibox.springbootext.logback.access.AbstractAccessEventTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test of the Jetty access event.
 */
@SpringApplicationConfiguration
public class JettyAccessEventTest extends AbstractAccessEventTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class TestContextConfiguration {
    }

}

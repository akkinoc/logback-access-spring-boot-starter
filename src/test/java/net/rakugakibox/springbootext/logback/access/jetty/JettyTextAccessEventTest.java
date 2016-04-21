package net.rakugakibox.springbootext.logback.access.jetty;

import net.rakugakibox.springbootext.logback.access.AbstractTextAccessEventTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test of text resource access event on Jetty.
 */
@SpringApplicationConfiguration
public class JettyTextAccessEventTest extends AbstractTextAccessEventTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class TestContextConfiguration {
    }

}

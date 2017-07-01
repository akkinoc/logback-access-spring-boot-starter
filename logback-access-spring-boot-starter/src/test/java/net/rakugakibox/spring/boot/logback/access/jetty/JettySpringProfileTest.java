package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractSpringProfileTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test to use {@code <springProfile>} tag for Jetty.
 */
public class JettySpringProfileTest extends AbstractSpringProfileTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

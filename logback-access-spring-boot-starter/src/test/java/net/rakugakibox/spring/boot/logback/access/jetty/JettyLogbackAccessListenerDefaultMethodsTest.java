package net.rakugakibox.spring.boot.logback.access.jetty;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessListenerDefaultMethodsTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessListener;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test of the {@link LogbackAccessListener}'s default methods for Jetty.
 */
public class JettyLogbackAccessListenerDefaultMethodsTest extends AbstractLogbackAccessListenerDefaultMethodsTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

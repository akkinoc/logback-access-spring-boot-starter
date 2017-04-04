package net.rakugakibox.spring.boot.logback.access.undertow;

import net.rakugakibox.spring.boot.logback.access.AbstractLogbackAccessListenerDefaultMethodsTest;
import net.rakugakibox.spring.boot.logback.access.LogbackAccessListener;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test of the {@link LogbackAccessListener}'s default methods for Undertow.
 */
public class UndertowLogbackAccessListenerDefaultMethodsTest extends AbstractLogbackAccessListenerDefaultMethodsTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedUndertow.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

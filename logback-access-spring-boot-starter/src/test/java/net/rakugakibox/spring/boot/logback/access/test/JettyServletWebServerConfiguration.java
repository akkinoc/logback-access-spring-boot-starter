package net.rakugakibox.spring.boot.logback.access.test;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for using {@link JettyServletWebServerFactory}.
 */
@Configuration
public class JettyServletWebServerConfiguration {

    /**
     * Creates a {@link JettyServletWebServerFactory}.
     *
     * @return a {@link JettyServletWebServerFactory}.
     */
    @Bean
    public JettyServletWebServerFactory jettyServletWebServerFactory() {
        return new JettyServletWebServerFactory();
    }

}

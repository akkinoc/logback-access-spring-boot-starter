package net.rakugakibox.spring.boot.logback.access.test;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for using {@link UndertowServletWebServerFactory}.
 */
@Configuration
public class UndertowServletWebServerConfiguration {

    /**
     * Creates a {@link UndertowServletWebServerFactory}.
     *
     * @return a {@link UndertowServletWebServerFactory}.
     */
    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        return new UndertowServletWebServerFactory();
    }

}

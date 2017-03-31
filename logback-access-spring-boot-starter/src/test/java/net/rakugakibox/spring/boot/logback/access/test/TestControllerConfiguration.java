package net.rakugakibox.spring.boot.logback.access.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for using {@link TestController}.
 */
@Configuration
public class TestControllerConfiguration {

    /**
     * Creates a {@link TestController}.
     *
     * @return a {@link TestController}.
     */
    @Bean
    public TestController testController() {
        return new TestController();
    }

}

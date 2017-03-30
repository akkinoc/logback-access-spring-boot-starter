package net.rakugakibox.spring.boot.logback.access.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration to use the test controller.
 */
@Configuration
public class TestControllerConfiguration {

    /**
     * Creates a test controller.
     *
     * @return a test controller.
     */
    @Bean
    public TestController testController() {
        return new TestController();
    }

}

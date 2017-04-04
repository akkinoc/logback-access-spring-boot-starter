package net.rakugakibox.spring.boot.logback.access.sample.undertow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The sample application of "logback-access-spring-boot-starter" for Undertow.
 */
@SpringBootApplication
public class Application {

    /**
     * The entry point of application.
     *
     * @param args unused.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

package net.rakugakibox.springbootext.logback.access.sample.jetty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample Application for Jetty.
 */
@SpringBootApplication
public class Application {

    /**
     * Application entry point.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

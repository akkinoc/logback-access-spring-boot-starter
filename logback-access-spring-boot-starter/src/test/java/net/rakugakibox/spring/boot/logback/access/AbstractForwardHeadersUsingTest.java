package net.rakugakibox.spring.boot.logback.access;

import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueuesRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The base class for testing to use {@code X-Forwarded-*} headers.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        value = {
                "server.useForwardHeaders=true",
                "logback.access.config=classpath:logback-access-test.in-memory-default-queue.xml",
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractForwardHeadersUsingTest {

    /**
     * The REST template.
     */
    @Autowired
    protected TestRestTemplate rest;

    /**
     * Creates a test rule.
     *
     * @return a test rule.
     */
    @Rule
    public TestRule rule() {
        return new InMemoryLogbackAccessEventQueuesRule();
    }

    /**
     * Tests a Logback-access event.
     */
    @Test
    public abstract void logbackAccessEvent();

    /**
     * The base class of context configuration.
     */
    @EnableAutoConfiguration
    public static abstract class AbstractContextConfiguration {

        /**
         * Creates a controller.
         *
         * @return a controller.
         */
        @Bean
        public Controller controller() {
            return new Controller();
        }

    }

    /**
     * The controller.
     */
    @RestController
    @RequestMapping("/test")
    public static class Controller {

        /**
         * Gets the text.
         *
         * @return the text.
         */
        @GetMapping(value = "/text", produces = MediaType.TEXT_PLAIN_VALUE)
        public String getText() {
            return "TEST-TEXT";
        }

    }

}

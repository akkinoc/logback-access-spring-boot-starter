package net.rakugakibox.spring.boot.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.spring.boot.logback.access.test.ClassPathRule;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueues;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueuesRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The base class for testing to auto detect test configuration file.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTestConfigurationFileAutoDetectingTest {

    /**
     * The REST template.
     */
    @Autowired
    protected TestRestTemplate rest;

    /**
     * Creates a class test rule.
     *
     * @return a class test rule.
     */
    @ClassRule
    public static TestRule classRule() {
        return new ClassPathRule(AbstractTestConfigurationFileAutoDetectingTest.class);
    }

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
    public void logbackAccessEvent() {

        ResponseEntity<String> response = rest.getForEntity("/test/text", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop("logback-access-test.xml");

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).isNotNull();

    }

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

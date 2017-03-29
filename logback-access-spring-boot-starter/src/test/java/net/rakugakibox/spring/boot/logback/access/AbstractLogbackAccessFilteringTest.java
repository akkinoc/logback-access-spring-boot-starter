package net.rakugakibox.spring.boot.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.spi.FilterReply;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueues;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The base class for testing to filter Logback-access events.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        value = "logback.access.config=classpath:logback-access.request-header-driven-filtered.in-memory-queue.xml",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractLogbackAccessFilteringTest {

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
     * Tests an accepted Logback-access event.
     */
    @Test
    public void acceptedLogbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Filter-Reply", FilterReply.ACCEPT.name())
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).isNotNull();

    }

    /**
     * Tests an neutral Logback-access event.
     */
    @Test
    public void neutralLogbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Filter-Reply", FilterReply.NEUTRAL.name())
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).isNotNull();

    }

    /**
     * Tests a denied Logback-access event.
     */
    @Test
    public void deniedLogbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Filter-Reply", FilterReply.DENY.name())
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);

        assertThat(response).hasStatusCode(HttpStatus.OK);

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

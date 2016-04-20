package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import java.net.URI;
import java.time.LocalDateTime;
import static net.rakugakibox.springbootext.logback.access.test.AccessEventAssert.assertThat;
import net.rakugakibox.springbootext.logback.access.test.SingletonQueueAppender;
import net.rakugakibox.springbootext.logback.access.test.TextRestController;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * The base class to test of access event.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(
        value = "logback.access.config=classpath:logback-access-test.singleton-queue.xml",
        randomPort = true
)
public abstract class AbstractAccessEventTest {

    /**
     * The REST template.
     */
    private final RestTemplate restTemplate = new TestRestTemplate();

    /**
     * The base URL.
     */
    @Value("http://localhost:${local.server.port}/")
    private URI baseUrl;

    /**
     * Sets up resources.
     */
    @Before
    public void setup() {
        SingletonQueueAppender.clear();
    }

    /**
     * Tests the case to POST the text.
     */
    @Test
    public void postText() {

        restTemplate.put(baseUrl.resolve(TextRestController.PATH), "POST-TEXT");
        SingletonQueueAppender.pop();

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.postForObject(baseUrl.resolve(TextRestController.PATH), "-TEXT", String.class);
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasHttpProtocol()
                .hasMethod(HttpMethod.POST)
                .hasContentLength("POST-TEXT-TEXT".getBytes().length);

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to GET the text.
     */
    @Test
    public void getText() {

        restTemplate.put(baseUrl.resolve(TextRestController.PATH), "GET-TEXT");
        SingletonQueueAppender.pop();

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.getForObject(baseUrl.resolve(TextRestController.PATH), String.class);
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasHttpProtocol()
                .hasMethod(HttpMethod.GET)
                .hasContentLength("GET-TEXT".getBytes().length);

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to PUT the text.
     */
    @Test
    public void putText() {

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.put(baseUrl.resolve(TextRestController.PATH), "PUT-TEXT");
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasHttpProtocol()
                .hasMethod(HttpMethod.PUT)
                .hasContentLength("PUT-TEXT".getBytes().length);

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to DELETE the text.
     */
    @Test
    public void deleteText() {

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.delete(baseUrl.resolve(TextRestController.PATH));
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasHttpProtocol()
                .hasMethod(HttpMethod.DELETE);
                // TODO: .hasContentLength(0);

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * The context configuration.
     */
    @Configuration
    @EnableAutoConfiguration
    public static class TestContextConfiguration {

        /**
         * Creates a REST controller of text resource.
         *
         * @return a REST controller of text resource.
         */
        @Bean
        public TextRestController textRestController() {
            return new TextRestController();
        }

    }

}

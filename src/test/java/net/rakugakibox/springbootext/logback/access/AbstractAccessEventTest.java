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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
     * The base URL.
     */
    @Value("http://localhost:${local.server.port}/")
    private URI baseUrl;

    /**
     * The REST template.
     */
    private RestTemplate rest;

    /**
     * Sets up resources.
     */
    @Before
    public void setup() {

        // Initializes the REST template.
        rest = new TestRestTemplate();

        // Initializes the event queue.
        SingletonQueueAppender.clear();

        // Initializes the text resource.
        rest.put(url(TextRestController.PATH).build().toUri(), "TEXT");
        SingletonQueueAppender.pop();

    }

    /**
     * Tests the case to POST the text.
     */
    @Test
    public void postText() {

        LocalDateTime startTime = LocalDateTime.now();
        String response = rest.postForObject(
                url(TextRestController.PATH).build().toUri(), "-POST-TEXT", String.class);
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response).isEqualTo("TEXT-POST-TEXT");
        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.POST)
                .hasRequestUri(TextRestController.PATH)
                .hasRequestUrl(HttpMethod.POST, TextRestController.PATH, "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.CREATED)
                .hasContentLength("TEXT-POST-TEXT".getBytes().length);
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to GET the text.
     */
    @Test
    public void getText() {

        LocalDateTime startTime = LocalDateTime.now();
        String response = rest.getForObject(
                url(TextRestController.PATH).queryParam("addition", "-GET-TEXT").build().toUri(), String.class);
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response).isEqualTo("TEXT-GET-TEXT");
        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.GET)
                .hasRequestUri(TextRestController.PATH)
                .hasRequestUrl(HttpMethod.GET, TextRestController.PATH + "?addition=-GET-TEXT", "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.OK)
                .hasContentLength("TEXT-GET-TEXT".getBytes().length);
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to PUT the text.
     */
    @Test
    public void putText() {

        LocalDateTime startTime = LocalDateTime.now();
        rest.put(url(TextRestController.PATH).build().toUri(), "PUT-TEXT");
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.PUT)
                .hasRequestUri(TextRestController.PATH)
                .hasRequestUrl(HttpMethod.PUT, TextRestController.PATH, "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.NO_CONTENT);
                // TODO: .hasContentLength(0);
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to DELETE the text.
     */
    @Test
    public void deleteText() {

        LocalDateTime startTime = LocalDateTime.now();
        rest.delete(url(TextRestController.PATH).build().toUri());
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.DELETE)
                .hasRequestUri(TextRestController.PATH)
                .hasRequestUrl(HttpMethod.DELETE, TextRestController.PATH, "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.NO_CONTENT);
                // TODO: .hasContentLength(0);
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Starts building the URL.
     *
     * @param The path of URL.
     * @return a URI components builder.
     */
    private UriComponentsBuilder url(String path) {
        return UriComponentsBuilder.fromUri(baseUrl).path(path);
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

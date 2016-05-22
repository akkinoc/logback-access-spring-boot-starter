package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import java.time.LocalDateTime;
import static net.rakugakibox.springbootext.logback.access.test.AccessEventAssert.assertThat;
import net.rakugakibox.springbootext.logback.access.test.SingletonQueueAppender;
import net.rakugakibox.springbootext.logback.access.test.TextController;
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
 * The base class to test of text resource access event.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(
        value = "logback.access.config=classpath:logback-access-test.singleton-queue.xml",
        randomPort = true
)
public abstract class AbstractTextAccessEventTest {

    /**
     * The server port.
     */
    @Value("${local.server.port}")
    private int port;

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
        rest.put(url(TextController.PATH).build().toUri(), "TEXT");
        SingletonQueueAppender.pop();

    }

    /**
     * Tests the case to POST.
     */
    @Test
    public void post() {

        LocalDateTime startTime = LocalDateTime.now();
        String response = rest.postForObject(
                url(TextController.PATH).build().toUri(),
                "-POST",
                String.class
        );
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response).isEqualTo("TEXT-POST");
        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.POST)
                .hasRequestUri(TextController.PATH)
                .doesNotHaveQueryString()
                .hasRequestUrl(HttpMethod.POST, TextController.PATH, "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.CREATED)
                .hasContentLength(response.getBytes().length)
                .hasThreadName();
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to GET.
     */
    @Test
    public void get() {

        LocalDateTime startTime = LocalDateTime.now();
        String response = rest.getForObject(
                url(TextController.PATH)
                        .queryParam("addition", "-GET")
                        .build()
                        .toUri(),
                String.class
        );
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response).isEqualTo("TEXT-GET");
        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.GET)
                .hasRequestUri(TextController.PATH)
                .hasQueryString("addition=-GET")
                .hasRequestUrl(HttpMethod.GET, TextController.PATH, "addition=-GET", "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.OK)
                .hasContentLength(response.getBytes().length)
                .hasThreadName();
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to PUT.
     */
    @Test
    public void put() {

        LocalDateTime startTime = LocalDateTime.now();
        rest.put(
                url(TextController.PATH).build().toUri(),
                "PUT"
        );
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.PUT)
                .hasRequestUri(TextController.PATH)
                .doesNotHaveQueryString()
                .hasRequestUrl(HttpMethod.PUT, TextController.PATH, "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.NO_CONTENT)
                .hasContentLength(0)
                .hasThreadName();
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to DELETE.
     */
    @Test
    public void delete() {

        LocalDateTime startTime = LocalDateTime.now();
        rest.delete(url(TextController.PATH).build().toUri());
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.DELETE)
                .hasRequestUri(TextController.PATH)
                .doesNotHaveQueryString()
                .hasRequestUrl(HttpMethod.DELETE, TextController.PATH, "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.NO_CONTENT)
                .hasContentLength(0)
                .hasThreadName();
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Starts building the URL.
     *
     * @param The path of URL.
     * @return a URI components builder.
     */
    private UriComponentsBuilder url(String path) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path(path);
    }

    /**
     * The context configuration.
     */
    @Configuration
    @EnableAutoConfiguration
    public static class TestContextConfiguration {

        /**
         * Creates a controller of text resource.
         *
         * @return a controller of text resource.
         */
        @Bean
        public TextController textController() {
            return new TextController();
        }

    }

}

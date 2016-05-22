package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static net.rakugakibox.springbootext.logback.access.test.AccessEventAssert.assertThat;
import net.rakugakibox.springbootext.logback.access.test.JsonController;
import net.rakugakibox.springbootext.logback.access.test.SingletonQueueAppender;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
 * The base class to test of JSON resource access event.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(
        value = "logback.access.config=classpath:logback-access-test.singleton-queue.xml",
        randomPort = true
)
public abstract class AbstractJsonAccessEventTest {

    /**
     * The server port.
     */
    @Value("${local.server.port}")
    private int port;

    /**
     * The JSON mapper.
     */
    @Autowired
    private ObjectMapper mapper;

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

        // Initializes the JSON resource.
        rest.put(url(JsonController.PATH).build().toUri(), map("JSON-KEY", "JSON-VALUE"));
        SingletonQueueAppender.pop();

    }

    /**
     * Tests the case to POST.
     *
     * @throws IOException if an I/O exception occurs.
     */
    @Test
    public void post() throws IOException {

        LocalDateTime startTime = LocalDateTime.now();
        @SuppressWarnings("unchecked") Map<String, String> response = rest.postForObject(
                url(JsonController.PATH).build().toUri(),
                map("POST-KEY", "POST-VALUE"),
                Map.class
        );
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response)
                .hasSize(2)
                .containsEntry("JSON-KEY", "JSON-VALUE")
                .containsEntry("POST-KEY", "POST-VALUE");
        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasServerName("localhost")
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.POST)
                .hasRequestUri(JsonController.PATH)
                .doesNotHaveQueryString()
                .hasRequestUrl(HttpMethod.POST, JsonController.PATH, "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.CREATED)
                .hasContentLength(mapper.writeValueAsBytes(response).length)
                .hasThreadName();
        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to GET.
     *
     * @throws IOException if an I/O exception occurs.
     */
    @Test
    public void get() throws IOException {

        LocalDateTime startTime = LocalDateTime.now();
        @SuppressWarnings("unchecked") Map<String, String> response = rest.getForObject(
                url(JsonController.PATH)
                        .queryParam("additionalKey", "GET-KEY")
                        .queryParam("additionalValue", "GET-VALUE")
                        .build()
                        .toUri(),
                Map.class
        );
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response)
                .hasSize(2)
                .containsEntry("JSON-KEY", "JSON-VALUE")
                .containsEntry("GET-KEY", "GET-VALUE");
        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasServerName("localhost")
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.GET)
                .hasRequestUri(JsonController.PATH)
                .hasQueryString("additionalKey=GET-KEY&additionalValue=GET-VALUE")
                .hasRequestUrl(
                        HttpMethod.GET,
                        JsonController.PATH,
                        "additionalKey=GET-KEY&additionalValue=GET-VALUE",
                        "HTTP/1.1"
                )
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.OK)
                .hasContentLength(mapper.writeValueAsBytes(response).length)
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
                url(JsonController.PATH).build().toUri(),
                map("PUT-KEY", "PUT-VALUE")
        );
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasServerName("localhost")
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.PUT)
                .hasRequestUri(JsonController.PATH)
                .doesNotHaveQueryString()
                .hasRequestUrl(HttpMethod.PUT, JsonController.PATH, "HTTP/1.1")
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
        rest.delete(url(JsonController.PATH).build().toUri());
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasServerName("localhost")
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.DELETE)
                .hasRequestUri(JsonController.PATH)
                .doesNotHaveQueryString()
                .hasRequestUrl(HttpMethod.DELETE, JsonController.PATH, "HTTP/1.1")
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
     * Creates a map.
     *
     * @param keyAndValues the pairs of key and value.
     * @return a map.
     */
    private Map<String, String> map(String... keyAndValues) {
        Map<String, String> map = new HashMap<>();
        for (Iterator<String> iterator = asList(keyAndValues).iterator(); iterator.hasNext(); ) {
            map.put(iterator.next(), iterator.next());
        }
        return map;
    }

    /**
     * The context configuration.
     */
    @Configuration
    @EnableAutoConfiguration
    public static class TestContextConfiguration {

        /**
         * Creates a controller of JSON resource.
         *
         * @return a controller of JSON resource.
         */
        @Bean
        public JsonController jsonController() {
            return new JsonController();
        }

    }

}

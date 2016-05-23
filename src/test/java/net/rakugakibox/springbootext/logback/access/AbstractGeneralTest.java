package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static net.rakugakibox.springbootext.logback.access.test.AccessEventAssert.assertThat;
import net.rakugakibox.springbootext.logback.access.test.SingletonQueueAppender;
import net.rakugakibox.springbootext.logback.access.test.SingletonQueueAppenderRule;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The base class to test general cases.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(
        value = "logback.access.config=classpath:logback-access-test.singleton-queue.xml",
        randomPort = true
)
public abstract class AbstractGeneralTest {

    /**
     * The server port.
     */
    @Value("${local.server.port}")
    private int port;

    /**
     * The REST template.
     */
    @Autowired
    private RestTemplate rest;

    /**
     * Creates a test rule.
     *
     * @return a test rule.
     */
    @Rule
    public TestRule rule() {
        return new SingletonQueueAppenderRule();
    }

    /**
     * Tests the basic attributes.
     */
    @Test
    public void testBasicAttributes() {

        RequestEntity<Void> request = RequestEntity
                .get(url("/text").build().toUri())
                .build();

        LocalDateTime startTime = LocalDateTime.now();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response.getBody())
                .isEqualTo("text");

        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasServerName("localhost")
                .hasLocalPort(port)
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.GET)
                .hasRequestUri("/text")
                .hasRequestUrl(HttpMethod.GET, "/text", "HTTP/1.1")
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasRemoteUser(null)
                .hasStatusCode(HttpStatus.OK)
                .hasContentLength(response.getBody().getBytes().length)
                .hasElapsedTime(startTime, endTime)
                .hasElapsedSeconds(startTime, endTime)
                .hasThreadName();

    }

    /**
     * Tests the query string.
     */
    @Test
    public void testQueryString() {

        RequestEntity<Void> request = RequestEntity
                .get(url("/text").query("query").build().toUri())
                .build();

        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = SingletonQueueAppender.pop();

        assertThat(response.getBody())
                .isEqualTo("text");

        assertThat(event)
                .hasQueryString("?query")
                .hasRequestUrl(HttpMethod.GET, "/text?query", "HTTP/1.1");

    }

    /**
     * Tests the request parameter.
     */
    @Test
    public void testRequestParameter() {

        RequestEntity<Void> request = RequestEntity
                .get(url("/text").queryParam("param", "value1", "value2").build().toUri())
                .build();

        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = SingletonQueueAppender.pop();

        assertThat(response.getBody())
                .isEqualTo("text");

        assertThat(event)
                .hasQueryString("?param=value1&param=value2")
                .hasRequestUrl(HttpMethod.GET, "/text?param=value1&param=value2", "HTTP/1.1")
                .hasRequestParameter("param", "value1", "value2");

    }

    /**
     * Tests the content length when "Content-Length" response header is contained.
     */
    @Test
    public void testContentLengthWhenHeaderIsContained() {

        RequestEntity<Void> request = RequestEntity
                .get(url("/text").build().toUri())
                .build();

        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = SingletonQueueAppender.pop();

        assertThat(response.getHeaders())
                .containsKey(HttpHeaders.CONTENT_LENGTH);
        assertThat(response.getBody())
                .isEqualTo("text");

        assertThat(event)
                .hasContentLength(response.getBody().getBytes().length);

    }

    /**
     * Tests the content length when "Content-Length" response header is not contained.
     */
    @Test
    public void testContentLengthWhenHeaderIsNotContained() {

        RequestEntity<Void> request = RequestEntity
                .get(url("/json").build().toUri())
                .build();

        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = SingletonQueueAppender.pop();

        assertThat(response.getHeaders())
                .doesNotContainKey(HttpHeaders.CONTENT_LENGTH);
        assertThat(response.getBody())
                .containsSequence("json-key", ":", "json-value");

        assertThat(event)
                .hasContentLength(response.getBody().getBytes().length);

    }

    /**
     * Starts building the URL.
     *
     * @param path the path of URL.
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
         * Creates a REST template.
         *
         * @return a REST template.
         */
        @Bean
        public RestTemplate testRestTemplate() {
            return new TestRestTemplate();
        }

        /**
         * Creates a controller.
         *
         * @return a controller.
         */
        @Bean
        public TestController testController() {
            return new TestController();
        }

    }

    /**
     * The controller.
     */
    @RestController
    public static class TestController {

        /**
         * Returns the text.
         *
         * @return the text.
         */
        @RequestMapping(path = "/text", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
        public String getText() {
            return "text";
        }

        /**
         * Returns the JSON.
         *
         * @return the JSON.
         */
        @RequestMapping(path = "/json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public Map<String, Object> getJson() {
            Map<String, Object> map = new HashMap<>();
            map.put("json-key", "json-value");
            return map;
        }

    }

}

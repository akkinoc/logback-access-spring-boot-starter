package net.rakugakibox.spring.boot.logback.access;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.servlet.http.HttpServletResponse;
import static java.util.Collections.singletonMap;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueues;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueuesRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
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
 * The base class for testing Logback-access events.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        value = "logback.access.config=classpath:logback-access.in-memory-queue.xml",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractLogbackAccessEventsTest {

    /**
     * The REST template.
     */
    @Autowired
    protected TestRestTemplate rest;

    /**
     * The server port.
     */
    @LocalServerPort
    protected int port;

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

        LocalDateTime startTime = LocalDateTime.now();
        ResponseEntity<String> response = rest.getForEntity("/test/text", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(response)
                .hasStatusCode(HttpStatus.OK)
                .hasContentLengthHeader("TEST-TEXT".getBytes(StandardCharsets.UTF_8).length)
                .hasBody("TEST-TEXT");
        assertThat(event)
                .hasTimestamp(startTime, endTime)
                .hasServerName("localhost")
                .hasLocalPort(port)
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .doesNotHaveRemoteUser()
                .hasProtocol("HTTP/1.1")
                .hasMethod(HttpMethod.GET)
                .hasRequestUri("/test/text")
                .hasQueryString("")
                .hasRequestUrl(HttpMethod.GET, "/test/text", "HTTP/1.1")
                .doesNotHaveRequestHeaderName("X-Test-Header")
                .doesNotHaveRequestHeader("X-Test-Header")
                .doesNotHaveRequestHeaderInMap("X-Test-Header")
                .doesNotHaveRequestParameter("test-parameter")
                .doesNotHaveRequestParameterInMap("test-parameter")
                .hasStatusCode(HttpStatus.OK)
                .hasContentLength("TEST-TEXT".getBytes(StandardCharsets.UTF_8).length)
                .doesNotHaveResponseHeaderName("X-Test-Header")
                .doesNotHaveResponseHeader("X-Test-Header")
                .doesNotHaveResponseHeaderInMap("X-Test-Header")
                .hasElapsedTime(startTime, endTime)
                .hasElapsedSeconds(startTime, endTime)
                .hasThreadName();

    }

    /**
     * Tests a Logback-access event with a query string.
     */
    @Test
    public void logbackAccessEventWithQueryString() {

        ResponseEntity<String> response = rest.getForEntity("/test/text?query", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event)
                .hasRequestUri("/test/text")
                .hasQueryString("?query")
                .hasRequestUrl(HttpMethod.GET, "/test/text?query", "HTTP/1.1");

    }

    /**
     * Tests a Logback-access event with a request header.
     */
    @Test
    public void logbackAccessEventWithRequestHeader() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Test-Header", "TEST-HEADER")
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event)
                .hasRequestHeaderName("X-Test-Header")
                .hasRequestHeader("X-Test-Header", "TEST-HEADER")
                .hasRequestHeaderInMap("X-Test-Header", "TEST-HEADER");

    }

    /**
     * Tests a Logback-access event with a request parameter.
     */
    @Test
    public void logbackAccessEventWithRequestParameter() {

        ResponseEntity<String> response = rest.getForEntity("/test/text?test-parameter=TEST-PARAMETER", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event)
                .hasRequestParameter("test-parameter", "TEST-PARAMETER")
                .hasRequestParameterInMap("test-parameter", "TEST-PARAMETER");

    }

    /**
     * Tests a Logback-access event with a response header.
     */
    @Test
    public void logbackAccessEventWithResponseHeader() {

        ResponseEntity<String> response = rest.getForEntity("/test/text-with-header", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event)
                .hasResponseHeaderName("X-Test-Header")
                .hasResponseHeader("X-Test-Header", "TEST-HEADER")
                .hasResponseHeaderInMap("X-Test-Header", "TEST-HEADER");

    }

    /**
     * Tests a Logback-access event asynchronously.
     */
    @Test
    public void logbackAccessEventAsynchronously() {

        ResponseEntity<String> response = rest.getForEntity("/test/text-asynchronously", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).hasThreadName();

    }

    /**
     * Tests a Logback-access event without a response header of content length.
     */
    @Test
    public void logbackAccessEventWithoutContentLengthResponseHeader() {

        ResponseEntity<String> response = rest.getForEntity("/test/json", String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response)
                .hasStatusCode(HttpStatus.OK)
                .doesNotHaveContentLengthHeader()
                .hasBody("{\"TEST-KEY\":\"TEST-VALUE\"}");
        assertThat(event)
                .hasContentLength("{\"TEST-KEY\":\"TEST-VALUE\"}".getBytes(StandardCharsets.UTF_8).length);

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

        /**
         * Gets the text with header.
         *
         * @param response the HTTP response to give a header.
         * @return the text.
         */
        @GetMapping(value = "/text-with-header", produces = MediaType.TEXT_PLAIN_VALUE)
        public String getTextWithHeader(HttpServletResponse response) {
            response.addHeader("X-Test-Header", "TEST-HEADER");
            return getText();
        }

        /**
         * Gets the text asynchronously.
         *
         * @return the callback to return the text.
         */
        @GetMapping(value = "/text-asynchronously", produces = MediaType.TEXT_PLAIN_VALUE)
        public Callable<String> getTextAsynchronously() {
            return this::getText;
        }

        /**
         * Gets the JSON.
         *
         * @return the map to be JSON.
         */
        @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
        public Map<?, ?> getJson() {
            return singletonMap("TEST-KEY", "TEST-VALUE");
        }

    }

}

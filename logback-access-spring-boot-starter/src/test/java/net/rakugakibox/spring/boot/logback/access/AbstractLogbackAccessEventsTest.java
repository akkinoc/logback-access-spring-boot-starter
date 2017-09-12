package net.rakugakibox.spring.boot.logback.access;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingAppender;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingAppenderRule;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingListener;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingListenerConfiguration;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingListenerRule;
import net.rakugakibox.spring.boot.logback.access.test.TestControllerConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The base class for testing Logback-access events.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        value = "logback.access.config=classpath:logback-access.queue.xml",
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
        return RuleChain
                .outerRule(new LogbackAccessEventQueuingAppenderRule())
                .around(new LogbackAccessEventQueuingListenerRule());
    }

    /**
     * Tests a Logback-access event.
     */
    @Test
    public void logbackAccessEvent() {

        LocalDateTime startTime = LocalDateTime.now();
        ResponseEntity<String> response = rest.getForEntity("/test/text", String.class);
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LocalDateTime endTime = LocalDateTime.now();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

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
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

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
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

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
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

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
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

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
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).hasThreadName();

    }

    /**
     * Tests a Logback-access event to get empty text.
     */
    @Test
    public void logbackAccessEventToGetEmptyText() {

        ResponseEntity<String> response = rest.getForEntity("/test/empty-text", String.class);
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

        assertThat(response)
                .hasStatusCode(HttpStatus.OK)
                .hasContentLengthHeader(0);
        assertThat(event).hasContentLength(0);

    }

    /**
     * Tests a Logback-access event without a response header of content length.
     */
    @Test
    public void logbackAccessEventWithoutContentLengthResponseHeader() {

        ResponseEntity<String> response = rest.getForEntity("/test/json", String.class);
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

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
    @Import({LogbackAccessEventQueuingListenerConfiguration.class, TestControllerConfiguration.class})
    public static abstract class AbstractContextConfiguration {
    }

}

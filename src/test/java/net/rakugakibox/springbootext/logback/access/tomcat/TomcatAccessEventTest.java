package net.rakugakibox.springbootext.logback.access.tomcat;

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
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * The test of the Tomcat access event.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(
        value = "logback.access.config=classpath:logback-access-test.singleton-queue.xml",
        randomPort = true
)
public class TomcatAccessEventTest {

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
     * Tests the case to post the text.
     */
    @Test
    public void postText() {

        restTemplate.put(baseUrl.resolve(TextRestController.PATH), "post");
        SingletonQueueAppender.pop();

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.postForObject(baseUrl.resolve(TextRestController.PATH), "post", String.class);
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestampOfBetween(startTime, endTime)
                .hasContentLength("postpost".getBytes().length);

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to get the text.
     */
    @Test
    public void getText() {

        restTemplate.put(baseUrl.resolve(TextRestController.PATH), "get");
        SingletonQueueAppender.pop();

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.getForObject(baseUrl.resolve(TextRestController.PATH), String.class);
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestampOfBetween(startTime, endTime)
                .hasContentLength("get".getBytes().length);

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to put the text.
     */
    @Test
    public void putText() {

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.put(baseUrl.resolve(TextRestController.PATH), "put");
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestampOfBetween(startTime, endTime)
                .hasContentLength("put".getBytes().length);

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * Tests the case to delete the text.
     */
    @Test
    public void deleteText() {

        LocalDateTime startTime = LocalDateTime.now();
        restTemplate.delete(baseUrl.resolve(TextRestController.PATH));
        IAccessEvent event = SingletonQueueAppender.pop();
        LocalDateTime endTime = LocalDateTime.now();

        assertThat(event)
                .hasTimestampOfBetween(startTime, endTime)
                .doesNotHaveContentLength();

        assertThat(SingletonQueueAppender.isEmpty()).isTrue();

    }

    /**
     * The context configuration.
     */
    @Configuration
    @EnableAutoConfiguration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class)
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

package net.rakugakibox.springbootext.logback.access.jetty;

import ch.qos.logback.access.spi.IAccessEvent;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
 * The test using {@code X-Forwarded-*} headers on Jetty.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(
        value = {
            "logback.access.config=classpath:logback-access-test.singleton-queue.xml",
            "server.useForwardHeaders=true",
        },
        randomPort = true
)
public class JettyForwardHeadersUsingTest {

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
     * Tests the affected attributes by forwarded headers.
     */
    @Test
    public void testAffectedAttributesByForwardedHeaders() {

        RequestEntity<Void> request = RequestEntity.get(url("/attributes").build().toUri())
                .header("X-Forwarded-Host", "forwarded-host:5432")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-For", "1.2.3.4")
                .build();

        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent accessEvent = SingletonQueueAppender.pop();

        // The ForwardedRequestCustomizer doesn't actually set the X-Forwarded-Proto as the Protocol,
        // but treats the request as secure if it matches the ForwardedRequestCustomizer#getForwardedProtoHeader().
        // So we check that just to be sure.
        assertThat(response.getBody())
                .containsSequence("secure", ":", "true");

        assertThat(accessEvent)
                .hasServerName("forwarded-host")
                .hasLocalPort(5432)
                .hasRemoteAddr("1.2.3.4")
                .hasRemoteHost("1.2.3.4");

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
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class)
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
         * Gets the attributes.
         *
         * @param request the HTTP request.
         * @return the attributes.
         */
        @RequestMapping(path = "/attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        public Map<String, Object> getAttributes(HttpServletRequest request) {
            Map<String, Object> map = new HashMap<>();
            map.put("secure", request.isSecure());
            return map;
        }

    }

}

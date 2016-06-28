package net.rakugakibox.springbootext.logback.access;

import ch.qos.logback.access.spi.IAccessEvent;
import static net.rakugakibox.springbootext.logback.access.test.AccessEventAssert.assertThat;
import net.rakugakibox.springbootext.logback.access.test.ClassPathRule;
import net.rakugakibox.springbootext.logback.access.test.NamedEventQueues;
import net.rakugakibox.springbootext.logback.access.test.NamedEventQueuesRule;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.ClassRule;
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
 * The base class to test that auto-detect test configuration file.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(randomPort = true)
public abstract class AbstractTestConfigurationAutoDetectionTest {

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
     * Creates a class test rule.
     *
     * @return a class test rule.
     */
    @ClassRule
    public static TestRule classRule() {
        return new ClassPathRule(AbstractTestConfigurationAutoDetectionTest.class);
    }

    /**
     * Creates a test rule.
     *
     * @return a test rule.
     */
    @Rule
    public TestRule rule() {
        return new NamedEventQueuesRule();
    }

    /**
     * Tests that the configuration file is auto-detected.
     */
    @Test
    public void testAutoDetected() {

        RequestEntity<Void> request = RequestEntity
                .get(url("/text").build().toUri())
                .build();

        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = NamedEventQueues.pop("logback-access-test.xml");

        assertThat(response.getBody())
                .isEqualTo("text");

        assertThat(event)
                .isNotNull();

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
         * Gets the text.
         *
         * @return the text.
         */
        @RequestMapping(path = "/text", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
        public String getText() {
            return "text";
        }

    }

}

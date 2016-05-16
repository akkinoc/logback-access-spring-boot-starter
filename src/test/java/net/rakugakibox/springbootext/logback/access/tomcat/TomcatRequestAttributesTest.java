package net.rakugakibox.springbootext.logback.access.tomcat;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.springbootext.logback.access.test.AccessEventAssert;
import net.rakugakibox.springbootext.logback.access.test.SingletonQueueAppender;
import net.rakugakibox.springbootext.logback.access.test.TextController;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test of requestAttributes on Tomcat.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration
@WebIntegrationTest(
        value = {"logback.access.config=classpath:logback-access-test.singleton-queue.xml",
                "server.use-forward-headers=true",
                // The Proto and Port Header must be explicitly set to work.
                "server.tomcat.protocol-header=X-Forwarded-Proto",
                "server.tomcat.port-header=X-Forwarded-Port",
                "server.tomcat.protocol-header-https-value=https"
        },
        randomPort = true)
public class TomcatRequestAttributesTest {

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
     * The context configuration.
     */
    @Configuration
    @EnableAutoConfiguration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class)
    public static class TestContextConfiguration {
        @Bean
        public TextController textController() {
            return new TextController();
        }
    }

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

    @Test
    public void checkRequestAttributes() throws IOException {

        HttpHeaders headers = new HttpHeaders();

        headers.add("X-Forwarded-For", "1.2.3.4");
        headers.add("X-Forwarded-Proto", "https");
        headers.add("X-Forwarded-Port", "5432");

        RequestEntity<String> requestEntity = new RequestEntity<>(headers,
                                                                  HttpMethod.GET,
                                                                  url(TextController.PATH +
                                                                      TextController.SECURE_PATH).build().toUri());

        ResponseEntity<String> response = rest.exchange(requestEntity, String.class);

        IAccessEvent accessEvent = SingletonQueueAppender.pop();

        // The RemoteIpValve doesn't actually set the X-Forwarded-Proto as the Protocol, but
        // treats the request as secure if it matches the server.tomcat.protocol-header-https-value.
        // So we check that just to be sure.
        assertThat(response.getBody()).isEqualTo("true");

        AccessEventAssert.assertThat(accessEvent).hasRemoteAddr("1.2.3.4").hasRemoteHost("1.2.3.4").hasLocalPort(5432);

    }

    /**
     * Starts building the URL.
     *
     * @param path The path of URL.
     * @return a URI components builder.
     */
    private UriComponentsBuilder url(String path) {
        return UriComponentsBuilder.fromUri(baseUrl).path(path);
    }

}

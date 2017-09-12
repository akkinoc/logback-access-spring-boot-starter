package net.rakugakibox.spring.boot.logback.access.tomcat;

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
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The test to disable the request attributes for Tomcat.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        value = {
                "server.useForwardHeaders=true",
                "logback.access.config=classpath:logback-access.queue.xml",
                "logback.access.tomcat.enableRequestAttributes=false",
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TomcatRequestAttributesDisablingTest {

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

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Forwarded-Port", "12345")
                .header("X-Forwarded-For", "1.2.3.4")
                .header("X-Forwarded-Proto", "https")
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();
        LogbackAccessEventQueuingListener.appendedEventQueue.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event)
                .hasServerName("localhost")
                .hasLocalPort(port)
                .hasRemoteAddr("127.0.0.1")
                .hasRemoteHost("127.0.0.1")
                .hasProtocol("HTTP/1.1");

    }

    /**
     * The context configuration.
     */
    @EnableAutoConfiguration
    @Configuration
    @Import({
            ServletWebServerFactoryAutoConfiguration.EmbeddedTomcat.class,
            LogbackAccessEventQueuingListenerConfiguration.class,
            TestControllerConfiguration.class,
    })
    public static class ContextConfiguration {
    }

}

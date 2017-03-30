package net.rakugakibox.spring.boot.logback.access.tomcat;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.spring.boot.logback.access.AbstractForwardHeadersUsingTest;
import net.rakugakibox.spring.boot.logback.access.test.LogbackAccessEventQueuingAppender;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The test to use {@code X-Forwarded-*} headers for Tomcat.
 */
public class TomcatForwardHeadersUsingTest extends AbstractForwardHeadersUsingTest {

    /** {@inheritDoc} */
    @Override
    public void logbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Forwarded-Port", "12345")
                .header("X-Forwarded-For", "1.2.3.4")
                .header("X-Forwarded-Proto", "https")
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = LogbackAccessEventQueuingAppender.appendedEventQueue.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event)
                .hasServerName("localhost")
                .hasLocalPort(12345)
                .hasRemoteAddr("1.2.3.4")
                .hasRemoteHost("1.2.3.4")
                .hasProtocol("HTTP/1.1");

    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

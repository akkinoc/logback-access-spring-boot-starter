package net.rakugakibox.spring.boot.logback.access.tomcat;

import ch.qos.logback.access.spi.IAccessEvent;
import net.rakugakibox.spring.boot.logback.access.AbstractServerPortUnusingTest;
import net.rakugakibox.spring.boot.logback.access.test.InMemoryLogbackAccessEventQueues;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import static net.rakugakibox.spring.boot.logback.access.test.AccessEventAssert.assertThat;
import static net.rakugakibox.spring.boot.logback.access.test.ResponseEntityAssert.assertThat;

/**
 * The test to unuse the server port for Tomcat.
 */
public class TomcatServerPortUnusingTest extends AbstractServerPortUnusingTest {

    /** {@inheritDoc} */
    @Override
    public void logbackAccessEvent() {

        RequestEntity<Void> request = RequestEntity
                .get(rest.getRestTemplate().getUriTemplateHandler().expand("/test/text"))
                .header("X-Forwarded-Port", "12345")
                .build();
        ResponseEntity<String> response = rest.exchange(request, String.class);
        IAccessEvent event = InMemoryLogbackAccessEventQueues.pop();

        assertThat(response).hasStatusCode(HttpStatus.OK);
        assertThat(event).hasLocalPort(port);

    }

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class)
    public static class ContextConfiguration extends AbstractContextConfiguration {
    }

}

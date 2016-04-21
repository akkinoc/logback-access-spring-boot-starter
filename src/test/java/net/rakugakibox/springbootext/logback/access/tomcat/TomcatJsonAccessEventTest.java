package net.rakugakibox.springbootext.logback.access.tomcat;

import net.rakugakibox.springbootext.logback.access.AbstractJsonAccessEventTest;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The test of JSON resource access event on Tomcat.
 */
@SpringApplicationConfiguration
public class TomcatJsonAccessEventTest extends AbstractJsonAccessEventTest {

    /**
     * The context configuration.
     */
    @Configuration
    @Import(EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class)
    public static class TestContextConfiguration {
    }

}
